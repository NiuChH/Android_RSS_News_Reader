package com.java.niuchenhao;
//
//import android.animation.Animator;
//import android.content.Context;
//import android.content.Intent;
//import android.databinding.ViewDataBinding;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.database.DatabaseUtils.*;
//import android.databinding.DataBindingUtil;
//
//
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.animation.ObjectAnimator;
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.support.design.widget.Snackbar;
//import android.support.v4.view.MenuItemCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.SearchView;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.AccelerateDecelerateInterpolator;
//import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
//import com.java.niuchenhao.bean.FeedItem;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//
//import java.util.ArrayList;
//
//import com.java.niuchenhao.databinding.*;
//
//// ref: https://github.com/Wrdlbrnft/Searchable-RecyclerView-Demo


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.java.niuchenhao.bean.ChannelItem;
import com.java.niuchenhao.bean.FeedItem;

import java.util.ArrayList;
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



        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                FeedsPresenter.queryFeedItemList(channelItem, '%' + query + '%', 10, false);
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
//                if (!TextUtils.isEmpty(newText)){
//                    mListView.setFilterText(newText);
//                }else{
//                    mListView.clearTextFilter();
//                }
                return false;
            }
        });
    }
}







//public class SearchActivity extends AppCompatActivity {
//
//
//    class SearchAdapter extends SortedListAdapter<FeedItem> {
//
//        public interface Listener {
//            void onFeedItemClicked(FeedItem model);
//        }
//
//        public SearchAdapter(@NonNull Context context, @NonNull Class<FeedItem> itemClass, @NonNull Comparator<FeedItem> comparator) {
//            super(context, itemClass, comparator);
//        }
//
//        class SearchItemViewHolder extends SortedListAdapter.ViewHolder<FeedItem> {
//            private final ItemFeedItemBinding mBinding;
//
//            public SearchItemViewHolder(ItemFeedItemBinding binding, SearchAdapter.Listener listener) {
//                super(binding.getRoot());
//                binding.setListener(listener);
//
//                mBinding = binding;
//            }
//
//            @Override
//            protected void performBind(@NonNull FeedItem item) {
//                mBinding.setModel(item);
//            }
//        }
//
//        @NonNull
//        @Override
//        protected ViewHolder<? extends FeedItem> onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
//            return null;
//        }
//    }
//
//    private SearchAdapter mAdapter;
//    private Animator mAnimator;
//    private ActivitySearchBinding mBinding;
//
//    private static final Comparator<FeedItem> COMPARATOR = new SortedListAdapter.ComparatorBuilder<FeedItem>()
//            .setOrderForModel(FeedItem.class, (a, b) -> Integer.signum((int) (a.getLongDate() - b.getLongDate())))
//            .build();
//
//    public static void actionStart(Context context) {
//        Intent intent = new Intent(context, ChannelsActivity.class);
//        context.startActivity(intent);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//
//        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
//
//        setSupportActionBar(mBinding.toolbar);
//
//        mAdapter = new SearchAdapter(this, COMPARATOR, model -> {
//            final String message = getString(R.string.model_clicked_pattern, model.getRank(), model.getWord());
//            Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
//        });
//
//        mAdapter.addCallback(this);
//
//        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mBinding.recyclerView.setAdapter(mAdapter);
//
//        mModels = new ArrayList<>();
//        final String[] words = getResources().getStringArray(R.array.words);
//        for (int i = 0; i < words.length; i++) {
//            mModels.add(new WordModel(i, i + 1, words[i]));
//        }
//        mAdapter.edit()
//                .replaceAll(mModels)
//                .commit();
//
//
//    }
//
//}
