package com.jim.multipos.ui.products_expired.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.products_expired.ExpiredProductsActivity;
import com.jim.multipos.ui.products_expired.ExpiredProductsPresenter;
import com.jim.multipos.ui.products_expired.ExpiredProductsPresenterImpl;
import com.jim.multipos.ui.products_expired.ExpiredProductsView;
import com.jim.multipos.ui.products_expired.expired_products.ExpiredProductsFragment;
import com.jim.multipos.ui.products_expired.expired_products.ExpiredProductsFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module(includes = BaseActivityModule.class)
public abstract class ExpiredProductsActivityModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideAppCompatActivity(ExpiredProductsActivity activity);

    @Binds
    @PerActivity
    abstract ExpiredProductsView provideExpiredProductsView(ExpiredProductsActivity activity);

    @Binds
    @PerActivity
    abstract ExpiredProductsPresenter provideExpiredProductsPresenter(ExpiredProductsPresenterImpl presenter);

    @PerFragment
    @ContributesAndroidInjector(modules = ExpiredProductsFragmentModule.class)
    abstract ExpiredProductsFragment provideExpiredProductsFragment();
}
