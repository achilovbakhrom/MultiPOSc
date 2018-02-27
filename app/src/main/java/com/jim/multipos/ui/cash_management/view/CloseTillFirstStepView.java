package com.jim.multipos.ui.cash_management.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.order.Order;

import java.util.List;

/**
 * Created by Sirojiddin on 12.01.2018.
 */

public interface CloseTillFirstStepView extends BaseView {
    void setAdapterList(List<Order> orderList);
    void checkCompletion();
    void firstStepCompletionStatus(boolean status);
    void collectData();
    void updateOrderList();
    void sendEvent(String event, Long id);
}
