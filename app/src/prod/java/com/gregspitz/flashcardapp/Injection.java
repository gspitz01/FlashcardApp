package com.gregspitz.flashcardapp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.data.source.RetrofitRemoteFlashcardDataSource;
import com.gregspitz.flashcardapp.flashcard.domain.usecase.GetFlashcard;

/**
 * Production injection
 * {@link com.gregspitz.flashcardapp.data.source.FlashcardDataSource} at compile time.
 */

public class Injection {
    public static FlashcardRepository provideFlashcardRepository(@NonNull Context context) {
        return FlashcardRepository.getInstance(RetrofitRemoteFlashcardDataSource.getInstance());
    }

    public static UseCaseHandler provideUseCaseHandler() {
        return UseCaseHandler.getInstance();
    }

    public static GetFlashcard provideGetFlashcard(@NonNull Context context) {
        return new GetFlashcard(provideFlashcardRepository(context));
    }
}
