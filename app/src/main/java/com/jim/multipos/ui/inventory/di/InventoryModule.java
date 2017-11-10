package com.jim.multipos.ui.inventory.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.inventory.InventoryActivity;
import com.jim.multipos.ui.inventory.fragments.InventoryFragment;
import com.jim.multipos.ui.inventory.presenter.InventoryFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by developer on 09.11.2017.
 */
@Module(includes = BaseActivityModule.class)
public abstract class InventoryModule {
    @Binds
    @PerActivity
    abstract AppCompatActivity provideInventoryActivity(InventoryActivity inventoryActivity);

    @PerFragment
    @ContributesAndroidInjector(modules = InventoryFragmentModule.class)
    abstract InventoryFragment provideInventoryFragment();

}
