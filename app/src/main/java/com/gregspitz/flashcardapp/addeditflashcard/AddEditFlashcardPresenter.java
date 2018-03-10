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

package com.gregspitz.flashcardapp.addeditflashcard;

import com.gregspitz.flashcardapp.UseCase;
import com.gregspitz.flashcardapp.UseCaseHandler;
import com.gregspitz.flashcardapp.addeditflashcard.domain.usecase.GetFlashcard;
import com.gregspitz.flashcardapp.addeditflashcard.domain.usecase.SaveFlashcard;
import com.gregspitz.flashcardapp.data.model.Flashcard;

/**
 * A presenter for adding and editing {@link Flashcard}s
 */

public class AddEditFlashcardPresenter implements AddEditFlashcardContract.Presenter {

    private UseCaseHandler mUseCaseHandler;
    private AddEditFlashcardContract.View mView;
    private GetFlashcard mGetFlashcard;
    private SaveFlashcard mSaveFlashcard;
    private Flashcard mFlashcard;

    public AddEditFlashcardPresenter(
            UseCaseHandler useCaseHandler, AddEditFlashcardContract.View view,
            GetFlashcard getFlashcard, SaveFlashcard saveFlashcard) {
        mUseCaseHandler = useCaseHandler;
        mView = view;
        mGetFlashcard = getFlashcard;
        mSaveFlashcard = saveFlashcard;
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
    public void saveFlashcard(Flashcard flashcard) {
        mUseCaseHandler.execute(mSaveFlashcard, new SaveFlashcard.RequestValues(flashcard),
                new UseCase.UseCaseCallback<SaveFlashcard.ResponseValue>() {
                    @Override
                    public void onSuccess(SaveFlashcard.ResponseValue response) {
                        if (mView.isActive()) {
                            mView.showSaveSuccessful();
                            mView.showFlashcardList();
                        }
                    }

                    @Override
                    public void onError() {
                        if (mView.isActive()) {
                            mView.showSaveFailed();
                        }
                    }
                });
    }

    @Override
    public void showList() {
        mView.showFlashcardList();
    }
}
