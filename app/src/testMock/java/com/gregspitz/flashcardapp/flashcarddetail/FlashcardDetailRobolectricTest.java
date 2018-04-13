package com.gregspitz.flashcardapp.flashcarddetail;

import static junit.framework.Assert.assertEquals;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.widget.TextView;

import com.gregspitz.flashcardapp.Injection;
import com.gregspitz.flashcardapp.R;
import com.gregspitz.flashcardapp.TestUseCaseScheduler;
import com.gregspitz.flashcardapp.addeditflashcard.AddEditFlashcardActivity;
import com.gregspitz.flashcardapp.data.model.Flashcard;
import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.data.source.local.FakeFlashcardLocalDataSource;
import com.gregspitz.flashcardapp.data.source.remote.FakeFlashcardRemoteDataSource;
import com.gregspitz.flashcardapp.flashcarddetail.FlashcardDetailActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;

@RunWith(RobolectricTestRunner.class)
public class FlashcardDetailRobolectricTest {

    private static final Flashcard FLASHCARD_1 =
            new Flashcard("0", "Front", "Back");

    private static final Flashcard FLASHCARD_2 =
            new Flashcard("1", "Front", "Back");

    private Activity mActivity;

    @Before
    public void setup() {
        Injection.setScheduler(new TestUseCaseScheduler());
        FlashcardRepository.destroyInstance();
        FakeFlashcardLocalDataSource.getInstance().deleteAllFlashcards();
        FakeFlashcardRemoteDataSource.getInstance().deleteAllFlashcards();
        FakeFlashcardLocalDataSource.getInstance().addFlashcards(FLASHCARD_1, FLASHCARD_2);
        FakeFlashcardRemoteDataSource.getInstance().addFlashcards(FLASHCARD_1, FLASHCARD_2);
    }

    @Test
    public void atStart_showsFlashcard() {
        mActivity = buildWithIntentAndStartActivity();
        TextView flashcardFront = mActivity.findViewById(R.id.flashcard_front);
        TextView flashcardBack = mActivity.findViewById(R.id.flashcard_back);
        assertEquals(FLASHCARD_1.getFront(), flashcardFront.getText().toString());
        assertEquals(FLASHCARD_1.getBack(), flashcardBack.getText().toString());
    }

    @Test
    public void clickEditFlashcardFab_showsEditFlashcardActivity() {
        mActivity = buildWithIntentAndStartActivity();
        FloatingActionButton fab = mActivity.findViewById(R.id.edit_flashcard_fab);
        fab.performClick();

        Intent expectedIntent = new Intent(mActivity, AddEditFlashcardActivity.class);
        Intent actualIntent = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
        assertEquals(FLASHCARD_1.getId(),
                actualIntent.getStringExtra(AddEditFlashcardActivity.FLASHCARD_ID_EXTRA));
    }

    @Test
    public void noFlashcardsToShow_showsNoFlashcardsText() {
        FakeFlashcardRemoteDataSource.getInstance().deleteAllFlashcards();
        FakeFlashcardLocalDataSource.getInstance().deleteAllFlashcards();
        mActivity = buildWithIntentAndStartActivity();
        TextView flashcardFront = mActivity.findViewById(R.id.flashcard_front);
        String noFlashcardText = mActivity.getString(R.string.no_flashcards_to_show_text);
        assertEquals(noFlashcardText, flashcardFront.getText().toString());
    }

    private FlashcardDetailActivity buildWithIntentAndStartActivity() {
        return Robolectric.buildActivity(FlashcardDetailActivity.class, createIntent())
                .create().start().resume().get();
    }

    private Intent createIntent() {
        Intent intent = new Intent();
        intent.putExtra(FlashcardDetailActivity.FLASHCARD_ID_EXTRA, FLASHCARD_1.getId());
        return intent;
    }
}
