package com.jim.multipos.ui.billing_vendor.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.ui.billing_vendor.fragments.BillingOperationFragment;

import java.util.Calendar;

/**
 * Created by developer on 30.11.2017.
 */

public interface BillingOperationPresenter extends Presenter {
    void filterBy(BillingOperationFragment.SortModes searchMode);
    void filterInvert();
    void findVendor(Long vendorId);
    void updateBillings();
    void dateIntervalPicked(Calendar fromDate, Calendar toDate);
    void datePicked(Calendar pickedDate);
    void clearIntervals();

}
