package com.jim.multipos.ui.mainpospage.di;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.R;
import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.main_menu.product_menu.ProductMenuActivity;
import com.jim.multipos.ui.main_menu.product_menu.ProductMenuView;
import com.jim.multipos.ui.main_menu.product_menu.presenters.ProductMenuPresenter;
import com.jim.multipos.ui.main_menu.product_menu.presenters.ProductMenuPresenterImpl;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.mainpospage.MainPosPageActivityImpl;
import com.jim.multipos.ui.mainpospage.MainPosPageActivityPresenter;
import com.jim.multipos.ui.mainpospage.MainPosPageActivityView;
import com.jim.multipos.ui.mainpospage.view.OrderListFragment;
import com.jim.multipos.ui.mainpospage.view.OrderListFragmentModule;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by bakhrom on 10/6/17.
 */

@Module(includes = BaseActivityModule.class)
public abstract class MainPageMenuModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideMainPosPageActivity(MainPosPageActivity mainPosPageActivity);

    @Binds
    @PerActivity
    abstract MainPosPageActivityPresenter provideMainPosPageActivityPresenter(MainPosPageActivityImpl mainPosPageActivityPresenter);

    @Binds
    @PerActivity
    abstract MainPosPageActivityView provideMainPosPageActivityView(MainPosPageActivity mainPosPageActivity);

    @PerFragment
    @ContributesAndroidInjector(modules = ProductPickerFragmentModule.class)
    abstract ProductPickerFragment provideProductPickerFragmentInjector();

    @PerFragment
    @ContributesAndroidInjector(modules = ProductSquareFragmentModule.class)
    abstract ProductSquareViewFragment provideProductSquareViewFragmentInjector();

    @PerFragment
    @ContributesAndroidInjector(modules = ProductFolderFragmentModule.class)
    abstract ProductFolderViewFragment provideProductFolderViewFragmentInjector();

    @PerFragment
    @ContributesAndroidInjector(modules = SearchModeFragmentModule.class)
    abstract SearchModeFragment provideSearchModeFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = ProductInfoFragmentModule.class)
    abstract ProductInfoFragment provideProductInfoFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = OrderListFragmentModule.class)
    abstract OrderListFragment provideOrderListFragment();

    @PerActivity
    @Provides
    @Named(value = "discount_amount_types")
    static String[] provideDiscountAmountTypes(Context context) {
        return context.getResources().getStringArray(R.array.discount_amount_types_abr);
    }

    @PerActivity
    @Provides
    @Named(value = "discount_used_types_abr")
    static String[] provideDiscountUsedTypesAbr(Context context) {
        return context.getResources().getStringArray(R.array.discount_used_types_abr);
    }
}
