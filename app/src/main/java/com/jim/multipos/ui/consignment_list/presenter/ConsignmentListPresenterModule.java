package com.jim.multipos.ui.consignment_list.presenter;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 30.11.2017.
 */

@Module
public abstract class ConsignmentListPresenterModule {
    @Binds
    @PerFragment
    abstract ConsignmentListPresenter provideConsignmentListPresenter(ConsignmentListPresenterImpl presenter);
}
