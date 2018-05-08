package com.jim.multipos.ui.settings.pos_details;

import com.jim.multipos.core.Presenter;

public interface PosDetailsPresenter extends Presenter {
    void initDetails();
    void savePosDetails(String posId, String alias, String address, String phone);
}
