package com.jim.mpviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by developer on 07.12.2017.
 */

public class MpCalendarIntervView extends RelativeLayout {
    public MpCalendarIntervView(Context context) {
        super(context);
        init(context,null);
    }

    public MpCalendarIntervView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);

    }

    public MpCalendarIntervView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);

    }

    public MpCalendarIntervView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);

    }

    public void init(Context context, AttributeSet attributeSet) {
        LayoutInflater.from(context).inflate(R.layout.mp_calendar_interv, this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
    }
}
