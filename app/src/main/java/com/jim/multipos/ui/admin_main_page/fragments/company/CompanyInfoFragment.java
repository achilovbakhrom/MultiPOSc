package com.jim.multipos.ui.admin_main_page.fragments.company;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.admin_main_page.AdminMainPageActivity;
import com.jim.multipos.ui.admin_main_page.fragments.company.adapter.CompanyInfoAdapter;
import com.jim.multipos.ui.admin_main_page.fragments.company.model.CompanyModel;
import com.jim.multipos.ui.admin_main_page.fragments.company.presenter.CompanyFragmentPresenter;
import com.jim.multipos.ui.admin_main_page.fragments.company.presenter.CompanyFragmentView;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.admin_main_page.AddMode;
import com.jim.multipos.utils.rxevents.admin_main_page.CompanyEditor;
import com.jim.multipos.utils.rxevents.admin_main_page.CompanyEvent;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CompanyInfoFragment extends BaseFragment implements CompanyInfoAdapter.OnItemClick {

    @BindView(R.id.rvCompanyList)
    RecyclerView rvCompanyList;


    @OnClick(R.id.fab)
    public void onFabClick(View view){
        if(getActivity() instanceof AdminMainPageActivity && getActivity()!=null) {
            bus.send(new AddMode());
            ((AdminMainPageActivity) getActivity()).openEditor();
        }
    }

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
        rvCompanyList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvCompanyList.setAdapter(new CompanyInfoAdapter(items, getContext(), this));

    }


    public void onCompanyEdit(boolean isVisible) {
        if(isVisible)
            rvCompanyList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        else rvCompanyList.setLayoutManager(new GridLayoutManager(getContext(), 3));

    }

    @Override
    public void onClick(int pos) {
        bus.send(items.get(pos));
        if(getActivity() instanceof AdminMainPageActivity)
            ((AdminMainPageActivity) getActivity()).openEditor();
    }
}
