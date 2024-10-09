package com.sakander.session;

public interface ResultContext<T> {
    T getResultObejct();

    int getResultCount();

    boolean isStopped();

    void stop();
}
