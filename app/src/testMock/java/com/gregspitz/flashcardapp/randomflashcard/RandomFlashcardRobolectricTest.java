package com.gregspitz.flashcardapp.randomflashcard;

import static junit.framework.Assert.assertEquals;

import static org.junit.Assert.assertNotEquals;

import android.app.Activity;
import android.widget.Button;

import com.gregspitz.flashcardapp.Injection;
import com.gregspitz.flashcardapp.R;
import com.gregspitz.flashcardapp.TestUseCaseScheduler;
import com.gregspitz.flashcardapp.data.model.Flashcard;
import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.data.source.local.FakeFlashcardLocalDataSource;
import com.gregspitz.flashcardapp.data.source.remote.FakeFlashcardRemoteDataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RandomFlashcardRobolectricTest {


    private static Flashcard FLASHCARD =
            new Flashcard("Some random front", "Some random back");

    private static Flashcard FLASHCARD2 =
            new Flashcard("A different front", "A different back");

    private Activity mActivity;

    @Before
    public void setup() {
        FlashcardRepository.destroyInstance();
        Injection.setScheduler(new TestUseCaseScheduler());
        FakeFlashcardRemoteDataSource.getInstance().deleteAllFlashcards();
        FakeFlashcardRemoteDataSource.getInstance().addFlashcards(FLASHCARD);
        FakeFlashcardLocalDataSource.getInstance().deleteAllFlashcards();
        FakeFlashcardLocalDataSource.getInstance().addFlashcards(FLASHCARD);
    }

    @Test
    public void flashcardFront_displayedInUi() {
        mActivity = Robolectric.setupActivity(RandomFlashcardActivity.class);
        FlashcardView flashcardView = mActivity.findViewById(R.id.flashcard_view);
        assertEquals(FLASHCARD.getFront(), flashcardView.getText().toString());
    }

    @Test
    public void clickFlashcard_displaysBackOfFlashcardInUi() {
        mActivity = Robolectric.setupActivity(RandomFlashcardActivity.class);
        FlashcardView flashcardView = mActivity.findViewById(R.id.flashcard_view);
        flashcardView.performClick();
        assertEquals(FLASHCARD.getBack(), flashcardView.getText().toString());
    }

    @Test
    public void nextFlashcardButtonClick_loadsDifferentFlashcard() {
        FakeFlashcardRemoteDataSource.getInstance().addFlashcards(FLASHCARD2);
        FakeFlashcardLocalDataSource.getInstance().addFlashcards(FLASHCARD2);

        mActivity = Robolectric.setupActivity(RandomFlashcardActivity.class);
        FlashcardView flashcardView = mActivity.findViewById(R.id.flashcard_view);
        String firstCardFront = flashcardView.getText().toString();

        Button nextFlashcardButton = mActivity.findViewById(R.id.next_flashcard_button);
        nextFlashcardButton.performClick();

        String secondCardFront = flashcardView.getText().toString();

        assertNotEquals(firstCardFront, secondCardFront);
    }
}
