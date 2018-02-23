package com.gregspitz.flashcardapp.flashcard;

import com.gregspitz.flashcardapp.BasePresenter;
import com.gregspitz.flashcardapp.BaseView;
import com.gregspitz.flashcardapp.flashcard.domain.model.Flashcard;

/**
 * Contract between the flashcard view and the flashcard presenter
 */
public interface FlashcardContract {
    interface View extends BaseView {
        void setLoadingIndicator(boolean active);
        void showFlashcardFront(Flashcard flashcard);
        void showFlashcardBack(Flashcard flashcard);
        void showFailedToLoadFlashcard();
        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void turnFlashcard();
        void loadNewFlashcard();
    }
}
