package com.gregspitz.flashcardapp.flashcard.domain.model;

import java.util.UUID;

/**
 * An immutable text flashcard with a front and a back
 */
public class Flashcard {
    private String mId;
    private String mFront;
    private String mBack;

    public Flashcard(String front, String back) {
        mId = UUID.randomUUID().toString();
        mFront = front;
        mBack = back;
    }

    public Flashcard(String id, String front, String back) {
        mId = id;
        mFront = front;
        mBack = back;
    }

    public String getId() {
        return mId;
    }

    public String getFront() {
        return mFront;
    }

    public String getBack() {
        return mBack;
    }
}
