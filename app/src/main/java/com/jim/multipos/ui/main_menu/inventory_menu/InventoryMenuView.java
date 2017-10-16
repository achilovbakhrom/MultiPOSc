package com.jim.multipos.ui.main_menu.inventory_menu;


import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.intosystem.TitleDescription;

import java.util.ArrayList;

/**
 * Created by DEV on 08.08.2017.
 */

public interface InventoryMenuView extends BaseView {
    void setRecyclerView(ArrayList<TitleDescription> titleDescriptions);

    void openActivity(int position);
}
