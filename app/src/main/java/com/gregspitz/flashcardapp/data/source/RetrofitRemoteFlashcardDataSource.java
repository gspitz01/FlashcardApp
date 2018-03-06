package com.gregspitz.flashcardapp.data.source;

import android.support.annotation.NonNull;

import com.gregspitz.flashcardapp.data.services.FlashcardService;
import com.gregspitz.flashcardapp.data.services.ServiceBuilder;
import com.gregspitz.flashcardapp.randomflashcard.domain.model.Flashcard;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Flashcard data source using Retrofit
 */
public class RetrofitRemoteFlashcardDataSource implements FlashcardDataSource {

    private static RetrofitRemoteFlashcardDataSource INSTANCE;

    private FlashcardService mFlashcardService;

    private RetrofitRemoteFlashcardDataSource() {
        mFlashcardService = ServiceBuilder.buildService(FlashcardService.class);
    }

    public static RetrofitRemoteFlashcardDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RetrofitRemoteFlashcardDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getFlashcards(@NonNull final GetFlashcardsCallback callback) {
        Call<List<Flashcard>> call = mFlashcardService.getFlashcards();
        call.enqueue(new Callback<List<Flashcard>>() {
            @Override
            public void onResponse(Call<List<Flashcard>> call, Response<List<Flashcard>> response) {
                callback.onFlashcardsLoaded(response.body());
            }

            @Override
            public void onFailure(Call<List<Flashcard>> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getFlashcard(@NonNull String flashcardId, @NonNull GetFlashcardCallback callback) {
        // TODO: fill this in
    }

    @Override
    public void saveFlashcard(@NonNull Flashcard flashcard, @NonNull SaveFlashcardCallback callback) {
        // TODO: fill this in
    }
}
