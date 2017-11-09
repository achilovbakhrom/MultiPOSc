package com.jim.multipos.ui.consignment.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.consignment.presenter.IncomeConsignmentPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 27.10.2017.
 */
@Module(includes = {
        IncomeConsignmentPresenterModule.class
})
public abstract class IncomeConsignmentFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(IncomeConsignmentFragment incomeConsignmentFragment);

    @Binds
    @PerFragment
    abstract IncomeConsignmentView provideIncomeConsignmentView(IncomeConsignmentFragment incomeConsignmentFragment);
}
