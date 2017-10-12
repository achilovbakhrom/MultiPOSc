package com.jim.multipos.ui.product_class.presenters;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.product_class.fragments.ProductClassListFragment;
import com.jim.multipos.ui.product_class.fragments.ProductClassListPresenterModule;
import com.jim.multipos.ui.product_class.fragments.ProductClassListView;
import com.jim.multipos.ui.signing.sign_up.presenter.SignUpPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by bakhrom on 10/6/17.
 */
@Module(includes = {
        ProductClassListPresenterModule.class
})
public abstract class ProductClassListFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(ProductClassListFragment classListFragment);

    @Binds
    @PerFragment
    abstract ProductClassListView provideProductClassListFragment(ProductClassListFragment addProductClassFragment);

}
