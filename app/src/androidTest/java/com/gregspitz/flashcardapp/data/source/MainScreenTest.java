package com.gregspitz.flashcardapp.data.source;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.gregspitz.flashcardapp.MainActivity;
import com.gregspitz.flashcardapp.R;
import com.gregspitz.flashcardapp.flashcardlist.FlashcardListActivity;
import com.gregspitz.flashcardapp.randomflashcard.RandomFlashcardActivity;
import com.gregspitz.flashcardapp.settings.SettingsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Tests for the implementation of {@link com.gregspitz.flashcardapp.MainActivity}
 */
@RunWith(AndroidJUnit4.class)
public class MainScreenTest {

    @Rule
    public IntentsTestRule<MainActivity> mIntentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Test
    public void settingsMenuItemClick_showsSettingsActivity() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText(R.string.setting_menu_item_text)).perform(click());
        intended(hasComponent(SettingsActivity.class.getName()));
    }

    @Test
    public void showListButtonClick_showsListActivity() {
        onView(withId(R.id.show_list_button)).perform(click());
        intended(hasComponent(FlashcardListActivity.class.getName()));
    }

    @Test
    public void playGameButtonClick_showsFlashcardActivity() {
        onView(withId(R.id.play_game_button)).perform(click());
        intended(hasComponent(RandomFlashcardActivity.class.getName()));
    }
}
