package com.jim.multipos.ui.first_configure.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.currency.Currency;

import java.util.List;

/**
 * Created by user on 19.08.17.
 */

public class CurrencySpinnerAdapter extends ArrayAdapter {
    private List<Currency> currencies;
    private LayoutInflater inflater;

    public CurrencySpinnerAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);

        currencies = objects;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return currencies.size();
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
        StringBuilder str = new StringBuilder();
        String currName = currencies.get(position).getName();
        String currAbbr = currencies.get(position).getAbbr();

        str.append(currName);
        str.append(" (");
        str.append(currAbbr);
        str.append(")");

        TextView tv = (TextView) view.findViewById(R.id.tvItemText);
        tv.setText(str.toString());

        return view;
    }
}
