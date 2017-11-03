package com.jim.multipos.ui.product_last;

import android.os.Bundle;
import android.util.Log;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.product_last.helpers.AddingMode;
import com.jim.multipos.ui.product_last.helpers.FragmentType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private AddingMode mode = AddingMode.ADD;

    @Setter
    @Getter
    private FragmentType type = FragmentType.CATEGORY;

    @Setter
    @Getter
    private Category category, subcategory;

    @Getter
    @Setter
    private Product product;

    @Getter
    private Map<Long, Long> selectedItems;

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
        initSelectedItemsList(bundle);
    }

    private void initSelectedItemsList(Bundle bundle) {
        selectedItems = new HashMap();
        List<Category> categories = new ArrayList<>();
        categories.addAll(databaseManager.getAllCategories().blockingSingle());
        if (bundle == null) {
            for (Category category : categories)
                selectedItems.put(category.getId(), -1L);
        } else {
            for (Category category : categories)
                selectedItems.put(category.getId(), bundle.getLong(category.getId().toString()));
        }
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
    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (bundle != null) {
            for (Object key : selectedItems.keySet()) { bundle.putLong(key.toString(), selectedItems.get(key)); }
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
            mode = AddingMode.ADD;
            view.openAddCategoryMode();
            view.clearSubcategoryList();
        }
        else {
            category.resetSubCategories();
            this.category = category;
            mode = AddingMode.EDIT;
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
//            Long id = selectedItems.get(category.getId());
//            if (id == -1L) {
//                mode = AddingMode.ADD;
//                view.openAddSubcategoryMode(category.getName());
//                view.selectAddSubcategoryItem();
//            } else {
//                mode = AddingMode.EDIT;
//                view.selectSubcategoryListItem(id);
//                Category selectedSubcategory = getCategoryById(id);
//                Category parent = null;
//                if (selectedSubcategory != null) {
//                    parent = getCategoryById(selectedSubcategory.getParentId());
//                }
//                if (parent != null) {
//                    view.selectSubcategory(selectedItems.get(id));
//                    view.openEditSubcategoryMode(
//                            selectedSubcategory.getName(),
//                            selectedSubcategory.getDescription(),
//                            selectedSubcategory.isActive(),
//                            parent.getName()
//                    );
//                    this.category = selectedSubcategory;
//                }
//            }
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
            mode = AddingMode.ADD;
            view.openAddSubcategoryMode(this.category.getName());
//            if (this.category != null && !Objects.equals(this.category.getParentId(), Category.WITHOUT_PARENT)) {
//                Category parent = getCategoryById(this.category.getParentId());
//                if (parent != null) {
//                    selectedItems.put(parent.getId(), -1L);
//                }
//            } else if(this.category != null && Objects.equals(this.category.getParentId(), Category.WITHOUT_PARENT)) {
//                selectedItems.put(this.category.getId(), -1L);
//            }
        }
        else {
            category.resetProducts();
            subcategory = category;
            mode = AddingMode.EDIT;
            view.setTypeToCategoryFragment(type);
            if (this.category != null) {
                view.openEditSubcategoryMode(category.getName(), category.getDescription(), category.isActive(), this.category.getName());
                selectedItems.put(this.category.getId(), category.getId());
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
     * adding or editing category and subcategory
     * @param name - name of category or subcategory
     * @param description - description of category or subcategory
     * @param isActive - active state of category or subcategory
     */
    @Override
    public void addCategory(String name, String description, boolean isActive) {
        if ((mode == AddingMode.EDIT && !isCategoryNameUnique(name) && !name.equals(category.getName()) && category.getParentId().equals(Category.WITHOUT_PARENT)) ||
                (mode == AddingMode.ADD && category == null && !isCategoryNameUnique(name))) {
            view.suchCategoryNameExists(name);
            return;
        }
        if ((mode == AddingMode.EDIT && !category.getParentId().equals(Category.WITHOUT_PARENT) && !isSubcategoryNameUnique(getCategoryById(category.getParentId()).getName(), name)
                && !name.equals(category.getName()) && !category.getParentId().equals(Category.WITHOUT_PARENT)) ||
                mode == AddingMode.ADD && category != null && Objects.equals(category.getParentId(), Category.WITHOUT_PARENT) &&
                        !isSubcategoryNameUnique(getCategoryById(category.getId()).getName(), name)) {
            view.suchSubcategoryNameExists(name);
            return;
        }
        Category result = new Category();
        if (mode == AddingMode.ADD && category != null) { // adding subcategory
            result.setParentId(category.getId());
            result.setName(name);
            result.setDescription(description);
            result.setActive(isActive);
            databaseManager.addCategory(result).subscribe(id -> view.addToSubcategoryList(result));
            view.openAddSubcategoryMode(category.getName());
        } else if (mode == AddingMode.ADD && category == null) { // adding category
            result.setName(name);
            result.setDescription(description);
            result.setIsActive(isActive);
            databaseManager.addCategory(result).subscribe(id -> {
                view.addToCategoryList(result);
                selectedItems.put(id, -1L);
            });
            view.openAddCategoryMode();
        } else if (mode == AddingMode.EDIT && category != null && !category.getParentId().equals(Category.WITHOUT_PARENT)) { // edit subcategory
            result.setId(category.getId());
            result.setParentId(category.getParentId());
            result.setName(name);
            result.setDescription(description);
            result.setActive(isActive);
            databaseManager.addCategory(result).subscribe(id -> {
                view.editSubcategory(result);
                Log.d("sss", "addCategory: " + id);
            });
        } else if(mode == AddingMode.EDIT && category != null && category.getParentId().equals(Category.WITHOUT_PARENT)) { // edit category
            result.setId(category.getId());
            result.setName(name);
            result.setDescription(description);
            result.setActive(isActive);
            databaseManager.addCategory(result).subscribe(id -> {
                view.editCategory(result);
                Log.d("sss", "addCategory: " + id);
            });
        }
    }


    @Override
    public void addProduct(Product product) {
        //TODO next step
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
}
