package com.jim.multipos.ui.mainpospage;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.customer.Customer;

/**
 * Created by developer on 07.08.2017.
 */

public interface MainPosPageActivityView extends BaseView {
    void notifyView(Customer customer);
}
