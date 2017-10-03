package com.jim.multipos.ui.main_menu.product_menu.presenters;

import com.jim.multipos.ui.main_menu.product_menu.ProductMenuView;
import com.jim.multipos.data.db.model.intosystem.TitleDescription;

import java.util.ArrayList;

/**
 * Created by DEV on 08.08.2017.
 */

public class ProductMenuPresenterImpl implements ProductMenuPresenter {
    private ArrayList<TitleDescription> titleDescriptions;
    private ProductMenuView view;

    public ProductMenuPresenterImpl() {
        titleDescriptions = new ArrayList<>();
    }

    @Override
    public void init(ProductMenuView view) {
        this.view = view;
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
