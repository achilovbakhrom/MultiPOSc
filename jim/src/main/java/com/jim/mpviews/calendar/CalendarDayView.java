package com.jim.mpviews.calendar;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.R;
import com.jim.mpviews.utils.CalendarDate;

import java.util.Calendar;


public class CalendarDayView extends LinearLayout {

    private CalendarDate mCalendarDate;
    private TextView mTextDay, mDiff, mStartTime, mEndTime;
    private LinearLayout mLayoutBackground;

    public CalendarDayView(Context context, CalendarDate calendarDate) {
        super(context);
        mCalendarDate = calendarDate;
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.mp_calendar_day, this);
        mLayoutBackground = (LinearLayout) findViewById(R.id.mpDayBg);
        mTextDay = (TextView) findViewById(R.id.mpDay);
        mDiff = (TextView) findViewById(R.id.mpDiff);
        mStartTime = (TextView) findViewById(R.id.mpStartTime);
        mEndTime = (TextView) findViewById(R.id.mpEndTime);
        mTextDay.setText("" + mCalendarDate.getDay());
    }

    public void setStartTime(String date) {
        mStartTime.setText(date);
    }

    public void setEndTime(String date) {
        mEndTime.setText(date);
    }

    public CalendarDate getDate() {
        return mCalendarDate;
    }

    public void setThisMothTextColor() {
        mTextDay.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlue));
        mStartTime.setTextColor(ContextCompat.getColor(getContext(), R.color.colorMainText));
        mEndTime.setTextColor(ContextCompat.getColor(getContext(), R.color.colorMainText));
    }

    public void setOtherMothTextColor() {
        mTextDay.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTransparentBlue));
        mStartTime.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTransparentMainText));
        mEndTime.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTransparentMainText));
    }

    public void setGreenBackground() {
        mLayoutBackground.setBackgroundResource(R.color.colorLightGreen);
    }

    public void setWhiteBackground() {
        mLayoutBackground.setBackgroundResource(R.color.colorWhite);
    }

    public void setGreyBackground() {
        mLayoutBackground.setBackgroundResource(R.color.colorBackground);
    }

}
