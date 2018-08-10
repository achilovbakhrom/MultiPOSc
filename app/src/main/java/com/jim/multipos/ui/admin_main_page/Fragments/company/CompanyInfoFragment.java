package com.jim.multipos.ui.admin_main_page.fragments.company;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.admin_main_page.fragments.company.model.CompanyModel;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.admin_main_page.CompanyEvent;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

public class CompanyInfoFragment extends BaseFragment implements CompanyInfoAdapter.OnItemClick {

    @BindView(R.id.rvCompanyList)
    RecyclerView rvCompanyList;
    @BindView(R.id.tvCompanyName)
    TextView tvCompanyName;

    @Inject
    RxBus bus;

    ArrayList<CompanyModel> items;

    @Override
    protected int getLayout() {
        return R.layout.admin_company_info_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        items = new ArrayList<>();
        items.add(new CompanyModel("Farm Sanoat", "Lorem ipsum", ""));
        items.add(new CompanyModel("Angels Food", "Lorem ipsum", ""));
        items.add(new CompanyModel("Coca Cola", "Lorem ipsum", ""));
        rvCompanyList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvCompanyList.setAdapter(new CompanyInfoAdapter(items, getContext(), this));
        CompanyModel model = items.get(0);
        bus.send(new CompanyEvent(model.getCompanyName(), model.getCompanyDescription(), "1", "Tashkent", "112"));
    }

    @Override
    public void onClick(int position) {
        tvCompanyName.setText(items.get(position).getCompanyName());
        CompanyModel model = items.get(position);
        bus.send(new CompanyEvent(model.getCompanyName(), model.getCompanyDescription(), "1", "Tashkent", "112"));
    }
}
