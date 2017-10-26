package com.jim.multipos.ui.customer_group.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.ServiceFee;

import java.util.List;

/**
 * Created by user on 30.08.17.
 */

public class ServiceFeeSpinnerAdapter extends BaseAdapter {
    private List<ServiceFee> serviceFees;

    public ServiceFeeSpinnerAdapter(List<ServiceFee> serviceFees) {
        this.serviceFees = serviceFees;
    }

    @Override
    public int getCount() {
        return serviceFees.size();
    }

    @Override
    public Object getItem(int position) {
        return serviceFees.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner, parent, false);

        TextView tv = (TextView) view.findViewById(R.id.tvItemText);
        tv.setText(serviceFees.get(position).getName());

        return view;
    }
}
