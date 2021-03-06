package com.jim.multipos.ui.main_menu.product_menu.presenters;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.db.model.intosystem.TitleDescription;
import com.jim.multipos.ui.main_menu.product_menu.ProductMenuView;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by DEV on 08.08.2017.
 */

public class ProductMenuPresenterImpl extends BasePresenterImpl<ProductMenuView> implements ProductMenuPresenter {
    private ArrayList<TitleDescription> titleDescriptions;
    private ProductMenuView view;
    @Inject
    public ProductMenuPresenterImpl(ProductMenuView productMenuView) {
        super(productMenuView);
        view = productMenuView;
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
