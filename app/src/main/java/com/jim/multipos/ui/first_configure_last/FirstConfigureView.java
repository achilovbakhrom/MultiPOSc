package com.jim.multipos.ui.first_configure_last;

import com.jim.multipos.core.BaseView;

/**
 * Created by user on 07.10.17.
 */

public interface FirstConfigureView extends BaseView {

    void openPOSDetailsFragment(CompletionMode mode);

    void openAccountFragment(CompletionMode mode);

    void openCurrencyFragment(CompletionMode mode);

    void openPaymentTypeFragment(CompletionMode mode);

    void select(int position);

    void changeState(int position, int state);

    void makeToast(int resId);
}
