package com.jim.multipos.ui.mainpospage.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.ui.mainpospage.model.OrderProductItem;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

public interface ProductInfoView extends BaseView {
    void changeQuantityColor(int color);

    void showAlert();

    void hideAlert();

    void initProductData(OrderProductItem orderProductItem);

}
