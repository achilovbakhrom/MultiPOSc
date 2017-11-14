package com.jim.multipos.ui.product_last;

import android.os.Bundle;
import android.util.Log;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.VendorProductCon;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.db.model.unit.UnitCategory;
import com.jim.multipos.ui.product_last.helpers.CategoryAddEditMode;
import com.jim.multipos.utils.UIUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

@PerActivity
public class ProductPresenterImpl extends BasePresenterImpl<ProductView> implements ProductPresenter {

    private final String CATEGORY_KEY = "CATEGORY_KEY", SUBCATEGORY_KEY = "SUBCATEGORY_KEY", PRODUCT_KEY = "PRODUCT_KEY";

    @Setter
    @Getter
    private CategoryAddEditMode mode = CategoryAddEditMode.CATEGORY_ADD_MODE;

    @Setter
    @Getter
    private Category category, subcategory;

    @Getter
    @Setter
    private Product product;

    private List<Currency> currencies;

    private List<ProductClass> productClasses;

    @Getter
    DatabaseManager databaseManager;

    @Inject
    ProductPresenterImpl(ProductView productView, DatabaseManager databaseManager) {
        super(productView);
        this.databaseManager = databaseManager;
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        if (bundle != null) {
            category = (Category) bundle.getSerializable(CATEGORY_KEY);
            subcategory = (Category) bundle.getSerializable(SUBCATEGORY_KEY);
            product = (Product) bundle.getSerializable(PRODUCT_KEY);
        }
    }

    private List<ProductClass> getProductClasses() {
        if (productClasses == null)
            productClasses = databaseManager.getAllProductClass().blockingGet();
        return productClasses;
    }

    private List<Currency> getCurrencies() {
        if (currencies == null)
            currencies = databaseManager.getCurrencies();
        return currencies;
    }

    /**
     * item move processing for categories
     * writes to db immediately
     */
    @Override
    public void setCategoryItemsMoved() {
        List<Category> categories = view.getCategories();
        if (categories != null) {
            for (int i = 0; i <categories.size(); i++) {
                Category category = categories.get(i);
                if (category != null) {
                    category.setPosition((double) i);
                    databaseManager.addCategory(category)
                            .subscribe(id -> Log.d("sss", "setCategoryItemsMoved: " + id));
                }
            }
        }
    }

    /**
     * item move processing for subcategories
     * writes to db immediately
     */
    @Override
    public void setSubcategoryItemsMoved() {
        List<Category> categories = view.getSubcategories();
        if (categories != null) {
            for (int i = 0; i <categories.size(); i++) {
                Category category = categories.get(i);
                if (category != null) {
                    category.setPosition((double) i);
                    databaseManager.addCategory(category)
                            .subscribe(id -> Log.d("sss", "setSubcategoryItemsMoved: " + id));
                }
            }
        }
    }

