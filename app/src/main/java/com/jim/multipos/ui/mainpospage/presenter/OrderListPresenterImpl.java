package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.ui.mainpospage.view.OrderListView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;


/**
 * Created by Portable-Acer on 27.10.2017.
 */

public class OrderListPresenterImpl extends BasePresenterImpl<OrderListView> implements OrderListPresenter {
    private DatabaseManager databaseManager;

    @Inject
    public OrderListPresenterImpl(OrderListView orderListView, DatabaseManager databaseManager) {
        super(orderListView);

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
    public void addDiscount(double amount, String description, int amountType) {
        Discount discount = new Discount();
        discount.setAmount(amount);
        discount.setName(description);
        discount.setAmountType(amountType);
        discount.setUsedType(Discount.ORDER);
        discount.setCreatedDate(System.currentTimeMillis());
        discount.setDeleted(false);
        discount.setNotModifyted(true);

        if (amountType == Discount.REPRICE) {
            discount.setDeleted(true);
        }

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

        if (amountType == ServiceFee.REPRICE) {
            serviceFee.setDeleted(true);
        }

        databaseManager.getServiceFeeOperations().addServiceFee(serviceFee).blockingSingle();
    }
}
