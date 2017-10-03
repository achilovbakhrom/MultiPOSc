package com.jim.multipos.ui.first_configure.common;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.jim.mpviews.MpButton;

import java.util.HashMap;

/**
 * Created by user on 04.08.17.
 */

public abstract class BaseFragmentFirstConfig extends com.jim.multipos.core.BaseFragment {
    public abstract boolean checkData();
    public abstract void saveData();

    public abstract HashMap<String, String> getData();

    public void hideKeyboard() {
        View view = getActivity().getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
