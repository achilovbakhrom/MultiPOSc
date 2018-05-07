package com.jim.multipos.ui.settings.common;

import android.content.Intent;
import android.os.Bundle;
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

import java.text.DecimalFormat;

import javax.inject.Inject;

import butterknife.BindView;

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
                etFirstNominal.setError("Nominal ca'nt be empty");
                return;
            }else etFirstNominal.setError(null);
            double dFirst = 0;
            try {
                dFirst = decimalFormat.parse(firtNominal).doubleValue();
                etFirstNominal.setError(null);
            }catch (Exception e){
                etFirstNominal.setError("Invalid value");
                return;
            }

            String secNominal = etSecondNominal.getText().toString();
            if(secNominal.isEmpty()) {
                etSecondNominal.setError("Nominal ca'nt be empty");
                return;
            }else etSecondNominal.setError(null);
            double dSecond = 0;
            try {
                dSecond = decimalFormat.parse(secNominal).doubleValue();
                etSecondNominal.setError(null);
            }catch (Exception e){
                etSecondNominal.setError("Invalid value");
                return;
            }

            preferencesHelper.setFirstOptionalPaymentButton(dFirst);
            preferencesHelper.setSecondOptionalPaymentButton(dSecond);

            if(spLangauges.getSelectedPosition()!=preferencesHelper.getLanguageCode()){
                preferencesHelper.setLanguage(spLangauges.getSelectedPosition());
            }

           //TODO REFRESH MAIM PAGE

        });
        btnRevert.setOnClickListener(view -> {
            initViews();

        });
    }

    void initViews(){
        decimalFormat = BaseAppModule.getFormatterWithoutGroupingPattern("#.##");
        String[] languages = getResources().getStringArray(R.array.languages);
        spLangauges.setAdapter(languages);
        spLangauges.setSelectedPosition(preferencesHelper.getLanguageCode());
        etFirstNominal.setText(decimalFormat.format(preferencesHelper.getFirstOptionalPaymentButton()));
        etSecondNominal.setText(decimalFormat.format(preferencesHelper.getSecondOptionalPaymentButton()));

    }
}
