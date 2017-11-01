package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 27.10.2017.
 */
@Module
public abstract class ProductFolderViewPresenterModule {
    @Binds
    @PerFragment
    abstract ProductFolderViewPresenter provideProductFolderViewPresenter(ProductFolderViewPresenterImpl folderViewPresenter);
}
