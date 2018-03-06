package com.gregspitz.flashcardapp.flashcardlist.domain.usecase;

import com.gregspitz.flashcardapp.UseCase;
import com.gregspitz.flashcardapp.data.source.FlashcardDataSource;
import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.data.model.Flashcard;

import java.util.List;

/**
 * A use case for retrieving all available flashcards
 */

public class GetFlashcards extends UseCase<GetFlashcards.RequestValues, GetFlashcards.ResponseValue> {

    private FlashcardRepository mFlashcardRepository;

    public GetFlashcards(FlashcardRepository flashcardRepository) {
        mFlashcardRepository = flashcardRepository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        mFlashcardRepository.getFlashcards(new FlashcardDataSource.GetFlashcardsCallback() {
            @Override
            public void onFlashcardsLoaded(List<Flashcard> flashcards) {
                if (flashcards.size() > 0) {
                    getUseCaseCallback().onSuccess(new ResponseValue(flashcards));
                } else {
                    getUseCaseCallback().onError();
                }
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {}

    public static final class ResponseValue implements UseCase.ResponseValue {
        private List<Flashcard> mFlashcards;

        public ResponseValue(List<Flashcard> flashcards) {
            mFlashcards = flashcards;
        }

        public List<Flashcard> getFlashcards() {
            return mFlashcards;
        }
    }
}
