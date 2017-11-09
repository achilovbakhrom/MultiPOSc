package com.jim.multipos.ui.customers_edit;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customers_edit.adapters.CustomerAdapter;
import com.jim.multipos.ui.customers_edit.adapters.CustomerGroupSpinnerAdapter;
import com.jim.multipos.ui.customers_edit.dialogs.CustomerGroupDialog;
import com.jim.multipos.ui.customers_edit.entity.CustomersWithCustomerGroups;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.rxevents.CustomerGroupEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

public class CustomersEditActivity extends BaseActivity implements CustomersEditView, CustomerAdapter.OnClickListener, CustomerGroupDialog.OnClickListener {
    public static final String CUSTOMER_GROUP_ADDED = "customer_groups_added";

    @Inject
    RxBus rxBus;
    @Inject
    CustomersEditPresenter presenter;
    /*@BindView(R.id.spCustomerGroup)
    MPosSpinner spCustomerGroup;
    @BindView(R.id.toolbar)
    MpToolbar toolbar;
    @BindView(R.id.rvCustomers)*/
    RecyclerView rvCustomers;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    @BindView(R.id.tvFullName)
    TextView tvFullName;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvClientId)
    TextView tvClientId;
    @BindView(R.id.tvQrCode)
    TextView tvQrCode;
    private Unbinder unbinder;
    private CustomerAdapter adapter;
    private CustomerGroupDialog rvItemCustomerGroupDialog;
    private CustomerGroupDialog customerGroupDialog;
    private ArrayList<Disposable> subscriptions;
    private boolean isFullNameClicked = false;
    private boolean isPhoneClicked = false;
    private boolean isAddressClicked = false;
    private boolean isIdClicked = false;
    private boolean isQrCodeClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_fragment);

        unbinder = ButterKnife.bind(this);

        //toolbar.setMode(MpToolbar.DEFAULT_TYPE);

        presenter.getCustomerGroups();
        presenter.getCustomers();
        /*presenter.getClientId();
        presenter.getQrCode();*/
        rxConnections();

        RxView.clicks(btnBack).subscribe(aVoid -> {
            presenter.back();
        });

        /*spCustomerGroup.setItemSelectionListener((view, position) -> {
            UIUtils.closeKeyboard(tvFullName, getBaseContext());
            presenter.filterCustomerGroups(position);
        });*/

        RxView.clicks(tvFullName).subscribe(o -> {
            UIUtils.closeKeyboard(tvFullName, getBaseContext());
            if (isFullNameClicked) {
                isFullNameClicked = false;
                presenter.sortByIdDesc();
            } else {
                isFullNameClicked = true;
                presenter.sortByName();
            }
        });

        RxView.clicks(tvPhone).subscribe(o -> {
            UIUtils.closeKeyboard(tvFullName, getBaseContext());
            if (isPhoneClicked) {
                isPhoneClicked = false;
                presenter.sortByIdDesc();
            } else {
                isPhoneClicked = true;
                presenter.sortByPhone();
            }
        });

        RxView.clicks(tvAddress).subscribe(o -> {
            UIUtils.closeKeyboard(tvFullName, getBaseContext());
            if (isAddressClicked) {
                isAddressClicked = false;
                presenter.sortByIdDesc();
            } else {
                isAddressClicked = true;
                presenter.sortByAddress();
            }
        });

        RxView.clicks(tvClientId).subscribe(o -> {
            UIUtils.closeKeyboard(tvFullName, getBaseContext());
            if (isIdClicked) {
                isIdClicked = false;
                presenter.sortByIdDesc();
            } else {
                isIdClicked = true;
                presenter.sortByClientId();
            }
        });

        RxView.clicks(tvQrCode).subscribe(o -> {
            UIUtils.closeKeyboard(tvFullName, getBaseContext());
            if (isQrCodeClicked) {
                isQrCodeClicked = false;
                presenter.sortByIdDesc();
            } else {
                isQrCodeClicked = true;
                presenter.sortByQrCode();
            }
        });
    }

    @Override
    public void showCustomerCustomerGroupsDialog(int position) {
        presenter.getCustomerCustomerGroups(position);
    }

    @Override
    protected void onDestroy() {
        RxBus.removeListners(subscriptions);
        unbinder.unbind();

        super.onDestroy();
    }

    @Override
    public void showCustomerCustomerGroupsDialog() {
        presenter.getCustomerCustomerGroups();
    }

    @Override
    public void refreshCustomerItemQrCode() {
        presenter.refreshQrCode();
    }
