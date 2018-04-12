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

package com.gregspitz.flashcardapp.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.gregspitz.flashcardapp.data.source.FlashcardDataSource;
import com.gregspitz.flashcardapp.data.model.Flashcard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A fake local data source for testing
 */

public class FakeFlashcardLocalDataSource implements FlashcardDataSource {

    private static FakeFlashcardLocalDataSource INSTANCE;

    private Map<String, Flashcard> mDatabase;

    /**
     * Constructor, private to avoid instantiation
     */
    @VisibleForTesting
    public FakeFlashcardLocalDataSource() {
        mDatabase = new HashMap<>();
    }

    public static FakeFlashcardLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeFlashcardLocalDataSource();
        }
        return INSTANCE;
    }
    @Override
    public void getFlashcards(@NonNull GetFlashcardsCallback callback) {
        callback.onFlashcardsLoaded(new ArrayList<>(mDatabase.values()));
    }

    @Override
    public void getFlashcard(@NonNull String flashcardId, @NonNull GetFlashcardCallback callback) {
        Flashcard flashcard = mDatabase.get(flashcardId);
        if (flashcard == null) {
            callback.onDataNotAvailable();
        } else {
            callback.onFlashcardLoaded(flashcard);
        }
    }

    @Override
    public void saveFlashcard(@NonNull Flashcard flashcard, @NonNull SaveFlashcardCallback callback) {
        mDatabase.put(flashcard.getId(), flashcard);
        callback.onSaveSuccessful();
    }

    @Override
    public void deleteAllFlashcards() {
        mDatabase.clear();
    }

    @Override
    public void refreshFlashcards() {
        // Not needed, handled by FlashcardRepository
    }

    public void addFlashcards(Flashcard... flashcards) {
        for (Flashcard flashcard : flashcards) {
            mDatabase.put(flashcard.getId(), flashcard);
        }
    }
}
