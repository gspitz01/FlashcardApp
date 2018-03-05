package com.gregspitz.flashcardapp.flashcardlist;

import com.gregspitz.flashcardapp.BasePresenter;
import com.gregspitz.flashcardapp.BaseView;
import com.gregspitz.flashcardapp.flashcard.domain.model.Flashcard;

import java.util.List;

/**
 * A contract between the flashcard list view and its presenter
 */

public interface FlashcardListContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);
        void showFlashcards(List<Flashcard> flashcards);
        void showFailedToLoadFlashcards();
        void showAddFlashcard();
        void showFlashcardDetailsUi(String flashcardId);
        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void selectFlashcard(Flashcard flashcard);
        void addFlashcard();
        void loadFlashcards();
    }
}
