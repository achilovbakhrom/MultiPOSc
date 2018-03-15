package com.jim.multipos.utils;

import android.text.Editable;
import android.text.TextWatcher;

import com.jim.mpviews.MpEditText;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Created by Sirojiddin on 14.03.2018.
 */

public class NumberTextWatcher implements TextWatcher {

    private DecimalFormat df;
    private DecimalFormat dfnd;
    private boolean hasFractionalPart;

    private MpEditText editText;

    public NumberTextWatcher(MpEditText et) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        df = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(' ');
        df.setDecimalFormatSymbols(symbols);
        df.setDecimalSeparatorAlwaysShown(true);

        dfnd = new DecimalFormat("#,###");
        this.editText = et;
        hasFractionalPart = false;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        hasFractionalPart = charSequence.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()));
    }

    @Override
    public void afterTextChanged(Editable editable) {
        editText.removeTextChangedListener(this);
        int inilen, endlen;
        inilen = editText.getText().length();

        String v = editable.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
        Number n = 0;
        try {
            n = df.parse(v);
        } catch (ParseException e) {
            //
        }
        n = roundTwoDecimals(n.doubleValue());
        int cp = editText.getSelectionStart();
        if (n.doubleValue() == 0 && v.isEmpty()) {
            editText.setText("");
        } else if (hasFractionalPart) {
            editText.setText(df.format(n));
        } else {
            editText.setText(dfnd.format(n));
        }
        endlen = editText.getText().length();
        int sel = (cp + (endlen - inilen));
        if (sel > 0 && sel <= editText.getText().length()) {
            editText.setSelection(sel);
        } else {
            int index = editText.getText().length();
            if (index < 0) index = 0;
            editText.setSelection(index);
        }
        editText.addTextChangedListener(this);
    }

    private double roundTwoDecimals(double d) {
        return Math.floor(d * 100) / 100;
    }
}
