package com.jim.multipos.ui.main_menu.inventory_menu.presenters;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.ui.main_menu.inventory_menu.InventoryMenuView;

/**
 * Created by DEV on 08.08.2017.
 */

public interface InventoryMenuPresenter extends BaseFragmentPresenter<InventoryMenuView>{
    void setRecyclerViewItems(String[] title, String[] description);
    void setItemPosition(int position);
}
