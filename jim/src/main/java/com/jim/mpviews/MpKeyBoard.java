package com.jim.mpviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
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

import static com.jim.mpviews.utils.Utils.convertDpToPixel;


/**
 * Created by DEV on 17.07.2017.
 */

public class MpKeyBoard extends FrameLayout implements View.OnClickListener, View.OnLongClickListener {

    public static final int ENGLISH = 0;
    public static final int RUSSIAN = 1;
    int mode = 0;
    private TextView mpText, mpName, mpSKU, mpBarcode, mpLang;
    private LinearLayout mpCaseRU, mpSpaceBar, mpCase, mpSearch;
    private ImageView mpShift, mpShiftRU;
    int ids[] = {R.id.mpOne, R.id.mpTwo, R.id.mpThree, R.id.mpFour, R.id.mpFive, R.id.mpSix, R.id.mpSeven, R.id.mpEight, R.id.mpNine, R.id.mpZero,
            R.id.mpQ, R.id.mpW, R.id.mpE, R.id.mpR, R.id.mpT, R.id.mpY, R.id.mpU, R.id.mpI, R.id.mpO, R.id.mpP, R.id.mpA, R.id.mpS, R.id.mpD, R.id.mpF, R.id.mpG, R.id.mpH, R.id.mpJ, R.id.mpK, R.id.mpL, R.id.mpZ, R.id.mpX, R.id.mpC, R.id.mpV, R.id.mpB, R.id.mpN, R.id.mpM};
    char keyValues[] = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M'};

