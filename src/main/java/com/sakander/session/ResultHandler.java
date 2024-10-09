package com.sakander.session;

public interface ResultHandler<T> {
    void handleResult(ResultContext<? extends T> resultContext);
}
