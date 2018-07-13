package com.jim.multipos.ui.consignment.adapter;

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
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.mpviews.StockStatusView;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.data.db.model.intosystem.StockQueueItem;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.utils.DecimalUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductStockQueuesAdapter extends RecyclerView.Adapter<ProductStockQueuesAdapter.StockQueueItemViewHolder> {

    private List<StockQueueItem> items;
    private SimpleDateFormat simpleDateFormat;
    private DecimalFormat decimalFormat;
    private OnCustomSelectListener listener;
    private String searchText;
    private boolean searchMode = false;

    public ProductStockQueuesAdapter() {
        decimalFormat = BaseAppModule.getFormatterGrouping();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    }

    @NonNull
    @Override
    public StockQueueItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_positions_item, parent, false);
        return new StockQueueItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockQueueItemViewHolder holder, int position) {
        holder.ssvStockStatus.setMax(items.get(position).getStockQueue().getIncomeCount());
        holder.ssvStockStatus.setSold(items.get(position).getStockQueue().getIncomeCount() - items.get(position).getAvailable());
        double count = 0;
        for (int i = 0; i < items.get(position).getDetialCounts().size(); i++) {
            count += items.get(position).getDetialCounts().get(i).getCount();
        }
        holder.tvAvailable.setText(decimalFormat.format(DecimalUtils.roundDouble((items.get(position).getAvailable() - count))) + "/" + decimalFormat.format(DecimalUtils.roundDouble(items.get(position).getStockQueue().getIncomeCount())));
        holder.ssvStockStatus.setCount(count);
        if (count != 0) {
            holder.ivSelected.setVisibility(View.VISIBLE);
        } else holder.ivSelected.setVisibility(View.INVISIBLE);

        if (!searchMode) {
            if (items.get(position).getStockQueue().getIncomeProduct().getInvoice() != null)
                holder.tvInvoiceId.setText(String.valueOf(items.get(position).getStockQueue().getIncomeProduct().getInvoiceId()));
            else holder.tvInvoiceId.setText("");
            holder.tvDateInput.setText(simpleDateFormat.format(items.get(position).getStockQueue().getIncomeProductDate()));
            holder.tvCost.setText(decimalFormat.format(items.get(position).getStockQueue().getCost()));
            if (items.get(position).getStockQueue().getVendor() != null)
                holder.tvVendor.setText(items.get(position).getStockQueue().getVendor().getName());
            else holder.tvVendor.setText("");
            if (items.get(position).getStockQueue().getExpiredProductDate() != 0)
                holder.tvExpireDate.setText(simpleDateFormat.format(items.get(position).getStockQueue().getExpiredProductDate()));
            if (items.get(position).getStockQueue().getStockId() == null)
                holder.tvStockId.setText(items.get(position).getStockQueue().getStockId());
        } else {
            if (items.get(position).getStockQueue().getIncomeProduct().getInvoice() != null)
                colorSubSeq(String.valueOf(items.get(position).getStockQueue().getIncomeProduct().getInvoiceId()), searchText, Color.parseColor("#95ccee"), holder.tvInvoiceId);
            colorSubSeq(simpleDateFormat.format(items.get(position).getStockQueue().getIncomeProductDate()), searchText, Color.parseColor("#95ccee"), holder.tvDateInput);
            if (items.get(position).getStockQueue().getExpiredProductDate() != 0)
                colorSubSeq(simpleDateFormat.format(items.get(position).getStockQueue().getExpiredProductDate()), searchText, Color.parseColor("#95ccee"), holder.tvExpireDate);
            colorSubSeq(decimalFormat.format(items.get(position).getStockQueue().getCost()), searchText, Color.parseColor("#95ccee"), holder.tvCost);
            if (items.get(position).getStockQueue().getVendor() != null)
                colorSubSeq(items.get(position).getStockQueue().getVendor().getName(), searchText, Color.parseColor("#95ccee"), holder.tvVendor);
            if (items.get(position).getStockQueue().getStockId() != null)
                colorSubSeq(items.get(position).getStockQueue().getStockId(), searchText, Color.parseColor("#95ccee"), holder.tvStockId);
        }
    }


    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
        else
            return items.size();
    }

    public void setData(List<StockQueueItem> items) {
        this.items = items;
        searchMode = false;
        notifyDataSetChanged();
    }

    public void setListener(OnCustomSelectListener listener) {
        this.listener = listener;
    }

    public void setSearchResult(List<StockQueueItem> searchResults, String searchText) {
        this.items = searchResults;
        this.searchText = searchText;
        searchMode = true;
        notifyDataSetChanged();
    }

    public interface OnCustomSelectListener {
        void onSelect(StockQueueItem stockQueueItem, int position);
    }

    class StockQueueItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivSelected)
        ImageView ivSelected;
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
        @BindView(R.id.btnSelect)
        MpButton btnSelect;
        @BindView(R.id.tvExpireDate)
        TextView tvExpireDate;

        public StockQueueItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSelect(items.get(getAdapterPosition()), getAdapterPosition());
                }
            });
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
}
