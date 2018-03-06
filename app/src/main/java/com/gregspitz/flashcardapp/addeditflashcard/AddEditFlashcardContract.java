package com.gregspitz.flashcardapp.addeditflashcard;

import com.gregspitz.flashcardapp.BasePresenter;
import com.gregspitz.flashcardapp.BaseView;
import com.gregspitz.flashcardapp.randomflashcard.domain.model.Flashcard;

/**
 * A contract between the AddEditFlashcard view and its presenter
 */

public interface AddEditFlashcardContract {

    interface View extends BaseView<Presenter> {
        
        void showLoadingIndicator(boolean active);
        
        String getIdFromIntent();
        
        void showFlashcard(Flashcard flashcard);
        
        void showFlashcardList();
        
        void showFailedToLoadFlashcard();
        
        void showSaveSuccessful();
        
        void showSaveFailed();
        
        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        
        void loadFlashcard(String flashcardId);
        
        void saveFlashcard(Flashcard flashcard);
        
        void showList();
    }
}
