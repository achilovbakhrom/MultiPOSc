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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

@PerActivity
public class ProductPresenterImpl extends BasePresenterImpl<ProductView> implements ProductPresenter {

    @Setter
    @Getter
    private AddingMode mode = AddingMode.ADD;

    @Setter
    @Getter
    private FragmentType type = FragmentType.CATEGORY;

    @Setter
    @Getter
    private Category category;

    @Getter
    private Map<Long, Integer> selectedPositions;


    DatabaseManager databaseManager;
    ProductView productView;
    @Inject
    protected ProductPresenterImpl(ProductView productView, DatabaseManager databaseManager) {
        super(productView);
        this.databaseManager = databaseManager;
        this.productView = productView;
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        selectedPositions = new HashMap();
        List<Category> categories = new ArrayList<>();
        categories.addAll(getCategories());
        if (bundle == null) {
            for (Category category : categories) {
                if (category == null) continue;
                selectedPositions.put(category.getId(), 0);
            }
        } else {
            for (Category category : categories) {
                if (category == null) continue;
                selectedPositions.put(category.getId(), bundle.getInt(category.getId().toString()));
            }
        }
    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle bundle) {
        super.onSaveInstanceState(bundle);
        for (Object key : selectedPositions.keySet()) {
            bundle.putInt(key.toString(), selectedPositions.get(key));
        }
    }

    @Override
    public void categorySelected(Category category) {
        if (category == null) {
            this.category = null;
            mode = AddingMode.ADD;
            type = FragmentType.CATEGORY;
            view.setTypeToCategoryFragment(type);
            view.openCategoryAddEditFragment(mode, type, category);
            view.clearSubcategoryList();
        }
        else {
            this.category = category;
            mode = AddingMode.EDIT;
            type = FragmentType.CATEGORY;
            view.setTypeToCategoryFragment(type);
            view.openCategoryAddEditFragment(mode, type, category);
            List<Category> list = new ArrayList<>();
            list.add(null);
            if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
                list.addAll(category.getSubCategories());
            }
            view.setListToSubcategoryList(list);
            view.selectSubcategoryListItem(selectedPositions.get(category.getId()));
        }
    }

    @Override
    public void subcategorySelected(Category category) {
        if (category == null) {
            mode = AddingMode.ADD;
            type = FragmentType.SUBCATEGORY;
            view.setTypeToCategoryFragment(type);
            view.openCategoryAddEditFragment(mode, type, category);
        }
        else {
            this.category = category;
            mode = AddingMode.EDIT;
            type = FragmentType.SUBCATEGORY;
            view.setTypeToCategoryFragment(type);
            view.openCategoryAddEditFragment(mode, type, category);
        }
    }

    @Override
    public void addCategory(String name, String description, boolean isActive) {

        if ((mode == AddingMode.EDIT && !isCategoryNameUnique(name) && !name.equals(category.getName()) && category.getParentId().equals(Category.WITHOUT_PARENT)) ||
                (mode == AddingMode.ADD && category == null && !isCategoryNameUnique(name))) {
            view.suchCategoryNameExists(name);
            return;
        }

        if ((mode == AddingMode.EDIT && !category.getParentId().equals(Category.WITHOUT_PARENT) && !isSubcategoryNameUnique(getCategoryById(category.getParentId()).getName(), name)
                && !name.equals(category.getName()) && !category.getParentId().equals(Category.WITHOUT_PARENT)) ||
                mode == AddingMode.ADD && category != null && !isSubcategoryNameUnique(getCategoryById(category.getParentId()).getName(), name)) {
            view.suchSubcategoryNameExists(name);
            return;
        }

        Category result = new Category();
        if (mode == AddingMode.ADD && category != null) { // adding subcategory
            result.setParentId(category.getId());
            result.setName(name);
            result.setDescription(description);
            result.setActive(isActive);
            databaseManager.addSubCategory(result).subscribe(id -> view.addToSubcategoryList(result));
        } else if (mode == AddingMode.ADD && category == null) { // adding category
            result.setName(name);
            result.setDescription(description);
            result.setIsActive(isActive);
            databaseManager.addCategory(result).subscribe(id -> view.addToCategoryList(result));
            view.openCategoryAddEditFragment(AddingMode.ADD, FragmentType.CATEGORY, null);
        } else if (mode == AddingMode.EDIT && category != null && !category.getParentId().equals(Category.WITHOUT_PARENT)) { // edit subcategory
            result.setId(category.getId());
            result.setParentId(category.getParentId());
            result.setName(name);
            result.setDescription(description);
            result.setActive(isActive);
            databaseManager.addSubCategory(result).subscribe(id -> {
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

    @Override
    public List<Category> getCategories() {
        List<Category> result = databaseManager.getAllCategories().blockingSingle();
        result = result == null ? new ArrayList<>() : result;
        result.add(0, null);
        return result;
    }

    @Override
    public List<Product> getProducts() {
        //TODO next step
        return null;
    }

    @Override
    public boolean isSubcategoryNameUnique(String categoryName, String subcategoryName) {
        return !databaseManager.isSubCategoryNameExists(categoryName, subcategoryName).blockingSingle();
    }

    @Override
    public boolean isCategoryNameUnique(String categoryName) {
        return !databaseManager.isCategoryNameExists(categoryName).blockingSingle();
    }

    @Override
    public void openAddEditCategoryFragment(AddingMode mode, FragmentType type, Category category) {

    }

    @Override
    public void openAddEditProductFragment(AddingMode mode, Product product) {
        //TODO next step
    }

    @Override
    public Category getCategoryById(Long id) {
        return databaseManager
                .getCategoryById(id)
                .blockingSingle();
    }

    @Override
    public List<Category> getSubcategories(Category category) {
        List<Category> result = new ArrayList<>();
            result.add(null);
        if (category.getSubCategories() != null) {
            result.addAll(category.getSubCategories());
        }
        return result;
    }

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
