package com.jim.multipos.ui.main_menu.inventory_menu.presenters;

import com.jim.multipos.core.Presenter;

/**
 * Created by DEV on 08.08.2017.
 */

public interface InventoryMenuPresenter extends Presenter {
    void setRecyclerViewItems(String[] title, String[] description);
    void setItemPosition(int position);
}
