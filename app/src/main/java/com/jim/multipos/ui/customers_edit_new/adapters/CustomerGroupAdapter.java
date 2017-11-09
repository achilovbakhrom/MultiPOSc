package com.jim.multipos.ui.customers_edit_new.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpCheckbox;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseAdapter;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.data.db.model.customer.CustomerGroup;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Portable-Acer on 07.11.2017.
 */

public class CustomerGroupAdapter extends BaseAdapter<CustomerGroup, CustomerGroupAdapter.CustomerGroupViewHolder> {
    private List<CustomerGroup> customerGroups;
    private List<CustomerGroup> selectedCustomerGroups;
    private boolean isModified = false;

    public CustomerGroupAdapter(List<CustomerGroup> customerGroups, List<CustomerGroup> selectedCustomerGroups) {
        super(customerGroups);
        this.customerGroups = customerGroups;
        this.selectedCustomerGroups = selectedCustomerGroups;
    }

    @Override
    public CustomerGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_group_dialog_item, parent, false);

        return new CustomerGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerGroupViewHolder holder, int position) {
        if (!selectedCustomerGroups.isEmpty()) {
            for (CustomerGroup cg : selectedCustomerGroups) {
                if (getItem(position).getId().equals(cg.getId())) {
                    holder.chbCustomerGroup.setChecked(true);
                    break;
                }
            }
        }

        holder.tvCustomerGroupName.setText(getItem(position).getName());
    }

    public List<CustomerGroup> getSelectedCustomerGroups() {
        return selectedCustomerGroups;
    }

    public boolean isModified() {
        return isModified;
    }

    class CustomerGroupViewHolder extends BaseViewHolder {
        @BindView(R.id.chbCustomerGroup)
        MpCheckbox chbCustomerGroup;
        @BindView(R.id.tvCustomerGroupName)
        TextView tvCustomerGroupName;

        public CustomerGroupViewHolder(View itemView) {
            super(itemView);

            RxView.clicks(itemView).subscribe(o -> {
                onClickHandler();
            });

            RxView.clicks(chbCustomerGroup).subscribe(o -> {
                onClickHandler();
            });
        }

        private void onClickHandler() {
            if (chbCustomerGroup.isChecked()) {
                chbCustomerGroup.setChecked(false);
                selectedCustomerGroups.remove(customerGroups.get(getAdapterPosition()));
                isModified = true;
            } else {
                chbCustomerGroup.setChecked(true);
                selectedCustomerGroups.add(customerGroups.get(getAdapterPosition()));
                isModified = true;
            }
        }
    }
}
