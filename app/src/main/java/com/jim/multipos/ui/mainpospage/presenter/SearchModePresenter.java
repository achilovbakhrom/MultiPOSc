package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.Presenter;

/**
 * Created by Sirojiddin on 27.10.2017.
 */

public interface SearchModePresenter extends Presenter {
    void setSkuSearchMode(boolean active);

    void setNameSearchMode(boolean active);

    void setBarcodeSearchMode(boolean active);

    void onSearchTextChange(String s);

    void onOkPressed();
}
