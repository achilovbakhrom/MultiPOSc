package com.jim.multipos.ui.consignment.presenter;

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
import com.jim.multipos.ui.consignment.model.TempProduct;
import com.jim.multipos.ui.consignment.view.IncomeConsignmentView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.jim.multipos.data.db.model.inventory.BillingOperations.DEBT_CONSIGNMENT;
import static com.jim.multipos.data.db.model.inventory.BillingOperations.PAID_TO_CONSIGNMENT;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public class IncomeConsignmentPresenterImpl extends BasePresenterImpl<IncomeConsignmentView> implements IncomeConsignmentPresenter {

    private Product product;
    private Vendor vendor;
    private Consignment consignment;
    private List<ConsignmentProduct> consignmentProductList, deletedProductsList;
    private List<Long> ids;
    private List<TempProduct> tempProductList;
    private List<BillingOperations> billingOperations;
    private DatabaseManager databaseManager;
    private DecimalFormat decimalFormat;
    private double sum = 0;
    private List<Account> accountList;
    private String number, description, totalAmount;
    private boolean checked;
    private double paidSum = 0;
    private int selectedPosition;
    public static final int ADD = 0;
    public static final int EDIT = 1;
    private int viewType = ADD;
    private Long productId;

    @Inject
    protected IncomeConsignmentPresenterImpl(IncomeConsignmentView incomeConsignmentView, DatabaseManager databaseManager, DecimalFormat decimalFormat) {
        super(incomeConsignmentView);
        this.databaseManager = databaseManager;
        this.decimalFormat = decimalFormat;
        consignmentProductList = new ArrayList<>();
        accountList = new ArrayList<>();
        deletedProductsList = new ArrayList<>();
        tempProductList = new ArrayList<>();
        ids = new ArrayList<>();
        billingOperations = new ArrayList<>();
    }


    @Override
    public void setData(Long productId, Long vendorId, Long consignmentId) {
        this.productId = productId;
        getAccounts();
        if (consignmentId != null) {
            this.consignment = databaseManager.getConsignmentById(consignmentId).blockingGet();
            consignmentProductList = this.consignment.getAllConsignmentProducts();
            for (int i = 0; i < consignmentProductList.size(); i++) {
                TempProduct tempProduct = new TempProduct();
                tempProduct.setId(consignmentProductList.get(i).getId());
                tempProduct.setCount(consignmentProductList.get(i).getCountValue());
                tempProduct.setCost(consignmentProductList.get(i).getCostValue());
                tempProductList.add(tempProduct);
                ids.add(consignmentProductList.get(i).getId());
            }
            viewType = EDIT;
            view.fillConsignmentProductList(consignmentProductList, viewType);
            calculateConsignmentSum();
            this.vendor = this.consignment.getVendor();
            view.setVendorName(this.vendor.getName());
            BillingOperations firstPay = null;
            if (this.consignment.getFirstPayId() != null)
                firstPay = databaseManager.getBillingOperationsById(this.consignment.getFirstPayId()).blockingGet();
            BillingOperations debt = databaseManager.getBillingOperationsByConsignmentId(this.consignment.getId()).blockingGet();
            billingOperations.add(debt);
            int selectedAccount = 0;
            if (firstPay != null) {
                if (consignment.getIsFromAccount()) {
                    for (int i = 0; i < accountList.size(); i++) {
                        if (accountList.get(i).getId().equals(firstPay.getAccountId()))
                            selectedAccount = i;
                    }
                    view.setAccountSpinnerSelection(selectedAccount);
                }
                billingOperations.add(firstPay);
                view.fillConsignmentData(consignment.getConsignmentNumber(), consignment.getDescription(), consignment.getIsFromAccount(), firstPay.getAmount());
            } else
                view.fillConsignmentData(consignment.getConsignmentNumber(), consignment.getDescription(), consignment.getIsFromAccount(), 0.0d);
        } else if (productId != null) {
            viewType = ADD;
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
            view.setConsignmentNumber(databaseManager.getConsignments().blockingSingle().size() + 1);
        } else {
            viewType = ADD;
            databaseManager.getVendorById(vendorId).subscribe(vendor -> {
                this.vendor = vendor;
                view.setVendorName(this.vendor.getName());
            });
            view.setConsignmentNumber(databaseManager.getConsignments().blockingSingle().size() + 1);
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
        consignmentProduct.setCountValue(0.0d);
        consignmentProductList.add(consignmentProduct);
        view.fillConsignmentProductList(consignmentProductList, viewType);
        calculateConsignmentSum();
    }

    @Override
    public void saveConsignment(String number, String description, String totalAmount, String paidSum, boolean checked, int selectedPosition) {
        this.number = number;
        this.description = description;
        this.totalAmount = totalAmount;
        try {
            this.paidSum = decimalFormat.parse(paidSum).doubleValue();
        } catch (ParseException e) {
            this.paidSum = 0;
            e.printStackTrace();
        }
        this.checked = checked;
        this.selectedPosition = selectedPosition;
        boolean hasOpenTill = databaseManager.hasOpenTill().blockingGet();
        if (!hasOpenTill && checked) {
            view.setError("Opened till wasn't found. Please, open till");
        } else if (consignmentProductList.isEmpty()) {
            view.setError("Please, add product to consignment");
        } else {
            int countPos = consignmentProductList.size(), costPos = consignmentProductList.size();
            for (int i = 0; i < consignmentProductList.size(); i++) {
                if (consignmentProductList.get(i).getCountValue() == 0)
                    countPos = i;
                if (consignmentProductList.get(i).getCostValue() == null)
                    costPos = i;
            }

            if (countPos != consignmentProductList.size()) {
                view.setError("Some counts are empty or equals 0");
            } else if (costPos != consignmentProductList.size())
                view.setError("Some costs are empty");
            else if (consignment == null) {
                if (databaseManager.isConsignmentNumberExists(number).blockingGet()) {
                    view.setConsignmentNumberError();
                    return;
                }
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
                operationDebt.setPaymentDate(System.currentTimeMillis());
                operationDebt.setOperationType(BillingOperations.DEBT_CONSIGNMENT);
                billingOperationsList.add(operationDebt);
                if (this.paidSum != 0) {
                    BillingOperations operationPaid = new BillingOperations();
                    operationPaid.setAmount(this.paidSum);
                    operationPaid.setCreateAt(System.currentTimeMillis());
                    operationPaid.setVendor(this.vendor);
                    operationPaid.setPaymentDate(System.currentTimeMillis());
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
                view.closeFragment(this.vendor);
            } else {
                if (!this.consignment.getConsignmentNumber().equals(number)) {
                    if (databaseManager.isConsignmentNumberExists(number).blockingGet()) {
                        view.setConsignmentNumberError();
                        return;
                    }
                }
                view.openSaveChangesDialog();
            }
        }

    }

    @Override
    public void deleteFromList(ConsignmentProduct consignmentProduct) {
        if (this.consignment != null) {
            deletedProductsList.add(consignmentProduct);
        }
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
            if (consignmentProduct.getCostValue() != null && consignmentProduct.getCountValue() != null)
                sum += consignmentProduct.getCostValue() * consignmentProduct.getCountValue();
        }
        view.setConsignmentSumValue(sum);

    }

    @Override
    public void checkChanges(String number, String description, String totalPaid, boolean checked, int selectedPosition) {

        if (viewType == ADD) {
            if (!number.equals("") || !description.equals("") || !totalPaid.equals("0") || !checked || selectedPosition != 0) {
                view.openDiscardDialog();
            } else {
                if (productId != null) {
                    if (consignmentProductList.size() > 1)
                        view.openDiscardDialog();
                    else view.closeFragment(this.vendor);
                } else {
                    if (consignmentProductList.size() > 0)
                        view.openDiscardDialog();
                    else view.closeFragment(this.vendor);
                }
            }
        } else {
            if (!number.equals(consignment.getConsignmentNumber()) || !description.equals(consignment.getDescription())) {
                view.openDiscardDialog();
            } else if (this.consignment.getFirstPayId() != null) {
                BillingOperations operation = databaseManager.getBillingOperationsById(this.consignment.getFirstPayId()).blockingGet();
                try {
                    double firstPay = decimalFormat.parse(totalPaid).doubleValue();
                    if (firstPay != operation.getAmount())
                        view.openDiscardDialog();
                    else {
                        int count = 0;
                        if (ids.size() != consignmentProductList.size()) {
                            view.openDiscardDialog();
                        } else {
                            for (int i = 0; i < consignmentProductList.size(); i++) {
                                if (ids.contains(consignmentProductList.get(i).getId())) {
                                    count++;
                                }
                            }
                            if (count != ids.size()) {
                                view.openDiscardDialog();
                            } else view.closeFragment(this.vendor);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                if (!totalPaid.equals("0")) {
                    view.openDiscardDialog();
                } else {
                    int count = 0;
                    if (ids.size() != consignmentProductList.size()) {
                        view.openDiscardDialog();
                    } else {
                        for (int i = 0; i < consignmentProductList.size(); i++) {
                            if (ids.contains(consignmentProductList.get(i).getId())) {
                                count++;
                            }
                        }
                        if (count != ids.size()) {
                            view.openDiscardDialog();
                        } else view.closeFragment(this.vendor);
                    }
                }
            }
        }
    }

    @Override
    public void saveChanges() {
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
        List<BillingOperations> billingOperationsList = new ArrayList<>();
        boolean firstPayConfirmed = false;
        for (int i = 0; i < billingOperations.size(); i++) {
            if (billingOperations.get(i).getOperationType() == DEBT_CONSIGNMENT) {
                BillingOperations debtOperation = new BillingOperations();
                debtOperation.setAmount(sum * -1);
                debtOperation.setCreateAt(System.currentTimeMillis());
                debtOperation.setVendor(this.vendor);
                debtOperation.setOperationType(BillingOperations.DEBT_CONSIGNMENT);
                debtOperation.setPaymentDate(billingOperations.get(i).getPaymentDate());
                if (billingOperations.get(i).getRootId() != null)
                    debtOperation.setRootId(billingOperations.get(i).getRootId());
                else debtOperation.setRootId(billingOperations.get(i).getId());
                billingOperationsList.add(debtOperation);
                billingOperations.get(i).setNotModifyted(false);
                databaseManager.insertBillingOperation(billingOperations.get(i)).blockingGet();

            } else if (this.paidSum != 0) {
                BillingOperations operationPaid = new BillingOperations();
                operationPaid.setAmount(this.paidSum);
                operationPaid.setCreateAt(System.currentTimeMillis());
                operationPaid.setVendor(this.vendor);
                operationPaid.setOperationType(PAID_TO_CONSIGNMENT);
                operationPaid.setPaymentDate(billingOperations.get(i).getPaymentDate());
                if (checked) {
                    operationPaid.setAccount(accountList.get(selectedPosition));
                }
                if (billingOperations.get(i).getRootId() != null)
                    operationPaid.setRootId(billingOperations.get(i).getRootId());
                else operationPaid.setRootId(billingOperations.get(i).getId());
                billingOperationsList.add(operationPaid);
                billingOperations.get(i).setNotModifyted(false);
                databaseManager.insertBillingOperation(billingOperations.get(i)).blockingGet();
                firstPayConfirmed = true;
            }
        }
        if (!firstPayConfirmed && (this.paidSum != 0)) {
            BillingOperations operationPaid = new BillingOperations();
            operationPaid.setAmount(this.paidSum);
            operationPaid.setCreateAt(System.currentTimeMillis());
            operationPaid.setVendor(this.vendor);
            operationPaid.setOperationType(PAID_TO_CONSIGNMENT);
            operationPaid.setPaymentDate(System.currentTimeMillis());
            if (checked) {
                operationPaid.setAccount(accountList.get(selectedPosition));
            }
            billingOperationsList.add(operationPaid);
        }
        if (deletedProductsList.size() != 0) {
            for (ConsignmentProduct consignmentProduct : deletedProductsList) {
                consignmentProduct.setDeleted(true);
                WarehouseOperations warehouseOperations = databaseManager.getWarehouseOperationById(consignmentProduct.getWarehouseId()).blockingGet();
                warehouseOperations.setDeleted(true);
                databaseManager.insertConsignmentProduct(consignmentProduct).blockingSingle();
                databaseManager.replaceWarehouseOperation(warehouseOperations).blockingGet();
            }
        }
        List<ConsignmentProduct> newConsignmentProductList = new ArrayList<>();
        List<WarehouseOperations> warehouseOperationsList = new ArrayList<>();
        for (int i = 0; i < consignmentProductList.size(); i++) {
            ConsignmentProduct product = consignmentProductList.get(i);
            if (ids.contains(consignmentProductList.get(i).getId())) {
                for (int j = 0; j < tempProductList.size(); j++) {
                    if (tempProductList.get(j).getId().equals(consignmentProductList.get(i).getId())) {
                        if (tempProductList.get(j).getCount() != (product.getCountValue())) {
                            ConsignmentProduct consignmentProduct = new ConsignmentProduct();
                            consignmentProduct.setCostValue(product.getCostValue());
                            consignmentProduct.setCountValue(product.getCountValue());
                            consignmentProduct.setProduct(product.getProduct());
                            consignmentProduct.setCreatedDate(System.currentTimeMillis());
                            if (product.getRootId() != null)
                                consignmentProduct.setRootId(product.getRootId());
                            else consignmentProduct.setRootId(product.getId());
                            product.setNotModifyted(false);
                            product.setCountValue(tempProductList.get(j).getCount());
                            product.setCostValue(tempProductList.get(j).getCost());
                            WarehouseOperations warehouseOperations = databaseManager.getWarehouseOperationById(product.getWarehouseId()).blockingGet();
                            warehouseOperations.setNotModifyted(false);
                            WarehouseOperations newOperation = new WarehouseOperations();
                            newOperation.setValue(consignmentProduct.getCountValue());
                            newOperation.setType(WarehouseOperations.INCOME_FROM_VENDOR);
                            newOperation.setCreateAt(System.currentTimeMillis());
                            newOperation.setVendor(this.vendor);
                            newOperation.setProduct(consignmentProduct.getProduct());
                            if (warehouseOperations.getRootId() != null) {
                                newOperation.setRootId(warehouseOperations.getRootId());
                            } else newOperation.setRootId(warehouseOperations.getId());
                            warehouseOperationsList.add(newOperation);
                            newConsignmentProductList.add(consignmentProduct);
                            databaseManager.insertConsignmentProduct(product).blockingSingle();
                            databaseManager.replaceWarehouseOperation(warehouseOperations).blockingGet();
                        } else if (tempProductList.get(j).getCost() != (product.getCostValue())) {
                            ConsignmentProduct consignmentProduct = new ConsignmentProduct();
                            consignmentProduct.setCostValue(product.getCostValue());
                            consignmentProduct.setCountValue(product.getCountValue());
                            consignmentProduct.setProduct(product.getProduct());
                            consignmentProduct.setCreatedDate(System.currentTimeMillis());
                            if (product.getRootId() != null)
                                consignmentProduct.setRootId(product.getRootId());
                            else consignmentProduct.setRootId(product.getId());
                            product.setNotModifyted(false);
                            databaseManager.insertConsignmentProduct(product).blockingSingle();
                            WarehouseOperations warehouseOperations = databaseManager.getWarehouseOperationById(product.getWarehouseId()).blockingGet();
                            consignmentProduct.setWarehouseId(warehouseOperations.getId());
                            consignmentProduct.setWarehouse(warehouseOperations);
                            newConsignmentProductList.add(consignmentProduct);
                            warehouseOperationsList.add(null);
                        } else {
                            newConsignmentProductList.add(product);
                            warehouseOperationsList.add(null);
                        }
                        break;
                    }
                }
            } else {
                newConsignmentProductList.add(product);
                WarehouseOperations newOperation = new WarehouseOperations();
                newOperation.setValue(product.getCountValue());
                newOperation.setType(WarehouseOperations.INCOME_FROM_VENDOR);
                newOperation.setCreateAt(System.currentTimeMillis());
                newOperation.setVendor(this.vendor);
                newOperation.setProduct(product.getProduct());
                warehouseOperationsList.add(newOperation);
            }
        }
        databaseManager.insertConsignment(consignment, null, null, null).blockingGet();
        databaseManager.insertConsignment(consignmentNew, billingOperationsList, newConsignmentProductList, warehouseOperationsList).subscribe();
        view.closeFragment(this.vendor);
    }

    @Override
    public void loadVendorProducts() {
        this.vendor.resetProducts();
        List<Product> productList = this.vendor.getProducts();
        List<Product> sortedProductList = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            if (product.getIsDeleted().equals(false) && product.getIsNotModified().equals(true) && product.getIsActive().equals(true))
                sortedProductList.add(product);
        }
        view.fillDialogItems(sortedProductList);
    }
}
