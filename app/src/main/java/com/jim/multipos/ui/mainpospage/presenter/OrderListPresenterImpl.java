package com.jim.multipos.ui.mainpospage.presenter;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.order.OrderProduct;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.VendorProductCon;
import com.jim.multipos.ui.mainpospage.model.DiscountItem;
import com.jim.multipos.ui.mainpospage.model.OrderProductItem;
import com.jim.multipos.ui.mainpospage.view.OrderListView;
import com.jim.multipos.ui.mainpospage.view.ProductInfoFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;

import static com.jim.multipos.ui.consignment.ConsignmentActivity.PRODUCT_ID;


/**
 * Created by Portable-Acer on 27.10.2017.
 */

public class OrderListPresenterImpl extends BasePresenterImpl<OrderListView> implements OrderListPresenter {
    private DatabaseManager databaseManager;
    List<Object> list;
    @Inject
    public OrderListPresenterImpl(OrderListView orderListView, DatabaseManager databaseManager) {
        super(orderListView);
        list = new ArrayList<>();
        this.databaseManager = databaseManager;
    }

    @Override
    public List<Discount> getDiscounts() {
        return databaseManager.getAllDiscounts().blockingGet();
    }

    @Override
    public List<ServiceFee> getServiceFees() {
        return databaseManager.getServiceFeeOperations().getAllServiceFees().blockingSingle();
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
//        List<Product> products = databaseManager.getAllProducts().blockingFirst();
//        OrderProductItem orderProductItem = new OrderProductItem();
//
//        OrderProduct orderProduct = new OrderProduct();
//        VendorProductCon productCon = databaseManager.getVendorProductConnectionById(products.get(0).getId(), products.get(0).getVendor().get(0).getId()).blockingSingle();
//        orderProduct.setCost(productCon.getCost());
//        orderProduct.setPrice(products.get(0).getPrice());
//        orderProduct.setCount(3);
//        orderProduct.setProduct(products.get(0));
//        orderProduct.setVendor(products.get(0).getVendor().get(0));
//        orderProductItem.setOrderProduct(orderProduct);
//
//        Discount discount  = new Discount();
//        discount.setUsedType(Discount.ITEM);
//        discount.setActive(true);
//        discount.setAmountType(Discount.PERCENT);
//        discount.setName("Happy sales 10%");
//        discount.setCreatedDate(System.currentTimeMillis());
//        discount.setNotModifyted(true);
//        discount.setAmount(10);
//
//        orderProductItem.setDiscountAmmount(-2500d);
//        orderProductItem.setDiscount(discount);
//
//
//        ServiceFee serviceFee = new ServiceFee();
//        serviceFee.setApplyingType(ServiceFee.ITEM);
//        serviceFee.setActive(true);
//        serviceFee.setType(ServiceFee.PERCENT);
//        serviceFee.setName("Transportation 12%");
//        serviceFee.setCreatedDate(System.currentTimeMillis());
//        serviceFee.setNotModifyted(true);
//        serviceFee.setAmount(15);
//
//        orderProductItem.setServiceFee(serviceFee);
//        orderProductItem.setServiceFeeAmmount(3000d);
//        list.add(orderProductItem);
//
//        Discount orderDiscount  = new Discount();
//        orderDiscount.setUsedType(Discount.ORDER);
//        orderDiscount.setActive(true);
//        orderDiscount.setAmountType(Discount.PERCENT);
//        orderDiscount.setName("VIP 10%");
//        orderDiscount.setCreatedDate(System.currentTimeMillis());
//        orderDiscount.setNotModifyted(true);
//        orderDiscount.setAmount(10);
//        DiscountItem discountItem = new DiscountItem();
//        discountItem.setDiscount(orderDiscount);
//        discountItem.setAmmount(-7650d);
//
//        list.add(discountItem);
//        view.initOrderList(list);
    }

    @Override
    public void addDiscount(double amount, String description, int amountType) {
        Discount discount = new Discount();
        discount.setAmount(amount);
        discount.setName(description);
        discount.setAmountType(amountType);
        discount.setUsedType(Discount.ORDER);
        discount.setCreatedDate(System.currentTimeMillis());
        discount.setDeleted(false);
        discount.setNotModifyted(true);
        databaseManager.insertDiscount(discount).subscribe();
    }

    @Override
    public void addServiceFee(double amount, String description, int amountType) {
        ServiceFee serviceFee = new ServiceFee();
        serviceFee.setAmount(amount);
        serviceFee.setName(description);
        serviceFee.setType(amountType);
        serviceFee.setApplyingType(Discount.ORDER);
        serviceFee.setCreatedDate(System.currentTimeMillis());
        serviceFee.setDeleted(false);
        serviceFee.setNotModifyted(true);
        databaseManager.getServiceFeeOperations().addServiceFee(serviceFee).blockingSingle();
    }

    @Override
    public void onPlusCount(int position) {
        OrderProductItem orderProductItem   = (OrderProductItem) list.get(position);
        orderProductItem.getOrderProduct().setCount(orderProductItem.getOrderProduct().getCount()+1);
        list.set(position,orderProductItem);
        view.notifyItemChanged(position);
    }

    @Override
    public void onMinusCount(int position) {
        OrderProductItem orderProductItem   = (OrderProductItem) list.get(position);
        if(orderProductItem.getOrderProduct().getCount()>1){
            orderProductItem.getOrderProduct().setCount(orderProductItem.getOrderProduct().getCount()-1);
        }
        list.set(position,orderProductItem);
        view.notifyItemChanged(position);
    }

    @Override
    public void onOrderProductClick(int position) {
        OrderProductItem orderProductItem   = (OrderProductItem) list.get(position);
        view.openProductInfoFragment(orderProductItem,position);
    }

    @Override
    public void onOrderDiscountClick() {

    }

    @Override
    public void onOrderServiceFeeClick() {

    }

    @Override
    public void addProductToList(Long productId) {
        Product product = databaseManager.getProductById(productId).blockingFirst();
        OrderProductItem orderProductItem = new OrderProductItem();
        OrderProduct orderProduct = new OrderProduct();
        VendorProductCon productCon = databaseManager.getVendorProductConnectionById(product.getId(), product.getVendor().get(0).getId()).blockingSingle();
        orderProduct.setCost(productCon.getCost());
        orderProduct.setPrice(product.getPrice());
        orderProduct.setCount(1);
        orderProduct.setProduct(product);
        orderProduct.setVendor(product.getVendor().get(0));
        orderProductItem.setOrderProduct(orderProduct);
        list.add(0,orderProductItem);
        view.notifyItemAdded(0);
    }
}
