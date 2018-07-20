package com.jim.multipos.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.jim.multipos.config.common.BaseAppModule;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

/**
 * Created by Sirojiddin on 14.03.2018.
 */

public class NumberTextWatcher implements TextWatcher {

    private DecimalFormat df;
    private DecimalFormat dfnd;
    private int trailingZeroCount;
    private boolean hasFractionalPart;

    private EditText editText;

    public NumberTextWatcher(EditText et) {
        df = BaseAppModule.getFormatter();
        df.setDecimalSeparatorAlwaysShown(true);


        DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(' ');
        dfnd = new DecimalFormat();
        dfnd.setDecimalFormatSymbols(symbols);
        dfnd.setGroupingSize(3);
        this.editText = et;
        hasFractionalPart = false;
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        int index = charSequence.toString().indexOf(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()));
        trailingZeroCount = 0;
        if (index > -1) {
            for (index++; index < charSequence.length(); index++) {
                if (charSequence.charAt(index) == '0' && trailingZeroCount < 2)
                    trailingZeroCount++;
                else {
                    trailingZeroCount = 0;
                }
            }

            hasFractionalPart = true;
        } else {
            hasFractionalPart = false;
        }
//        hasFractionalPart = charSequence.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()));
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
            StringBuilder trailingZeros = new StringBuilder();
            while (trailingZeroCount-- > 0)
                trailingZeros.append('0');
            editText.setText(df.format(n) + trailingZeros.toString());
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
