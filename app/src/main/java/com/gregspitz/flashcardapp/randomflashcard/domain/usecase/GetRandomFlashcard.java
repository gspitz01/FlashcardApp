/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gregspitz.flashcardapp.randomflashcard.domain.usecase;

import com.gregspitz.flashcardapp.UseCase;
import com.gregspitz.flashcardapp.data.source.FlashcardDataSource;
import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.data.model.Flashcard;

import java.util.List;
import java.util.Random;

/**
 * A use case for getting a single random flashcard
 */

public class GetRandomFlashcard extends UseCase<GetRandomFlashcard.RequestValues, GetRandomFlashcard.ResponseValue> {

    private FlashcardRepository mFlashcardRepository;
    private Random mRandom;

    public GetRandomFlashcard(FlashcardRepository flashcardRepository) {
        mFlashcardRepository = flashcardRepository;
        mRandom = new Random();
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        mFlashcardRepository.getFlashcards(new FlashcardDataSource.GetFlashcardsCallback() {
            @Override
            public void onFlashcardsLoaded(List<Flashcard> flashcards) {
                if (flashcards != null && flashcards.size() > 0) {
                    int randInt = mRandom.nextInt(flashcards.size());
                    getUseCaseCallback().onSuccess(
                            new ResponseValue(flashcards.get(randInt)));
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
