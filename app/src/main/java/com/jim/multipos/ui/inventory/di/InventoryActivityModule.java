package com.jim.multipos.ui.inventory.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.inventory.InventoryActivity;
import com.jim.multipos.ui.inventory.InventoryActivityPresenter;
import com.jim.multipos.ui.inventory.InventoryActivityPresenterImpl;
import com.jim.multipos.ui.inventory.InventoryActivityView;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.mainpospage.MainPosPageActivityImpl;
import com.jim.multipos.ui.mainpospage.MainPosPageActivityPresenter;
import com.jim.multipos.ui.mainpospage.MainPosPageActivityView;
import com.jim.multipos.ui.mainpospage.view.ProductFolderFragmentModule;
import com.jim.multipos.ui.mainpospage.view.ProductFolderViewFragment;
import com.jim.multipos.ui.mainpospage.view.ProductInfoFragment;
import com.jim.multipos.ui.mainpospage.view.ProductInfoFragmentModule;
import com.jim.multipos.ui.mainpospage.view.ProductPickerFragment;
import com.jim.multipos.ui.mainpospage.view.ProductPickerFragmentModule;
import com.jim.multipos.ui.mainpospage.view.ProductSquareFragmentModule;
import com.jim.multipos.ui.mainpospage.view.ProductSquareViewFragment;
import com.jim.multipos.ui.mainpospage.view.SearchModeFragment;
import com.jim.multipos.ui.mainpospage.view.SearchModeFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by bakhrom on 10/6/17.
 */

@Module(includes = BaseActivityModule.class)
public abstract class InventoryActivityModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideInventoryActivity(InventoryActivity inventoryActivity);

    @Binds
    @PerActivity
    abstract InventoryActivityPresenter provideInventoryActivityPresenter(InventoryActivityPresenterImpl inventoryActivityPresenter);

    @Binds
    @PerActivity
    abstract InventoryActivityView provideInventoryActivityView(InventoryActivity inventoryActivity);
}
