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

package com.gregspitz.flashcardapp.flashcarddetail;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.gregspitz.flashcardapp.R;
import com.gregspitz.flashcardapp.addeditflashcard.AddEditFlashcardActivity;
import com.gregspitz.flashcardapp.data.source.local.FakeFlashcardLocalDataSource;
import com.gregspitz.flashcardapp.data.source.remote.FakeFlashcardRemoteDataSource;
import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.data.model.Flashcard;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Tests for the implementation of {@link FlashcardDetailActivity}
 */
@RunWith(AndroidJUnit4.class)
public class FlashcardDetailScreenTest {

    private static final Flashcard FLASHCARD_1 =
            new Flashcard("0", "Front", "Back");

    private static final Flashcard FLASHCARD_2 =
            new Flashcard("1", "Front", "Back");

    @Rule
    public IntentsTestRule<FlashcardDetailActivity> mIntentsTestRule =
            new IntentsTestRule<>(FlashcardDetailActivity.class,
                    true, false);

    @Before
    public void setup() {
        FlashcardRepository.destroyInstance();
        FakeFlashcardLocalDataSource.getInstance().deleteAllFlashcards();
        FakeFlashcardRemoteDataSource.getInstance().deleteAllFlashcards();
        FakeFlashcardLocalDataSource.getInstance().addFlashcards(FLASHCARD_1, FLASHCARD_2);
        FakeFlashcardRemoteDataSource.getInstance().addFlashcards(FLASHCARD_1, FLASHCARD_2);
    }

    @Test
    public void atStart_showFlashcard() {
        createIntentAndLaunchActivity();
        onView(withId(R.id.flashcard_front)).check(matches(withText(FLASHCARD_1.getFront())));
        onView(withId(R.id.flashcard_back)).check(matches(withText(FLASHCARD_1.getBack())));
    }

    @Test
    public void clickEditFlashcard_showsEditFlashcardActivity() {
        createIntentAndLaunchActivity();
        onView(withId(R.id.edit_flashcard_fab)).perform(click());
        intended(allOf(hasComponent(AddEditFlashcardActivity.class.getName()),
                hasExtra(AddEditFlashcardActivity.FLASHCARD_ID_EXTRA, FLASHCARD_1.getId())));
    }

    @Test
    public void noAvailableFlashcard_showsFlashcardNotAvailableText() {
        FakeFlashcardRemoteDataSource.getInstance().deleteAllFlashcards();
        FakeFlashcardLocalDataSource.getInstance().deleteAllFlashcards();
        createIntentAndLaunchActivity();
        onView(withId(R.id.flashcard_front))
                .check(matches(withText(R.string.no_flashcards_to_show_text)));
    }

    private void createIntentAndLaunchActivity() {
        Intent intent = new Intent();
        intent.putExtra(FlashcardDetailActivity.FLASHCARD_ID_EXTRA, FLASHCARD_1.getId());
        mIntentsTestRule.launchActivity(intent);
    }
}
