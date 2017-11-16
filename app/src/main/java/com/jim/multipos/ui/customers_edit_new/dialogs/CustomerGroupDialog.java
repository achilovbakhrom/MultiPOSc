package com.jim.multipos.ui.customers_edit_new.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customer_group_new.CustomerGroupActivity;
import com.jim.multipos.ui.customers_edit_new.adapters.CustomerGroupAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Portable-Acer on 06.11.2017.
 */

public class CustomerGroupDialog extends Dialog {
    public interface DialogOnClickListener {
        void onOkClicked(Customer customer, boolean isModified);
    }

    @BindView(R.id.rvCustomerGroups)
    RecyclerView rvCustomerGroups;
    @BindView(R.id.btnOK)
    MpButton btnOK;
    @BindView(R.id.btnAdd)
    Button tvAdd;
    private List<CustomerGroup> customerGroups;
    private Customer customer;
    private DialogOnClickListener listener;

    public CustomerGroupDialog(@NonNull Context context, DialogOnClickListener listener, List<CustomerGroup> customerGroups, Customer customer) {
        super(context);
        this.listener = listener;
        this.customerGroups = customerGroups;
        this.customer = customer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_dialog);
        getWindow().getDecorView().setBackgroundResource(R.color.colorTransparent);

        ButterKnife.bind(this);

        rvCustomerGroups.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCustomerGroups.setAdapter(new CustomerGroupAdapter(customerGroups, customer.getCustomerGroups()));

        RxView.clicks(btnOK).subscribe(o -> {
            listener.onOkClicked(customer, ((CustomerGroupAdapter) rvCustomerGroups.getAdapter()).isModified());
            dismiss();
        });

        RxView.clicks(tvAdd).subscribe(o -> {
            getContext().startActivity(new Intent(getContext(), CustomerGroupActivity.class));
        });
    }
}
