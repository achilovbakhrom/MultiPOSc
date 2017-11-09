package com.jim.multipos.ui.customers_edit_new;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customers_edit_new.adapters.CustomerAdapter;
import com.jim.multipos.ui.customers_edit_new.dialogs.CustomerGroupDialog;
import com.jim.multipos.utils.WarningDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CustomersEditActivity extends BaseActivity implements CustomersEditView, CustomerAdapter.OnClickListener, CustomerGroupDialog.DialogOnClickListener {
    @Inject
    CustomersEditPresenter presenter;
    @BindView(R.id.toolbar)
    MpToolbar toolbar;
    @BindView(R.id.rvItems)
    RecyclerView rvItems;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    private Unbinder unbinder;
    private CustomerAdapter adapter;
    private int position;
    private CustomerGroupDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_fragment);

        unbinder = ButterKnife.bind(this);

        toolbar.setMode(MpToolbar.DEFAULT_TYPE);
        init();
    }

    private void init() {
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomerAdapter(this, presenter.getCustomers(), this);
        rvItems.setAdapter(adapter);

        RxView.clicks(btnBack).subscribe(o -> {
            if (adapter.getNotSavedItemCount() == 0) {
                finish();
            } else {
                WarningDialog warningDialog = new WarningDialog(this);
                warningDialog.setWarningText(getString(R.string.you_have_unsaved_customers, adapter.getNotSavedItemCount()));
                warningDialog.setOnYesClickListener(view -> finish());
                warningDialog.setOnNoClickListener(view -> warningDialog.dismiss());
                warningDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            if (position != -1) {
                showItemCustomerGroupsDialog(position);
            } else {
                showAddCustomerGroupDialog(adapter.getCustomer());
            }
        }
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();

        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onAddClicked(String clientId, String fullName, String phone, String address, String qrCode, List<CustomerGroup> customerGroups) {
        presenter.addCustomer(clientId, fullName, phone, address, qrCode, customerGroups);
    }

    @Override
    public void onSaveClicked(Customer customer) {
        presenter.updateCustomer(customer);
    }

    @Override
    public void onDeleteClicked(Customer customer) {
        WarningDialog removeWarningDialog = new WarningDialog(this);
        removeWarningDialog.setWarningText(getString(R.string.do_you_want_delete));
        removeWarningDialog.setOnYesClickListener(view -> {
            presenter.removeCustomer(customer);
            removeWarningDialog.dismiss();
        });
        removeWarningDialog.setOnNoClickListener(view -> {
            removeWarningDialog.dismiss();
        });
        removeWarningDialog.show();
    }

    @Override
    public void onSortByClientId() {
        presenter.sortByClientId();
    }

    @Override
    public void onSortByFullName() {
        presenter.sortByFullName();
    }

    @Override
    public void onSortByAddress() {
        presenter.sortByAddress();
    }

    @Override
    public void onSortByQrCode() {
        presenter.sortByQrCode();
    }

    @Override
    public void onSortByDefault() {
        presenter.sortByDefault();
    }

    @Override
    public Long getQrCode() {
        return presenter.getQrCode();
    }

    @Override
    public Long getClientId() {
        return presenter.getClientId();
    }

    @Override
    public boolean isCustomerExists(String name) {
        return presenter.isCustomerExists(name);
    }

    @Override
    public void customerAdded(Customer customer) {
        adapter.addItem(customer);
    }

    @Override
    public void customerRemoved(Customer customer) {
        adapter.removeItem(customer);
    }

    @Override
    public void updateRecyclerView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showItemCustomerGroupsDialog(int position) {
        this.position = position;
        dialog = new CustomerGroupDialog(this, this, presenter.getCustomerGroups(), presenter.getCustomers().get(position));
        dialog.show();
    }

    @Override
    public void showAddCustomerGroupDialog(Customer customer) {
        position = -1;
        dialog = new CustomerGroupDialog(this, this, presenter.getCustomerGroups(), customer);
        dialog.show();
    }

    @Override
    public void onOkClicked(Customer customer, boolean isModified) {
        adapter.updateItem(customer, isModified);
    }
}
