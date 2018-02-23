package com.gregspitz.flashcardapp.flashcard.domain.model;

/**
 * An immutable text flashcard with a front and a back
 */
public class Flashcard {
    private String mFront;
    private String mBack;

    public Flashcard(String front, String back) {
        mFront = front;
        mBack = back;
    }

    public String getFront() {
        return mFront;
    }

    public String getBack() {
        return mBack;
    }
}
