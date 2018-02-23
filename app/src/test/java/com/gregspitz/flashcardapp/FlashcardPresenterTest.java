package com.gregspitz.flashcardapp;

import com.gregspitz.flashcardapp.data.source.FlashcardDataSource;
import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.flashcard.FlashcardContract;
import com.gregspitz.flashcardapp.flashcard.FlashcardPresenter;
import com.gregspitz.flashcardapp.flashcard.domain.model.Flashcard;
import com.gregspitz.flashcardapp.flashcard.domain.usecase.GetFlashcard;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

/**
 * Tests for the implementation of {@link FlashcardPresenter}
 */
public class FlashcardPresenterTest {

    // TODO: make more tests for this, specifically sad cases

    private static final Flashcard FLASHCARD = new Flashcard("This is the front", "This is the back");

    @Mock
    private FlashcardRepository mFlashcardRepository;

    @Captor
    private ArgumentCaptor<FlashcardDataSource.GetFlashcardsCallback> mCallbackArgumentCaptor;

    @Mock
    private FlashcardContract.View mFlashcardView;

    private FlashcardPresenter mFlashcardPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mFlashcardView.isActive()).thenReturn(true);
    }

    @Test
    public void creation_setsPresenterOnView() {
        mFlashcardPresenter = createPresenter();

        verify(mFlashcardView).setPresenter(mFlashcardPresenter);
    }

    @Test
    public void onStart_loadsAFlashcard() {
        mFlashcardPresenter = createPresenter();
        mFlashcardPresenter.start();

        verify(mFlashcardRepository).getFlashcards(mCallbackArgumentCaptor.capture());
        InOrder inOrder = inOrder(mFlashcardView);
        inOrder.verify(mFlashcardView).setLoadingIndicator(true);

        //Trigger callback
        List<Flashcard> cards = new ArrayList<>();
        cards.add(FLASHCARD);
        mCallbackArgumentCaptor.getValue().onFlashcardsLoaded(cards);

        inOrder.verify(mFlashcardView).setLoadingIndicator(false);
        verify(mFlashcardView).showFlashcardFront(FLASHCARD);
    }

    private FlashcardPresenter createPresenter() {
        UseCaseHandler useCaseHandler = new UseCaseHandler(new TestUseCaseScheduler());
        GetFlashcard getFlashCard = new GetFlashcard(mFlashcardRepository);
        return new FlashcardPresenter(useCaseHandler, mFlashcardView, getFlashCard);
    }
}
