package com.jim.multipos.ui.main_menu.product_menu.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.main_menu.product_menu.ProductMenuActivity;
import com.jim.multipos.ui.main_menu.product_menu.ProductMenuView;
import com.jim.multipos.ui.main_menu.product_menu.presenters.ProductMenuPresenter;
import com.jim.multipos.ui.main_menu.product_menu.presenters.ProductMenuPresenterImpl;

import dagger.Binds;
import dagger.Module;

/**
 * Created by bakhrom on 10/6/17.
 */

@Module(includes = BaseActivityModule.class)
public abstract class ProductMenuModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideProductMenuActivity(ProductMenuActivity productMenuActivity);

    @Binds
    @PerActivity
    abstract ProductMenuPresenter provideProductMenuPresenter(ProductMenuPresenterImpl productMenuPresenter);

    @Binds
    @PerActivity
    abstract ProductMenuView provideProductMenuViewView(ProductMenuActivity productMenuActivity);

}
