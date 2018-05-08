package com.jim.multipos.ui.settings.pos_details;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.prefs.PreferencesHelper;

import javax.inject.Inject;

public class PosDetailsPresenterImpl extends BasePresenterImpl<PosDetailsView> implements PosDetailsPresenter {

    private final PreferencesHelper preferencesHelper;

    @Inject
    protected PosDetailsPresenterImpl(PosDetailsView posDetailsView, PreferencesHelper preferencesHelper) {
        super(posDetailsView);
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    public void initDetails() {
        String posId = preferencesHelper.getPosDetailPosId();
        String alias = preferencesHelper.getPosDetailAlias();
        String address = preferencesHelper.getPosDetailAddress();
        String phone = preferencesHelper.getPosDetailPhone();
        view.fillPosDetails(posId, alias, address, phone);
    }

    @Override
    public void savePosDetails(String posId, String alias, String address, String phone) {
        preferencesHelper.setPosDetailPosId(posId);
        preferencesHelper.setPosDetailAlias(alias);
        preferencesHelper.setPosDetailAddress(address);
        preferencesHelper.setPosDetailPhone(phone);
    }
}
