package com.jim.multipos.ui.mainpospage.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.mainpospage.presenter.ProductPickerPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 27.10.2017.
 */
@Module(includes = {
        ProductPickerPresenterModule.class
})
public abstract class ProductPickerFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(ProductPickerFragment productPickerFragment);

    @Binds
    @PerFragment
    abstract ProductPickerView provideProductPickerView(ProductPickerFragment productPickerFragment);
}
