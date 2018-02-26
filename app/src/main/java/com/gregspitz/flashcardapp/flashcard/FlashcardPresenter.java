package com.gregspitz.flashcardapp.flashcard;

import android.support.annotation.NonNull;
import android.util.Log;

import com.gregspitz.flashcardapp.UseCase;
import com.gregspitz.flashcardapp.UseCaseHandler;
import com.gregspitz.flashcardapp.flashcard.domain.model.Flashcard;
import com.gregspitz.flashcardapp.flashcard.domain.usecase.GetFlashcard;

/**
 * Presenter of flashcards
 */

public class FlashcardPresenter implements FlashcardContract.Presenter {

    private final UseCaseHandler mUseCaseHandler;
    private final FlashcardContract.View mView;
    private final GetFlashcard mGetFlashcard;
    private Flashcard mCurrentFlashcard;
    private boolean mShowingFront;

    public FlashcardPresenter(@NonNull UseCaseHandler useCaseHandler,
                              @NonNull FlashcardContract.View view,
                              @NonNull GetFlashcard getFlashcard) {
        mUseCaseHandler = useCaseHandler;
        mView = view;
        mGetFlashcard = getFlashcard;
        mShowingFront = false;
        mView.setPresenter(this);
    }

    @Override
    public void turnFlashcard() {
        String sideToShow = mCurrentFlashcard.getFront();
        if (mShowingFront) {
            sideToShow = mCurrentFlashcard.getBack();
        }
        mView.showFlashcardSide(sideToShow);
        mShowingFront = !mShowingFront;
    }

    @Override
    public void loadNewFlashcard() {
        mView.setLoadingIndicator(true);

        mUseCaseHandler.execute(mGetFlashcard, new GetFlashcard.RequestValues(),
                new UseCase.UseCaseCallback<GetFlashcard.ResponseValue>() {
                    @Override
                    public void onSuccess(GetFlashcard.ResponseValue response) {
                        mCurrentFlashcard = response.getFlashcard();
                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);
                            mView.showFlashcardSide(mCurrentFlashcard.getFront());
                            mShowingFront = true;
                        }
                    }

                    @Override
                    public void onError() {
                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);
                            mView.showFailedToLoadFlashcard();
                        }
                    }
                });
    }

    @Override
    public void start() {
        loadNewFlashcard();
    }
}
