package com.jim.multipos.ui.reports_admin;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.db.model.intosystem.TitleDescription;
import com.jim.multipos.ui.reports_admin.model.ReportsCategory;
import com.jim.multipos.ui.reports_admin.model.SubReportsCategory;

import java.util.ArrayList;

import javax.inject.Inject;

public class ReportsAdminActivityPresenterImpl extends BasePresenterImpl<ReportsAdminActivityView> implements ReportsAdminActivityPresenter {

    private ArrayList<ReportsCategory> reportsCategories;
    private ArrayList<SubReportsCategory> subReportsCategories;
    ReportsAdminActivityView view;


    @Inject
    protected ReportsAdminActivityPresenterImpl(ReportsAdminActivityView reportsAdminView) {
        super(reportsAdminView);
        reportsCategories = new ArrayList<>();
        subReportsCategories = new ArrayList<>();
        view = reportsAdminView;
    }


    @Override
    public void setUpRecyclerView(String[] categories, int[] id) {
        reportsCategories.clear();
        for (int i = 0; i < categories.length; i++) {
            reportsCategories.add(new ReportsCategory(categories[i], id[i]));
        }
        view.setRecyclerview(reportsCategories);
    }

    @Override
    public void setUpSubCategoryRecyclerView(String[] subCategories, String[] description) {
        subReportsCategories.clear();
        for (int i = 0; i < subCategories.length; i++) {
            subReportsCategories.add(new SubReportsCategory(subCategories[i], description[i]));
        }
        view.setSubRecyclerview(subReportsCategories);
    }

}
