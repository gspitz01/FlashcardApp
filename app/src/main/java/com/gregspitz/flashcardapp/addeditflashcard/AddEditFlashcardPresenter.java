package com.gregspitz.flashcardapp.addeditflashcard;

import com.gregspitz.flashcardapp.UseCase;
import com.gregspitz.flashcardapp.UseCaseHandler;
import com.gregspitz.flashcardapp.addeditflashcard.domain.usecase.GetFlashcard;
import com.gregspitz.flashcardapp.randomflashcard.domain.model.Flashcard;

/**
 * A presenter for adding and editing {@link Flashcard}s
 */

public class AddEditFlashcardPresenter implements AddEditFlashcardContract.Presenter {

    private UseCaseHandler mUseCaseHandler;
    private AddEditFlashcardContract.View mView;
    private GetFlashcard mGetFlashcard;
    private Flashcard mFlashcard;

    public AddEditFlashcardPresenter(
            UseCaseHandler useCaseHandler, AddEditFlashcardContract.View view,
            GetFlashcard getFlashcard) {
        mUseCaseHandler = useCaseHandler;
        mView = view;
        mGetFlashcard = getFlashcard;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadFlashcard(mView.getIdFromIntent());
    }

    @Override
    public void loadFlashcard(String flashcardId) {
        mView.showLoadingIndicator(true);

        mUseCaseHandler.execute(mGetFlashcard, new GetFlashcard.RequestValues(flashcardId),
                new UseCase.UseCaseCallback<GetFlashcard.ResponseValue>() {
                    @Override
                    public void onSuccess(GetFlashcard.ResponseValue response) {
                        mFlashcard = response.getFlashcard();
                        if (mView.isActive()) {
                            mView.showLoadingIndicator(false);
                            mView.showFlashcard(mFlashcard);
                        }
                    }

                    @Override
                    public void onError() {
                        if (mView.isActive()) {
                            mView.showLoadingIndicator(false);
                            mView.showFailedToLoadFlashcard();
                        }
                    }
                });
    }

    @Override
    public void saveFlashcard() {
        // TODO: implement and test
    }
}
