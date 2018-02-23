package com.gregspitz.flashcardapp;

import android.support.annotation.NonNull;

/**
 * Runs {@link UseCase}s using a {@link UseCaseScheduler}
 */
public class UseCaseHandler {

    private static UseCaseHandler INSTANCE;

    private final UseCaseScheduler mUseCaseScheduler;

    public UseCaseHandler(@NonNull UseCaseScheduler useCaseScheduler) {
        mUseCaseScheduler = useCaseScheduler;
    }

    public <T extends UseCase.RequestValues, R extends UseCase.ResponseValue> void execute(
            final UseCase<T, R> useCase, T values, UseCase.UseCaseCallback<R> callback) {

        useCase.setRequestValues(values);
        useCase.setUseCaseCallback(new UiCallbackWrapper(callback, this));

        mUseCaseScheduler.execute(new Runnable() {
            @Override
            public void run() {
                useCase.run();
            }
        });
    }

    public <V extends UseCase.ResponseValue> void notifyResponse(
            final V response, final UseCase.UseCaseCallback<V> useCaseCallback) {
        mUseCaseScheduler.notifyResponse(response, useCaseCallback);
    }


    public <V extends UseCase.ResponseValue> void notifyError(
            final UseCase.UseCaseCallback<V> useCaseCallback) {
        mUseCaseScheduler.onError(useCaseCallback);
    }

    public static final class UiCallbackWrapper<V extends UseCase.ResponseValue> implements
            UseCase.UseCaseCallback<V> {
        private final UseCase.UseCaseCallback<V> mCallback;
        private final UseCaseHandler mUseCaseHandler;

        public UiCallbackWrapper(UseCase.UseCaseCallback<V> callback, UseCaseHandler handler) {
            mCallback = callback;
            mUseCaseHandler = handler;
        }

        @Override
        public void onSuccess(V response) {
            mUseCaseHandler.notifyResponse(response, mCallback);
        }

        @Override
        public void onError() {
            mUseCaseHandler.notifyError(mCallback);
        }
    }
}
