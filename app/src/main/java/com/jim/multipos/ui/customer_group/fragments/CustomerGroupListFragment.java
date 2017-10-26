package com.jim.multipos.ui.customer_group.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customer_group.adapters.CustomerGroupListAdapter;
import com.jim.multipos.ui.customer_group.presenters.CustomerGroupListFragmentPresenter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.WarningDialog;
import com.jim.multipos.utils.rxevents.CustomerGroupEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerGroupListFragment extends BaseFragment implements CustomerGroupListFragmentView, CustomerGroupListAdapter.OnItemClickListener {
    public static final String CUSTOMER_GROUP_ADDED = "customer_group_added";
    public static final String CUSTOMER_GROUP_DELETE = "customer_group_delete";
    public static final String CUSTOMER_GROUP_DELETED = "customer_group_deleted";
    public static final String CUSTOMER_GROUP_NAME = "customer_group_name";
    public static final String CUSTOMER_GROUP_UPDATE = "customer_group_update";

    @Inject
    RxBusLocal rxBusLocal;
    @Inject
    CustomerGroupListFragmentPresenter presenter;
    @BindView(R.id.rvCustomerGroups)
    RecyclerView rvCustomerGroups;
    private ArrayList<Disposable> subscriptions = new ArrayList<>();
    private CustomerGroupListAdapter adapter;

    public void showCustomerGroups(List<CustomerGroup> customerGroups, int selectedPosition) {
        rvCustomerGroups.setItemAnimator(null);
        rvCustomerGroups.setLayoutManager(new GridLayoutManager(getContext(), 6));
        adapter = new CustomerGroupListAdapter(customerGroups, this, selectedPosition);
        rvCustomerGroups.setAdapter(adapter);
    }


    public void onAddButtonPressed() {
        UIUtils.closeKeyboard(rvCustomerGroups, getContext());
        presenter.addItemClicked();
    }


    public void onItemPressed(int t) {
        UIUtils.closeKeyboard(rvCustomerGroups, getContext());
        presenter.itemClicked(t);
    }

    @Override
    protected int getLayout() {
        return R.layout.customer_group_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.getCustomerGroups();
    }

    @Override
    protected void rxConnections() {
        subscriptions.add(rxBusLocal.toObservable().subscribe(o -> {
            if (o instanceof CustomerGroupEvent) {
                CustomerGroupEvent customerGroupEvent = (CustomerGroupEvent) o;

                if (customerGroupEvent.getEventType().equals(CUSTOMER_GROUP_ADDED)) {
                    presenter.customerGroupAdded();
                } else if (customerGroupEvent.getEventType().equals(CUSTOMER_GROUP_DELETE)) {
                    presenter.deleteCustomerGroup(customerGroupEvent.getCustomerGroup());
                } else if (customerGroupEvent.getEventType().equals(CUSTOMER_GROUP_NAME)) {
                    presenter.checkCustomerGroupName(customerGroupEvent.getCustomerGroup().getName());
                }
            }
        }));
    }

    @Override
    public void onDestroy() {
        RxBus.removeListners(subscriptions);
        super.onDestroy();
    }

    @Override
    public void showCustomerGroupRemoveWarningDialog() {
        WarningDialog warningDialog = new WarningDialog(getContext());
        warningDialog.onlyText(true);
        warningDialog.setWarningText(getString(R.string.can_not_remove_customer_group));
        warningDialog.setOnYesClickListener(view -> warningDialog.dismiss());
        warningDialog.show();
    }

    @Override
    public void showCustomerGroupSaveDialog() {
        WarningDialog warningDialog = new WarningDialog(getContext());
        warningDialog.setWarningText(getString(R.string.do_you_want_to_save_the_change));
        warningDialog.setOnYesClickListener(view -> {
            presenter.customerGroupUpdate();
            warningDialog.dismiss();
        });
        warningDialog.setOnNoClickListener(view -> {
            warningDialog.dismiss();
        });
        warningDialog.show();
    }

    @Override
    public void updateRV() {
        adapter.notifyDataSetChanged();
    }
}
