package com.java.niuchenhao.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.java.niuchenhao.R;
import com.java.niuchenhao.presenter.ChannelsPresenter;
import com.java.niuchenhao.view.adapter.ChannelAdapter;


public class ChannelsActivity extends AppCompatActivity {

    RecyclerView checkedRecyclerView;
    RecyclerView uncheckedRecyclerView;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ChannelsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);
        Log.d("ChannelsActivity", "create");

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(R.string.customize_channels);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkedRecyclerView = findViewById(R.id.rv_checked_list);
        uncheckedRecyclerView = findViewById(R.id.rv_unchecked_list);
        initRecyclerView(checkedRecyclerView);
        initRecyclerView(uncheckedRecyclerView);
        checkedRecyclerView.setAdapter(new ChannelAdapter(getApplicationContext(),
                ChannelsPresenter.getCheckedChannels()));
        uncheckedRecyclerView.setAdapter(new ChannelAdapter(getApplicationContext(),
                ChannelsPresenter.getUncheckedChannels()));
    }

    private void initRecyclerView(RecyclerView recyclerView) {
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
                //a layoutOrientation of layout manager, could be VERTICAL OR HORIZONTAL. HORIZONTAL by default
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                // row strategy for views in completed row, could be STRATEGY_DEFAULT, STRATEGY_FILL_VIEW,
                //STRATEGY_FILL_SPACE or STRATEGY_CENTER
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                // whether strategy is applied to last row. FALSE by default
//                .withLastRow(true)
                .build();
        recyclerView.setLayoutManager(chipsLayoutManager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //    private int dp2px(float value) {
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
//    }

}
