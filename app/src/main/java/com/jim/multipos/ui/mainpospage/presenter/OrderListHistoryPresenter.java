package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.Presenter;

/**
 * Created by developer on 02.02.2018.
 */

public interface OrderListHistoryPresenter extends Presenter {
     void onNextOrder();
     void onPrevOrder();
     void refreshData();
     void onClickPaymentDetials();
     void onEditOrder(String reason);
     void onDeleteOrder(String reason);
     void onRestoreDialog();
     void onBruteForce();
     void onEditClicked();
     void onCancelClicked();
}
