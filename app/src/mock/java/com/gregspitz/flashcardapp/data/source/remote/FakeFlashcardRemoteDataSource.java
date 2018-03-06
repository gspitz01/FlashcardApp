package com.gregspitz.flashcardapp.data.source.remote;

import android.support.annotation.NonNull;

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

    // prevent direct instantiation
    private FakeFlashcardRemoteDataSource() {}

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
