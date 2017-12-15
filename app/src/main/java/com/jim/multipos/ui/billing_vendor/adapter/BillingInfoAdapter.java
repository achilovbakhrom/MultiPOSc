package com.jim.multipos.ui.billing_vendor.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.inventory.BillingOperations;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by developer on 30.11.2017.
 */

public class BillingInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private Currency currency;
    private SimpleDateFormat simpleDateFormat;
    private List<BillingOperations> items;

    public void setData(List<BillingOperations> items) {
        this.items = items;
    }

    public BillingInfoAdapter(Context context, Currency currency) {
        this.context = context;
        this.currency = currency;
        items = new ArrayList<>();
        simpleDateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.billing_info_item, parent, false);
            return new BillingInfoViewHolder(view);
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.billing_info_edited_item, parent, false);
            return new BillingInfoEditedViewHolder(view);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BillingInfoViewHolder) {
            BillingInfoViewHolder infoViewHolder = (BillingInfoViewHolder) holder;
            Date createdDate = new Date(items.get(position).getCreateAt());
            Date paymentDate = new Date(items.get(position).getPaymentDate());
            infoViewHolder.tvDate.setText(simpleDateFormat.format(createdDate));
            infoViewHolder.tvPaymentDate.setText(simpleDateFormat.format(paymentDate));
            if (items.get(position).getAccount() != null)
                infoViewHolder.tvAccount.setText(items.get(position).getAccount().getName());
            else {
                infoViewHolder.tvAccount.setText("None");
            }
            infoViewHolder.tvAmount.setText(String.valueOf(items.get(position).getAmount()) + " " + currency.getAbbr());
            if (items.size() > 1) {
                infoViewHolder.llBackground.setVisibility(View.VISIBLE);
                if (!items.get(position).getAmount().equals(items.get(position + 1).getAmount())) {
                    infoViewHolder.tvAmount.setTextColor(context.getColor(R.color.colorRed));
                }
                if (items.get(position).getAccount() != null) {
                    if (items.get(position + 1).getAccount() == null) {
                        infoViewHolder.tvAccount.setTextColor(context.getColor(R.color.colorRed));
                    } else if (!items.get(position).getAccount().getId().equals(items.get(position + 1).getAccount().getId())) {
                        infoViewHolder.tvAccount.setTextColor(context.getColor(R.color.colorRed));
                    }
                } else {
                    if (items.get(position + 1).getAccount() != null)
                        infoViewHolder.tvAccount.setTextColor(context.getColor(R.color.colorRed));
                }
            }
        } else if (holder instanceof BillingInfoEditedViewHolder) {
            BillingInfoEditedViewHolder infoViewHolder = (BillingInfoEditedViewHolder) holder;
            Date createdDate = new Date(items.get(position).getCreateAt());
            Date paymentDate = new Date(items.get(position).getPaymentDate());
            infoViewHolder.tvDate.setText(simpleDateFormat.format(createdDate));
            infoViewHolder.tvPaymentDate.setText(simpleDateFormat.format(paymentDate));
            if (items.get(position).getAccount() != null)
                infoViewHolder.tvAccount.setText(items.get(position).getAccount().getName());
            else {
                infoViewHolder.tvAccount.setText("None");
            }
            infoViewHolder.tvAmount.setText(String.valueOf(items.get(position).getAmount()) + " " + currency.getAbbr());
            infoViewHolder.tvCount.setText(String.valueOf(position));
            if (position != items.size() - 1) {
                if (!items.get(position).getAmount().equals(items.get(position + 1).getAmount())) {
                    infoViewHolder.tvAmount.setTextColor(context.getColor(R.color.colorRed));
                }
                if (items.get(position).getAccount() != null) {
                    if (items.get(position + 1).getAccount() == null) {
                        infoViewHolder.tvAccount.setTextColor(context.getColor(R.color.colorRed));
                    } else if (!items.get(position).getAccount().getId().equals(items.get(position + 1).getAccount().getId())) {
                        infoViewHolder.tvAccount.setTextColor(context.getColor(R.color.colorRed));
                    }
                } else {
                    if (items.get(position + 1).getAccount() != null)
                        infoViewHolder.tvAccount.setTextColor(context.getColor(R.color.colorRed));
                }
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else return 1;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class BillingInfoViewHolder extends BaseViewHolder {
        @BindView(R.id.llBackground)
        LinearLayout llBackground;
        @BindView(R.id.tvCreatedDate)
        TextView tvDate;
        @BindView(R.id.tvPaymentDate)
        TextView tvPaymentDate;
        @BindView(R.id.tvAccount)
        TextView tvAccount;
        @BindView(R.id.tvAmount)
        TextView tvAmount;

        public BillingInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public class BillingInfoEditedViewHolder extends BaseViewHolder {
        @BindView(R.id.tvCreatedDate)
        TextView tvDate;
        @BindView(R.id.tvPaymentDate)
        TextView tvPaymentDate;
        @BindView(R.id.tvAccount)
        TextView tvAccount;
        @BindView(R.id.tvAmount)
        TextView tvAmount;
        @BindView(R.id.tvCount)
        TextView tvCount;

        public BillingInfoEditedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
