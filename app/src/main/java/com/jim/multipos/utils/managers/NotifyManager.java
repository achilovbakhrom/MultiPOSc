package com.jim.multipos.utils.managers;

import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.ui.mainpospage.MainPosPageActivityView;

/**
 * Created by Sirojiddin on 26.12.2017.
 */

public class NotifyManager {
    private static NotifyManager instance;
    private MainPosPageActivityView view;

    public static NotifyManager getInstance() {
        if (instance == null) {
            instance = new NotifyManager();
        }
        return instance;
    }

    public NotifyManager() {
    }

    public void setView(MainPosPageActivityView view) {
        this.view = view;
    }

    public void notifyView(Customer customer){
        if (this.view != null){
            this.view.notifyView(customer);
        }
    }
}
