package com.jim.multipos.ui.customers_edit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpCheckbox;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.customer.CustomerGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;

/**
 * Created by user on 13.09.17.
 */

public class CustomerGroupAdapter extends RecyclerView.Adapter<CustomerGroupAdapter.ViewHolder> {
    private Context context;
    private List<CustomerGroup> customerGroups;
    private List<CustomerGroup> selectedCustomerGroups;
    private List<CustomerGroup> currentSelectedItems;

    public CustomerGroupAdapter(Context context, List<CustomerGroup> customerGroups, List<CustomerGroup> selectedCustomerGroups) {
        this.context = context;
        this.customerGroups = customerGroups;
        this.selectedCustomerGroups = selectedCustomerGroups;
        currentSelectedItems = new ArrayList<>();
        currentSelectedItems.addAll(selectedCustomerGroups);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_group_dialog_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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

    public List<CustomerGroup> getSelectedItems() {
        return currentSelectedItems;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chbCustomerGroup)
        MpCheckbox chbCustomerGroup;
        @BindView(R.id.tvCustomerGroupName)
        TextView tvCustomerGroupName;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            RxView.clicks(itemView).subscribe(o -> {
                clickHandler();
            });

            RxView.clicks(chbCustomerGroup).subscribe(o -> {
                clickHandler();
            });
        }

        private void clickHandler() {
            CustomerGroup customerGroup = customerGroups.get(getAdapterPosition());

            if (chbCustomerGroup.isCheckboxChecked()) {
                chbCustomerGroup.setChecked(false);
                currentSelectedItems.remove(customerGroup);
            } else {
                chbCustomerGroup.setChecked(true);
                currentSelectedItems.add(customerGroup);
            }
        }
    }
}
