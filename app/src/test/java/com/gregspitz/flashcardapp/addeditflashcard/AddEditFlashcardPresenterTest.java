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

import com.gregspitz.flashcardapp.TestUseCaseScheduler;
import com.gregspitz.flashcardapp.UseCaseHandler;
import com.gregspitz.flashcardapp.addeditflashcard.domain.usecase.GetFlashcard;
import com.gregspitz.flashcardapp.addeditflashcard.domain.usecase.SaveFlashcard;
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

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the implementation of {@link AddEditFlashcardPresenter}
 */

public class AddEditFlashcardPresenterTest {

    private static final Flashcard FLASHCARD =
            new Flashcard("0", "Front", "Back");

    @Mock
    private FlashcardRepository mFlashcardRepository;

    @Mock
    private AddEditFlashcardContract.View mView;

    @Captor
    private ArgumentCaptor<FlashcardDataSource.GetFlashcardCallback> mArgumentCaptor;

    @Captor
    private ArgumentCaptor<Flashcard> mSaveFlashcardArgumentCaptor;

    @Captor
    private ArgumentCaptor<FlashcardDataSource.SaveFlashcardCallback>
            mSaveCallbackArgumentCaptor;

    private AddEditFlashcardPresenter mAddEditFlashcardPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        // View is always appears active
        when(mView.isActive()).thenReturn(true);
        // View acts as though it had started with an intent including the flashcard ID
        when(mView.getIdFromIntent()).thenReturn(FLASHCARD.getId());
    }

    @Test
    public void creation_setsPresenterOnView() {
        mAddEditFlashcardPresenter = createPresenter();
        verify(mView).setPresenter(mAddEditFlashcardPresenter);
    }

    @Test
    public void onStart_showsFlashcardInView() {
        createAndStartPresenterAndSetArgumentCaptorForGet();
        InOrder inOrder = inOrder(mView);
        inOrder.verify(mView).showLoadingIndicator(true);
        // Trigger callback with FLASHCARD
        mArgumentCaptor.getValue().onFlashcardLoaded(FLASHCARD);
        inOrder.verify(mView).showLoadingIndicator(false);
        verify(mView).showFlashcard(FLASHCARD);
    }

    @Test
    public void noAvailableFlashcard_showsFailedToLoadInView() {
        createAndStartPresenterAndSetArgumentCaptorForGet();
        InOrder inOrder = inOrder(mView);
        inOrder.verify(mView).showLoadingIndicator(true);
        mArgumentCaptor.getValue().onDataNotAvailable();
        inOrder.verify(mView).showLoadingIndicator(false);
        verify(mView).showFailedToLoadFlashcard();
    }

    @Test
    public void updateFlashcard_savesFlashcardToRepositoryAndShowSuccessInView() {
        mAddEditFlashcardPresenter = createPresenter();
        mAddEditFlashcardPresenter.saveFlashcard(FLASHCARD);
        verify(mFlashcardRepository)
                .saveFlashcard(mSaveFlashcardArgumentCaptor.capture(),
                        mSaveCallbackArgumentCaptor.capture());
        // Trigger successful save on callback
        mSaveCallbackArgumentCaptor.getValue().onSaveSuccessful();
        Flashcard savedFlashcard = mSaveFlashcardArgumentCaptor.getValue();
        assertEquals(FLASHCARD, savedFlashcard);
        verify(mView).showSaveSuccessful();
    }

    @Test
    public void failedSaved_showsFailedSaveInView() {
        mAddEditFlashcardPresenter = createPresenter();
        mAddEditFlashcardPresenter.saveFlashcard(FLASHCARD);
        verify(mFlashcardRepository).saveFlashcard(any(Flashcard.class),
                mSaveCallbackArgumentCaptor.capture());
        // Trigger failed save on callback
        mSaveCallbackArgumentCaptor.getValue().onSaveFailed();
        verify(mView).showSaveFailed();
    }

    @Test
    public void showList_showsListInView() {
        mAddEditFlashcardPresenter = createPresenter();
        mAddEditFlashcardPresenter.showList();
        verify(mView).showFlashcardList();
    }

    private void createAndStartPresenterAndSetArgumentCaptorForGet() {
        createAndStartPresenter();
        verify(mFlashcardRepository).getFlashcard(eq(FLASHCARD.getId()),
                mArgumentCaptor.capture());
    }

    private void createAndStartPresenter() {
        mAddEditFlashcardPresenter = createPresenter();
        mAddEditFlashcardPresenter.start();
    }

    private AddEditFlashcardPresenter createPresenter() {
        UseCaseHandler useCaseHandler = new UseCaseHandler(new TestUseCaseScheduler());
        GetFlashcard getFlashcard = new GetFlashcard(mFlashcardRepository);
        SaveFlashcard saveFlashcard = new SaveFlashcard(mFlashcardRepository);
        return new AddEditFlashcardPresenter(useCaseHandler, mView,
                getFlashcard, saveFlashcard);
    }
}
