package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 27.10.2017.
 */
@Module
public abstract class ProductPickerPresenterModule {
    @Binds
    @PerFragment
    abstract ProductPickerPresenter provideProductPickerPresenter(ProductPickerPresenterImpl productPickerPresenter);
}
