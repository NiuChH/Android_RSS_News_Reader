package com.java.niuchenhao.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.java.niuchenhao.R;
import com.java.niuchenhao.model.bean.FeedItem;
import com.java.niuchenhao.presenter.FeedsPresenter;
import com.java.niuchenhao.utils.ShareUitls;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class NewsContentActivity extends AppCompatActivity {

    private WebView webView;

    private ShareActionProvider mShareActionProvider;
    private FeedItem feedItem;

    public static void actionStart(Context context, FeedItem feedItem) {
        Intent intent = new Intent(context, NewsContentActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        webView = findViewById(R.id.web_view);

//        WebSettings settings = webView.getSettings();
//
//        String ua = settings.getUserAgentString();
//        settings.setUserAgentString(ua + " APP_TAG/5.0.1");
//        settings.setBuiltInZoomControls(true);
//        settings.setLoadWithOverviewMode(true);
//        settings.setUseWideViewPort(true);
//        settings.setJavaScriptEnabled(true);
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//        settings.setSupportMultipleWindows(true);
//        settings.setAppCacheEnabled(true);
//        settings.setAppCacheMaxSize(10 * 1024 * 1024);
//        settings.setAppCachePath("");
//        settings.setDatabaseEnabled(true);
//        settings.setDomStorageEnabled(true);
//        settings.setGeolocationEnabled(true);
//        settings.setSaveFormData(false);
//        settings.setSavePassword(false);

        WebSettings settings = webView.getSettings();
        String ua = webView.getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(ua + " APP_TAG/5.0.1");
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setAppCacheEnabled(true);
        String appCachDir = this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        settings.setAppCachePath(appCachDir);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);

        settings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);

        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                invalidateOptionsMenu();
                super.onPageFinished(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient());

        webView.loadUrl(feedItem.getLink());

//        if (feedItem.isFavourite() && !(DatabaseModel.getIsOnline())) {
//            // TODO check this
//            String path = getExternalFilesDir(null) + File.separator + feedItem.getFilename() + ".mht";
//            File file = new File(getExternalFilesDir(null), feedItem.getFilename() + ".mht");
//            settings.setDefaultTextEncodingName(codeString(path));
////            Log.d("1path: ", codeString(path) + " " + file.exists() + "file:///" + getExternalFilesDir(null) + File.separator + feedItem.getFilename() + ".mht");
//            if (file.exists())
//                webView.loadUrl("file:///" + getExternalFilesDir(null) + File.separator + feedItem.getFilename() + ".mht");
//            else
//                webView.loadUrl(feedItem.getLink());
//        } else
//            webView.loadUrl(feedItem.getLink());


    }


    public static String codeString(String fileName) {
        BufferedInputStream bin = null;
        String code = null;
        try {
            bin = new BufferedInputStream(new FileInputStream(fileName));
            int p = (bin.read() << 8) + bin.read();
            bin.close();
            switch (p) {
                case 0xefbb:
                    code = "UTF-8";
                    break;
                case 0xfffe:
                    code = "Unicode";
                    break;
                case 0xfeff:
                    code = "UTF-16BE";
                    break;
                default:
                    code = "GBK";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }


//    public String getFileEncoding(String filePath){
//
//        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
//        detector.add(new ParsingDetector(false));
//        detector.add(JChardetFacade.getInstance());
//        detector.add(UnicodeDetector.getInstance());
//
//        Charset charset = null;
//        File file = new File(filePath);
//        try {
//            charset = detector.detectCodepage(file.toURI().toURL());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        String charsetName = "GBK";
//        if(charset != null){
//            if(charset.name().equals("US-ASCII")){
//                charsetName = "ISO-8859-1";
//            }else if(charset.name().startsWith("UTF")){
//                charsetName = charset.name();
//            }
//        }
//        return charsetName;
//    }


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
//        invalidateOptionsMenu();

        refreshFavourite(menu.findItem(R.id.menu_favourite));

        return super.onCreateOptionsMenu(menu);
    }
    // END_INCLUDE(get_sap)

    private void refreshFavourite(MenuItem item) {
        if (feedItem.isFavourite()) {
            item.setIcon(R.drawable.ic_favorite_black_24dp);
            item.setTitle(R.string.unfavourite);
        } else {
            item.setIcon(R.drawable.ic_favorite_border_black_24dp);
            item.setTitle(R.string.favourite);
        }
    }

    private void setShareIntent() {

        // BEGIN_INCLUDE(update_sap)
        if (mShareActionProvider != null) {

            View mView = LayoutInflater.from(this).inflate(R.layout.custum_row_news_item, null);


            ((TextView) mView.findViewById(R.id.title_text)).setText(feedItem.getTitle());
            ((TextView) mView.findViewById(R.id.description_text)).setText(feedItem.getDescription().replaceAll("<.*?>", ""));
            final ImageView thumbnails = (ImageView) mView.findViewById(R.id.thumb_img);
            final String thumbnailUrl = feedItem.getThumbnailUrl();

            final Context context = getApplicationContext();
            try {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap qr_bitmap;
                qr_bitmap = barcodeEncoder.encodeBitmap(feedItem.getLink(), BarcodeFormat.QR_CODE, 400, 400);
                ImageView imageViewQrCode = mView.findViewById(R.id.zxing_img);
                imageViewQrCode.setImageBitmap(qr_bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }


            //图片的宽度为屏幕宽度，高度为wrap_content


            if (thumbnailUrl == null) {
                Picasso.with(context).load(R.drawable.rss_logo).into(thumbnails);
//                thumbnails.setImageDrawable(getResources().getDrawable(R.drawable.rss_logo));
            } else {
                Picasso.with(context)
                        .load(feedItem.getThumbnailUrl())
                        .placeholder(R.drawable.rss_logo)
                        .error(R.drawable.rss_logo)
                        .into(thumbnails);
            }

            mView.setDrawingCacheEnabled(true);
            mView.buildDrawingCache();
            mView.measure(View.MeasureSpec.makeMeasureSpec(getResources().getDisplayMetrics().widthPixels, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            //放置mView
            mView.layout(0, 0, mView.getMeasuredWidth(), mView.getMeasuredHeight());
            Bitmap bitmap = mView.getDrawingCache();
            Intent shareIntent = ShareUitls.file2ShareIntent(ShareUitls.bitMap2File(bitmap, getApplicationContext()));
            if (shareIntent != null)
                mShareActionProvider.setShareIntent(shareIntent);
            else
                Log.e("NewsActivity", "null share intent");
        } else {
            Log.e("NewsActivity", "null share provider");
        }
        // END_INCLUDE(update_sap)
    }

    private Intent createIntent(FeedItem feedItem) {
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SUBJECT, feedItem.getTitle());//一般用于邮件
        intent.putExtra(Intent.EXTRA_TEXT,
                feedItem.getDescription()
                        .replaceAll("<.*?>", "")
                        + " " + feedItem.getLink());
        boolean hasImage = (feedItem.getThumbnailUrl() != null);
        if (!hasImage) {//如果没有图片则设置类型为text/plain
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
        } else {
            //获取要分享的Image的Uri数字
            ArrayList<Uri> imageUris = new ArrayList<>();
            try {
                imageUris.add(Uri.parse(new URL(feedItem.getThumbnailUrl()).toURI().toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            //TODO set image uris

            //只要有图片就设置type为image/*
            intent.setType("image/*");
            if (imageUris.size() == 1) {
                //一张图片用ACTION_SEND
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, imageUris.get(0));
            } else {
                //多张图片用ACTION_SEND_MUTIPLE
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
            }
        }
        return Intent.createChooser(intent, getResources().getString(R.string.menu_share));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favourite:
                if (!feedItem.isFavourite()) {
                    feedItem.setFavourite(true);
                    Log.d("toggle00", feedItem.getTitle() + " " + feedItem.isFavourite());
                    refreshFavourite(item);
                    FeedsPresenter.toggleFavourite(feedItem);
//                    File file = new File(Environment.getExternalStorageDirectory(),feedItem.getFilename()+".mht");
//                    webView.saveWebArchive(file.getAbsolutePath());
                    webView.saveWebArchive(getExternalFilesDir(null) + File.separator + feedItem.getFilename() + ".mht");
                    Log.d("path: ", getExternalFilesDir(null) + File.separator + feedItem.getFilename() + ".mht");
                } else {
                    feedItem.setFavourite(false);
                    Log.d("toggle0", feedItem.getTitle() + " " + feedItem.isFavourite());
                    refreshFavourite(item);
                    FeedsPresenter.toggleFavourite(feedItem);
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Files.deleteIfExists(Paths.get(getExternalFilesDir(null) + "/" + feedItem.getFilename() + ".mht"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//                FeedsPresenter.toggleFavourite(feedItem);
//                refreshFavourite(item);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
