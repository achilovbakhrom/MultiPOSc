package com.jim.multipos.ui.consignment.presenter;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

@Module
public abstract class IncomeConsignmentPresenterModule {
    @Binds
    @PerFragment
    abstract IncomeConsignmentPresenter provideIncomeConsignmentPresenter(IncomeConsignmentPresenterImpl presenter);
}
