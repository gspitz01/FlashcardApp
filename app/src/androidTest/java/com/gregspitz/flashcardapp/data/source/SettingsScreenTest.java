package com.gregspitz.flashcardapp.data.source;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.gregspitz.flashcardapp.R;
import com.gregspitz.flashcardapp.settings.SettingsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Tests for {@link SettingsActivity}
 */
@RunWith(AndroidJUnit4.class)
public class SettingsScreenTest {

    @Rule
    public ActivityTestRule<SettingsActivity> mActivityTestRule =
            new ActivityTestRule<>(SettingsActivity.class);

    @Test
    public void preferencesAreShown() {
        onView(withText(R.string.pref_app_theme_text)).check(matches(isDisplayed()));
    }
}
