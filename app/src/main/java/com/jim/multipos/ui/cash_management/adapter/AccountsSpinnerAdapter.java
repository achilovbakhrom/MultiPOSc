package com.jim.multipos.ui.cash_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jim.mpviews.R;

import java.util.List;

/**
 * Created by Sirojiddin on 10.01.2018.
 */

public class AccountsSpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<String> objects;

    public AccountsSpinnerAdapter(Context context, List<String> strings) {
        this.context = context;
        this.objects = strings;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View customSpinner = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_gravity_right, parent, false);
        TextView tvSpinnerItems = customSpinner.findViewById(R.id.text1);
        tvSpinnerItems.setText(objects.get(position));
        return customSpinner;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        View customSpinner = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_dropdown_item, parent, false);
        TextView tvSpinnerItems = customSpinner.findViewById(R.id.tvSpinnerItems);
        tvSpinnerItems.setText(objects.get(position));
        return customSpinner;
    }
}
