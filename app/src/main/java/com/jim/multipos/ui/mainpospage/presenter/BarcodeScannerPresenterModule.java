package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 20.01.2018.
 */
@Module
public abstract class BarcodeScannerPresenterModule {

    @Binds
    @PerFragment
    abstract BarcodeScannerPresenter provideBarcodeScannerPresenter(BarcodeScannerPresenterImpl barcodeScannerPresenter);
}
