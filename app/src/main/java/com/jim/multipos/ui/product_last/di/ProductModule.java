package com.jim.multipos.ui.product_last.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.product_last.ProductActivity;
import com.jim.multipos.ui.product_last.ProductPresenter;
import com.jim.multipos.ui.product_last.ProductPresenterImpl;
import com.jim.multipos.ui.product_last.ProductView;

import dagger.Binds;
import dagger.Module;

/**
 * Created by DEV on 09.08.2017.
 */
@Module(includes = BaseActivityModule.class)
public abstract class ProductModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideAppCompatAcitivity(ProductActivity productActivity);

    @Binds
    @PerActivity
    abstract ProductView provideProductView(ProductActivity productActivity);

    @Binds
    @PerActivity
    abstract ProductPresenter provideProductPresenter(ProductPresenterImpl presenterImplementation);

}
