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

package com.gregspitz.flashcardapp.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.collect.Lists;
import com.gregspitz.flashcardapp.data.source.FlashcardDataSource;
import com.gregspitz.flashcardapp.data.model.Flashcard;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of a remote data source with static access to the data for easy testing
 */

public class FakeFlashcardRemoteDataSource implements FlashcardDataSource {

    private static FakeFlashcardRemoteDataSource INSTANCE;

    private static final Map<String, Flashcard> FLASHCARD_SERVICE_DATA = new LinkedHashMap<>();

    public static final String FAILED_SAVE_FRONT = "failed_save";

    // prevent direct instantiation
    @VisibleForTesting
    public FakeFlashcardRemoteDataSource() {}

    public static FakeFlashcardRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeFlashcardRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getFlashcards(@NonNull GetFlashcardsCallback callback) {
        callback.onFlashcardsLoaded(Lists.newArrayList(FLASHCARD_SERVICE_DATA.values()));
    }

    @Override
    public void getFlashcard(@NonNull String flashcardId, @NonNull GetFlashcardCallback callback) {
        if (FLASHCARD_SERVICE_DATA.get(flashcardId) == null) {
            callback.onDataNotAvailable();
        } else {
            callback.onFlashcardLoaded(FLASHCARD_SERVICE_DATA.get(flashcardId));
        }
    }

    @Override
    public void saveFlashcard(@NonNull Flashcard flashcard, @NonNull SaveFlashcardCallback callback) {
        if (flashcard.getFront().equals(FAILED_SAVE_FRONT)) {
            callback.onSaveFailed();
        } else {
            if (FLASHCARD_SERVICE_DATA.get(flashcard.getId()) != null) {
                FLASHCARD_SERVICE_DATA.remove(flashcard.getId());
            }
            FLASHCARD_SERVICE_DATA.put(flashcard.getId(), flashcard);
            callback.onSaveSuccessful();
        }
    }

    public void addFlashcards(Flashcard... flashcards) {
        for (Flashcard flashcard : flashcards) {
            FLASHCARD_SERVICE_DATA.put(flashcard.getId(), flashcard);
        }
    }

    public void clearFlashcards() {
        FLASHCARD_SERVICE_DATA.clear();
    }
}
