package com.jim.multipos.ui.customers.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customers.adapter.CustomersAdapter;
import com.jim.multipos.ui.customers.model.CustomerAdapterDetails;
import com.jim.multipos.utils.BarcodeStack;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.WarningDialog;
import com.jim.multipos.utils.rxevents.main_order_events.CustomerEvent;
import com.jim.multipos.utils.rxevents.main_order_events.CustomerGroupEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

public class CustomerFragment extends BaseFragment implements CustomerFragmentView {

    @Inject
    CustomersAdapter customersAdapter;
    @Inject
    CustomerFragmentPresenter presenter;
    @Inject
    BarcodeStack barcodeStack;
    @Inject
    RxBus rxBus;
    @BindView(R.id.rvCustomers)
    RecyclerView rvCustomers;
    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    private int scanPosition = -1;
    private int position = -1;
    private ArrayList<Disposable> subscriptions;

    @Override
    protected void rxConnections() {
        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBus.toObservable().subscribe(o -> {
                    if (o instanceof CustomerGroupEvent) {
                        presenter.initData();
                    }
                }));
    }

    @Override
    protected int getLayout() {
        return R.layout.customers_fragment_layout;
    }



    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.initData();
        customersAdapter.setBarcodeStack(barcodeStack);
        customersAdapter.setListener(new CustomersAdapter.OnCustomerCallback() {

            @Override
            public void onAddPressed(String id, String name, String phone, String address, String qrCode, List<CustomerGroup> groups) {
                presenter.onAddPressed(id, name, phone, address, qrCode, groups);
            }

            @Override
            public void onSave(String name, String phone, String address, String qrCode, List<CustomerGroup> groups, Customer customer) {
                presenter.onSave(name, phone, address, qrCode, groups, customer);
            }

            @Override
            public void onDelete(Customer customer) {
                presenter.onDelete(customer);
            }

            @Override
            public void onGroupSelected(int position) {
                CustomerFragment.this.position = position;
            }

            @Override
            public void sortList(CustomerFragmentPresenterImpl.CustomerSortTypes customerSortTypes) {
                presenter.sortList(customerSortTypes);
            }

            @Override
            public void scanBarcode() {
                IntentIntegrator.forSupportFragment(CustomerFragment.this).initiateScan();
            }

            @Override
            public void scanBarcode(int position) {
                scanPosition = position;
                IntentIntegrator.forSupportFragment(CustomerFragment.this).initiateScan();
            }
        });

        rvCustomers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCustomers.setAdapter(customersAdapter);
        ((SimpleItemAnimator) rvCustomers.getItemAnimator()).setSupportsChangeAnimations(false);
        btnCancel.setOnClickListener(view -> presenter.onCloseAction(customersAdapter.getAddItem()));
    }

    @Override
    public void refreshList(List<CustomerAdapterDetails> items, List<CustomerGroup> groups, long id) {
        customersAdapter.setData(items, groups, id);
        customersAdapter.notifyDataSetChanged();
        if (position != -1){
            customersAdapter.openCustomerGroupDialog(position);
            position = -1;
        }
    }

    @Override
    public void sendEvent(int type, Customer customer) {
        rxBus.send(new CustomerEvent(customer, type));
    }

    @Override
    public void notifyItemAdd(int position) {
        customersAdapter.notifyItemInserted(position);
    }

    @Override
    public void notifyItemChanged(int position) {
        customersAdapter.notifyItemChanged(position);
    }

    @Override
    public void notifyItemRemove(int position) {
        customersAdapter.notifyItemRemoved(position);
    }

    @Override
    public void refreshList() {
        customersAdapter.notifyDataSetChanged();
    }

    @Override
    public void closeActivity() {
        getActivity().finish();
    }

    @Override
    public void openWarning() {
        WarningDialog warningDialog = new WarningDialog(getContext());
        warningDialog.setWarningMessage(getString(R.string.warning_discard_changes));
        warningDialog.setOnYesClickListener(view1 -> warningDialog.dismiss());
        warningDialog.setOnNoClickListener(view1 -> closeActivity());
        warningDialog.setPositiveButtonText(getString(R.string.cancel));
        warningDialog.setNegativeButtonText(getString(R.string.discard));
        warningDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() != null) {
                if (scanPosition == -1) {
                    customersAdapter.setBarcode(intentResult.getContents());
                } else {
                    customersAdapter.setBarcodeToPosition(intentResult.getContents(), scanPosition);
                    scanPosition = -1;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.removeListners(subscriptions);
    }
}
