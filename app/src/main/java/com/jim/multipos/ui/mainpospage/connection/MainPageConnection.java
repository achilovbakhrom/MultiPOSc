package com.jim.multipos.ui.mainpospage.connection;

import android.content.Context;

import com.jim.multipos.ui.mainpospage.model.OrderProductItem;
import com.jim.multipos.ui.mainpospage.view.OrderListView;
import com.jim.multipos.ui.mainpospage.view.ProductInfoView;

/**
 * Created by developer on 27.12.2017.
 */

public class MainPageConnection {
    OrderListView orderListView;
    ProductInfoView productInfoView;
    private Context context;

    public MainPageConnection(Context context){
        this.context = context;
    }
    public void plusProductCount(){
        if(orderListView!=null){
            orderListView.plusProductCount();
        }
    }
    public void minusProductCount(){
        if(orderListView!=null){
            orderListView.minusProductCount();
        }
    }

    public void giveToProductInfoFragmentProductItem(){
        if(orderListView !=null){
            orderListView.sendToProductInfoProductItem();
        }
    }
    public void sendProductItemToProductInfo(OrderProductItem orderProductItem){
        if(productInfoView!=null){
            productInfoView.initProductData(orderProductItem);
        }
    }
    public void addProductToOrder(Long productId){
        if(orderListView!=null){
            orderListView.addProductToOrder(productId);
        }
    }



    public OrderListView getOrderListView() {
        return orderListView;
    }

    public void setOrderListView(OrderListView orderListView) {
        this.orderListView = orderListView;
    }

    public ProductInfoView getProductInfoView() {
        return productInfoView;
    }

    public void setProductInfoView(ProductInfoView productInfoView) {
        this.productInfoView = productInfoView;
    }
}
