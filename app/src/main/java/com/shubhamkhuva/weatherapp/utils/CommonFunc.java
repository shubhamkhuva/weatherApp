package com.shubhamkhuva.weatherapp.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.view.Window;
import android.view.WindowManager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommonFunc {
    public static void setSharedPrefString(Context context, String key, String value) {
        SharedPreferences mPrefs = context.getSharedPreferences(CommonKey.SHARED_PERF_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public static SharedPreferences getMembersSharedPrefString(Context context) {
        return context.getSharedPreferences(CommonKey.SHARED_PERF_FILE, Context.MODE_PRIVATE);
    }
    public static void setFullScreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    public static boolean isNetworkConnected(Context mContext) {
        try {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
