package com.jim.multipos.ui.mainpospage.view;

import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v7.widget.LinearLayoutManager;

import com.jim.mpviews.RecyclerViewWithMaxHeight;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.intosystem.NotificationData;
import com.jim.multipos.ui.mainpospage.adapter.NotificationsAdapter;
import com.jim.multipos.ui.mainpospage.presenter.CustomerNotificationsPresenter;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Sirojiddin on 26.12.2017.
 */

public class CustomerNotificationsFragment extends BaseFragment implements CustomerNotificationsView {

    @BindView(R.id.rvCustomerNotifications)
    RecyclerViewWithMaxHeight rvCustomerNotifications;
    @Inject
    CustomerNotificationsPresenter presenter;
    private NotificationsAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.customer_notifications_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        adapter = new NotificationsAdapter(getContext());
        rvCustomerNotifications.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCustomerNotifications.setAdapter(adapter);
        rvCustomerNotifications.setMaxHeight(500);
//        if (getArguments() != null) {
//            Long customerId = getArguments().getLong("CUSTOMER_ID");
//            presenter.setData(customerId);
//        }
    }

    final long DELAY = 5000;

    @Override
    public void fillNotificationData(List<NotificationData> data) {
        adapter.setData(data);
        for (int i = 0; i < data.size(); i++) {
            Timer timer = new Timer();
            timer.schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    removeItem();
                                }
                            });

                        }
                    },
                    DELAY
            );
        }

    }

    @UiThread
    private void removeItem() {
        adapter.removeItem(adapter.getItemCount() - 1);
    }

    public void addDataToCustomerList(Customer customer) {
        presenter.setData(customer);
    }
}
