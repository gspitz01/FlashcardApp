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

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

/**
 * Tests for the implementation of {@link RandomFlashcardPresenter}
 */
public class RandomFlashcardPresenterTest {

    private static final Flashcard FLASHCARD_1 =
            new Flashcard("This is the front", "This is the back");
    private static final Flashcard FLASHCARD_2 =
            new Flashcard("Other front", "Other back");

    @Mock
    private FlashcardRepository mFlashcardRepository;

    @Captor
    private ArgumentCaptor<FlashcardDataSource.GetFlashcardsCallback> mCallbackArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> mFlashcardFrontCaptor;

    @Mock
    private RandomFlashcardContract.View mFlashcardView;

    private RandomFlashcardPresenter mRandomFlashcardPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        // View always appears active
        when(mFlashcardView.isActive()).thenReturn(true);
    }

    @Test
    public void creation_setsPresenterOnView() {
        mRandomFlashcardPresenter = createPresenter();

        verify(mFlashcardView).setPresenter(mRandomFlashcardPresenter);
    }

    @Test
    public void onStart_loadsAFlashcardAndShowFrontInView() {
        createAndStartPresenterAndSetGetFlashcardsCallbackCaptor();
        InOrder inOrder = inOrder(mFlashcardView);
        inOrder.verify(mFlashcardView).setLoadingIndicator(true);

        //Trigger callback with list of Flashcards
        mCallbackArgumentCaptor.getValue().onFlashcardsLoaded(
                getSingleFlashcardList());

        inOrder.verify(mFlashcardView).setLoadingIndicator(false);
        verify(mFlashcardView).showFlashcardSide(FLASHCARD_1.getFront());
    }

    @Test
    public void onLoadNewFlashcard_loadsDifferentFlashcard() {
        createAndStartPresenterAndSetGetFlashcardsCallbackCaptor();
        mCallbackArgumentCaptor.getValue().onFlashcardsLoaded(getFlashcardsList());
        mRandomFlashcardPresenter.loadNewFlashcard();
        verify(mFlashcardRepository, times(2))
                .getFlashcards(mCallbackArgumentCaptor.capture());
        mCallbackArgumentCaptor.getValue().onFlashcardsLoaded(getFlashcardsList());
        verify(mFlashcardView, times(2))
                .showFlashcardSide(mFlashcardFrontCaptor.capture());
        String firstFlashcardFrontShown = mFlashcardFrontCaptor.getAllValues().get(0);
        String secondFlashcardFrontShown = mFlashcardFrontCaptor.getAllValues().get(1);
        assertNotEquals(firstFlashcardFrontShown, secondFlashcardFrontShown);
    }

    @Test
    public void turnFlashcard_showsBackInView() {
        createAndStartPresenterAndSetGetFlashcardsCallbackCaptor();
        mCallbackArgumentCaptor.getValue().onFlashcardsLoaded(
                getSingleFlashcardList());
        mRandomFlashcardPresenter.turnFlashcard();
        verify(mFlashcardView).showFlashcardSide(FLASHCARD_1.getBack());
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
        mRandomFlashcardPresenter = createPresenter();
        mRandomFlashcardPresenter.start();
        verify(mFlashcardRepository).getFlashcards(mCallbackArgumentCaptor.capture());
    }

    private List<Flashcard> getSingleFlashcardList() {
        List<Flashcard> cards = new ArrayList<>();
        cards.add(FLASHCARD_1);
        return cards;
    }

    private List<Flashcard> getFlashcardsList() {
        List<Flashcard> cards = new ArrayList<>();
        cards.add(FLASHCARD_1);
        cards.add(FLASHCARD_2);
        return cards;
    }

    private RandomFlashcardPresenter createPresenter() {
        UseCaseHandler useCaseHandler = new UseCaseHandler(new TestUseCaseScheduler());
        GetRandomFlashcard getRandomFlashCard = new GetRandomFlashcard(mFlashcardRepository);
        return new RandomFlashcardPresenter(useCaseHandler, mFlashcardView, getRandomFlashCard);
    }
}
