package com.gregspitz.flashcardapp.addeditflashcard.domain.usecase;

import com.gregspitz.flashcardapp.UseCase;
import com.gregspitz.flashcardapp.data.source.FlashcardDataSource;
import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.data.model.Flashcard;

/**
 * A use case for saving a {@link Flashcard}
 * to a {@link com.gregspitz.flashcardapp.data.source.FlashcardRepository}
 */

public class SaveFlashcard
        extends UseCase<SaveFlashcard.RequestValues, SaveFlashcard.ResponseValue> {

    private FlashcardRepository mFlashcardRepository;

    public SaveFlashcard(FlashcardRepository repository) {
        mFlashcardRepository = repository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        mFlashcardRepository.saveFlashcard(requestValues.getFlashcard(),
                new FlashcardDataSource.SaveFlashcardCallback() {
                    @Override
                    public void onSaveSuccessful() {
                        getUseCaseCallback().onSuccess(new ResponseValue());
                    }

                    @Override
                    public void onSaveFailed() {
                        getUseCaseCallback().onError();
                    }
                });
    }

    public static class RequestValues implements UseCase.RequestValues {
        private Flashcard mFlashcard;

        public RequestValues(Flashcard flashcard) {
            mFlashcard = flashcard;
        }

        public Flashcard getFlashcard() {
            return mFlashcard;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {}
}
