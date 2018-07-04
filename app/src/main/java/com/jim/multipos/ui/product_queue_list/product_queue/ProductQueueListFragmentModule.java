package com.jim.multipos.ui.product_queue_list.product_queue;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module(includes = ProductQueueListFragmentPresenterModule.class)
public abstract class ProductQueueListFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(ProductQueueListFragment fragment);

    @Binds
    @PerFragment
    abstract ProductQueueListFragmentView provideProductQueueListFragmentView(ProductQueueListFragment fragment);
}
