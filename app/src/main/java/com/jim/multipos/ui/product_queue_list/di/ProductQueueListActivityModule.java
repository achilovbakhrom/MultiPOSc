package com.jim.multipos.ui.product_queue_list.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.product_queue_list.ProductQueueListActivity;
import com.jim.multipos.ui.product_queue_list.ProductQueueListPresenter;
import com.jim.multipos.ui.product_queue_list.ProductQueueListPresenterImpl;
import com.jim.multipos.ui.product_queue_list.ProductQueueListView;
import com.jim.multipos.ui.product_queue_list.product_queue.ProductQueueListFragment;
import com.jim.multipos.ui.product_queue_list.product_queue.ProductQueueListFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module(includes = BaseActivityModule.class)
public abstract class ProductQueueListActivityModule {

    @Binds
    @PerActivity
    abstract AppCompatActivity provideProductQueueListActivity(ProductQueueListActivity activity);

    @Binds
    @PerActivity
    abstract ProductQueueListView provideProductQueueListView(ProductQueueListActivity activity);

    @Binds
    @PerActivity
    abstract ProductQueueListPresenter productQueueListPresenter(ProductQueueListPresenterImpl presenter);

    @PerFragment
    @ContributesAndroidInjector(modules = ProductQueueListFragmentModule.class)
    abstract ProductQueueListFragment productQueueListFragment();

}
