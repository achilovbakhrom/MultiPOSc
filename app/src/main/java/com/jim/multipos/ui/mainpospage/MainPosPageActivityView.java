package com.jim.multipos.ui.mainpospage;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.order.Order;

/**
 * Created by developer on 07.08.2017.
 */

public interface MainPosPageActivityView extends BaseView {
    void notifyView(Customer customer);
    void openNewOrderFrame(Long newOrderId);
    void updateIndicator(Long orderId);
    void openOrUpdateOrderHistory(Order order);
    void openOrderForEdit(String reason,Order order,Long newOrderId);


}
