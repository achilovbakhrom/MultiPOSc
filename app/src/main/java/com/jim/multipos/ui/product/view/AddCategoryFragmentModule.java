package com.jim.multipos.ui.product.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.product.presenter.CategoryPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 06.10.2017.
 */
@Module(includes = {
        CategoryPresenterModule.class
})
public abstract class AddCategoryFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(AddCategoryFragment categoryFragment);

    @Binds
    @PerFragment
    abstract CategoryView provideCategoryView(AddCategoryFragment categoryFragment);
}
