package com.jim.multipos.ui.start_configuration.currency;

import com.jim.multipos.core.Presenter;

public interface CurrencyPresenter extends Presenter {
    void initCurrency();
    void setCurrency(int position);
    void setAppRunFirstTimeValue(boolean state);
}
