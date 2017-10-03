package com.jim.multipos.ui.first_configure.fragments;

import java.util.HashMap;

/**
 * Created by user on 01.08.17.
 */

public interface  PosDetailsFragmentView {
    void showPosIdError(String error);
    void showAliasError(String error);
    void showAddressError(String error);
    void showPasswordError(String error);
    void showRepeatPasswordError(String error);
    void openNextFragment();
    void clearViews();
}
