package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.DiscountLog;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.ServiceFeeLog;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.inventory.model.InventoryItem;
import com.jim.multipos.ui.mainpospage.view.ProductInfoView;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

public class ProductInfoPresenterImpl extends BasePresenterImpl<ProductInfoView> implements ProductInfoPresenter {
    private DatabaseManager databaseManager;
    private Product product;
    private InventoryItem inventoryItem;
    private int currentVendorPosition = 0;
    private double productQuantity = 20;
    private int currentProductQuantity = 1;
    private String[] discountAmountTypes;

    @Inject
    public ProductInfoPresenterImpl(ProductInfoView productInfoView, DatabaseManager databaseManager, @Named(value = "discount_amount_types") String[] discountAmountTypes) {
        super(productInfoView);
        this.databaseManager = databaseManager;
        this.discountAmountTypes = discountAmountTypes;
    }

    @Override
    public void initProduct(Long productId) {
    }



    @Override
    public Double getProductQuantity() {
        double result = productQuantity - currentProductQuantity;

        if (result >= 0) {
            view.changeQuantityColor(R.color.colorBlue);
            view.hideAlert();
        } else {
            view.changeQuantityColor(R.color.colorDarkRed);
            view.showAlert();
        }

        return result;
    }

    @Override
    public int getCurrentProductQuantity() {
        return currentProductQuantity;
    }

    @Override
    public int incrementQuantity() {
        return ++currentProductQuantity;
    }

    @Override
    public int decrementQuantity() {
        currentProductQuantity--;

        if (currentProductQuantity < 1) {
            currentProductQuantity = 1;
        }

        return currentProductQuantity;
    }

    @Override
    public void setCurrentQuantity(int quantity) {
        currentProductQuantity = quantity;
    }

    @Override
    public List<ServiceFee> getServiceFees() {
        return databaseManager.getServiceFeeOperations().getServiceFeesWithAllItemTypes().blockingSingle();
    }

    @Override
    public List<Discount> getDiscount(String[] discountType) {
        List<Discount> discounts = databaseManager.getDiscountsWithAllItemTypes(discountType).blockingSingle();

        return discounts;
    }

    @Override
    public void addDiscount(double amount, String description, int amountType) {
        Discount discount = new Discount();
        discount.setAmount(amount);
        discount.setName(description);
        discount.setAmountType(amountType);
        discount.setUsedType(Discount.ITEM);
        discount.setCreatedDate(System.currentTimeMillis());
        discount.setDeleted(false);
        discount.setNotModifyted(true);
        databaseManager.insertDiscount(discount).subscribe(discount1 -> {
            DiscountLog discountLog = new DiscountLog();
            discountLog.setChangeDate(System.currentTimeMillis());
            discountLog.setDiscount(discount1);
            discountLog.setStatus(DiscountLog.DISCOUNT_ADDED);
            databaseManager.insertDiscountLog(discountLog).subscribe();
        });
    }

    @Override
    public void addServiceFee(double amount, String description, int amountType) {
        ServiceFee serviceFee = new ServiceFee();
        serviceFee.setAmount(amount);
        serviceFee.setName(description);
        serviceFee.setType(amountType);
        serviceFee.setApplyingType(Discount.ITEM);
        serviceFee.setCreatedDate(System.currentTimeMillis());
        serviceFee.setDeleted(false);
        serviceFee.setNotModifyted(true);

        databaseManager.getServiceFeeOperations().addServiceFee(serviceFee).subscribe(serviceFee1 -> {
            ServiceFeeLog serviceFeeLog = new ServiceFeeLog();
            serviceFeeLog.setServiceFee(serviceFee1);
            serviceFeeLog.setChangeDate(System.currentTimeMillis());
            serviceFeeLog.setStatus(ServiceFeeLog.SERVICE_FEE_ADDED);
            databaseManager.insertServiceFeeLog(serviceFeeLog).subscribe();
        });
    }
}
