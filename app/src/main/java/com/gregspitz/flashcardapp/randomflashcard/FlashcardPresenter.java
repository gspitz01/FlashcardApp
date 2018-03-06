package com.gregspitz.flashcardapp.randomflashcard;

import android.support.annotation.NonNull;

import com.gregspitz.flashcardapp.UseCase;
import com.gregspitz.flashcardapp.UseCaseHandler;
import com.gregspitz.flashcardapp.data.model.Flashcard;
import com.gregspitz.flashcardapp.randomflashcard.domain.usecase.GetRandomFlashcard;

/**
 * Presenter of flashcards
 */

public class FlashcardPresenter implements FlashcardContract.Presenter {

    private final UseCaseHandler mUseCaseHandler;
    private final FlashcardContract.View mView;
    private final GetRandomFlashcard mGetRandomFlashcard;
    private Flashcard mCurrentFlashcard;
    private boolean mShowingFront;

    public FlashcardPresenter(@NonNull UseCaseHandler useCaseHandler,
                              @NonNull FlashcardContract.View view,
                              @NonNull GetRandomFlashcard getRandomFlashcard) {
        mUseCaseHandler = useCaseHandler;
        mView = view;
        mGetRandomFlashcard = getRandomFlashcard;
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

        mUseCaseHandler.execute(mGetRandomFlashcard, new GetRandomFlashcard.RequestValues(),
                new UseCase.UseCaseCallback<GetRandomFlashcard.ResponseValue>() {
                    @Override
                    public void onSuccess(GetRandomFlashcard.ResponseValue response) {
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
