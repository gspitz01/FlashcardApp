package com.gregspitz.flashcardapp.randomflashcard;

import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.gregspitz.flashcardapp.Injection;
import com.gregspitz.flashcardapp.R;

public class FlashcardActivity extends AppCompatActivity implements FlashcardContract.View {

    private FlashcardView mFlashcardView;
    private FlashcardContract.Presenter mPresenter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);
        mProgressBar = findViewById(R.id.progress_bar);
        mFlashcardView = findViewById(R.id.flashcard_view);
        mFlashcardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.turnFlashcard();
            }
        });

        Button nextFlashcardButton = findViewById(R.id.next_flashcard_button);
        nextFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadNewFlashcard();
            }
        });

        // Create presenter
        new FlashcardPresenter(Injection.provideUseCaseHandler(),
                this, Injection.provideGetRandomFlashcard(getApplicationContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showFlashcardSide(String flashcardSide) {
        mFlashcardView.setText(flashcardSide);
    }

    @Override
    public void showFailedToLoadFlashcard() {
        mFlashcardView.setText(R.string.unable_to_load_flashcard_text);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void setPresenter(FlashcardContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @VisibleForTesting
    public FlashcardContract.Presenter getPresenter() {
        return mPresenter;
    }
}
