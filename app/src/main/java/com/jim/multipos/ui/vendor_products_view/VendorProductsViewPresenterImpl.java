package com.jim.multipos.ui.vendor_products_view;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.products.VendorProductCon;
import com.jim.multipos.ui.inventory.model.InventoryItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Portable-Acer on 17.11.2017.
 */

public class VendorProductsViewPresenterImpl extends BasePresenterImpl<VendorProductsView> implements VendorProductsViewPresenter {
    private DatabaseManager databaseManager;
    private long vendorId;
    private Vendor vendor;
    private List<InventoryItem> inventoryItems;

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
        //TODO
        if (databaseManager.getVendors().blockingSingle().isEmpty()) {
            Vendor vendor = new Vendor();
            vendor.setActive(true);
            vendor.setName("Abdulla Production");
            vendor.setContactName("Abdulla");
            vendor.setAddress("Texno plaza");
            vendor.setCreatedDate(System.currentTimeMillis());

            databaseManager.addVendor(vendor).blockingSingle();

            Contact contact1 = new Contact();
            contact1.setType(Contact.PHONE);
            contact1.setName("+998007");
            contact1.setVendorId(vendor.getId());

            Contact contact2 = new Contact();
            contact2.setType(Contact.PHONE);
            contact2.setName("+998008");
            contact2.setVendorId(vendor.getId());

            Contact contact3 = new Contact();
            contact3.setType(Contact.E_MAIL);
            contact3.setName("mymail@gmail.com");
            contact3.setVendorId(vendor.getId());

            databaseManager.addContact(contact1).blockingSingle();
            databaseManager.addContact(contact2).blockingSingle();
            databaseManager.addContact(contact3).blockingSingle();

            /*ProductClass productClass = new ProductClass();
            productClass.setName("Pizza");
            databaseManager.insertProductClass(productClass).blockingGet();
            Product product = new Product();
            product.setActive(true);
            product.setName("Angles Food");
            product.setBarcode("12345679");
            product.setSku("811589");
            product.setClassId(productClass.getId());

            ProductClass productClass2 = new ProductClass();
            productClass2.setName("T-shirts");
            databaseManager.insertProductClass(productClass2).blockingGet();
            Product product2 = new Product();
            product2.setActive(true);
            product2.setName("POLO Red 32");
            product2.setBarcode("456879");
            product2.setSku("14952");
            product2.setClassId(productClass2.getId());

            databaseManager.addProduct(product).blockingSingle();
            databaseManager.addProduct(product2).blockingSingle();
            databaseManager.insertProductClass(productClass).blockingGet();
            databaseManager.insertProductClass(productClass2).blockingGet();*/

            /*VendorProductCon vendorProductCon1 = new VendorProductCon();
            vendorProductCon1.setProductId(product.getId());
            vendorProductCon1.setVendorId(vendor.getId());

            VendorProductCon vendorProductCon2 = new VendorProductCon();
            vendorProductCon2.setProductId(product2.getId());
            vendorProductCon2.setVendorId(vendor.getId());

            databaseManager.addVendorProductConnection(vendorProductCon1).blockingSingle();
            databaseManager.addVendorProductConnection(vendorProductCon2).blockingSingle();*/
        }

        vendor = databaseManager.getVendors().blockingSingle().get(0);

        return vendor;
    }

    @Override
    public List<Product> getProducts() {
        return databaseManager.getVendors().blockingSingle().get(0).getProducts();
    }

    @Override
    public List<InventoryItem> getInventoryItems() {
        inventoryItems = databaseManager.getInventoryItems().blockingGet();
        sortByProductAsc();

        return inventoryItems;
    }

    @Override
    public InventoryItem getInventoryItem(int position) {
        return inventoryItems.get(position);
    }

    @Override
    public void sortByProductAsc() {
        Collections.sort(inventoryItems, (o1, o2) -> o1.getProduct().getName().compareTo(o2.getProduct().getName()));
    }

    @Override
    public void sortByProductDesc() {
        Collections.sort(inventoryItems, (o1, o2) -> o2.getProduct().getName().compareTo(o1.getProduct().getName()));
    }

    @Override
    public void sortByInventoryAsc() {
        Collections.sort(inventoryItems, (o1, o2) -> o1.getInventory().compareTo(o2.getInventory()));
    }

    @Override
    public void sortByInventoryDesc() {
        Collections.sort(inventoryItems, (o1, o2) -> o2.getInventory().compareTo(o1.getInventory()));
    }

    @Override
    public void sortByUnitAsc() {
        Collections.sort(inventoryItems, (o1, o2) -> o1.getProduct().getMainUnit().getAbbr().compareTo(o2.getProduct().getMainUnit().getAbbr()));
    }

    @Override
    public void sortByUnitDesc() {
        Collections.sort(inventoryItems, (o1, o2) -> o2.getProduct().getMainUnit().getAbbr().compareTo(o1.getProduct().getMainUnit().getAbbr()));
    }

    @Override
    public void sortByCreatedDate() {
        Collections.sort(inventoryItems, (o1, o2) -> o2.getProduct().getCreatedDate().compareTo(o1.getProduct().getCreatedDate()));
    }
}
