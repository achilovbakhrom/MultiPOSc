package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.order.Order;

/**
 * Created by developer on 02.02.2018.
 */

public interface OrderListHistoryPresenter extends Presenter {
     void refreshData(Order order);
     void onClickPaymentDetials();

     void onBruteForce();
     void onEditClicked();
     void onCancelClicked();
}
