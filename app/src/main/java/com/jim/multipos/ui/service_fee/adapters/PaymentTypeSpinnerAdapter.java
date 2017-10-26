package com.jim.multipos.ui.service_fee.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.PaymentType;

import java.util.List;

/**
 * Created by user on 18.10.17.
 */

public class PaymentTypeSpinnerAdapter extends BaseAdapter {
    private List<PaymentType> paymentTypes;

    public PaymentTypeSpinnerAdapter(List<PaymentType> paymentTypes) {
        this.paymentTypes = paymentTypes;
    }

    @Override
    public int getCount() {
        return paymentTypes.size();
    }

    @Override
    public Object getItem(int position) {
        return paymentTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(convertView.getContext()).inflate(R.layout.item_spinner, parent, false);

        TextView tv = (TextView) view.findViewById(R.id.tvItemText);
        tv.setText(paymentTypes.get(position).getName());

        return view;
    }
}
