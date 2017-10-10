package com.jim.multipos.ui.first_configure;

import com.jim.multipos.core.BaseView;

import java.util.Map;

/**
 * Created by user on 07.10.17.
 */

public interface FirstConfigureView extends BaseView {
    void showPosDetailsErrors(Map<String, String> errors);
    void replaceFragment(int position, boolean isNextButton);
    void replaceFragment(int position);
    void showLeftSideFragment(boolean[] isCompletedFragments);
    void closeActivity();
}
