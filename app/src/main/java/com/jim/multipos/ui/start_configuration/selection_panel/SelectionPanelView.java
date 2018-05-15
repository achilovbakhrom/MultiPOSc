package com.jim.multipos.ui.start_configuration.selection_panel;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.utils.CompletionMode;

public interface SelectionPanelView extends BaseView {
    void showNextFragment(int position);
    void sendMode(CompletionMode mode, int position);
    void setPosDataCompletion(boolean state);
    void setCurrencyCompletion(boolean state);
    void setAccountCompletion(boolean state);
    void setPaymentTypeCompletion(boolean state);
    void openNextFragment(int position);
}
