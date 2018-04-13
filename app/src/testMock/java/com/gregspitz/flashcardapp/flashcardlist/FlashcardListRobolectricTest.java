package com.gregspitz.flashcardapp.flashcardlist;

import static junit.framework.Assert.assertEquals;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
public class FlashcardListRobolectricTest {

    private static final Flashcard FLASHCARD_1 =
            new Flashcard("0", "A front", "A back");

    private static final Flashcard FLASHCARD_2 =
            new Flashcard("1", "A different front", "A different back");

    private Activity mActivity;

    @Before
    public void setup() {
        Injection.setScheduler(new TestUseCaseScheduler());
        FlashcardRepository.destroyInstance();
        FakeFlashcardLocalDataSource.getInstance().deleteAllFlashcards();
        FakeFlashcardLocalDataSource.getInstance().addFlashcards(FLASHCARD_1, FLASHCARD_2);
        FakeFlashcardRemoteDataSource.getInstance().deleteAllFlashcards();
        FakeFlashcardRemoteDataSource.getInstance().addFlashcards(FLASHCARD_1, FLASHCARD_2);
    }

    @Test
    public void recyclerView_showsFlashcardFronts() {
        mActivity = Robolectric.setupActivity(FlashcardListActivity.class);
        RecyclerView recyclerView = getRecyclerViewAndLayItOut();

        FlashcardRecyclerAdapter adapter = (FlashcardRecyclerAdapter) recyclerView.getAdapter();
        assertEquals(2, adapter.getItemCount());

        TextView flashcardHolder1TextView =
                recyclerView.getChildAt(0).findViewById(R.id.flashcard_front);
        String flashcardHolder1Text = flashcardHolder1TextView.getText().toString();
        TextView flashcardHolder2TextView =
                recyclerView.getChildAt(1).findViewById(R.id.flashcard_front);
        String flashcardHolder2Text = flashcardHolder2TextView.getText().toString();

        assertEquals(FLASHCARD_1.getFront(), flashcardHolder1Text);
        assertEquals(FLASHCARD_2.getFront(), flashcardHolder2Text);
    }

    @Test
    public void noFlashcardsToShow_showsNoFlashcardsMessage() {
        FakeFlashcardRemoteDataSource.getInstance().deleteAllFlashcards();
        FakeFlashcardLocalDataSource.getInstance().deleteAllFlashcards();
        mActivity = Robolectric.setupActivity(FlashcardListActivity.class);
        TextView noFlashcardsView = mActivity.findViewById(R.id.no_flashcards_to_show);
        assertEquals(View.VISIBLE, noFlashcardsView.getVisibility());
    }

    @Test
    public void clickOnFlashcard_startsFlashcardDetailActivity() {
        mActivity = Robolectric.setupActivity(FlashcardListActivity.class);
        RecyclerView recyclerView = getRecyclerViewAndLayItOut();
        View flashcardHolder = recyclerView.getChildAt(0);

        flashcardHolder.performClick();

        Intent expectedIntent = new Intent(mActivity, FlashcardDetailActivity.class);
        Intent actualIntent = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
        assertEquals(FLASHCARD_1.getId(),
                actualIntent.getStringExtra(FlashcardDetailActivity.FLASHCARD_ID_EXTRA));
    }

    @Test
    public void fabClick_startsAddFlashcardActivity() {
        mActivity = Robolectric.setupActivity(FlashcardListActivity.class);
        FloatingActionButton fab = mActivity.findViewById(R.id.add_flashcard_fab);

        fab.performClick();

        Intent expectedIntent = new Intent(mActivity, AddEditFlashcardActivity.class);
        Intent actualIntent = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
        assertEquals(AddEditFlashcardActivity.NEW_FLASHCARD,
                actualIntent.getStringExtra(AddEditFlashcardActivity.FLASHCARD_ID_EXTRA));
    }

    private RecyclerView getRecyclerViewAndLayItOut() {
        RecyclerView recyclerView = mActivity.findViewById(R.id.flashcard_recycler_view);
        // Silly Robolectric; need to force measure and layout of RecyclerView
        recyclerView.measure(0, 0);
        recyclerView.layout(0, 0, 100, 1000);
        return recyclerView;
    }
}
