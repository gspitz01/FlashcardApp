package com.gregspitz.flashcardapp.addeditflashcard.domain.usecase;

import com.gregspitz.flashcardapp.UseCase;
import com.gregspitz.flashcardapp.data.source.FlashcardDataSource;
import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.data.model.Flashcard;

/**
 * A use case for getting a single {@link Flashcard} by its ID
 */

public class GetFlashcard extends UseCase<GetFlashcard.RequestValues, GetFlashcard.ResponseValue> {

    private FlashcardRepository mFlashcardRepository;

    public GetFlashcard(FlashcardRepository flashcardRepository) {
        mFlashcardRepository = flashcardRepository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        mFlashcardRepository.getFlashcard(requestValues.getFlashcardId(),
                new FlashcardDataSource.GetFlashcardCallback() {
                    @Override
                    public void onFlashcardLoaded(Flashcard flashcard) {
                        getUseCaseCallback().onSuccess(new GetFlashcard.ResponseValue(flashcard));
                    }

                    @Override
                    public void onDataNotAvailable() {
                        getUseCaseCallback().onError();
                    }
                });
    }

    public static class RequestValues implements UseCase.RequestValues {
        private String mFlashcardId;

        public RequestValues(String flashcardId) {
            mFlashcardId = flashcardId;
        }

        public String getFlashcardId() {
            return mFlashcardId;
        }
    }

    public static class ResponseValue implements UseCase.ResponseValue {
        private Flashcard mFlashcard;

        public ResponseValue(Flashcard flashcard) {
            mFlashcard = flashcard;
        }

        public Flashcard getFlashcard() {
            return mFlashcard;
        }
    }
}
