package com.jim.multipos.ui.product_class_new.presenters;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.product_class_new.adapters.ProductsClassListAdapter;
import com.jim.multipos.ui.product_class_new.fragments.ProductsClassFragment;
import com.jim.multipos.ui.product_class_new.fragments.ProductsClassPresenterModule;
import com.jim.multipos.ui.product_class_new.fragments.ProductsClassView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by developer on 17.10.2017.
 */
@Module(includes = {
        ProductsClassPresenterModule.class
})
public abstract class ProductsClassFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(ProductsClassFragment productsClassFragment);

    @Binds
    @PerFragment
    abstract ProductsClassView provideProductsClassFragment(ProductsClassFragment productsClassFragment);

    @Provides
    @PerFragment
    static ProductsClassListAdapter provideProductsClassListAdapter(AppCompatActivity context){
        return new ProductsClassListAdapter(context);
    }
}
