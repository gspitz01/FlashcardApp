package com.gregspitz.flashcardapp.flashcarddetail;

import com.gregspitz.flashcardapp.BasePresenter;
import com.gregspitz.flashcardapp.BaseView;
import com.gregspitz.flashcardapp.randomflashcard.domain.model.Flashcard;

/**
 * A contract between the FlashcardDetail view and its presenter
 */

public interface FlashcardDetailContract {
    // TODO: fill this out and make tests and a presenter
    interface View extends BaseView<Presenter> {
        void showFlashcard(Flashcard flashcard);
        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void loadFlashcard(String flashcardId);
    }
}
