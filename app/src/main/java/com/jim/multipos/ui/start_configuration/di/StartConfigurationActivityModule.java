package com.jim.multipos.ui.start_configuration.di;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.R;
import com.jim.multipos.config.scope.PerActivity;
import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BaseActivityModule;
import com.jim.multipos.ui.settings.SettingsActivity;
import com.jim.multipos.ui.settings.SettingsPresenter;
import com.jim.multipos.ui.settings.SettingsPresenterImpl;
import com.jim.multipos.ui.settings.SettingsView;
import com.jim.multipos.ui.settings.accounts.AccountSettingsFragment;
import com.jim.multipos.ui.settings.accounts.AccountSettingsFragmentModule;
import com.jim.multipos.ui.settings.choice_panel.ChoicePanelFragment;
import com.jim.multipos.ui.settings.choice_panel.ChoicePanelFragmentModule;
import com.jim.multipos.ui.settings.common.CommonConfigFragment;
import com.jim.multipos.ui.settings.common.CommonConfigFragmentModule;
import com.jim.multipos.ui.settings.connection.SettingsConnection;
import com.jim.multipos.ui.settings.currency.CurrencySettingsFragment;
import com.jim.multipos.ui.settings.currency.CurrencySettingsFragmentModule;
import com.jim.multipos.ui.settings.payment_type.PaymentTypeSettingsFragment;
import com.jim.multipos.ui.settings.payment_type.PaymentTypeSettingsFragmentModule;
import com.jim.multipos.ui.settings.pos_details.PosDetailsFragment;
import com.jim.multipos.ui.settings.pos_details.PosDetailsFragmentModule;
import com.jim.multipos.ui.settings.print.PrintFragment;
import com.jim.multipos.ui.settings.print.PrintFragmentModule;
import com.jim.multipos.ui.settings.security.SecurityFragment;
import com.jim.multipos.ui.settings.security.SecurityFragmentModule;
import com.jim.multipos.ui.start_configuration.StartConfigurationActivity;
import com.jim.multipos.ui.start_configuration.StartConfigurationPresenter;
import com.jim.multipos.ui.start_configuration.StartConfigurationPresenterImpl;
import com.jim.multipos.ui.start_configuration.StartConfigurationView;
import com.jim.multipos.ui.start_configuration.account.AccountFragment;
import com.jim.multipos.ui.start_configuration.account.AccountFragmentModule;
import com.jim.multipos.ui.start_configuration.basics.BasicsFragment;
import com.jim.multipos.ui.start_configuration.basics.BasicsFragmentModule;
import com.jim.multipos.ui.start_configuration.connection.StartConfigurationConnection;
import com.jim.multipos.ui.start_configuration.currency.CurrencyFragment;
import com.jim.multipos.ui.start_configuration.currency.CurrencyFragmentModule;
import com.jim.multipos.ui.start_configuration.payment_type.PaymentTypeFragment;
import com.jim.multipos.ui.start_configuration.payment_type.PaymentTypeFragmentModule;
import com.jim.multipos.ui.start_configuration.pos_data.PosDataFragment;
import com.jim.multipos.ui.start_configuration.pos_data.PosDataFragmentModule;
import com.jim.multipos.ui.start_configuration.selection_panel.SelectionPanelFragment;
import com.jim.multipos.ui.start_configuration.selection_panel.SelectionPanelFragmentModule;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Portable-Acer on 28.10.2017.
 */

@Module(includes = BaseActivityModule.class)
public abstract class StartConfigurationActivityModule {
    @Binds
    @PerActivity
    abstract AppCompatActivity provideStartConfigurationActivity(StartConfigurationActivity activity);

    @Binds
    @PerActivity
    abstract StartConfigurationView provideStartConfigurationView(StartConfigurationActivity activity);

    @Binds
    @PerActivity
    abstract StartConfigurationPresenter provideStartConfigurationPresenter(StartConfigurationPresenterImpl presenter);

    @PerFragment
    @ContributesAndroidInjector(modules = SelectionPanelFragmentModule.class)
    abstract SelectionPanelFragment provideSelectionPanelFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = PosDataFragmentModule.class)
    abstract PosDataFragment providePosDataFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = CurrencyFragmentModule.class)
    abstract CurrencyFragment provideCurrencyFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = AccountFragmentModule.class)
    abstract AccountFragment provideAccountFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = PaymentTypeFragmentModule.class)
    abstract PaymentTypeFragment providePaymentTypeFragment();

    @PerFragment
    @ContributesAndroidInjector(modules = BasicsFragmentModule.class)
    abstract BasicsFragment provideBasicsFragment();

    @PerActivity
    @Provides
    static StartConfigurationConnection provideStartConfigurationConnection(Context context) {
        return new StartConfigurationConnection(context);
    }

    @PerActivity
    @Provides
    @Named(value = "currency_name")
    static String[] provideCurrencyName(Context context) {
        return context.getResources().getStringArray(R.array.currency_title);
    }

    @PerActivity
    @Provides
    @Named(value = "currency_abbr")
    static String[] provideCurrencyAbbr(Context context) {
        return context.getResources().getStringArray(R.array.currency_abbrs);
    }

    @PerActivity
    @Provides
    @Named(value = "till")
    static String provideAccountName(Context context) {
        return context.getResources().getString(R.string.till);
    }

    @PerActivity
    @Provides
    @Named(value = "cash")
    static String providePaymentTypeName(Context context) {
        return context.getResources().getString(R.string.cash);
    }

    @PerActivity
    @Provides
    @Named(value = "debt")
    static String provideDebtName(Context context) {
        return context.getResources().getString(R.string.debt_report);
    }

    @PerActivity
    @Provides
    @Named(value = "debtAccount")
    static String provideDebtAccountName(Context context) {
        return context.getResources().getString(R.string.debt_report);
    }

}
