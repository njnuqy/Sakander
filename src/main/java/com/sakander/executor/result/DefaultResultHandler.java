package com.sakander.executor.result;

import com.sakander.refection.factory.ObjectFactory;
import com.sakander.session.ResultContext;
import com.sakander.session.ResultHandler;

import java.util.ArrayList;
import java.util.List;

public class DefaultResultHandler implements ResultHandler<Object> {
    private final List<Object> list;

    public DefaultResultHandler(){
        list = new ArrayList<>();
    }

    public DefaultResultHandler(ObjectFactory objectFactory){
        list = objectFactory.create(List.class);
    }

    @Override
    public void handleResult(ResultContext<?> context) {
        list.add(context.getResultObejct());
    }

    public List<Object> getResultList(){
        return list;
    }
}
