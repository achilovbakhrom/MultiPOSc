package com.jim.multipos.ui.main_menu.employer_menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.db.model.intosystem.TitleDescription;
import com.jim.multipos.di.BaseAppComponent;
import com.jim.multipos.ui.HasComponent;
import com.jim.multipos.ui.main_menu.MenuListAdapter;
import com.jim.multipos.ui.main_menu.employer_menu.di.EmployerMenuComponent;
import com.jim.multipos.ui.main_menu.employer_menu.di.EmployerMenuModule;
import com.jim.multipos.ui.main_menu.employer_menu.presenters.EmployerMenuPresenter;


import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DEV on 09.08.2017.
 */

public class EmployerMenuActivity extends BaseActivity implements HasComponent<EmployerMenuComponent>, EmployerMenuView {
    @BindView(R.id.rvMenu)
    RecyclerView rvMenu;
    @BindView(R.id.btnBackToMain)
    MpButton btnBackToMain;
    @Inject
    EmployerMenuPresenter presenter;
    private EmployerMenuComponent employerMenuComponent;
    final int EMPLOYER_MENU = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.product_menu_layout);
        ButterKnife.bind(this);
        String title[] = getResources().getStringArray(R.array.employer_menu_title);
        String description[] = getResources().getStringArray(R.array.employer_menu_description);
        presenter.init(this);
        presenter.setRecyclerViewItems(title, description);
    }

    @Override
    protected void setupComponent(BaseAppComponent baseAppComponent) {
        employerMenuComponent = baseAppComponent.plus(new EmployerMenuModule(this));
        employerMenuComponent.inject(this);
    }

    @Override
    public EmployerMenuComponent getComponent() {
        return employerMenuComponent;
    }

    @OnClick(R.id.btnBackToMain)
    public void onBack() {
        finish();
    }


    @Override
    public void setRecyclerView(ArrayList<TitleDescription> titleDescriptions) {
        rvMenu.setLayoutManager(new LinearLayoutManager(this));
        MenuListAdapter adapter = new MenuListAdapter(this, titleDescriptions, presenter, EMPLOYER_MENU);
        rvMenu.setAdapter(adapter);
    }

    @Override
    public void openActivity(int position) {
        //TODO add activities for opening
        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
}
