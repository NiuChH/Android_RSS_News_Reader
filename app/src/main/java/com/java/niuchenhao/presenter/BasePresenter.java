package com.java.niuchenhao.presenter;

import com.java.niuchenhao.view.adapter.NotifiableAdapter;

import java.util.LinkedList;
import java.util.List;

public class BasePresenter {

    private static BasePresenter mPresenter = new BasePresenter();
    private static List<NotifiableAdapter> adapterList = new LinkedList<>();

    BasePresenter(){}

    public static void registerAdapter(NotifiableAdapter adapter){
        adapterList.add(adapter);
    }

    public static void unregisterAdapter(NotifiableAdapter adapter){
        try {
            adapterList.remove(adapter);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

//    public static BasePresenter getPresenter(){
//        return mPresenter;
//    }

    protected static void notifyAdapter(){
        for(NotifiableAdapter adapter : adapterList)
            if(adapter != null)
                adapter.notifyDiff();
    }
}