    int idsRU[] = {R.id.mpOne, R.id.mpTwo, R.id.mpThree, R.id.mpFour, R.id.mpFive, R.id.mpSix, R.id.mpSeven, R.id.mpEight, R.id.mpNine, R.id.mpZero,
            R.id.mpY_RU, R.id.mpTs_RU, R.id.mpU_RU, R.id.mpK_RU, R.id.mpE_RU, R.id.mpN_RU, R.id.mpG_RU, R.id.mpSh_RU, R.id.mpSh2_RU, R.id.mpZ_Ru, R.id.mpX_RU, R.id.mpF_RU, R.id.mpHard_RU, R.id.mpV_RU, R.id.mpA_RU, R.id.mpP_RU, R.id.mpR_RU, R.id.mpO_RU, R.id.mpL_RU, R.id.mpD_RU, R.id.mpJ_RU, R.id.mpE2_RU, R.id.mpYa_RU, R.id.mpCh_RU, R.id.mpC_RU, R.id.mpM_RU, R.id.mpI_Ru, R.id.mpT_RU, R.id.mpSoft_RU, R.id.mpB_RU, R.id.mpYu_RU};
    char keyValuesRU[] = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            'Й', 'Ц', 'У', 'К', 'Е', 'Н', 'Г', 'Ш', 'Щ', 'З', 'Х', 'Ф', 'Ы', 'В', 'А', 'П', 'Р', 'О', 'Л', 'Д', 'Ж', 'Э', 'Я', 'Ч', 'С', 'М', 'И', 'Т', 'Ь', 'Б', 'Ю'};

    private boolean isPressed = false;
    private RelativeLayout mpDot;
    private Context context;

    public MpKeyBoard(Context context) {
        super(context);
        init(context);
    }

    public MpKeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public MpKeyBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    public MpKeyBoard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.mp_keyboard, this);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        mpText = (TextView) findViewById(R.id.mpText);
        mpName = (TextView) findViewById(R.id.mpName);
        mpSKU = (TextView) findViewById(R.id.mpSKU);
        mpBarcode = (TextView) findViewById(R.id.mpBarcode);
        mpSpaceBar = (LinearLayout) findViewById(R.id.mpSpaceBar);
        mpLang = (TextView) findViewById(R.id.mpLang);
        mpSearch = (LinearLayout) findViewById(R.id.mpSearchBtn);
        mpCase = (LinearLayout) findViewById(R.id.mpCase);
        mpCaseRU = (LinearLayout) findViewById(R.id.mpCaseRU);
        mpDot = (RelativeLayout) findViewById(R.id.mpDot);
        mpShift = (ImageView) findViewById(R.id.mpShift);
        mpShiftRU = (ImageView) findViewById(R.id.mpShiftRU);
        for (int i = 0; i < ids.length; i++) {
            findViewById(ids[i]).setOnClickListener(this);
            findViewById(ids[i]).setOnLongClickListener(this);
        }
        for (int i = 0; i < idsRU.length; i++) {
            findViewById(idsRU[i]).setOnClickListener(this);
            findViewById(idsRU[i]).setOnLongClickListener(this);
        }
        findViewById(R.id.mpClear).setOnClickListener(this);
        findViewById(R.id.mpBackspace).setOnClickListener(this);
        findViewById(R.id.mpBackspaceRU).setOnClickListener(this);
        findViewById(R.id.mpDot).setOnClickListener(this);
        findViewById(R.id.mpSwitcherEN).setOnClickListener(this);
        mpCase.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        VibrateManager.startVibrate(context, 10);
                        if (!isPressed) {
                            mpCase.setBackgroundResource(R.drawable.key_pad_blue);
                            isPressed = true;
                            mpShift.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                            for (int i = 0; i < ids.length; i++) {
                                MpKeyPad mpKeyPad = (MpKeyPad) findViewById(ids[i]);
                                mpKeyPad.toLowerCase();
                            }
                        } else {
                            mpCase.setBackgroundResource(R.drawable.key_pad_big_btn);
                            isPressed = false;
                            mpShift.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlue)));
                            for (int i = 0; i < ids.length; i++) {
                                MpKeyPad mpKeyPad = (MpKeyPad) findViewById(ids[i]);
                                mpKeyPad.toUpperCase();
                            }
                        }
                        return true;
                }
                return false;
            }
        });

        mpCaseRU.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        VibrateManager.startVibrate(context, 10);
                        if (!isPressed) {
                            mpCaseRU.setBackgroundResource(R.drawable.key_pad_blue);
                            isPressed = true;
                            mpShiftRU.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                            for (int i = 0; i < idsRU.length; i++) {
                                MpKeyPad mpKeyPad = (MpKeyPad) findViewById(idsRU[i]);
                                mpKeyPad.toLowerCase();
                            }
                        } else {
                            mpCaseRU.setBackgroundResource(R.drawable.key_pad_big_btn);
                            isPressed = false;
                            mpShiftRU.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlue)));
                            for (int i = 0; i < idsRU.length; i++) {
                                MpKeyPad mpKeyPad = (MpKeyPad) findViewById(idsRU[i]);
                                mpKeyPad.toUpperCase();
                            }
                        }
                        return true;
                }
                return false;
            }
        });

        mpSpaceBar.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        VibrateManager.startVibrate(context, 10);
                        mpSpaceBar.setBackgroundResource(R.drawable.space_bar_pressed);
                        String text = mpText.getText().toString();
                        text = text + " ";
                        mpText.setText(text);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mpSpaceBar.setBackgroundResource(R.drawable.space_bar);
                        return true;
                }
                return false;
            }
        });

    }

    public String getText() {
        return mpText.getText().toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onClick(View view) {

        if (mode == ENGLISH) {
            for (int i = 0; i < ids.length; i++) {
                if (view.getId() == ids[i]) {
                    String text = mpText.getText().toString();
                    String values = "";
                    values = values + keyValues[i];
                    if (!isPressed) text = text + values.toUpperCase();
                    else text = text + values.toLowerCase();

                    mpText.setText(text);
                }
            }

            if (view.getId() == R.id.mpBackspace) {
                VibrateManager.startVibrate(context, 10);
                String text = mpText.getText().toString();
                if (!text.isEmpty())
                    mpText.setText(text.substring(0, text.length() - 1));
            }


        } else {
            for (int i = 0; i < idsRU.length; i++) {
                if (view.getId() == idsRU[i]) {
                    String text = mpText.getText().toString();
                    StringBuilder values = new StringBuilder();
                    values.append(keyValuesRU[i]);
                    if (!isPressed) text = text + values.toString().toUpperCase();
                    else text = text + values.toString().toLowerCase();

                    mpText.setText(text);
                }
            }

            if (view.getId() == R.id.mpBackspaceRU) {
                VibrateManager.startVibrate(context, 10);
                String text = mpText.getText().toString();
                if (!text.isEmpty())
                    mpText.setText(text.substring(0, text.length() - 1));
            }
        }

        if (view.getId() == R.id.mpClear)
            mpText.setText("");

        if (view.getId() == R.id.mpDot) {
            VibrateManager.startVibrate(context, 10);
            String text = mpText.getText().toString();
            mpText.setText(text + ".");
        }

        if (view.getId() == R.id.mpSwitcherEN) {
            if (mode == ENGLISH) {
                findViewById(R.id.mpEnglishLetters).setVisibility(GONE);
                findViewById(R.id.mpRussianLetters).setVisibility(VISIBLE);
                mode = RUSSIAN;
                mpLang.setText("EN");
                setWidth(mpSpaceBar, 380, 3);
                setWidth(mpSearch, 120, 0);
                setWidth(mpDot, 60, 3);
                setWidth(findViewById(R.id.mpSwitcherEN), 120, 3);
            } else {
                findViewById(R.id.mpEnglishLetters).setVisibility(VISIBLE);
                findViewById(R.id.mpRussianLetters).setVisibility(GONE);
                mode = ENGLISH;
                mpLang.setText("RU");
                setWidth(mpSpaceBar, 410, 10);
                setWidth(mpSearch, 95, 0);
                setWidth(mpDot, 60, 10);
                setWidth(findViewById(R.id.mpSwitcherEN), 95, 10);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public boolean onLongClick(View view) {
        if (mode == ENGLISH) {
            for (int i = 0; i < ids.length; i++) {
                if (view.getId() == ids[i]) {
                    MpKeyPad mpKeyPad = (MpKeyPad) findViewById(ids[i]);
                    String text = mpText.getText().toString();
                    if (!mpKeyPad.getSymbol().isEmpty()) {
                        String values = "";
                        values = values + mpKeyPad.getSymbol();
                        if (!isPressed) text = text + values.toUpperCase();
                        else text = text + values.toLowerCase();
                        mpText.setText(text);
                    }
                }
            }
        } else {
            for (int i = 0; i < idsRU.length; i++) {
                if (view.getId() == idsRU[i]) {
                    MpKeyPad mpKeyPad = (MpKeyPad) findViewById(idsRU[i]);
                    String text = mpText.getText().toString();
                    if (!mpKeyPad.getSymbol().isEmpty()) {
                        String values = "";
                        values = values + mpKeyPad.getSymbol();
                        if (!isPressed) text = text + values.toUpperCase();
                        else text = text + values.toLowerCase();
                        mpText.setText(text);
                    }
                }
            }
        }

        return true;
    }

    private void setWidth(View view, int width, int rightMargin) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = (int) convertDpToPixel(width);
        params.setMargins(0, 0, (int) convertDpToPixel(rightMargin), 0);
        view.setLayoutParams(params);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof MpButton.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        MpItem.SavedState savedState = (MpItem.SavedState)state;
        super.onRestoreInstanceState(savedState.getSuperState());

        this.isPressed = savedState.boolValue;

    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        MpItem.SavedState savedState = new MpItem.SavedState(superState);
        savedState.boolValue = this.isPressed;

        return savedState;
    }

    static class SavedState extends BaseSavedState{
        boolean boolValue;

        public SavedState(Parcelable source) {
            super(source);
        }

        private SavedState(Parcel in) {
            super(in);
            this.boolValue = in.readInt() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(boolValue ? 1 : 0);
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
