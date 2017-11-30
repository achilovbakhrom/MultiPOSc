package com.jim.multipos.ui.mainpospage.presenter;

import android.os.Bundle;
import android.util.Log;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.ui.mainpospage.view.OrderListView;

import org.greenrobot.greendao.database.Database;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import static com.jim.multipos.ui.service_fee_new.Constants.TYPE_REPRICE;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

public class OrderListPresenterImpl extends BasePresenterImpl<OrderListView> implements OrderListPresenter {
    private DatabaseManager databaseManager;
    private String[] discountAmountTypes;

    @Inject
    public OrderListPresenterImpl(OrderListView orderListView, DatabaseManager databaseManager, @Named(value = "discount_amount_types") String[] discountAmountTypes) {
        super(orderListView);

        this.databaseManager = databaseManager;
        this.discountAmountTypes = discountAmountTypes;
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
    public void addDiscount(double amount, String description, String amountType) {
        Discount discount = new Discount();
        discount.setAmount(amount);
        discount.setDiscription(description);
        discount.setAmountType(amountType);
        discount.setCreatedDate(System.currentTimeMillis());
        discount.setDeleted(false);
        discount.setNotModifyted(true);

        if (amountType.equals(discountAmountTypes[2])) {
            discount.setDeleted(true);
        }

        databaseManager.insertDiscount(discount).subscribe();
    }

    @Override
    public void addServiceFee(double amount, String description, String amountType) {
        ServiceFee serviceFee = new ServiceFee();
        serviceFee.setAmount(amount);
        serviceFee.setReason(description);
        serviceFee.setType(amountType);
        serviceFee.setCreatedDate(System.currentTimeMillis());
        serviceFee.setDeleted(false);
        serviceFee.setNotModifyted(true);

        if (amountType.equals(TYPE_REPRICE)) {
            serviceFee.setDeleted(true);
        }

        databaseManager.getServiceFeeOperations().addServiceFee(serviceFee).blockingSingle();
    }
}
