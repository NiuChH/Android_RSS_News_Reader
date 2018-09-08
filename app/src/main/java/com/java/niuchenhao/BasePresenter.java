package com.java.niuchenhao;

import java.util.LinkedList;
import java.util.List;

public class BasePresenter {

    private static BasePresenter mPresenter = new BasePresenter();
    private static List<Notifiable> adapterList = new LinkedList<>();

    BasePresenter(){}

    public static void registerAdapter(Notifiable adapter){
        adapterList.add(adapter);
    }

    public static void unregisterAdapter(Notifiable adapter){
        try {
            adapterList.remove(adapter);
        } catch (Exception e){
            e.printStackTrace();
        }
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
