package com.java.niuchenhao.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.java.niuchenhao.R;
import com.java.niuchenhao.model.bean.ChannelItem;
import com.java.niuchenhao.model.bean.FeedItem;
import com.java.niuchenhao.presenter.FeedsPresenter;
import com.java.niuchenhao.view.adapter.SearchAdapter;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private SearchAdapter searchAdapter;
    private List<FeedItem> feedItems;
    private ChannelItem channelItem = new ChannelItem("search", "");

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        feedItems = FeedsPresenter.getFeedItemList(channelItem);
        mSearchView = findViewById(R.id.searchView);
        mRecyclerView = findViewById(R.id.searchRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addItemDecoration(new VerticalSpace(10));
        searchAdapter = new SearchAdapter(getApplicationContext(), feedItems, channelItem);
        mRecyclerView.setAdapter(searchAdapter);


        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FeedsPresenter.queryFeedItemList(channelItem, '%' + query + '%', 20, false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}