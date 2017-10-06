package com.jim.multipos.ui.products;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.di.BaseAppComponent;
import com.jim.multipos.ui.HasComponent;
import com.jim.multipos.ui.products.di.ProductsComponent;
import com.jim.multipos.ui.products.di.ProductsModule;
import com.jim.multipos.ui.products.fragments.AddCategoryFragment;
import com.jim.multipos.ui.products.fragments.AddProductFragment;
import com.jim.multipos.ui.products.fragments.AddSubCategoryFragment;
import com.jim.multipos.ui.products.fragments.AdvancedOptionFragment;
import com.jim.multipos.ui.products.fragments.GlobalVendorsFragment;
import com.jim.multipos.ui.products.fragments.ProductsListFragment;
import com.jim.multipos.utils.managers.PosFragmentManager;

import javax.inject.Inject;

import static com.jim.multipos.utils.BundleConstants.CATEGORY_EDIT;
import static com.jim.multipos.utils.BundleConstants.PRODUCT_EDIT;
import static com.jim.multipos.utils.BundleConstants.SUBCATEGORY_EDIT;

/**
 * Created by DEV on 09.08.2017.
 */

public class ProductsActivity extends AppCompatActivity { //BaseActivity implements HasComponent<ProductsComponent>, ProductsActivityView {

    @Inject
    PosFragmentManager posFragmentManager;
    private ProductsComponent productsComponent;

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.products_layout);
//        posFragmentManager.replaceFragmentWithTag(new AddCategoryFragment(), R.id.flLeftContainer, CATEGORY_EDIT);
//        posFragmentManager.displayFragmentWithoutBackStack(new ProductsListFragment(), R.id.flRightContainer);
//    }

//    @Override
    public ProductsComponent getComponent() {
        return productsComponent;
    }

    protected void setupComponent(BaseAppComponent baseAppComponent) {
//        productsComponent = baseAppComponent.plus(new ProductsModule(this));
        productsComponent.inject(this);
    }


//    @Override
    public void openCategory() {
        AddCategoryFragment fragment = (AddCategoryFragment) posFragmentManager.findFragmentbyTag(new AddCategoryFragment(), CATEGORY_EDIT);
        if (fragment != null && fragment.isVisible()) {
            fragment.setData();
        } else if (fragment != null && !fragment.isVisible()) {
            posFragmentManager.replaceFragmentWithTag(fragment, R.id.flLeftContainer, CATEGORY_EDIT);
        } else {
            fragment = new AddCategoryFragment();
            posFragmentManager.replaceFragmentWithTag(fragment, R.id.flLeftContainer, CATEGORY_EDIT);
        }
    }

//    @Override
    public void openSubCategory() {
        AddSubCategoryFragment fragment = (AddSubCategoryFragment) posFragmentManager.findFragmentbyTag(new AddSubCategoryFragment(), SUBCATEGORY_EDIT);
        if (fragment != null && fragment.isVisible()) {
            fragment.setData();
        } else if (fragment != null && !fragment.isVisible()) {
            posFragmentManager.replaceFragmentWithTag(fragment, R.id.flLeftContainer, SUBCATEGORY_EDIT);
        } else {
            fragment = new AddSubCategoryFragment();
            posFragmentManager.replaceFragmentWithTag(fragment, R.id.flLeftContainer, SUBCATEGORY_EDIT);
        }


    }
//    @Override
    public void openProduct() {
        AddProductFragment fragment = (AddProductFragment) posFragmentManager.findFragmentbyTag(new AddProductFragment(), PRODUCT_EDIT);
        if (fragment != null && fragment.isVisible()) {
            fragment.setData();
        } else if (fragment != null && !fragment.isVisible()) {
            posFragmentManager.replaceFragmentWithTag(fragment, R.id.flLeftContainer, PRODUCT_EDIT);
        } else {
            fragment = new AddProductFragment();
            posFragmentManager.replaceFragmentWithTag(fragment, R.id.flLeftContainer, PRODUCT_EDIT);
        }
    }

//    @Override
    public void openList() {
        posFragmentManager.displayFragmentWithoutBackStack(new ProductsListFragment(), R.id.flRightContainer);
    }

//    @Override
    public void openAdvancedOptions() {
        posFragmentManager.popBackStack();
        posFragmentManager.displayFragment(new AdvancedOptionFragment(), R.id.flRightContainer);
    }

//    @Override
    public void openGlobalVendors() {
        posFragmentManager.displayFragment(new GlobalVendorsFragment(), R.id.flRightContainer);
    }
}
