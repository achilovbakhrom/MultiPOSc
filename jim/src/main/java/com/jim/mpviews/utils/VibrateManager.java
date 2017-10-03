package com.jim.mpviews.utils;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by Пользователь on 25.05.2017.
 */

public class VibrateManager {
    private Context context;
    private int time = 50;

    public VibrateManager(Context context) {
        this.context = context;
    }

    public VibrateManager(Context context, int time) {
        this.context = context;
        this.time = time;
    }

    public void startVibrate() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(time);
    }
}
