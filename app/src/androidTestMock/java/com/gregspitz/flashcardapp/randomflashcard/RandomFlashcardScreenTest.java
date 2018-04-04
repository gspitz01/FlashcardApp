/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gregspitz.flashcardapp.randomflashcard;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.gregspitz.flashcardapp.R;
import com.gregspitz.flashcardapp.data.source.local.FakeFlashcardLocalDataSource;
import com.gregspitz.flashcardapp.data.source.remote.FakeFlashcardRemoteDataSource;
import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.data.model.Flashcard;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Tests for the implementation of {@link RandomFlashcardActivity}
 */
@RunWith(AndroidJUnit4.class)
public class RandomFlashcardScreenTest {
    @Rule
    public ActivityTestRule<RandomFlashcardActivity> mActivityTestRule =
            new ActivityTestRule<>(RandomFlashcardActivity.class, true, false);

    /**
     * {@link Flashcard} stub that is added to the fake service layer
     */
    private static Flashcard FLASHCARD = new Flashcard("Some random front", "Some random back");

    private static Flashcard FLASHCARD2 = new Flashcard("A different front", "A different back");

    @Before
    public void setup() {
        FlashcardRepository.destroyInstance();
        FakeFlashcardRemoteDataSource.getInstance().addFlashcards(FLASHCARD);
        FakeFlashcardLocalDataSource.getInstance(InstrumentationRegistry.getTargetContext())
                .addFlashcards(FLASHCARD);
    }

    @Test
    public void flashcardFront_displayedInUI() {
        mActivityTestRule.launchActivity(new Intent());
        onView(withId(R.id.flashcard_view)).check(matches(withText(FLASHCARD.getFront())));
    }

    @Test
    public void clickFlashcard_displaysBackOfCardInUI() {
        mActivityTestRule.launchActivity(new Intent());
        onView(withId(R.id.flashcard_view)).perform(click());
        onView(withId(R.id.flashcard_view)).check(matches(withText(FLASHCARD.getBack())));
    }

    @Ignore
    @Test
    public void nextCardButtonClick_loadsNewCard() {
        // TODO: figure out how to do this
        mActivityTestRule.launchActivity(new Intent());
        onView(withId(R.id.next_flashcard_button)).perform(click());
    }
}
