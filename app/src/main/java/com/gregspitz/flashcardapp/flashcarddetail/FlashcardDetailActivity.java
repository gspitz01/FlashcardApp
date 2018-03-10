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

package com.gregspitz.flashcardapp.flashcarddetail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gregspitz.flashcardapp.Injection;
import com.gregspitz.flashcardapp.R;
import com.gregspitz.flashcardapp.addeditflashcard.AddEditFlashcardActivity;
import com.gregspitz.flashcardapp.data.model.Flashcard;

public class FlashcardDetailActivity extends AppCompatActivity
        implements FlashcardDetailContract.View {

    public static final String FLASHCARD_ID_EXTRA = "flashcard_id";

    private FlashcardDetailContract.Presenter mPresenter;
    private TextView mFlashcardFront;
    private TextView mFlashcardBack;
    private Button mEditFlashcardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_detail);

        mFlashcardFront = findViewById(R.id.flashcard_front);
        mFlashcardBack = findViewById(R.id.flashcard_back);
        mEditFlashcardButton = findViewById(R.id.edit_flashcard_button);
        mEditFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.editFlashcard();
            }
        });

        new FlashcardDetailPresenter(Injection.provideUseCaseHandler(),
                this, Injection.provideGetFlashcard(getApplicationContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(FlashcardDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoadingIndicator(boolean active) {
        // TODO: implement
    }

    @Override
    public void showFlashcard(Flashcard flashcard) {
        mFlashcardFront.setText(flashcard.getFront());
        mFlashcardBack.setText(flashcard.getBack());
    }

    @Override
    public void showEditFlashcard(String flashcardId) {
        Intent intent = new Intent(this, AddEditFlashcardActivity.class);
        intent.putExtra(AddEditFlashcardActivity.FLASHCARD_ID_EXTRA, flashcardId);
        startActivity(intent);
    }

    @Override
    public void showFailedToLoadFlashcard() {
        mFlashcardFront.setText(R.string.no_flashcards_to_show_text);
    }

    @Override
    public String getIdFromIntent() {
        return getIntent().getStringExtra(FLASHCARD_ID_EXTRA);
    }

    @Override
    public boolean isActive() {
        return true;
    }
}
