package com.example.android.criminal;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;

import java.util.Map;

/**
 * Created by Administrator on 2016/3/19.
 */
public class PicturesUtils {
    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;
        Log.i("CrimeFragment", "srcWidth=" + String.valueOf(srcWidth) + " ,srcHeight=" + String.valueOf(srcHeight));
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        Log.i("CrimeFragment", "inSampleSize = " + String.valueOf(inSampleSize));
        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap getScaledBitmap(String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        Log.i("CrimeFragment", String.valueOf(size.x) + "  " + String.valueOf(size.y));

        return getScaledBitmap(path, size.x, size.y);
    }

}
