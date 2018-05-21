package com.jim.multipos.ui.start_configuration.pos_data;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.utils.SecurityTools;

import javax.inject.Inject;

public class PosDataPresenterImpl extends BasePresenterImpl<PosDataView> implements PosDataPresenter {

    private PreferencesHelper preferences;

    @Inject
    protected PosDataPresenterImpl(PosDataView posDataView, PreferencesHelper preferencesHelper) {
        super(posDataView);
        this.preferences = preferencesHelper;
    }

    @Override
    public void savePosDetails(String posId, String alias, String address, String phone, String password) {
        preferences.setPosDetailPosId(posId);
        preferences.setPosDetailAlias(alias);
        preferences.setPosDetailAddress(address);
        preferences.setPhoneNumber(phone);
        preferences.setPosDetailPassword(SecurityTools.md5(password));
        view.onComplete();
    }

    @Override
    public void setAppRunFirstTimeValue(boolean status) {
        preferences.setAppRunFirstTimeValue(status);
    }
}
