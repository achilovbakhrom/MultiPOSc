package com.jim.multipos.ui.discount.fragments;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.discount.presenters.DiscountAddingPresenter;
import com.jim.multipos.ui.discount.presenters.DiscountAddingPresenterImpl;

import dagger.Binds;
import dagger.Module;

/**
 * Created by developer on 23.10.2017.
 */
@Module
public abstract class DiscountAddingPresenterModule {
    @Binds
    @PerFragment
    abstract DiscountAddingPresenter provideDiscountAddingPresenter(DiscountAddingPresenterImpl discountAddingPresenter);

}
