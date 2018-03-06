package com.gregspitz.flashcardapp.addeditflashcard;

import com.gregspitz.flashcardapp.BasePresenter;
import com.gregspitz.flashcardapp.BaseView;
import com.gregspitz.flashcardapp.randomflashcard.domain.model.Flashcard;

/**
 * A contract between the AddEditFlashcard view and its presenter
 */

public interface AddEditFlashcardContract {

    // TODO: fill this out and make tests and presenter
    interface View extends BaseView<Presenter> {
        void showLoadingIndicator(boolean active);
        String getIdFromIntent();
        void showFlashcard(Flashcard flashcard);
        void showFlashcardList();
        void showFailedToLoadFlashcard();
        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void loadFlashcard(String flashcardId);
        void saveFlashcard();
    }
}
