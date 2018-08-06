package com.jim.multipos.ui.reports_admin;

import com.jim.multipos.core.Presenter;

public interface ReportsAdminActivityPresenter extends Presenter {
    void setUpRecyclerView(String categories[], int id[]);
    void setUpSubCategoryRecyclerView(String subCategories[], String description[]);
}
