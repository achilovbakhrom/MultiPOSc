package com.jim.multipos.ui.reports_admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.ui.reports_admin.adapter.ReportCategoryClickAdapter;
import com.jim.multipos.ui.reports_admin.adapter.SubReportsCategoryAdapter;
import com.jim.multipos.ui.reports_admin.model.ReportsCategory;
import com.jim.multipos.ui.reports_admin.model.SubReportsCategory;
import com.jim.multipos.utils.OnItemClickListener;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportsAdminActivity extends BaseActivity implements ReportsAdminActivityView{

    @BindView(R.id.mpToolBar)
    MpToolbar toolbar;

    @BindView(R.id.rvReportsCategory)
    RecyclerView rvReportsCategory;

    @BindView(R.id.rvSubReportsCategory)
    RecyclerView rvSubReportsCategory;

    @Inject
    ReportsAdminActivityPresenter presenter;

    ReportCategoryClickAdapter adapter;
    int oldPos, newPos = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity2);
        ButterKnife.bind(this);
        String categories[] = getResources().getStringArray(R.array.reports_category);
        int id[] = {R.drawable.finance_report, R.drawable.product_report, R.drawable.stock_report, R.drawable.vendors_report, R.drawable.debts_report,
        R.drawable.discount_report, R.drawable.sales_report, R.drawable.tills_report, R.drawable.staff_report, R.drawable.customer_report,
        R.drawable.service_fee_report, R.drawable.pos_report};

        String title[] = getResources().getStringArray(R.array.products_menu_title);
        String description[] = getResources().getStringArray(R.array.products_menu_description);


        toolbar.setOnBackArrowClick(this::finish);
        presenter.setUpRecyclerView(categories, id);
        presenter.setUpSubCategoryRecyclerView(title, description);

    }

    @Override
    public void setRecyclerview(ArrayList<ReportsCategory> items) {
        rvReportsCategory.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new ReportCategoryClickAdapter(items);
        rvReportsCategory.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                oldPos = newPos;
                newPos = position;
                adapter.changeSelectedColor(oldPos, newPos);
                String title[] = getResources().getStringArray(R.array.products_menu_title);
                String description[] = getResources().getStringArray(R.array.products_menu_description);
                for (int i=0; i<title.length; i++)
                    title[i] += (items.get(position).getTitle());
                presenter.setUpSubCategoryRecyclerView(title, description);
            }

            @Override
            public void onItemClicked(Object item) {

            }
        });

    }

    @Override
    public void setSubRecyclerview(ArrayList<SubReportsCategory> items) {
        rvSubReportsCategory.setLayoutManager(new LinearLayoutManager(this));
        SubReportsCategoryAdapter adapter = new SubReportsCategoryAdapter(items);
        rvSubReportsCategory.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Toast.makeText(getApplicationContext(), items.get(position).getTitle(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemClicked(Object item) {

            }
        });
    }
}
