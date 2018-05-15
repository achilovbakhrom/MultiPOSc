package com.jim.multipos.ui.start_configuration.pos_data;

import com.jim.multipos.core.Presenter;

public interface PosDataPresenter extends Presenter {
    void savePosDetails(String posId, String alias, String address, String phone, String password);
    void setAppRunFirstTimeValue(boolean status);
}
