package com.gregspitz.flashcardapp.data.source;

import android.support.annotation.NonNull;

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

    @Override
    public void getFlashcards(@NonNull GetFlashcardsCallback callback) {
        mRemoteDataService.getFlashcards(callback);
    }
}
