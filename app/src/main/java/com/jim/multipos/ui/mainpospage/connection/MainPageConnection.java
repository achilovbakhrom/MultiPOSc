package com.jim.multipos.ui.mainpospage.connection;

import android.content.Context;

import com.itextpdf.text.pdf.PdfICCBased;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.ui.mainpospage.model.OrderProductItem;
import com.jim.multipos.ui.mainpospage.view.OrderListHistoryView;
import com.jim.multipos.ui.mainpospage.view.OrderListView;
import com.jim.multipos.ui.mainpospage.view.PaymentView;
import com.jim.multipos.ui.mainpospage.view.ProductFolderView;
import com.jim.multipos.ui.mainpospage.view.ProductInfoView;
import com.jim.multipos.ui.mainpospage.view.ProductPickerView;

import java.util.List;

/**
 * Created by developer on 27.12.2017.
 * --------------
 * This class is connector for some fragments which located in main page
 * Оf course we could do it through a rxBus or EventBus, but the code would be terribly incomprehensible
 * This method work perfectly for this case
 * All fragments Inject this class and sends their views
 * For review you just travel in code with hot keys "ctrl+alt+b"
 * --------------
 */

public class MainPageConnection {

    private OrderListView orderListView;
    private ProductInfoView productInfoView;
    private ProductPickerView productPickerView;
    private PaymentView paymentView;
    private OrderListHistoryView orderListHistoryView;
    private ProductFolderView productFolderView;
    private Context context;

    public MainPageConnection(Context context){
        this.context = context;
    }
    public void onHoldOrderSendingData(Order order, List<PayedPartitions> payedPartitions,Debt debt){
        if(orderListView!=null){
            orderListView.onHoldOrderSendingData(order,payedPartitions,debt);
        }
    }
    public void onHoldOrderClicked(){
        if(paymentView != null){
            paymentView.onHoldOrderClicked();
        }
    }

    public void onStockPositionClicked(){
        if(orderListView!=null) {
            orderListView.onStockPositionClicked();
        }
    }
    public void sendDataToPaymentFragmentWhenEdit(Order order, List<PayedPartitions> payedPartitions,Debt debt){
        if(paymentView != null){
            paymentView.sendDataToPaymentFragmentWhenEdit(order,payedPartitions,debt);
        }
    }


    public void onNewOrderPaymentFragment(){
        if(paymentView!=null){
            paymentView.onNewOrder();
        }
    }
    public void updateCustomer(Customer customer){
        if(orderListView!=null){
            orderListView.updateCustomer(customer);
        }
    }
    public void onPayedPartition(){
        if(orderListView!=null){
           orderListView.onPayedPartition();
        }
    }
    public void onCloseOrder(Order order, List<PayedPartitions> payedPartitions, Debt debt){
        if(orderListView!=null){
            orderListView.onCloseOrder(order,payedPartitions,debt);
        }
    }
    public void addProductWithWeightToListEdit(double weight){
        if(orderListView!=null){
            orderListView.addProductWithWeightToListEdit(weight);
        }
    }

    public void setCount(double count){
        if(orderListView!=null){
            orderListView.setCount(count);
        }
    }

    public void scanBarcode(boolean fromAddCustomer){
        if (orderListView != null){
            orderListView.initScan();
            orderListView.scanBarcodeFor(fromAddCustomer);
        }
    }

    public void changeCustomer(Customer customer){
        if(orderListView!=null){
            orderListView.changeCustomer(customer);
        }
    }

    public void setCustomer(Customer customer){
        if (paymentView != null){
            paymentView.setCustomer(customer);
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
    public void sendDataToPaymentFragment(Order order, List<PayedPartitions> payedPartitions){
        if(paymentView!=null){
            paymentView.getDataFromListOrder(order,payedPartitions);
        }
    }
    public void giveToProductInfoFragmentProductItem(){
        if(orderListView !=null){
            orderListView.sendToProductInfoProductItem();
        }
    }
    public void giveToPaymentFragmentOrderAndPaymentsList(){
        if(orderListView!=null){
            orderListView.sendToPaymentFragmentOrderAndPaymentsList();
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

    public void setProductFolderView(ProductFolderView productFolderView) {
        this.productFolderView = productFolderView;
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

    public PaymentView getPaymentView() {
        return paymentView;
    }

    public void setPaymentView(PaymentView paymentView) {
        this.paymentView = paymentView;
    }

    public void sendSelectedCategory(Category category, String type){
        if (productPickerView != null){
            productPickerView.updateCategoryTitles(category, type);
        }
    }

    public OrderListHistoryView getOrderListHistoryView() {
        return orderListHistoryView;
    }

    public void setOrderListHistoryView(OrderListHistoryView orderListHistoryView) {
        this.orderListHistoryView = orderListHistoryView;
    }
}
