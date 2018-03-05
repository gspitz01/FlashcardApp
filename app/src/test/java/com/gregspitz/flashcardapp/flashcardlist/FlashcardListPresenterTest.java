package com.gregspitz.flashcardapp.flashcardlist;

import com.gregspitz.flashcardapp.TestUseCaseScheduler;
import com.gregspitz.flashcardapp.UseCaseHandler;
import com.gregspitz.flashcardapp.data.source.FlashcardDataSource;
import com.gregspitz.flashcardapp.data.source.FlashcardRepository;
import com.gregspitz.flashcardapp.randomflashcard.domain.model.Flashcard;
import com.gregspitz.flashcardapp.flashcardlist.domain.usecase.GetFlashcards;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the implementation of {@link FlashcardListPresenter}
 */

public class FlashcardListPresenterTest {

    private static final Flashcard FLASHCARD_1 =
            new Flashcard("0", "A flashcard front", "The back");

    private static final Flashcard FLASHCARD_2 =
            new Flashcard("1", "A different front", "A different back");

    private static List<Flashcard> FLASHCARDS;

    @Mock
    private FlashcardRepository mFlashcardRepository;

    @Mock
    private FlashcardListContract.View mFlashcardListView;

    @Captor
    private ArgumentCaptor<FlashcardDataSource.GetFlashcardsCallback> mCallbackArgumentCaptor;

    private FlashcardListPresenter mFlashcardListPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mFlashcardListView.isActive()).thenReturn(true);
    }

    @Test
    public void creation_setsPresenterOnView() {
        mFlashcardListPresenter = createPresenter();

        verify(mFlashcardListView).setPresenter(mFlashcardListPresenter);
    }

    @Test
    public void startup_showsFlashcardListInView() {
        createAndStartPresenterAndSetGetFlashcardsCallbackCaptor();
        InOrder inOrder = inOrder(mFlashcardListView);
        inOrder.verify(mFlashcardListView).setLoadingIndicator(true);
        mCallbackArgumentCaptor.getValue().onFlashcardsLoaded(
                createAndReturnTestCardsList());
        inOrder.verify(mFlashcardListView).setLoadingIndicator(false);
        verify(mFlashcardListView).showFlashcards(FLASHCARDS);
    }

    @Test
    public void noFlashcardsToLoad_showsFailedToLoadInView() {
        createAndStartPresenterAndSetGetFlashcardsCallbackCaptor();
        mCallbackArgumentCaptor.getValue().onDataNotAvailable();
        verify(mFlashcardListView).showFailedToLoadFlashcards();
    }

    @Test
    public void emptyListOfFlashcards_showsFailedToLoadInView() {
        createAndStartPresenterAndSetGetFlashcardsCallbackCaptor();
        mCallbackArgumentCaptor.getValue().onFlashcardsLoaded(new ArrayList<Flashcard>());
        verify(mFlashcardListView).showFailedToLoadFlashcards();
    }

    @Test
    public void addFlashcard_showsAddFlashcard() {
        createAndStartPresenterAndSetGetFlashcardsCallbackCaptor();
        mFlashcardListPresenter.addFlashcard();
        verify(mFlashcardListView).showAddFlashcard();
    }

    @Test
    public void selectFlashcard_showsFlashcardDetails() {
        createAndStartPresenterAndSetGetFlashcardsCallbackCaptor();
        mFlashcardListPresenter.selectFlashcard(FLASHCARD_1);
        verify(mFlashcardListView).showFlashcardDetailsUi(FLASHCARD_1.getId());
    }

    private List<Flashcard> createAndReturnTestCardsList() {
        FLASHCARDS = Arrays.asList(FLASHCARD_1, FLASHCARD_2);
        return FLASHCARDS;
    }

    private void createAndStartPresenterAndSetGetFlashcardsCallbackCaptor() {
        mFlashcardListPresenter = createPresenter();
        mFlashcardListPresenter.start();
        verify(mFlashcardRepository).getFlashcards(mCallbackArgumentCaptor.capture());
    }

    private FlashcardListPresenter createPresenter() {
        UseCaseHandler useCaseHandler = new UseCaseHandler(new TestUseCaseScheduler());
        GetFlashcards getFlashcards = new GetFlashcards(mFlashcardRepository);
        return new FlashcardListPresenter(useCaseHandler, mFlashcardListView, getFlashcards);
    }
}
