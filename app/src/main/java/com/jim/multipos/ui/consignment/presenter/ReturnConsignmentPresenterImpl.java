package com.jim.multipos.ui.consignment.presenter;

import android.os.Build;
import android.support.annotation.RequiresApi;

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

import static com.jim.multipos.data.db.model.inventory.BillingOperations.DEBT_CONSIGNMENT;

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
            view.fillReturnList(consignmentProductList);
            calculateConsignmentSum();
            this.vendor = this.returnConsignment.getVendor();
            view.setVendorName(this.vendor.getName());
            debt = databaseManager.getBillingOperationsByConsignmentId(this.returnConsignment.getId()).blockingGet();
            view.fillConsignmentData(returnConsignment.getConsignmentNumber(), returnConsignment.getDescription());
        } else if (productId != null) {
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
        } else {
            databaseManager.getVendorById(vendorId).subscribe(vendor -> {
                this.vendor = vendor;
                view.setVendorName(this.vendor.getName());
            });
        }
    }

    @Override
    public void setReturnItem(Product product) {
        ConsignmentProduct consignmentProduct = new ConsignmentProduct();
        consignmentProduct.setProduct(product);
        consignmentProduct.setProductId(product.getId());
        VendorProductCon productCon = databaseManager.getVendorProductConnectionById(product.getId(), vendor.getId()).blockingSingle();
        if (productCon != null) {
            consignmentProduct.setCostValue(productCon.getCost());
        } else consignmentProduct.setCostValue(null);
        consignmentProduct.setCountValue(0d);
        consignmentProductList.add(consignmentProduct);
        view.fillReturnList(consignmentProductList);
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
            if (consignmentProduct.getCostValue() != null)
                sum += consignmentProduct.getCostValue() * consignmentProduct.getCountValue();
        }
        view.setTotalProductsSum(sum);
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

    @Override
    public void saveReturnConsignment(String number, String description) {
        this.number = number;
        this.description = description;
        if (consignmentProductList.isEmpty()) {
            view.setError();
        } else {
            if (this.returnConsignment == null) {
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
                view.closeFragment(this.vendor.getId());
            } else {
                view.openSaveChangesDialog();
            }
        }
    }

    @Override
    public void checkChanges(String number, String des) {
        int count = 0;
        if (!number.equals("") || !des.equals("")) {
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
                } else view.closeFragment(this.vendor.getId());
            }
        } else view.closeFragment(this.vendor.getId());
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
        debtOperation.setAmount(sum * -1);
        debtOperation.setCreateAt(System.currentTimeMillis());
        debtOperation.setVendor(this.vendor);
        debtOperation.setPaymentDate(debt.getPaymentDate());
        debtOperation.setOperationType(BillingOperations.DEBT_CONSIGNMENT);
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
        view.closeFragment(this.vendor.getId());
    }
}
