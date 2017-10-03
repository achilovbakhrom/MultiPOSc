package com.jim.mpviews.utils;


import android.content.Context;

import com.jim.mpviews.R;

import java.util.Calendar;

/**
 * Created by Пользователь on 15.06.2017.
 */
public class DateUtils {

    public static String monthToString(int month, Context context) {
        switch (month) {
            case Calendar.JANUARY:
                return context.getString(R.string.january);

            case Calendar.FEBRUARY:
                return context.getString(R.string.february);

            case Calendar.MARCH:
                return context.getString(R.string.march);

            case Calendar.APRIL:
                return context.getString(R.string.april);

            case Calendar.MAY:
                return context.getString(R.string.may);

            case Calendar.JUNE:
                return context.getString(R.string.june);

            case Calendar.JULY:
                return context.getString(R.string.july);

            case Calendar.AUGUST:
                return context.getString(R.string.august);

            case Calendar.SEPTEMBER:
                return context.getString(R.string.september);

            case Calendar.OCTOBER:
                return context.getString(R.string.october);

            case Calendar.NOVEMBER:
                return context.getString(R.string.november);

            case Calendar.DECEMBER:
                return context.getString(R.string.december);
        }
        return "";
    }

    public static String dayOfWeekToString(int day, Context context) {
        switch (day) {
            case Calendar.SUNDAY:
                return context.getString(R.string.sunday);

            case Calendar.MONDAY:
                return context.getString(R.string.monday);

            case Calendar.TUESDAY:
                return context.getString(R.string.tuesday);

            case Calendar.WEDNESDAY:
                return context.getString(R.string.wednesday);

            case Calendar.THURSDAY:
                return context.getString(R.string.thursday);

            case Calendar.FRIDAY:
                return context.getString(R.string.friday);

            case Calendar.SATURDAY:
                return context.getString(R.string.saturday);
        }
        return "";
    }
}
