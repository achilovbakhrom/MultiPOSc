package com.jim.mpviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by DEV on 30.06.2017.
 */

public class MpToolbar extends RelativeLayout {
    public static final int MAIN_PAGE_TYPE = 1;
    public static final int WITH_SEARCH_TYPE = 2;
    public static final int DEFAULT_TYPE = 3;
    public static final int WITH_CALENDAR_TYPE = 4;
    public static final int WITH_SEARCH_CALENDAR_TYPE = 5;
    public static final int GONE_TYPE = 0;
    public static final int DEFAULT_TYPE_TWO_SECTION = 6;
    public static final int SEARCH_MODE_TYPE = 7;

    public static final int DEFAULT_MODE = 10;
    public static final int MAIN_MODE = 11;
    public static final int BACK_ARROW_MODE = 12;
    public static final int ADMIN_MODE = 13;
    public static final int PAYMENT_MODE = 14;
    public static final int BALANCE_MODE = 15;
    public static final int BACK_ARROW_WITH_ICON = 16;
    boolean pressed = false;
    boolean isSearchFragmentOpened = false;
    private SimpleDateFormat simpleDateFormat;
    private int mode;
    private LinearLayout mpMainMenu, llEmployer, llDateIntervalPicker, mpProfile;
    private RelativeLayout mpSearch, mpProducts, mpCustomers, mpReports, mpInventory;
    private RelativeLayout mpLeftSide, mpRightSide, rlBackgroun;
    private ImageView mpSettings;
    private MpHorizontalScroller mpHorizontalScroller;
    private MpSearchView mpSearchView;
    private Calendar from;
    private Calendar to;
    private DataIntervalCallbackToToolbar dataIntervalPicker;
    private TextView tvPeriod;
    private CallbackSearchFragmentClick onSearchClickListener;
    private OnBackArrowClick onBackArrowClick;

    public MpToolbar(Context context) {
        super(context);
        init(context, null);
    }

    public MpToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MpToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public MpToolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public EditText getSearchEditText() {
        return mpSearchView.getSearchView();
    }

    public ImageView getBarcodeView() {
        return mpSearchView.getBarcodeView();
    }

