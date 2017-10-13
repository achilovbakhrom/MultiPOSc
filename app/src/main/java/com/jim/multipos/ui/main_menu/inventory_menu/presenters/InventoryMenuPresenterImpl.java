package com.jim.multipos.ui.main_menu.inventory_menu.presenters;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.db.model.intosystem.TitleDescription;
import com.jim.multipos.ui.main_menu.inventory_menu.InventoryMenuView;
import com.jim.multipos.ui.product_class.fragments.AddProductClassView;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by DEV on 08.08.2017.
 */

public class InventoryMenuPresenterImpl extends BasePresenterImpl<InventoryMenuView> implements InventoryMenuPresenter {
    private ArrayList<TitleDescription> titleDescriptions;
    private InventoryMenuView view;
    @Inject
    public InventoryMenuPresenterImpl(InventoryMenuView inventoryMenuView) {
        super(inventoryMenuView);
        view  = inventoryMenuView;
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
