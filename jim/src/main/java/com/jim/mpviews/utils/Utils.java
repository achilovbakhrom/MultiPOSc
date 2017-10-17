package com.jim.mpviews.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by developer on 18.05.2017.
 */

public class Utils {
    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    public static int getScreenWidth(Context context) {
        return getPointSize(context).x;
    }

    public static int getScreenHeight(Context context) {
        return getPointSize(context).y;
    }

    private static Point getPointSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }
}
