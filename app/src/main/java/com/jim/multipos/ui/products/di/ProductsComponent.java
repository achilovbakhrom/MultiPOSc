package com.jim.multipos.ui.products.di;

import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.products.fragments.AddMatrixAttributesFragment;
import com.jim.multipos.ui.products.fragments.AdvancedOptionFragment;
import com.jim.multipos.ui.products.fragments.GlobalVendorsFragment;
import com.jim.multipos.ui.products.ProductsActivity;
import com.jim.multipos.ui.products.fragments.AddCategoryFragment;
import com.jim.multipos.ui.products.fragments.AddProductFragment;
import com.jim.multipos.ui.products.fragments.AddSubCategoryFragment;
import com.jim.multipos.ui.products.fragments.MatrixDialogFragment;
import com.jim.multipos.ui.products.fragments.ProductsListFragment;

import dagger.Subcomponent;

/**
 * Created by DEV on 09.08.2017.
 */
@ActivityScope
@Subcomponent(modules = {ProductsModule.class})
public interface ProductsComponent {
    void inject(ProductsActivity productsActivity);
    void inject(AddCategoryFragment addCategoryFragment);
    void inject(AddSubCategoryFragment addSubCategory);
    void inject(ProductsListFragment productsListFragment);
    void inject(AddProductFragment addProductFragment);
    void inject(GlobalVendorsFragment globalVendorsFragment);
    void inject(AdvancedOptionFragment advancedOptionFragment);
    void inject(MatrixDialogFragment matrixDialogFragment);
    void inject(AddMatrixAttributesFragment addMatrixAttributesFragment);
}
