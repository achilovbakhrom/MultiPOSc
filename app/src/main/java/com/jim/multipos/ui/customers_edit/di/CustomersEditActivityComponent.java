package com.jim.multipos.ui.customers_edit.di;

import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.customers_edit.CustomersEditActivity;

import dagger.Subcomponent;

/**
 * Created by user on 02.09.17.
 */

@ActivityScope
@Subcomponent(modules = {CustomersEditActivityModule.class})
public interface CustomersEditActivityComponent {
    void inject(CustomersEditActivity activity);
}
