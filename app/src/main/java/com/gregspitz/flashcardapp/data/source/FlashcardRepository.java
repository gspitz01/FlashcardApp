package com.gregspitz.flashcardapp.data.source;

import android.support.annotation.NonNull;

import com.gregspitz.flashcardapp.data.model.Flashcard;

/**
 * Data source for flashcards
 */

public class FlashcardRepository implements FlashcardDataSource {

    private static FlashcardRepository INSTANCE;

    private final FlashcardDataSource mLocalDataService;
    private final FlashcardDataSource mRemoteDataService;

    // Prevent direct instantiation
    private FlashcardRepository(
            @NonNull FlashcardDataSource localDataService,
            @NonNull FlashcardDataSource remoteDataService) {
        mLocalDataService = localDataService;
        mRemoteDataService = remoteDataService;
    }

    public static FlashcardRepository getInstance(
            FlashcardDataSource localDataService, FlashcardDataSource remoteDataService) {
        if (INSTANCE == null) {
            INSTANCE = new FlashcardRepository(localDataService, remoteDataService);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getFlashcards(@NonNull GetFlashcardsCallback callback) {
        // TODO: update this to use local service
        mRemoteDataService.getFlashcards(callback);
    }

    @Override
    public void getFlashcard(@NonNull String flashcardId, @NonNull GetFlashcardCallback callback) {
        // TODO: update this to use local service
        mRemoteDataService.getFlashcard(flashcardId, callback);
    }

    @Override
    public void saveFlashcard(@NonNull Flashcard flashcard, @NonNull SaveFlashcardCallback callback) {
        // TODO: fill this in
    }
}
