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

/**
 * A fake local data source for testing
 */

public class FakeFlashcardLocalDataSource implements FlashcardDataSource {

    private static FakeFlashcardLocalDataSource INSTANCE;

    /**
     * Constructor, private to avoid instantiation
     * @param context included for consistency with the real version
     */
    @VisibleForTesting
    public FakeFlashcardLocalDataSource(@NonNull Context context) {}

    public static FakeFlashcardLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FakeFlashcardLocalDataSource(context);
        }
        return INSTANCE;
    }
    @Override
    public void getFlashcards(@NonNull GetFlashcardsCallback callback) {
        // TODO: fill this in for testing
    }

    @Override
    public void getFlashcard(@NonNull String flashcardId, @NonNull GetFlashcardCallback callback) {
        // TODO: fill this in for testing
    }

    @Override
    public void saveFlashcard(@NonNull Flashcard flashcard, @NonNull SaveFlashcardCallback callback) {
        // TODO: fill this in for testing
    }
}
