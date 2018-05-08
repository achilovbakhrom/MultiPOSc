package com.jim.multipos.ui.settings.di;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.first_configure_last.fragment.POSDetailsFragment;
import com.jim.multipos.ui.inventory.fragments.InventoryFragment;
import com.jim.multipos.ui.inventory.presenter.InventoryFragmentModule;
import com.jim.multipos.ui.mainpospage.connection.MainPageConnection;
import com.jim.multipos.ui.service_fee_new.ServiceFeeActivity;
import com.jim.multipos.ui.service_fee_new.ServiceFeePresenter;
import com.jim.multipos.ui.service_fee_new.ServiceFeePresenterImpl;
import com.jim.multipos.ui.service_fee_new.ServiceFeeView;
import com.jim.multipos.ui.settings.SettingsActivity;
import com.jim.multipos.ui.settings.SettingsPresenter;
import com.jim.multipos.ui.settings.SettingsPresenterImpl;
import com.jim.multipos.ui.settings.SettingsView;
import com.jim.multipos.ui.settings.accounts.AccountSettingsFragment;
import com.jim.multipos.ui.settings.accounts.AccountSettingsFragmentModule;
import com.jim.multipos.ui.settings.choice_panel.ChoicePanelFragment;
import com.jim.multipos.ui.settings.choice_panel.ChoicePanelFragmentModule;
import com.jim.multipos.ui.settings.connection.SettingsConnection;
import com.jim.multipos.ui.settings.currency.CurrencySettingsFragment;
import com.jim.multipos.ui.settings.currency.CurrencySettingsFragmentModule;
import com.jim.multipos.ui.settings.payment_type.PaymentTypeSettingsFragment;
import com.jim.multipos.ui.settings.payment_type.PaymentTypeSettingsFragmentModule;
import com.jim.multipos.ui.settings.pos_details.PosDetailsFragment;
import com.jim.multipos.ui.settings.pos_details.PosDetailsFragmentModule;
import com.jim.multipos.ui.settings.common.CommonConfigFragment;
import com.jim.multipos.ui.settings.common.CommonConfigFragmentModule;
import com.jim.multipos.ui.settings.print.PrintFragment;
import com.jim.multipos.ui.settings.print.PrintFragmentModule;
import com.jim.multipos.ui.settings.security.SecurityFragment;
import com.jim.multipos.ui.settings.security.SecurityFragmentModule;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Portable-Acer on 28.10.2017.
 */

@Module(includes = BaseActivityModule.class)
public abstract class SettingsActivityModule {
    @Binds
    @PerActivity
    abstract AppCompatActivity provideSettingsActivity(SettingsActivity activity);

    @Binds
    @PerActivity
    abstract SettingsView provideSettingsView(SettingsActivity activity);

    @Binds
    @PerActivity
    abstract SettingsPresenter provideSettingsPresenter(SettingsPresenterImpl presenter);

    @PerFragment
    @ContributesAndroidInjector(modules = ChoicePanelFragmentModule.class)
    abstract ChoicePanelFragment provideChoicePanelFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = SecurityFragmentModule.class)
    abstract SecurityFragment provideSecurityFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = PrintFragmentModule.class)
    abstract PrintFragment providePrintFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = CommonConfigFragmentModule.class)
    abstract CommonConfigFragment provideCommonConfigFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = PosDetailsFragmentModule.class)
    abstract PosDetailsFragment providePosDetailsFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = CurrencySettingsFragmentModule.class)
    abstract CurrencySettingsFragment provideCurrencySettingsFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = AccountSettingsFragmentModule.class)
    abstract AccountSettingsFragment provideAccountSettingsFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = PaymentTypeSettingsFragmentModule.class)
    abstract PaymentTypeSettingsFragment providePaymentTypeSettingsFragment();

    @PerActivity
    @Provides
    static SettingsConnection provideSettingsConnection(Context context){
        return new SettingsConnection(context);
    }
}
