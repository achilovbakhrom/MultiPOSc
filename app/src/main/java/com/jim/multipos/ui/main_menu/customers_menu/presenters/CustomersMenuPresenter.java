package com.jim.multipos.ui.main_menu.customers_menu.presenters;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.ui.main_menu.customers_menu.CustomersMenuView;

/**
 * Created by DEV on 08.08.2017.
 */

public interface CustomersMenuPresenter extends BaseFragmentPresenter<CustomersMenuView>{
    void setRecyclerViewItems(String[] title, String[] description);
    void setItemPosition(int position);
}
