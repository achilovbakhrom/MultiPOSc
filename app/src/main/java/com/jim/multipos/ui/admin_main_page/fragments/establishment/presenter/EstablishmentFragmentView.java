package com.jim.multipos.ui.admin_main_page.fragments.establishment.presenter;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.model.Establishment;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.model.EstablishmentPos;

public interface EstablishmentFragmentView extends BaseView {
    void setUpEditor(Establishment model);
    void setUpPosEditor(EstablishmentPos pos);
    void onAddMode(int mode);
}
