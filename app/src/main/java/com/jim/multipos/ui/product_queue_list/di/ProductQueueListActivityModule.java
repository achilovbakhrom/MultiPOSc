package com.jim.multipos.ui.product_queue_list.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.product_queue_list.ProductQueueListActivity;
import com.jim.multipos.ui.product_queue_list.ProductQueueListView;
import com.jim.multipos.ui.product_queue_list.product_queue.ProductQueueListFragmentModule;

import dagger.Binds;
import dagger.Module;

@Module(includes = ProductQueueListFragmentModule.class)
public abstract class ProductQueueListActivityModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideProductQueueListActivity(ProductQueueListActivity activity);

    @Binds
    @PerFragment
    abstract ProductQueueListView provideProductQueueListView(ProductQueueListActivity activity);
}
