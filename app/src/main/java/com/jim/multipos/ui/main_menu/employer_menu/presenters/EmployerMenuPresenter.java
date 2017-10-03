package com.jim.multipos.ui.main_menu.employer_menu.presenters;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.ui.main_menu.employer_menu.EmployerMenuView;

/**
 * Created by DEV on 08.08.2017.
 */

public interface EmployerMenuPresenter extends BaseFragmentPresenter<EmployerMenuView>{
    void setRecyclerViewItems(String[] title, String[] description);
    void setItemPosition(int position);
}
