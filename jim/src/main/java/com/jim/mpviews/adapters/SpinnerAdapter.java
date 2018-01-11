package com.jim.mpviews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jim.mpviews.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sirojiddin on 10.01.2018.
 */

public class SpinnerAdapter extends BaseAdapter {
    private Context context;
    private String[] objects;

    public SpinnerAdapter(Context context, String[] strings) {
        this.context = context;
        this.objects = strings;
    }

    @Override
    public int getCount() {
        return objects.length;
    }

    @Override
    public Object getItem(int position) {
        return objects[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View customSpinner = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_gravity_left, parent, false);
        TextView tvSpinnerItems = (TextView) customSpinner.findViewById(R.id.text1);
        tvSpinnerItems.setText(objects[position]);
        return customSpinner;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        View customSpinner = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_dropdown_item, parent, false);
        TextView tvSpinnerItems = (TextView) customSpinner.findViewById(R.id.tvSpinnerItems);
        tvSpinnerItems.setText(objects[position]);
        return customSpinner;
    }
}
