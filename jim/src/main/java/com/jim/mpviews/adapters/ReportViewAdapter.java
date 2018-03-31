package com.jim.mpviews.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.ExpandableView;
import com.jim.mpviews.R;
import com.jim.mpviews.utils.ReportViewConstants;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Sirojiddin on 03.03.2018.
 */

public class ReportViewAdapter extends RecyclerView.Adapter<ReportViewAdapter.ViewHolder> {

    private Object[][] data;
    private int[] weight;
    private int[] dataTypes;
    private int[] alignTypes;
    private Object[][] statusTypes;
    private Context context;
    private DecimalFormat decimalFormat;
    private SimpleDateFormat simpleDateFormat;
    private OnItemClickListener listener;

    public ReportViewAdapter(Context context) {
        this.context = context;
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    }

    public void setData(Object[][] data, int[] weight, int[] dataTypes, int[] alignTypes, Object[][] statusTypes) {
        this.data = data;
        this.weight = weight;
        this.dataTypes = dataTypes;
        this.alignTypes = alignTypes;
        this.statusTypes = statusTypes;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public ReportViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ExpandableView itemView = new ExpandableView(context);
        itemView.setWeight(weight);
        itemView.setAlign(alignTypes);
        itemView.setSize(data[0].length);
        itemView.create();
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        if (holder.customView.getChildAt(0) instanceof LinearLayout) {
            LinearLayout row = (LinearLayout) holder.customView.getChildAt(0);
            if (position % 2 == 0)
                row.setBackgroundResource(R.color.colorWhite);
            else row.setBackgroundResource(R.color.colorReportItemBackground);
            int count = 0;
            for (int i = 0; i < row.getChildCount(); i++) {
                if (row.getChildAt(i) instanceof LinearLayout) {
                    LinearLayout col = (LinearLayout) row.getChildAt(i);
                    for (int j = 0; j < col.getChildCount(); j++) {
                        if (col.getChildAt(j) instanceof TextView) {
                            TextView textView = (TextView) col.getChildAt(j);
                            if (data[position][count] instanceof Long) {
                                Long item = (Long) data[position][count];
                                switch (dataTypes[count]) {
                                    case ReportViewConstants.DATE:
                                        textView.setText(simpleDateFormat.format(item));
                                        break;
                                    case ReportViewConstants.ID:
                                        textView.setText(String.valueOf(item));
                                        break;
                                    case ReportViewConstants.ACTION:
                                        String span = String.valueOf(item);
                                        SpannableString content = new SpannableString(span);
                                        content.setSpan(new UnderlineSpan(), 0, span.length(), 0);
                                        textView.setText(content);
                                        textView.setTextColor(ContextCompat.getColor(context, R.color.colorBlue));
                                        final int finalCount = count;
                                        col.setOnClickListener(view -> listener.onAction(position, finalCount));
                                        break;
                                }
                            } else if (data[position][count] instanceof String) {
                                String item = (String) data[position][count];
                                switch (dataTypes[count]) {
                                    case ReportViewConstants.NAME:
                                        textView.setText(item);
                                        break;
                                    case ReportViewConstants.ACTION:
                                        SpannableString content = new SpannableString(item);
                                        content.setSpan(new UnderlineSpan(), 0, item.length(), 0);
                                        textView.setText(content);
                                        textView.setTextColor(ContextCompat.getColor(context, R.color.colorBlue));
                                        final int finalCount = count;
                                        col.setOnClickListener(view -> listener.onAction(position, finalCount));
                                        break;
                                }
                            } else if (data[position][count] instanceof Double) {
                                Double item = (Double) data[position][count];
                                switch (dataTypes[count]) {
                                    case ReportViewConstants.AMOUNT:
                                        textView.setText(decimalFormat.format(item));
                                        break;
                                    case ReportViewConstants.QUANTITY:
                                        textView.setText(String.valueOf(item));
                                        break;
                                }
                            } else if (data[position][count] instanceof Integer) {
                                Integer item = (Integer) data[position][count];

                                switch (dataTypes[count]) {
                                    case ReportViewConstants.STATUS:
                                        if (statusTypes != null) {
                                            for (int k = 0; k < statusTypes.length; k++) {
                                                if (item == statusTypes[k][0]) {
                                                    String status = (String) statusTypes[k][1];
                                                    Integer color = (Integer) statusTypes[k][2];
                                                    textView.setText(status);
                                                    textView.setTextColor(ContextCompat.getColor(context, color));
                                                }
                                            }
                                        }
                                        break;
                                    default:
                                        textView.setText(String.valueOf(item));
                                        break;
                                }
                            }
                            count++;
                        }
                    }
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

    public interface OnItemClickListener {
        void onAction(int rowPosition, int colPosition);
    }
}
