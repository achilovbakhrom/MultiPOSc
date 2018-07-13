package com.jim.multipos.ui.product_queue_list.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
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
import com.jim.multipos.ui.product_queue_list.product_queue.ProductQueueListFragmentPresenterImpl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductQueueListAdapter extends RecyclerView.Adapter<ProductQueueListAdapter.ProductQueueViewHolder> {

    private final SimpleDateFormat simpleDateFormat;
    private final DecimalFormat decimalFormat;
    private List<StockQueue> items;
    private OnStockOperationsListener listener;
    private boolean searchMode = false;
    private String searchText;
    private int mode;

    public ProductQueueListAdapter() {
        items = new ArrayList<>();
        decimalFormat = BaseAppModule.getFormatterGrouping();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_queue_item, parent, false);
        return new ProductQueueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductQueueViewHolder holder, int position) {
        if (position % 2 == 0) holder.llBackground.setBackgroundColor(Color.parseColor("#f9f9f9"));
        else holder.llBackground.setBackgroundColor(Color.parseColor("#f0f0f0"));
        if (!searchMode) {
            if (items.get(position).getIncomeProduct().getInvoice() != null)
                holder.tvInvoiceId.setText(String.valueOf(items.get(position).getIncomeProduct().getInvoiceId()));
            else holder.tvInvoiceId.setText("");
            holder.tvDateInput.setText(simpleDateFormat.format(items.get(position).getIncomeProductDate()));
            holder.tvCost.setText(decimalFormat.format(items.get(position).getCost()));
            if (mode == ProductQueueListFragmentPresenterImpl.VENDOR) {
                holder.tvVendor.setText(items.get(position).getProduct().getName());
            } else {
                if (items.get(position).getVendor() != null)
                    holder.tvVendor.setText(items.get(position).getVendor().getName());
                else holder.tvVendor.setText("");
            }

            if (items.get(position).getStockId() != null)
                holder.tvStockId.setText(items.get(position).getStockId());
            holder.ssvStockStatus.setMax(items.get(position).getIncomeCount());
            holder.ssvStockStatus.setSold(items.get(position).getIncomeCount() - items.get(position).getAvailable());
            holder.tvAvailable.setText(items.get(position).getAvailable() + "/" + items.get(position).getIncomeCount());
        } else {
            if (items.get(position).getIncomeProduct().getInvoice() != null)
                colorSubSeq(String.valueOf(items.get(position).getIncomeProduct().getInvoiceId()), searchText, Color.parseColor("#95ccee"), holder.tvInvoiceId);
            colorSubSeq(simpleDateFormat.format(items.get(position).getIncomeProductDate()), searchText, Color.parseColor("#95ccee"), holder.tvDateInput);
            colorSubSeq(decimalFormat.format(items.get(position).getCost()), searchText, Color.parseColor("#95ccee"), holder.tvCost);
            if (items.get(position).getStockId() != null)
                colorSubSeq(items.get(position).getStockId(), searchText, Color.parseColor("#95ccee"), holder.tvStockId);
            if (mode == ProductQueueListFragmentPresenterImpl.VENDOR) {
                colorSubSeq(items.get(position).getProduct().getName(), searchText, Color.parseColor("#95ccee"), holder.tvVendor);
            } else {
                if (items.get(position).getVendor() != null)
                    colorSubSeq(items.get(position).getVendor().getName(), searchText, Color.parseColor("#95ccee"), holder.tvVendor);
                else holder.tvVendor.setText("");
            }
            colorSubSeq(items.get(position).getAvailable() + "/" + items.get(position).getIncomeCount(), searchText, Color.parseColor("#95ccee"), holder.tvAvailable);
            holder.ssvStockStatus.setMax(items.get(position).getIncomeCount());
            holder.ssvStockStatus.setSold(items.get(position).getIncomeCount() - items.get(position).getAvailable());
            holder.tvAvailable.setText(items.get(position).getAvailable() + "/" + items.get(position).getIncomeCount());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setListener(OnStockOperationsListener listener) {
        this.listener = listener;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    class ProductQueueViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ssvStockStatus)
        StockStatusView ssvStockStatus;
        @BindView(R.id.tvInvoiceId)
        TextView tvInvoiceId;
        @BindView(R.id.tvDateInput)
        TextView tvDateInput;
        @BindView(R.id.tvCost)
        TextView tvCost;
        @BindView(R.id.tvAvailable)
        TextView tvAvailable;
        @BindView(R.id.tvVendor)
        TextView tvVendor;
        @BindView(R.id.tvStockId)
        TextView tvStockId;
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
