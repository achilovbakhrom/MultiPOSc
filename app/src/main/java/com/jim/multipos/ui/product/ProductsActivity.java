package com.jim.multipos.ui.product;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.jim.multipos.R;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.ui.product.view.AddCategoryFragment;
import com.jim.multipos.ui.product.view.AddProductFragment;
import com.jim.multipos.ui.product.view.AddSubCategoryFragment;
import com.jim.multipos.ui.product.view.AdvancedOptionFragment;
import com.jim.multipos.ui.product.view.ProductsListFragment;

import static com.jim.mpviews.MpToolbar.DEFAULT_TYPE;

/**
 * Created by DEV on 09.08.2017.
 */

public class ProductsActivity extends DoubleSideActivity implements ProductsActivityView {

    public static final String SUBCATEGORY_EDIT = "the_subcategory_is_edited";
    public static final String CATEGORY_EDIT = "the_category_is_edited";
    public static final String PRODUCT_EDIT = "the_product_is_edited";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openList();
    }

    @Override
    protected int getToolbarMode() {
        return DEFAULT_TYPE;
    }

    public void openList(){
        replaceFragmentWithTag(new AddCategoryFragment(), R.id.flLeftContainer, CATEGORY_EDIT);
        addFragment(R.id.flRightContainer, new ProductsListFragment());
    }

    @Override
    public void openCategory() {
        AddCategoryFragment fragment = (AddCategoryFragment) activityFragmentManager.findFragmentByTag(CATEGORY_EDIT);
        if (fragment != null && fragment.isVisible()) {
            fragment.setData();
        } else if (fragment != null && !fragment.isVisible()) {
            replaceFragmentWithTag(fragment, R.id.flLeftContainer, CATEGORY_EDIT);
        } else {
            fragment = new AddCategoryFragment();
            replaceFragmentWithTag(fragment, R.id.flLeftContainer, CATEGORY_EDIT);
        }
    }

    @Override
    public void openSubCategory() {
        AddSubCategoryFragment fragment = (AddSubCategoryFragment) activityFragmentManager.findFragmentByTag(SUBCATEGORY_EDIT);
        if (fragment != null && fragment.isVisible()) {
            fragment.setData();
        } else if (fragment != null && !fragment.isVisible()) {
            replaceFragmentWithTag(fragment, R.id.flLeftContainer, SUBCATEGORY_EDIT);
        } else {
            fragment = new AddSubCategoryFragment();
            replaceFragmentWithTag(fragment, R.id.flLeftContainer, SUBCATEGORY_EDIT);
        }


    }
    @Override
    public void openProduct() {
        AddProductFragment fragment = (AddProductFragment) activityFragmentManager.findFragmentByTag(PRODUCT_EDIT);
        if (fragment != null && fragment.isVisible()) {
            fragment.setData();
        } else if (fragment != null && !fragment.isVisible()) {
            replaceFragmentWithTag(fragment, R.id.flLeftContainer, PRODUCT_EDIT);
        } else {
            fragment = new AddProductFragment();
            replaceFragmentWithTag(fragment, R.id.flLeftContainer, PRODUCT_EDIT);
        }
    }

    @Override
    public void openAdvancedOptions() {
        activityFragmentManager.popBackStack();
        addFragmentToTopRight(new AdvancedOptionFragment());
    }

    protected final void replaceFragmentWithTag(Fragment fragment, @IdRes int resId, String tag){
        activityFragmentManager
                .beginTransaction()
                .replace(resId, fragment, tag)
                .commit();
    }
}
