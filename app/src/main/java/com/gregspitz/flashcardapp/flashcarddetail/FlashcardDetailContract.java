package com.gregspitz.flashcardapp.flashcarddetail;

import com.gregspitz.flashcardapp.BasePresenter;
import com.gregspitz.flashcardapp.BaseView;
import com.gregspitz.flashcardapp.data.model.Flashcard;

/**
 * A contract between the FlashcardDetail view and its presenter
 */

public interface FlashcardDetailContract {

    interface View extends BaseView<Presenter> {
        void showLoadingIndicator(boolean active);
        void showFlashcard(Flashcard flashcard);
        void showEditFlashcard(String flashcardId);
        void showFailedToLoadFlashcard();
        String getIdFromIntent();
        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void loadFlashcard(String flashcardId);
        void editFlashcard();
    }
}
