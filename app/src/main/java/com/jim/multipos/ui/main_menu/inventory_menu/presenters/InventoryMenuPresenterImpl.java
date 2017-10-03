package com.jim.multipos.ui.main_menu.inventory_menu.presenters;

import com.jim.multipos.data.db.model.intosystem.TitleDescription;
import com.jim.multipos.ui.main_menu.inventory_menu.InventoryMenuView;

import java.util.ArrayList;

/**
 * Created by DEV on 08.08.2017.
 */

public class InventoryMenuPresenterImpl implements InventoryMenuPresenter {
    private ArrayList<TitleDescription> titleDescriptions;
    private InventoryMenuView view;

    public InventoryMenuPresenterImpl() {
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
    public void init(InventoryMenuView view) {

        this.view = view;
    }
}
