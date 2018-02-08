package com.jim.multipos.ui.cash_management.view;

import com.jim.multipos.core.BaseView;

/**
 * Created by Sirojiddin on 12.01.2018.
 */

public interface CashDetailsView extends BaseView {
    void fillTillDetails(double payOut, double payIn, double payToVendor, double incomeDebt, double bankDrop, double expectedCash, double tips, double cashTransactions);
    void updateDetails();
}
