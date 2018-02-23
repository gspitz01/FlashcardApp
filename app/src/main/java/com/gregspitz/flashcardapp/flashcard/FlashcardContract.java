package com.gregspitz.flashcardapp.flashcard;

import com.gregspitz.flashcardapp.flashcard.domain.model.Flashcard;

/**
 * Contract between the flashcard view and the flashcard presenter
 */
public interface FlashcardContract {
    interface View {
        void showFlashcardFront(Flashcard flashcard);
        void showFlashcardBack(Flashcard flashcard);
    }

    interface Presenter {
        void turnFlashcard();
    }
}
