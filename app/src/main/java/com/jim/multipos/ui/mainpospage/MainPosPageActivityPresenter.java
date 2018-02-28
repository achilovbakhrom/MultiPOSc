package com.jim.multipos.ui.mainpospage;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;

import java.util.List;

/**
 * Created by developer on 07.08.2017.
 */

public interface MainPosPageActivityPresenter extends Presenter{
    void onPrevClick();
    void onNextClick();
    void openOrderForEdit(Long orderId);
    void openOrderForView(Long orderId);
    void openNewOrder();
    void orderAdded(Order order);
    void onEditComplete(String reason,Order order);
    void onEditOrder(String reason);

    void onCancelOrder(String reason);
    void onRestoreOrder();

    void holdOrderClosed(Order order);
    void newOrderHolded(Order order);
    void holdOrderHolded(Order order);
    void editedOrderHolded(String reason, Order order);

    void onTillClose();
    void onOrderCanceledFromOutSide(Long orderId);
    void onOrderClosedFromOutSide(Long orderId);
    void onHeldOrderSelected(Order selectedOrder);
    void onTodayOrderSelected(Order selectedOrder);



}
