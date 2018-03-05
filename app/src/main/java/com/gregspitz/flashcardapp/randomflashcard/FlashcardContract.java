package com.gregspitz.flashcardapp.randomflashcard;

import com.gregspitz.flashcardapp.BasePresenter;
import com.gregspitz.flashcardapp.BaseView;

/**
 * Contract between the flashcard view and the flashcard presenter
 */
public interface FlashcardContract {
    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);
        void showFlashcardSide(String flashcardSide);
        void showFailedToLoadFlashcard();
        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void turnFlashcard();
        void loadNewFlashcard();
    }
}
