package com.jim.multipos.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.terrakok.phonematter.PhoneFormat;

public class PhoneNumberFormatTextWatcher implements TextWatcher {

    private PhoneFormat phoneFormat;
    private EditText editText;

    public PhoneNumberFormatTextWatcher(Context context, EditText editText) {
        phoneFormat = new PhoneFormat(context);
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        editText.removeTextChangedListener(this);
        int inilen, endlen;
        inilen = editText.getText().length();
        int cp = editText.getSelectionStart();
        editText.setText(phoneFormat.format(s.toString()));
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
}
