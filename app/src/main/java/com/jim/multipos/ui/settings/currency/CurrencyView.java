package com.jim.multipos.ui.settings.currency;

import com.jim.multipos.core.BaseView;

public interface CurrencyView extends BaseView {
    void setItemsToSpinner(String[] items);
    void setSelection(int position);
}
