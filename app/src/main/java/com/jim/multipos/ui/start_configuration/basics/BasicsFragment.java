package com.jim.multipos.ui.start_configuration.basics;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.start_configuration.StartConfigurationActivity;
import com.jim.multipos.ui.start_configuration.connection.StartConfigurationConnection;
import com.jim.multipos.utils.CompletionMode;
import com.jim.multipos.utils.managers.LocaleManger;

import java.text.DecimalFormat;

import javax.inject.Inject;

import butterknife.BindView;

import static com.jim.multipos.utils.managers.LocaleManger.ENGLISH;
import static com.jim.multipos.utils.managers.LocaleManger.RUSSIAN;

public class BasicsFragment extends BaseFragment implements BasicsView {

    @BindView(R.id.spLangauges)
    MPosSpinner spLangauges;
    @BindView(R.id.btnSave)
    MpButton btnSave;

    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    StartConfigurationConnection connection;

    private CompletionMode mode = CompletionMode.NEXT;

    @Override
    protected int getLayout() {
        return R.layout.basics_configure_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        connection.setBasicsView(this);
        String[] languages = getResources().getStringArray(R.array.languages);
        spLangauges.setAdapter(languages);
        switch (preferencesHelper.getLanguageCode()) {
            case ENGLISH:
                spLangauges.setSelectedPosition(0);
                break;
            case RUSSIAN:
                spLangauges.setSelectedPosition(1);
                break;
        }

        btnSave.setOnClickListener(view -> {
            connection.setBasicCompletion(true);
            if (mode == CompletionMode.NEXT)
                connection.openNextFragment(1);
            else {
                preferencesHelper.setAppRunFirstTimeValue(false);
                ((StartConfigurationActivity) getActivity()).openLockScreen();
            }
        });

        spLangauges.setItemSelectionListener((view, position) -> {
            String language = "";
            switch (position) {
                case 0:
                    language = ENGLISH;
                    break;
                case 1:
                    language = RUSSIAN;
                    break;
            }
            if (!language.matches(preferencesHelper.getLanguageCode())) {
                LocaleManger.setNewLocale(getContext(), language);
                ((StartConfigurationActivity) getContext()).refreshActivity();
            }
        });
    }

    @Override
    public void checkPosDataCompletion() {
        connection.setBasicCompletion(true);
    }

    @Override
    public void setMode(CompletionMode mode) {
        this.mode = mode;
        if (mode == CompletionMode.NEXT) {
            btnSave.setText(getContext().getString(R.string.next));
        } else btnSave.setText(getContext().getString(R.string.finish));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        connection.setBasicsView(null);
    }
}
