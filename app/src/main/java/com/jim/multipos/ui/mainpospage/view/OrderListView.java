package com.jim.multipos.ui.mainpospage.view;

import android.os.Bundle;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.ui.mainpospage.model.OrderProductItem;

import java.util.List;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

public interface OrderListView extends BaseView {
    void initOrderList(List<Object> objectList);
    void notifyList();
    void notifyItemAdded(int adapterPosition);
    void notifyItemChanged(int adapterPosition);
    void openProductInfoFragment(OrderProductItem orderProductItem,int position);
    void sendToProductInfoProductItem();
    void plusProductCount();
    void minusProductCount();
    void addProductToOrder(Long productId);
}
