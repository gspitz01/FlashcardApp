package com.gregspitz.flashcardapp.flashcarddetail;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gregspitz.flashcardapp.R;
import com.gregspitz.flashcardapp.addeditflashcard.AddEditFlashcardActivity;
import com.gregspitz.flashcardapp.data.model.Flashcard;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlashcardDetailFragment extends Fragment
        implements FlashcardDetailContract.View {

    private TextView mFlashcardFront;
    private TextView mFlashcardBack;
    private FlashcardDetailContract.Presenter mPresenter;
    private boolean mActive = false;


    public FlashcardDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flashcard_detail, container, false);

        mFlashcardFront = view.findViewById(R.id.flashcard_front);
        mFlashcardBack = view.findViewById(R.id.flashcard_back);

        FloatingActionButton fab = getActivity().findViewById(R.id.edit_flashcard_fab);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mPresenter.editFlashcard();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
        mActive = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        mActive = false;
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
        mFlashcardFront.setText(flashcard.getFront());
        mFlashcardBack.setText(flashcard.getBack());
    }

    @Override
    public void showEditFlashcard(String flashcardId) {
        Intent intent = new Intent(getActivity(), AddEditFlashcardActivity.class);
        intent.putExtra(AddEditFlashcardActivity.FLASHCARD_ID_EXTRA, flashcardId);
        startActivity(intent);
    }

    @Override
    public void showFailedToLoadFlashcard() {
        mFlashcardFront.setText(R.string.no_flashcards_to_show_text);
    }

    @Override
    public String getIdFromIntent() {
        return getActivity().getIntent().getStringExtra(FlashcardDetailActivity.FLASHCARD_ID_EXTRA);
    }

    @Override
    public boolean isActive() {
        return mActive;
    }
}
