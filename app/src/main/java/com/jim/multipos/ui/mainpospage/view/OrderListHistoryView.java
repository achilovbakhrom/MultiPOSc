package com.jim.multipos.ui.mainpospage.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.order.Order;

/**
 * Created by developer on 02.02.2018.
 */

public interface OrderListHistoryView extends BaseView {
    void hideMeAndShowOrderList();
    void updateDetials(Order order);
}
