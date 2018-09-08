package com.java.niuchenhao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.java.niuchenhao.bean.FeedItem;

public class NewsContentActivity extends AppCompatActivity {

    private View view;

    private ShareActionProvider mShareActionProvider;
    private FeedItem feedItem;

    public static void actionStart(Context context, FeedItem feedItem) {
        Intent intent = new Intent(context, NewsContentActivity.class);
        intent.putExtra("news_item", feedItem);
        context.startActivity(intent);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.news_toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        feedItem = (FeedItem) getIntent().getSerializableExtra("news_item");
        WebView webView = findViewById(R.id.web_view);
        WebSettings settings = webView.getSettings();
        String ua = settings.getUserAgentString();
        settings.setUserAgentString(ua + " APP_TAG/5.0.1");
        settings.setBuiltInZoomControls(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);
        settings.setAppCacheEnabled(true);
        settings.setAppCacheMaxSize(10 * 1024 * 1024);
        settings.setAppCachePath("");
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setSaveFormData(false);
        settings.setSavePassword(false);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(feedItem.getLink());
    }

    // BEGIN_INCLUDE(get_sap)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("NewsActivity", "create menu");

        // Inflate the menu resource
        getMenuInflater().inflate(R.menu.share_menu, menu);

        // Retrieve the share menu item
        MenuItem shareItem = menu.findItem(R.id.menu_share);

        // Now get the ShareActionProvider from the item
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        setShareIntent();

        return super.onCreateOptionsMenu(menu);
    }
    // END_INCLUDE(get_sap)


    private void setShareIntent() {
        // BEGIN_INCLUDE(update_sap)
        if (mShareActionProvider != null) {
            // Get the currently selected item, and retrieve it's share intent

            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            //TODO set MIME type with switch
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, feedItem.getLink());

            // Now update the ShareActionProvider with the new share intent
            mShareActionProvider.setShareIntent(shareIntent);
        } else {
            Log.e("NewsActivity", "null share provider");
        }
        // END_INCLUDE(update_sap)
    }
}
