package com.jim.multipos.ui.admin_main_page.fragments.product.di;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.admin_main_page.fragments.product.ProductAddFragment;
import com.jim.multipos.ui.admin_main_page.fragments.product.presenter.ProductPresenter;
import com.jim.multipos.ui.admin_main_page.fragments.product.presenter.ProductPresenterImpl;
import com.jim.multipos.ui.admin_main_page.fragments.product.presenter.ProductView;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ProductAddModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(ProductAddFragment fragment);

    @Binds
    @PerFragment
    abstract ProductView provideView(ProductAddFragment fragment);

    @Binds
    @PerFragment
    abstract ProductPresenter providePresenter(ProductPresenterImpl presenter);
}
