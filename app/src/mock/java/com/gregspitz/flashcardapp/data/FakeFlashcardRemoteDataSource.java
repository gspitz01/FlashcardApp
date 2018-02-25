package com.gregspitz.flashcardapp.data;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.gregspitz.flashcardapp.data.source.FlashcardDataSource;
import com.gregspitz.flashcardapp.flashcard.domain.model.Flashcard;

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
}
