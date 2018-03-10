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

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.gregspitz.flashcardapp.data.source.FlashcardDataSource;
import com.gregspitz.flashcardapp.data.model.Flashcard;

/**
 * A Room-based local data source for {@link Flashcard}s
 */
@Database(entities = {Flashcard.class}, version = 1)
public abstract class RoomLocalFlashcardDataSource
        extends RoomDatabase implements FlashcardDataSource{

    private static RoomLocalFlashcardDataSource INSTANCE;

    public abstract FlashcardDao flashcardModel();

    public static RoomLocalFlashcardDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                    RoomLocalFlashcardDataSource.class).build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getFlashcards(@NonNull GetFlashcardsCallback callback) {
        callback.onFlashcardsLoaded(flashcardModel().getFlashcards());
    }

    @Override
    public void getFlashcard(@NonNull String flashcardId, @NonNull GetFlashcardCallback callback) {
        callback.onFlashcardLoaded(flashcardModel().getFlashcard(flashcardId));
    }

    @Override
    public void saveFlashcard(
            @NonNull Flashcard flashcard, @NonNull SaveFlashcardCallback callback) {
        INSTANCE.flashcardModel().insert(flashcard);
        callback.onSaveSuccessful();
    }
}
