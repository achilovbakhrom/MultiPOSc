package com.jim.multipos.ui.product.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.product.presenter.SubCategoryModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 06.10.2017.
 */
@Module(includes = {
        SubCategoryModule.class
})
public abstract class AddSubCategoryFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(AddSubCategoryFragment subCategoryFragment);

    @Binds
    @PerFragment
    abstract SubCategoryView provideSubCategoryView(AddSubCategoryFragment subCategoryFragment);
}
