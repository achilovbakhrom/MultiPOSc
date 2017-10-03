package com.jim.multipos.ui.main_menu.product_menu.di;

import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.main_menu.product_menu.ProductMenuActivity;
import com.jim.multipos.ui.main_menu.product_menu.presenters.ProductMenuPresenter;
import com.jim.multipos.ui.main_menu.product_menu.presenters.ProductMenuPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DEV on 08.08.2017.
 */
@Module
public class ProductMenuModule {
    private ProductMenuActivity activity;

    public ProductMenuModule(ProductMenuActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public ProductMenuActivity getActivity() {
        return activity;
    }


    @Provides
    @ActivityScope
    public ProductMenuPresenter getMenuProductPresenter() {
        return new ProductMenuPresenterImpl();
    }
}
