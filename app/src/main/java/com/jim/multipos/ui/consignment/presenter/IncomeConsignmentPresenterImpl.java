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

import static com.jim.multipos.data.db.model.consignment.Consignment.INCOME_CONSIGNMENT;
import static com.jim.multipos.data.db.model.inventory.BillingOperations.DEBT_CONSIGNMENT;
import static com.jim.multipos.data.db.model.inventory.BillingOperations.PAID_TO_CONSIGNMENT;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public class IncomeConsignmentPresenterImpl extends BasePresenterImpl<IncomeConsignmentView> implements IncomeConsignmentPresenter {

    private Product product;
    private Vendor vendor;
    private Consignment consignment;
    private List<ConsignmentProduct> consignmentProductList, tempProductList;
    private List<BillingOperations> billingOperations;
    private DatabaseManager databaseManager;
    private double sum = 0;
    private List<Account> accountList;

    @Inject
    protected IncomeConsignmentPresenterImpl(IncomeConsignmentView incomeConsignmentView, DatabaseManager databaseManager) {
        super(incomeConsignmentView);
        this.databaseManager = databaseManager;
        consignmentProductList = new ArrayList<>();
        accountList = new ArrayList<>();
        tempProductList = new ArrayList<>();
        billingOperations = new ArrayList<>();
    }


    @Override
    public void setData(Long productId, Long vendorId, Long consignmentId) {
        getAccounts();
        if (consignmentId != null) {
            this.consignment = databaseManager.getConsignmentById(consignmentId).blockingGet();
            consignmentProductList = databaseManager.getConsignmentProductsByConsignmentId(consignmentId).blockingGet();
            tempProductList.addAll(consignmentProductList);
            view.fillConsignmentProductList(consignmentProductList);
            calculateConsignmentSum();
            this.vendor = this.consignment.getVendor();
            view.setVendorName(this.vendor.getName());
            BillingOperations operations = null;
            billingOperations = databaseManager.getBillingOperations().blockingSingle();
            for (BillingOperations operation : billingOperations) {
                if (operation.getOperationType() == PAID_TO_CONSIGNMENT)
                    operations = operation;
            }
            int selectedAccount = 0;
            if (operations != null) {
                if (consignment.getIsFromAccount()) {
                    for (int i = 0; i < accountList.size(); i++) {
                        if (accountList.get(i).getId().equals(operations.getAccountId()))
                            selectedAccount = i;
                    }
                    view.setAccountSpinnerSelection(selectedAccount);
                }
                view.fillConsignmentData(consignment.getConsignmentNumber(), consignment.getDescription(), consignment.getIsFromAccount(), operations.getAmount());
            } else
                view.fillConsignmentData(consignment.getConsignmentNumber(), consignment.getDescription(), consignment.getIsFromAccount(), 0.0d);
        } else if (productId != null) {
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
            if (consignment == null) {
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
                operationDebt.setAmount(sum * -1);
                operationDebt.setCreateAt(System.currentTimeMillis());
                operationDebt.setVendor(this.vendor);
                operationDebt.setOperationType(BillingOperations.DEBT_CONSIGNMENT);
                billingOperationsList.add(operationDebt);
                BillingOperations operationPaid;
                if (!paidSum.equals("") && !paidSum.equals("0")) {
                    operationPaid = new BillingOperations();
                    operationPaid.setAmount(Double.parseDouble(paidSum));
                    operationPaid.setCreateAt(System.currentTimeMillis());
                    operationPaid.setVendor(this.vendor);
                    operationPaid.setOperationType(PAID_TO_CONSIGNMENT);
                    if (checked) {
                        operationPaid.setAccount(accountList.get(selectedPosition));
                    }
                    billingOperationsList.add(operationPaid);
                }
                List<WarehouseOperations> warehouseOperationsList = new ArrayList<>();
                for (ConsignmentProduct consignmentProduct : consignmentProductList) {
                    WarehouseOperations warehouseOperations = new WarehouseOperations();
                    warehouseOperations.setProduct(consignmentProduct.getProduct());
                    warehouseOperations.setVendor(consignment.getVendor());
                    warehouseOperations.setCreateAt(System.currentTimeMillis());
                    warehouseOperations.setType(WarehouseOperations.INCOME_FROM_VENDOR);
                    warehouseOperations.setValue(consignmentProduct.getCountValue());
                    warehouseOperationsList.add(warehouseOperations);
                }
                databaseManager.insertConsignment(consignment, billingOperationsList, consignmentProductList, warehouseOperationsList).subscribe();
                view.closeFragment();
            } else {
                Consignment consignmentNew = new Consignment();
                consignmentNew.setConsignmentNumber(number);
                consignmentNew.setCreatedDate(System.currentTimeMillis());
                consignmentNew.setDescription(description);
                consignmentNew.setTotalAmount(sum);
                consignmentNew.setIsFromAccount(checked);
                consignmentNew.setVendor(this.vendor);
                consignmentNew.setConsignmentType(Consignment.INCOME_CONSIGNMENT);
                if (consignment.getRootId() == null)
                    consignmentNew.setRootId(consignment.getId());
                else consignmentNew.setRootId(consignment.getRootId());
                consignment.setIsNotModified(false);
                databaseManager.insertConsignment(consignment, null, null, null);
                List<BillingOperations> billingOperationsList = new ArrayList<>();
                BillingOperations billing;
                if (consignment.getTotalAmount() != sum) {
                    for (BillingOperations operation : billingOperations) {
                        if (operation.getOperationType() == DEBT_CONSIGNMENT) {
                            billing = new BillingOperations();
                            billing.setAmount(sum * -1);
                            billing.setCreateAt(System.currentTimeMillis());
                            billing.setVendor(this.vendor);
                            billing.setOperationType(BillingOperations.DEBT_CONSIGNMENT);
                            billingOperationsList.add(billing);
                            operation.setIsNotModified(false);
                            databaseManager.insertBillingOperation(operation).subscribe();
                        }
                    }
                }
                BillingOperations operationPaid;
                if (!paidSum.equals("") && !paidSum.equals("0")) {
                    for (BillingOperations operation : billingOperations) {
                        if (operation.getOperationType() == PAID_TO_CONSIGNMENT && operation.getAmount() != Double.parseDouble(paidSum)) {
                            operationPaid = new BillingOperations();
                            operationPaid.setAmount(Double.parseDouble(paidSum));
                            operationPaid.setCreateAt(System.currentTimeMillis());
                            operationPaid.setVendor(this.vendor);
                            operationPaid.setOperationType(PAID_TO_CONSIGNMENT);
                            operation.setIsNotModified(false);
                            if (checked) {
                                operationPaid.setAccount(accountList.get(selectedPosition));
                            }
                            databaseManager.insertBillingOperation(operation).subscribe();
                            billingOperationsList.add(operationPaid);
                        }
                    }
                }
                List<WarehouseOperations> warehouseOperationsList = new ArrayList<>();
                for (int i = 0; i < consignmentProductList.size(); i++) {
                    if (tempProductList.contains(consignmentProductList.get(i))) {
                        double count = 0;
                        for (ConsignmentProduct product : tempProductList) {
                            if (product.getId().equals(consignmentProductList.get(i).getId())) {
                                count = product.getCountValue();
                                break;
                            }
                        }
                        if (count != consignmentProductList.get(i).getCountValue()) {
                            WarehouseOperations warehouseOperations = new WarehouseOperations();
                            warehouseOperations.setProduct(consignmentProductList.get(i).getProduct());
                            warehouseOperations.setVendor(consignmentNew.getVendor());
                            warehouseOperations.setCreateAt(System.currentTimeMillis());
                            warehouseOperations.setType(WarehouseOperations.INCOME_FROM_VENDOR);
                            warehouseOperations.setValue(consignmentProductList.get(i).getCountValue() - count);
                            warehouseOperationsList.add(warehouseOperations);
                        } else warehouseOperationsList.add(null);

                    } else {
                        WarehouseOperations warehouseOperations = new WarehouseOperations();
                        warehouseOperations.setProduct(consignmentProductList.get(i).getProduct());
                        warehouseOperations.setVendor(consignmentNew.getVendor());
                        warehouseOperations.setCreateAt(System.currentTimeMillis());
                        warehouseOperations.setType(WarehouseOperations.INCOME_FROM_VENDOR);
                        warehouseOperations.setValue(consignmentProductList.get(i).getCountValue());
                        warehouseOperationsList.add(warehouseOperations);
                    }
                }
                databaseManager.insertConsignment(consignmentNew, billingOperationsList, consignmentProductList, warehouseOperationsList).subscribe();
                view.closeFragment();
            }
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
