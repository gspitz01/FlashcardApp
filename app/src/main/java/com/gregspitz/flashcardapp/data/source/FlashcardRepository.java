package com.gregspitz.flashcardapp.data.source;

import android.support.annotation.NonNull;

import com.gregspitz.flashcardapp.randomflashcard.domain.model.Flashcard;

/**
 * Data source for flashcards
 */

public class FlashcardRepository implements FlashcardDataSource {

    private static FlashcardRepository INSTANCE;

    private final FlashcardDataSource mRemoteDataService;

    // Prevent direct instantiation
    private FlashcardRepository(@NonNull FlashcardDataSource remoteDataService) {
        mRemoteDataService = remoteDataService;
    }

    public static FlashcardRepository getInstance(FlashcardDataSource remoteDataService) {
        if (INSTANCE == null) {
            INSTANCE = new FlashcardRepository(remoteDataService);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getFlashcards(@NonNull GetFlashcardsCallback callback) {
        mRemoteDataService.getFlashcards(callback);
    }

    @Override
    public void getFlashcard(@NonNull String flashcardId, @NonNull GetFlashcardCallback callback) {
        mRemoteDataService.getFlashcard(flashcardId, callback);
    }

    @Override
    public void saveFlashcard(@NonNull Flashcard flashcard, @NonNull SaveFlashcardCallback callback) {
        // TODO: fill this in
    }
}
