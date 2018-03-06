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
}
