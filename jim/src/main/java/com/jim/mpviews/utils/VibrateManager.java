package com.jim.mpviews.utils;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by Пользователь on 25.05.2017.
 */

public class VibrateManager {

    static public void startVibrate(Context context, int time) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(time);
    }
}
