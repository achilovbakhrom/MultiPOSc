package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.inventory.model.InventoryItem;
import com.jim.multipos.ui.mainpospage.view.ProductInfoView;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import static com.jim.multipos.data.db.model.ServiceFee.APP_TYPE_ITEM;
import static com.jim.multipos.data.db.model.ServiceFee.TYPE_REPRICE;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

public class ProductInfoPresenterImpl extends BasePresenterImpl<ProductInfoView> implements ProductInfoPresenter {
    private DatabaseManager databaseManager;
    private Product product;
    private InventoryItem inventoryItem;
    private int currentVendorPosition = 0;
    private double productQuantity = 20;
    private int currentProductQuantity = 1;
    private String[] discountAmountTypes;
    private String[] discountUsedTypesAbr;

    @Inject
    public ProductInfoPresenterImpl(ProductInfoView productInfoView, DatabaseManager databaseManager, @Named(value = "discount_amount_types") String[] discountAmountTypes,
                                    @Named(value = "discount_used_types_abr") String[] discountUsedTypesAbr) {
        super(productInfoView);
        this.databaseManager = databaseManager;
        this.discountAmountTypes = discountAmountTypes;
        this.discountUsedTypesAbr = discountUsedTypesAbr;
    }

    @Override
    public void initProduct(Long productId) {
        this.product = databaseManager.getProductById(productId).blockingSingle();
        databaseManager.getInventoryItems().subscribe((inventoryItems, throwable) -> {
            for(InventoryItem inventoryItem: inventoryItems){
                if (inventoryItem.getProduct().getId().equals(productId)){
                    this.inventoryItem = inventoryItem;
                    productQuantity = inventoryItem.getInventory();
                }
            }
        });
        view.initProductData(product, productQuantity);
    }

    @Override
    public List<Vendor> getVendors() {
        return product.getVendor();
    }

    @Override
    public Vendor getVendor(int position) {
        return product.getVendor().get(position);
    }

    @Override
    public Vendor getPrevVendor() {
        currentVendorPosition--;

        if (currentVendorPosition < 0) {
            currentVendorPosition = product.getVendor().size() - 1;
        }

        return product.getVendor().get(currentVendorPosition);
    }

    @Override
    public Vendor getNextVendor() {
        currentVendorPosition++;

        if (currentVendorPosition > product.getVendor().size() - 1) {
            currentVendorPosition = 0;
        }

        return product.getVendor().get(currentVendorPosition);
    }

    @Override
    public Vendor getRandomVendor() {
        int temp;
        Random random = new Random();

        do {
            temp = random.nextInt(product.getVendor().size());
        } while (temp == currentVendorPosition);

        currentVendorPosition = temp;

        return product.getVendor().get(currentVendorPosition);
    }

    @Override
    public Double getProductQuantity() {
        double result = productQuantity - currentProductQuantity;

        if (result >= 0) {
            view.changeQuantityColor(R.color.colorBlue);
            view.hideAlert();
        } else {
            view.changeQuantityColor(R.color.colorDarkRed);
            view.showAlert();
        }

        return result;
    }

    @Override
    public int getCurrentProductQuantity() {
        return currentProductQuantity;
    }

    @Override
    public int incrementQuantity() {
        return ++currentProductQuantity;
    }

    @Override
    public int decrementQuantity() {
        currentProductQuantity--;

        if (currentProductQuantity < 1) {
            currentProductQuantity = 1;
        }

        return currentProductQuantity;
    }

    @Override
    public void setCurrentQuantity(int quantity) {
        currentProductQuantity = quantity;
    }

    @Override
    public List<ServiceFee> getServiceFees() {
        return databaseManager.getServiceFeeOperations().getServiceFeesWithAllItemTypes().blockingSingle();
    }

    @Override
    public List<Discount> getDiscount(String[] discountType) {
        List<Discount> discounts = databaseManager.getDiscountsWithAllItemTypes(discountType).blockingSingle();

        return discounts;
    }

    @Override
    public void addDiscount(double amount, String description, String amountType) {
        Discount discount = new Discount();
        discount.setAmount(amount);
        discount.setDiscription(description);
        discount.setAmountType(amountType);
        discount.setUsedType(discountUsedTypesAbr[0]);
        discount.setCreatedDate(System.currentTimeMillis());
        discount.setDeleted(false);
        discount.setNotModifyted(true);

        if (amountType.equals(discountAmountTypes[2])) {
            discount.setDeleted(true);
        }

        databaseManager.insertDiscount(discount).subscribe();
    }

    @Override
    public void addServiceFee(double amount, String description, int amountType) {
        ServiceFee serviceFee = new ServiceFee();
        serviceFee.setAmount(amount);
        serviceFee.setName(description);
        serviceFee.setType(amountType);
        serviceFee.setApplyingType(APP_TYPE_ITEM);
        serviceFee.setCreatedDate(System.currentTimeMillis());
        serviceFee.setDeleted(false);
        serviceFee.setNotModifyted(true);

        if (amountType == (TYPE_REPRICE)) {
            serviceFee.setDeleted(true);
        }

        databaseManager.getServiceFeeOperations().addServiceFee(serviceFee).blockingSingle();
    }
}
