package com.jim.multipos.ui.vendor_products_view;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.inventory.InventoryState;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Portable-Acer on 17.11.2017.
 */

public class VendorProductsViewPresenterImpl extends BasePresenterImpl<VendorProductsView> implements VendorProductsViewPresenter {
    private DatabaseManager databaseManager;
    private long vendorId;
    private List<InventoryState> inventoryStates;

    @Inject
    public VendorProductsViewPresenterImpl(VendorProductsView vendorProductsView, DatabaseManager databaseManager) {
        super(vendorProductsView);
        this.databaseManager = databaseManager;
    }

    @Override
    public void setVendorId(long vendorId) {
        this.vendorId = vendorId;
    }

    @Override
    public Vendor getVendor() {
        return databaseManager.getVendorById(vendorId).blockingSingle();
    }

    @Override
    public List<Product> getProducts() {
        return databaseManager.getVendors().blockingSingle().get(0).getProducts();
    }

    @Override
    public List<InventoryState> getInventoryStates() {
        /*if (databaseManager.getInventoryStatesByVendorId(vendorId).blockingSingle().isEmpty()) {
            Unit unit = new Unit();
            unit.setAbbr("kg");
            unit.setFactorRoot(1);
            unit.setIsActive(true);
            databaseManager.getUnitOperations().addUnit(unit).blockingSingle();

            Unit unit1 = new Unit();
            unit1.setAbbr("pcs");
            unit1.setFactorRoot(1);
            unit1.setIsActive(true);
            databaseManager.getUnitOperations().addUnit(unit1).blockingSingle();

            Currency currency = new Currency();
            currency.setAbbr("uzs");
            currency.setName("Sum");
            currency.setIsMain(true);
            databaseManager.getCurrencyOperations().addCurrency(currency).blockingSingle();

            ProductClass productClass = new ProductClass();
            productClass.setName("T-shirt");
            productClass.setCreatedDate(System.currentTimeMillis());
            productClass.setActive(true);
            productClass.setDeleted(false);
            databaseManager.insertProductClass(productClass).blockingGet();

            Category category = new Category();
            category.setName("MoneyIsGood");
            databaseManager.addCategory(category).blockingSingle();

            Category subcategory = new Category();
            subcategory.setParentId(category.getId());
            subcategory.setName("GirlsAreGood");
            databaseManager.getCategoryOperations().addCategory(subcategory).blockingSingle();

            Product product = new Product();
            product.setName("Taxima");
            product.setBarcode("88974");
            product.setSku("12312421");
            product.setPrice(6500d);
            product.setCreatedDate(System.currentTimeMillis());
            product.setMainUnitId(unit.getId());
            product.setCostCurrencyId(currency.getId());
            product.setPriceCurrencyId(currency.getId());
            product.setClassId(productClass.getId());
            product.setCategoryId(subcategory.getId());
            databaseManager.getProductOperations().addProduct(product).blockingSingle();

            Product product1 = new Product();
            product1.setName("To'xtaniyoz ota kapchonniy");
            product1.setBarcode("sadq");
            product1.setSku("qweqs");
            product1.setPrice(5000d);
            product1.setCreatedDate(System.currentTimeMillis());
            product1.setMainUnitId(unit1.getId());
            product1.setCostCurrencyId(currency.getId());
            product1.setClassId(productClass.getId());
            product1.setPriceCurrencyId(currency.getId());
            product1.setCategoryId(subcategory.getId());
            databaseManager.getProductOperations().addProduct(product1).blockingSingle();

            Product product5 = new Product();
            product5.setName("Bono Chocolate");
            product5.setBarcode("3414124");
            product5.setSku("8732344");
            product5.setPrice(6500d);
            product5.setCreatedDate(System.currentTimeMillis());
            product5.setMainUnitId(unit1.getId());
            product5.setCostCurrencyId(currency.getId());
            product5.setPriceCurrencyId(currency.getId());
            product5.setClassId(productClass.getId());
            product5.setCategoryId(subcategory.getId());
            databaseManager.getProductOperations().addProduct(product5).blockingSingle();

            Product product6 = new Product();
            product6.setName("Sugar 1000gr");
            product6.setBarcode("8875154");
            product6.setSku("px1412");
            product6.setPrice(6500d);
            product6.setCreatedDate(System.currentTimeMillis());
            product6.setMainUnitId(unit1.getId());
            product6.setCostCurrencyId(currency.getId());
            product6.setPriceCurrencyId(currency.getId());
            product6.setClassId(productClass.getId());
            product6.setCategoryId(subcategory.getId());
            databaseManager.getProductOperations().addProduct(product6).blockingSingle();

            InventoryState inventoryState1 = new InventoryState();
            inventoryState1.setVendorId(vendorId);
            inventoryState1.setProductId(product.getId());
            inventoryState1.setValue(12);
            databaseManager.insertInventoryState(inventoryState1).blockingSingle();


            InventoryState inventoryState2 = new InventoryState();
            inventoryState2.setVendorId(vendorId);
            inventoryState2.setProductId(product1.getId());
            inventoryState2.setValue(24);
            databaseManager.insertInventoryState(inventoryState2).blockingSingle();

            InventoryState inventoryState3 = new InventoryState();
            inventoryState3.setVendorId(vendorId);
            inventoryState3.setProductId(product5.getId());
            inventoryState3.setValue(40);
            databaseManager.insertInventoryState(inventoryState3).blockingSingle();

            InventoryState inventoryState4 = new InventoryState();
            inventoryState4.setVendorId(vendorId);
            inventoryState4.setProductId(product6.getId());
            inventoryState4.setValue(102);
            databaseManager.insertInventoryState(inventoryState4).blockingSingle();
        }*/

        inventoryStates = databaseManager.getInventoryStatesByVendorId(vendorId).blockingSingle();

        sortByProductAsc();

        return inventoryStates;
    }

    @Override
    public InventoryState getInventoryState(int position) {
        return inventoryStates.get(position);
    }

    @Override
    public void sortByProductAsc() {
        Collections.sort(inventoryStates, (o1, o2) -> o1.getProduct().getName().compareTo(o2.getProduct().getName()));
    }

    @Override
    public void sortByProductDesc() {
        Collections.sort(inventoryStates, (o1, o2) -> o2.getProduct().getName().compareTo(o1.getProduct().getName()));
    }

    @Override
    public void sortByInventoryAsc() {
        Collections.sort(inventoryStates, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
    }

    @Override
    public void sortByInventoryDesc() {
        Collections.sort(inventoryStates, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
    }

    @Override
    public void sortByUnitAsc() {
        Collections.sort(inventoryStates, (o1, o2) -> o1.getProduct().getMainUnit().getAbbr().compareTo(o2.getProduct().getMainUnit().getAbbr()));
    }

    @Override
    public void sortByUnitDesc() {
        Collections.sort(inventoryStates, (o1, o2) -> o2.getProduct().getMainUnit().getAbbr().compareTo(o1.getProduct().getMainUnit().getAbbr()));
    }

    @Override
    public void sortByCreatedDate() {
        Collections.sort(inventoryStates, (o1, o2) -> o2.getProduct().getCreatedDate().compareTo(o1.getProduct().getCreatedDate()));
    }

    @Override
    public ProductClass getProductClassById(Long id) {
        return databaseManager.getProductClass(id).blockingSingle();
    }
}
