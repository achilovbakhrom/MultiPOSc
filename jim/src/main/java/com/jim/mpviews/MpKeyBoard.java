package com.jim.mpviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
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
    private TextView mpLang;
    private LinearLayout mpCaseRU, mpSpaceBar, mpCase;
    private ImageView mpShift, mpShiftRU;
    OnTextTyped onTextTyped;

    int ids[] = {R.id.mpOne, R.id.mpTwo, R.id.mpThree, R.id.mpFour, R.id.mpFive, R.id.mpSix, R.id.mpSeven, R.id.mpEight, R.id.mpNine, R.id.mpZero,
            R.id.mpQ, R.id.mpW, R.id.mpE, R.id.mpR, R.id.mpT, R.id.mpY, R.id.mpU, R.id.mpI, R.id.mpO, R.id.mpP, R.id.mpA, R.id.mpS, R.id.mpD, R.id.mpF, R.id.mpG, R.id.mpH, R.id.mpJ, R.id.mpK, R.id.mpL, R.id.mpZ, R.id.mpX, R.id.mpC, R.id.mpV, R.id.mpB, R.id.mpN, R.id.mpM};
    char keyValues[] = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M'};


    int idsRU[] = {R.id.mpOne, R.id.mpTwo, R.id.mpThree, R.id.mpFour, R.id.mpFive, R.id.mpSix, R.id.mpSeven, R.id.mpEight, R.id.mpNine, R.id.mpZero,
            R.id.mpY_RU, R.id.mpTs_RU, R.id.mpU_RU, R.id.mpK_RU, R.id.mpE_RU, R.id.mpN_RU, R.id.mpG_RU, R.id.mpSh_RU, R.id.mpSh2_RU, R.id.mpZ_Ru, R.id.mpX_RU, R.id.mpF_RU, R.id.mpHard_RU, R.id.mpV_RU, R.id.mpA_RU, R.id.mpP_RU, R.id.mpR_RU, R.id.mpO_RU, R.id.mpL_RU, R.id.mpD_RU, R.id.mpJ_RU, R.id.mpE2_RU, R.id.mpYa_RU, R.id.mpCh_RU, R.id.mpC_RU, R.id.mpM_RU, R.id.mpI_Ru, R.id.mpT_RU, R.id.mpSoft_RU, R.id.mpB_RU, R.id.mpYu_RU};
    char keyValuesRU[] = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            'Й', 'Ц', 'У', 'К', 'Е', 'Н', 'Г', 'Ш', 'Щ', 'З', 'Х', 'Ф', 'Ы', 'В', 'А', 'П', 'Р', 'О', 'Л', 'Д', 'Ж', 'Э', 'Я', 'Ч', 'С', 'М', 'И', 'Т', 'Ь', 'Б', 'Ю'};

    char symbols[]  = {'!','@','#','$','%','^','&','*','(',')'};
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
        mpSpaceBar = (LinearLayout) findViewById(R.id.mpSpaceBar);
        mpLang = (TextView) findViewById(R.id.mpLang);
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
        findViewById(R.id.mpBackspace).setOnClickListener(this);
        findViewById(R.id.mpBackspace).setOnLongClickListener(this);
        findViewById(R.id.mpBackspaceRU).setOnClickListener(this);
        findViewById(R.id.mpBackspaceRU).setOnLongClickListener(this);
        findViewById(R.id.mpDot).setOnClickListener(this);
        findViewById(R.id.mpSpaceBar).setOnClickListener(this);
        findViewById(R.id.mpOkButton).setOnClickListener(this);
        findViewById(R.id.mpSwitcherEN).setOnClickListener(this);

        mpCase.setBackgroundResource(R.drawable.key_pad_blue);
        isPressed = true;
        mpShift.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));


        mpCase.setOnClickListener(view -> {
            if (!isPressed) {
                mpCase.setBackgroundResource(R.drawable.key_pad_blue);
                isPressed = true;
                mpShift.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                for (int i = 0; i < ids.length; i++) {
                    MpKeyPad mpKeyPad = (MpKeyPad) findViewById(ids[i]);
                    mpKeyPad.toUpperCase();
                }
            } else {
                mpCase.setBackgroundResource(R.drawable.key_pad_big_btn);
                isPressed = false;
                mpShift.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlue)));
                for (int i = 0; i < ids.length; i++) {
                    MpKeyPad mpKeyPad = (MpKeyPad) findViewById(ids[i]);
                    mpKeyPad.toLowerCase();
                }
            }
        });
        mpCaseRU.setOnClickListener(view -> {
            if (!isPressed) {
                mpCaseRU.setBackgroundResource(R.drawable.key_pad_blue);
                isPressed = true;
                mpShiftRU.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                for (int i = 0; i < idsRU.length; i++) {
                    MpKeyPad mpKeyPad = (MpKeyPad) findViewById(idsRU[i]);
                    mpKeyPad.toUpperCase();
                }
            } else {
                mpCaseRU.setBackgroundResource(R.drawable.key_pad_big_btn);
                isPressed = false;
                mpShiftRU.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlue)));
                for (int i = 0; i < idsRU.length; i++) {
                    MpKeyPad mpKeyPad = (MpKeyPad) findViewById(idsRU[i]);
                    mpKeyPad.toLowerCase();
                }
            }

        });



    }

    public void setOnTextTyped(OnTextTyped onTextTyped){
        this.onTextTyped = onTextTyped;
    }

    @Override
    public void onClick(View view) {

        if (mode == ENGLISH) {
            for (int i = 0; i < ids.length; i++) {
                if (view.getId() == ids[i]) {
                    String values =  "" + keyValues[i];
                    if (isPressed) values = "" + values.toUpperCase();
                    else values = "" + values.toLowerCase();
                    onTextTyped.onSymbolPressed(values);
                }
            }


        } else {
            for (int i = 0; i < idsRU.length; i++) {
                if (view.getId() == idsRU[i]) {
                    String values =  "" + keyValuesRU[i];
                    if (isPressed) values = "" + values.toUpperCase();
                    else values = "" + values.toLowerCase();
                    onTextTyped.onSymbolPressed(values);
                }
            }


        }
        if(view.getId() == R.id.mpSpaceBar){
            onTextTyped.onSymbolPressed(" ");
        }
        if (view.getId() == R.id.mpBackspaceRU || view.getId() == R.id.mpBackspace) {
            onTextTyped.onBackClear();
        }

        if (view.getId() == R.id.mpDot) {
            onTextTyped.onSymbolPressed(".");
        }

        if (view.getId() == R.id.mpSwitcherEN) {
            if (mode == ENGLISH) {
                findViewById(R.id.mpEnglishLetters).setVisibility(GONE);
                findViewById(R.id.mpRussianLetters).setVisibility(VISIBLE);
                mode = RUSSIAN;
                mpLang.setText("Ru");

                if (isPressed) {
                    mpCaseRU.setBackgroundResource(R.drawable.key_pad_blue);
                    mpShiftRU.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    for (int i = 0; i < idsRU.length; i++) {
                        MpKeyPad mpKeyPad = (MpKeyPad) findViewById(idsRU[i]);
                        mpKeyPad.toUpperCase();
                    }
                } else {
                    mpCaseRU.setBackgroundResource(R.drawable.key_pad_big_btn);
                    mpShiftRU.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlue)));
                    for (int i = 0; i < idsRU.length; i++) {
                        MpKeyPad mpKeyPad = (MpKeyPad) findViewById(idsRU[i]);
                        mpKeyPad.toLowerCase();
                    }
                }


            } else {
                findViewById(R.id.mpEnglishLetters).setVisibility(VISIBLE);
                findViewById(R.id.mpRussianLetters).setVisibility(GONE);
                mode = ENGLISH;
                mpLang.setText("En");

                if (isPressed) {
                    mpCase.setBackgroundResource(R.drawable.key_pad_blue);
                    mpShift.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    for (int i = 0; i < ids.length; i++) {
                        MpKeyPad mpKeyPad = (MpKeyPad) findViewById(ids[i]);
                        mpKeyPad.toUpperCase();
                    }
                } else {
                    mpCase.setBackgroundResource(R.drawable.key_pad_big_btn);
                    mpShift.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlue)));
                    for (int i = 0; i < ids.length; i++) {
                        MpKeyPad mpKeyPad = (MpKeyPad) findViewById(ids[i]);
                        mpKeyPad.toLowerCase();
                    }
                }

            }
        }
        if(view.getId() == R.id.mpOkButton){
            onTextTyped.onOkPressed();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public boolean onLongClick(View view) {
        if(view.getId() == R.id.mpBackspace || view.getId() == R.id.mpBackspaceRU )
            onTextTyped.onClear();
        if (mode == ENGLISH) {
            for (int i = 0; i < 10; i++) {
                if (view.getId() == ids[i]) {
                    String keyValue = "" + symbols[i];
                    if(isPressed) keyValue= keyValue.toUpperCase();
                    else keyValue=  keyValue.toLowerCase();
                    onTextTyped.onSymbolPressed(keyValue);
                }
            }
        } else {
            for (int i = 0; i < 10; i++) {
                if (view.getId() == idsRU[i]) {
                    String keyValue = "" + symbols[i];
                    if(isPressed) keyValue= keyValue.toUpperCase();
                    else keyValue=keyValue.toLowerCase();
                    onTextTyped.onSymbolPressed(keyValue);
                }
            }
            if(view.getId() == idsRU[14]){
                String keyValue = "Ё";
                if(isPressed) keyValue= keyValue.toUpperCase();
                else keyValue=keyValue.toLowerCase();
                onTextTyped.onSymbolPressed(keyValue);

            }
            if(view.getId() == idsRU[38]){
                String keyValue = "Ъ";
                if(isPressed) keyValue= keyValue.toUpperCase();
                else keyValue=keyValue.toLowerCase();
                onTextTyped.onSymbolPressed(keyValue);
            }
        }

        return true;
    }
    public interface OnTextTyped{
        void onSymbolPressed(String s);
        void onOkPressed();
        void onBackClear();
        void onClear();
    }


}
