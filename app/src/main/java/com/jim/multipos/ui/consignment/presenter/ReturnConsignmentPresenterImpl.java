package com.jim.multipos.ui.consignment.presenter;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.products.VendorProductCon;
import com.jim.multipos.data.db.model.unit.Unit;
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
    private double sum = 0;

    @Inject
    protected ReturnConsignmentPresenterImpl(ReturnConsignmentView view, DatabaseManager databaseManager) {
        super(view);
        this.databaseManager = databaseManager;
        consignmentProductList = new ArrayList<>();
        returnConsignment = null;
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
        if (consignmentProductList.isEmpty()) {
            view.setError();
        } else {
            this.returnConsignment = new Consignment();
            this.returnConsignment.setConsignmentNumber(number);
            this.returnConsignment.setCreatedDate(System.currentTimeMillis());
            this.returnConsignment.setDescription(description);
            this.returnConsignment.setTotalAmount(sum);
            this.returnConsignment.setVendor(this.vendor);
            this.returnConsignment.setConsignmentType(1);
            databaseManager.insertConsignment(this.returnConsignment).subscribe(aLong -> {
                for (ConsignmentProduct consignmentProduct : consignmentProductList) {
                    consignmentProduct.setConsignmentId(this.returnConsignment.getId());
                    databaseManager.insertConsignmentProduct(consignmentProduct).subscribe();
                }
            });
        }
    }
}
