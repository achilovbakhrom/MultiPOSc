package com.jim.multipos.ui.customers_edit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.customer.CustomerGroup;

import java.util.List;

/**
 * Created by user on 13.09.17.
 */

public class CustomerGroupSpinnerAdapter extends BaseAdapter {
    private List<CustomerGroup> customerGroups;

    public CustomerGroupSpinnerAdapter(List<CustomerGroup> customerGroups) {
        this.customerGroups = customerGroups;
    }

    @Override
    public int getCount() {
        return customerGroups.size();
    }

    @Override
    public Object getItem(int position) {
        return customerGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner, parent, false);

        TextView tv = (TextView) view.findViewById(R.id.tvItemText);
        tv.setText(customerGroups.get(position).getName());

        return view;
    }
}
