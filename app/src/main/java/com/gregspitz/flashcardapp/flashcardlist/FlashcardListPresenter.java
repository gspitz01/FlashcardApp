package com.gregspitz.flashcardapp.flashcardlist;

import com.gregspitz.flashcardapp.UseCase;
import com.gregspitz.flashcardapp.UseCaseHandler;
import com.gregspitz.flashcardapp.data.model.Flashcard;
import com.gregspitz.flashcardapp.flashcardlist.domain.usecase.GetFlashcards;

/**
 * A presenter for a FlashcardListView
 */

public class FlashcardListPresenter implements FlashcardListContract.Presenter {

    private final UseCaseHandler mUseCaseHandler;
    private final FlashcardListContract.View mView;
    private final GetFlashcards mGetFlashcards;

    public FlashcardListPresenter(
            UseCaseHandler useCaseHandler, FlashcardListContract.View view,
            GetFlashcards getFlashcards) {
        mUseCaseHandler = useCaseHandler;
        mView = view;
        mGetFlashcards = getFlashcards;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadFlashcards();
    }

    @Override
    public void selectFlashcard(Flashcard flashcard) {
        mView.showFlashcardDetailsUi(flashcard.getId());
    }

    @Override
    public void addFlashcard() {
        mView.showAddFlashcard();
    }

    @Override
    public void loadFlashcards() {
        mView.setLoadingIndicator(true);

        mUseCaseHandler.execute(mGetFlashcards, new GetFlashcards.RequestValues(),
                new UseCase.UseCaseCallback<GetFlashcards.ResponseValue>() {
                    @Override
                    public void onSuccess(GetFlashcards.ResponseValue response) {
                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);
                            mView.showFlashcards(response.getFlashcards());
                        }
                    }

                    @Override
                    public void onError() {
                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);
                            mView.showFailedToLoadFlashcards();
                        }
                    }
                });
    }

    @Override
    public void onFlashcardClick(String flashcardId) {
        mView.showFlashcardDetailsUi(flashcardId);
    }
}
