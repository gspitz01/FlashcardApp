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

import com.gregspitz.flashcardapp.UseCase;
import com.gregspitz.flashcardapp.UseCaseHandler;
import com.gregspitz.flashcardapp.addeditflashcard.domain.usecase.GetFlashcard;
import com.gregspitz.flashcardapp.data.model.Flashcard;

/**
 * A presenter for FlashcardDetail view
 */

public class FlashcardDetailPresenter implements FlashcardDetailContract.Presenter {

    private UseCaseHandler mUseCaseHandler;
    private FlashcardDetailContract.View mView;
    private GetFlashcard mGetFlashcard;
    private Flashcard mFlashcard;

    public FlashcardDetailPresenter(
            UseCaseHandler useCaseHandler, FlashcardDetailContract.View view,
            GetFlashcard getFlashcard) {
        mUseCaseHandler = useCaseHandler;
        mView = view;
        mGetFlashcard = getFlashcard;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadFlashcard(mView.getIdFromIntent());
    }

    @Override
    public void loadFlashcard(String flashcardId) {
        mView.showLoadingIndicator(true);

        mUseCaseHandler.execute(mGetFlashcard, new GetFlashcard.RequestValues(flashcardId),
                new UseCase.UseCaseCallback<GetFlashcard.ResponseValue>() {
                    @Override
                    public void onSuccess(GetFlashcard.ResponseValue response) {
                        mFlashcard = response.getFlashcard();
                        if (mView.isActive()) {
                            mView.showLoadingIndicator(false);
                            mView.showFlashcard(mFlashcard);
                        }
                    }

                    @Override
                    public void onError() {
                        if (mView.isActive()) {
                            mView.showLoadingIndicator(false);
                            mView.showFailedToLoadFlashcard();
                        }
                    }
                });
    }

    @Override
    public void editFlashcard() {
        if (mFlashcard != null) {
            mView.showEditFlashcard(mFlashcard.getId());
        }
    }
}
