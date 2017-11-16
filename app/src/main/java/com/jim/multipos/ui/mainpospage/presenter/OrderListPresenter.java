package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.Discount;

import java.util.List;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

public interface OrderListPresenter extends Presenter {
    List<Discount> getDiscounts();
}
