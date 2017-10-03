package com.jim.multipos.ui.mainpospage.di;

import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.mainpospage.fragments.OrderListFragment;

import dagger.Subcomponent;

/**
 * Created by developer on 07.08.2017.
 */
@ActivityScope
@Subcomponent(modules = {MainPosPageActivityModule.class})
public interface MainPosPageActivityComponent {
    void inject(MainPosPageActivity  mainPosPageActivity);
    void inject(OrderListFragment orderListFragment);

}
