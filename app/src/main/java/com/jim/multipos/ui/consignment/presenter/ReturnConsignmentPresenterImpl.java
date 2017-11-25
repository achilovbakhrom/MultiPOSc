package com.jim.multipos.ui.consignment.presenter;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.consignment.ReturnConsignment;
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
    private ReturnConsignment returnConsignment;
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
    public void setData() {
        //        databaseManager.getProductById(id).subscribe(product -> {
//            this.product = product;
//            if (product != null)
//                setConsignmentItem(product);
//        });


        Currency currency = new Currency();
        currency.setAbbr("uzs");
        currency.setName("Sum");
        currency.setMain(true);
        databaseManager.addCurrency(currency).blockingSingle();
        Unit unit = new Unit();
        unit.setAbbr("gr");
        unit.setFactorRoot(1);
        databaseManager.addUnit(unit).blockingSingle();
        Product product = new Product();
        product.setName("Tooth paste");
        product.setCostCurrency(currency);
        product.setCreatedDate(System.currentTimeMillis());
        product.setMainUnit(unit);
        databaseManager.addProduct(product).blockingSingle();

        this.product = product;

        Vendor vendor = new Vendor();
        vendor.setName("Huawei Design");
        vendor.setContactName("Islomov Sardor");
        databaseManager.addVendor(vendor).blockingSingle();
        this.vendor = vendor;

        VendorProductCon vendorProductCon = new VendorProductCon();
        vendorProductCon.setProductId(product.getId());
        vendorProductCon.setVendorId(vendor.getId());
        databaseManager.addVendorProductConnection(vendorProductCon).subscribe();

        Product product1 = new Product();
        product1.setName("Others paste");
        product1.setCostCurrency(currency);
        product1.setCreatedDate(System.currentTimeMillis());
        product1.setMainUnit(unit);
        databaseManager.addProduct(product1).blockingSingle();

        Product product2 = new Product();
        product2.setName("Mini paste");
        product2.setCostCurrency(currency);
        product2.setCreatedDate(System.currentTimeMillis());
        product2.setMainUnit(unit);
        databaseManager.addProduct(product2).blockingSingle();

        VendorProductCon productCon = new VendorProductCon();
        productCon.setProductId(product1.getId());
        productCon.setVendorId(vendor.getId());
        databaseManager.addVendorProductConnection(productCon).subscribe();

        VendorProductCon con = new VendorProductCon();
        con.setProductId(product2.getId());
        con.setVendorId(vendor.getId());
        databaseManager.addVendorProductConnection(con).subscribe();
        view.setVendorName(vendor.getName());
        setReturnItem(product);
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
            this.returnConsignment = new ReturnConsignment();
            this.returnConsignment.setReturnNumber(number);
            this.returnConsignment.setCreatedDate(System.currentTimeMillis());
            this.returnConsignment.setDescription(description);
            this.returnConsignment.setTotalReturnAmount(sum);
            this.returnConsignment.setVendor(this.vendor);
            this.returnConsignment.setCurrency(this.product.getCostCurrency());
            databaseManager.insertReturnConsignment(this.returnConsignment).subscribe(aLong -> {
                for (ConsignmentProduct consignmentProduct : consignmentProductList) {
                    consignmentProduct.setConsignmentId(this.returnConsignment.getId());
                    databaseManager.insertConsignmentProduct(consignmentProduct).subscribe();
                }
            });
        }
    }
}
