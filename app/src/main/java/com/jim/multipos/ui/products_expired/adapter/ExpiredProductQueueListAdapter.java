package com.jim.multipos.ui.products_expired.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.StockStatusView;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.utils.DecimalUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpiredProductQueueListAdapter extends RecyclerView.Adapter<ExpiredProductQueueListAdapter.ProductQueueViewHolder> {

    private final SimpleDateFormat simpleDateFormat;
    private List<StockQueue> items;
    private DecimalFormat decimalFormat;
    private OnStockOperationsListener listener;
    private boolean searchMode = false;
    private String searchText;
    private Context context;

    public ExpiredProductQueueListAdapter(Context context) {
        this.context = context;
        items = new ArrayList<>();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        decimalFormat = BaseAppModule.getFormatterGrouping();
    }

    public void setItems(List<StockQueue> items) {
        this.items = items;
        searchMode = false;
        notifyDataSetChanged();
    }

    public void setSearchResults(List<StockQueue> items, String searchText) {
        this.items = items;
        this.searchText = searchText;
        searchMode = true;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductQueueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expired_product_queue_item, parent, false);
        return new ProductQueueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductQueueViewHolder holder, int position) {
        if (position % 2 == 0) holder.llBackground.setBackgroundColor(Color.parseColor("#f9f9f9"));
        else holder.llBackground.setBackgroundColor(Color.parseColor("#f0f0f0"));
        GregorianCalendar calendar = new GregorianCalendar();
        GregorianCalendar now = new GregorianCalendar();
        calendar.setTime(new Date(items.get(position).getExpiredProductDate()));
        if ((now.after(calendar) || now.equals(calendar))) {
            holder.ssvStockStatus.setExpired(true);
            holder.tvExpireDate.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
        } else if (now.before(calendar)) {
            long date = (calendar.getTimeInMillis() - now.getTimeInMillis()) / 1000 / 60 / 60 / 24;
            if (date <= 7){
                holder.ssvStockStatus.setCloseToExpire(true);
                holder.tvExpireDate.setTextColor(ContextCompat.getColor(context, R.color.colorDirtyOrange));
            }
        }
        if (!searchMode) {
            if (items.get(position).getIncomeProduct().getInvoice() != null)
                holder.tvInvoiceId.setText(String.valueOf(items.get(position).getIncomeProduct().getInvoiceId()));
            else holder.tvInvoiceId.setText("");
            holder.tvDateInput.setText(simpleDateFormat.format(items.get(position).getIncomeProductDate()));
            holder.tvProductName.setText((items.get(position).getProduct().getName()));
            if (items.get(position).getVendor() != null)
                holder.tvVendor.setText(items.get(position).getVendor().getName());
            else holder.tvVendor.setText("");
            holder.tvExpireDate.setText(simpleDateFormat.format(items.get(position).getExpiredProductDate()));
            holder.ssvStockStatus.setMax(items.get(position).getIncomeCount());
            holder.ssvStockStatus.setSold(items.get(position).getIncomeCount() - items.get(position).getAvailable());
            holder.tvAvailable.setText(decimalFormat.format(DecimalUtils.roundDouble(items.get(position).getAvailable())) + "/" + decimalFormat.format(DecimalUtils.roundDouble(items.get(position).getIncomeCount())));
        } else {
            if (items.get(position).getIncomeProduct().getInvoice() != null)
                colorSubSeq(String.valueOf(items.get(position).getIncomeProduct().getInvoiceId()), searchText, Color.parseColor("#95ccee"), holder.tvInvoiceId);
            colorSubSeq(simpleDateFormat.format(items.get(position).getIncomeProductDate()), searchText, Color.parseColor("#95ccee"), holder.tvDateInput);
            colorSubSeq(items.get(position).getProduct().getName(), searchText, Color.parseColor("#95ccee"), holder.tvProductName);
            colorSubSeq(simpleDateFormat.format(items.get(position).getExpiredProductDate()), searchText, Color.parseColor("#95ccee"), holder.tvExpireDate);
            if (items.get(position).getVendor() != null)
                colorSubSeq(items.get(position).getVendor().getName(), searchText, Color.parseColor("#95ccee"), holder.tvVendor);
            colorSubSeq(items.get(position).getAvailable() + "/" + items.get(position).getIncomeCount(), searchText, Color.parseColor("#95ccee"), holder.tvAvailable);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setListener(OnStockOperationsListener listener) {
        this.listener = listener;
    }

    class ProductQueueViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ssvStockStatus)
        StockStatusView ssvStockStatus;
        @BindView(R.id.tvInvoiceId)
        TextView tvInvoiceId;
        @BindView(R.id.tvDateInput)
        TextView tvDateInput;
        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.tvAvailable)
        TextView tvAvailable;
        @BindView(R.id.tvVendor)
        TextView tvVendor;
        @BindView(R.id.tvExpireDate)
        TextView tvExpireDate;
        @BindView(R.id.ivBackReturn)
        ImageView ivBackReturn;
        @BindView(R.id.ivInfo)
        ImageView ivInfo;
        @BindView(R.id.ivWriteOff)
        ImageView ivWriteOff;
        @BindView(R.id.llBackground)
        LinearLayout llBackground;

        public ProductQueueViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivBackReturn.setOnClickListener(v -> listener.onOutvoice(items.get(getAdapterPosition()), getAdapterPosition()));
            ivInfo.setOnClickListener(v -> listener.onInfo(items.get(getAdapterPosition())));
            ivWriteOff.setOnClickListener(v -> listener.onWriteOff(items.get(getAdapterPosition()), getAdapterPosition()));
        }
    }


    public void colorSubSeq(String text, String whichWordColor, int colorCode, TextView textView) {
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

    public interface OnStockOperationsListener {
        void onInfo(StockQueue stockQueue);

        void onWriteOff(StockQueue stockQueue, int position);

        void onOutvoice(StockQueue stockQueue, int position);
    }
}
