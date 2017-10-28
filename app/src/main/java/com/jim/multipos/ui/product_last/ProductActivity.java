package com.jim.multipos.ui.product_last;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.product_last.fragment.CategoryAddEditFragment;
import com.jim.multipos.ui.product_last.fragment.ProductListFragment;
import com.jim.multipos.ui.product_last.fragment.ProductAddEditFragment;
import com.jim.multipos.ui.product_last.helpers.AddingMode;
import com.jim.multipos.ui.product_last.helpers.FragmentType;
import com.jim.multipos.utils.UIUtils;

import java.util.List;

import javax.inject.Inject;

import lombok.Getter;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

public class ProductActivity extends DoubleSideActivity implements ProductView {

    public static final String CATEGORY_FRAGMENT = CategoryAddEditFragment.class.getName();
    public static final String PRODUCT_FRAGMENT = ProductAddEditFragment.class.getName();

    @Inject
    @Getter
    ProductPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.onCreateView(savedInstanceState);
        if (activityFragmentManager.getFragments().isEmpty()) {
            prepareLeftSide();
            prepareRightSide();
        }

    }


    @Override
    public void addProductAddEditFragment() {
        addFragmentWithTagToLeft(new CategoryAddEditFragment(), PRODUCT_FRAGMENT);
    }

    @Override
    public void addCategoryAddEditFragment() {
        addFragmentWithTagToLeft(new CategoryAddEditFragment(), CATEGORY_FRAGMENT);
    }

    @Override
    public void addCategoryListFragment() {
        addFragmentToRight(new ProductListFragment());
    }

    private void prepareLeftSide() {
//        addProductAddEditFragment();
        addCategoryAddEditFragment();
    }

    private void prepareRightSide() {
        addCategoryListFragment();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public void setModeToProductAddEditFragment(AddingMode mode) {

    }

    @Override
    public void setTypeToCategoryFragment(FragmentType type) {
        //TODO
    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }

    @Override
    public void openProductAddEditFragment(AddingMode mode, Product product) {

    }

    @Override
    public void openCategoryAddEditFragment(AddingMode mode, FragmentType type, Category category) {
        ProductAddEditFragment productAddEditFragment = (ProductAddEditFragment) activityFragmentManager.findFragmentByTag(PRODUCT_FRAGMENT);
        if (productAddEditFragment != null) {
            activityFragmentManager.beginTransaction().show(productAddEditFragment).commit();
        } else {
            Log.d("sss", "openCategoryAddEditFragment: product fragment is null, maybe not added");
        }
        CategoryAddEditFragment categoryAddEditFragment = (CategoryAddEditFragment) activityFragmentManager.findFragmentByTag(CATEGORY_FRAGMENT);
        if (categoryAddEditFragment != null) {
            activityFragmentManager.beginTransaction().show(categoryAddEditFragment).commit();
            categoryAddEditFragment.setFragmentType(type);
            categoryAddEditFragment.setMode(mode, category);
        } else {
            Log.d("sss", "openCategoryAddEditFragment: category fragment is null, maybe not added");
        }
    }

    @Override
    public void addToCategoryList(Category category) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.addToCategoryList(category);
        }
    }

    @Override
    public void addToSubcategoryList(Category category) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.addToSubcategoryLIst(category);
        }
    }


    @Override
    public void clearSubcategoryList() {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.clearSubcategoryList();
        }
    }

    @Override
    public void setListToSubcategoryList(List<Category> categories) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.setListToSubcategoryList(categories);
        }
    }

    @Override
    public void setListToCategoryList(List<Category> categories) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.setListToCategoryList(categories);
        }
    }

    @Override
    public void suchCategoryNameExists(String name) {
        UIUtils.showAlert(this, getString(R.string.ok), getString(R.string.warning_such_category_name_exists),
                getString(R.string.warning_such_category_name_exists_message) + name, () -> {
                    Log.d("sss", "suchCategoryNameExists: user accepted existing name warning");
                });
    }

    @Override
    public void suchSubcategoryNameExists(String name) {
        UIUtils.showAlert(this, getString(R.string.ok), getString(R.string.warning_such_subcategory_name_exists_title),
                getString(R.string.warning_subcategory_name_exists) + name, () -> {
                    Log.d("sss", "suchSubcategoryNameExists: user accepted existing name warning");
                });
    }

    @Override
    public void editCategory(Category category) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.editCartegoryItem(category);
        }
    }

    @Override
    public void editSubcategory(Category category) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.editSubcategoryItem(category);
        }
    }

    @Override
    public void showCannotDeleteActiveItemDialog() {
        UIUtils.showAlert(this, getString(R.string.ok), getString(R.string.warning_deletion_of_active_elements),
                getString(R.string.cannot_delete_active_item), () -> Log.d("sss", "onButtonClicked: "));
    }

    @Override
    public void selectSubcategoryListItem(int position) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.selectSubcategoryListItem(position);
        }
    }
}
