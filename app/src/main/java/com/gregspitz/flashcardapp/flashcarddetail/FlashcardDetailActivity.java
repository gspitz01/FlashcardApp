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

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.gregspitz.flashcardapp.Injection;
import com.gregspitz.flashcardapp.R;

public class FlashcardDetailActivity extends AppCompatActivity {

    public static final String FLASHCARD_ID_EXTRA = "flashcard_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FlashcardDetailFragment flashcardDetailFragment =
                (FlashcardDetailFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.content_frame);

        if (flashcardDetailFragment == null) {
            flashcardDetailFragment = new FlashcardDetailFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.content_frame, flashcardDetailFragment);
            transaction.commit();
        }

        new FlashcardDetailPresenter(Injection.provideUseCaseHandler(),
                flashcardDetailFragment, Injection.provideGetFlashcard(getApplicationContext()));
    }
}
