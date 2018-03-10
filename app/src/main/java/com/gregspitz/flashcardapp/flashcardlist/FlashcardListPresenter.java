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

import com.gregspitz.flashcardapp.UseCase;
import com.gregspitz.flashcardapp.UseCaseHandler;
import com.gregspitz.flashcardapp.data.model.Flashcard;
import com.gregspitz.flashcardapp.flashcardlist.domain.usecase.GetFlashcards;

/**
 * A presenter for a FlashcardListView
 */

public class FlashcardListPresenter implements FlashcardListContract.Presenter {

    private final UseCaseHandler mUseCaseHandler;
    private final FlashcardListContract.View mView;
    private final GetFlashcards mGetFlashcards;

    public FlashcardListPresenter(
            UseCaseHandler useCaseHandler, FlashcardListContract.View view,
            GetFlashcards getFlashcards) {
        mUseCaseHandler = useCaseHandler;
        mView = view;
        mGetFlashcards = getFlashcards;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadFlashcards();
    }

    @Override
    public void selectFlashcard(Flashcard flashcard) {
        mView.showFlashcardDetailsUi(flashcard.getId());
    }

    @Override
    public void addFlashcard() {
        mView.showAddFlashcard();
    }

    @Override
    public void loadFlashcards() {
        mView.setLoadingIndicator(true);

        mUseCaseHandler.execute(mGetFlashcards, new GetFlashcards.RequestValues(),
                new UseCase.UseCaseCallback<GetFlashcards.ResponseValue>() {
                    @Override
                    public void onSuccess(GetFlashcards.ResponseValue response) {
                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);
                            mView.showFlashcards(response.getFlashcards());
                        }
                    }

                    @Override
                    public void onError() {
                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);
                            mView.showFailedToLoadFlashcards();
                        }
                    }
                });
    }

    @Override
    public void onFlashcardClick(String flashcardId) {
        mView.showFlashcardDetailsUi(flashcardId);
    }
}
