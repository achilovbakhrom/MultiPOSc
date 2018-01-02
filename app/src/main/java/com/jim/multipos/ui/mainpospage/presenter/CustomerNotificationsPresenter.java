package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.customer.Customer;

/**
 * Created by Sirojiddin on 26.12.2017.
 */

public interface CustomerNotificationsPresenter extends Presenter {
    void setData(Customer customerId);
}
