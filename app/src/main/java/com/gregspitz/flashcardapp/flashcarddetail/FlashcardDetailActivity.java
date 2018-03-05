package com.gregspitz.flashcardapp.flashcarddetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gregspitz.flashcardapp.R;
import com.gregspitz.flashcardapp.randomflashcard.domain.model.Flashcard;

public class FlashcardDetailActivity extends AppCompatActivity
        implements FlashcardDetailContract.View {

    public static final String FLASHCARD_ID_EXTRA = "flashcard_id";

    private FlashcardDetailContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_detail);
    }

    @Override
    public void setPresenter(FlashcardDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoadingIndicator(boolean active) {
        // TODO: implement
    }

    @Override
    public void showFlashcard(Flashcard flashcard) {
        // TODO: implement
    }

    @Override
    public void showEditFlashcard(String flashcardId) {
        // TODO: implement
    }

    @Override
    public void showFailedToLoadFlashcard() {
        // TODO: implement and test
    }

    @Override
    public String getIdFromIntent() {
        // TODO: implement and test
        return null;
    }

    @Override
    public boolean isActive() {
        return false;
    }
}
