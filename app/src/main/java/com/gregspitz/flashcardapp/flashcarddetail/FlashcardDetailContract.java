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

package com.gregspitz.flashcardapp.flashcarddetail;

import com.gregspitz.flashcardapp.BasePresenter;
import com.gregspitz.flashcardapp.BaseView;
import com.gregspitz.flashcardapp.data.model.Flashcard;

/**
 * A contract between the FlashcardDetail view and its presenter
 */

public interface FlashcardDetailContract {

    interface View extends BaseView<Presenter> {
        void showLoadingIndicator(boolean active);
        void showFlashcard(Flashcard flashcard);
        void showEditFlashcard(String flashcardId);
        void showFailedToLoadFlashcard();
        String getIdFromIntent();
        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void loadFlashcard(String flashcardId);
        void editFlashcard();
    }
}
