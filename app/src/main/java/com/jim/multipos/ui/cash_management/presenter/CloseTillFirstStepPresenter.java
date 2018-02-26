package com.jim.multipos.ui.cash_management.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.order.Order;

/**
 * Created by Sirojiddin on 12.01.2018.
 */

public interface CloseTillFirstStepPresenter extends Presenter {
    void initAdapterData();
    void checkCompletion();
    void onReturn(Order order, int position);
    void onClose(Order order, int position);
    void onGoToOrder(Order order);
}
