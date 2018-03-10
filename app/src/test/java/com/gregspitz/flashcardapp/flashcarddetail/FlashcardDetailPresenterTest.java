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

import com.gregspitz.flashcardapp.TestUseCaseScheduler;
import com.gregspitz.flashcardapp.UseCaseHandler;
import com.gregspitz.flashcardapp.addeditflashcard.domain.usecase.GetFlashcard;
import com.gregspitz.flashcardapp.data.source.FlashcardDataSource;
import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.data.model.Flashcard;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the implementation of {@link FlashcardDetailPresenter}
 */

public class FlashcardDetailPresenterTest {

    private static final Flashcard FLASHCARD =
            new Flashcard("0", "Front", "Back");

    @Mock
    private FlashcardRepository mFlashcardRepository;

    @Mock
    private FlashcardDetailContract.View mView;

    @Captor
    ArgumentCaptor<FlashcardDataSource.GetFlashcardCallback> mArgumentCaptor;

    private FlashcardDetailPresenter mFlashcardDetailPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        // View always appears active
        when(mView.isActive()).thenReturn(true);
        // View acts as though it was started with an intent which included the flashcard ID
        when(mView.getIdFromIntent()).thenReturn(FLASHCARD.getId());
    }

    @Test
    public void createPresenter_setsPresenterOnView() {
        mFlashcardDetailPresenter = createPresenter();
        verify(mView).setPresenter(mFlashcardDetailPresenter);
    }

    @Test
    public void loadFlashcard_displaysFlashcardInView() {
        createAndStartPresenterAndSetArgumentCaptor();

        InOrder inOrder = inOrder(mView);
        inOrder.verify(mView).showLoadingIndicator(true);

        mArgumentCaptor.getValue().onFlashcardLoaded(FLASHCARD);

        inOrder.verify(mView).showLoadingIndicator(false);
        verify(mView).showFlashcard(FLASHCARD);
    }

    @Test
    public void noAvailableFlashcard_displaysFailedToLoadInView() {
        createAndStartPresenterAndSetArgumentCaptor();

        InOrder inOrder = inOrder(mView);
        inOrder.verify(mView).showLoadingIndicator(true);

        mArgumentCaptor.getValue().onDataNotAvailable();

        inOrder.verify(mView).showLoadingIndicator(false);
        verify(mView).showFailedToLoadFlashcard();
    }

    @Test
    public void editFlashcard_tellsViewToShowEditView() {
        createAndStartPresenterAndSetArgumentCaptor();
        mArgumentCaptor.getValue().onFlashcardLoaded(FLASHCARD);
        mFlashcardDetailPresenter.editFlashcard();
        verify(mView).showEditFlashcard(FLASHCARD.getId());
    }

    private void createAndStartPresenterAndSetArgumentCaptor() {
        mFlashcardDetailPresenter = createPresenter();
        mFlashcardDetailPresenter.start();
        verify(mFlashcardRepository)
                .getFlashcard(eq(FLASHCARD.getId()), mArgumentCaptor.capture());
    }

    private FlashcardDetailPresenter createPresenter() {
        UseCaseHandler useCaseHandler = new UseCaseHandler(new TestUseCaseScheduler());
        GetFlashcard getFlashcard = new GetFlashcard(mFlashcardRepository);
        return new FlashcardDetailPresenter(useCaseHandler, mView, getFlashcard);
    }
}
