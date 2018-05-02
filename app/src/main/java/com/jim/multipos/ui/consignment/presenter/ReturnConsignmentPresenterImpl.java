package com.jim.multipos.ui.consignment.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.inventory.WarehouseOperations;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.products.VendorProductCon;
import com.jim.multipos.ui.consignment.model.TempProduct;
import com.jim.multipos.ui.consignment.view.ReturnConsignmentView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 24.11.2017.
 */

public class ReturnConsignmentPresenterImpl extends BasePresenterImpl<ReturnConsignmentView> implements ReturnConsignmentPresenter {

    private Product product;
    private Vendor vendor;
    private Consignment returnConsignment;
    private List<ConsignmentProduct> consignmentProductList;
    private DatabaseManager databaseManager;
    private List<Long> ids;
    private List<TempProduct> tempProductList;
    private double sum = 0;
    private BillingOperations debt;
    private List<ConsignmentProduct> deletedProductsList;
    private String number;
    private String description;
    public static final int ADD = 0;
    public static final int EDIT = 1;
    private int viewType = ADD;
    private Long productId;

    @Inject
    protected ReturnConsignmentPresenterImpl(ReturnConsignmentView view, DatabaseManager databaseManager) {
        super(view);
        this.databaseManager = databaseManager;
        consignmentProductList = new ArrayList<>();
        returnConsignment = null;
        tempProductList = new ArrayList<>();
        deletedProductsList = new ArrayList<>();
        ids = new ArrayList<>();
    }

