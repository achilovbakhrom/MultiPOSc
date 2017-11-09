package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.ui.mainpospage.view.PaymentView;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

public class PaymentPresenterImpl extends BasePresenterImpl<PaymentView> implements PaymentPresenter {
    public PaymentPresenterImpl(PaymentView paymentView) {
        super(paymentView);
    }
}
