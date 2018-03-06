package com.gregspitz.flashcardapp;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;

import com.gregspitz.flashcardapp.addeditflashcard.domain.usecase.GetFlashcard;
import com.gregspitz.flashcardapp.data.FakeFlashcardRemoteDataSource;
import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.randomflashcard.domain.usecase.GetRandomFlashcard;
import com.gregspitz.flashcardapp.flashcardlist.domain.usecase.GetFlashcards;

/**
 * Enables injection of mock implementations for
 * {@link com.gregspitz.flashcardapp.data.source.FlashcardDataSource} at compile time.
 * This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */

public class Injection {
    public static FlashcardRepository provideFlashcardRepository(@NonNull Context context) {
        return FlashcardRepository.getInstance(FakeFlashcardRemoteDataSource.getInstance());
    }

    public static UseCaseHandler provideUseCaseHandler() {
        return UseCaseHandler.getInstance();
    }

    public static GetRandomFlashcard provideGetRandomFlashcard(@NonNull Context context) {
        return new GetRandomFlashcard(provideFlashcardRepository(context));
    }

    public static GetFlashcards provideGetFlashcards(@NonNull Context context) {
        return new GetFlashcards(provideFlashcardRepository(context));
    }

    public static GetFlashcard provideGetFlashcard(@NonNull Context context) {
        return new GetFlashcard(provideFlashcardRepository(context));
    }
}
