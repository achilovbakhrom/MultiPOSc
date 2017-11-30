package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.products.VendorProductCon;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.ui.mainpospage.view.ProductInfoView;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import static com.jim.multipos.ui.service_fee_new.Constants.APP_TYPE_ITEM;
import static com.jim.multipos.ui.service_fee_new.Constants.TYPE_REPRICE;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

public class ProductInfoPresenterImpl extends BasePresenterImpl<ProductInfoView> implements ProductInfoPresenter {
    private DatabaseManager databaseManager;
    private Product product;
    private int currentVendorPosition = 0;
    private int productQuantity = 20;
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
    public Product getProduct() {
        if (databaseManager.getProductOperations().getAllProducts().blockingSingle().isEmpty()) {
            Unit unit = new Unit();
            unit.setAbbr("pcs");
            unit.setIsActive(true);

            databaseManager.getUnitOperations().addUnit(unit).blockingSingle();

            Currency currency = new Currency();
            currency.setAbbr("UZS");
            currency.setName("SUM");
            currency.setIsMain(true);

            databaseManager.getCurrencyOperations().addCurrency(currency).blockingSingle();

            Vendor vendor = new Vendor();
            vendor.setName("Bobur Corporation");
            vendor.setAddress("Temur Malik ko'chasi, Texno Plaza");
            vendor.setContactName("Bobur");
            vendor.setCreatedDate(System.currentTimeMillis());
            vendor.setIsActive(false);

            Vendor vendor1 = new Vendor();
            vendor1.setName("Temur Corporation");
            vendor1.setAddress("Bobur Malik ko'chasi, Ocean Plaza");
            vendor1.setContactName("Temur");
            vendor1.setCreatedDate(System.currentTimeMillis());
            vendor1.setIsActive(false);

            Vendor vendor2 = new Vendor();
            vendor2.setName("Baxtiyor Corporation");
            vendor2.setAddress("Baxtiyor Malik ko'chasi, Sky Mall");
            vendor2.setContactName("Baxtiyor");
            vendor2.setCreatedDate(System.currentTimeMillis());
            vendor2.setIsActive(false);

            databaseManager.addVendor(vendor).blockingSingle();
            databaseManager.addVendor(vendor1).blockingSingle();
            databaseManager.addVendor(vendor2).blockingSingle();

            Product product = new Product();
            product.setName("Coca cola");
            product.setCreatedDate(System.currentTimeMillis());
            product.setIsDeleted(false);
            product.setIsActive(true);
            product.setSku(String.valueOf(new Random().nextInt()));
            product.setBarcode(String.valueOf(new Random().nextInt()));
            product.setPrice(13.3);
            product.setPhotoPath("http://pngimg.com/uploads/cocacola/cocacola_PNG2.png");
            product.setMainUnitId(unit.getId());
            product.setCostCurrencyId(currency.getId());
            product.setPriceCurrencyId(currency.getId());
            product.setDescription("Coca-Cola is the most popular and biggest-selling soft drink in history, as well as one of the most recognizable brands in the world.");

            databaseManager.getProductOperations().addProduct(product).blockingSingle();

            VendorProductCon vendorProductCon = new VendorProductCon();
            vendorProductCon.setProductId(product.getId());
            vendorProductCon.setVendorId(vendor.getId());

            VendorProductCon vendorProductCon1 = new VendorProductCon();
            vendorProductCon1.setProductId(product.getId());
            vendorProductCon1.setVendorId(vendor1.getId());

            VendorProductCon vendorProductCon2 = new VendorProductCon();
            vendorProductCon2.setProductId(product.getId());
            vendorProductCon2.setVendorId(vendor2.getId());

            databaseManager.addVendorProductConnection(vendorProductCon).blockingSingle();
            databaseManager.addVendorProductConnection(vendorProductCon1).blockingSingle();
            databaseManager.addVendorProductConnection(vendorProductCon2).blockingSingle();
        }

        product = databaseManager.getProductOperations().getAllProducts().blockingSingle().get(0);

        return product;
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
    public int getProductQuantity() {
        int result = productQuantity - currentProductQuantity;

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
    public void addServiceFee(double amount, String description, String amountType) {
        ServiceFee serviceFee = new ServiceFee();
        serviceFee.setAmount(amount);
        serviceFee.setReason(description);
        serviceFee.setType(amountType);
        serviceFee.setApplyingType(APP_TYPE_ITEM);
        serviceFee.setCreatedDate(System.currentTimeMillis());
        serviceFee.setDeleted(false);
        serviceFee.setNotModifyted(true);

        if (amountType.equals(TYPE_REPRICE)) {
            serviceFee.setDeleted(true);
        }

        databaseManager.getServiceFeeOperations().addServiceFee(serviceFee).blockingSingle();
    }
}
