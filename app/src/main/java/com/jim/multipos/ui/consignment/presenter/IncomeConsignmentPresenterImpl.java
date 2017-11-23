package com.jim.multipos.ui.consignment.presenter;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.data.db.model.consignment.ConsignmentProduct;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.products.VendorProductCon;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.ui.consignment.view.IncomeConsignmentView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public class IncomeConsignmentPresenterImpl extends BasePresenterImpl<IncomeConsignmentView> implements IncomeConsignmentPresenter {

    private Product product;
    private Vendor vendor;
    private Consignment consignment;
    private List<ConsignmentProduct> consignmentProductList;
    private DatabaseManager databaseManager;
    private List<Account> accountList;
    private double sum = 0;

    @Inject
    protected IncomeConsignmentPresenterImpl(IncomeConsignmentView incomeConsignmentView, DatabaseManager databaseManager) {
        super(incomeConsignmentView);
        this.databaseManager = databaseManager;
        consignmentProductList = new ArrayList<>();
        accountList = new ArrayList<>();
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
        List<Account> accountList = new ArrayList<>();
        Account account = new Account();
        account.setName("Cash");
        account.setType(1);
        account.setCirculation(0);
        databaseManager.addAccount(account).blockingSingle();
        accountList.add(account);
        Account account1 = new Account();
        account1.setName("Bank");
        account1.setType(0);
        account1.setCirculation(1);
        databaseManager.addAccount(account1).blockingSingle();
        accountList.add(account1);
        this.accountList.add(account);
        this.accountList.add(account1);
        List<String> strings = new ArrayList<>();
        for (Account ac : accountList) {
            strings.add(ac.getName());
        }
        view.fillAccountsList(strings);

        Product product = new Product();
        product.setName("Tooth paste");
        product.setCostCurrency(currency);
        product.setCreatedDate(System.currentTimeMillis());
        product.setMainUnit(unit);
        databaseManager.addProduct(product).blockingSingle();

        this.product = product;
        setConsignmentItem(product);

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
//        getAccounts();
    }

    @Override
    public void setConsignmentItem(Product product) {
        ConsignmentProduct consignmentProduct = new ConsignmentProduct();
        consignmentProduct.setProduct(product);
        consignmentProduct.setProductId(product.getId());
        VendorProductCon productCon = databaseManager.getVendorProductConnectionById(product.getId(), vendor.getId()).blockingSingle();
        if (productCon != null) {
            consignmentProduct.setCostValue(productCon.getCost());
        } else consignmentProduct.setCostValue(null);
        consignmentProduct.setCountValue(0d);
        consignmentProductList.add(consignmentProduct);
        view.fillConsignmentProductList(consignmentProductList);
        calculateConsignmentSum();
    }

    @Override
    public void saveConsignment(String number, String description, String totalAmount, boolean checked, int selectedPosition) {
        if (consignmentProductList.isEmpty()) {
            view.setError();
        } else {
            consignment = new Consignment();
            consignment.setConsignmentNumber(number);
            consignment.setCreatedDate(System.currentTimeMillis());
            consignment.setDescription(description);
            consignment.setTotalAmount(sum);
            consignment.setIsFromAccount(checked);
            consignment.setCurrency(product.getCostCurrency());
            if (checked)
                consignment.setAccount(accountList.get(selectedPosition));
            databaseManager.insertConsignment(consignment).subscribe(aLong -> {
                for (ConsignmentProduct consignmentProduct : consignmentProductList) {
                    consignmentProduct.setConsignmentId(consignment.getId());
                    databaseManager.insertConsignmentProduct(consignmentProduct).subscribe();
                }
            });
        }
    }

    @Override
    public void deleteFromList(ConsignmentProduct consignmentProduct) {
        consignmentProductList.remove(consignmentProduct);
    }

    @Override
    public void getAccounts() {
        databaseManager.getAllAccounts().subscribe(accounts -> {
            List<String> strings = new ArrayList<>();
            if (!accounts.isEmpty())
                for (Account account : accounts) {
                    this.accountList = accounts;
                    strings.add(account.getName());
                }
            view.fillAccountsList(strings);
        });
    }

    @Override
    public void calculateConsignmentSum() {
        sum = 0;
        for (ConsignmentProduct consignmentProduct : consignmentProductList) {
            sum += consignmentProduct.getCostValue() * consignmentProduct.getCountValue();
        }
        view.setConsignmentSumValue(sum);

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
