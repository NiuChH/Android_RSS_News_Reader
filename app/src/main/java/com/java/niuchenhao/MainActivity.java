package com.java.niuchenhao;

import android.content.Context;
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
            case R.id.search:
                Toast.makeText(this, "You clicked Search", Toast.LENGTH_SHORT).show();
                SearchActivity.actionStart(this);
                break;
            case R.id.add_channel:
                ChannelsActivity.actionStart(this);
                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

}