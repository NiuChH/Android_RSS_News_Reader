package com.java.niuchenhao;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import org.litepal.LitePal;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

//    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LitePal.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        ChannelsPresenter.initChannelsPresenter(getApplicationContext());
        FeedsPresenter.init();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = findViewById(R.id.viewpager);

        // Give the TabLayout the ViewPager
        SlidingTabLayout tabLayout = findViewById(R.id.sliding_tabs);

        // Create an adapter that knows which fragment should be shown on each page
        ChannelPagerAdapter adapter = new ChannelPagerAdapter(this, getSupportFragmentManager(), tabLayout);

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        tabLayout.setViewPager(viewPager);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favourite:
                FavouriteActivity.actionStart(this);
                break;
            case R.id.search:
                SearchActivity.actionStart(this);
                break;
            case R.id.add_channel:
                ChannelsActivity.actionStart(this);
                break;
            case R.id.clear_data:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_deleteAll)
                        .setPositiveButton(R.string.confirm, (dialog, id) -> LitePal.deleteDatabase("database"))
                        .setNegativeButton(R.string.cancel, (dialog, id) -> {});
                builder.create().show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}