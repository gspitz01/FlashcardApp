package com.gregspitz.flashcardapp.data.source;

import android.support.annotation.NonNull;

import com.gregspitz.flashcardapp.randomflashcard.domain.model.Flashcard;

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
}
