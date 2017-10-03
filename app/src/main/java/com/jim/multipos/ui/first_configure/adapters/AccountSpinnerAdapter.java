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
import com.jim.multipos.data.db.model.Account;

import java.util.List;

/**
 * Created by user on 21.08.17.
 */

public class AccountSpinnerAdapter extends ArrayAdapter {
    private List<Account> accounts;
    private LayoutInflater inflater;

    public AccountSpinnerAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);

        accounts = objects;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return accounts.size();
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
        tv.setText(accounts.get(position).getName());

        return view;
    }
}
