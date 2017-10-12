package com.jim.mpviews;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jim.mpviews.utils.VibrateManager;

import java.util.ArrayList;
import java.util.List;

import static com.jim.mpviews.utils.Constants.BALANCE_MODE;
import static com.jim.mpviews.utils.Constants.PAYMENT_MODE;

/**
 * Created by DEV on 24.08.2017.
 */

public class MpPaymentField extends FrameLayout {

    private LinearLayout mpCurrencies, mpBackGround;
    private TextView mpMainCurr, mpSecondCurr, mpValue, mpType;
    private ImageView mpOtherCurr;
    private int mode = 0;
    private List<String> otherCurrencies;
    private int max_length = 0;
    private int counter = 0;
    private boolean isPressed = false;
    private Context context;

    public MpPaymentField(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MpPaymentField(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MpPaymentField(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MpPaymentField(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.mp_payment_field, this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        mpCurrencies = (LinearLayout) findViewById(R.id.mpCurrencies);
        mpBackGround = (LinearLayout) findViewById(R.id.mpBackGround);
        mpMainCurr = (TextView) findViewById(R.id.mpMainCurrency);
        mpSecondCurr = (TextView) findViewById(R.id.mpSecondaryCurrency);
        mpValue = (TextView) findViewById(R.id.mpPaymentValue);
        mpType = (TextView) findViewById(R.id.mpType);
        mpOtherCurr = (ImageView) findViewById(R.id.mpOtherCurrencies);
        otherCurrencies = new ArrayList<>();
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isPressed) {
                            mpMainCurr.setTextColor(Color.parseColor("#dbdbdb"));
                            mpSecondCurr.setTextColor(getResources().getColor(R.color.colorBlue));
                            isPressed = true;
                        } else {
                            mpMainCurr.setTextColor(getResources().getColor(R.color.colorBlue));
                            mpSecondCurr.setTextColor(Color.parseColor("#dbdbdb"));
                            isPressed = false;
                        }
                        break;
                }
                return false;
            }
        });
    }

    public void setMainCurrency(String mainCurrency) {
        mpMainCurr.setText(mainCurrency);
        mpMainCurr.setTextColor(getResources().getColor(R.color.colorBlue));
    }

    public void setSecondaryCurrency(String secondaryCurrency) {
        mpSecondCurr.setText(secondaryCurrency);
        mpSecondCurr.setTextColor(Color.parseColor("#dbdbdb"));
    }


    public void setOtherCurrencies(List<String> otherCurrencies) {
        this.otherCurrencies = otherCurrencies;
        setSecondaryCurrency(otherCurrencies.get(0));
        if (otherCurrencies.size() > 1) {
            mpOtherCurr.setVisibility(VISIBLE);
        } else mpOtherCurr.setVisibility(GONE);
        max_length = otherCurrencies.size();
    }

    public void setOnCurrencyChangeListener(OnClickListener onCurrencyChangeListener) {
        setOnClickListener(onCurrencyChangeListener);
    }

    public void setOtherCurrenciesClickListener() {
        mpOtherCurr.setOnClickListener(view -> {
            if (max_length != 0) {
                if (counter < max_length - 1) {
                    counter++;
                } else {
                    counter = 0;
                }
            } else {
                counter++;
            }
            mpSecondCurr.setText(otherCurrencies.get(counter));
            VibrateManager.startVibrate(context, 50);
        });
    }

    public void setMode(int mode) {
        this.mode = mode;
        switch (mode) {
            case PAYMENT_MODE:
                mpValue.setTextColor(getResources().getColor(R.color.colorGreen));
                mpType.setTextColor(getResources().getColor(R.color.colorGreen));
                mpType.setText(getResources().getString(R.string.payment));
                break;
            case BALANCE_MODE:
                mpValue.setTextColor(getResources().getColor(R.color.colorDarkRed));
                mpType.setTextColor(getResources().getColor(R.color.colorDarkRed));
                mpType.setText(getResources().getString(R.string.balance_due));
                break;
        }
    }

    public void setValue(String paymentValue) {
        mpValue.setText(paymentValue);
    }
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof MpButton.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        this.isPressed = savedState.boolValue;
        this.mode = savedState.intValue;

    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState savedState = new SavedState(superState);
        savedState.boolValue = this.isPressed;
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
//    Example:
//    mpPaymentField = (MpPaymentField) view.findViewById(R.id.change);
//    List<String> cStrings = new ArrayList<>();
//        cStrings.add("uzs");
//        cStrings.add("eur");
//        cStrings.add("rub");
//        mpPaymentField.setMainCurrency("usd");
//        mpPaymentField.setOtherCurrencies(cStrings);
//        mpPaymentField.setMode(PAYMENT_MODE);
//        mpPaymentField.setValue("25 000");
//        mpPaymentField.setOtherCurrenciesClickListener();
}
