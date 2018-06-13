package com.jim.multipos.ui.discount.presenters;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.discount.adapters.DiscountListAdapter;
import com.jim.multipos.ui.discount.fragments.DiscountAddingFragment;
import com.jim.multipos.ui.discount.fragments.DiscountAddingPresenterModule;
import com.jim.multipos.ui.discount.fragments.DiscountAddingView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by developer on 23.10.2017.
 */
@Module(includes = {
        DiscountAddingPresenterModule.class
})
public abstract class DiscountAddingFragmentModule {
    @Binds
    @PerFragment
    abstract Fragment provideFragment(DiscountAddingFragment discountAddingFragment);

    @Binds
    @PerFragment
    abstract DiscountAddingView provideDiscountAddingView(DiscountAddingFragment discountAddingFragment);

    @Provides
    @PerFragment
    static DiscountListAdapter provideDiscountListAdapter(AppCompatActivity context){
        return new DiscountListAdapter(context);
    }
}
