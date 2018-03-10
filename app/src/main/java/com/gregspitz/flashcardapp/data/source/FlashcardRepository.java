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

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.gregspitz.flashcardapp.data.model.Flashcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data source for flashcards
 */

public class FlashcardRepository implements FlashcardDataSource {

    // TODO: finish this

    private static FlashcardRepository INSTANCE;

    private final FlashcardDataSource mLocalDataService;
    private final FlashcardDataSource mRemoteDataService;
    private Map<String, Flashcard> mCache;
    private boolean mCacheIsDirty;

    // Prevent direct instantiation
    @VisibleForTesting
    public FlashcardRepository(
            @NonNull FlashcardDataSource localDataService,
            @NonNull FlashcardDataSource remoteDataService) {
        mLocalDataService = localDataService;
        mRemoteDataService = remoteDataService;
        mCache = new HashMap<>();
        mCacheIsDirty = true;
    }

    public static FlashcardRepository getInstance(
            FlashcardDataSource localDataService, FlashcardDataSource remoteDataService) {
        if (INSTANCE == null) {
            INSTANCE = new FlashcardRepository(localDataService, remoteDataService);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getFlashcards(@NonNull GetFlashcardsCallback callback) {
        if (mCacheIsDirty) {
            updateCache();
        }
        callback.onFlashcardsLoaded(new ArrayList<>(mCache.values()));

//        mRemoteDataService.getFlashcards(callback);
    }

    @Override
    public void getFlashcard(@NonNull String flashcardId, @NonNull GetFlashcardCallback callback) {
        mLocalDataService.getFlashcard(flashcardId, callback);
//        mRemoteDataService.getFlashcard(flashcardId, callback);
    }

    @Override
    public void saveFlashcard(@NonNull Flashcard flashcard, @NonNull SaveFlashcardCallback callback) {
        mLocalDataService.saveFlashcard(flashcard, callback);
//        mRemoteDataService.saveFlashcard(flashcard, callback);
    }

    private void updateCache() {
        mLocalDataService.getFlashcards(new GetFlashcardsCallback() {
            @Override
            public void onFlashcardsLoaded(List<Flashcard> flashcards) {
                mCache.clear();
                for (Flashcard flashcard : flashcards) {
                    mCache.put(flashcard.getId(), flashcard);
                }
            }

            @Override
            public void onDataNotAvailable() {
//               throw new FlashcardRepositoryException("Data unavailable from local data source.");
            }
        });
    }

}
