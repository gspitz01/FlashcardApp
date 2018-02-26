package com.gregspitz.flashcardapp.flashcard;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.gregspitz.flashcardapp.R;
import com.gregspitz.flashcardapp.data.FakeFlashcardRemoteDataSource;
import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.flashcard.domain.model.Flashcard;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.test.MoreAsserts.assertNotEqual;

/**
 * Tests for the implementation of {@link FlashcardActivity}
 */
@RunWith(AndroidJUnit4.class)
public class FlashcardActivityTest {
    @Rule
    public ActivityTestRule<FlashcardActivity> mActivityTestRule =
            new ActivityTestRule<>(FlashcardActivity.class, true, false);

    /**
     * {@link Flashcard} stub that is added to the fake service layer
     */
    private static Flashcard FLASHCARD = new Flashcard("Some random front", "Some random back");

    private static Flashcard FLASHCARD2 = new Flashcard("A different front", "A different back");

    @Before
    public void setup() {
        FlashcardRepository.destroyInstance();
        FakeFlashcardRemoteDataSource.getInstance().addFlashcards(FLASHCARD);
    }

    @Test
    public void flashcardFront_displayedInUI() {
        mActivityTestRule.launchActivity(new Intent());
        onView(withId(R.id.flashcard_side_text)).check(matches(withText(FLASHCARD.getFront())));
    }

    @Test
    public void clickFlashcard_displaysBackOfCardInUI() {
        mActivityTestRule.launchActivity(new Intent());
        onView(withId(R.id.flashcard_side_text)).perform(click());
        onView(withId(R.id.flashcard_side_text)).check(matches(withText(FLASHCARD.getBack())));
    }

    @Test
    public void nextCardButtonClick_loadsNewCard() {
        // TODO: figure out how to do this
        mActivityTestRule.launchActivity(new Intent());
        onView(withId(R.id.next_flashcard_button)).perform(click());
    }
}
