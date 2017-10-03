package com.jim.mpviews.calendar;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jim.mpviews.R;
import com.jim.mpviews.adapters.CalendarViewPagerAdapter;
import com.jim.mpviews.interfaces.OnDateSelectedListener;
import com.jim.mpviews.utils.CalendarMonth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Пользователь on 15.06.2017.
 */

public class MpCalendarView extends FrameLayout implements View.OnClickListener {

    private TextView mPagerTextMonth;
    private ImageButton mButtonLeftArrow;
    private ImageButton mButtonRightArrow;
    private ViewPager mViewPager;
    private CalendarViewPagerAdapter mViewPagerAdapter;
    private OnDateSelectedListener mListener;
    private Context context;

    public MpCalendarView(@NonNull Context context) {
        super(context);
        init();
    }

    public MpCalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MpCalendarView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        context = getContext();
        inflate(getContext(), R.layout.mp_custom_calendar, this);
        mViewPager = (ViewPager) findViewById(R.id.mpViewPager);
        mPagerTextMonth = (TextView) findViewById(R.id.mpMonth);
        mButtonLeftArrow = (ImageButton) findViewById(R.id.mpBtnLeft);
        mButtonRightArrow = (ImageButton) findViewById(R.id.mpBtnRight);
        mButtonLeftArrow.setOnClickListener(this);
        mButtonRightArrow.setOnClickListener(this);
        buildCalendarView();
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        mViewPagerAdapter.setOnDateSelectedListener(listener);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.mpBtnRight) {
            int next = mViewPager.getCurrentItem() + 1;
            mViewPager.setCurrentItem(next, true);

        } else if (i == R.id.mpBtnLeft) {
            int prev = mViewPager.getCurrentItem() - 1;
            mViewPager.setCurrentItem(prev, true);

        }
    }

    private void buildCalendarView() {
        List<CalendarMonth> list = new ArrayList<>();
        CalendarMonth today = new CalendarMonth(Calendar.getInstance());

        list.add(new CalendarMonth(today, -2));
        list.add(new CalendarMonth(today, -1));
        list.add(today);
        list.add(new CalendarMonth(today, 1));
        list.add(new CalendarMonth(today, 2));

        mViewPagerAdapter = new CalendarViewPagerAdapter(list, mViewPager, context);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(2);
        mPagerTextMonth.setText(mViewPagerAdapter.getItemPageHeader(2));
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            int position = mViewPager.getCurrentItem();
            mPagerTextMonth.setText(mViewPagerAdapter.getItemPageHeader(position));

            // current item is the first item in the list
            if (state == ViewPager.SCROLL_STATE_IDLE && position == 1) {
                addPrev();
            }

            // current item is the last item in the list
            if (state == ViewPager.SCROLL_STATE_IDLE && position == mViewPagerAdapter.getCount() - 2) {
                addNext();
            }

        }
    };

    private void addNext() {
        CalendarMonth month = mViewPagerAdapter.getItem(mViewPagerAdapter.getCount() - 1);
        mViewPagerAdapter.addNext(new CalendarMonth(month, 1));
    }

    private void addPrev() {
        CalendarMonth month = mViewPagerAdapter.getItem(0);
        mViewPagerAdapter.addPrev(new CalendarMonth(month, -1));
    }

}
