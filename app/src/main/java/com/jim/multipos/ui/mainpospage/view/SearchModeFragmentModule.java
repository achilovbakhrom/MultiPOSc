package com.jim.multipos.ui.mainpospage.view;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.mainpospage.presenter.SearchModeViewPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Sirojiddin on 27.10.2017.
 */
@Module(includes = {
        SearchModeViewPresenterModule.class
})
public abstract class SearchModeFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(SearchModeFragment searchModeFragment);

    @Binds
    @PerFragment
    abstract SearchModeView provideSearchModeView(SearchModeFragment searchModeFragment);
}
