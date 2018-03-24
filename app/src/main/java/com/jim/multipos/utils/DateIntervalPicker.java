package com.jim.multipos.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jim.mpviews.MpButtonLong;
import com.jim.mpviews.MpCalendarIntervView;
import com.jim.mpviews.utils.SimpleTimePickerDialog;
import com.jim.multipos.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by developer on 04.12.2017.
 */

public class DateIntervalPicker extends Dialog {
    private View dialogView;
    @BindView(R.id.calLeft)
    MpCalendarIntervView calLeft;
    @BindView(R.id.calRight)
    MpCalendarIntervView calRight;
    SimpleDateFormat simpleDateFormat;
    @BindView(R.id.btnToday)
    MpButtonLong btnToday;
    @BindView(R.id.btnYesterday)
    MpButtonLong btnYesterday;
    @BindView(R.id.btnLastWeek)
    MpButtonLong btnLastWeek;
    @BindView(R.id.btnLastMonth)
    MpButtonLong btnLastMonth;
    @BindView(R.id.btnThisMonth)
    MpButtonLong btnThisMonth;
    @BindView(R.id.btnLastYear)
    MpButtonLong btnLastYear;

    @BindView(R.id.tvFromTime)
    TextView tvFromTime;
    @BindView(R.id.tvCurrentIntervalFrom)
    TextView tvCurrentIntervalFrom;
    @BindView(R.id.divider)
    TextView divider;
    @BindView(R.id.tvToTime)
    TextView tvToTime;
    @BindView(R.id.tvCurrentIntervalTo)
    TextView tvCurrentIntervalTo;

    boolean fromDateCurrent  = false, toDateCurrent = false;
    Calendar fromDate;
    Calendar toDate ;

    Calendar fromTime;
    Calendar toTime;

    int clicked = 0;
    Date currentDate;
    private CallbackIntervalPicker callbackIntervalPicker;
    SimpleDateFormat  timeSimple = new SimpleDateFormat("HH:mm");

