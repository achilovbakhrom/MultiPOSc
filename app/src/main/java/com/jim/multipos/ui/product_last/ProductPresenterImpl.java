package com.jim.multipos.ui.product_last;

import android.os.Bundle;
import android.util.Log;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.Vendor;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.ui.product_last.helpers.CategoryAddEditMode;
import com.jim.multipos.ui.product_last.helpers.FragmentType;

import java.util.ArrayList;
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
    private FragmentType type = FragmentType.CATEGORY;

    @Setter
    @Getter
    private Category category, subcategory;

    @Getter
    @Setter
    private Product product;

    private List<Currency> currencies;

    private List<ProductClass> productClasses;

    private List<Unit> units;

    DatabaseManager databaseManager;
    ProductView productView;

    @Inject
    ProductPresenterImpl(ProductView productView, DatabaseManager databaseManager) {
        super(productView);
        this.databaseManager = databaseManager;
        this.productView = productView;
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

    private List<Unit> getUnits() {
        if (units == null)
            units = databaseManager.getAllStaticUnits().blockingSingle();
        return units;
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
    public void onResume() {
        super.onResume();
        view.initRightSide(getCategories());
        view.initProductForm(provideUnitList(), provideCurrencyList(), provideCostCurrencyList(), provideProductClassList());
    }

    private String[] provideUnitList() {
        return null;
    }

    private String[] provideCurrencyList() {
        return null;
    }

    private String[] provideCostCurrencyList() {
        return null;
    }

    private String[] provideProductClassList() {
        return null;
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
        this.product = null;
        view.clearProductList();
        if (category == null) {
            this.category = null;
            this.subcategory = null;
            mode = CategoryAddEditMode.CATEGORY_ADD_MODE;
            view.openAddCategoryMode();
            view.clearSubcategoryList();
        }
        else {
            category.resetSubCategories();
            this.category = category;
            mode = CategoryAddEditMode.CATEGORY_EDIT_MODE;
            view.openEditCategoryMode(category.getName(), category.getDescription(), category.isActive());
            if (subcategory == null) {
                List<Category> list = new ArrayList<>();
                if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
                    list.addAll(category.getSubCategories());
                    Collections.sort(list, (o1, o2) -> o1.getPosition().compareTo(o2.getPosition()));
                    Collections.sort(list, (o1, o2) -> -((Boolean) o1.isActive()).compareTo(o2.isActive()));
                }
                list.add(0, null);
                view.setListToSubcategoryList(list);
            } else
                view.unselectSubcategoryList();
            subcategory = null;
        }
    }


    /**
     * subcategory selection processing
     * @param category - selected subcategory from view
     */
    @Override
    public void subcategorySelected(Category category) {
        if (category == null) {
            this.product = null;
            subcategory = null;
            mode = CategoryAddEditMode.SUBCATEGORY_ADD_MODE;
            view.openAddSubcategoryMode(this.category.getName());
        }
        else {
            category.resetProducts();
            subcategory = category;
            mode = CategoryAddEditMode.SUBCATEGORY_EDIT_MODE;
            view.setTypeToCategoryFragment(type);
            if (this.category != null) {
                view.openEditSubcategoryMode(category.getName(), category.getDescription(), category.isActive(), this.category.getName());
                if (product == null) {
                    List<Product> products = new ArrayList<>();
                    products.addAll(category.getProducts());
                    Collections.sort(products, (o1, o2) -> o1.getPosition().compareTo(o2.getPosition()));
                    Collections.sort(products, (o1, o2) -> ((Boolean) o1.isActive()).compareTo(o2.isActive()));
                    products.add(0, null);
                    view.setListToProducts(products);
                } else
                    view.unselectProductsList();
            }
        }
    }


    /**
     * product selection processing
     * @param product - selected product from view
     */
    @Override
    public void productSelected(Product product) {
        if (product == null) {

        }
        else {

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
        if ((mode == CategoryAddEditMode.CATEGORY_ADD_MODE || mode == CategoryAddEditMode.CATEGORY_EDIT_MODE)
                && !isCategoryNameUnique(name)) {
            view.suchCategoryNameExists(name);
            return;
        }

        if ((mode == CategoryAddEditMode.SUBCATEGORY_ADD_MODE || mode == CategoryAddEditMode.SUBCATEGORY_EDIT_MODE)
                && category != null && !isSubcategoryNameUnique(category.getName(), name)) {
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
            });
            view.openAddCategoryMode();
        }
        else if (mode == CategoryAddEditMode.SUBCATEGORY_ADD_MODE && category != null) { // adding subcategory
            result.setParentId(category.getId());
            result.setName(name);
            result.setDescription(description);
            result.setActive(isActive);
            databaseManager.addCategory(result).subscribe(id -> view.addToSubcategoryList(result));
            view.openAddSubcategoryMode(category.getName());
        } else if(mode == CategoryAddEditMode.CATEGORY_EDIT_MODE && category != null) { // edit category
            result.setId(category.getId());
            result.setName(name);
            result.setDescription(description);
            result.setActive(isActive);
            databaseManager.addCategory(result).subscribe(id -> {
                view.editCategory(result);
                Log.d("sss", "addCategory: " + id);
                category.refresh();
            });
        } else if (mode == CategoryAddEditMode.SUBCATEGORY_EDIT_MODE && subcategory != null) { // edit subcategory
            result.setId(subcategory.getId());
            result.setParentId(subcategory.getParentId());
            result.setName(name);
            result.setDescription(description);
            result.setActive(isActive);
            databaseManager.addCategory(result).subscribe(id -> {
                view.editSubcategory(result);
                Log.d("sss", "addCategory: " + id);
                subcategory.refresh();
            });
        }
    }


    /**
     * All categories from db added to first index null object
     * @return - all categories from db
     */
    private List<Category> getCategories() {
        List<Category> result = databaseManager.getAllCategories().blockingSingle();
        Collections.sort(result, (o1, o2) -> o1.getPosition().compareTo(o2.getPosition()));
        Collections.sort(result, (o1, o2) -> -((Boolean) o1.isActive()).compareTo(o2.isActive()));
        result.add(0, null);
        return result;
    }

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
     * deleting category
     */
    @Override
    public void deleteCategory() {
        if (category != null) {
            if (category.isActive()) {
                view.showCannotDeleteActiveItemDialog();
                return;
            }
            databaseManager.removeCategory(category).subscribe(isDeleted -> {
                Log.d("sss", "deleteCategory: ");
            });
        }
    }

    /**
     * creating or editing product
     * @param name - product name
     * @param price - product price
     * @param createdDate - product created date
     * @param barcode - product barcode
     * @param sku - product sku
     * @param photoPath - product photo path
     * @param isActive - product active state
     * @param costCurrency - product cost currency
     * @param priceCurrency - product price currency
     * @param productClass - product class
     * @param mainUnit - product main unit
     * @param subunits - product sub units
     * @param vendor - product vendor
     * @param description - product description
     */
    @Override
    public void addProduct(String name,
                           Double price,
                           Long createdDate,
                           String barcode,
                           String sku,
                           String photoPath,
                           boolean isActive,
                           Currency costCurrency,
                           Currency priceCurrency,
                           ProductClass productClass,
                           Unit mainUnit,
                           List<Unit> subunits,
                           Vendor vendor,
                           String description) {
        if (product == null) {}
        //TODO fill product class
        databaseManager.addProduct(product).subscribe(id -> {
            view.openProductAddMode();
            product = null;
        });
    }
}
