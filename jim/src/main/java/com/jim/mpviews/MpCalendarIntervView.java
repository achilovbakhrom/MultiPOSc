package com.jim.mpviews;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jim.mpviews.utils.SimpleDatePickerDialog;
import com.jim.mpviews.utils.SimpleDatePickerDialogFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by developer on 07.12.2017.
 */

public class MpCalendarIntervView extends RelativeLayout {
    Calendar calendar;
    Calendar indexCalendar;
    Calendar calendarToday;

    int fromDay = -1;
    int fromMonth = -1;
    int fromYear = -1;

    int toDay = -1;
    int toMonth = -1;
    int toYear = -1;
    private CalendarCallback calendarCallback;
    private Context context;

    public interface CalendarCallback{
        void dayChoised(int day,int month, int year);
    }
    int calendar_matrix_ids []= {
            R.id.tvOneAndOne,R.id.tvOneAndTwo,R.id.tvOneAndThree,R.id.tvOneAndFour,R.id.tvOneAndFive,R.id.tvOneAndSix,R.id.tvOneAndSeven,
            R.id.tvTwoAndOne,R.id.tvTwoAndTwo,R.id.tvTwoAndThree,R.id.tvTwoAndFour,R.id.tvTwoAndFive,R.id.tvTwoAndSix,R.id.tvTwoAndSeven,
            R.id.tvThreeAndOne,R.id.tvThreeAndTwo,R.id.tvThreeAndThree,R.id.tvThreeAndFour,R.id.tvThreeAndFive,R.id.tvThreeAndSix,R.id.tvThreeAndSeven,
            R.id.tvFourAndOne,R.id.tvFourAndTwo,R.id.tvFourAndThree,R.id.tvFourAndFour,R.id.tvFourAndFive,R.id.tvFourAndSix,R.id.tvFourAndSeven,
            R.id.tvFiveAndOne,R.id.tvFiveAndTwo,R.id.tvFiveAndThree,R.id.tvFiveAndFour,R.id.tvFiveAndFive,R.id.tvFiveAndSix,R.id.tvFiveAndSeven,
            R.id.tvSixAndOne,R.id.tvSixAndTwo,R.id.tvSixAndThree,R.id.tvSixAndFour,R.id.tvSixAndFive,R.id.tvSixAndSix,R.id.tvSixAndSeven
    };
    private boolean isEnd;
    public void initCallback(CalendarCallback calendarCallback){

        this.calendarCallback = calendarCallback;
    }
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
    public void setFrom(int day,int month,int year){
        fromDay = day;
        fromMonth = month;
        fromYear  = year;
    }
    public void setTo(int day,int month,int year){
        toDay = day;
        toMonth = month;
        toYear  = year;
    }

    public void init(Context context, AttributeSet attributeSet) {
        this.context = context;
        calendar = new GregorianCalendar();
        indexCalendar  = new GregorianCalendar();
        calendarToday  = new GregorianCalendar();

        LayoutInflater.from(context).inflate(R.layout.mp_calendar_interv, this);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        findViewById(R.id.tvMonthYear).setOnClickListener(view -> {
            SimpleDatePickerDialog datePickerDialog;
            datePickerDialog = new SimpleDatePickerDialog(context, (year, monthOfYear) -> {
                indexCalendar.set(Calendar.YEAR,year);
                indexCalendar.set(Calendar.MONTH,monthOfYear);
                setCurrenMonth(indexCalendar.get(Calendar.MONTH),indexCalendar.get(Calendar.YEAR));
            },indexCalendar.get(Calendar.YEAR),indexCalendar.get(Calendar.MONTH));
            datePickerDialog.show();
        });

        findViewById(R.id.ivLeft).setOnClickListener(view -> {
            indexCalendar.add(Calendar.MONTH,-1);
            setCurrenMonth(indexCalendar.get(Calendar.MONTH),indexCalendar.get(Calendar.YEAR));
        });
        findViewById(R.id.ivRigth).setOnClickListener(view -> {
            indexCalendar.add(Calendar.MONTH,1);
            setCurrenMonth(indexCalendar.get(Calendar.MONTH),indexCalendar.get(Calendar.YEAR));
        });
    }
    public void setUnderlineText(TextView textView, String text){
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
    }
    public void viewPosition(boolean isEnd){
        this.isEnd = isEnd;
        if(isEnd) {
                indexCalendar.set(Calendar.MONTH, toMonth);
                indexCalendar.set(Calendar.YEAR, toYear);
        }else {
                indexCalendar.set(Calendar.MONTH, fromMonth);
                indexCalendar.set(Calendar.YEAR, fromYear);
            if(toMonth == fromMonth && toYear == fromYear)
                indexCalendar.add(Calendar.MONTH,-1);
        }
        setCurrenMonth(indexCalendar.get(Calendar.MONTH),indexCalendar.get(Calendar.YEAR));

    }

    public void update(){
        setCurrenMonth(indexCalendar.get(Calendar.MONTH),indexCalendar.get(Calendar.YEAR));
    }


    public void setCurrenMonth(int setMonth,int setYear){
        int currentDate = calendarToday.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendarToday.get(Calendar.MONTH);
        int currentYear = calendarToday.get(Calendar.YEAR);

        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.MONTH,setMonth);
        calendar.set(Calendar.YEAR,setYear);

