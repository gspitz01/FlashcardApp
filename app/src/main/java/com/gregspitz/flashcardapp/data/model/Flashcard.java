package com.gregspitz.flashcardapp.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * An immutable text flashcard with a front and a back
 */
@Entity
public class Flashcard {

    @PrimaryKey
    @NonNull
    private String id;
    private String front;
    private String back;

    @Ignore
    public Flashcard(String front, String back) {
        this(UUID.randomUUID().toString(), front, back);
    }

    public Flashcard(@NonNull String id, String front, String back) {
        this.id = id;
        this.front = front;
        this.back = back;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getFront() {
        return front;
    }

    public String getBack() {
        return back;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public void setBack(String back) {
        this.back = back;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != Flashcard.class) {
            return false;
        }
        Flashcard otherCard = (Flashcard) obj;
        return otherCard.getId().equals(getId()) &&
                otherCard.getFront().equals(getFront()) &&
                otherCard.getBack().equals(getBack());
    }
}
