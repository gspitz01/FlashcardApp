package com.gregspitz.flashcardapp.flashcardlist;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gregspitz.flashcardapp.R;
import com.gregspitz.flashcardapp.data.FakeFlashcardRemoteDataSource;
import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.flashcard.domain.model.Flashcard;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;


/**
 * Tests for the implementation of {@link FlashcardListActivity}
 */
@RunWith(AndroidJUnit4.class)
public class FlashcardListActivityTest {

    private static final Flashcard FLASHCARD_1 =
            new Flashcard("0", "A front", "A back");

    private static final Flashcard FLASHCARD_2 =
            new Flashcard("1", "A different front", "A different back");

    @Rule
    public IntentsTestRule<FlashcardListActivity> mIntentsTestRule =
            new IntentsTestRule<>(FlashcardListActivity.class, true, false);

    @Before
    public void setup() {
        FlashcardRepository.destroyInstance();
        FakeFlashcardRemoteDataSource.getInstance().addFlashcards(
                FLASHCARD_1, FLASHCARD_2);
    }

    @Test
    public void flashcardRecyclerView_shouldHaveShowFlashcardFront() {
        createIntentAndLaunchActivity();
        onView(withId(R.id.flashcard_recycler_view))
                .check(matches(hasFlashcardFrontForPosition(0, FLASHCARD_1)));
        onView(withId(R.id.flashcard_recycler_view))
                .check(matches(hasFlashcardFrontForPosition(1, FLASHCARD_2)));
        onView(withId(R.id.no_flashcards_to_show)).check(matches(not(isDisplayed())));
    }

    @Test
    public void noFlashcardsToShow_shouldShowNoFlashcardMessage() {
        FakeFlashcardRemoteDataSource.getInstance().clearFlashcards();
        createIntentAndLaunchActivity();
        onView(withId(R.id.no_flashcards_to_show)).check(matches(isDisplayed()));
    }

    private void createIntentAndLaunchActivity() {
        Intent intent = new Intent();
        mIntentsTestRule.launchActivity(intent);
    }

    private Matcher<View> hasFlashcardFrontForPosition(final int position, final Flashcard flashcard) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("Item has flashcard data at position " + position + ": ");
            }

            @Override
            protected boolean matchesSafely(RecyclerView item) {
                if (item == null) {
                    return false;
                }

                RecyclerView.ViewHolder holder = item.findViewHolderForAdapterPosition(position);

                return holder != null &&
                        withChild(withText(flashcard.getFront())).matches(holder.itemView);
            }
        };
    }
}