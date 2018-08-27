package com.jim.mpviews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class MPCustomContainer extends FrameLayout{

    public MPCustomContainer(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public MPCustomContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MPCustomContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private float beginX = 0;
    private float startingCoord = 0;
    private long beginTime = 0L;
    private int width = 400;
    private boolean isDragged = false;
    private MPCustomContainerListener listener;

    public void setListener(MPCustomContainerListener listener) {
        this.listener = listener;
    }

    public void init(Context context, AttributeSet attrs){

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MPCustomContainer);
            if (array != null) {
                width = array.getDimensionPixelSize(R.styleable.MPCustomContainer_width, width);
            }
            array.recycle();
        }
    }

    private boolean open = false;
    int lastOffset = 0;

    public void setOpen(boolean open) {
        this.open = open;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("open", open);
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.bundle = bundle;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());
        Bundle bundle = ss.bundle;
        this.open = bundle.getBoolean("open");
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                beginX = getX() - event.getRawX();
                startingCoord = event.getRawX();
                beginTime = System.currentTimeMillis();
                lastOffset = 0;
                break;
            case MotionEvent.ACTION_MOVE:

                if (!isDragged) {
                    isDragged = Math.abs(beginX - event.getRawX()) > 25;
                    if (isDragged && listener != null) {
                        listener.startDragging();
                    }
                }

                float currPos = event.getRawX() + beginX;

                if(-width < currPos && currPos < width){
                    if(open && currPos>0 && listener!=null) {
//                        animate().translationX(currPos).setDuration(0);

                        int w = (int) (getWidth() + currPos - lastOffset);
                        lastOffset = (int) currPos;
                        setLayoutParams(new RelativeLayout.LayoutParams(w, ViewGroup.LayoutParams.MATCH_PARENT));
                        listener.onDrag(currPos);
                    }else if(!open && currPos<0&&listener!=null){
                        animate().translationX(currPos).setDuration(0);
                        listener.onDrag(currPos);
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
                long deltaTime = System.currentTimeMillis() - beginTime;

                if (deltaTime < 400 && !isDragged) {
                    if (listener != null) {
                        listener.onClick();
                    }
                    isDragged = false;
                    return false;
                } else {
                    if (listener != null&&Math.abs(Math.abs(beginX)-event.getRawX())>25) {
                        listener.stopDragging();
                        if (Math.abs(beginX+event.getRawX()) > width / 2 && !open) {
                            listener.onOpen();
                            open = true;
                        } else if(Math.abs(beginX+event.getRawX()) > width/2 && open){
                            listener.onClose();
                            open = false;
                        } else if(Math.abs(beginX+event.getRawX()) < width/2 && open){
                            listener.onOpen();
                            open = true;
                        } else {
                            listener.onClose();
                            open = false;
                        }
                    }else {
                        if (open&&listener!=null)
                            listener.onOpen();
                        else if(!open&&listener!=null)
                            listener.onClose();
                    }
                    isDragged = false;
                    return super.onInterceptTouchEvent(event);
                }
        }

        return super.onInterceptTouchEvent(event);
    }


    public interface MPCustomContainerListener {
        void onClick();
        void onDrag(float x);
        void startDragging();
        void stopDragging();
        void onClose();
        void onOpen();
    }

    static class SavedState extends BaseSavedState {
        Bundle bundle;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.bundle = in.readBundle();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeBundle(this.bundle);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

}