        setUnderlineText((TextView) findViewById(R.id.tvMonthYear),monthName(calendar.get(Calendar.MONTH))+"  "+ String.valueOf(calendar.get(Calendar.YEAR)));


        boolean firtLine = false;
        boolean secondLine = false;
        int currentWeekDate = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;
        calendar.add(Calendar.DAY_OF_MONTH,-currentWeekDate);
        for (int i = 0; i <calendar_matrix_ids.length; i++) {
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            ((TextView)findViewById(calendar_matrix_ids[i])).setText(String.valueOf(day));
            calendar.add(Calendar.DAY_OF_MONTH,1);
            if(day == 1 && firtLine){
                secondLine = true;
            }
            if(day == 1 && !firtLine){
                firtLine = true;
            }
            if(!firtLine){
                ((TextView)findViewById(calendar_matrix_ids[i])).setTextColor(Color.parseColor("#c3c3c3"));
                findViewById(calendar_matrix_ids[i]).setBackgroundColor(Color.parseColor("#ffffff"));
                findViewById(calendar_matrix_ids[i]).setEnabled(false);

            }else
            if(!secondLine){
                ((TextView)findViewById(calendar_matrix_ids[i])).setTextColor(Color.parseColor("#212121"));

                if(((long)fromMonth*fromDay*fromYear*toYear*toMonth*toDay)>=0 ) {
                    long currentIndecator = year * 365 + month * 30 + day;
                    long indecatorFrom = fromYear * 365 + fromMonth * 30 + fromDay;
                    long indecatorTo = toYear * 365 + toMonth * 30 + toDay;

                    if (currentIndecator < indecatorTo && currentIndecator > indecatorFrom) {
                        findViewById(calendar_matrix_ids[i]).setBackgroundColor(Color.parseColor("#bff4ff"));
                    } else {
                        findViewById(calendar_matrix_ids[i]).setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                    if(day== currentDate && month == currentMonth && year == currentYear){
                        ((TextView)findViewById(calendar_matrix_ids[i])).setTextColor(Color.parseColor("#36a614"));
                    }
                    if(day == fromDay && month == fromMonth && year == fromYear){
                        findViewById(calendar_matrix_ids[i]).setBackgroundResource(R.drawable.rounded_calendar_indecator_from);
                        ((TextView) findViewById(calendar_matrix_ids[i])).setTextColor(Color.parseColor("#ffffff"));


                    }

                    if(day == toDay && month == toMonth && year == toYear){
                        findViewById(calendar_matrix_ids[i]).setBackgroundResource(R.drawable.rounded_calendar_indecator_to);
                        ((TextView) findViewById(calendar_matrix_ids[i])).setTextColor(Color.parseColor("#ffffff"));

                    }
                    if(day == toDay && month == toMonth && year == toYear && day == fromDay && month == fromMonth && year == fromYear){
                        findViewById(calendar_matrix_ids[i]).setBackgroundResource(R.drawable.rounded_calendar_indecator);
                        ((TextView) findViewById(calendar_matrix_ids[i])).setTextColor(Color.parseColor("#ffffff"));
                    }



                }else {
                    findViewById(calendar_matrix_ids[i]).setBackgroundColor(Color.parseColor("#ffffff"));



                    if(day== currentDate && month == currentMonth && year == currentYear){
                        ((TextView)findViewById(calendar_matrix_ids[i])).setTextColor(Color.parseColor("#36a614"));
                    }
                    if(fromDay*fromMonth*fromYear>=0 && day == fromDay && month == fromMonth && year == fromYear){
                        findViewById(calendar_matrix_ids[i]).setBackgroundResource(R.drawable.rounded_calendar_indecator);
                        ((TextView) findViewById(calendar_matrix_ids[i])).setTextColor(Color.parseColor("#ffffff"));
                    }

                    if(toDay*toMonth*toYear >=0 && day == toDay && month == toMonth && year == toYear){
                        findViewById(calendar_matrix_ids[i]).setBackgroundResource(R.drawable.rounded_calendar_indecator);
                        ((TextView) findViewById(calendar_matrix_ids[i])).setTextColor(Color.parseColor("#ffffff"));
                    }

                }

                findViewById(calendar_matrix_ids[i]).setEnabled(true);

                findViewById(calendar_matrix_ids[i]).setOnClickListener(view -> {
                    for (int j = 0; j < calendar_matrix_ids.length; j++) {
                        if(view.getId() == calendar_matrix_ids[j]){
                            indexCalendar.set(Calendar.DAY_OF_MONTH,1);
                            int differency = (indexCalendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;
                            int today = j - differency+1;
                            calendarCallback.dayChoised(today,indexCalendar.get(Calendar.MONTH),indexCalendar.get(Calendar.YEAR));
                            break;
                        }

                    }
                });
            }else {
                ((TextView)findViewById(calendar_matrix_ids[i])).setTextColor(Color.parseColor("#c3c3c3"));
                findViewById(calendar_matrix_ids[i]).setBackgroundColor(Color.parseColor("#ffffff"));
                findViewById(calendar_matrix_ids[i]).setEnabled(false);

            }






        }

    }

    private String monthName(int month){
        switch (month){
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
            default:
                return "ERROR";
        }
    }



}