    @Override
    public void setProductItemsMoved() {
        List<Product> products = view.getProducts();
        if (products != null) {
            for (int i = 0; i < products.size(); i++) {
                Product product = products.get(i);
                if (product != null) {
                    product.setPosition((double) i);
                    databaseManager.addProduct(product).subscribe(id -> {
                        Log.d("sss", "setProductItemsMoved: " + id);
                    });
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        view.initRightSide(getCategories());
        view.initProductForm(provideUnitCategoriesList(), provideUnitList(), provideProductClassList(), provideCurrencyName());
    }

    private List<Category> getCategories() {
        List<Category> result = null;
        if (!view.isActiveVisible())
            result = databaseManager.getActiveCategories().blockingSingle();
        else
            result = databaseManager.getAllCategories().blockingSingle();
        Collections.sort(result, (o1, o2) -> o1.getPosition().compareTo(o2.getPosition()));
        Collections.sort(result, (o1, o2) -> -((Boolean) o1.isActive()).compareTo(o2.isActive()));
        result.add(0, null);
        return result;
    }


    private String provideCurrencyName() {
        List<Currency> currencies = databaseManager.getCurrencies();
        if (currencies != null && !currencies.isEmpty()) {
            return currencies.get(0).getAbbr();
        }
        return null;
    }

    private String[] provideUnitCategoriesList() {
        List<UnitCategory> unitCategories = databaseManager.getAllUnitCategories().blockingSingle();
        if (unitCategories != null && !unitCategories.isEmpty()) {
            String[] result = new String[unitCategories.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = unitCategories.get(i).getName();
            }
            return result;
        }
        return new String[0];
    }

    private String[] provideUnitList() {
        List<UnitCategory> unitCategories = databaseManager.getAllUnitCategories().blockingSingle();
        if (unitCategories != null && !unitCategories.isEmpty()) {
            List<Unit> units = unitCategories.get(0).getUnits();
            if (units != null && !units.isEmpty()) {
                String[] result = new String[units.size()];
                for (int i = 0; i < result.length; i++) {
                    result[i] = units.get(i).getName();
                }
                return result;
            }
        }
        return new String[0];
    }



    private String[] provideProductClassList() {
        List<ProductClass> productClasses = databaseManager.getAllProductClass().blockingGet();
         if (productClasses != null && !productClasses.isEmpty()) {
            String[] result = new String[productClasses.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = productClasses.get(i).getName();
            }
            return result;
        }
        return new String[0];
    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (bundle != null) {
            if (category != null) { bundle.putSerializable(CATEGORY_KEY, category); }
            if (subcategory != null) { bundle.putSerializable(SUBCATEGORY_KEY, subcategory); }
            if (product != null) { bundle.putSerializable(PRODUCT_KEY, product); }
        }
    }

    /**
     * category selection processing
     * @param category - selected category object from the view
     */
    @Override
    public void categorySelected(Category category) {
        view.closeKeyboard();
        switch (mode) {
            case CATEGORY_ADD_MODE:
                if (!view.getName().equals("") || !view.getDescription().equals("") || !view.isActive()) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            openCategory(category);
                        }
                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectAddCategoryItem();
                        }
                    });
                } else
                    openCategory(category);
                break;
            case SUBCATEGORY_ADD_MODE:
                if (!view.getName().equals("") || !view.getDescription().equals("") || !view.isActive()) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            openCategory(category);
                        }
                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectAddSubcategoryItem();
                        }
                    });
                } else
                    openCategory(category);
                break;
            case CATEGORY_EDIT_MODE:
                if (!view.getName().equals(this.category.getName()) || !view.getDescription().equals(this.category.getDescription()) || this.category.getIsActive() != view.isActive()) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            openCategory(category);
                        }
                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectCategory(ProductPresenterImpl.this.category.getId());
                        }
                    });
                } else
                    openCategory(category);
                break;
            case SUBCATEGORY_EDIT_MODE:
                if (!view.getName().equals(subcategory.getName()) || !view.getDescription().equals(subcategory.getDescription()) || subcategory.getIsActive() != view.isActive()) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            openCategory(category);
                        }
                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectSubcategory(ProductPresenterImpl.this.subcategory.getId());
                            view.selectCategory(ProductPresenterImpl.this.category.getId());
                        }
                    });
                } else
                    openCategory(category);
                break;
            case PRODUCT_ADD_MODE:
                if (!view.getProductName().equals("") || !view.getBarCode().equals("") || !view.getSku().equals("") ||
                        !view.getPrice().equals(0.0d) || !view.getCost().equals(0.0d) || view.getUnitCategorySelectedPos() != 0 ||
                        view.getUnitSelectedPos() != 0 || !view.getVendorSelectedPos().isEmpty() || view.getProductClassSelectedPos() > 0 ||
                        !view.getProductIsActive()) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            openCategory(category);
                        }
                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectAddProductListItem();
                            mode = CategoryAddEditMode.PRODUCT_ADD_MODE;
                        }
                    });
                } else {
                    openCategory(category);
                }
                break;
            case PRODUCT_EDIT_MODE:
                if (this.product == null) return;
                UnitCategory unitCategory = null;
                List<UnitCategory> tempUnitCategories = databaseManager.getAllUnitCategories().blockingSingle();
                if (tempUnitCategories.size() > view.getUnitCategorySelectedPos())
                    unitCategory = tempUnitCategories.get(view.getUnitCategorySelectedPos());
                ProductClass productClass = null;
                List<ProductClass> tempProductClasses = databaseManager.getAllProductClass().blockingGet();
                if (view.getProductClassSelectedPos() >= 0 && tempProductClasses.size() > view.getProductClassSelectedPos())
                    productClass = tempProductClasses.get(view.getProductClassSelectedPos());
                List<Vendor> vendors = this.product.getVendor();
                List<Long> viewVendors = view.getVendorSelectedPos();
                boolean hasChanged = vendors.size() != viewVendors.size();
                if (!hasChanged) {
                    for (Long id : viewVendors) {
                        boolean found = false;
                        for (Vendor vendor : vendors) {
                            if (vendor.getId().equals(id)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            hasChanged = true;
                            break;
                        }
                    }
                }
                if (!view.getProductName().equals(this.product.getName()) || !view.getBarCode().equals(this.product.getBarcode()) || !view.getSku().equals(this.product.getSku()) ||
                        !view.getPrice().equals(this.product.getPrice()) || !view.getCost().equals(this.product.getCost()) ||
                        (unitCategory != null && !unitCategory.getUnits().get(view.getUnitSelectedPos()).getId().equals(this.product.getMainUnitId())) ||
                        (productClass != null && !productClass.getId().equals(this.product.getProductClass().getId())) ||
                        view.getProductIsActive() != this.product.getIsActive() || hasChanged) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            openCategory(category);
                        }
                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectProductListItem(ProductPresenterImpl.this.product.getId());
                            mode = CategoryAddEditMode.PRODUCT_EDIT_MODE;
                        }
                    });
                } else {
                    openCategory(category);
                }
                break;
        }


    }

    /**
     * product selection processing
     * @param product - selected product from view
     */
    @Override
    public void productSelected(Product product) {
        view.closeKeyboard();
        switch (mode) {
            case PRODUCT_ADD_MODE:
                if (!view.getProductName().equals("") || !view.getBarCode().equals("") || !view.getSku().equals("") ||
                        !view.getPrice().equals(0.0d) || !view.getCost().equals(0.0d) || view.getUnitCategorySelectedPos() != 0 ||
                        view.getUnitSelectedPos() != 0 || !view.getVendorSelectedPos().isEmpty() || view.getProductClassSelectedPos() > 0 ||
                        !view.getProductIsActive()) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            openProduct(product);
                        }
                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectAddProductListItem();
                            mode = CategoryAddEditMode.PRODUCT_ADD_MODE;
                        }
                    });
                } else {
                    openProduct(product);
                }
                break;
            case PRODUCT_EDIT_MODE:
                if (this.product == null) return;
                UnitCategory unitCategory = null;
                List<UnitCategory> tempUnitCategories = databaseManager.getAllUnitCategories().blockingSingle();
                if (tempUnitCategories.size() > view.getUnitCategorySelectedPos())
                    unitCategory = tempUnitCategories.get(view.getUnitCategorySelectedPos());
                ProductClass productClass = null;
                List<ProductClass> tempProductClasses = databaseManager.getAllProductClass().blockingGet();
                if (view.getProductClassSelectedPos() >= 0 && tempProductClasses.size() > view.getProductClassSelectedPos())
                    productClass = tempProductClasses.get(view.getProductClassSelectedPos());
                List<Vendor> vendors = this.product.getVendor();
                List<Long> viewVendors = view.getVendorSelectedPos();
                boolean hasChanged = vendors.size() != viewVendors.size();
                if (!hasChanged) {
                    for (Long id : viewVendors) {
                        boolean found = false;
                        for (Vendor vendor : vendors) {
                            if (vendor.getId().equals(id)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            hasChanged = true;
                            break;
                        }
                    }
                }
                if (!view.getProductName().equals(this.product.getName()) || !view.getBarCode().equals(this.product.getBarcode()) || !view.getSku().equals(this.product.getSku()) ||
                        !view.getPrice().equals(this.product.getPrice()) || !view.getCost().equals(this.product.getCost()) ||
                        (unitCategory != null && !unitCategory.getUnits().get(view.getUnitSelectedPos()).getId().equals(this.product.getMainUnitId())) ||
                        (productClass != null && !productClass.getId().equals(this.product.getProductClass().getId())) ||
                        view.getProductIsActive() != this.product.getIsActive() || hasChanged) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            openProduct(product);
                        }
                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectProductListItem(ProductPresenterImpl.this.product.getId());
                            mode = CategoryAddEditMode.PRODUCT_EDIT_MODE;
                        }
                    });
                } else {
                    openProduct(product);
                }
                break;
            case SUBCATEGORY_EDIT_MODE:
                if (!view.getName().equals(subcategory.getName()) || !view.getDescription().equals(subcategory.getDescription()) || subcategory.getIsActive() != view.isActive()) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            openProduct(product);
                        }
                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectSubcategory(ProductPresenterImpl.this.subcategory.getId());
                            view.selectCategory(ProductPresenterImpl.this.category.getId());
                        }
                    });
                } else
                    openProduct(product);
                break;

        }
    }

    private void openCategory(Category category) {
        this.product = null;
        this.subcategory = null;
        view.setSubcategoryPath(null);
        view.clearProductList();
        view.unselectProductsList();
        if (category == null) {
            view.setCategoryPath(null);
            this.category = null;
            mode = CategoryAddEditMode.CATEGORY_ADD_MODE;
            view.unselectSubcategoryList();
            view.clearSubcategoryList();
            view.setCategoryPath(null);
            view.openAddCategoryMode();
            view.unselectCategoryList();
        }
        else {
            this.category = category;
            this.category.resetSubCategories();
            view.setCategoryPath(this.category.getName());
            view.selectCategory(category.getId());
            mode = CategoryAddEditMode.CATEGORY_EDIT_MODE;
            view.openEditCategoryMode(category.getName(), category.getDescription(), category.isActive());
            List<Category> subCategories;
            if (view.isActiveVisible()) {
                subCategories = this.category.getSubCategories();
            } else {
                subCategories = this.category.getActiveSubCategories();
            }
            if (subCategories != null && !subCategories.isEmpty()) {
                Collections.sort(subCategories, (o1, o2) -> o1.getPosition().compareTo(o2.getPosition()));
                Collections.sort(subCategories, (o1, o2) -> -((Boolean) o1.isActive()).compareTo(o2.isActive()));
            }
            subCategories.add(0, null);
            view.unselectSubcategoryList();
            view.setListToSubcategoryList(subCategories);
        }
    }

    private void openSubcategory(Category category) {
        this.product = null;
        view.unselectProductsList();
        if (category == null) {
            view.setSubcategoryPath(null);
            this.subcategory = null;
            mode = CategoryAddEditMode.SUBCATEGORY_ADD_MODE;
            view.openAddSubcategoryMode(this.category.getName());
            view.clearProductList();
            view.unselectSubcategoryList();
        }
        else {
            view.setSubcategoryPath(category.getName());
            this.subcategory = category;
            this.subcategory.resetProducts();
            mode = CategoryAddEditMode.SUBCATEGORY_EDIT_MODE;
            if (this.category != null) {
                view.openEditSubcategoryMode(category.getName(), category.getDescription(), category.isActive(), this.category.getName());
                view.selectSubcategory(subcategory.getId());
                List<Product> products;
                if (view.isActiveVisible()) {
                    products = category.getProducts();
                } else {
                    products = category.getActiveProducts();
                }
                if (products != null && !products.isEmpty()) {
                    Collections.sort(products, (o1, o2) -> o1.getPosition().compareTo(o2.getPosition()));
                    Collections.sort(products, (o1, o2) -> -((Boolean) o1.isActive()).compareTo(o2.isActive()));
                }
                products.add(0, null);
                view.setListToProducts(products);
            }
        }
    }


    private void openProduct(Product product) {
        if (product == null) {
            this.product = null;
            mode = CategoryAddEditMode.PRODUCT_ADD_MODE;
            view.unselectProductsList();
            view.openProductAddMode();
        }
        else {
            this.product = product;
            view.selectProductListItem(this.product.getId());
            mode = CategoryAddEditMode.PRODUCT_EDIT_MODE;
            List<UnitCategory> unitCategories = databaseManager.getAllUnitCategories().blockingSingle();
            int unitCategoryPos = 0;
            UnitCategory temp = null;
            for (UnitCategory unitCategory : unitCategories) {
                if (unitCategory.getId().equals(product.getMainUnit().getUnitCategoryId())) {
                    unitCategoryPos = unitCategories.indexOf(unitCategory);
                    temp = unitCategory;
                    break;
                }
            }
            int unitPos = 0;
            String[] units = null;
            if (temp != null) {
                unitPos = temp.getUnits().indexOf(product.getMainUnit());
                units = new String[temp.getUnits().size()];
                for (int i  = 0; i < units.length; i++) {
                    units[i] = temp.getUnits().get(i).getName();
                }

            }
            int productClassPos = 0;
            if (product.getProductClass() != null) {
                List<ProductClass> dbProductClasses = databaseManager.getAllProductClass().blockingGet();
                for (ProductClass productClass : dbProductClasses) {
                    if (productClass.getId().equals(product.getProductClass().getId())) {
                        productClassPos = dbProductClasses.indexOf(productClass);
                        break;
                    }
                }
            }
            List<Long> vendorIds = new ArrayList<>();
            for (Vendor vendor : product.getVendor()) {
                vendorIds.add(vendor.getId());
            }
            view.openProductEditMode(product.getName(),
                    product.getPrice(),
                    product.getCost(),
                    product.getBarcode(),
                    product.getSku(),
                    product.isActive(),
                    product.getPriceCurrency().getAbbr(),
                    product.getCostCurrency().getAbbr(),
                    productClassPos,
                    unitCategoryPos,
                    units,
                    unitPos,
                    vendorIds,
                    product.getDescription());
        }
    }


    @Override
    public void openVendorChooserDialog() {
        view.openVendorChooserDialog(databaseManager.getVendors().blockingSingle());
    }


    /**
     * subcategory selection processing
     * @param category - selected subcategory from view
     */
    @Override
    public void subcategorySelected(Category category) {
        view.closeKeyboard();

        switch (mode) {
            case CATEGORY_ADD_MODE:
                if (!view.getName().equals("") || !view.getDescription().equals("") || !view.isActive()) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            openCategory(category);
                        }
                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectAddCategoryItem();
                        }
                    });
                } else
                    openCategory(category);
                break;
            case SUBCATEGORY_ADD_MODE:
                if (!view.getName().equals("") || !view.getDescription().equals("") || !view.isActive()) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            openSubcategory(category);
                        }
                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectAddSubcategoryItem();
                        }
                    });
                } else
                    openSubcategory(category);
                break;
            case CATEGORY_EDIT_MODE:
                if (!view.getName().equals(this.category.getName()) || !view.getDescription().equals(this.category.getDescription()) || this.category.getIsActive() != view.isActive()) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            openSubcategory(category);
                        }
                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectCategory(ProductPresenterImpl.this.category.getId());
                            view.unselectSubcategoryList();
                        }
                    });
                } else
                    openSubcategory(category);
                break;
            case SUBCATEGORY_EDIT_MODE:
                if (!view.getName().equals(subcategory.getName()) || !view.getDescription().equals(subcategory.getDescription()) || subcategory.getIsActive() != view.isActive()) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            openSubcategory(category);
                        }
                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectSubcategoryListItem(ProductPresenterImpl.this.subcategory.getId());
                        }
                    });
                }
                else openSubcategory(category);
                break;
            case PRODUCT_ADD_MODE:
                if (!view.getProductName().equals("") || !view.getBarCode().equals("") || !view.getSku().equals("") ||
                        !view.getPrice().equals(0.0d) || !view.getCost().equals(0.0d) || view.getUnitCategorySelectedPos() != 0 ||
                        view.getUnitSelectedPos() != 0 || !view.getVendorSelectedPos().isEmpty() || view.getProductClassSelectedPos() > 0 ||
                        !view.getProductIsActive()) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            openSubcategory(category);
                        }
                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectAddProductListItem();
                            mode = CategoryAddEditMode.PRODUCT_ADD_MODE;
                        }
                    });
                } else {
                    openSubcategory(category);
                }
                break;
            case PRODUCT_EDIT_MODE:
                if (this.product == null) return;
                UnitCategory unitCategory = null;
                List<UnitCategory> tempUnitCategories = databaseManager.getAllUnitCategories().blockingSingle();
                if (tempUnitCategories.size() > view.getUnitCategorySelectedPos())
                    unitCategory = tempUnitCategories.get(view.getUnitCategorySelectedPos());
                ProductClass productClass = null;
                List<ProductClass> tempProductClasses = databaseManager.getAllProductClass().blockingGet();
                if (view.getProductClassSelectedPos() >= 0 && tempProductClasses.size() > view.getProductClassSelectedPos())
                    productClass = tempProductClasses.get(view.getProductClassSelectedPos());
                List<Vendor> vendors = this.product.getVendor();
                List<Long> viewVendors = view.getVendorSelectedPos();
                boolean hasChanged = vendors.size() != viewVendors.size();
                if (!hasChanged) {
                    for (Long id : viewVendors) {
                        boolean found = false;
                        for (Vendor vendor : vendors) {
                            if (vendor.getId().equals(id)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            hasChanged = true;
                            break;
                        }
                    }
                }
                if (!view.getProductName().equals(this.product.getName()) || !view.getBarCode().equals(this.product.getBarcode()) || !view.getSku().equals(this.product.getSku()) ||
                        !view.getPrice().equals(this.product.getPrice()) || !view.getCost().equals(this.product.getCost()) ||
                        (unitCategory != null && !unitCategory.getUnits().get(view.getUnitSelectedPos()).getId().equals(this.product.getMainUnitId())) ||
                        (productClass != null && !productClass.getId().equals(this.product.getProductClass().getId())) ||
                        view.getProductIsActive() != this.product.getIsActive() || hasChanged) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            openSubcategory(category);
                        }
                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectProductListItem(ProductPresenterImpl.this.product.getId());
                            mode = CategoryAddEditMode.PRODUCT_EDIT_MODE;
                        }
                    });
                } else {
                    openSubcategory(category);
                }
                break;
        }


    }





    /**
     * adding or editing category and subcategory
     * @param name - name of category or subcategory
     * @param description - description of category or subcategory
     * @param isActive - active state of category or subcategory
     */
    @Override
    public void addCategory(String name, String description, boolean isActive) {
        if ((mode == CategoryAddEditMode.CATEGORY_ADD_MODE
                && !isCategoryNameUnique(name)) || (mode == CategoryAddEditMode.CATEGORY_EDIT_MODE && !isCategoryNameUnique(name) && category != null && !category.getName().equals(name))) {
            view.suchCategoryNameExists(name);
            return;
        }

        if (mode == CategoryAddEditMode.SUBCATEGORY_ADD_MODE
                && category != null && !isSubcategoryNameUnique(category.getName(), name) ||
                (mode == CategoryAddEditMode.SUBCATEGORY_EDIT_MODE && category != null && !isSubcategoryNameUnique(category.getName(), name) &&
                subcategory != null && !subcategory.getName().equals(name))) {
            view.suchSubcategoryNameExists(name);
            return;
        }

        Category result = new Category();
        if (mode == CategoryAddEditMode.CATEGORY_ADD_MODE) { // adding category
            result.setName(name);
            result.setDescription(description);
            result.setIsActive(isActive);
            databaseManager.addCategory(result).subscribe(id -> {
                view.addToCategoryList(result);
                view.setCategoryPath(null);
                openCategory(null);
            });
        }
        else if (mode == CategoryAddEditMode.SUBCATEGORY_ADD_MODE && category != null) { // adding subcategory
            result.setParentId(category.getId());
            result.setName(name);
            result.setDescription(description);
            result.setActive(isActive);
            databaseManager.addCategory(result).subscribe(id -> {
                view.addToSubcategoryList(result);
                view.setSubcategoryPath(null);
                openSubcategory(null);
            });

        } else if(mode == CategoryAddEditMode.CATEGORY_EDIT_MODE && category != null) { // edit category
            view.showEditDialog(new UIUtils.AlertListener() {
                @Override
                public void onPositiveButtonClicked() {
                    result.setId(category.getId());
                    result.setName(name);
                    result.setDescription(description);
                    result.setActive(isActive);
                    databaseManager.addCategory(result).subscribe(id -> {
                        view.editCategory(result);
                        category.refresh();
                        openCategory(null);
                    });
                }

                @Override
                public void onNegativeButtonClicked() {

                }
            });

        } else if (mode == CategoryAddEditMode.SUBCATEGORY_EDIT_MODE && subcategory != null) { // edit subcategory
            view.showEditDialog(new UIUtils.AlertListener() {
                @Override
                public void onPositiveButtonClicked() {
                    result.setId(subcategory.getId());
                    result.setParentId(subcategory.getParentId());
                    result.setName(name);
                    result.setDescription(description);
                    result.setActive(isActive);
                    databaseManager.addCategory(result).subscribe(id -> {
                        view.editSubcategory(result);
                        subcategory.refresh();
                        category.resetSubCategories();
                        view.unselectSubcategoryList();
                        openSubcategory(null);
                    });
                }

                @Override
                public void onNegativeButtonClicked() {

                }
            });
        }
    }

    /**
     * All categories from db added to first index null object
     * @return - all categories from db
     */

    /**
     * All products from db added to first index null object
     * @return - all products from db
     */
    public List<Product> getProducts() {
        List<Product> result = databaseManager.getAllProducts().blockingSingle();
        Collections.sort(result, (o1, o2) -> o1.getPosition().compareTo(o2.getPosition()));
        Collections.sort(result, (o1, o2) -> -((Boolean) o1.isActive()).compareTo(o2.isActive()));
        result.add(0, null);
        return result;
    }

    /**
     * Check for uniqueness the given subcategory name
     * @param categoryName - parent's name
     * @param subcategoryName - given subcategory name
     * @return - true if subcategory name is unique, otherwise false
     */
    @Override
    public boolean isSubcategoryNameUnique(String categoryName, String subcategoryName) {
        return !databaseManager.isSubCategoryNameExists(categoryName, subcategoryName).blockingSingle();
    }

    /**
     * Check for uniqueness the given category name
     * @param categoryName - give category name
     * @return - true if category name is unique, otherwise false
     */
    @Override
    public boolean isCategoryNameUnique(String categoryName) {
        return !databaseManager.isCategoryNameExists(categoryName).blockingSingle();
    }

    /**
     * Check for uniqueness the given product name
     * @param productName - given product name
     * @return true if product name is unique, otherwise false
     */
    @Override
    public boolean isProductNameUnique(String productName, Long categoryId) {
        return !databaseManager.isProductNameExists(productName, categoryId).blockingSingle();
    }

    /**
     * Gets category by given id
     * @param id - give id
     * @return Category object if found, otherwise null
     */
    @Override
    public Category getCategoryById(Long id) {
        return databaseManager
                .getCategoryById(id)
                .blockingSingle();
    }

    /**
     * Ejects subcategory of given category added by first index null object
     * @param category - given category
     * @return subcategories of given category
     */
    @Override
    public List<Category> getSubcategories(Category category) {
        List<Category> result = new ArrayList<>();
            result.add(null);
        if (category.getSubCategories() != null) {
            result.addAll(category.getSubCategories());
        }
        return result;
    }

    /**
     * product deletion
     */
    @Override
    public void deleteProduct() {
        if (product != null) {
            if (product.isActive()) {
                view.showCannotDeleteActiveItemDialog();
                return;
            }
            view.showDeleteDialog(new UIUtils.AlertListener() {
                @Override
                public void onPositiveButtonClicked() {
                    databaseManager.removeProduct(product).subscribe(isDeleted -> {
                        if (isDeleted) {
                            if (subcategory != null) {
                                subcategory.resetProducts();
                                openSubcategory(subcategory);
                                openProduct(null);
//                                List<Product> list = new ArrayList<>();
//                                if (ProductPresenterImpl.this.subcategory != null &&
//                                        !ProductPresenterImpl.this.subcategory.getProducts().isEmpty()) {
//                                    list.addAll(subcategory.getProducts());
//                                    Collections.sort(list, (o1, o2) -> o1.getPosition().compareTo(o2.getPosition()));
//                                    Collections.sort(list, (o1, o2) -> -((Boolean) o1.isActive()).compareTo(o2.isActive()));
//                                }
//                                list.add(0, null);
//                                view.setListToProducts(list);
//                                mode = CategoryAddEditMode.PRODUCT_ADD_MODE;
//                                view.openProductAddMode();
//                                view.unselectProductsList();
//                                showActivesToggled();
                            }

                        }
                    });
                }
                @Override
                public void onNegativeButtonClicked() {}
            });
        }
    }

    /**
     * deleting category
     */
    @Override
    public void deleteCategory() {
        switch (mode) {
            case SUBCATEGORY_EDIT_MODE:
                if (subcategory != null) {
                    if (subcategory.isActive()) {
                        view.showCannotDeleteActiveItemDialog();
                        return;
                    }
                    subcategory.resetProducts();
                    if (!subcategory.getProducts().isEmpty()) {
                        view.showListMustBeEmptyDialog();
                        return;
                    }
                    view.showDeleteDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            databaseManager.removeCategory(subcategory).subscribe(isDeleted -> {
                                Log.d("sss", "deleteCategory: ");
                                if (isDeleted) {
                                    category.resetSubCategories();
                                    openCategory(category);
                                    openSubcategory(null);
                                }
                            });
                        }
                        @Override
                        public void onNegativeButtonClicked() {}
                    });

                }
                break;
            case CATEGORY_EDIT_MODE:
                if (category != null) {
                    if (category.isActive()) {
                        view.showCannotDeleteActiveItemDialog();
                        return;
                    }
                    category.resetSubCategories();
                    if (!category.getSubCategories().isEmpty()) {
                        view.showListMustBeEmptyDialog();
                        return;
                    }
                    view.showDeleteDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            databaseManager.removeCategory(category).subscribe(isDeleted -> {
                                Log.d("sss", "deleteCategory: ");
                                if (isDeleted) {
                                    List<Category> categories;
                                    if (!view.isActiveVisible()) {
                                        categories = databaseManager.getActiveCategories().blockingSingle();
                                    } else {
                                        categories = databaseManager.getAllCategories().blockingSingle();
                                    }
                                    if (categories != null && !categories.isEmpty()) {
                                        Collections.sort(categories, (o1, o2) -> o1.getPosition().compareTo(o2.getPosition()));
                                        Collections.sort(categories, (o1, o2) -> -((Boolean) o1.isActive()).compareTo(o2.isActive()));
                                    }
                                    categories.add(0, null);
                                    view.setListToCategoryList(categories);
                                    view.unselectSubcategoryList();
                                    view.unselectProductsList();
                                    view.clearSubcategoryList();
                                    view.clearProductList();
                                    view.selectAddCategoryItem();
                                    view.openAddCategoryMode();
                                    view.setCategoryPath(null);
                                    view.setSubcategoryPath(null);
                                    mode = CategoryAddEditMode.CATEGORY_ADD_MODE;
                                    subcategory = null;
                                    category = null;
                                    product = null;
                                }
                            });
                        }

                        @Override
                        public void onNegativeButtonClicked() {

                        }
                    });
                }
                break;

        }
    }




    /**
     * creating or editing product
     * @param name - product name
     * @param price - product price
     * @param barcode - product barcode
     * @param sku - product sku
     * @param photoPath - product photo path
     * @param isActive - product active state
     * @param costCurrencyPos - product cost currency
     * @param priceCurrencuyPos - product price currency
     * @param productClassPos - product class
     * @param unitCategoryPos - product main unit
     * @param unitPos - product sub units
     * @param vendors - product vendors
     * @param description - product description
     */
    @Override
    public void addProduct(String name, Double price, Double cost, String barcode,
                           String sku, String photoPath, boolean isActive,
                           int costCurrencyPos, int priceCurrencuyPos, int productClassPos,
                           int unitCategoryPos, int unitPos, List<Long> vendors, String description) {

        switch (mode) {
            case PRODUCT_ADD_MODE:
                Product product = new Product();
                product.setName(name);
                product.setCost(cost);
                product.setPrice(price);
                product.setBarcode(barcode);
                product.setSku(sku);
                product.setPhotoPath(photoPath);
                product.setIsActive(isActive);
                product.setCategory(subcategory);
                product.setCategoryId(subcategory.getId());
                List<Currency> currencies = databaseManager.getAllCurrencies().blockingSingle();
                if (currencies != null && !currencies.isEmpty()) {
                    product.setCostCurrency(currencies.get(costCurrencyPos));
                    product.setPriceCurrency(currencies.get(priceCurrencuyPos));
                }
                List<ProductClass> productClasses = databaseManager.getAllProductClass().blockingGet();
                if (productClasses != null && !productClasses.isEmpty()) {
                    product.setProductClass(productClasses.get(productClassPos));
                }
                List<UnitCategory> unitCategories = databaseManager.getAllUnitCategories().blockingSingle();
                if (unitCategories != null && !unitCategories.isEmpty()) {
                    UnitCategory unitCategory = unitCategories.get(unitCategoryPos);
                    if (unitCategory.getUnits() != null && !unitCategory.getUnits().isEmpty()) {
                        product.setMainUnit(unitCategory.getUnits().get(unitPos));
                    }
                }
                product.setDescription(description);
                databaseManager.addProduct(product).subscribe(id -> {
                    for (Long vendorId : vendors) {
                        VendorProductCon connection = new VendorProductCon();
                        connection.setVendorId(vendorId);
                        connection.setProductId(id);
                        databaseManager.addVendorProductConnection(connection).subscribe();
                    }
                    view.addToProductList(product);
                    openProduct(null);
                });
                break;
            case PRODUCT_EDIT_MODE:
                view.showEditDialog(new UIUtils.AlertListener() {
                    @Override
                    public void onPositiveButtonClicked() {
                        if (ProductPresenterImpl.this.product != null) {
                            Product result = new Product();
                            result.setId(ProductPresenterImpl.this.product.getId());
                            result.setName(name);
                            result.setPrice(price);
                            result.setCost(cost);
                            result.setBarcode(barcode);
                            result.setSku(sku);
                            result.setPhotoPath(photoPath);
                            result.setIsActive(isActive);
                            result.setCategory(subcategory);
                            result.setCategoryId(subcategory.getId());
                            List<Currency> tempCurrencies = databaseManager.getAllCurrencies().blockingSingle();
                            if (tempCurrencies.size() > priceCurrencuyPos) {
                                result.setPriceCurrency(tempCurrencies.get(priceCurrencuyPos));
                                result.setPriceCurrencyId(tempCurrencies.get(priceCurrencuyPos).getId());
                            }
                            if (tempCurrencies.size() > costCurrencyPos) {
                                result.setCostCurrency(tempCurrencies.get(costCurrencyPos));
                                result.setCostCurrencyId(tempCurrencies.get(costCurrencyPos).getId());
                            }
                            List<ProductClass> tempProductClasses = databaseManager.getAllProductClass().blockingGet();
                            if (tempProductClasses.size() > productClassPos) {
                                result.setProductClass(tempProductClasses.get(productClassPos));
                            }
                            List<UnitCategory> tempUnitCategories = databaseManager.getAllUnitCategories().blockingSingle();
                            if (tempUnitCategories.size() > unitCategoryPos) {
                                List<Unit> units = tempUnitCategories.get(unitCategoryPos).getUnits();
                                if (units.size() > unitPos) {
                                    result.setMainUnit(units.get(unitPos));
                                    result.setMainUnitId(units.get(unitPos).getId());
                                }
                            }
                            result.setDescription(description);
                            databaseManager.addProduct(result).subscribe(id -> {
                                databaseManager.removeVendorProductConnectionByProductId(id).subscribe();
                                for (Long vendorId : vendors) {
                                    VendorProductCon connection = new VendorProductCon();
                                    connection.setVendorId(vendorId);
                                    connection.setProductId(id);
                                    databaseManager.addVendorProductConnection(connection).subscribe();
                                }
                                view.editProduct(result);
                                view.unselectProductsList();
                                openProduct(null);
                            });
                        }

                    }

                    @Override
                    public void onNegativeButtonClicked() {

                    }
                });
                break;
        }

        //TODO fill product class

    }

    @Override
    public void unitCategorySelected(int position) {
        List<UnitCategory> unitCategories = databaseManager.getAllUnitCategories().blockingSingle();
        if (unitCategories != null && !unitCategories.isEmpty()) {
            UnitCategory category = unitCategories.get(position);
            List<Unit> units = category.getUnits();
            if (units != null && !units.isEmpty()) {
                String[] result = new String[units.size()];
                for (int i = 0; i < result.length; i++) {
                    result[i] = units.get(i).getName();
                }
                view.setUnitsToProductsAddEdit(result);
            }
        }
    }

    @Override
    public void setVendorName(List<Long> vendors) {
        String result = "";
        for (Long id : vendors) {
            Vendor vendor = databaseManager.getVendorById(id).blockingSingle();
            if (vendor != null) {
                result += vendor.getName();
            }
            if (vendors.indexOf(id) != vendors.size() - 1) {
                result += ", ";
            }
        }
        view.setVendorNameToAddEditProductFragment(result);
    }

    @Override
    public void showActivesToggled() {
        view.initRightSide(getCategories());
        switch (mode) {
            case CATEGORY_ADD_MODE:
                openCategory(category);
                break;
            case CATEGORY_EDIT_MODE:
                if (!category.isActive())
                    category = null;
                openCategory(category);
                break;
            case SUBCATEGORY_ADD_MODE:
                if (!category.isActive()) {
                    openCategory(null);
                    return;
                }
                Category tempCategory = subcategory;
                openCategory(category);
                openSubcategory(tempCategory);
                break;
            case SUBCATEGORY_EDIT_MODE:
                if (!category.isActive()) {
                    category = null;
                    openCategory(category);
                } else {
                    Category tempSubcategory = subcategory;
                    openCategory(category);
                    if (!tempSubcategory.isActive()) {
                        openSubcategory(null);
                        return;
                    }
                    openSubcategory(tempSubcategory);
                }
                break;
            case PRODUCT_ADD_MODE:
                if (!category.isActive()) {
                    category = null;
                    openCategory(category);
                } else {
                    Category tempSubcategory = subcategory;
                    openCategory(category);
                    if (!tempSubcategory.isActive()) {
                        openSubcategory(null);
                        return;
                    }
                    openSubcategory(tempSubcategory);
                    openProduct(product);
                }
                break;
            case PRODUCT_EDIT_MODE:
                if (!category.isActive()) {
                    category = null;
                    openCategory(category);
                } else {
                    Category tempSubcategory = subcategory;
                    Product tempProduct = product;
                    openCategory(category);
                    if (!tempSubcategory.isActive()) {
                        openSubcategory(null);
                        return;
                    }
                    openSubcategory(tempSubcategory);
                    if (!tempProduct.isActive()) {
                        openProduct(null);
                    }
                    openProduct(tempProduct);
                }
                break;
        }
    }

    @Override
    public boolean backPressFinish() {
        switch (mode) {
            case CATEGORY_ADD_MODE:
                return true;
            case CATEGORY_EDIT_MODE:
            case SUBCATEGORY_ADD_MODE:
                openCategory(null);
                return false;
            case SUBCATEGORY_EDIT_MODE:
            case PRODUCT_ADD_MODE:
                openSubcategory(null);
                return false;
            case PRODUCT_EDIT_MODE:
                openProduct(null);
                return false;
            default:
                    return false;
        }
    }
}
