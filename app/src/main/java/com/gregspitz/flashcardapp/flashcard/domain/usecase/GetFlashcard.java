package com.gregspitz.flashcardapp.flashcard.domain.usecase;

import android.util.Log;

import com.gregspitz.flashcardapp.UseCase;
import com.gregspitz.flashcardapp.data.source.FlashcardDataSource;
import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.flashcard.domain.model.Flashcard;

import java.util.List;
import java.util.Random;

/**
 * A use case for getting a single flashcard
 */

public class GetFlashcard extends UseCase<GetFlashcard.RequestValues, GetFlashcard.ResponseValue> {

    private FlashcardRepository mFlashcardRepository;
    private Random mRandom;

    public GetFlashcard(FlashcardRepository flashcardRepository) {
        mFlashcardRepository = flashcardRepository;
        mRandom = new Random();
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        mFlashcardRepository.getFlashcards(new FlashcardDataSource.GetFlashcardsCallback() {
            @Override
            public void onFlashcardsLoaded(List<Flashcard> flashcards) {
                if (flashcards != null && flashcards.size() > 0) {
                    getUseCaseCallback().onSuccess(
                            new ResponseValue(flashcards.get(
                                    mRandom.nextInt(flashcards.size()))));
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
        private Flashcard mFlashcard;

        public ResponseValue(Flashcard flashcard) {
            mFlashcard = flashcard;
        }

        public Flashcard getFlashcard() {
            return mFlashcard;
        }
    }
}
