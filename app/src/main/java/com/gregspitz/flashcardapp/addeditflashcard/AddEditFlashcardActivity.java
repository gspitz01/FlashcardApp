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

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gregspitz.flashcardapp.Injection;
import com.gregspitz.flashcardapp.R;
import com.gregspitz.flashcardapp.data.model.Flashcard;
import com.gregspitz.flashcardapp.flashcardlist.FlashcardListActivity;

public class AddEditFlashcardActivity extends AppCompatActivity
        implements AddEditFlashcardContract.View {

    public static final String FLASHCARD_ID_EXTRA = "flashcard_id";
    public static final String NEW_FLASHCARD = "-1";

    private AddEditFlashcardContract.Presenter mPresenter;
    private EditText mFlashcardFront;
    private EditText mFlashcardBack;
    private Button mSaveFlashcardButton;
    private Flashcard mFlashcard;

    private boolean mActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_flashcard);

        mFlashcardFront = findViewById(R.id.flashcard_front);
        mFlashcardBack = findViewById(R.id.flashcard_back);
        mSaveFlashcardButton = findViewById(R.id.save_flashcard_button);

        new AddEditFlashcardPresenter(Injection.provideUseCaseHandler(),
                this, Injection.provideGetFlashcard(getApplicationContext()),
                Injection.provideSaveFlashcard(getApplicationContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActive = true;
        mPresenter.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActive = false;
    }

    @Override
    public void setPresenter(AddEditFlashcardContract.Presenter presenter) {
        mPresenter = presenter;
        mSaveFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String front = mFlashcardFront.getText().toString();
                String back = mFlashcardBack.getText().toString();
                if (mFlashcard != null) {
                    mFlashcard.setFront(front);
                    mFlashcard.setBack(back);
                } else {
                    mFlashcard = new Flashcard(front, back);
                }
                mPresenter.saveFlashcard(mFlashcard);
            }
        });
    }

    @Override
    public void showLoadingIndicator(boolean active) {
        // TODO: implement and test
    }

    @Override
    public String getIdFromIntent() {
        return getIntent().getStringExtra(FLASHCARD_ID_EXTRA);
    }

    @Override
    public void showFlashcard(Flashcard flashcard) {
        mFlashcard = flashcard;
        mFlashcardFront.setText(flashcard.getFront());
        mFlashcardBack.setText(flashcard.getBack());
    }

    @Override
    public void showFlashcardList() {
        startActivity(new Intent(this, FlashcardListActivity.class));
    }

    @Override
    public void showFailedToLoadFlashcard() {
        // TODO: implement and test
    }

    @Override
    public void showSaveSuccessful() {
        // TODO: implement and test
    }

    @Override
    public void showSaveFailed() {
        Toast.makeText(this, R.string.failed_save_toast_text, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return mActive;
    }
}
