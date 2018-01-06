package com.jim.multipos.ui.mainpospage.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.customer.Customer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 05.01.2018.
 */

public class CustomerSearchResultsAdapter extends RecyclerView.Adapter<CustomerSearchResultsAdapter.CustomerSearchViewHolder> {

    private List<Customer> items;

    public CustomerSearchResultsAdapter() {
        items = new ArrayList<>();
    }

    @Override
    public CustomerSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_search_item, parent, false);
        return new CustomerSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerSearchViewHolder holder, int position) {
        holder.tvCustomerName.setText(items.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<Customer> customers) {
        this.items = customers;
        notifyDataSetChanged();
    }

    class CustomerSearchViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvCustomerName)
        TextView tvCustomerName;

        public CustomerSearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
