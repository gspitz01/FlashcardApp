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

package com.gregspitz.flashcardapp.flashcardlist;

import com.gregspitz.flashcardapp.BasePresenter;
import com.gregspitz.flashcardapp.BaseView;
import com.gregspitz.flashcardapp.data.model.Flashcard;

import java.util.List;

/**
 * A contract between the flashcard list view and its presenter
 */

public interface FlashcardListContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);
        void showFlashcards(List<Flashcard> flashcards);
        void showFailedToLoadFlashcards();
        void showAddFlashcard();
        void showFlashcardDetailsUi(String flashcardId);
        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void selectFlashcard(Flashcard flashcard);
        void addFlashcard();
        void loadFlashcards();
        void onFlashcardClick(String flashcardId);
    }
}