/*@Override


    @Override
    public void requestFocus() {
        etFullName.requestFocus();
    }

    @Override
    public void showQrCode(String qrCode) {
        tvQrCode.setText(qrCode);
    }

    @Override
    public void showClientId(String clientId) {
        tvId.setText(clientId);
    }

    @Override
    public void showFullNameError(String error) {
        etFullName.setError(error);
    }

    @Override
    public void showPhoneError(String error) {
        etPhone.setError(error);
    }

    @Override
    public void showAddressError(String error) {
        etAddress.setError(error);
    }*/

    /*@Override
    public void showCustomersWithCustomerGroups(List<CustomersWithCustomerGroups> customersWithCustomerGroups, String clientId, String qrCode) {
        adapter = new CustomerAdapter(this, customersWithCustomerGroups, clientId, qrCode, this);

        rvCustomers.setLayoutManager(new LinearLayoutManager(this));
        rvCustomers.setAdapter(adapter);
    }*/

    @Override
    public void showCustomersWithCustomerGroups(List<CustomerGroup> customerTypeSelectedCustomerGroups, List<CustomersWithCustomerGroups> customersWithCustomerGroups, String clientId, String qrCode) {
        adapter = new CustomerAdapter(this, customerTypeSelectedCustomerGroups, customersWithCustomerGroups, clientId, qrCode, this);

        rvCustomers.setLayoutManager(new LinearLayoutManager(this));
        rvCustomers.setAdapter(adapter);
    }

    @Override
    public void showCustomerGroups(List<CustomerGroup> customerGroups) {
        //spCustomerGroup.setAdapter(new CustomerGroupSpinnerAdapter(customerGroups));
    }

    @Override
    public void updateRecyclerView(List<CustomersWithCustomerGroups> customersWithCustomerGroups) {
        ((CustomerAdapter) rvCustomers.getAdapter()).setCustomersList(customersWithCustomerGroups);
    }

    @Override
    public void updateClientId() {
        presenter.showClientId();
    }

    @Override
    public void showClientId(String clientId) {
        ((CustomerAdapter) rvCustomers.getAdapter()).updateClientId(clientId);
    }

    @Override
    public void refreshQrCode(int position) {
        presenter.refreshQrCode(position);
    }

    @Override
    public void removeCustomer(int position) {
        presenter.removeCustomer(position);
    }

    @Override
    public void saveCustomer(int position, String fullName, String phone, String address, String qrCode) {
        presenter.saveCustomer(position, fullName, phone, address, qrCode);
    }

    @Override
    public void updateRecyclerView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateCustomerTypeQrCode(String qrCode) {
        ((CustomerAdapter) rvCustomers.getAdapter()).refreshCustomerTypeQrCode(qrCode);
    }

    @Override
    public void save() {
        finish();
    }

    @Override
    public void cancel() {
        finish();
    }

    @Override
    public void onClick(List<CustomerGroup> selectedItems) {
        presenter.customerGroupsSelected(selectedItems);
    }

    @Override
    public void addCustomer(String clientId, String fullName, String phone, String address, String qrCode, List<CustomerGroup> selectedGroups) {
        presenter.addCustomer(clientId, fullName, phone, address, qrCode, selectedGroups);
    }

/*@Override
    public void showSelectedCustomerGroups(String selectedCustomerGroups) {
        tvCustomerGroup.setText(selectedCustomerGroups);
    }*/

    @Override
    public void showCustomerGroupDialog(List<CustomerGroup> customerGroups, List<CustomerGroup> selectedCustomerGroups) {
        customerGroupDialog = new CustomerGroupDialog(this, customerGroups, selectedCustomerGroups, this);
        customerGroupDialog.show();
    }

    @Override
    public void showRVItemCustomerGroupDialog(int position, List<CustomerGroup> customerGroups, List<CustomerGroup> selectedCustomerGroups) {
        rvItemCustomerGroupDialog = new CustomerGroupDialog(this, customerGroups, selectedCustomerGroups, selectedItems -> {
            presenter.updateCustomerCustomerGroup(position, selectedItems);
        });
        rvItemCustomerGroupDialog.show();
    }

    @Override
    public void showRVItemCustomerGroupDialog(List<CustomerGroup> customerGroups, List<CustomerGroup> selectedCustomerGroups) {
        rvItemCustomerGroupDialog = new CustomerGroupDialog(this, customerGroups, selectedCustomerGroups, selectedItems -> {
            presenter.updateCustomerCustomerGroup(selectedItems);
        });

        rvItemCustomerGroupDialog.show();
    }

    @Override
    public void updateCustomerGroupDialog() {
        if (customerGroupDialog != null && customerGroupDialog.isShowing()) {
            customerGroupDialog.dismiss();
            presenter.showCustomerGroupDialog();
        }

        if (rvItemCustomerGroupDialog != null && rvItemCustomerGroupDialog.isShowing()) {
            rvItemCustomerGroupDialog.dismiss();
            presenter.getCustomerCustomerGroups(-1);
        }
    }

    private void rxConnections() {
        subscriptions = new ArrayList<>();
        subscriptions.add(rxBus.toObservable().subscribe(o -> {
            if (o instanceof CustomerGroupEvent) {
                CustomerGroupEvent customerGroupEvent = (CustomerGroupEvent) o;

                if (customerGroupEvent.getEventType().equals(CUSTOMER_GROUP_ADDED)) {
                    presenter.showCustomerGroups();
                }
            }
        }));
    }

    @Override
    public boolean isCustomerExists(String name) {
        return presenter.isCustomerExists(name);
    }
}
