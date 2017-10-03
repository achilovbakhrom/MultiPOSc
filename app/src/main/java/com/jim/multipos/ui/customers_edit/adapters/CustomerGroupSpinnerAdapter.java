package com.jim.multipos.ui.customers_edit.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.customer.CustomerGroup;

import java.util.List;

/**
 * Created by user on 13.09.17.
 */

public class CustomerGroupSpinnerAdapter extends ArrayAdapter {
    private List<CustomerGroup> customerGroups;
    private LayoutInflater inflater;

    public CustomerGroupSpinnerAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);

        customerGroups = objects;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return customerGroups.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getViewItem(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getViewItem(position, convertView, parent);
    }

    private View getViewItem(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_spinner, parent, false);

        TextView tv = (TextView) view.findViewById(R.id.tvItemText);
        tv.setText(customerGroups.get(position).getName());

        return view;
    }
}
