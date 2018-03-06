package com.jim.mpviews.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.mpviews.ExpandableView;
import com.jim.mpviews.utils.ReportViewConstants;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sirojiddin on 03.03.2018.
 */

public class ReportViewAdapter extends RecyclerView.Adapter<ReportViewAdapter.ViewHolder> {


    private Object[][] data;
    private int[] weight;
    private int[] dataTypes;
    private Context context;
    private DecimalFormat decimalFormat;
    private SimpleDateFormat simpleDateFormat;

    public ReportViewAdapter(Context context) {
        this.context = context;
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        numberFormat.setMaximumFractionDigits(2);
        decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    }

    public void setData(Object[][] data, int[] weight, int[] dataTypes) {
        this.data = data;
        this.weight = weight;
        this.dataTypes = dataTypes;
        notifyDataSetChanged();
    }

    @Override
    public ReportViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ExpandableView itemView = new ExpandableView(context);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.customView.setWeight(weight);
        holder.customView.setSize(data[position].length);
        holder.customView.create();

        for (int i = 0; i < data[position].length; i++) {
            if (holder.customView.getChildAt(i) instanceof TextView) {
                TextView textView = (TextView) holder.customView.getChildAt(i);

                if (data[position][i] instanceof Long) {
                    Long item = (Long) data[position][i];
                    switch (dataTypes[i]) {
                        case ReportViewConstants.DATE:
                            textView.setText(simpleDateFormat.format(item));
                            break;
                        case ReportViewConstants.ID:
                            textView.setText(String.valueOf(item));
                            break;
                    }
                } else if (data[position][i] instanceof String) {
                    String item = (String) data[position][i];
                    switch (dataTypes[i]) {
                        case ReportViewConstants.NAME:
                            textView.setText(item);
                            break;
                    }
                } else if (data[position][i] instanceof Double) {
                    Double item = (Double) data[position][i];
                    switch (dataTypes[i]) {
                        case ReportViewConstants.AMOUNT:
                            textView.setText(decimalFormat.format(item));
                            break;
                        case ReportViewConstants.QUANTITY:
                            textView.setText(String.valueOf(item));
                            break;
                    }
                } else if (data[position][i] instanceof Integer) {
                    Integer item = (Integer) data[position][i];
                    textView.setText(String.valueOf(item));
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ExpandableView customView;

        public ViewHolder(View v) {
            super(v);
            customView = (ExpandableView) v;
        }
    }

}
