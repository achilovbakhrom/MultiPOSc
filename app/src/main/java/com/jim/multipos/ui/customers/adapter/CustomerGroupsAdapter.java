package com.jim.multipos.ui.customers.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.mpviews.MpCheckbox;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseAdapter;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.data.db.model.customer.CustomerGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Portable-Acer on 07.11.2017.
 */

public class CustomerGroupsAdapter extends RecyclerView.Adapter<CustomerGroupsAdapter.CustomerGroupViewHolder> {

    private List<CustomerGroup> customerGroups;
    private List<CustomerGroup> selectedCustomerGroups;

    public CustomerGroupsAdapter(List<CustomerGroup> customerGroups, List<CustomerGroup> selectedCustomerGroups) {
        this.customerGroups = customerGroups;
        this.selectedCustomerGroups = selectedCustomerGroups;
    }

    @NonNull
    @Override
    public CustomerGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_group_dialog_item, parent, false);
        return new CustomerGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerGroupViewHolder holder, int position) {
        if (!selectedCustomerGroups.isEmpty()) {
            for (CustomerGroup cg : selectedCustomerGroups) {
                if (customerGroups.get(position).getId().equals(cg.getId())) {
                    holder.chbCustomerGroup.setChecked(true);
                    break;
                }
            }
        }
        holder.tvCustomerGroupName.setText(customerGroups.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return customerGroups.size();
    }

    class CustomerGroupViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chbCustomerGroup)
        MpCheckbox chbCustomerGroup;
        @BindView(R.id.tvCustomerGroupName)
        TextView tvCustomerGroupName;

        public CustomerGroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> onClickHandler());
            chbCustomerGroup.setOnClickListener(v -> onClickHandler());
        }

        private void onClickHandler() {
            if (chbCustomerGroup.isChecked()) {
                chbCustomerGroup.setChecked(false);
                selectedCustomerGroups.remove(customerGroups.get(getAdapterPosition()));
            } else {
                chbCustomerGroup.setChecked(true);
                selectedCustomerGroups.add(customerGroups.get(getAdapterPosition()));
            }
        }
    }
}
