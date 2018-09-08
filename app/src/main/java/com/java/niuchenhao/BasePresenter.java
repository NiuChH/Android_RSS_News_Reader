package com.java.niuchenhao;

import java.util.LinkedList;
import java.util.List;

public class BasePresenter {

    private static BasePresenter mPresenter = null;
    private static List<Notifiable> adapterList = new LinkedList<>();

    public static void registerAdapter(Notifiable adapter){
        adapterList.add(adapter);
    }

    public static BasePresenter getPresenter(){
        return mPresenter;
    }

    protected static void notifyAdapter(){
        for(Notifiable adapter : adapterList)
            if(adapter != null)
                adapter.notifyDiff();
    }
}
