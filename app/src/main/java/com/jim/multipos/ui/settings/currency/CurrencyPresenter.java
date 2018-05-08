package com.jim.multipos.ui.settings.currency;

import com.jim.multipos.core.Presenter;

public interface CurrencyPresenter extends Presenter {
    void initCurrency();
    void setCurrency(int position);
}
