package com.java.niuchenhao.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.java.niuchenhao.FeedsAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import static android.content.ContentValues.TAG;

public class PicUtils {


    //ref: https://www.cnblogs.com/whoislcj/p/5547758.html
    public static void loadPic(Context context, FeedsAdapter.FeedViewHolder holder) {
        String pathName = context.getFilesDir().getPath() + "/" + holder.getId() + ".png";
//        if (!new File(context.getFilesDir().getPath(), holder.getId() + ".png").exists())
//            savePic(context, holder);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(pathName);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;

        if (srcHeight > holder.getThumbnail().getMaxHeight() || srcWidth > holder.getThumbnail().getMaxWidth()) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / holder.getThumbnail().getMaxHeight());
            } else {
                inSampleSize = Math.round(srcWidth / holder.getThumbnail().getMaxWidth());
            }
        }

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;

//                    Bitmap bitmap = BitmapFactory.decodeFile("/data/data/com.example.d.test.feature/files/"+Integer.toString(current.getId())+".png", options);
        Log.d(TAG, "onBindViewHolder: " + pathName);
        Bitmap bitmap = BitmapFactory.decodeFile(pathName);
        holder.getThumbnail().setImageBitmap(bitmap);
        Log.e(TAG, "onBindViewHolder: load bitmap!");
    }

    public static void savePic(final String pathPrefix, final String filename, final Bitmap b) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                FileOutputStream fos;
                try {
                    Log.i(TAG, "start savePic");
                    File f = new File(pathPrefix, filename);
                    if (f.exists()) {
                        return null;
                    }
                    fos = new FileOutputStream(f);
                    Log.i(TAG, "strFileName 1= " + f.getPath());
                    b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                    fos.flush();
                    fos.close();
                    Log.i(TAG, "save pic OK!");
                } catch (FileNotFoundException e) {
                    Log.i(TAG, "FileNotFoundException");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i(TAG, "IOException");
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }
}
