package com.gregspitz.flashcardapp;

/**
 * An interface for all views
 */
public interface BaseView<T extends BasePresenter> {
    void setPresenter(T presenter);
}
