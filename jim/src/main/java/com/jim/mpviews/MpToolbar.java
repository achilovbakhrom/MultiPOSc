package com.jim.mpviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jim.mpviews.utils.VibrateManager;

import java.util.ArrayList;

import static com.jim.mpviews.utils.Constants.ADMIN_MODE;
import static com.jim.mpviews.utils.Constants.DEFAULT_MODE;
import static com.jim.mpviews.utils.Constants.MAIN_MODE;
import static com.jim.mpviews.utils.Constants.SEARCH_MODE;

/**
 * Created by DEV on 30.06.2017.
 */

public class MpToolbar extends RelativeLayout {

    private int mode;
    boolean pressed = false;
    private LinearLayout mpMainMenu, llEmployer;
    private RelativeLayout mpSearch;
    private RelativeLayout mpLeftSide, mpRightSide;
    private ImageView mpSettings;
    private TextView mpEmpName, mpEmpRole;
    private MpHorizontalScroller mpHorizontalScroller;
    private MpSearchView mpSearchView;
    private VibrateManager VibrateManager;

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

    public void init(Context context, AttributeSet attributeSet) {
        VibrateManager = new VibrateManager(context, 50);
        LayoutInflater.from(context).inflate(R.layout.mp_toolbar, this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.MpToolbar);
        mpMainMenu = (LinearLayout) findViewById(R.id.mpMainMenu);
        llEmployer = (LinearLayout) findViewById(R.id.llEmployer);
        mpSearch = (RelativeLayout) findViewById(R.id.mpSearch);
        mpSettings = (ImageView) findViewById(R.id.mpSettings);
        mpEmpName = (TextView) findViewById(R.id.mpEmpName);
        mpEmpRole = (TextView) findViewById(R.id.mpEmpRole);
        mpHorizontalScroller = (MpHorizontalScroller) findViewById(R.id.mpHorRoller);
        mpSearchView = (MpSearchView) findViewById(R.id.mpSearchView);
        mpLeftSide = (RelativeLayout) findViewById(R.id.mpLeftSide);
        mpRightSide = (RelativeLayout) findViewById(R.id.mpRightSide);
        mode = array.getInt(R.styleable.MpToolbar_view_mode, DEFAULT_MODE);
        setMode(mode);
        mpSettings.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!pressed) {
                            VibrateManager.startVibrate();
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
            }
        });
        findViewById(R.id.mpProducts).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!pressed) {
                            VibrateManager.startVibrate();
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
            }
        });


        findViewById(R.id.mpInventory).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!pressed) {
                            VibrateManager.startVibrate();
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
                            VibrateManager.startVibrate();
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
                            VibrateManager.startVibrate();
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
                            VibrateManager.startVibrate();
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
                        if (!pressed) {
                            VibrateManager.startVibrate();
                            pressed = true;
                        }
                        findViewById(R.id.searchLine).setVisibility(GONE);
                        findViewById(R.id.searchPressed).setVisibility(VISIBLE);
                        return false;
                    case MotionEvent.ACTION_UP:
                        pressed = false;
                        findViewById(R.id.searchLine).setVisibility(VISIBLE);
                        findViewById(R.id.searchPressed).setVisibility(GONE);
                        return false;
                }
                return false;
            }
        });
        array.recycle();
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

    public void setOnSearchClickListener(OnClickListener searchClickListener) {
        mpSearch.setOnClickListener(searchClickListener);
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
            case DEFAULT_MODE: {
                mpRightSide.setVisibility(GONE);
                mpMainMenu.setVisibility(GONE);
                mpSettings.setVisibility(GONE);
                mpHorizontalScroller.setVisibility(GONE);
                mpSearchView.setVisibility(GONE);
                llEmployer.setVisibility(GONE);
                findViewById(R.id.mpInfo).setVisibility(GONE);
                findViewById(R.id.blackLine).setVisibility(GONE);
                break;
            }
            case MAIN_MODE: {
                mpRightSide.setVisibility(VISIBLE);
                mpMainMenu.setVisibility(VISIBLE);
                mpSettings.setVisibility(VISIBLE);
                mpHorizontalScroller.setVisibility(VISIBLE);
                mpSearchView.setVisibility(GONE);
                llEmployer.setVisibility(VISIBLE);
                findViewById(R.id.mpInfo).setVisibility(GONE);
                findViewById(R.id.blackLine).setVisibility(GONE);
                break;
            }
            case SEARCH_MODE: {
                mpRightSide.setVisibility(VISIBLE);
                mpMainMenu.setVisibility(GONE);
                mpSettings.setVisibility(GONE);
                mpHorizontalScroller.setVisibility(GONE);
                mpSearchView.setVisibility(VISIBLE);
                llEmployer.setVisibility(GONE);
                findViewById(R.id.mpInfo).setVisibility(GONE);
                findViewById(R.id.blackLine).setVisibility(GONE);
                break;
            }
            case ADMIN_MODE: {
                mpRightSide.setVisibility(VISIBLE);
                mpMainMenu.setVisibility(VISIBLE);
                mpSettings.setVisibility(VISIBLE);
                mpHorizontalScroller.setVisibility(VISIBLE);
                mpSearchView.setVisibility(GONE);
                llEmployer.setVisibility(GONE);
                findViewById(R.id.mpInfo).setVisibility(VISIBLE);
                findViewById(R.id.blackLine).setVisibility(VISIBLE);
                break;
            }
        }
    }

    public void setName(String name) {
        mpEmpName.setText(name);
        invalidate();
    }

    public void setRole(String role) {
        mpEmpRole.setText(role);
        invalidate();
    }

    public void setOrders(ArrayList<String> orders) {
        mpHorizontalScroller.setItems(orders);
    }

    public void setOrders(String[] orders) {
        mpHorizontalScroller.setItems(orders);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof MpButton.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        this.pressed = savedState.boolValue;
        this.mode = savedState.intValue;

    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState savedState = new SavedState(superState);
        savedState.boolValue = this.pressed;
        savedState.intValue = this.mode;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        boolean boolValue;
        int intValue;

        public SavedState(Parcelable source) {
            super(source);
        }

        private SavedState(Parcel in) {
            super(in);
            this.boolValue = in.readInt() != 0;
            this.intValue = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(boolValue ? 1 : 0);
            out.writeInt(this.intValue);
            super.writeToParcel(out, flags);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
