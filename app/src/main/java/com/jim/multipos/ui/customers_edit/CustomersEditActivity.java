package com.jim.multipos.ui.customers_edit;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.di.BaseAppComponent;
import com.jim.multipos.ui.HasComponent;
import com.jim.multipos.ui.customers_edit.adapters.CustomerAdapter;
import com.jim.multipos.ui.customers_edit.adapters.CustomerGroupSpinnerAdapter;
import com.jim.multipos.ui.customers_edit.connector.CustomersEditConnector;
import com.jim.multipos.ui.customers_edit.di.CustomersEditActivityComponent;
import com.jim.multipos.ui.customers_edit.di.CustomersEditActivityModule;
import com.jim.multipos.ui.customers_edit.dialogs.CustomerGroupDialog;
import com.jim.multipos.ui.customers_edit.entity.CustomersWithCustomerGroups;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.CustomerGroupEvent;
import com.jim.multipos.utils.rxevents.Unsibscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

public class CustomersEditActivity extends BaseActivity implements HasComponent<CustomersEditActivityComponent>, CustomersEditView, CustomerAdapter.OnClick, CustomerGroupDialog.OnClickListener {
    @Inject
    RxBus rxBus;
    @Inject
    RxBusLocal rxBusLocal;
    @Inject
    CustomersEditPresenter presenter;
    @BindView(R.id.spCustomerGroup)
    MpSpinner spCustomerGroup;
    @BindView(R.id.tvId)
    TextView tvId;
    @BindView(R.id.etFullName)
    MpEditText etFullName;
    @BindView(R.id.etPhone)
    MpEditText etPhone;
    @BindView(R.id.etAddress)
    MpEditText etAddress;
    @BindView(R.id.tvQrCode)
    TextView tvQrCode;
    @BindView(R.id.ivRefreshQrCode)
    ImageView ivRefreshQrCode;
    @BindView(R.id.tvCustomerGroup)
    TextView tvCustomerGroup;
    @BindView(R.id.ivAdd)
    ImageView ivAdd;
    @BindView(R.id.rvCustomers)
    RecyclerView rvCustomers;
    @BindView(R.id.btnSave)
    MpButton btnSave;
    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    private CustomersEditActivityComponent component;
    private Unbinder unbinder;
    private CustomerAdapter adapter;
    private CustomerGroupDialog rvItemCustomerGroupDialog;
    private CustomerGroupDialog customerGroupDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_fragment);

        unbinder = ButterKnife.bind(this);

        presenter.getCustomerGroups();
        presenter.getCustomers();
        presenter.getClientId();
        presenter.getQrCode();

        RxView.clicks(ivAdd).subscribe(aVoid -> {
            String clientId = tvId.getText().toString();
            String fullName = etFullName.getText().toString();
            String phone = etPhone.getText().toString();
            String address = etAddress.getText().toString();
            String qrCode = tvQrCode.getText().toString();

            presenter.addCustomer(clientId, fullName, phone, address, qrCode);
        });

        RxView.clicks(ivRefreshQrCode).subscribe(aVoid -> {
            presenter.getQrCode();
        });

        RxView.clicks(btnSave).subscribe(aVoid -> {
            presenter.save();
        });

        RxView.clicks(btnCancel).subscribe(aVoid -> {
            presenter.cancel();
        });

        RxView.clicks(tvCustomerGroup).subscribe(o -> {
            presenter.getDialogData();
        });

        spCustomerGroup.setOnItemSelectedListener((adapterView, view, i, l) -> {
            presenter.filterCustomerGroups(i);
        });
    }

    @Override
    public void showCustomerCustomerGroupsDialog(int position) {
        presenter.getCustomerCustomerGroups(position);
    }

    @Override
    protected void onDestroy() {
        rxBus.send(new Unsibscribe(CustomersEditActivity.class.getName()));
        unbinder.unbind();

        super.onDestroy();
    }

    protected void setupComponent(BaseAppComponent baseAppComponent) {
//        component = baseAppComponent.plus(new CustomersEditActivityModule(this));
        component.inject(this);
    }

    @Override
    public CustomersEditActivityComponent getComponent() {
        return component;
    }

    @Override
    public void clearViews() {
        etFullName.setText("");
        etPhone.setText("");
        etAddress.setText("");
    }

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
    }

    @Override
    public void showCustomersWithCustomerGroups(List<CustomersWithCustomerGroups> customersWithCustomerGroups) {
        adapter = new CustomerAdapter(this, customersWithCustomerGroups, this);

        rvCustomers.setLayoutManager(new LinearLayoutManager(this));
        rvCustomers.setAdapter(adapter);
    }

    @Override
    public void showCustomerGroups(List<CustomerGroup> customerGroups) {
        spCustomerGroup.setAdapter(new CustomerGroupSpinnerAdapter(this, R.layout.item_spinner, customerGroups));
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
    public void showSelectedCustomerGroups(String selectedCustomerGroups) {
        tvCustomerGroup.setText(selectedCustomerGroups);
    }

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
    public void updateCustomerGroupDialog() {
        if (customerGroupDialog != null && customerGroupDialog.isShowing()) {
            customerGroupDialog.dismiss();
            presenter.getDialogData();
        }

        if (rvItemCustomerGroupDialog != null && rvItemCustomerGroupDialog.isShowing()) {
            rvItemCustomerGroupDialog.dismiss();
            presenter.getCustomerCustomerGroups(-1);
        }
    }
}
