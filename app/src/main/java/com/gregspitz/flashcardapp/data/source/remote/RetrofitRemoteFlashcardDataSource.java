/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gregspitz.flashcardapp.data.source.remote;

import android.support.annotation.NonNull;

import com.gregspitz.flashcardapp.data.services.FlashcardService;
import com.gregspitz.flashcardapp.data.services.ServiceBuilder;
import com.gregspitz.flashcardapp.data.source.FlashcardDataSource;
import com.gregspitz.flashcardapp.data.model.Flashcard;

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
            public void onResponse(@NonNull Call<List<Flashcard>> call,
                                   @NonNull Response<List<Flashcard>> response) {
                callback.onFlashcardsLoaded(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Flashcard>> call,
                                  @NonNull Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getFlashcard(@NonNull final String flashcardId,
                             @NonNull final GetFlashcardCallback callback) {
        Call<Flashcard> call = mFlashcardService.getFlashcardById(flashcardId);
        call.enqueue(new Callback<Flashcard>() {
            @Override
            public void onResponse(@NonNull Call<Flashcard> call,
                                   @NonNull Response<Flashcard> response) {
                callback.onFlashcardLoaded(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Flashcard> call, @NonNull Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void saveFlashcard(@NonNull Flashcard flashcard,
                              @NonNull final SaveFlashcardCallback callback) {
        Call<Flashcard> call = mFlashcardService.saveFlashcard(flashcard);
        call.enqueue(new Callback<Flashcard>() {
            @Override
            public void onResponse(@NonNull Call<Flashcard> call,
                                   @NonNull Response<Flashcard> response) {
                callback.onSaveSuccessful();
            }

            @Override
            public void onFailure(@NonNull Call<Flashcard> call, @NonNull Throwable t) {
                callback.onSaveFailed();
            }
        });
    }

    @Override
    public void deleteAllFlashcards() {
        // Probably don't want to allow this
    }

    @Override
    public void refreshFlashcards() {
        // Not needed, handled by FlashcardRepository
    }
}