    public interface CallbackIntervalPicker{
        void dateIntervalPicked(Calendar fromDate,Calendar toDate);
        void datePicked(Calendar pickedDate);
    }
    public DateIntervalPicker(@NonNull Context context,Calendar fromDateInstance,Calendar toDateInstance,CallbackIntervalPicker callbackIntervalPicker) {
        super(context);
        this.callbackIntervalPicker = callbackIntervalPicker;
        simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");

        dialogView = getLayoutInflater().inflate(R.layout.date_interval_picker_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        currentDate = new Date();
        fromDate =  new GregorianCalendar();
        toDate = new GregorianCalendar();

        fromTime = new GregorianCalendar();
        toTime = new GregorianCalendar();

        fromTime.set(Calendar.HOUR_OF_DAY, 0);
        fromTime.set(Calendar.MINUTE, 0);
        fromTime.set(Calendar.SECOND, 0);

        toTime.set(Calendar.HOUR_OF_DAY,23);
        toTime.set(Calendar.MINUTE,59);
        toTime.set(Calendar.SECOND,59);

        if(fromDateInstance == null && toDateInstance == null){


            toDate.setTime(currentDate);
            fromDate.setTime(currentDate);
            setDatas();
            updateCalendars();
            fromDateCurrent = true;
            toDateCurrent = false;

        }else if(fromDateInstance != null && toDateInstance == null){
            fromDate.setTimeInMillis(fromDateInstance.getTimeInMillis());
            toDate.setTimeInMillis(fromDateInstance.getTimeInMillis());


            setDatas();
            updateCalendars();
            fromDateCurrent = true;
            toDateCurrent = false;
        }else {
            fromDate.setTimeInMillis(fromDateInstance.getTimeInMillis());
            toDate.setTimeInMillis(toDateInstance.getTimeInMillis());

            fromTime.setTimeInMillis(fromDateInstance.getTimeInMillis());
            toTime.setTimeInMillis(toDateInstance.getTimeInMillis());

            setDatas();
            updateCalendars();
            fromDateCurrent = true;
            toDateCurrent = true;
        }
        updateCurrentDate();

        tvFromTime.setOnClickListener((view)->{
            SimpleTimePickerDialog simpleTimePickerDialog = new SimpleTimePickerDialog(getContext(), fromTime.get(Calendar.HOUR_OF_DAY), fromTime.get(Calendar.MINUTE), new SimpleTimePickerDialog.SimpleDatePickerListner() {
                @Override
                public void onOk(int hour, int minut) {
                    fromTime.set(Calendar.HOUR_OF_DAY,hour);
                    fromTime.set(Calendar.MINUTE,minut);
                    setUnderlineText(tvFromTime,timeSimple.format(fromTime.getTime()));
                    setUnderlineText(tvToTime,timeSimple.format(toTime.getTime()));
                }
            });
            simpleTimePickerDialog.show();

        });
        tvToTime.setOnClickListener((view)->{
            SimpleTimePickerDialog simpleTimePickerDialog = new SimpleTimePickerDialog(getContext(), toTime.get(Calendar.HOUR_OF_DAY), toTime.get(Calendar.MINUTE), new SimpleTimePickerDialog.SimpleDatePickerListner() {
                @Override
                public void onOk(int hour, int minut) {
                    toTime.set(Calendar.HOUR_OF_DAY,hour);
                    toTime.set(Calendar.MINUTE,minut);
                    setUnderlineText(tvFromTime,timeSimple.format(fromTime.getTime()));
                    setUnderlineText(tvToTime,timeSimple.format(toTime.getTime()));
                }
            });
            simpleTimePickerDialog.show();

        });
        dialogView.findViewById(R.id.btnWarningYES).setOnClickListener(view -> {
            if(clicked >0){
                if(callbackIntervalPicker !=null){
                    toDate = (Calendar) fromDate.clone();

                    fromDate.set(Calendar.HOUR_OF_DAY,fromTime.get(Calendar.HOUR_OF_DAY));
                    fromDate.set(Calendar.MINUTE,fromTime.get(Calendar.MINUTE));

                    toDate.set(Calendar.HOUR_OF_DAY,toTime.get(Calendar.HOUR_OF_DAY));
                    toDate.set(Calendar.MINUTE,toTime.get(Calendar.MINUTE));
                    if(fromDate.getTimeInMillis()>toDate.getTimeInMillis())
                        callbackIntervalPicker.dateIntervalPicked(toDate,fromDate);
                    else callbackIntervalPicker.dateIntervalPicked(fromDate,toDate);


                }
            }else if(clicked == 0){
                if(callbackIntervalPicker!=null){
                    fromDate.set(Calendar.HOUR_OF_DAY,fromTime.get(Calendar.HOUR_OF_DAY));
                    fromDate.set(Calendar.MINUTE,fromTime.get(Calendar.MINUTE));

                    toDate.set(Calendar.HOUR_OF_DAY,toTime.get(Calendar.HOUR_OF_DAY));
                    toDate.set(Calendar.MINUTE,toTime.get(Calendar.MINUTE));
                    if(fromDate.getTimeInMillis()>toDate.getTimeInMillis())
                        callbackIntervalPicker.dateIntervalPicked(toDate,fromDate);
                    else callbackIntervalPicker.dateIntervalPicked(fromDate,toDate);
                }
            }
            dismiss();
        });
        dialogView.findViewById(R.id.btnWarningNO).setOnClickListener(view -> {
            dismiss();
        });
        calRight.initCallback((day, month, year) -> {
            long currentIndecator = year * 365 + month * 30 + day;

            long indecatorFrom = fromDate.get(Calendar.YEAR) * 365 + fromDate.get(Calendar.MONTH) * 30 + fromDate.get(Calendar.DAY_OF_MONTH);
            if(clicked == 0){
                clicked++;
                fromDate.set(Calendar.YEAR,year);
                fromDate.set(Calendar.MONTH,month);
                fromDate.set(Calendar.DAY_OF_MONTH,day);
                calLeft.setTo(-1,-1,-1);
                calRight.setTo(-1,-1,-1);
                calLeft.setFrom(fromDate.get(Calendar.DAY_OF_MONTH),fromDate.get(Calendar.MONTH),fromDate.get(Calendar.YEAR));
                calRight.setFrom(fromDate.get(Calendar.DAY_OF_MONTH),fromDate.get(Calendar.MONTH),fromDate.get(Calendar.YEAR));
                calLeft.update();
                calRight.update();
                fromDateCurrent = true;
                toDateCurrent = false;
                updateCurrentDate();
            }else{
                clicked = 0;
                if(currentIndecator>indecatorFrom){
                    toDate.set(Calendar.YEAR,year);
                    toDate.set(Calendar.MONTH,month);
                    toDate.set(Calendar.DAY_OF_MONTH,day);
                }
                else {
                    toDate.set(Calendar.YEAR,fromDate.get(Calendar.YEAR));
                    toDate.set(Calendar.MONTH,fromDate.get(Calendar.MONTH));
                    toDate.set(Calendar.DAY_OF_MONTH,fromDate.get(Calendar.DAY_OF_MONTH));
                    fromDate.set(Calendar.YEAR,year);
                    fromDate.set(Calendar.MONTH,month);
                    fromDate.set(Calendar.DAY_OF_MONTH,day);
                }
                calLeft.setTo(toDate.get(Calendar.DAY_OF_MONTH),toDate.get(Calendar.MONTH),toDate.get(Calendar.YEAR));
                calRight.setTo(toDate.get(Calendar.DAY_OF_MONTH),toDate.get(Calendar.MONTH),toDate.get(Calendar.YEAR));
                calLeft.setFrom(fromDate.get(Calendar.DAY_OF_MONTH),fromDate.get(Calendar.MONTH),fromDate.get(Calendar.YEAR));
                calRight.setFrom(fromDate.get(Calendar.DAY_OF_MONTH),fromDate.get(Calendar.MONTH),fromDate.get(Calendar.YEAR));
                calRight.update();
                calLeft.update();
                fromDateCurrent = true;
                toDateCurrent = true;
                updateCurrentDate();
            }
        });
        calLeft.initCallback((day, month, year) -> {
            long currentIndecator = year * 365 + month * 30 + day;
            long indecatorFrom = fromDate.get(Calendar.YEAR) * 365 + fromDate.get(Calendar.MONTH) * 30 + fromDate.get(Calendar.DAY_OF_MONTH);
            if(clicked == 0){
                clicked++;
                fromDate.set(Calendar.YEAR,year);
                fromDate.set(Calendar.MONTH,month);
                fromDate.set(Calendar.DAY_OF_MONTH,day);
                calLeft.setTo(-1,-1,-1);
                calRight.setTo(-1,-1,-1);
                calLeft.setFrom(fromDate.get(Calendar.DAY_OF_MONTH),fromDate.get(Calendar.MONTH),fromDate.get(Calendar.YEAR));
                calRight.setFrom(fromDate.get(Calendar.DAY_OF_MONTH),fromDate.get(Calendar.MONTH),fromDate.get(Calendar.YEAR));
                calLeft.update();
                calRight.update();
                fromDateCurrent = true;
                toDateCurrent = false;
                updateCurrentDate();
            }else{
                clicked = 0;
                if(currentIndecator>indecatorFrom){
                    toDate.set(Calendar.YEAR,year);
                    toDate.set(Calendar.MONTH,month);
                    toDate.set(Calendar.DAY_OF_MONTH,day);
                }
                else {
                    toDate.set(Calendar.YEAR,fromDate.get(Calendar.YEAR));
                    toDate.set(Calendar.MONTH,fromDate.get(Calendar.MONTH));
                    toDate.set(Calendar.DAY_OF_MONTH,fromDate.get(Calendar.DAY_OF_MONTH));
                    fromDate.set(Calendar.YEAR,year);
                    fromDate.set(Calendar.MONTH,month);
                    fromDate.set(Calendar.DAY_OF_MONTH,day);
                }
                calLeft.setTo(toDate.get(Calendar.DAY_OF_MONTH),toDate.get(Calendar.MONTH),toDate.get(Calendar.YEAR));
                calRight.setTo(toDate.get(Calendar.DAY_OF_MONTH),toDate.get(Calendar.MONTH),toDate.get(Calendar.YEAR));
                calLeft.setFrom(fromDate.get(Calendar.DAY_OF_MONTH),fromDate.get(Calendar.MONTH),fromDate.get(Calendar.YEAR));
                calRight.setFrom(fromDate.get(Calendar.DAY_OF_MONTH),fromDate.get(Calendar.MONTH),fromDate.get(Calendar.YEAR));
                calRight.update();
                calLeft.update();
                fromDateCurrent = true;
                toDateCurrent = true;
                updateCurrentDate();
            }
        });
        btnToday.setOnClickListener(view -> {
            toDate.setTime(currentDate);
            fromDate.setTime(currentDate);
            timeInit();
            setDatas();
            updateCalendars();
            fromDateCurrent = true;
            toDateCurrent = true;
            updateCurrentDate();
        });
        btnYesterday.setOnClickListener(view -> {
            toDate.setTime(currentDate);
            fromDate.setTime(currentDate);
            toDate.add(Calendar.DAY_OF_MONTH,-1);
            fromDate.add(Calendar.DAY_OF_MONTH,-1);
            timeInit();
            setDatas();
            updateCalendars();
            fromDateCurrent = true;
            toDateCurrent = true;
            updateCurrentDate();
        });
        btnLastWeek.setOnClickListener(view -> {
            toDate.setTime(currentDate);
            fromDate.setTime(currentDate);
            fromDate.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            timeInit();
            setDatas();
            updateCalendars();
            fromDateCurrent = true;
            toDateCurrent = true;
            updateCurrentDate();
        });
        btnLastMonth.setOnClickListener(view -> {
            toDate.setTime(currentDate);
            fromDate.setTime(currentDate);
            fromDate.add(Calendar.DAY_OF_MONTH,-30);
            timeInit();
            setDatas();
            updateCalendars();
            fromDateCurrent = true;
            toDateCurrent = true;
            updateCurrentDate();
        });
        btnThisMonth.setOnClickListener(view -> {
            toDate.setTime(currentDate);
            fromDate.setTime(currentDate);
            fromDate.set(Calendar.DAY_OF_MONTH,1);
            timeInit();
            setDatas();
            updateCalendars();
            fromDateCurrent = true;
            toDateCurrent = true;
            updateCurrentDate();
        });
        btnLastYear.setOnClickListener(view -> {
            toDate.setTime(currentDate);
            fromDate.setTime(currentDate);
            fromDate.set(Calendar.DAY_OF_YEAR,1);
            timeInit();
            setDatas();
            updateCalendars();
            fromDateCurrent = true;
            toDateCurrent = true;
            updateCurrentDate();
        });

        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

    }
    private void setDatas(){
        calLeft.setFrom(fromDate.get(Calendar.DAY_OF_MONTH),fromDate.get(Calendar.MONTH),fromDate.get(Calendar.YEAR));
        calLeft.setTo(toDate.get(Calendar.DAY_OF_MONTH),toDate.get(Calendar.MONTH),toDate.get(Calendar.YEAR));
        calRight.setFrom(fromDate.get(Calendar.DAY_OF_MONTH),fromDate.get(Calendar.MONTH),fromDate.get(Calendar.YEAR));
        calRight.setTo(toDate.get(Calendar.DAY_OF_MONTH),toDate.get(Calendar.MONTH),toDate.get(Calendar.YEAR));

    }
    private void updateCalendars(){
        calLeft.viewPosition(false);
        calRight.viewPosition(true);
    }
    private void updateCurrentDate(){
        String date = "";
        if(fromDateCurrent && !toDateCurrent){
            String format = simpleDateFormat.format(fromDate.getTime());
            date =  format.substring(0, 1).toUpperCase() + format.substring(1);
            tvCurrentIntervalTo.setText(date);
            tvCurrentIntervalFrom.setText(date);
            setUnderlineText(tvFromTime,timeSimple.format(fromTime.getTime()));
            setUnderlineText(tvToTime,timeSimple.format(toTime.getTime()));
        }else if(fromDateCurrent && toDateCurrent){
            long indecatorTo = toDate.get(Calendar.YEAR) * 365 + toDate.get(Calendar.MONTH) * 30 + toDate.get(Calendar.DAY_OF_MONTH);
            long indecatorFrom = fromDate.get(Calendar.YEAR) * 365 + fromDate.get(Calendar.MONTH) * 30 + fromDate.get(Calendar.DAY_OF_MONTH);
            if(indecatorTo == indecatorFrom){
                String format = simpleDateFormat.format(fromDate.getTime());
                date =  format.substring(0, 1).toUpperCase() + format.substring(1);
                tvCurrentIntervalTo.setText(date);
                tvCurrentIntervalFrom.setText(date);
                setUnderlineText(tvFromTime,timeSimple.format(fromTime.getTime()));
                setUnderlineText(tvToTime,timeSimple.format(toTime.getTime()));

            }else {

                String format = simpleDateFormat.format(fromDate.getTime());
                String format1 = simpleDateFormat.format(toDate.getTime());

                tvCurrentIntervalTo.setText(format.substring(0, 1).toUpperCase() + format.substring(1) );
                tvCurrentIntervalFrom.setText(format1.substring(0, 1).toUpperCase() + format1.substring(1));
                setUnderlineText(tvFromTime,timeSimple.format(fromTime.getTime()));
                setUnderlineText(tvToTime,timeSimple.format(toTime.getTime()));
            }

        }

    }
    public void setUnderlineText(TextView textView, String text){
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
    }
    public void timeInit(){
        fromTime.set(Calendar.HOUR_OF_DAY, 0);
        fromTime.set(Calendar.MINUTE, 0);
        fromTime.set(Calendar.SECOND, 0);

        toTime.set(Calendar.HOUR_OF_DAY,23);
        toTime.set(Calendar.MINUTE,59);
        toTime.set(Calendar.SECOND,59);
    }
}
