package com.gregspitz.flashcardapp.flashcardlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gregspitz.flashcardapp.Injection;
import com.gregspitz.flashcardapp.R;
import com.gregspitz.flashcardapp.randomflashcard.domain.model.Flashcard;

import java.util.ArrayList;
import java.util.List;

public class FlashcardListActivity extends AppCompatActivity implements FlashcardListContract.View {

    private RecyclerView mFlashcardRecyclerView;
    private FlashcardRecyclerAdapter mFlashcardRecyclerAdapter;
    private FlashcardListContract.Presenter mPresenter;
    private TextView mFailedToLoadText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_list);

        mFlashcardRecyclerView = findViewById(R.id.flashcard_recycler_view);
        mFlashcardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFlashcardRecyclerAdapter = new FlashcardRecyclerAdapter(new ArrayList<Flashcard>());
        mFlashcardRecyclerView.setAdapter(mFlashcardRecyclerAdapter);
        mFailedToLoadText = findViewById(R.id.no_flashcards_to_show);

        new FlashcardListPresenter(Injection.provideUseCaseHandler(),
                this, Injection.provideGetFlashcards(getApplicationContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(FlashcardListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showFlashcards(List<Flashcard> flashcards) {
        mFailedToLoadText.setVisibility(View.GONE);
        mFlashcardRecyclerAdapter.updateFlashcards(flashcards);
    }

    @Override
    public void showFailedToLoadFlashcards() {
        mFailedToLoadText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAddFlashcard() {

    }

    @Override
    public void showFlashcardDetailsUi(String flashcardId) {

    }

    @Override
    public boolean isActive() {
        return true;
    }
}
