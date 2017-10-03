package com.jim.multipos.ui.product_class.di;

import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.product_class.ProductClassActivity;
import com.jim.multipos.ui.product_class.fragments.AddProductClassFragment;
import com.jim.multipos.ui.product_class.fragments.ProductClassListFragment;

import dagger.Subcomponent;

/**
 * Created by developer on 29.08.2017.
 */
@ActivityScope
@Subcomponent(modules = {ProductClassModule.class})
public interface ProductClassComponent {
    void inject(ProductClassActivity productClassActivity);
    void inject(AddProductClassFragment addProductClassFragment);
    void inject(ProductClassListFragment productClassListFragment);
}
