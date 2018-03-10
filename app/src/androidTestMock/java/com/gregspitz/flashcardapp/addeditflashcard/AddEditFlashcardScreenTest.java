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

package com.gregspitz.flashcardapp.addeditflashcard;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.gregspitz.flashcardapp.R;
import com.gregspitz.flashcardapp.data.model.Flashcard;
import com.gregspitz.flashcardapp.data.source.FlashcardDataSource;
import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.data.source.remote.FakeFlashcardRemoteDataSource;
import com.gregspitz.flashcardapp.flashcardlist.FlashcardListActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the implementation of {@link AddEditFlashcardActivity}
 */
@RunWith(AndroidJUnit4.class)
public class AddEditFlashcardScreenTest {

    private static final Flashcard FLASHCARD_1 =
            new Flashcard("0", "Front1", "Back1");

    private static final Flashcard FLASHCARD_2 =
            new Flashcard("1", FakeFlashcardRemoteDataSource.FAILED_SAVE_FRONT, "Back2");

    @Rule
    public IntentsTestRule<AddEditFlashcardActivity> mIntentsTestRule =
            new IntentsTestRule<>(AddEditFlashcardActivity.class,
                    true, false);

    @Before
    public void setup() {
        FlashcardRepository.destroyInstance();
        FakeFlashcardRemoteDataSource.getInstance().addFlashcards(FLASHCARD_1, FLASHCARD_2);
    }

    @Test
    public void startWithAvailableFlashcardIdIntent_displaysFlashcard() {
        launchActivityWithExtra(FLASHCARD_1.getId());

        onView(withId(R.id.flashcard_front)).check(matches(withText(FLASHCARD_1.getFront())));
        onView(withId(R.id.flashcard_back)).check(matches(withText(FLASHCARD_1.getBack())));
    }

    @Test
    public void startWithNewFlashcardExtraIntent_displaysBlankFields() {
        launchActivityWithExtra(AddEditFlashcardActivity.NEW_FLASHCARD);

        onView(withId(R.id.flashcard_front)).check(matches(withText("")));
        onView(withId(R.id.flashcard_back)).check(matches(withText("")));
    }

    @Test
    public void saveFlashcardButtonClick_savesChangesToExistingFlashcard() {
        launchActivityWithExtra(FLASHCARD_1.getId());

        String newFront = "Different front";
        String newBack = "Different back";
        onView(withId(R.id.flashcard_front)).perform(replaceText(newFront));
        onView(withId(R.id.flashcard_back)).perform(replaceText(newBack));
        onView(withId(R.id.save_flashcard_button)).perform(click());

        Flashcard savedFlashcard = getFlashcardFromRepoById(FLASHCARD_1.getId());
        assertEquals(newFront, savedFlashcard.getFront());
        assertEquals(newBack, savedFlashcard.getBack());
    }

    @Test
    public void saveFlashcardButtonClick_addsNewFlashcard() {
        launchActivityWithExtra(AddEditFlashcardActivity.NEW_FLASHCARD);

        String newFront = "new front";
        String newBack = "new back";
        onView(withId(R.id.flashcard_front)).perform(typeText(newFront));
        onView(withId(R.id.flashcard_back)).perform(typeText(newBack));
        onView(withId(R.id.save_flashcard_button)).perform(click());

        List<Flashcard> savedFlashcards = getFlashcardsFromRepo();

        assertEquals(3, savedFlashcards.size());
        boolean inThere = false;
        for (Flashcard flashcard : savedFlashcards) {
            if (flashcard.getFront().equals(newFront) &&
                    flashcard.getBack().equals(newBack)) {
                inThere = true;
            }
        }
        assertTrue(inThere);
    }

    @Test
    public void successfulSaveButtonClick_showsFlashcardList() {
        launchActivityWithExtra(FLASHCARD_1.getId());
        onView(withId(R.id.save_flashcard_button)).perform(click());
        intended(hasComponent(FlashcardListActivity.class.getName()));
    }

    @Test
    public void failedSaveButtonClick_showsFailedSaveToast() {
        // the front of FLASHCARD_2 is the trigger for a failed save in
        // FakeFlashcardRemoteDataSource
        launchActivityWithExtra(FLASHCARD_2.getId());
        onView(withId(R.id.save_flashcard_button)).perform(click());
        onView(withText(R.string.failed_save_toast_text))
                .inRoot(withDecorView(not(is(mIntentsTestRule.getActivity()
                        .getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    private Flashcard getFlashcardFromRepoById(String id) {
        final List<Flashcard> savedFlashcards = new ArrayList<>();
        FakeFlashcardRemoteDataSource.getInstance().getFlashcard(id,
                new FlashcardDataSource.GetFlashcardCallback() {
                    @Override
                    public void onFlashcardLoaded(Flashcard flashcard) {
                        savedFlashcards.add(flashcard);
                    }

                    @Override
                    public void onDataNotAvailable() {

                    }
                });
        return savedFlashcards.get(0);
    }

    private List<Flashcard> getFlashcardsFromRepo() {
        final List<Flashcard> savedFlashcards = new ArrayList<>();
        FakeFlashcardRemoteDataSource.getInstance().getFlashcards(
                new FlashcardDataSource.GetFlashcardsCallback() {
            @Override
            public void onFlashcardsLoaded(List<Flashcard> flashcards) {
                savedFlashcards.addAll(flashcards);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
        return savedFlashcards;
    }

    private void launchActivityWithExtra(String extra) {
        Intent intent = new Intent();
        intent.putExtra(AddEditFlashcardActivity.FLASHCARD_ID_EXTRA, extra);
        mIntentsTestRule.launchActivity(intent);
    }

}
