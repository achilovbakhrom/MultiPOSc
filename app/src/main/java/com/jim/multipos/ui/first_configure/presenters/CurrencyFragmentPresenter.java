package com.jim.multipos.ui.first_configure.presenters;

import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.ui.first_configure.common.BaseFragmentPresenterFirstConfig;
import com.jim.multipos.ui.first_configure.fragments.CurrencyFragmentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 05.08.17.
 */

public interface CurrencyFragmentPresenter extends BaseFragmentPresenterFirstConfig<CurrencyFragmentView> {
    boolean isCompleteData();
    void setMainCurrencies();
    void setOtherCurrencies();
    void setAdapterData();
    void changeMainCurrency(int position);
    List<Currency> getCurrencies();
    void addCurrency(int position);
    void removeSelectedCurrency(int position);
    List<Currency> getRemovedList();
    void setData();
}