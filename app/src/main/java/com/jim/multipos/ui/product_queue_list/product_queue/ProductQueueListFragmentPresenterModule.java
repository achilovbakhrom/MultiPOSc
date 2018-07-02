package com.jim.multipos.ui.product_queue_list.product_queue;

import com.jim.multipos.config.scope.PerFragment;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ProductQueueListFragmentPresenterModule {

    @Binds
    @PerFragment
    abstract ProductQueueListFragmentPresenter provideProductQueueListFragmentPresenter(ProductQueueListFragmentPresenterImpl presenter);
}
