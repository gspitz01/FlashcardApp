package com.gregspitz.flashcardapp.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.gregspitz.flashcardapp.data.model.Flashcard;

import java.util.List;

/**
 * Database access object for {@link Flashcard}s
 */
@Dao
public interface FlashcardDao {

    @Query("SELECT id, front, back FROM flashcard")
    List<Flashcard> getFlashcards();

    @Query("SELECT id, front, back FROM flashcard " +
        "WHERE id = :flashcardId")
    Flashcard getFlashcard(String flashcardId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Flashcard flashcard);

}
