package com.gregspitz.flashcardapp.flashcard;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * A custom view for a Flashcard
 */

public class FlashcardView extends AppCompatTextView {
    public FlashcardView(Context context) {
        super(context);
    }

    public FlashcardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlashcardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
