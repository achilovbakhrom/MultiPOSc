package com.jim.mpviews.adapters;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Sirojiddin on 05.01.2018.
 */

public abstract class GestureDetectorListenerAdapter implements
        GestureDetector.OnGestureListener {

    private static final String TAG = "GestureDetectorListenerAdapter";

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