    @Override
    public void setData(Long productId, Long vendorId, Long consignmentId) {
        this.productId = productId;
        if (consignmentId != null) {
            this.returnConsignment = databaseManager.getConsignmentById(consignmentId).blockingGet();
            consignmentProductList = this.returnConsignment.getAllConsignmentProducts();
            for (int i = 0; i < consignmentProductList.size(); i++) {
                TempProduct tempProduct = new TempProduct();
                tempProduct.setId(consignmentProductList.get(i).getId());
                tempProduct.setCount(consignmentProductList.get(i).getCountValue());
                tempProduct.setCost(consignmentProductList.get(i).getCostValue());
                tempProductList.add(tempProduct);
                ids.add(consignmentProductList.get(i).getId());
            }
            viewType = EDIT;
            view.fillReturnList(consignmentProductList, viewType);
            calculateConsignmentSum();
            this.vendor = this.returnConsignment.getVendor();
            view.setVendorName(this.vendor.getName());
            debt = databaseManager.getBillingOperationsByConsignmentId(this.returnConsignment.getId()).blockingGet();
            view.fillConsignmentData(returnConsignment.getConsignmentNumber(), returnConsignment.getDescription());
        } else if (productId != null) {
            viewType = ADD;
            databaseManager.getProductById(productId).subscribe(product -> {
                this.product = product;
                List<Vendor> vendorList = this.product.getVendor();
                for (Vendor vendor : vendorList) {
                    if (vendor.getId().equals(vendorId))
                        this.vendor = vendor;
                }
                setReturnItem(product);
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
    public void setReturnItem(Product product) {
        ConsignmentProduct consignmentProduct = new ConsignmentProduct();
        consignmentProduct.setProduct(product);
        consignmentProduct.setCreatedDate(System.currentTimeMillis());
        consignmentProduct.setProductId(product.getId());
        VendorProductCon productCon = databaseManager.getVendorProductConnectionById(product.getId(), vendor.getId()).blockingSingle();
        if (productCon != null) {
            consignmentProduct.setCostValue(productCon.getCost());
        } else consignmentProduct.setCostValue(null);
        consignmentProduct.setCountValue(0d);
        consignmentProductList.add(consignmentProduct);
        view.fillReturnList(consignmentProductList, viewType);
        calculateConsignmentSum();
    }

    @Override
    public void deleteFromList(ConsignmentProduct consignmentProduct) {
        if (this.returnConsignment != null) {
            deletedProductsList.add(consignmentProduct);
        }
        consignmentProductList.remove(consignmentProduct);
    }

    @Override
    public void calculateConsignmentSum() {
        sum = 0;
        for (ConsignmentProduct consignmentProduct : consignmentProductList) {
            if (consignmentProduct.getCostValue() != null && consignmentProduct.getCountValue() != null)
                sum += consignmentProduct.getCostValue() * consignmentProduct.getCountValue();
        }
        view.setTotalProductsSum(sum);
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

    @Override
    public void saveReturnConsignment(String number, String description) {
        this.number = number;
        this.description = description;
        if (consignmentProductList.isEmpty()) {
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
            else if (this.returnConsignment == null) {
                if (databaseManager.isConsignmentNumberExists(number).blockingGet()) {
                    view.setConsignmentNumberError();
                    return;
                }
                this.returnConsignment = new Consignment();
                this.returnConsignment.setConsignmentNumber(number);
                this.returnConsignment.setCreatedDate(System.currentTimeMillis());
                this.returnConsignment.setDescription(description);
                this.returnConsignment.setTotalAmount(sum);
                this.returnConsignment.setVendor(this.vendor);
                this.returnConsignment.setConsignmentType(Consignment.RETURN_CONSIGNMENT);
                List<BillingOperations> billingOperationsList = new ArrayList<>();
                BillingOperations operationDebt = new BillingOperations();
                operationDebt.setAmount(sum);
                operationDebt.setCreateAt(System.currentTimeMillis());
                operationDebt.setVendor(this.vendor);
                operationDebt.setNotModifyted(true);
                operationDebt.setDeleted(false);
                operationDebt.setPaymentDate(System.currentTimeMillis());
                operationDebt.setOperationType(BillingOperations.RETURN_TO_VENDOR);
                billingOperationsList.add(operationDebt);
                List<WarehouseOperations> warehouseOperationsList = new ArrayList<>();
                for (ConsignmentProduct consignmentProduct : consignmentProductList) {
                    WarehouseOperations warehouseOperations = new WarehouseOperations();
                    warehouseOperations.setProduct(consignmentProduct.getProduct());
                    warehouseOperations.setVendor(returnConsignment.getVendor());
                    warehouseOperations.setCreateAt(System.currentTimeMillis());
                    warehouseOperations.setType(WarehouseOperations.RETURN_TO_VENDOR);
                    warehouseOperations.setValue(consignmentProduct.getCountValue() * -1);
                    warehouseOperationsList.add(warehouseOperations);
                }
                databaseManager.insertConsignment(this.returnConsignment, billingOperationsList, consignmentProductList, warehouseOperationsList).subscribe();
                view.closeFragment(this.vendor);
            } else {
                if (!this.returnConsignment.getConsignmentNumber().equals(number)) {
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
    public void checkChanges(String number, String des) {
        if (viewType == ADD) {
            if (!des.equals("")) {
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
            int count = 0;
            if (!number.equals(this.returnConsignment.getConsignmentNumber()) || !des.equals(this.returnConsignment.getDescription())) {
                view.openDiscardDialog();
            } else {
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

    @Override
    public void saveChanges() {
        Consignment consignmentNew = new Consignment();
        consignmentNew.setConsignmentNumber(number);
        consignmentNew.setCreatedDate(System.currentTimeMillis());
        consignmentNew.setDescription(description);
        consignmentNew.setTotalAmount(sum);
        consignmentNew.setVendor(this.vendor);
        consignmentNew.setConsignmentType(Consignment.RETURN_CONSIGNMENT);
        if (this.returnConsignment.getRootId() == null)
            consignmentNew.setRootId(this.returnConsignment.getId());
        else consignmentNew.setRootId(this.returnConsignment.getRootId());
        this.returnConsignment.setIsNotModified(false);
        List<BillingOperations> billingOperationsList = new ArrayList<>();
        BillingOperations debtOperation = new BillingOperations();
        debtOperation.setAmount(sum);
        debtOperation.setCreateAt(System.currentTimeMillis());
        debtOperation.setVendor(this.vendor);
        debtOperation.setPaymentDate(debt.getPaymentDate());
        debtOperation.setOperationType(BillingOperations.RETURN_TO_VENDOR);
        if (debt.getRootId() != null)
            debtOperation.setRootId(debt.getRootId());
        else debtOperation.setRootId(debt.getId());
        billingOperationsList.add(debtOperation);
        debt.setNotModifyted(false);
        databaseManager.insertBillingOperation(debt).blockingGet();
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
                            warehouseOperations.setNotModifyted(true);
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
        databaseManager.insertConsignment(this.returnConsignment, null, null, null).blockingGet();
        databaseManager.insertConsignment(consignmentNew, billingOperationsList, newConsignmentProductList, warehouseOperationsList).subscribe();
        view.closeFragment(this.vendor);
    }

    @Override
    public void onBarcodeScaned(String barcode) {
        this.vendor.resetProducts();
        List<Product> productList = this.vendor.getProducts();
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            if (product.getIsDeleted().equals(false) && product.getIsNotModified().equals(true) && product.getIsActive().equals(true) && product.getBarcode()!=null && product.getBarcode().equals(barcode))
                setReturnItem(product);
        }
    }
}
