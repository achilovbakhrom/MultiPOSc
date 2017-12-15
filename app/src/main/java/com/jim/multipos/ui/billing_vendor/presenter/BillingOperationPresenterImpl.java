package com.jim.multipos.ui.billing_vendor.presenter;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.billing_vendor.fragments.BillingOperationFragment;
import com.jim.multipos.ui.billing_vendor.fragments.BillingOperationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;

import static com.jim.multipos.ui.billing_vendor.fragments.BillingOperationFragment.SortModes.TIME;

/**
 * Created by developer on 30.11.2017.
 */

public class BillingOperationPresenterImpl extends BasePresenterImpl<BillingOperationView> implements BillingOperationPresenter {
    @Inject
    DatabaseManager databaseManager;
    Vendor vendor = null;
    List<BillingOperations> billingOperations;
    int SORTING = 1;
    BillingOperationFragment.SortModes sortModes = TIME;
    Calendar fromDate, toDate;
    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);

    }

    @Inject
    protected BillingOperationPresenterImpl(BillingOperationView billingOperationView) {
        super(billingOperationView);
    }

    @Override
    public void filterBy(BillingOperationFragment.SortModes sortModes) {
        this.sortModes = sortModes;
        SORTING =1;
        sortList();
        view.notifyList();
    }

    @Override
    public void filterInvert() {
        SORTING *=-1;
        sortList();
        view.notifyList();
    }

    @Override
    public void findVendor(Long vendorId) {
        databaseManager.getVendorById(vendorId).subscribe(vendor -> {
            this.vendor = vendor;
            view.setVendorData(vendor);
            collectBillings();
        });
    }

    @Override
    public void updateBillings() {
        collectBillings();

        databaseManager.getVendorDebt(vendor.getId()).subscribe((aDouble, throwable) -> {
            view.updateDebt(aDouble);
        });
    }

    @Override
    public void dateIntervalPicked(Calendar fromDate, Calendar toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        databaseManager.getBillingOperationInteval(vendor.getId(),fromDate,toDate).subscribe((billingOperations1, throwable) -> {
            billingOperations.clear();
            billingOperations.addAll(billingOperations1);
            sortList();
            view.setTransactions(billingOperations);
        });
    }

    @Override
    public void datePicked(Calendar pickedDate) {
        Calendar temp = new GregorianCalendar();
        temp.setTimeInMillis(pickedDate.getTimeInMillis());
        this.fromDate = temp;
        this.toDate = null;
        databaseManager.getBillingOperationInteval(vendor.getId(),pickedDate,temp).subscribe((billingOperations1, throwable) -> {
            billingOperations.clear();
            billingOperations.addAll(billingOperations1);
            sortList();
            view.setTransactions(billingOperations);
        });
    }

    private void collectBillings(){
        if(fromDate != null && toDate !=null){
            databaseManager.getBillingOperationInteval(vendor.getId(),fromDate,toDate).subscribe((billingOperations1, throwable) -> {
                billingOperations.clear();
                billingOperations.addAll(billingOperations1);
                sortList();
                view.setTransactions(billingOperations);
            });
        }else if(fromDate !=null){
            Calendar temp = new GregorianCalendar();
            temp.setTimeInMillis(fromDate.getTimeInMillis());
            databaseManager.getBillingOperationInteval(vendor.getId(),fromDate,temp).subscribe((billingOperations1, throwable) -> {
                billingOperations.clear();
                billingOperations.addAll(billingOperations1);
                sortList();
                view.setTransactions(billingOperations);
            });
        }else
        databaseManager.getBillingOperationForVendor(vendor.getId()).subscribe((billingOperations1, throwable) -> {
            billingOperations = billingOperations1;
            sortList();
            view.setTransactions(billingOperations);
        });
    }
    private void sortList(){
        switch (sortModes){
            case TIME:
                Collections.sort(billingOperations,(billing, t1) -> t1.getPaymentDate().compareTo(billing.getPaymentDate())*SORTING);
                break;
            case OPERATION:
                Collections.sort(billingOperations,(billing, t1) -> billing.getOperationType().compareTo(t1.getOperationType())*SORTING);
                break;
            case EXTRA:
                Collections.sort(billingOperations,(billing, t1) -> {
                    if(t1.getConsignment()!=null && billing.getConsignment()!=null &&t1.getConsignment().getConsignmentNumber()!=null && billing.getConsignment().getConsignmentNumber()!=null)
                        return t1.getConsignment().getConsignmentNumber().compareTo(billing.getConsignment().getConsignmentNumber())*SORTING;
                    else return -1;
                });
                break;
            case DESCRIPTION:
                Collections.sort(billingOperations,(billing, t1) -> {
                    String from ="";
                    String to ="";
                    if(billing.getOperationType() == BillingOperations.PAID_TO_CONSIGNMENT)
                        if(billing.getDescription() !=null)
                            from = billing.getDescription();
                    if(t1.getOperationType() == BillingOperations.PAID_TO_CONSIGNMENT)
                        if(t1.getDescription() !=null)
                            to = t1.getDescription();

                    if(billing.getOperationType() == BillingOperations.DEBT_CONSIGNMENT && billing.getOperationType() == BillingOperations.PAID_TO_CONSIGNMENT)
                        if( billing.getConsignment() !=null &&billing.getConsignment().getDescription()!=null)
                            from = billing.getConsignment().getDescription();

                    if(t1.getOperationType() == BillingOperations.DEBT_CONSIGNMENT && t1.getOperationType() == BillingOperations.PAID_TO_CONSIGNMENT)
                        if( t1.getConsignment() !=null &&t1.getConsignment().getDescription()!=null)
                            to = t1.getConsignment().getDescription();
                        return to.compareTo(from)*SORTING;
                });
                break;
            case PAYMENT:
                Collections.sort(billingOperations,(billing, t1) -> t1.getAmount().compareTo(billing.getAmount())*SORTING);
                break;
        }

    }
    @Override
    public void clearIntervals(){
        fromDate = null;
        toDate = null;
        updateBillings();
    }
}
