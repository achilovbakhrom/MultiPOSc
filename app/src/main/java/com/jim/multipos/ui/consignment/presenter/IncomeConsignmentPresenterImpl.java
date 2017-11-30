package com.jim.multipos.ui.consignment.presenter;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.inventory.WarehouseOperations;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.products.VendorProductCon;
import com.jim.multipos.ui.consignment.view.IncomeConsignmentView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.jim.multipos.data.db.model.inventory.WarehouseOperations.INCOME_FROM_VENDOR;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public class IncomeConsignmentPresenterImpl extends BasePresenterImpl<IncomeConsignmentView> implements IncomeConsignmentPresenter {

    private Product product;
    private Vendor vendor;
    private Consignment consignment;
    private List<ConsignmentProduct> consignmentProductList;
    private DatabaseManager databaseManager;
    private double sum = 0;
    private List<Account> accountList;

    @Inject
    protected IncomeConsignmentPresenterImpl(IncomeConsignmentView incomeConsignmentView, DatabaseManager databaseManager) {
        super(incomeConsignmentView);
        this.databaseManager = databaseManager;
        consignmentProductList = new ArrayList<>();
        accountList = new ArrayList<>();
    }


    @Override
    public void setData(Long productId, Long vendorId) {

        if (productId != null) {
            databaseManager.getProductById(productId).subscribe(product -> {
                this.product = product;
                List<Vendor> vendorList = this.product.getVendor();
                for (Vendor vendor : vendorList) {
                    if (vendor.getId().equals(vendorId))
                        this.vendor = vendor;
                }
                setConsignmentItem(product);
                view.setVendorName(this.vendor.getName());
            });
        } else {
            databaseManager.getVendorById(vendorId).subscribe(vendor -> {
                this.vendor = vendor;
                view.setVendorName(this.vendor.getName());
            });
        }
        getAccounts();
    }

    @Override
    public void setConsignmentItem(Product product) {
        ConsignmentProduct consignmentProduct = new ConsignmentProduct();
        consignmentProduct.setProduct(product);
        consignmentProduct.setProductId(product.getId());
        VendorProductCon productCon = databaseManager.getVendorProductConnectionById(product.getId(), this.vendor.getId()).blockingSingle();
        if (productCon != null) {
            consignmentProduct.setCostValue(productCon.getCost());
        } else consignmentProduct.setCostValue(null);
        consignmentProduct.setCountValue(0d);
        consignmentProductList.add(consignmentProduct);
        view.fillConsignmentProductList(consignmentProductList);
        calculateConsignmentSum();
    }

    @Override
    public void saveConsignment(String number, String description, String totalAmount, String paidSum, boolean checked, int selectedPosition) {
        if (consignmentProductList.isEmpty()) {
            view.setError();
        } else {
            consignment = new Consignment();
            consignment.setConsignmentNumber(number);
            consignment.setCreatedDate(System.currentTimeMillis());
            consignment.setDescription(description);
            consignment.setTotalAmount(sum);
            consignment.setIsFromAccount(checked);
            consignment.setVendor(this.vendor);
            consignment.setConsignmentType(Consignment.INCOME_CONSIGNMENT);
            List<BillingOperations> billingOperationsList = new ArrayList<>();
            BillingOperations operationDebt = new BillingOperations();
            operationDebt.setAmount(sum);
            operationDebt.setCreateAt(System.currentTimeMillis());
            operationDebt.setVendor(this.vendor);
            operationDebt.setOperationType(BillingOperations.DEBT);
            billingOperationsList.add(operationDebt);
            BillingOperations operationPaid = null;
            if (!paidSum.equals("") && !paidSum.equals("0")) {
                operationPaid = new BillingOperations();
                operationPaid.setAmount(Double.parseDouble(paidSum));
                operationPaid.setCreateAt(System.currentTimeMillis());
                operationPaid.setVendor(this.vendor);
                operationPaid.setOperationType(BillingOperations.PAID);
                billingOperationsList.add(operationPaid);
            }
            if (checked) {
                if (operationPaid != null)
                    operationPaid.setAccount(accountList.get(selectedPosition));
                operationDebt.setAccount(accountList.get(selectedPosition));
            }
            databaseManager.insertConsignment(consignment, billingOperationsList, consignmentProductList).subscribe();
            view.closeFragment();
        }
    }

    @Override
    public void deleteFromList(ConsignmentProduct consignmentProduct) {
        consignmentProductList.remove(consignmentProduct);
    }

    @Override
    public void getAccounts() {
        databaseManager.getAllAccounts().subscribe(accounts -> {
            this.accountList = accounts;
            List<String> strings = new ArrayList<>();
            if (!accounts.isEmpty())
                for (Account account : accounts) {
                    strings.add(account.getName());
                }
            view.fillAccountsList(strings);
        });
    }

    @Override
    public void calculateConsignmentSum() {
        sum = 0;
        for (ConsignmentProduct consignmentProduct : consignmentProductList) {
            if (consignmentProduct.getCostValue() != null)
                sum += consignmentProduct.getCostValue() * consignmentProduct.getCountValue();
        }
        view.setConsignmentSumValue(sum);

    }

    @Override
    public void checkChanges(String number, String description, String totalPaid, boolean checked, int selectedPosition) {
        if (!number.equals("") || !description.equals("") || !totalPaid.equals("") || !checked || selectedPosition != 0) {
            view.openDiscardDialog();
        } else view.closeFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void loadVendorProducts() {
        this.vendor.resetProducts();
        List<Product> productList = this.vendor.getProducts();
        productList.sort((product1, t1) -> product1.getIsActive().compareTo(true));
        productList.sort((product1, t1) -> product1.getIsDeleted().compareTo(false));
        view.fillDialogItems(productList);
    }
}
