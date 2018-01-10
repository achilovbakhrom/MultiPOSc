package com.jim.multipos.ui.mainpospage.connection;

import android.content.Context;

import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.mainpospage.model.OrderProductItem;
import com.jim.multipos.ui.mainpospage.view.OrderListView;
import com.jim.multipos.ui.mainpospage.view.ProductInfoView;
import com.jim.multipos.ui.mainpospage.view.ProductPickerView;

/**
 * Created by developer on 27.12.2017.
 */

public class MainPageConnection {
    private OrderListView orderListView;
    private ProductInfoView productInfoView;
    private ProductPickerView productPickerView;

    private Context context;
    public MainPageConnection(Context context){
        this.context = context;
    }
    public void setCount(double count){
        if(orderListView!=null){
            orderListView.setCount(count);
        }
    }
    public void changeCustomer(Customer customer){
        if(orderListView!=null){
            orderListView.changeCustomer(customer);
        }
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
    public void setDiscountToProduct(Discount discountToProduct){
        if(orderListView !=null){
            orderListView.setDiscountToProduct(discountToProduct);
        }
    }
    public void setServiceFeeProduct(ServiceFee serviceFeeProduct){
        if(orderListView !=null){
            orderListView.setServiceFeeProduct(serviceFeeProduct);
        }
    }
    public void removeOrderProducts(){
        if(orderListView!=null){
            orderListView.removeOrderProducts();
        }
    }
    public void changeDiscription(String discription){
        if(orderListView !=null){
            orderListView.changeDiscription(discription);
        }
    }
    public void giveToProductInfoFragmentProductItem(){
        if(orderListView !=null){
            orderListView.sendToProductInfoProductItem();
        }
    }
    public void changeProductVendor(Vendor vendor){
        if(orderListView != null){
            orderListView.changeProductVendor(vendor);
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

    public void addDiscountToProduct(Long productId, Discount discount, boolean isManual){
        if(orderListView!=null){
            orderListView.addDiscountToProduct(productId,discount,isManual);
        }
    }
    public void addServiceFeeToProduct(Long productId, ServiceFee serviceFee, boolean isManual){
        if(orderListView!=null){
            orderListView.addServiceFeeToProduct(productId,serviceFee,isManual);
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

    public void setProductPickerView(ProductPickerView productPickerView) {
        this.productPickerView = productPickerView;
    }

    public ProductPickerView getProductPickerView() {
        return productPickerView;
    }

    public void sendSelectedCategory(Category category, String type){
        if (productPickerView != null){
            productPickerView.updateCategoryTitles(category, type);
        }
    }
}
