package com.jim.mpviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jim.mpviews.utils.VibrateManager;

/**
 * Created by DEV on 19.07.2017.
 */

public class MpKeyPad extends FrameLayout {

    private TextView mpLetter, mpSymbol;
    private RelativeLayout mpKeyPadBg;

    public MpKeyPad(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public MpKeyPad(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public MpKeyPad(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public MpKeyPad(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    public void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.mp_key_pad, this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);

        mpSymbol = (TextView) findViewById(R.id.mpSecondarySymbol);
        mpLetter = (TextView) findViewById(R.id.mpMainSymbol);
        mpKeyPadBg = (RelativeLayout) findViewById(R.id.mpKeyPadBg);



        TypedArray attribute = context.obtainStyledAttributes(attrs, R.styleable.MpKeyPad);

        mpLetter.setText(attribute.getText(R.styleable.MpKeyPad_letter));
        mpSymbol.setText(attribute.getText(R.styleable.MpKeyPad_symbol));
         mpLetter.setTextColor(attribute.getColor(R.styleable.MpKeyPad_letter_color, getResources().getColor(R.color.colorBlue)));

        if (attribute.getBoolean(R.styleable.MpKeyPad_has_extra, false)) {
            mpSymbol.setVisibility(VISIBLE);
        } else mpSymbol.setVisibility(GONE);

        Drawable buttonDrawable = context.getResources().getDrawable(R.drawable.key_pad_mini);
        buttonDrawable.mutate();
        mpKeyPadBg.setBackgroundDrawable(buttonDrawable);
        setClickable(true);

        attribute.recycle();
    }

    public String getSymbol() {
        return mpSymbol.getText().toString();
    }

    public String getLetter() {
        return mpLetter.getText().toString();
    }

    public void setSymbolVisibility(int visibility) {
        mpSymbol.setVisibility(visibility);
    }

    public void setSymbol(String symbol) {
        mpSymbol.setText(symbol);
    }

    public void setLetter(String letter) {
        mpLetter.setText(letter);
    }

    public void toLowerCase() {
        String letter = mpLetter.getText().toString();
        String symbol = mpSymbol.getText().toString();
        mpLetter.setText(letter.toLowerCase());
        if (!mpSymbol.getText().toString().isEmpty())
            mpSymbol.setText(symbol.toLowerCase());
    }

    public void toUpperCase() {
        String letter = mpLetter.getText().toString();
        String symbol = mpSymbol.getText().toString();
        mpLetter.setText(letter.toUpperCase());
        if (!mpSymbol.getText().toString().isEmpty())
            mpSymbol.setText(symbol.toUpperCase());
    }
}
