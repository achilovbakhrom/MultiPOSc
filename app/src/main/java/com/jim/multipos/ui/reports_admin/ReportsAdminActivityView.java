package com.jim.multipos.ui.reports_admin;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.ui.reports_admin.model.ReportsCategory;
import com.jim.multipos.ui.reports_admin.model.SubReportsCategory;

import java.util.ArrayList;

public interface ReportsAdminActivityView extends BaseView{
    void setRecyclerview(ArrayList<ReportsCategory> items);
    void setSubRecyclerview(ArrayList<SubReportsCategory> items);
}
