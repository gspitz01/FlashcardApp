package com.gregspitz.flashcardapp.data.source.local;

import android.support.annotation.NonNull;

import com.gregspitz.flashcardapp.data.source.FlashcardDataSource;
import com.gregspitz.flashcardapp.data.model.Flashcard;

/**
 * A fake local data source for testing
 */

public class FakeFlashcardLocalDataSource implements FlashcardDataSource {

    private static FakeFlashcardLocalDataSource INSTANCE;

    private FakeFlashcardLocalDataSource() {}

    public static FakeFlashcardLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeFlashcardLocalDataSource();
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
