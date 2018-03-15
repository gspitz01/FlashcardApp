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

package com.gregspitz.flashcardapp.data.source;

import android.support.annotation.NonNull;

import com.gregspitz.flashcardapp.data.model.Flashcard;

import java.util.List;

/**
 * Interface for flashcard data sources
 */

public interface FlashcardDataSource {

    interface GetFlashcardsCallback {

        void onFlashcardsLoaded(List<Flashcard> flashcards);

        void onDataNotAvailable();

    }

    void getFlashcards(@NonNull GetFlashcardsCallback callback);

    interface GetFlashcardCallback {

        void onFlashcardLoaded(Flashcard flashcard);

        void onDataNotAvailable();
    }

    void getFlashcard(@NonNull String flashcardId, @NonNull GetFlashcardCallback callback);

    interface SaveFlashcardCallback {

        void onSaveSuccessful();

        void onSaveFailed();

    }

    void saveFlashcard(@NonNull Flashcard flashcard, @NonNull SaveFlashcardCallback callback);

    void deleteAllFlashcards();

    void refreshFlashcards();
}
