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

package com.gregspitz.flashcardapp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gregspitz.flashcardapp.addeditflashcard.domain.usecase.GetFlashcard;
import com.gregspitz.flashcardapp.addeditflashcard.domain.usecase.SaveFlashcard;
import com.gregspitz.flashcardapp.data.source.local.FakeFlashcardLocalDataSource;
import com.gregspitz.flashcardapp.data.source.remote.FakeFlashcardRemoteDataSource;
import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.randomflashcard.domain.usecase.GetRandomFlashcard;
import com.gregspitz.flashcardapp.flashcardlist.domain.usecase.GetFlashcards;

/**
 * Enables injection of mock implementations for
 * {@link com.gregspitz.flashcardapp.data.source.FlashcardDataSource} at compile time.
 * This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */

public class Injection {

    private static UseCaseScheduler sScheduler = new UseCaseThreadPoolScheduler();

    public static FlashcardRepository provideFlashcardRepository(@NonNull Context context) {
        return FlashcardRepository.getInstance(
                FakeFlashcardLocalDataSource.getInstance(),
                FakeFlashcardRemoteDataSource.getInstance());
    }

    private static UseCaseScheduler provideScheduler() {
        return sScheduler;
    }

    public static void setScheduler(UseCaseScheduler useCaseScheduler) {
        sScheduler = useCaseScheduler;
    }

    public static UseCaseHandler provideUseCaseHandler() {
        return UseCaseHandler.getInstance(provideScheduler());
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
