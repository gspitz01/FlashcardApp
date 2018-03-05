package com.gregspitz.flashcardapp.flashcarddetail;

import com.gregspitz.flashcardapp.TestUseCaseScheduler;
import com.gregspitz.flashcardapp.UseCaseHandler;
import com.gregspitz.flashcardapp.addeditflashcard.domain.usecase.GetFlashcard;
import com.gregspitz.flashcardapp.data.source.FlashcardDataSource;
import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.randomflashcard.domain.model.Flashcard;

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

        when(mView.isActive()).thenReturn(true);
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
