package com.jim.multipos.ui.product_class.presenters;

import android.support.v4.app.Fragment;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.ui.product_class.fragments.AddProductClassFragment;
import com.jim.multipos.ui.product_class.fragments.AddProductClassPresenterModule;
import com.jim.multipos.ui.product_class.fragments.AddProductClassView;
import com.jim.multipos.ui.signing.sign_up.presenter.SignUpPresenterModule;

import dagger.Binds;
import dagger.Module;

/**
 * Created by bakhrom on 10/6/17.
 */
@Module(includes = {
        AddProductClassPresenterModule.class
})
public abstract class AddProductClassFragmentModule {

    @Binds
    @PerFragment
    abstract Fragment provideFragment(AddProductClassFragment productClassFragment);

    @Binds
    @PerFragment
    abstract AddProductClassView provideAddProductClassView(AddProductClassFragment addProductClassFragment);

}
