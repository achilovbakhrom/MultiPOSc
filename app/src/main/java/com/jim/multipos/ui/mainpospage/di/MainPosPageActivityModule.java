package com.jim.multipos.ui.mainpospage.di;

import com.jim.multipos.ui.ActivityScope;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.mainpospage.MainPosPageActivityImpl;
import com.jim.multipos.ui.mainpospage.MainPosPageActivityPresenter;
import com.jim.multipos.utils.managers.PosFragmentManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by developer on 07.08.2017.
 */
@Module
public class MainPosPageActivityModule {

    private MainPosPageActivity mainPosPageActivity;

    public MainPosPageActivityModule(MainPosPageActivity mainPosPageActivity){
        this.mainPosPageActivity = mainPosPageActivity;
    }

    @Provides
    @ActivityScope
    public MainPosPageActivity getActivity(){
        return mainPosPageActivity;
    }

    @Provides
    @ActivityScope
    public PosFragmentManager getFragmentManager(MainPosPageActivity activity) {
        return new PosFragmentManager(activity);
    }

    @Provides
    @ActivityScope
    public MainPosPageActivityPresenter getMainPosPageActivityPresenter(){
        return new MainPosPageActivityImpl();
    }
}
