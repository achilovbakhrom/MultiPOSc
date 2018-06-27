package com.jim.multipos.ui.product_last;

import android.os.Bundle;
import android.util.Log;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.db.model.unit.UnitCategory;
import com.jim.multipos.ui.product_last.helpers.CategoryAddEditMode;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;


/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

@PerActivity
public class ProductPresenterImpl extends BasePresenterImpl<ProductView> implements ProductPresenter {

    private final String CATEGORY_KEY = "CATEGORY_KEY", SUBCATEGORY_KEY = "SUBCATEGORY_KEY", PRODUCT_KEY = "PRODUCT_KEY", STATE_KEY = "STATE_KEY";

    private CategoryAddEditMode mode = CategoryAddEditMode.CATEGORY_ADD_MODE;

    private Category category, subcategory;

    private Product product;
    private ProductClass productClass;
    private DatabaseManager databaseManager;
    private StringBuilder searchBuilder;
    private boolean skuMode = true;
    private boolean nameMode = true;
    private boolean barcodeMode = true;
    private List<Product> productList;
    private String searchText = "";
    private boolean isNew = true;

    @Inject
    ProductPresenterImpl(ProductView productView, DatabaseManager databaseManager) {
        super(productView);
        this.databaseManager = databaseManager;
        productClass = null;
        searchBuilder = new StringBuilder();
        productList = new ArrayList<>();
    }

    @Override
    public CategoryAddEditMode getMode() {
        return mode;
    }

    @Override
    public void setMode(CategoryAddEditMode mode) {
        this.mode = mode;
    }

    @Override
    public Category getCategory() {
        return this.category;
    }

