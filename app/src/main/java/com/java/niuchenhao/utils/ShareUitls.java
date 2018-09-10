package com.java.niuchenhao.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ShareUitls {

    public static File bitMap2File(Bitmap bitmap, Context context, String filename) {


        String path = "";

        path += context.getExternalCacheDir() + File.separator;

        File f = new File(path, filename + ".jpg");
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
            out.flush();
            out.close();
            bitmap.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return f;
        }
    }

    public static Intent file2ShareIntent(File file, String share) {
        if (file != null && file.exists() && file.isFile()) {
            Uri imageUri = Uri.fromFile(file);
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/*");
            Log.d("share utils:", "share image ready!");
            return Intent.createChooser(shareIntent, share);
        }
        return null;
    }

}
