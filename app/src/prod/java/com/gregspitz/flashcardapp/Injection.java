package com.gregspitz.flashcardapp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gregspitz.flashcardapp.addeditflashcard.domain.usecase.GetFlashcard;
import com.gregspitz.flashcardapp.addeditflashcard.domain.usecase.SaveFlashcard;
import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.data.source.local.RoomLocalFlashcardDataSource;
import com.gregspitz.flashcardapp.data.source.remote.RetrofitRemoteFlashcardDataSource;
import com.gregspitz.flashcardapp.flashcardlist.domain.usecase.GetFlashcards;
import com.gregspitz.flashcardapp.randomflashcard.domain.usecase.GetRandomFlashcard;

/**
 * Production injection
 * {@link com.gregspitz.flashcardapp.data.source.FlashcardDataSource} at compile time.
 */

public class Injection {
    public static FlashcardRepository provideFlashcardRepository(@NonNull Context context) {
        return FlashcardRepository.getInstance(provideRoomLocalFlashcardDataSource(context),
                RetrofitRemoteFlashcardDataSource.getInstance());
    }

    public static RoomLocalFlashcardDataSource provideRoomLocalFlashcardDataSource(
            @NonNull Context context) {
        return RoomLocalFlashcardDataSource.getInstance(context);
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

    public static SaveFlashcard provideSaveFlashcard(@NonNull Context context) {
        return new SaveFlashcard(provideFlashcardRepository(context));
    }
}
