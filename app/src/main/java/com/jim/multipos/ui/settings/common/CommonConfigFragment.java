package com.jim.multipos.ui.settings.common;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.settings.SettingsActivity;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.managers.LocaleManger;
import com.jim.multipos.utils.rxevents.main_order_events.MainPosActivityRefreshEvent;
import com.jim.multipos.utils.rxevents.main_order_events.OrderEvent;

import java.text.DecimalFormat;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;

import static com.jim.multipos.utils.managers.LocaleManger.ENGLISH;
import static com.jim.multipos.utils.managers.LocaleManger.RUSSIAN;

public class CommonConfigFragment extends BaseFragment implements CommonConfigView {

    @BindView(R.id.spLangauges)
    MPosSpinner spLangauges;
    @BindView(R.id.etFirstNominal)
    EditText etFirstNominal;
    @BindView(R.id.etSecondNominal)
    EditText etSecondNominal;
    @BindView(R.id.btnSave)
    MpButton btnSave;
    @BindView(R.id.btnRevert)
    MpButton btnRevert;

    @Inject
    RxBus rxBus;
    DecimalFormat decimalFormat;

    @Inject
    PreferencesHelper preferencesHelper;

    @Override
    protected int getLayout() {
        return R.layout.common_config_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initViews();
        btnSave.setOnClickListener(view -> {
            String firtNominal = etFirstNominal.getText().toString();
            if(firtNominal.isEmpty()) {
                etFirstNominal.setError(getContext().getString(R.string.nominal_cant_be_empty));
                return;
            } else etFirstNominal.setError(null);
            double dFirst = 0;
            try {
                dFirst = decimalFormat.parse(firtNominal).doubleValue();
                etFirstNominal.setError(null);
            }catch (Exception e){
                etFirstNominal.setError(getContext().getString(R.string.invalid));
                return;
            }

            String secNominal = etSecondNominal.getText().toString();
            if(secNominal.isEmpty()) {
                etSecondNominal.setError(getContext().getString(R.string.nominal_cant_be_empty));
                return;
            } else etSecondNominal.setError(null);
            double dSecond = 0;
            try {
                dSecond = decimalFormat.parse(secNominal).doubleValue();
                etSecondNominal.setError(null);
            }catch (Exception e){
                etSecondNominal.setError(getContext().getString(R.string.invalid));
                return;
            }

            preferencesHelper.setFirstOptionalPaymentButton(dFirst);
            preferencesHelper.setSecondOptionalPaymentButton(dSecond);

            String language = "";
            switch (spLangauges.getSelectedPosition()){
                case 0:
                    language = ENGLISH;
                    break;
                case 1:
                    language = RUSSIAN;
                    break;
            }
            if (!language.matches(preferencesHelper.getLanguageCode())){
                LocaleManger.setNewLocale(getContext(), language);
                ((SettingsActivity) getContext()).openLockScreen();
            }

            //TODO REFRESH MAIM PAGE
            getActivity().finish();
            rxBus.send(new MainPosActivityRefreshEvent());
        });
        btnRevert.setOnClickListener(view -> {
            initViews();
        });
    }

    void initViews(){
        decimalFormat = BaseAppModule.getFormatterWithoutGroupingPattern("#.##");
        String[] languages = getResources().getStringArray(R.array.languages);
        spLangauges.setAdapter(languages);
        switch (preferencesHelper.getLanguageCode()){
            case ENGLISH:
                spLangauges.setSelectedPosition(0);
                break;
            case RUSSIAN:
                spLangauges.setSelectedPosition(1);
                break;
        }
        etFirstNominal.setText(decimalFormat.format(preferencesHelper.getFirstOptionalPaymentButton()));
        etSecondNominal.setText(decimalFormat.format(preferencesHelper.getSecondOptionalPaymentButton()));

    }
}
