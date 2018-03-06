package com.gregspitz.flashcardapp.addeditflashcard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gregspitz.flashcardapp.R;
import com.gregspitz.flashcardapp.randomflashcard.domain.model.Flashcard;

public class AddEditFlashcardActivity extends AppCompatActivity
        implements AddEditFlashcardContract.View {

    public static final String FLASHCARD_ID_EXTRA = "flashcard_id";
    public static final String NEW_FLASHCARD = "-1";

    private AddEditFlashcardContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_flashcard);
    }

    @Override
    public void setPresenter(AddEditFlashcardContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoadingIndicator(boolean active) {
        // TODO: implement and test
    }

    @Override
    public String getIdFromIntent() {
        // TODO: implement and test
        return null;
    }

    @Override
    public void showFlashcard(Flashcard flashcard) {
        // TODO: implement and test
    }

    @Override
    public void showFlashcardList() {
        // TODO: implement and test
    }

    @Override
    public void showFailedToLoadFlashcard() {
        // TODO: implement and test
    }

    @Override
    public boolean isActive() {
        // TODO: implement and test
        return false;
    }
}