    public void init(Context context, AttributeSet attributeSet) {
        LayoutInflater.from(context).inflate(R.layout.mp_toolbar, this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.MpToolbar);
        mpMainMenu = findViewById(R.id.mpMainMenu);
        llEmployer = findViewById(R.id.llEmployer);
        llDateIntervalPicker = findViewById(R.id.llDateIntervalPicker);
        mpSearch = findViewById(R.id.mpSearch);
        mpInventory = findViewById(R.id.mpInventory);
        mpReports = findViewById(R.id.mpReports);
        mpProducts = findViewById(R.id.mpProducts);
        mpCustomers = findViewById(R.id.mpCustomers);
        mpSettings = findViewById(R.id.mpSettings);
        mpHorizontalScroller = findViewById(R.id.mpHorRoller);
        mpSearchView = findViewById(R.id.mpSearchView);
        mpLeftSide = findViewById(R.id.mpLeftSide);
        mpRightSide = findViewById(R.id.mpRightSide);
        tvPeriod = findViewById(R.id.tvPeriod);
        rlBackgroun = findViewById(R.id.rlBackgroun);
        mode = array.getInt(R.styleable.MpToolbar_view_mode, DEFAULT_MODE);
        setMode(mode);
        simpleDateFormat = new SimpleDateFormat(" MMM dd, yyyy");
        mpSettings.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!pressed) {
                        pressed = true;
                    }
                    mpSettings.setImageResource(R.drawable.settings_blue_press);
                    return false;
                case MotionEvent.ACTION_UP:
                    pressed = false;
                    mpSettings.setImageResource(R.drawable.settings_blue);
                    return false;
            }
            return false;
        });
        findViewById(R.id.mpProducts).setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!pressed) {
                        pressed = true;
                    }
                    findViewById(R.id.productLine).setVisibility(GONE);
                    findViewById(R.id.productPressed).setVisibility(VISIBLE);
                    return false;
                case MotionEvent.ACTION_UP:
                    pressed = false;
                    findViewById(R.id.productLine).setVisibility(VISIBLE);
                    findViewById(R.id.productPressed).setVisibility(GONE);
                    return false;
            }
            return false;
        });


        findViewById(R.id.mpInventory).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!pressed) {
                            pressed = true;
                        }
                        findViewById(R.id.inventoryLine).setVisibility(GONE);
                        findViewById(R.id.inventoryPressed).setVisibility(VISIBLE);
                        return false;
                    case MotionEvent.ACTION_UP:
                        pressed = false;
                        findViewById(R.id.inventoryLine).setVisibility(VISIBLE);
                        findViewById(R.id.inventoryPressed).setVisibility(GONE);
                        return false;
                }
                return false;
            }
        });

        findViewById(R.id.mpCustomers).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!pressed) {
                            pressed = true;
                        }
                        findViewById(R.id.customerLine).setVisibility(GONE);
                        findViewById(R.id.customerPressed).setVisibility(VISIBLE);
                        return false;
                    case MotionEvent.ACTION_UP:
                        pressed = false;
                        findViewById(R.id.customerLine).setVisibility(VISIBLE);
                        findViewById(R.id.customerPressed).setVisibility(GONE);
                        return false;
                }
                return false;
            }
        });
        findViewById(R.id.mpReports).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!pressed) {
                            pressed = true;
                        }
                        findViewById(R.id.reportLine).setVisibility(GONE);
                        findViewById(R.id.reportPressed).setVisibility(VISIBLE);
                        return false;
                    case MotionEvent.ACTION_UP:
                        pressed = false;
                        findViewById(R.id.reportLine).setVisibility(VISIBLE);
                        findViewById(R.id.reportPressed).setVisibility(GONE);
                        return false;
                }
                return false;
            }
        });
        findViewById(R.id.mpInfo).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!pressed) {
                            pressed = true;
                        }
                        findViewById(R.id.whiteLine).setVisibility(GONE);
                        findViewById(R.id.infoPressed).setVisibility(VISIBLE);
                        return false;
                    case MotionEvent.ACTION_UP:
                        pressed = false;
                        findViewById(R.id.whiteLine).setVisibility(VISIBLE);
                        findViewById(R.id.infoPressed).setVisibility(GONE);
                        return false;
                }
                return false;
            }
        });
        findViewById(R.id.mpSearch).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pressed = false;
                        if (!isSearchFragmentOpened) {
                            pressed = true;
                            onSearchClickListener.onOpen();
                            isSearchFragmentOpened = true;
                            findViewById(R.id.searchLine).setVisibility(GONE);
                            findViewById(R.id.searchPressed).setVisibility(VISIBLE);
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (pressed) {
                            pressed = false;
                            return false;
                        }
                        if (isSearchFragmentOpened) {
                            onSearchClickListener.onClose();
                            isSearchFragmentOpened = false;
                            findViewById(R.id.searchLine).setVisibility(VISIBLE);
                            findViewById(R.id.searchPressed).setVisibility(GONE);
                        }
                        return false;
                }
                return false;
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> onBackArrowClick.onClick());

        array.recycle();
    }

    public void setOnBackArrowClick(OnBackArrowClick onBackArrowClick) {
        this.onBackArrowClick = onBackArrowClick;
    }

    public void setSearchClosedMode() {
        if (pressed) {
            pressed = false;
        }
        if (isSearchFragmentOpened) {
            isSearchFragmentOpened = false;
            findViewById(R.id.searchLine).setVisibility(VISIBLE);
            findViewById(R.id.searchPressed).setVisibility(GONE);
        }
    }

    public void enableSearchButton() {
        isSearchFragmentOpened = true;
        findViewById(R.id.searchLine).setVisibility(GONE);
        findViewById(R.id.searchPressed).setVisibility(VISIBLE);
    }

    public void disableSearchButton() {
        isSearchFragmentOpened = false;
        findViewById(R.id.searchLine).setVisibility(VISIBLE);
        findViewById(R.id.searchPressed).setVisibility(GONE);
    }

    public void setOnProductClickListener(OnClickListener productClickListener) {
        findViewById(R.id.mpProducts).setOnClickListener(productClickListener);
    }


    public void setOnInventoryClickListener(OnClickListener inventoryClickListener) {
        findViewById(R.id.mpInventory).setOnClickListener(inventoryClickListener);
    }

    public void setOnCustomerClickListener(OnClickListener customerClickListener) {
        findViewById(R.id.mpCustomers).setOnClickListener(customerClickListener);
    }

    public void setOnReportClickListener(OnClickListener reportClickListener) {
        findViewById(R.id.mpReports).setOnClickListener(reportClickListener);
    }

    public void setOnOrderClickListener(OnClickListener orderClickListener) {
        mpHorizontalScroller.setOnItemClickListener(orderClickListener);
    }

    public void setOnLeftOrderClickListner(OnClickListener onLeftOrderClickListner) {
        mpHorizontalScroller.setOnLeftArrowClickListner(onLeftOrderClickListner);
    }

    public void setOnRightOrderClickListner(OnClickListener onRightOrderClickListner) {
        mpHorizontalScroller.setOnRightArrowClickListner(onRightOrderClickListner);
    }

    public void setOnSearchClickListener(CallbackSearchFragmentClick onSearchClickListener) {
        this.onSearchClickListener = onSearchClickListener;

    }

    public void setOnSettingsClickListener(OnClickListener settingsClickListener) {
        mpSettings.setOnClickListener(settingsClickListener);
    }

    public void setOnProfileCliclListener(OnClickListener profileCliclListener) {
        findViewById(R.id.mpProfile).setOnClickListener(profileCliclListener);
    }

    public void setInfoClickListener(OnClickListener infoClickListener) {
        findViewById(R.id.mpInfo).setOnClickListener(infoClickListener);
    }

    public void setMode(int mode) {
        this.mode = mode;
        setVisibility();
        invalidate();
    }

    private void setVisibility() {
        switch (mode) {
            case DEFAULT_TYPE: {
                rlBackgroun.setVisibility(GONE);
                mpRightSide.setVisibility(GONE);
                mpMainMenu.setVisibility(GONE);
                mpSettings.setVisibility(GONE);
                llDateIntervalPicker.setVisibility(GONE);
                mpHorizontalScroller.setVisibility(GONE);
                mpSearchView.setVisibility(GONE);
                llEmployer.setVisibility(GONE);
                findViewById(R.id.mpInfo).setVisibility(GONE);
                findViewById(R.id.blackLine).setVisibility(GONE);
                break;
            }
            case DEFAULT_TYPE_TWO_SECTION: {
                rlBackgroun.setVisibility(VISIBLE);
                mpRightSide.setVisibility(VISIBLE);
                mpMainMenu.setVisibility(GONE);
                mpSettings.setVisibility(GONE);
                llDateIntervalPicker.setVisibility(GONE);
                mpHorizontalScroller.setVisibility(GONE);
                mpSearchView.setVisibility(GONE);
                llEmployer.setVisibility(GONE);
                findViewById(R.id.mpInfo).setVisibility(GONE);
                findViewById(R.id.blackLine).setVisibility(GONE);
                break;
            }
            case MAIN_PAGE_TYPE: {
                setFramesVisibility(VISIBLE);
                mpRightSide.setVisibility(VISIBLE);
                mpMainMenu.setVisibility(VISIBLE);
                mpCustomers.setVisibility(VISIBLE);
                mpReports.setVisibility(VISIBLE);
                mpProducts.setVisibility(VISIBLE);
                mpInventory.setVisibility(VISIBLE);
                mpSettings.setVisibility(VISIBLE);
                mpHorizontalScroller.setVisibility(VISIBLE);
                mpSearchView.setVisibility(GONE);
                llDateIntervalPicker.setVisibility(GONE);
                llEmployer.setVisibility(VISIBLE);
                findViewById(R.id.mpInfo).setVisibility(GONE);
                findViewById(R.id.blackLine).setVisibility(GONE);
                break;
            }
            case SEARCH_MODE_TYPE: {
                setFramesVisibility(GONE);
                mpRightSide.setVisibility(VISIBLE);
                mpMainMenu.setVisibility(VISIBLE);
                mpCustomers.setVisibility(INVISIBLE);
                mpReports.setVisibility(INVISIBLE);
                mpProducts.setVisibility(INVISIBLE);
                mpInventory.setVisibility(INVISIBLE);
                mpSettings.setVisibility(GONE);
                mpHorizontalScroller.setVisibility(GONE);
                mpSearchView.setVisibility(GONE);
                llDateIntervalPicker.setVisibility(GONE);
                llEmployer.setVisibility(INVISIBLE);
                findViewById(R.id.mpInfo).setVisibility(GONE);
                findViewById(R.id.blackLine).setVisibility(GONE);
                break;
            }
            case WITH_SEARCH_TYPE: {
                mpRightSide.setVisibility(GONE);
                mpMainMenu.setVisibility(GONE);
                mpSettings.setVisibility(GONE);
                mpHorizontalScroller.setVisibility(GONE);
                llDateIntervalPicker.setVisibility(GONE);
                mpSearchView.setVisibility(VISIBLE);
                llEmployer.setVisibility(GONE);
                findViewById(R.id.mpInfo).setVisibility(GONE);
                findViewById(R.id.blackLine).setVisibility(GONE);
                break;
            }
            case WITH_CALENDAR_TYPE: {
                mpRightSide.setVisibility(GONE);
                mpMainMenu.setVisibility(GONE);
                mpSettings.setVisibility(GONE);
                mpHorizontalScroller.setVisibility(GONE);
                mpSearchView.setVisibility(GONE);
                llDateIntervalPicker.setVisibility(VISIBLE);
                llEmployer.setVisibility(GONE);
                findViewById(R.id.mpInfo).setVisibility(GONE);
                findViewById(R.id.blackLine).setVisibility(GONE);
                break;
            }
            case WITH_SEARCH_CALENDAR_TYPE: {
                mpRightSide.setVisibility(GONE);
                mpMainMenu.setVisibility(GONE);
                mpSettings.setVisibility(GONE);
                mpHorizontalScroller.setVisibility(GONE);
                llDateIntervalPicker.setVisibility(VISIBLE);
                mpSearchView.setVisibility(VISIBLE);
                llEmployer.setVisibility(GONE);
                findViewById(R.id.mpInfo).setVisibility(GONE);
                findViewById(R.id.blackLine).setVisibility(GONE);
                break;
            }

            case BACK_ARROW_MODE: {
                mpLeftSide.setVisibility(GONE);
                rlBackgroun.setVisibility(GONE);
                findViewById(R.id.btnBack).setVisibility(VISIBLE);
                break;
            }

            case BACK_ARROW_WITH_ICON:
                mpLeftSide.setVisibility(GONE);
                rlBackgroun.setVisibility(GONE);
                findViewById(R.id.btnBack).setVisibility(VISIBLE);
                findViewById(R.id.tvMPCenter).setVisibility(VISIBLE);
                break;

        }
    }

    private void setFramesVisibility(int state) {
        findViewById(R.id.frame1).setVisibility(state);
        findViewById(R.id.frame2).setVisibility(state);
        findViewById(R.id.frame3).setVisibility(state);
        findViewById(R.id.frame4).setVisibility(state);
    }

    public void setDatePickerIntervalText(String intervalText) {
        tvPeriod.setText(intervalText);
    }

    public void setDataIntervalPicker(Calendar from, Calendar to, DataIntervalCallbackToToolbar dataIntervalPicker) {
        this.from = from;
        this.to = to;
        setCurrentIntervalToView();
        this.dataIntervalPicker = dataIntervalPicker;
        findViewById(R.id.llDateIntervalPicker).setOnClickListener(view -> {
            dataIntervalPicker.onDataIntervalPickerPressed();
        });
        findViewById(R.id.ivClearInterval).setOnClickListener(view -> {
            dataIntervalPicker.onDataIntervalPickerPressed();
        });

    }

    public void changeInterval(Calendar from, Calendar to) {
        this.from = from;
        this.to = to;
        setCurrentIntervalToView();

    }

    private void setCurrentIntervalToView() {
        if (from == null && to == null) return;
        StringBuilder builder = new StringBuilder();
        builder.append(simpleDateFormat.format(from.getTime()));
        builder.append(" - ");
        builder.append(simpleDateFormat.format(to.getTime()));
        tvPeriod.setText(builder.toString());
    }

    public void setOrderNumber(String orderNumber) {
        mpHorizontalScroller.setOrderNumber(orderNumber);
    }

    public void changeToCloseImgIntervalPick() {
        ((ImageView) findViewById(R.id.ivClearInterval)).setImageResource(R.drawable.interval_close);
        findViewById(R.id.ivClearInterval).setEnabled(true);
        findViewById(R.id.ivClearInterval).setOnClickListener(view -> {
            if (dataIntervalPicker != null)
                dataIntervalPicker.clearInterval();
        });
    }

    public void changeToCalendarImgIntervalPick() {
        ((ImageView) findViewById(R.id.ivClearInterval)).setImageResource(R.drawable.calendar_icon);
        findViewById(R.id.ivClearInterval).setOnClickListener(view -> {
            dataIntervalPicker.onDataIntervalPickerPressed();
        });
    }

    public interface CallbackSearchFragmentClick {
        void onOpen();

        void onClose();
    }


    public interface OnBackArrowClick {
        void onClick();
    }

    public interface DataIntervalCallbackToToolbar {
        void onDataIntervalPickerPressed();

        void clearInterval();
    }
}
