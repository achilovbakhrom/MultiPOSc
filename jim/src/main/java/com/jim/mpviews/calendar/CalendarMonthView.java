package com.jim.mpviews.calendar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;

import com.jim.mpviews.R;
import com.jim.mpviews.interfaces.OnDayViewClickListener;
import com.jim.mpviews.utils.CalendarDate;
import com.jim.mpviews.utils.CalendarMonth;


public class CalendarMonthView extends FrameLayout implements View.OnClickListener {

    private GridLayout mGridLayout;
    private ViewGroup mLayoutDays;
    private OnDayViewClickListener mListener;
    private CalendarDate mSelectedDate;

    public CalendarMonthView(Context context) {
        super(context);
        init();
    }

    public void setOnDayViewClickListener(OnDayViewClickListener listener) {
        mListener = listener;
    }

    public void setSelectedDate(CalendarDate selectedDate) {
        mSelectedDate = selectedDate;
    }

    private void init() {
        inflate(getContext(), R.layout.mp_calendar_month, this);
        mGridLayout = (GridLayout) findViewById(R.id.mpCalendarMonthGrid);
        mLayoutDays = (ViewGroup) findViewById(R.id.mpWeekDays);
    }

    public void buildView(CalendarMonth calendarMonth) {
      //  buildDaysLayout();
        buildGridView(calendarMonth);
    }

    private void buildDaysLayout() {
        String[] days;
        days = getResources().getStringArray(R.array.days_sunday_array);

        for (int i = 0; i < mLayoutDays.getChildCount(); i++) {
            TextView textView = (TextView) mLayoutDays.getChildAt(i);
            textView.setText(days[i]);
        }
    }

    private void buildGridView(CalendarMonth calendarMonth) {
        int row = CalendarMonth.NUMBER_OF_WEEKS_IN_MONTH;
        int col = CalendarMonth.NUMBER_OF_DAYS_IN_WEEK;
        mGridLayout.setRowCount(row);
        mGridLayout.setColumnCount(col);

        for (CalendarDate date : calendarMonth.getDays()) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = LayoutParams.WRAP_CONTENT;
            params.height = LayoutParams.WRAP_CONTENT;

            CalendarDayView dayView = new CalendarDayView(getContext(), date);
            dayView.setContentDescription(date.toString());
            dayView.setLayoutParams(params);
            dayView.setOnClickListener(this);
            decorateDayView(dayView, date, calendarMonth.getMonth());
            mGridLayout.addView(dayView);
        }
    }

    private void decorateDayView(CalendarDayView dayView, CalendarDate day, int month) {
        if (day.getMonth() != month) {
            dayView.setOtherMothTextColor();
            dayView.setGreyBackground();
        } else {
            dayView.setThisMothTextColor();
            dayView.setWhiteBackground();
        }

        if (day.isToday()) {
            dayView.setGreenBackground();
        } else {
            if (day.getMonth() != month){
                dayView.setGreyBackground();
            } else
            dayView.setWhiteBackground();
        }
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            mListener.onDayViewClick((CalendarDayView) view);
        }
    }
}
