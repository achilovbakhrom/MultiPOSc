package com.jim.multipos.ui.settings.pos_details;

import com.jim.multipos.core.BaseView;

public interface PosDetailsView extends BaseView {
    void fillPosDetails(String posId, String alias, String address, String phone);
}
