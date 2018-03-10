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
