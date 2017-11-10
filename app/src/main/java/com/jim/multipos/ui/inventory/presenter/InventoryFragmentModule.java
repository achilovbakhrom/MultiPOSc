package com.jim.multipos.ui.inventory.presenter;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.inventory.adapters.InventoryItemAdapter;
import com.jim.multipos.ui.inventory.fragments.InventoryFragment;
import com.jim.multipos.ui.inventory.fragments.InventoryPresenterModule;
import com.jim.multipos.ui.inventory.fragments.InventoryView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by developer on 09.11.2017.
 */
@Module(includes = {
        InventoryPresenterModule.class
})
public abstract class InventoryFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(InventoryFragment inventoryFragment);

    @Binds
    @PerFragment
    abstract InventoryView provideProductsClassFragment(InventoryFragment inventoryFragment);

}
