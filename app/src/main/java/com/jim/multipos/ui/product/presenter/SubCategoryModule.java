package com.jim.multipos.ui.product.presenter;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 06.10.2017.
 */
@Module
public abstract class SubCategoryModule {
    @Binds
    @PerFragment
    abstract SubCategoryPresenter provideSubCategoryPresenter(SubCategoryPresenterImpl subCategoryPresenter);
}
