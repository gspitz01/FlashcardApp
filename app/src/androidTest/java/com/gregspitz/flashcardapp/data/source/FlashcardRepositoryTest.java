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

package com.gregspitz.flashcardapp.data.source;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.gregspitz.flashcardapp.Injection;
import com.gregspitz.flashcardapp.data.model.Flashcard;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the implementation of {@link FlashcardRepository}
 */

@RunWith(AndroidJUnit4.class)
public class FlashcardRepositoryTest {

    private static final Flashcard FLASHCARD_1 =
            new Flashcard("0", "Front1", "Back1");

    private static final Flashcard FLASHCARD_2 =
            new Flashcard("1", "Front2", "Back2");

    private FlashcardRepository mFlashcardRepository;

    @Before
    public void setup() {
        FlashcardRepository.destroyInstance();
        mFlashcardRepository = Injection.provideFlashcardRepository(
                InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void canSaveAndRetrieveFlashcard() {
        assertTrue(saveFlashcardAndReturnSuccess(FLASHCARD_1));

        final List<Flashcard> flashcards = new ArrayList<>();
        mFlashcardRepository.getFlashcard(FLASHCARD_1.getId(),
                new FlashcardDataSource.GetFlashcardCallback() {
                    @Override
                    public void onFlashcardLoaded(Flashcard flashcard) {
                        flashcards.add(flashcard);
                    }

                    @Override
                    public void onDataNotAvailable() {}
                });
        assertEquals(1, flashcards.size());
        assertEquals(FLASHCARD_1, flashcards.get(0));
    }

    @Test
    public void canSaveAndRetrieveMultipleFlashcards() {
        assertTrue(saveFlashcardAndReturnSuccess(FLASHCARD_1));
        assertTrue(saveFlashcardAndReturnSuccess(FLASHCARD_2));
        final List<Flashcard> returnedFlashcards = new ArrayList<>();
        mFlashcardRepository.getFlashcards(new FlashcardDataSource.GetFlashcardsCallback() {
            @Override
            public void onFlashcardsLoaded(List<Flashcard> flashcards) {
                returnedFlashcards.addAll(flashcards);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
        assertEquals(2, returnedFlashcards.size());
        assertEquals(FLASHCARD_1, returnedFlashcards.get(0));
        assertEquals(FLASHCARD_2, returnedFlashcards.get(1));
    }

    private boolean saveFlashcardAndReturnSuccess(Flashcard flashcard) {
        final List<Boolean> saveSuccess = new ArrayList<>();
        mFlashcardRepository.saveFlashcard(flashcard,
                new FlashcardDataSource.SaveFlashcardCallback() {
                    @Override
                    public void onSaveSuccessful() {
                        saveSuccess.add(true);
                    }

                    @Override
                    public void onSaveFailed() {
                        saveSuccess.add(false);
                    }
                });
        return saveSuccess.get(0);
    }
}
