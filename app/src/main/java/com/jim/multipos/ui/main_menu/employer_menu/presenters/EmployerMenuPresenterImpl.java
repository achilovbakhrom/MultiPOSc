package com.jim.multipos.ui.main_menu.employer_menu.presenters;

import com.jim.multipos.data.db.model.intosystem.TitleDescription;
import com.jim.multipos.ui.main_menu.employer_menu.EmployerMenuView;

import java.util.ArrayList;

/**
 * Created by DEV on 08.08.2017.
 */

public class EmployerMenuPresenterImpl implements EmployerMenuPresenter {
    private ArrayList<TitleDescription> titleDescriptions;
    private EmployerMenuView view;

    public EmployerMenuPresenterImpl() {
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


    @Override
    public void init(EmployerMenuView view) {

        this.view = view;
    }
}
