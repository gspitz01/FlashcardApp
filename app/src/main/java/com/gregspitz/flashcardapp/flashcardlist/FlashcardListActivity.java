package com.gregspitz.flashcardapp.flashcardlist;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gregspitz.flashcardapp.Injection;
import com.gregspitz.flashcardapp.R;
import com.gregspitz.flashcardapp.addeditflashcard.AddEditFlashcardActivity;
import com.gregspitz.flashcardapp.flashcarddetail.FlashcardDetailActivity;
import com.gregspitz.flashcardapp.randomflashcard.domain.model.Flashcard;

import java.util.ArrayList;
import java.util.List;

public class FlashcardListActivity extends AppCompatActivity
        implements FlashcardListContract.View {

    private RecyclerView mFlashcardRecyclerView;
    private FlashcardRecyclerAdapter mFlashcardRecyclerAdapter;
    private FlashcardListContract.Presenter mPresenter;
    private TextView mFailedToLoadText;
    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_list);

        mFlashcardRecyclerView = findViewById(R.id.flashcard_recycler_view);
        mFlashcardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mFlashcardRecyclerAdapter = new FlashcardRecyclerAdapter(new ArrayList<Flashcard>());
        mFlashcardRecyclerView.setAdapter(mFlashcardRecyclerAdapter);
        mFailedToLoadText = findViewById(R.id.no_flashcards_to_show);
        mFloatingActionButton = findViewById(R.id.add_flashcard_fab);

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
        mFlashcardRecyclerAdapter.setPresenter(mPresenter);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addFlashcard();
            }
        });
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        // TODO: create loading indicator
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
        Intent intent = new Intent(this, AddEditFlashcardActivity.class);
        intent.putExtra(AddEditFlashcardActivity.FLASHCARD_ID_EXTRA,
                AddEditFlashcardActivity.NEW_FLASHCARD);
        startActivity(intent);
    }

    @Override
    public void showFlashcardDetailsUi(String flashcardId) {
        Intent intent = new Intent(this, FlashcardDetailActivity.class);
        intent.putExtra(FlashcardDetailActivity.FLASHCARD_ID_EXTRA, flashcardId);
        startActivity(intent);
    }

    @Override
    public boolean isActive() {
        return true;
    }
}
