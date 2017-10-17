package com.jim.multipos.ui.customer_group.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpCheckbox;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 08.09.17.
 */

public class CustomerGroupMembersAdapter extends RecyclerView.Adapter<CustomerGroupMembersAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void itemClicked(int position, boolean checked);
    }

    private CustomerGroup customerGroups;
    private List<Customer> customers;
    private OnItemClickListener callback;

    public CustomerGroupMembersAdapter(OnItemClickListener callback, CustomerGroup customerGroups, List<Customer> customers) {
        this.callback = callback;
        this.customerGroups = customerGroups;
        this.customers = customers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_group_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Customer customer = customers.get(position);

        holder.tvClientId.setText(customer.getClientId().toString());
        holder.tvFullName.setText(customer.getName());
        holder.tvPhoneNumber.setText(customer.getPhoneNumber());

        if (customerGroups.getName() != null) {
            List<Customer> c = customerGroups.getCustomers();

            for (Customer x : c) {
                if (x.getId().equals(customer.getId())) {
                    holder.chbMember.setChecked(true);
                    break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvClientId)
        TextView tvClientId;
        @BindView(R.id.tvFullName)
        TextView tvFullName;
        @BindView(R.id.tvPhoneNumber)
        TextView tvPhoneNumber;
        @BindView(R.id.chbMember)
        MpCheckbox chbMember;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            RxView.clicks(chbMember).subscribe(o -> {
                if (chbMember.isChecked()) {
                    chbMember.setChecked(false);
                } else {
                    chbMember.setChecked(true);
                }

                callback.itemClicked(getAdapterPosition(), chbMember.isChecked());
            });
        }
    }
}
