package com.gregspitz.flashcardapp.randomflashcard;

import com.gregspitz.flashcardapp.TestUseCaseScheduler;
import com.gregspitz.flashcardapp.UseCaseHandler;
import com.gregspitz.flashcardapp.data.source.FlashcardDataSource;
import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.data.model.Flashcard;
import com.gregspitz.flashcardapp.randomflashcard.domain.usecase.GetRandomFlashcard;

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
        // View always appears active
        when(mFlashcardView.isActive()).thenReturn(true);
    }

    @Test
    public void creation_setsPresenterOnView() {
        mFlashcardPresenter = createPresenter();

        verify(mFlashcardView).setPresenter(mFlashcardPresenter);
    }

    @Test
    public void onStart_loadsAFlashcardAndShowFrontInView() {
        createAndStartPresenterAndSetGetFlashcardsCallbackCaptor();
        InOrder inOrder = inOrder(mFlashcardView);
        inOrder.verify(mFlashcardView).setLoadingIndicator(true);

        //Trigger callback with list of Flashcards
        mCallbackArgumentCaptor.getValue().onFlashcardsLoaded(
                addTestCardToListAndReturnList());

        inOrder.verify(mFlashcardView).setLoadingIndicator(false);
        verify(mFlashcardView).showFlashcardSide(FLASHCARD.getFront());
    }

    @Test
    public void turnFlashcard_showsBackInView() {
        createAndStartPresenterAndSetGetFlashcardsCallbackCaptor();
        mCallbackArgumentCaptor.getValue().onFlashcardsLoaded(
                addTestCardToListAndReturnList());
        mFlashcardPresenter.turnFlashcard();
        verify(mFlashcardView).showFlashcardSide(FLASHCARD.getBack());
    }

    @Test
    public void noFlashcardsToLoad_showsFailedToLoadInView() {
        createAndStartPresenterAndSetGetFlashcardsCallbackCaptor();
        mCallbackArgumentCaptor.getValue().onDataNotAvailable();
        verify(mFlashcardView).showFailedToLoadFlashcard();
    }

    @Test
    public void emptyListOfFlashcards_showsFailedToLoadInView() {
        createAndStartPresenterAndSetGetFlashcardsCallbackCaptor();
        mCallbackArgumentCaptor.getValue().onFlashcardsLoaded(new ArrayList<Flashcard>());
        verify(mFlashcardView).showFailedToLoadFlashcard();
    }

    private void createAndStartPresenterAndSetGetFlashcardsCallbackCaptor() {
        mFlashcardPresenter = createPresenter();
        mFlashcardPresenter.start();
        verify(mFlashcardRepository).getFlashcards(mCallbackArgumentCaptor.capture());
    }

    private List<Flashcard> addTestCardToListAndReturnList() {
        List<Flashcard> cards = new ArrayList<>();
        cards.add(FLASHCARD);
        return cards;
    }

    private FlashcardPresenter createPresenter() {
        UseCaseHandler useCaseHandler = new UseCaseHandler(new TestUseCaseScheduler());
        GetRandomFlashcard getRandomFlashCard = new GetRandomFlashcard(mFlashcardRepository);
        return new FlashcardPresenter(useCaseHandler, mFlashcardView, getRandomFlashCard);
    }
}
