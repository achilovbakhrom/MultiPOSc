package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.products.Vendor;

import java.util.List;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

public interface OrderListPresenter extends Presenter {
    List<Discount> getDiscounts();
    List<ServiceFee> getServiceFees();
    void addDiscount(double amount, String description, int amountType);
    void addServiceFee(double amount, String description, int amountType);
    void onPlusCount(int position);
    void onMinusCount(int position);
    void setCount(int position,double count);
    void onOrderProductClick(int position);
    void onOrderDiscountClick();
    void onOrderServiceFeeClick();
    void addProductToList(Long productId);
    void addDiscountToProduct(Long productId, Discount discount, boolean isManual);
    void addServiceFeeToProduct(Long productId, ServiceFee serviceFee, boolean isManual);
    void openDiscountDialog();
    void openSeriveFeeDialog();
    void changeProductVendor(Vendor vendor,int position);
    void changeDiscription(String discription,int position);
    void removeOrderProducts(int removePosition);
    void setDiscountToProduct(Discount discountToProduct,int currentPosition);
    void setServiceFeeProduct(ServiceFee serviceFeeProduct,int currentPosition);
    void changeCustomer(Customer customer);
    void onClickChooseCustomerButton();
}
