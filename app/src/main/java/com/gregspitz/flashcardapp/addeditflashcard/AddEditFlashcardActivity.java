package com.gregspitz.flashcardapp.addeditflashcard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gregspitz.flashcardapp.R;

public class AddEditFlashcardActivity extends AppCompatActivity
        implements AddEditFlashcardContract.View {

    public static final String FLASHCARD_ID_EXTRA = "flashcard_id";
    public static final String NEW_FLASHCARD = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_flashcard);
    }

    @Override
    public void setPresenter(AddEditFlashcardContract.Presenter presenter) {

    }
}
