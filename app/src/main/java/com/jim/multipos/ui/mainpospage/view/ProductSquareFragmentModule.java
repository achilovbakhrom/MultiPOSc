package com.jim.multipos.ui.mainpospage.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.mainpospage.presenter.ProductSquareViewPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 27.10.2017.
 */
@Module(includes = {
        ProductSquareViewPresenterModule.class
})
public abstract class ProductSquareFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(ProductSquareViewFragment squareViewFragment);

    @Binds
    @PerFragment
    abstract ProductSquareView provideProductSquareView(ProductSquareViewFragment squareViewFragment);
}
