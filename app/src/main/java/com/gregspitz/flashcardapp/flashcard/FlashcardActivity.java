package com.gregspitz.flashcardapp.flashcard;

import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        mFlashcardSideText.setOnClickListener(new View.OnClickListener() {
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
        mFlashcardSideText.setText(R.string.unable_to_load_flashcard_text);
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
