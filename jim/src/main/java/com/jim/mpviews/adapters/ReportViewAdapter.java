package com.jim.mpviews.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
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
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sirojiddin on 03.03.2018.
 */

public class ReportViewAdapter extends RecyclerView.Adapter<ReportViewAdapter.ViewHolder> {

    private Object[][] data;
    private int[] weight;
    private int[] dataTypes;
    private int[] alignTypes;
    private Object[][][] statusTypes;
    private Context context;
    private DecimalFormat decimalFormat, decimalFormatWithoutGrouping;
    private SimpleDateFormat simpleDateFormat;
    private OnItemClickListener listener;
    private boolean searchMode = false;
    private String searchText;

    public ReportViewAdapter(Context context) {
        this.context = context;
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);
        decimalFormatWithoutGrouping = new DecimalFormat("0.00");
        DecimalFormatSymbols decimalFormatSymbols = decimalFormatWithoutGrouping.getDecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormatWithoutGrouping.setDecimalFormatSymbols(decimalFormatSymbols);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    }

    public void setData(Object[][] data, int[] weight, int[] dataTypes, int[] alignTypes, Object[][][] statusTypes) {
        this.data = data;
        this.weight = weight;
        this.dataTypes = dataTypes;
        this.alignTypes = alignTypes;
        this.statusTypes = statusTypes;
        searchMode = false;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
        searchMode = true;
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
        if (!searchMode) {
            int statusCount = 0;
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
                                            textView.setText(simpleDateFormat.format(new Date(item)));
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
                                                for (int k = 0; k < statusTypes[statusCount].length; k++) {
                                                    if (item == statusTypes[statusCount][k][0]) {
                                                        String status = (String) statusTypes[statusCount][k][1];
                                                        Integer color = (Integer) statusTypes[statusCount][k][2];
                                                        textView.setText(status);
                                                        textView.setTextColor(ContextCompat.getColor(context, color));
                                                    }
                                                }
                                                statusCount++;
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
        } else {
            int statusCount = 0;
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
                                            colorSubSeq(simpleDateFormat.format(new Date(item)), searchText, Color.parseColor("#95ccee"), textView);
                                            break;
                                        case ReportViewConstants.ID:
                                            colorSubSeq(String.valueOf(item), searchText, Color.parseColor("#95ccee"), textView);
                                            break;
                                        case ReportViewConstants.ACTION:
                                            textView.setTextColor(ContextCompat.getColor(context, R.color.colorBlue));
                                            colorSubSeqUnderLine(String.valueOf(item), searchText, Color.parseColor("#95ccee"), textView);
                                            final int finalCount = count;
                                            col.setOnClickListener(view -> listener.onAction(position, finalCount));
                                            break;
                                    }
                                } else if (data[position][count] instanceof String) {
                                    String item = (String) data[position][count];
                                    switch (dataTypes[count]) {
                                        case ReportViewConstants.NAME:
                                            colorSubSeq(item, searchText, Color.parseColor("#95ccee"), textView);
                                            break;
                                        case ReportViewConstants.ACTION:
                                            textView.setTextColor(ContextCompat.getColor(context, R.color.colorBlue));
                                            colorSubSeqUnderLine(String.valueOf(item), searchText, Color.parseColor("#95ccee"), textView);
                                            final int finalCount = count;
                                            col.setOnClickListener(view -> listener.onAction(position, finalCount));
                                            break;
                                    }
                                } else if (data[position][count] instanceof Double) {
                                    Double item = (Double) data[position][count];
                                    switch (dataTypes[count]) {
                                        case ReportViewConstants.AMOUNT:
                                            colorSubSeq(decimalFormatWithoutGrouping.format(item), searchText, Color.parseColor("#95ccee"), textView);
                                            break;
                                        case ReportViewConstants.QUANTITY:
                                            colorSubSeq(String.valueOf(item), searchText, Color.parseColor("#95ccee"), textView);
                                            break;
                                    }
                                } else if (data[position][count] instanceof Integer) {
                                    Integer item = (Integer) data[position][count];

                                    switch (dataTypes[count]) {
                                        case ReportViewConstants.STATUS:
                                            if (statusTypes != null) {
                                                for (int k = 0; k < statusTypes[statusCount].length; k++) {
                                                    if (item == statusTypes[statusCount][k][0]) {
                                                        String status = (String) statusTypes[statusCount][k][1];
                                                        Integer color = (Integer) statusTypes[statusCount][k][2];
                                                        textView.setTextColor(ContextCompat.getColor(context, color));
                                                        colorSubSeq(status, searchText, Color.parseColor("#95ccee"), textView);
                                                    }
                                                }
                                                statusCount++;
                                            }
                                            break;
                                        default:
                                            colorSubSeq(String.valueOf(item), searchText, Color.parseColor("#95ccee"), textView);
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

    private void colorSubSeq(String text, String whichWordColor, int colorCode, TextView textView) {
        String textUpper = text.toUpperCase();
        String whichWordColorUpper = whichWordColor.toUpperCase();
        SpannableString ss = new SpannableString(text);
        int strar = 0;

        while (textUpper.indexOf(whichWordColorUpper, strar) >= 0 && whichWordColor.length() != 0) {
            ss.setSpan(new BackgroundColorSpan(colorCode), textUpper.indexOf(whichWordColorUpper, strar), textUpper.indexOf(whichWordColorUpper, strar) + whichWordColorUpper.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            strar = textUpper.indexOf(whichWordColorUpper, strar) + whichWordColorUpper.length();
        }
        textView.setText(ss);
    }

    public void colorSubSeqUnderLine(String text, String whichWordColor, int colorCode, TextView textView) {
        String textUpper = text.toUpperCase();
        String whichWordColorUpper = whichWordColor.toUpperCase();
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new UnderlineSpan(), 0, ss.length(), 0);
        int strar = 0;

        while (textUpper.indexOf(whichWordColorUpper, strar) >= 0 && whichWordColor.length() != 0) {
            ss.setSpan(new BackgroundColorSpan(colorCode), textUpper.indexOf(whichWordColorUpper, strar), textUpper.indexOf(whichWordColorUpper, strar) + whichWordColorUpper.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            strar = textUpper.indexOf(whichWordColorUpper, strar) + whichWordColorUpper.length();
        }
        textView.setText(ss);
    }
}
