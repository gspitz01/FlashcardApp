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
import android.util.Log;

import com.gregspitz.flashcardapp.data.model.Flashcard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data source for flashcards
 */

public class FlashcardRepository implements FlashcardDataSource {

    private static FlashcardRepository INSTANCE;

    private final FlashcardDataSource mLocalDataService;
    private final FlashcardDataSource mRemoteDataService;

    // Cache is lazily created
    private Map<String, Flashcard> mCache;
    private boolean mCacheIsDirty = false;

    // Prevent direct instantiation
    private FlashcardRepository(
            @NonNull FlashcardDataSource localDataService,
            @NonNull FlashcardDataSource remoteDataService) {
        mLocalDataService = localDataService;
        mRemoteDataService = remoteDataService;
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
    public void getFlashcards(@NonNull final GetFlashcardsCallback callback) {
        if (mCache != null && !mCacheIsDirty) {
            callback.onFlashcardsLoaded(new ArrayList<>(mCache.values()));
            return;
        }

        if (mCacheIsDirty) {
            getFlashcardsFromRemoteDataService(callback);
        } else {
            mLocalDataService.getFlashcards(new GetFlashcardsCallback() {
                @Override
                public void onFlashcardsLoaded(List<Flashcard> flashcards) {
                    refreshCache(flashcards);
                    callback.onFlashcardsLoaded(flashcards);
                }

                @Override
                public void onDataNotAvailable() {
                    getFlashcardsFromRemoteDataService(callback);
                }
            });
        }
    }

    @Override
    public void getFlashcard(@NonNull final String flashcardId, @NonNull final GetFlashcardCallback callback) {

        // First look in the cache
        Flashcard flashcard = getFlashcardWithId(flashcardId);
        if (flashcard != null) {
            callback.onFlashcardLoaded(flashcard);
            return;
        }

        // If it's not in the cache
        // look in the local data source
        // if it's not in there, check the network
        mLocalDataService.getFlashcard(flashcardId, new GetFlashcardCallback() {
            @Override
            public void onFlashcardLoaded(Flashcard flashcard) {
                if (mCache == null) {
                    mCache = new HashMap<>();
                }
                mCache.put(flashcard.getId(), flashcard);
                callback.onFlashcardLoaded(flashcard);
            }

            @Override
            public void onDataNotAvailable() {
                mRemoteDataService.getFlashcard(flashcardId, new GetFlashcardCallback() {
                    @Override
                    public void onFlashcardLoaded(Flashcard flashcard) {
                        if (mCache == null) {
                            mCache = new HashMap<>();
                        }
                        mCache.put(flashcard.getId(), flashcard);
                        callback.onFlashcardLoaded(flashcard);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });

    }

    private Flashcard getFlashcardWithId(@NonNull String flashcardId) {
        if (mCache == null || mCache.isEmpty()) {
            return null;
        } else {
            return mCache.get(flashcardId);
        }
    }

    @Override
    public void saveFlashcard(@NonNull Flashcard flashcard, @NonNull SaveFlashcardCallback callback) {
        // Only save to remote data service
        // When a get is called it will update local and cache
        // This is not particularly efficient
        mRemoteDataService.saveFlashcard(flashcard, callback);
        mCacheIsDirty = true;
    }

    @Override
    public void refreshFlashcards() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllFlashcards() {
        // Don't allow this function for now
        // TODO: create an Exception for this
    }

    private void getFlashcardsFromRemoteDataService(@NonNull final GetFlashcardsCallback callback) {
        mRemoteDataService.getFlashcards(new GetFlashcardsCallback() {
            @Override
            public void onFlashcardsLoaded(List<Flashcard> flashcards) {
                refreshCache(flashcards);
                refreshLocalDataService(flashcards);
                callback.onFlashcardsLoaded(flashcards);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Flashcard> flashcards) {
        if (mCache == null) {
            mCache = new HashMap<>();
        }
        mCache.clear();
        for (Flashcard flashcard : flashcards) {
            mCache.put(flashcard.getId(), flashcard);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataService(List<Flashcard> flashcards) {
        mLocalDataService.deleteAllFlashcards();
        for (Flashcard flashcard : flashcards) {
            mLocalDataService.saveFlashcard(flashcard, new SaveFlashcardCallback() {
                @Override
                public void onSaveSuccessful() {
                    // TODO: put log statement here
                }

                @Override
                public void onSaveFailed() {
                    // TODO: put log statement here
                }
            });
        }
    }

}
