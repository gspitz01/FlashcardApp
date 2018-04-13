package com.gregspitz.flashcardapp.addeditflashcard;

import static junit.framework.Assert.assertEquals;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.gregspitz.flashcardapp.Injection;
import com.gregspitz.flashcardapp.R;
import com.gregspitz.flashcardapp.TestUseCaseScheduler;
import com.gregspitz.flashcardapp.data.model.Flashcard;
import com.gregspitz.flashcardapp.data.source.FlashcardDataSource;
import com.gregspitz.flashcardapp.data.source.local.FakeFlashcardLocalDataSource;
import com.gregspitz.flashcardapp.data.source.remote.FakeFlashcardRemoteDataSource;
import com.gregspitz.flashcardapp.flashcardlist.FlashcardListActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowToast;

import java.util.ArrayList;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class AddEditFlashcardRobolectricTest {

    private static final Flashcard FLASHCARD_1 =
            new Flashcard("0", "Front1", "Back1");

    private static final Flashcard FLASHCARD_2 =
            new Flashcard("1", FakeFlashcardRemoteDataSource.FAILED_SAVE_FRONT, "Back2");

    private Activity mActivity;

    @Before
    public void setup() {
        Injection.setScheduler(new TestUseCaseScheduler());
        FakeFlashcardLocalDataSource.getInstance().deleteAllFlashcards();
        FakeFlashcardRemoteDataSource.getInstance().deleteAllFlashcards();
        FakeFlashcardLocalDataSource.getInstance().addFlashcards(FLASHCARD_1, FLASHCARD_2);
        FakeFlashcardRemoteDataSource.getInstance().addFlashcards(FLASHCARD_1, FLASHCARD_2);
    }

    @Test
    public void startWithAvailableFlashcardIdIntent_showsFlashcard() {
        mActivity = launchWithIntent(FLASHCARD_1.getId());

        EditText flashcardFront = mActivity.findViewById(R.id.flashcard_front);
        EditText flashcardBack = mActivity.findViewById(R.id.flashcard_back);

        assertEquals(FLASHCARD_1.getFront(), flashcardFront.getText().toString());
        assertEquals(FLASHCARD_1.getBack(), flashcardBack.getText().toString());
    }

    @Test
    public void startWithNewFlashcardIntent_displaysBlankFields() {
        mActivity = launchWithIntent(AddEditFlashcardActivity.NEW_FLASHCARD);

        EditText flashcardFront = mActivity.findViewById(R.id.flashcard_front);
        EditText flashcardBack = mActivity.findViewById(R.id.flashcard_back);

        assertEquals("", flashcardFront.getText().toString());
        assertEquals("", flashcardBack.getText().toString());
    }

    @Test
    public void saveFlashcardButtonClick_savesChangesToExistingFlashcard() {
        mActivity = launchWithIntent(FLASHCARD_1.getId());

        String newFront = "new front text";
        String newBack = "new back text";
        setFlashcardTextAndPerformSaveClick(newFront, newBack);

        Flashcard savedFlashcard = getFlashcardFromRepoById(FLASHCARD_1.getId());
        assertEquals(newFront, savedFlashcard.getFront());
        assertEquals(newBack, savedFlashcard.getBack());
    }

    @Test
    public void saveFlashcardButtonClick_addsNewFlashcardToRepo() {
        mActivity = launchWithIntent(AddEditFlashcardActivity.NEW_FLASHCARD);

        String newFront = "new front text";
        String newBack = "new back text";
        setFlashcardTextAndPerformSaveClick(newFront, newBack);

        List<Flashcard> savedFlashcards = getFlashcardsFromRepo();
        Assert.assertEquals(3, savedFlashcards.size());
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
    public void successfulSaveClick_showsFlashcardList() {
        mActivity = launchWithIntent(FLASHCARD_1.getId());
        setFlashcardTextAndPerformSaveClick("new front", "new back");

        Intent expectedIntent = new Intent(mActivity, FlashcardListActivity.class);
        Intent actualIntent = ShadowApplication.getInstance().getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
    }

    @Test
    public void failedSaveClick_showsFailedSaveToast() {
        // FLASHCARD_2 has the failed save trigger as its front
        mActivity = launchWithIntent(FLASHCARD_2.getId());
        Button saveButton = mActivity.findViewById(R.id.save_flashcard_button);
        saveButton.performClick();
        String toastText = mActivity.getString(R.string.failed_save_toast_text);
        assertEquals(toastText, ShadowToast.getTextOfLatestToast());
    }

    private void setFlashcardTextAndPerformSaveClick(String newFront, String newBack) {
        EditText flashcardFront = mActivity.findViewById(R.id.flashcard_front);
        EditText flashcardBack = mActivity.findViewById(R.id.flashcard_back);
        flashcardFront.setText(newFront);
        flashcardBack.setText(newBack);
        Button saveButton = mActivity.findViewById(R.id.save_flashcard_button);
        saveButton.performClick();
    }

    private AddEditFlashcardActivity launchWithIntent(String intentExtra) {
        return Robolectric.buildActivity(AddEditFlashcardActivity.class,
                createIntentWithExtra(intentExtra)).create().start().resume().get();
    }

    private Intent createIntentWithExtra(String extra) {
        Intent intent = new Intent();
        intent.putExtra(AddEditFlashcardActivity.FLASHCARD_ID_EXTRA, extra);
        return intent;
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
}
