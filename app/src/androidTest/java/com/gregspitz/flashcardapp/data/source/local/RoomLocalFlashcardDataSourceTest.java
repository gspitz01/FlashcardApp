package com.gregspitz.flashcardapp.data.source.local;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.gregspitz.flashcardapp.data.model.Flashcard;
import com.gregspitz.flashcardapp.data.source.FlashcardDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Tests for the implementation of {@link RoomLocalFlashcardDataSource}
 */
@RunWith(AndroidJUnit4.class)
public class RoomLocalFlashcardDataSourceTest {

    // TODO: create test data class and update all tests to use it

    private static final Flashcard FLASHCARD =
            new Flashcard("0", "Front", "Back");

    private RoomLocalFlashcardDataSource mDatabase;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDatabase = RoomLocalFlashcardDataSource.getInstance(context);
    }

    @After
    public void breakdown() {
        RoomLocalFlashcardDataSource.destroyInstance();
    }

    @Test
    public void canAddAndRetrieveFlashcard() {
        saveFlashcardToDatabase(FLASHCARD);

        final List<Flashcard> returnedFlashcards = new ArrayList<>();
        mDatabase.getFlashcards(new FlashcardDataSource.GetFlashcardsCallback() {
            @Override
            public void onFlashcardsLoaded(List<Flashcard> flashcards) {
                returnedFlashcards.addAll(flashcards);
            }

            @Override
            public void onDataNotAvailable() {}
        });

        assertEquals(1, returnedFlashcards.size());
        assertEquals(FLASHCARD, returnedFlashcards.get(0));
    }

    @Test
    public void canRetrieveMultipleFlashcards() {
        saveFlashcardToDatabase(FLASHCARD);
        saveFlashcardToDatabase(new Flashcard("1", "Front", "Back"));
        saveFlashcardToDatabase(new Flashcard("2", "Front", "Back"));
        final List<Flashcard> returnedFlashcards = new ArrayList<>();
        mDatabase.getFlashcards(new FlashcardDataSource.GetFlashcardsCallback() {
            @Override
            public void onFlashcardsLoaded(List<Flashcard> flashcards) {
                returnedFlashcards.addAll(flashcards);
            }

            @Override
            public void onDataNotAvailable() {}
        });

        assertEquals(3, returnedFlashcards.size());
    }

    @Test
    public void canRetrieveById() {
        saveFlashcardToDatabase(FLASHCARD);
        final List<Flashcard> returnedFlashcards = new ArrayList<>();
        mDatabase.getFlashcard(FLASHCARD.getId(), new FlashcardDataSource.GetFlashcardCallback() {
            @Override
            public void onFlashcardLoaded(Flashcard flashcard) {
                returnedFlashcards.add(flashcard);
            }

            @Override
            public void onDataNotAvailable() {}
        });

        assertEquals(1, returnedFlashcards.size());
        assertEquals(FLASHCARD, returnedFlashcards.get(0));
    }

    @Test
    public void conflictingFlashcardIds_willOverwriteExisting() {
        saveFlashcardToDatabase(FLASHCARD);
        final List<Flashcard> returnedFlashcards = new ArrayList<>();
        mDatabase.getFlashcard(FLASHCARD.getId(), new FlashcardDataSource.GetFlashcardCallback() {
            @Override
            public void onFlashcardLoaded(Flashcard flashcard) {
                returnedFlashcards.add(flashcard);
            }

            @Override
            public void onDataNotAvailable() {}
        });
        assertEquals(FLASHCARD, returnedFlashcards.get(0));

        returnedFlashcards.clear();
        Flashcard otherFlashcard =
                new Flashcard(FLASHCARD.getId(), "Different front", "Back");
        saveFlashcardToDatabase(otherFlashcard);
        mDatabase.getFlashcard(otherFlashcard.getId(), new FlashcardDataSource.GetFlashcardCallback() {
            @Override
            public void onFlashcardLoaded(Flashcard flashcard) {
                returnedFlashcards.add(flashcard);
            }

            @Override
            public void onDataNotAvailable() {}
        });

        assertEquals(1, returnedFlashcards.size());
        assertEquals(otherFlashcard, returnedFlashcards.get(0));
    }

    private void saveFlashcardToDatabase(Flashcard flashcard) {
        mDatabase.saveFlashcard(flashcard, new FlashcardDataSource.SaveFlashcardCallback() {
            @Override
            public void onSaveSuccessful() {/* no need to do anything here */}

            @Override
            public void onSaveFailed() {/* nor here */}
        });
    }
}
