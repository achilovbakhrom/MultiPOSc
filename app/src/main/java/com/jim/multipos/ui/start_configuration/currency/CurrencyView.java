package com.jim.multipos.ui.start_configuration.currency;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.utils.CompletionMode;

public interface CurrencyView extends BaseView {
    void setItemsToSpinner(String[] items);
    void setSelection(int position);
    void setMode(CompletionMode mode);
    void checkCompletion();
}
