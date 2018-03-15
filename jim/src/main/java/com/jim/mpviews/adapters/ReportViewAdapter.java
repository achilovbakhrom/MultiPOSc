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

    private static final int TITLE_ITEM = 0;
    private static final int REPORT_ITEM = 1;
    private Object[][] data;
    private int[] weight;
    private int[] dataTypes;
    private String[] titles;
    private int[] alignTypes;
    private Object[][] statusTypes;
    private Context context;
    private DecimalFormat decimalFormat;
    private SimpleDateFormat simpleDateFormat;
    private OnItemClickListener listener;
    private int sorting = -1;

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

    public void setData(Object[][] data, int[] weight, int[] dataTypes, String[] titles, int[] alignTypes, Object[][] statusTypes) {
        this.data = data;
        this.weight = weight;
        this.dataTypes = dataTypes;
        this.titles = titles;
        this.alignTypes = alignTypes;
        this.statusTypes = statusTypes;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TITLE_ITEM : REPORT_ITEM;
    }

    @Override
    public ReportViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ExpandableView itemView = new ExpandableView(context);
        itemView.setWeight(weight);
        itemView.setAlign(alignTypes);
        if (viewType == TITLE_ITEM) {
            itemView.setHasTitle(true);
            itemView.setSize(titles.length);
            itemView.create();
            return new ViewHolder(itemView);
        } else {
            itemView.create();
            return new ViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (position == 0) {
            if (holder.customView.getChildAt(0) instanceof LinearLayout) {
                LinearLayout row = (LinearLayout) holder.customView.getChildAt(0);
                int count = 0;
                for (int i = 0; i < row.getChildCount(); i++) {
                    if (row.getChildAt(i) instanceof LinearLayout) {
                        LinearLayout col = (LinearLayout) row.getChildAt(i);
                        col.setBackgroundResource(R.color.colorReportTitleBackGround);
                        final int finalCount = count;
                        col.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                listener.onSort(finalCount);
                                sorting *= -1;
                                holder.customView.sorted(sorting, finalCount);
                            }
                        });
                        for (int j = 0; j < col.getChildCount(); j++) {
                            if (col.getChildAt(j) instanceof TextView) {
                                TextView textView = (TextView) col.getChildAt(j);
                                textView.setText(titles[count]);
                                count++;
                            }
                        }
                    }
                }

            }
        } else {
            holder.customView.removeAllViews();
            holder.customView.setSize(data[position - 1].length);
            holder.customView.create();

            if (holder.customView.getChildAt(0) instanceof LinearLayout) {
                LinearLayout row = (LinearLayout) holder.customView.getChildAt(0);
                if (position % 2 == 0)
                    row.setBackgroundResource(R.color.colorReportItemBackground);
                else row.setBackgroundResource(R.color.colorWhite);
                int count = 0;
                for (int i = 0; i < row.getChildCount(); i++) {
                    if (row.getChildAt(i) instanceof LinearLayout) {
                        LinearLayout col = (LinearLayout) row.getChildAt(i);
                        for (int j = 0; j < col.getChildCount(); j++) {
                            if (col.getChildAt(j) instanceof TextView) {
                                TextView textView = (TextView) col.getChildAt(j);
                                if (data[position - 1][count] instanceof Long) {
                                    Long item = (Long) data[position - 1][count];
                                    switch (dataTypes[count]) {
                                        case ReportViewConstants.DATE:
                                            textView.setText(simpleDateFormat.format(item));
                                            break;
                                        case ReportViewConstants.ID:
                                            textView.setText(String.valueOf(item));
                                            break;
                                    }
                                } else if (data[position - 1][count] instanceof String) {
                                    String item = (String) data[position - 1][count];
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
                                } else if (data[position - 1][count] instanceof Double) {
                                    Double item = (Double) data[position - 1][count];
                                    switch (dataTypes[count]) {
                                        case ReportViewConstants.AMOUNT:
                                            textView.setText(decimalFormat.format(item));
                                            break;
                                        case ReportViewConstants.QUANTITY:
                                            textView.setText(String.valueOf(item));
                                            break;
                                    }
                                } else if (data[position - 1][count] instanceof Integer) {
                                    Integer item = (Integer) data[position - 1][count];

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
    }

    @Override
    public int getItemCount() {
        return data.length + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ExpandableView customView;

        public ViewHolder(View v) {
            super(v);
            customView = (ExpandableView) v;
        }
    }

    public interface OnItemClickListener {
        void onSort(int position);
        void onAction(int rowPosition, int colPosition);
    }

}
