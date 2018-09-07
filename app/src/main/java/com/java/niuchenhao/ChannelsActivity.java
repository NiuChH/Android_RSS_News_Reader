package com.java.niuchenhao;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntRange;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.beloo.widget.chipslayoutmanager.layouter.breaker.IRowBreaker;
import com.java.niuchenhao.utils.OpmlReader;
import java.util.ArrayList;
import java.util.List;


public class ChannelsActivity extends AppCompatActivity {

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ChannelsActivity.class);
        context.startActivity(intent);
    }

    RecyclerView checkedRecyclerView;
    RecyclerView uncheckedRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);
        Log.d("ChannelsActivity", "create");

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("栏目选择");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkedRecyclerView = findViewById(R.id.rv_checked_list);
        uncheckedRecyclerView = findViewById(R.id.rv_unchecked_list);
        initRecyclerView(checkedRecyclerView);
        initRecyclerView(uncheckedRecyclerView);
        checkedRecyclerView.setAdapter(new ChannelAdapter(this,
                ChannelsPresenter.getCheckedChannels()));
        uncheckedRecyclerView.setAdapter(new ChannelAdapter(this,
                ChannelsPresenter.getUncheckedChannels()));
    }

    private void initRecyclerView(RecyclerView recyclerView){
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(this)
                //set vertical gravity for all items in a row. Default = Gravity.CENTER_VERTICAL
                .setChildGravity(Gravity.TOP)
                //whether RecyclerView can scroll. TRUE by default
                .setScrollingEnabled(true)
                //set maximum views count in a particular row
                .setMaxViewsInRow(5)
                //set gravity resolver where you can determine gravity for item in position.
                //This method have priority over previous one
                .setGravityResolver(new IChildGravityResolver() {
                    @Override
                    public int getItemGravity(int position) {
                        return Gravity.BOTTOM;
                    }
                })
                //you are able to break row due to your conditions. Row breaker should return true for that views
//                .setRowBreaker(new IRowBreaker() {
//                    @Override
//                    public boolean isItemBreakRow(@IntRange(from = 0) int position) {
//                        return position == 6 || position == 11;
//                    }
//                })
                //a layoutOrientation of layout manager, could be VERTICAL OR HORIZONTAL. HORIZONTAL by default
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                // row strategy for views in completed row, could be STRATEGY_DEFAULT, STRATEGY_FILL_VIEW,
                //STRATEGY_FILL_SPACE or STRATEGY_CENTER
                .setRowStrategy(ChipsLayoutManager.STRATEGY_CENTER)
                // whether strategy is applied to last row. FALSE by default
                .withLastRow(true)
                .build();
        recyclerView.setLayoutManager(chipsLayoutManager);
    }

//    private int dp2px(float value) {
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
//    }

}
