package com.gregspitz.flashcardapp.randomflashcard;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.gregspitz.flashcardapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RandomFlashcardFragment extends Fragment implements RandomFlashcardContract.View {

    private RandomFlashcardContract.Presenter mPresenter;
    private boolean mActive = false;
    private FlashcardView mFlashcardView;
    private ProgressBar mProgressBar;

    public RandomFlashcardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_random_flashcard, container, false);

        mFlashcardView = view.findViewById(R.id.flashcard_view);
        mFlashcardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.turnFlashcard();
            }
        });

        mProgressBar = view.findViewById(R.id.progress_bar);

        Button nextFlashcardButton = view.findViewById(R.id.next_flashcard_button);
        nextFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadNewFlashcard();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActive = true;
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mActive = false;
    }

    @Override
    public void setPresenter(RandomFlashcardContract.Presenter presenter) {
        mPresenter = presenter;
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
        return mActive;
    }
}
