package com.gregspitz.flashcardapp.data.services;

import com.gregspitz.flashcardapp.randomflashcard.domain.model.Flashcard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * A Retrofit service to retrieve flashcards
 */
public interface FlashcardService {

    @GET("flashcards")
    Call<List<Flashcard>> getFlashcards();
}
