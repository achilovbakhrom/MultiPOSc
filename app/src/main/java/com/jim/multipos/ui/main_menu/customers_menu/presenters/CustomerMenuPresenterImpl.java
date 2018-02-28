package com.jim.multipos.ui.main_menu.customers_menu.presenters;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.db.model.intosystem.TitleDescription;
import com.jim.multipos.ui.main_menu.customers_menu.CustomersMenuView;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by DEV on 08.08.2017.
 */

public class CustomerMenuPresenterImpl extends BasePresenterImpl<CustomersMenuView> implements CustomersMenuPresenter {
    private ArrayList<TitleDescription> titleDescriptions;
    private CustomersMenuView view;
    @Inject
    public CustomerMenuPresenterImpl(CustomersMenuView customersMenuView) {
        super(customersMenuView);
        view = customersMenuView;
        titleDescriptions = new ArrayList<>();
    }

    @Override
    public void setRecyclerViewItems(String[] title, String[] description) {
        for (int i = 0; i < title.length; i++) {
            titleDescriptions.add(new TitleDescription(title[i], description[i]));
        }
        view.setRecyclerView(titleDescriptions);
    }

    @Override
    public void setItemPosition(int position) {
        view.openActivity(position);
    }


}
