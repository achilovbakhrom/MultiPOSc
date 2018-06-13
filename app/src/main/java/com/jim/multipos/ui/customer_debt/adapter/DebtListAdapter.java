package com.jim.multipos.ui.customer_debt.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 29.12.2017.
 */

public class DebtListAdapter extends RecyclerView.Adapter<DebtListAdapter.DebtListViewHolder> {
    private OnDebtClickListener listener;
    private List<Debt> items;
    private Currency currency;
    private Context context;
    private DecimalFormat decimalFormat;
    protected int selectedPosition = -1;
    private boolean isPayToAllMode = false;

    public DebtListAdapter(Context context, DecimalFormat decimalFormat) {
        this.context = context;
        this.decimalFormat = decimalFormat;
        items = new ArrayList<>();
    }

    public void setItems(List<Debt> items, Currency currency) {
        this.items = items;
        this.currency = currency;
        notifyDataSetChanged();
    }

    @Override
    public DebtListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.debt_list_item, parent, false);
        return new DebtListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DebtListViewHolder holder, int position) {
        holder.tvOrderNumber.setText(String.valueOf(items.get(position).getOrder().getId()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        holder.tvTakenDate.setText(simpleDateFormat.format(items.get(position).getTakenDate()));
        holder.tvEndDate.setText(simpleDateFormat.format(items.get(position).getEndDate()));
        double feeAmount = items.get(position).getFee() * items.get(position).getDebtAmount() / 100;
        double total = items.get(position).getDebtAmount() + feeAmount;
        holder.tvTotalDebt.setText(decimalFormat.format(total) + " " + currency.getAbbr());
        GregorianCalendar now = new GregorianCalendar();
        Date date = new Date(items.get(position).getEndDate());
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        if (now.after(calendar) || now.equals(calendar)) {
            holder.tvEndDate.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
        } else
            holder.tvEndDate.setTextColor(ContextCompat.getColor(context, R.color.colorMainText));
        double dueSum = items.get(position).getDebtAmount() + feeAmount;
        if (items.get(position).getCustomerPayments().size() > 0) {
            for (int i = 0; i < items.get(position).getCustomerPayments().size(); i++) {
                dueSum -= items.get(position).getCustomerPayments().get(i).getPaymentAmount();
            }
            holder.tvDueSum.setText(decimalFormat.format(dueSum) + " " + currency.getAbbr());
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrangeLight));
        } else {
            holder.tvDueSum.setText(decimalFormat.format(dueSum) + " " + currency.getAbbr());
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

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        this.isPayToAllMode = false;
    }

    public void setListener(OnDebtClickListener listener) {
        this.listener = listener;
    }

    public interface OnDebtClickListener {
        void onItemClicked(Debt item, int position);
        void onCloseDebt(Debt item);
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
        @BindView(R.id.llExtraOptions)
        LinearLayout llExtraOptions;
        @BindView(R.id.ivExtraOptions)
        ImageView ivExtraOptions;
        @BindView(R.id.llBackground)
        LinearLayout llBackground;

        public DebtListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> {
                if (selectedPosition != getAdapterPosition()) {
                    if (listener != null) {
                        notifyItemChanged(selectedPosition);
                        notifyItemChanged(getAdapterPosition());
                        listener.onItemClicked(items.get(getAdapterPosition()), getAdapterPosition());
                        selectedPosition = getAdapterPosition();
                        isPayToAllMode = true;
                    }
                } else {
                    if (!isPayToAllMode){
                        llBackground.setBackground(ContextCompat.getDrawable(context, R.drawable.yellow_rect));
                        isPayToAllMode = true;
                    } else {
                        llBackground.setBackground(null);
                        isPayToAllMode = false;
                    }
                    listener.onItemClicked(items.get(getAdapterPosition()), getAdapterPosition());
                }
            });

            llExtraOptions.setOnClickListener(view -> {
                PopupMenu popupMenu = new PopupMenu(context, ivExtraOptions);
                popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.setGravity(Gravity.END);
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.closeDebt:
                            listener.onCloseDebt(items.get(getAdapterPosition()));
                            return true;
                    }
                    return true;
                });
                popupMenu.show();
            });
        }
    }
}
