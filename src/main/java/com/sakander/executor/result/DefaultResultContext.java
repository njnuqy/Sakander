package com.sakander.executor.result;

import com.sakander.executor.resultset.DefaultResultSetHandler;
import com.sakander.session.ResultContext;

public class DefaultResultContext<T> implements ResultContext<T> {
    private T resultObejct;
    private int resultCount;
    private boolean stopped;

    public DefaultResultContext(){
        resultObejct = null;
        resultCount = 0;
        stopped = false;
    }
    @Override
    public T getResultObejct() {
        return resultObejct;
    }

    @Override
    public int getResultCount() {
        return resultCount;
    }

    @Override
    public boolean isStopped() {
        return stopped;
    }

    public void nextResultObejct(T resultObejct){
        resultCount++;
        this.resultObejct = resultObejct;
    }

    @Override
    public void stop() {
        this.stopped = true;
    }
}
