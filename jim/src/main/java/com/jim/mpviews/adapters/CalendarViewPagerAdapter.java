package com.jim.mpviews.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.jim.mpviews.calendar.CalendarDayView;
import com.jim.mpviews.calendar.CalendarMonthView;
import com.jim.mpviews.interfaces.OnDateSelectedListener;
import com.jim.mpviews.interfaces.OnDayViewClickListener;
import com.jim.mpviews.utils.CalendarDate;
import com.jim.mpviews.utils.CalendarMonth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.view.View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION;


public class CalendarViewPagerAdapter extends PagerAdapter implements OnDayViewClickListener {

    private ViewPager mViewPager;
    private List<CalendarMonth> mData;
    private CalendarDate mSelectedDate;
    private Context context;
    private OnDateSelectedListener mListener;

    public CalendarViewPagerAdapter(List<CalendarMonth> list, ViewPager viewPager, Context context) {
        mData = list;
        this.context = context;
        mViewPager = viewPager;
        mSelectedDate = new CalendarDate(Calendar.getInstance());
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        CalendarMonth month = mData.get(position);
        CalendarMonthView monthView = new CalendarMonthView(container.getContext());
        monthView.setSelectedDate(mSelectedDate);
        monthView.setOnDayViewClickListener(this);
        monthView.buildView(month);
        (container).addView(monthView, 0);
        monthView.setTag(month);
        return monthView;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        View view = (View) object;
        CalendarMonth month = (CalendarMonth) view.getTag();
        int position = mData.indexOf(month);

        if (position >= 0) {
            return position;
        } else {
            return POSITION_NONE;
        }
    }

    public void addNext(CalendarMonth month) {
        mData.add(month);
        notifyDataSetChanged();
    }

    public void addPrev(CalendarMonth month) {
        mData.add(0, month);
        notifyDataSetChanged();
    }

    public String getItemPageHeader(int position) {
        return mData.get(position).parseToString(context);
    }

    public CalendarMonth getItem(int position) {
        return mData.get(position);
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        mListener = listener;

        if (mListener != null) {
            mListener.onDateSelected(new CalendarDate(mSelectedDate));
        }
    }

    @Override
    public void onDayViewClick(CalendarDayView view) {
      //   unset old selection
      //  decorateSelection(mSelectedDate.toString(), false);

        // set new selection
        mSelectedDate = view.getDate();
        //decorateSelection(mSelectedDate.toString(), true);

        if (mListener != null) {
            mListener.onDateSelected(new CalendarDate(mSelectedDate));
        }
    }

    private void decorateSelection(String tag, boolean isSelected) {
        ArrayList<View> output = new ArrayList<>();
        mViewPager.findViewsWithText(output, tag, FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        for (View outputView : output) {
            CalendarDayView dayView = (CalendarDayView) outputView;
            if (isSelected) {
                dayView.setGreenBackground();
            } else {
                dayView.setWhiteBackground();
            }

        }
    }
}
