package com.sakander.executor.result;

import com.sakander.session.ResultContext;
import com.sakander.session.ResultHandler;

import java.util.ArrayList;
import java.util.List;

public class DefaultResultHandler implements ResultHandler<Object> {
    private final List<Object> list;
    public DefaultResultHandler(){
        list = new ArrayList<>();
    }
    @Override
    public void handleResult(ResultContext<?> resultContext) {
        list.add(resultContext.getResultObejct());
    }

    public List<Object> getResultList(){
        return list;
    }
}
