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

package com.gregspitz.flashcardapp.flashcardlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gregspitz.flashcardapp.R;
import com.gregspitz.flashcardapp.data.model.Flashcard;

import java.util.ArrayList;
import java.util.List;

/**
 * An adapter for a flashcard recycler view
 */

public class FlashcardRecyclerAdapter
        extends RecyclerView.Adapter<FlashcardRecyclerAdapter.FlashcardViewHolder> {

    private List<Flashcard> mFlashcards;
    private FlashcardListContract.Presenter mPresenter;

    public FlashcardRecyclerAdapter(ArrayList<Flashcard> flashcards) {
        mFlashcards = flashcards;
    }

    @Override
    public FlashcardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flashcard_list_holder,
                parent, false);
        return new FlashcardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FlashcardViewHolder holder, int position) {
        holder.setFlashcard(mFlashcards.get(position));
    }

    @Override
    public int getItemCount() {
        return mFlashcards.size();
    }

    public void updateFlashcards(List<Flashcard> flashcards) {
        mFlashcards = flashcards;
        notifyDataSetChanged();
    }

    public void setPresenter(FlashcardListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    class FlashcardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mFlashcardFront;

        public FlashcardViewHolder(View itemView) {
            super(itemView);
            mFlashcardFront = itemView.findViewById(R.id.flashcard_front);
            itemView.setOnClickListener(this);
        }

        public void setFlashcard(Flashcard flashcard) {
            mFlashcardFront.setText(flashcard.getFront());
        }

        @Override
        public void onClick(View v) {
            if (mPresenter != null) {
                mPresenter.onFlashcardClick(mFlashcards.get(getAdapterPosition()).getId());
            }
        }
    }
}
