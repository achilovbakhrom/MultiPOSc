package com.jim.multipos.ui.customers.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customer_group_new.CustomerGroupActivity;
import com.jim.multipos.ui.customers.adapter.CustomerGroupsAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectCustomerGroupDialog extends Dialog {

    private CustomerGroupsAdapter adapter;
    private Context context;
    private final List<CustomerGroup> customerGroups;
    private final List<CustomerGroup> selectedCustomerGroups;

    public void setListener(OnGroupsSelected listener) {
        this.listener = listener;
    }

    private OnGroupsSelected listener;

    @BindView(R.id.rvCustomerGroups)
    RecyclerView rvCustomerGroups;
    @BindView(R.id.btnOK)
    MpButton btnOK;
    @BindView(R.id.btnAdd)
    Button btnAdd;

    public SelectCustomerGroupDialog(@NonNull Context context, List<CustomerGroup> customerGroups, List<CustomerGroup> selectedCustomerGroups) {
        super(context);
        this.context = context;
        this.customerGroups = customerGroups;
        this.selectedCustomerGroups = selectedCustomerGroups;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_dialog);
        getWindow().getDecorView().setBackgroundResource(R.color.colorTransparent);
        ButterKnife.bind(this);
        adapter = new CustomerGroupsAdapter(customerGroups, selectedCustomerGroups);
        rvCustomerGroups.setLayoutManager(new LinearLayoutManager(context));
        rvCustomerGroups.setAdapter(adapter);

        btnAdd.setOnClickListener(view -> {
            getContext().startActivity(new Intent(getContext(), CustomerGroupActivity.class));
        });

        btnOK.setOnClickListener(v -> {
            listener.onSelect(selectedCustomerGroups);
            dismiss();
        });
    }

    public interface OnGroupsSelected {
        void onSelect(List<CustomerGroup> groups);
    }
}
