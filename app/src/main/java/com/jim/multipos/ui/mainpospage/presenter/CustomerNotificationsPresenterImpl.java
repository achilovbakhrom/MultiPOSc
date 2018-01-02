package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.intosystem.NotificationData;
import com.jim.multipos.ui.mainpospage.view.CustomerNotificationsView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by Portable-Acer on 27.10.2017.
 */

public class CustomerNotificationsPresenterImpl extends BasePresenterImpl<CustomerNotificationsView> implements CustomerNotificationsPresenter {

    private DatabaseManager databaseManager;
    List<NotificationData> notificationData;

    @Inject
    protected CustomerNotificationsPresenterImpl(CustomerNotificationsView customerNotificationsView, DatabaseManager databaseManager) {
        super(customerNotificationsView);
        this.databaseManager = databaseManager;
        notificationData = new ArrayList<>();
    }

    @Override
    public void setData(Customer customer) {
//        Customer customer = databaseManager.getCustomerById(customerId).blockingGet();
        NotificationData data = new NotificationData();
        data.setCustomer(customer);
        data.setTotalDiscounts(2500);
        data.setTotalPayments(3000);
        data.setAbbr(databaseManager.getMainCurrency().getAbbr());
        notificationData.add(data);
        view.fillNotificationData(notificationData);
    }
}
