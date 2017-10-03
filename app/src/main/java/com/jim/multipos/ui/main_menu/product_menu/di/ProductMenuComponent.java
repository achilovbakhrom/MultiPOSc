package com.jim.multipos.ui.main_menu.product_menu.di;

import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.main_menu.product_menu.ProductMenuActivity;

import dagger.Subcomponent;

/**
 * Created by DEV on 08.08.2017.
 */
@ActivityScope
@Subcomponent(modules = {ProductMenuModule.class})
public interface ProductMenuComponent {
    void inject(ProductMenuActivity productMenuActivity);
}
