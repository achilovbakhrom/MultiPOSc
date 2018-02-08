package com.jim.multipos.ui.cash_management.presenter;

import com.jim.multipos.core.Presenter;

/**
 * Created by Sirojiddin on 11.01.2018.
 */

public interface CashOperationsPresenter extends Presenter {
    void changePayment(int position);
    void initData();
    void doPayIn(double payInAmount);
    void doPayOut(double payOutAmount);
    void doBankDrop(double bankDropAmount);
    void executeOperation(double amount, int operationType, String description);
    void showCloseTillDialog();
    void showOpenTillDialog();
}
