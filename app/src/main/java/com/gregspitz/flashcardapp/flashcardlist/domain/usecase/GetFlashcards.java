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
