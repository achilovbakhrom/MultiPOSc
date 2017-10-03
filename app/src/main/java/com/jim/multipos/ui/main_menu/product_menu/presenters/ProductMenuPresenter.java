package com.jim.multipos.ui.main_menu.product_menu.presenters;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.ui.main_menu.product_menu.ProductMenuView;

/**
 * Created by DEV on 08.08.2017.
 */

public interface ProductMenuPresenter extends BaseFragmentPresenter<ProductMenuView>{
    void setRecyclerViewItems(String[] title, String[] description);
    void setItemPosition(int position);
}
