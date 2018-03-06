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
