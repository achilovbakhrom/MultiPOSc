package com.jim.multipos.ui.mainpospage.di;

import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.main_menu.product_menu.ProductMenuActivity;
import com.jim.multipos.ui.main_menu.product_menu.ProductMenuView;
import com.jim.multipos.ui.main_menu.product_menu.presenters.ProductMenuPresenter;
import com.jim.multipos.ui.main_menu.product_menu.presenters.ProductMenuPresenterImpl;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.mainpospage.MainPosPageActivityImpl;
import com.jim.multipos.ui.mainpospage.MainPosPageActivityPresenter;
import com.jim.multipos.ui.mainpospage.MainPosPageActivityView;

import dagger.Binds;
import dagger.Module;

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

}
