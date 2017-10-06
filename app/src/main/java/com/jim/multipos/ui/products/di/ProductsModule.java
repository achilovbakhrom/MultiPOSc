package com.jim.multipos.ui.products.di;

import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.products.ProductsActivity;
import com.jim.multipos.ui.products.fragments.AddMatrixAttributeView;
import com.jim.multipos.ui.products.presenters.AddMatrixAttributePresenter;
import com.jim.multipos.ui.products.presenters.AddMatrixAttributesPresenterImpl;
import com.jim.multipos.ui.products.presenters.AdvancedOptionPresenter;
import com.jim.multipos.ui.products.presenters.AdvancedOptionsPresenterImpl;
import com.jim.multipos.ui.products.presenters.CategoryPresenter;
import com.jim.multipos.ui.products.presenters.CategoryPresenterImpl;
import com.jim.multipos.ui.products.presenters.ProductListPresenter;
import com.jim.multipos.ui.products.presenters.ProductListPresenterImpl;
import com.jim.multipos.ui.products.presenters.ProductsPresenter;
import com.jim.multipos.ui.products.presenters.ProductsPresenterImpl;
import com.jim.multipos.ui.products.presenters.SubCategoryPresenter;
import com.jim.multipos.ui.products.presenters.SubCategoryPresenterImpl;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.managers.PosFragmentManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DEV on 09.08.2017.
 */
@Module
public class ProductsModule {
    private ProductsActivity activity;

    public ProductsModule(ProductsActivity productsActivity) {
        this.activity = productsActivity;
    }

    @ActivityScope
    @Provides
    public ProductsActivity getProductsActivity() {
        return activity;
    }

    @ActivityScope
    @Provides
    public RxBusLocal getRxBusLocal() {
        return new RxBusLocal();
    }

    @ActivityScope
    @Provides
    public PosFragmentManager getPosFragmentManager(ProductsActivity activity) {
        return null; //new PosFragmentManager(activity);
    }

    @ActivityScope
    @Provides
    public RxPermissions provideRxPermissions(ProductsActivity activity){
        return null; //new RxPermissions(activity);
    }

    @ActivityScope
    @Provides
    public CategoryPresenter getCategoryPresenter(DatabaseManager databaseManager, RxBus rxBus, RxBusLocal rxBusLocal) {
        return new CategoryPresenterImpl(databaseManager, rxBus, rxBusLocal);
    }

    @ActivityScope
    @Provides
    public SubCategoryPresenter getSubCategoryPresenter(DatabaseManager databaseManager, RxBus rxBus, RxBusLocal rxBusLocal, PreferencesHelper preferencesHelper) {
        return new SubCategoryPresenterImpl(databaseManager, rxBus, rxBusLocal, preferencesHelper);
    }

    @ActivityScope
    @Provides
    public ProductListPresenter getProductListPresenter(DatabaseManager databaseManager, RxBus rxBus, RxBusLocal rxBusLocal, PreferencesHelper preferencesHelper) {
        return new ProductListPresenterImpl(databaseManager, rxBus, rxBusLocal, preferencesHelper);
    }

    @ActivityScope
    @Provides
    public ProductsPresenter getProductsPresenter(DatabaseManager databaseManager, RxBus rxBus, PreferencesHelper preferencesHelper, RxBusLocal rxBusLocal) {
        return new ProductsPresenterImpl(databaseManager, rxBus, preferencesHelper, rxBusLocal);
    }

    @ActivityScope
    @Provides
    public AdvancedOptionPresenter getAdvancedOptionPresenter(DatabaseManager databaseManager, RxBus rxBus, RxBusLocal rxBusLocal) {
        return new AdvancedOptionsPresenterImpl(databaseManager, rxBus, rxBusLocal);
    }

    @ActivityScope
    @Provides
    public AddMatrixAttributePresenter getAddMatrixAttributePresenter(DatabaseManager databaseManager, RxBus rxBus, RxBusLocal rxBusLocal) {
        return new AddMatrixAttributesPresenterImpl(databaseManager, rxBus, rxBusLocal);
    }
}
