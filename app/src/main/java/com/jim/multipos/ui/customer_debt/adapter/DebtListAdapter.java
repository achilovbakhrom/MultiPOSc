package com.jim.multipos.ui.customer_debt.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.Debt;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Setter;

/**
 * Created by Sirojiddin on 29.12.2017.
 */

public class DebtListAdapter extends RecyclerView.Adapter<DebtListAdapter.DebtListViewHolder> {
    @Setter
    private OnDebtClickListener listener;
    private List<Debt> items;
    private Currency currency;
    private Context context;
    private DecimalFormat decimalFormat;
    protected int selectedPosition = -1;
    private boolean isFirstTime = false;

    public DebtListAdapter(Context context, DecimalFormat decimalFormat) {
        this.context = context;
        this.decimalFormat = decimalFormat;
        items = new ArrayList<>();
    }

    public void setItems(List<Debt> items, Currency currency) {
        this.items = items;
        this.currency = currency;
        notifyDataSetChanged();
        isFirstTime = true;
    }

    @Override
    public DebtListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.debt_list_item, parent, false);
        return new DebtListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DebtListViewHolder holder, int position) {
//        holder.tvOrderNumber.setText(String.valueOf(items.get(position).getOrder().getId()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        holder.tvTakenDate.setText(simpleDateFormat.format(items.get(position).getTakenDate()));
        holder.tvEndDate.setText(simpleDateFormat.format(items.get(position).getEndDate()));
        holder.tvTotalDebt.setText(decimalFormat.format(items.get(position).getDebtAmount()));
        if (isFirstTime) {
            if (position == 0) {
                selectedPosition = position;
                holder.llBackground.setBackground(ContextCompat.getDrawable(context, R.drawable.yellow_rect));
            }
        }
        if (items.get(position).getCustomerPayments().size() > 0) {
            int dueSum = 0;
            for (int i = 0; i < items.get(position).getCustomerPayments().size(); i++) {
                dueSum += items.get(position).getCustomerPayments().get(i).getDebtDue();
            }
            holder.tvDueSum.setText(dueSum + "");
            if (dueSum != items.get(position).getDebtAmount())
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreenVeryLight));
            else
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrangeLight));
        } else {
            holder.tvDueSum.setText(decimalFormat.format(0));
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrangeLight));
        }
        if (selectedPosition == position) {
            holder.llBackground.setBackground(ContextCompat.getDrawable(context, R.drawable.yellow_rect));
        } else holder.llBackground.setBackground(null);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnDebtClickListener {
        void onItemClicked(Debt item, int position);
    }

    class DebtListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvOrderNumber)
        TextView tvOrderNumber;
        @BindView(R.id.tvTakenDate)
        TextView tvTakenDate;
        @BindView(R.id.tvEndDate)
        TextView tvEndDate;
        @BindView(R.id.tvTotalDebt)
        TextView tvTotalDebt;
        @BindView(R.id.tvDueSum)
        TextView tvDueSum;
        @BindView(R.id.ivExtraOptions)
        ImageView ivExtraOptions;
        @BindView(R.id.llBackground)
        LinearLayout llBackground;

        public DebtListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> {
                if (selectedPosition != getAdapterPosition()) {
                    isFirstTime = false;
                    if (listener != null) {
                        notifyItemChanged(selectedPosition);
                        notifyItemChanged(getAdapterPosition());
                        listener.onItemClicked(items.get(getAdapterPosition()), getAdapterPosition());
                        selectedPosition = getAdapterPosition();
                    }
                }
            });
        }
    }
}
