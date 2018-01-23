package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.Presenter;

/**
 * Created by Sirojiddin on 20.01.2018.
 */

public interface BarcodeScannerPresenter extends Presenter {
    void findProductByBarcode(String barcode);
}
