package com.jim.multipos.ui.customers_edit.adapters;

import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by user on 13.09.17.
 */

public class CustomerGroupAdapter extends BaseAdapter<CustomerGroup, CustomerGroupAdapter.ViewHolder> {
    private List<CustomerGroup> selectedCustomerGroups;
    private List<CustomerGroup> currentSelectedItems;

    public CustomerGroupAdapter(List<CustomerGroup> customerGroups, List<CustomerGroup> selectedCustomerGroups) {
        super(customerGroups);
        this.selectedCustomerGroups = selectedCustomerGroups;
        currentSelectedItems = new ArrayList<>();
        currentSelectedItems.addAll(selectedCustomerGroups);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_group_dialog_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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

    public List<CustomerGroup> getSelectedItems() {
        return currentSelectedItems;
    }

    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.chbCustomerGroup)
        MpCheckbox chbCustomerGroup;
        @BindView(R.id.tvCustomerGroupName)
        TextView tvCustomerGroupName;

        public ViewHolder(View itemView) {
            super(itemView);

            RxView.clicks(itemView).subscribe(o -> {
                clickHandler();
            });

            RxView.clicks(chbCustomerGroup).subscribe(o -> {
                clickHandler();
            });
        }

        private void clickHandler() {
            CustomerGroup customerGroup = getItem(getAdapterPosition());

            if (chbCustomerGroup.isChecked()) {
                chbCustomerGroup.setChecked(false);
                for (int i = 0; i < currentSelectedItems.size(); i++) {
                    if (currentSelectedItems.get(i).getId() == customerGroup.getId()) {
                        currentSelectedItems.remove(i);
                    }
                }
            } else {
                chbCustomerGroup.setChecked(true);
                currentSelectedItems.add(customerGroup);
            }
        }
    }
}
