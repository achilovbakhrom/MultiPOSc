package com.jim.multipos.ui.product.presenter;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.ui.product.view.AdvancedOptionView;

/**
 * Created by DEV on 06.09.2017.
 */

public interface AdvancedOptionPresenter extends BaseFragmentPresenter<AdvancedOptionView>{
    void setSubUnits(String name, String qty, boolean active);
    void onDestroy();
    void saveOptions(String description);
    void removeSubUnit(int position);
}
