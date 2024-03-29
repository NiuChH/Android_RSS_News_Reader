package com.java.niuchenhao.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.java.niuchenhao.R;
import com.java.niuchenhao.model.bean.ChannelItem;
import com.java.niuchenhao.model.bean.FeedItem;
import com.java.niuchenhao.presenter.FeedsPresenter;
import com.java.niuchenhao.view.adapter.SearchAdapter;

import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    public static ChannelItem channelItem = new ChannelItem("favourite", "");
    private SearchAdapter searchAdapter;
    private RecyclerView mRecyclerView;
    private List<FeedItem> feedItems;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, FavouriteActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        feedItems = FeedsPresenter.getFavourites();
        mRecyclerView = findViewById(R.id.favouriteRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addItemDecoration(new VerticalSpace(10));
        searchAdapter = new SearchAdapter(getApplicationContext(), feedItems, channelItem);
        mRecyclerView.setAdapter(searchAdapter);
    }
}
