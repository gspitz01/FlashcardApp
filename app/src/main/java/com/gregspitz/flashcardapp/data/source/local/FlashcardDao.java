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

package com.gregspitz.flashcardapp.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
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

    @Query("DELETE FROM flashcard")
    void deleteAll();

}
