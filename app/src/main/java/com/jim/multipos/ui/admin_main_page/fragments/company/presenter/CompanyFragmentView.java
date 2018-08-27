package com.jim.multipos.ui.admin_main_page.fragments.company.presenter;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.ui.admin_main_page.fragments.company.model.CompanyModel;

public interface CompanyFragmentView extends BaseView {
    void onCompanyEvent(CompanyModel model);
    void onAddMode();
}