    @Override
    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        if (bundle != null) {
            category = (Category) bundle.getSerializable(CATEGORY_KEY);
            subcategory = (Category) bundle.getSerializable(SUBCATEGORY_KEY);
            isNew = bundle.getBoolean(STATE_KEY);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private List<Category> getCategories() {
        List<Category> result = null;
        if (!view.isActiveVisible())
            result = databaseManager.getActiveCategories().blockingSingle();
        else
            result = databaseManager.getAllCategories().blockingSingle();
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


    private List<ProductClass> provideProductClassList() {
        List<ProductClass> productClasses = databaseManager.getAllProductClass().blockingGet();
        List<ProductClass> sortedList = new ArrayList<>();
        if (productClasses != null && !productClasses.isEmpty()) {
            for (int i = 0; i < productClasses.size(); i++) {
                if (productClasses.get(i).getParentId() == null) {
                    sortedList.add(productClasses.get(i));
                    for (int j = productClasses.size() - 1; j >= 0; j--) {
                        if (productClasses.get(i).getId().equals(productClasses.get(j).getParentId())) {
                            sortedList.add(productClasses.get(j));
                        }
                    }
                }
            }
            return sortedList;
        }
        return productClasses;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isNew) {
            isNew = false;
            view.openCategoryFragment();
        }
    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (bundle != null) {
            if (category != null) {
                bundle.putSerializable(CATEGORY_KEY, category);
            }
            if (subcategory != null) {
                bundle.putSerializable(SUBCATEGORY_KEY, subcategory);
            }
            bundle.putBoolean(STATE_KEY, isNew);
        }
    }

    /**
     * category selection processing
     *
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
                        !view.getPrice().equals(0.0d) || view.getUnitCategorySelectedPos() != 0 ||
                        view.getUnitSelectedPos() != 0 || this.productClass != null ||
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
                if (!view.getProductName().equals(this.product.getName()) || !view.getBarCode().equals(this.product.getBarcode()) || !view.getSku().equals(this.product.getSku()) ||
                        (unitCategory != null && !unitCategory.getUnits().get(view.getUnitSelectedPos()).getId().equals(this.product.getMainUnitId())) ||
                        !view.getPhotoPath().equals(this.product.getPhotoPath()) || !view.getPrice().equals(this.product.getPrice()) ||
                        view.getProductIsActive() != this.product.getIsActive()) {
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
                } else if (this.productClass != null && this.product.getProductClass() != null) {
                    if (!this.productClass.getId().equals(this.product.getProductClass().getId()))
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
                    else openCategory(category);
                } else if ((this.productClass != null && this.product.getProductClass() == null) || (this.productClass == null && this.product.getProductClass() != null)) {
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
     *
     * @param product - selected product from view
     */
    @Override
    public void productSelected(Product product) {
        view.closeKeyboard();
        switch (mode) {
            case PRODUCT_ADD_MODE:
                if (!view.getProductName().equals("") || !view.getBarCode().equals("") || !view.getSku().equals("") ||
                        !view.getPrice().equals(0.0d) || view.getUnitCategorySelectedPos() != 0 ||
                        view.getUnitSelectedPos() != 0 || this.productClass != null ||
                        !view.getProductIsActive() || !view.getPhotoPath().equals("")) {
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

                if (!view.getProductName().equals(this.product.getName()) || !view.getBarCode().equals(this.product.getBarcode()) || !view.getSku().equals(this.product.getSku()) ||
                        (unitCategory != null && !unitCategory.getUnits().get(view.getUnitSelectedPos()).getId().equals(this.product.getMainUnitId())) ||
                        !view.getPhotoPath().equals(this.product.getPhotoPath()) || !view.getPrice().equals(this.product.getPrice()) ||
                        view.getProductIsActive() != this.product.getIsActive()) {
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
                } else if (this.productClass != null && this.product.getProductClass() != null) {
                    if (!this.productClass.getId().equals(this.product.getProductClass().getId()))
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
                    else openProduct(product);
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

    @Override
    public DatabaseManager getDatabaseManager() {
        return null;
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
        } else {
            this.category = category;
            this.category.resetSubCategories();
            view.setCategoryPath(this.category.getName());
            view.selectCategory(category.getId());
            mode = CategoryAddEditMode.CATEGORY_EDIT_MODE;
            view.openEditCategoryMode(category.getName(), category.getDescription(), category.isActive());

            List<Category> subCategories;
            if (view.isActiveVisible()) {
                subCategories = this.category.getAllSubCategories();
            } else {
                subCategories = this.category.getActiveSubCategories();
            }
            if (subCategories != null && !subCategories.isEmpty()) {
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
        } else {
            view.setSubcategoryPath(category.getName());
            this.subcategory = category;
            this.subcategory.resetProducts();
            mode = CategoryAddEditMode.SUBCATEGORY_EDIT_MODE;
            if (this.category != null) {
                view.openEditSubcategoryMode(category.getName(), category.getDescription(), category.isActive(), this.category.getName());
                view.selectSubcategory(subcategory.getId());
                List<Product> products;
                if (view.isActiveVisible()) {
                    products = category.getAllProducts();
                } else {
                    products = category.getActiveProducts();
                }
                products.add(0, null);
                view.setListToProducts(products);
            }
        }
    }

    public void openProduct(Product product) {
        if (product == null) {
            this.product = null;
            this.productClass = null;
            mode = CategoryAddEditMode.PRODUCT_ADD_MODE;
            view.unselectProductsList();
            view.openProductAddMode();
        } else {
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
                for (int i = 0; i < units.length; i++) {
                    units[i] = temp.getUnits().get(i).getName();
                }

            }
            String productClassPos = "";
            if (product.getProductClass() != null) {
                this.productClass = product.getProductClass();
                productClassPos = product.getProductClass().getName();
            }
            view.openProductEditMode(product.getName(),
                    product.getBarcode(),
                    product.getSku(),
                    product.isActive(),
                    databaseManager.getMainCurrency().getAbbr(),
                    productClassPos,
                    unitCategoryPos,
                    units,
                    unitPos,
                    product.getDescription(),
                    product.getPhotoPath(),
                    product.getPrice(),
                    product.getStockKeepType());
        }
    }


    /**
     * subcategory selection processing
     *
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
                } else openSubcategory(category);
                break;
            case PRODUCT_ADD_MODE:
                if (!view.getProductName().equals("") || !view.getBarCode().equals("") || !view.getSku().equals("") ||
                        !view.getPrice().equals(0.0d) || view.getUnitCategorySelectedPos() != 0 ||
                        view.getUnitSelectedPos() != 0 || this.productClass != null ||
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
                if (!view.getProductName().equals(this.product.getName()) || !view.getBarCode().equals(this.product.getBarcode()) || !view.getSku().equals(this.product.getSku()) ||
                        (unitCategory != null && !unitCategory.getUnits().get(view.getUnitSelectedPos()).getId().equals(this.product.getMainUnitId())) ||
                        !view.getPhotoPath().equals(this.product.getPhotoPath()) || !view.getPrice().equals(this.product.getPrice()) ||
                        view.getProductIsActive() != this.product.getIsActive() ) {
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
                } else if (this.productClass != null && this.product.getProductClass() != null) {
                    if (!this.productClass.getId().equals(this.product.getProductClass().getId()))
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
                    else openSubcategory(category);
                } else if ((this.productClass != null && this.product.getProductClass() == null) || (this.productClass == null && this.product.getProductClass() != null)) {
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
     *
     * @param name        - name of category or subcategory
     * @param description - description of category or subcategory
     * @param isActive    - active state of category or subcategory
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
                view.sendCategoryEvent(result, GlobalEventConstants.ADD);
                view.setCategoryPath(null);
                openCategory(null);
            });
        } else if (mode == CategoryAddEditMode.SUBCATEGORY_ADD_MODE && category != null) { // adding subcategory
            result.setParentId(category.getId());
            result.setName(name);
            result.setDescription(description);
            result.setActive(isActive);
            databaseManager.addCategory(result).subscribe(id -> {
                view.addToSubcategoryList(result);
                view.sendCategoryEvent(result, GlobalEventConstants.ADD);
                view.setSubcategoryPath(null);
                openSubcategory(null);
            });

        } else if (mode == CategoryAddEditMode.CATEGORY_EDIT_MODE && category != null) { // edit category
            view.showEditDialog(new UIUtils.AlertListener() {
                @Override
                public void onPositiveButtonClicked() {
                    category.setName(name);
                    category.setDescription(description);
                    category.setActive(isActive);
                    databaseManager.replaceCategory(category).subscribe(aLong -> {
                        view.editCategory(category);
                        category.refresh();
                        view.setCategoryPath(category.getName());
                        view.sendCategoryEvent(category, GlobalEventConstants.UPDATE);
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
                    subcategory.setName(name);
                    subcategory.setDescription(description);
                    subcategory.setActive(isActive);
                    databaseManager.replaceCategory(subcategory).subscribe(aLong -> {
                        view.editSubcategory(subcategory);
                        subcategory.refresh();
                        view.sendCategoryEvent(category, GlobalEventConstants.UPDATE);
                        category.resetSubCategories();
                        view.setSubcategoryPath(subcategory.getName());
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
     *
     * @return - all products from db
     */
    public List<Product> getProducts() {
        List<Product> result = databaseManager.getAllProducts().blockingSingle();
        Collections.sort(result, (o1, o2) -> -((Boolean) o1.isActive()).compareTo(o2.isActive()));
        result.add(0, null);
        return result;
    }

    /**
     * Check for uniqueness the given subcategory name
     *
     * @param categoryName    - parent's name
     * @param subcategoryName - given subcategory name
     * @return - true if subcategory name is unique, otherwise false
     */
    @Override
    public boolean isSubcategoryNameUnique(String categoryName, String subcategoryName) {
        return !databaseManager.isSubCategoryNameExists(categoryName, subcategoryName).blockingSingle();
    }

    /**
     * Check for uniqueness the given category name
     *
     * @param categoryName - give category name
     * @return - true if category name is unique, otherwise false
     */
    @Override
    public boolean isCategoryNameUnique(String categoryName) {
        return !databaseManager.isCategoryNameExists(categoryName).blockingSingle();
    }

    /**
     * Check for uniqueness the given product name
     *
     * @param productName - given product name
     * @return true if product name is unique, otherwise false
     */
    @Override
    public boolean isProductNameUnique(String productName, Long categoryId) {
        return !databaseManager.isProductNameExists(productName, categoryId).blockingSingle();
    }

    /**
     * Gets category by given id
     *
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
     *
     * @param category - given category
     * @return subcategories of given category
     */
    @Override
    public List<Category> getSubcategories(Category category) {
        List<Category> result = new ArrayList<>();
        if (view.isActiveVisible()) {
            result = category.getAllSubCategories();
        } else {
            result = category.getActiveSubCategories();
        }
        if (result != null && !result.isEmpty()) {
            Collections.sort(result, (o1, o2) -> -((Boolean) o1.isActive()).compareTo(o2.isActive()));
        }
        return result;
    }

    /**
     * product deletion
     */
    @Override
    public void deleteProduct() {
        if (product != null) {
                view.showDeleteDialog(new UIUtils.AlertListener() {
                    @Override
                    public void onPositiveButtonClicked() {
                        //TODO EDITABLE TO STATEABLE

                        product.setActive(false);
                        product.setDeleted(true);
//                        product.setNotModifyted(false);
                        databaseManager.replaceProduct(product).subscribe(aLong -> {
                            if (subcategory != null) {
                                subcategory.resetProducts();
                                view.sendProductEvent(GlobalEventConstants.DELETE, product);
                                openSubcategory(subcategory);
                                openProduct(null);
                            }
                        });
                    }

                    @Override
                    public void onNegativeButtonClicked() {

                    }
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
                    if (!subcategory.getActiveProducts().isEmpty()) {
                        view.showListMustBeEmptyDialog();
                        return;
                    }
                    view.showDeleteDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            //TODO EDITABLE TO STATEABLE

//                            subcategory.setIsNotModified(false);
                            subcategory.setActive(false);
                            subcategory.setDeleted(true);
                            databaseManager.replaceCategory(subcategory).subscribe(isDeleted -> {
                                view.sendCategoryEvent(category, GlobalEventConstants.DELETE);
                                Log.d("sss", "deleteCategory: ");
                                category.resetSubCategories();
                                openCategory(category);
                                openSubcategory(null);
                            });
                        }

                        @Override
                        public void onNegativeButtonClicked() {
                        }
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
                    if (!category.getActiveSubCategories().isEmpty()) {
                        view.showListMustBeEmptyDialog();
                        return;
                    }
                    view.showDeleteDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            //TODO EDITABLE TO STATEABLE

//                            category.setIsNotModified(false);
                            category.setActive(false);
                            category.setDeleted(true);
                            databaseManager.replaceCategory(category).subscribe(isDeleted -> {
                                Log.d("sss", "deleteCategory: ");
                                List<Category> categories;
                                if (!view.isActiveVisible()) {
                                    categories = databaseManager.getActiveCategories().blockingSingle();
                                } else {
                                    categories = databaseManager.getAllCategories().blockingSingle();
                                }
                                if (categories != null && !categories.isEmpty()) {
                                    Collections.sort(categories, (o1, o2) -> -((Boolean) o1.isActive()).compareTo(o2.isActive()));
                                }
                                categories.add(0, null);
                                view.sendCategoryEvent(category, GlobalEventConstants.DELETE);
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
     *  @param name             - product name
     * @param barcode           - product barcode
     * @param sku               - product sku
     * @param photoPath         - product photo path
     * @param isActive          - product active state
     * @param costCurrencyPos   - product cost currency
     * @param priceCurrencuyPos - product price currency
     * @param unitCategoryPos   - product main unit
     * @param unitPos           - product unit
     * @param description       - product description
     * @param resultPrice       - product price
     * @param typePosition      - product stock keeping type
     */

    @Override
    public void addProduct(String name, String barcode,
                           String sku, String photoPath, boolean isActive,
                           int costCurrencyPos, int priceCurrencuyPos, int unitCategoryPos,
                           int unitPos, String description, Double resultPrice, int typePosition) {

        switch (mode) {
            case PRODUCT_ADD_MODE:
                Product product = new Product();
                product.setName(name);
                product.setBarcode(barcode.replace(" ", ""));
                product.setSku(sku);
                product.setPhotoPath(photoPath);
                product.setIsActive(isActive);
                product.setPrice(resultPrice);
                product.setCategory(subcategory);
                product.setCategoryId(subcategory.getId());
                product.setCreatedDate(System.currentTimeMillis());
                product.setStockKeepType(typePosition);
                List<Currency> currencies = databaseManager.getAllCurrencies().blockingSingle();
                if (currencies != null && !currencies.isEmpty()) {
                    product.setPriceCurrency(currencies.get(priceCurrencuyPos));
                }
                product.setProductClass(productClass);
                List<UnitCategory> unitCategories = databaseManager.getAllUnitCategories().blockingSingle();
                if (unitCategories != null && !unitCategories.isEmpty()) {
                    UnitCategory unitCategory = unitCategories.get(unitCategoryPos);
                    if (unitCategory.getUnits() != null && !unitCategory.getUnits().isEmpty()) {
                        product.setMainUnit(unitCategory.getUnits().get(unitPos));
                    }
                }
                product.setDescription(description);
                databaseManager.addProduct(product).subscribe(aLong -> {
                    //TODO EDITABLE TO STATEABLE
                    databaseManager.replaceProduct(product).blockingSingle();
                    view.addToProductList(product);
                    view.sendProductEvent(GlobalEventConstants.ADD, product);
                    openProduct(null);
                });
                break;
            case PRODUCT_EDIT_MODE:
                view.showEditDialog(new UIUtils.AlertListener() {
                    @Override
                    public void onPositiveButtonClicked() {
                        if (ProductPresenterImpl.this.product != null) {
                            //TODO EDITABLE TO STATEABLE
                            ProductPresenterImpl.this.product.keepToHistory();
                            ProductPresenterImpl.this.product.setName(name);
                            ProductPresenterImpl.this.product.setActive(isActive);
                            ProductPresenterImpl.this.product.setCategory(subcategory);
                            ProductPresenterImpl.this.product.setCreatedDate(System.currentTimeMillis());
                            ProductPresenterImpl.this.product.setBarcode(barcode.replace(" ", ""));
                            ProductPresenterImpl.this.product.setPrice(resultPrice);
                            ProductPresenterImpl.this.product.setPhotoPath(photoPath);
                            ProductPresenterImpl.this.product.setSku(sku);
                            ProductPresenterImpl.this.product.setDescription(description);
                            ProductPresenterImpl.this.product.setProductClass(productClass);
                            List<Currency> tempCurrencies = databaseManager.getAllCurrencies().blockingSingle();
                            if (tempCurrencies.size() > priceCurrencuyPos) {
                                ProductPresenterImpl.this.product.setPriceCurrency(tempCurrencies.get(priceCurrencuyPos));
                                ProductPresenterImpl.this.product.setPriceCurrencyId(tempCurrencies.get(priceCurrencuyPos).getId());
                            }
                            List<UnitCategory> tempUnitCategories = databaseManager.getAllUnitCategories().blockingSingle();
                            if (tempUnitCategories.size() > unitCategoryPos) {
                                List<Unit> units = tempUnitCategories.get(unitCategoryPos).getUnits();
                                if (units.size() > unitPos) {
                                    ProductPresenterImpl.this.product.setMainUnit(units.get(unitPos));
                                    ProductPresenterImpl.this.product.setMainUnitId(units.get(unitPos).getId());
                                }
                            }
                            databaseManager.replaceProduct(ProductPresenterImpl.this.product).subscribe(aLong -> {
                                view.editProduct(ProductPresenterImpl.this.product);
//                                view.sendProductChangeEvent(GlobalEventConstants.UPDATE, ProductPresenterImpl.this.product, result);
                                openSubcategory(subcategory);
                                if (ProductPresenterImpl.this.product.getIsActive())
                                    openProduct(ProductPresenterImpl.this.product);
                                else openProduct(null);
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
                view.setUnitsToProductsAddEdit(result, 0);
            }
        }
    }

    @Override
    public void unitCategorySelectedWithPosition(int position, int unitPos) {
        List<UnitCategory> unitCategories = databaseManager.getAllUnitCategories().blockingSingle();
        if (unitCategories != null && !unitCategories.isEmpty()) {
            UnitCategory category = unitCategories.get(position);
            List<Unit> units = category.getUnits();
            if (units != null && !units.isEmpty()) {
                String[] result = new String[units.size()];
                for (int i = 0; i < result.length; i++) {
                    result[i] = units.get(i).getName();
                }
                view.setUnitsToProductsAddEdit(result, unitPos);
            }
        }
    }

    @Override
    public boolean isProductNameExists(String name) {
        if (mode == CategoryAddEditMode.PRODUCT_EDIT_MODE) {
            if (ProductPresenterImpl.this.product.getName().equals(name)) {
                return false;
            }
        }
        return databaseManager.isProductNameExists(name, subcategory.getId()).blockingSingle();
    }

    @Override
    public List<ProductClass> updateProductClass() {
        return provideProductClassList();
    }

    @Override
    public boolean isProductSkuExists(String sku) {
        if (mode == CategoryAddEditMode.PRODUCT_EDIT_MODE) {
            if (ProductPresenterImpl.this.product.getSku().equals(sku)) {
                return false;
            }
        }
        return databaseManager.isProductSkuExists(sku, subcategory.getId()).blockingGet();
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
                    } else openProduct(tempProduct);
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

    @Override
    public void setProductClass(ProductClass productClass) {
        this.productClass = productClass;
    }

    @Override
    public void initDataForProduct() {
        view.initProductForm(provideUnitCategoriesList(), provideUnitList(), provideProductClassList(), provideCurrencyName());
    }

    @Override
    public void initDataForList() {
        view.initRightSide(getCategories());
    }

    @Override
    public void finishActivity() {
        switch (mode) {
            case CATEGORY_ADD_MODE:
                if (!view.getName().equals("") || !view.getDescription().equals("") || !view.isActive()) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            view.finishActivity();
                        }

                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectAddCategoryItem();
                        }
                    });
                } else
                    view.finishActivity();
                break;
            case SUBCATEGORY_ADD_MODE:
                if (!view.getName().equals("") || !view.getDescription().equals("") || !view.isActive()) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            view.finishActivity();
                        }

                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectAddSubcategoryItem();
                        }
                    });
                } else
                    view.finishActivity();
                break;
            case CATEGORY_EDIT_MODE:
                if (!view.getName().equals(this.category.getName()) || !view.getDescription().equals(this.category.getDescription()) || this.category.getIsActive() != view.isActive()) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            view.finishActivity();
                        }

                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectCategory(ProductPresenterImpl.this.category.getId());
                        }
                    });
                } else
                    view.finishActivity();
                break;
            case SUBCATEGORY_EDIT_MODE:
                if (!view.getName().equals(subcategory.getName()) || !view.getDescription().equals(subcategory.getDescription()) || subcategory.getIsActive() != view.isActive()) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            view.finishActivity();
                        }

                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectSubcategory(ProductPresenterImpl.this.subcategory.getId());
                            view.selectCategory(ProductPresenterImpl.this.category.getId());
                        }
                    });
                } else
                    view.finishActivity();
                break;
            case PRODUCT_ADD_MODE:
                if (!view.getProductName().equals("") || !view.getBarCode().equals("") || !view.getSku().equals("") ||
                        !view.getPrice().equals(0.0d) || view.getUnitCategorySelectedPos() != 0 ||
                        view.getUnitSelectedPos() != 0 || this.productClass != null ||
                        !view.getProductIsActive() || !view.getPhotoPath().equals("")) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            view.finishActivity();
                        }

                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectAddProductListItem();
                            mode = CategoryAddEditMode.PRODUCT_ADD_MODE;
                        }
                    });
                } else {
                    view.finishActivity();
                }
                break;
            case PRODUCT_EDIT_MODE:
                if (this.product == null) return;
                UnitCategory unitCategory = null;
                List<UnitCategory> tempUnitCategories = databaseManager.getAllUnitCategories().blockingSingle();
                if (tempUnitCategories.size() > view.getUnitCategorySelectedPos())
                    unitCategory = tempUnitCategories.get(view.getUnitCategorySelectedPos());

                if (!view.getProductName().equals(this.product.getName()) || !view.getBarCode().equals(this.product.getBarcode()) || !view.getSku().equals(this.product.getSku()) ||
                        (unitCategory != null && !unitCategory.getUnits().get(view.getUnitSelectedPos()).getId().equals(this.product.getMainUnitId())) ||
                        !view.getPhotoPath().equals(this.product.getPhotoPath()) || !view.getPrice().equals(this.product.getPrice()) ||
                        view.getProductIsActive() != this.product.getIsActive()) {
                    view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                        @Override
                        public void onPositiveButtonClicked() {
                            view.finishActivity();
                        }

                        @Override
                        public void onNegativeButtonClicked() {
                            view.selectProductListItem(ProductPresenterImpl.this.product.getId());
                            mode = CategoryAddEditMode.PRODUCT_EDIT_MODE;
                        }
                    });
                } else if (this.productClass != null && this.product.getProductClass() != null) {
                    if (!this.productClass.getId().equals(this.product.getProductClass().getId()))
                        view.showDiscardChangesDialog(new UIUtils.AlertListener() {
                            @Override
                            public void onPositiveButtonClicked() {
                                view.finishActivity();
                            }

                            @Override
                            public void onNegativeButtonClicked() {
                                view.selectProductListItem(ProductPresenterImpl.this.product.getId());
                                mode = CategoryAddEditMode.PRODUCT_EDIT_MODE;
                            }
                        });
                    else view.finishActivity();
                } else {
                    view.finishActivity();
                }
                break;
        }
    }

    @Override
    public void setSkuSearchMode(boolean active) {
        skuMode = active;
        onSearchTextChange(searchText);
        onModeChange();
    }

    @Override
    public void setSearchProduct(Product product) {
        Category subCategory = product.getCategory();
        this.category = subCategory.getParentCategory();
        this.category.resetSubCategories();
        view.setCategoryPath(this.category.getName());
        view.selectCategory(category.getId());
        List<Category> subCategories;
        if (view.isActiveVisible()) {
            subCategories = this.category.getAllSubCategories();
        } else {
            subCategories = this.category.getActiveSubCategories();
        }
        if (subCategories != null && !subCategories.isEmpty()) {
            Collections.sort(subCategories, (o1, o2) -> -((Boolean) o1.isActive()).compareTo(o2.isActive()));
        }
        subCategories.add(0, null);
        view.setListToSubcategoryList(subCategories);
        view.setSubcategoryPath(category.getName());
        this.subcategory = subCategory;
        this.subcategory.resetProducts();
        if (this.category != null) {
            view.selectSubcategory(subcategory.getId());
            List<Product> products;
            if (view.isActiveVisible()) {
                products = subcategory.getAllProducts();
            } else {
                products = subcategory.getActiveProducts();
            }
            if (products != null && !products.isEmpty()) {
                Collections.sort(products, (o1, o2) -> -((Boolean) o1.isActive()).compareTo(o2.isActive()));
            }
            products.add(0, null);
            view.setListToProducts(products);
            openProduct(product);
        }
    }

    @Override
    public void setNameSearchMode(boolean active) {
        nameMode = active;
        onSearchTextChange(searchText);
        onModeChange();

    }

    @Override
    public void setBarcodeSearchMode(boolean active) {
        barcodeMode = active;
        onSearchTextChange(searchText);
        onModeChange();

    }

    @Override
    public void onSearchTextChange(String s) {
        searchText = s;
        if (s.length() == 1) {
            databaseManager.getSearchProducts(s, skuMode, barcodeMode, nameMode).subscribe((products, throwable) -> {
                productList = products;
                view.setResultsList(productList, s);
            });
        } else if (s.length() > 0) {
            List<Product> productsTemp = new ArrayList<>();
            for (Product product : productList) {
                if (barcodeMode && product.getBarcode().toUpperCase().contains(s.toUpperCase())) {
                    productsTemp.add(product);
                } else if (nameMode && product.getName().toUpperCase().contains(s.toUpperCase())) {
                    productsTemp.add(product);
                } else if (skuMode && product.getSku().toUpperCase().contains(s.toUpperCase())) {
                    productsTemp.add(product);
                }
            }
            view.setResultsList(productsTemp, s);
        }
    }


    @Override
    public void onOkPressed() {
        view.addProductToOrderInCloseSelf();
    }

    private void onModeChange() {
        if (searchText.length() > 0)
            databaseManager.getSearchProducts(searchText.substring(0, 1), skuMode, barcodeMode, nameMode).subscribe((products, throwable) -> {
                productList = products;
                List<Product> productsTemp = new ArrayList<>();

                for (Product product : productList) {
                    if (barcodeMode && product.getBarcode().toUpperCase().contains(searchText.toUpperCase())) {
                        productsTemp.add(product);
                    } else if (nameMode && product.getName().toUpperCase().contains(searchText.toUpperCase())) {
                        productsTemp.add(product);
                    } else if (skuMode && product.getSku().toUpperCase().contains(searchText.toUpperCase())) {
                        productsTemp.add(product);
                    }
                }
                view.setResultsList(productsTemp, searchText);

            });
    }
}
