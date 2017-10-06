package com.jim.multipos.ui.product.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.product.presenter.ProductsPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 06.10.2017.
 */
@Module(includes = {
        ProductsPresenterModule.class
})
public abstract class AddProductFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(AddProductFragment productFragment);

    @Binds
    @PerFragment
    abstract ProductsView provideProductsView(AddProductFragment productFragment);
}
