package com.gregspitz.flashcardapp.flashcard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.gregspitz.flashcardapp.BasePresenter;
import com.gregspitz.flashcardapp.Injection;
import com.gregspitz.flashcardapp.R;

public class FlashcardActivity extends AppCompatActivity implements FlashcardContract.View {

    private TextView mFlashcardSideText;
    private FlashcardContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);
        mFlashcardSideText = findViewById(R.id.flashcard_side_text);

        // Create presenter
        new FlashcardPresenter(Injection.provideUseCaseHandler(),
                this, Injection.provideGetFlashcard(getApplicationContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showFlashcardSide(String flashcardSide) {
        mFlashcardSideText.setText(flashcardSide);
    }

    @Override
    public void showFailedToLoadFlashcard() {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setPresenter(FlashcardContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
