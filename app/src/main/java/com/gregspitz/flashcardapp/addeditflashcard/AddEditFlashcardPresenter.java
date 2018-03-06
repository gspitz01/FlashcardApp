package com.gregspitz.flashcardapp.addeditflashcard;

import com.gregspitz.flashcardapp.UseCase;
import com.gregspitz.flashcardapp.UseCaseHandler;
import com.gregspitz.flashcardapp.addeditflashcard.domain.usecase.GetFlashcard;
import com.gregspitz.flashcardapp.addeditflashcard.domain.usecase.SaveFlashcard;
import com.gregspitz.flashcardapp.randomflashcard.domain.model.Flashcard;

/**
 * A presenter for adding and editing {@link Flashcard}s
 */

public class AddEditFlashcardPresenter implements AddEditFlashcardContract.Presenter {

    private UseCaseHandler mUseCaseHandler;
    private AddEditFlashcardContract.View mView;
    private GetFlashcard mGetFlashcard;
    private SaveFlashcard mSaveFlashcard;
    private Flashcard mFlashcard;

    public AddEditFlashcardPresenter(
            UseCaseHandler useCaseHandler, AddEditFlashcardContract.View view,
            GetFlashcard getFlashcard, SaveFlashcard saveFlashcard) {
        mUseCaseHandler = useCaseHandler;
        mView = view;
        mGetFlashcard = getFlashcard;
        mSaveFlashcard = saveFlashcard;
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
    public void saveFlashcard(Flashcard flashcard) {
        mUseCaseHandler.execute(mSaveFlashcard, new SaveFlashcard.RequestValues(flashcard),
                new UseCase.UseCaseCallback<SaveFlashcard.ResponseValue>() {
                    @Override
                    public void onSuccess(SaveFlashcard.ResponseValue response) {
                        if (mView.isActive()) {
                            mView.showSaveSuccessful();
                        }
                    }

                    @Override
                    public void onError() {
                        if (mView.isActive()) {
                            mView.showSaveFailed();
                        }
                    }
                });
    }

    @Override
    public void showList() {
        mView.showFlashcardList();
    }
}
