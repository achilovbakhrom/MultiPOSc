package com.jim.multipos.ui.billing_vendor.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.history.BillingOperationsHistory;
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
    private List<Object> items;
    private DecimalFormat decimalFormat;

    public void setData(List<Object> items) {
        this.items = items;
    }

    public BillingInfoAdapter(Context context, Currency currency) {
        this.context = context;
        this.currency = currency;
        items = new ArrayList<>();
        simpleDateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        decimalFormat = BaseAppModule.getFormatter();
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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BillingInfoViewHolder) {
            BillingInfoViewHolder infoViewHolder = (BillingInfoViewHolder) holder;
            if (items.get(position) instanceof BillingOperations) {
                BillingOperations billingOperations = (BillingOperations) items.get(position);
                Date createdDate = new Date(billingOperations.getCreateAt());
                Date paymentDate = new Date(billingOperations.getPaymentDate());
                infoViewHolder.tvDate.setText(simpleDateFormat.format(createdDate));
                infoViewHolder.tvPaymentDate.setText(simpleDateFormat.format(paymentDate));
                infoViewHolder.tvDescription.setText(billingOperations.getDescription());
                if (billingOperations.getDescription() != null && !billingOperations.getDescription().equals(""))
                    infoViewHolder.tvDescription.setText(billingOperations.getDescription());
                else {
                    infoViewHolder.tvDescription.setText(context.getString(R.string.none));
                }
                if (billingOperations.getAccount() != null)
                    infoViewHolder.tvAccount.setText(billingOperations.getAccount().getName());
                else {
                    infoViewHolder.tvAccount.setText(context.getString(R.string.none));
                }
                infoViewHolder.tvAmount.setText(decimalFormat.format(billingOperations.getAmount()) + " " + currency.getAbbr());
                if (items.size() > 1) {
                    BillingOperationsHistory nextOperation = (BillingOperationsHistory) items.get(position + 1);
                    infoViewHolder.llBackground.setVisibility(View.VISIBLE);
                    if (!billingOperations.getAmount().equals(nextOperation.getAmount())) {
                        infoViewHolder.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                    }
                    if (billingOperations.getAccount() != null) {
                        if (nextOperation.getAccount() == null) {
                            infoViewHolder.tvAccount.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                        } else if (!billingOperations.getAccount().getId().equals(nextOperation.getAccount().getId())) {
                            infoViewHolder.tvAccount.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                        }
                    } else {
                        if (nextOperation.getAccount() != null)
                            infoViewHolder.tvAccount.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                    }
                    if (!billingOperations.getDescription().equals(nextOperation.getDescription())) {
                        infoViewHolder.tvDescription.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                    }
                }
            } else if (items.get(position) instanceof BillingOperationsHistory) {
                BillingOperationsHistory billingOperations = (BillingOperationsHistory) items.get(position);

                Date createdDate = new Date(billingOperations.getCreateAt());
                Date paymentDate = new Date(billingOperations.getPaymentDate());
                infoViewHolder.tvDate.setText(simpleDateFormat.format(createdDate));
                infoViewHolder.tvPaymentDate.setText(simpleDateFormat.format(paymentDate));
                infoViewHolder.tvDescription.setText(billingOperations.getDescription());
                if (billingOperations.getDescription() != null && !billingOperations.getDescription().equals(""))
                    infoViewHolder.tvDescription.setText(billingOperations.getDescription());
                else {
                    infoViewHolder.tvDescription.setText(context.getString(R.string.none));
                }
                if (billingOperations.getAccount() != null)
                    infoViewHolder.tvAccount.setText(billingOperations.getAccount().getName());
                else {
                    infoViewHolder.tvAccount.setText(context.getString(R.string.none));
                }
                infoViewHolder.tvAmount.setText(decimalFormat.format(billingOperations.getAmount()) + " " + currency.getAbbr());
                if (items.size() > 1) {
                    BillingOperationsHistory nextOperation = (BillingOperationsHistory) items.get(position + 1);
                    infoViewHolder.llBackground.setVisibility(View.VISIBLE);
                    if (!billingOperations.getAmount().equals(nextOperation.getAmount())) {

                        infoViewHolder.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                    }
                    if (billingOperations.getAccount() != null) {
                        if (nextOperation.getAccount() == null) {
                            infoViewHolder.tvAccount.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                        } else if (!billingOperations.getAccount().getId().equals(nextOperation.getAccount().getId())) {
                            infoViewHolder.tvAccount.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                        }
                    } else {
                        if (nextOperation.getAccount() != null)
                            infoViewHolder.tvAccount.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                    }
                    if (!billingOperations.getDescription().equals(nextOperation.getDescription())) {
                        infoViewHolder.tvDescription.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                    }
                }
            }

        } else if (holder instanceof BillingInfoEditedViewHolder) {
            BillingOperationsHistory history = (BillingOperationsHistory) items.get(position);

            BillingInfoEditedViewHolder infoViewHolder = (BillingInfoEditedViewHolder) holder;
            Date createdDate = new Date(history.getCreateAt());
            Date paymentDate = new Date(history.getPaymentDate());
            infoViewHolder.tvDate.setText(simpleDateFormat.format(createdDate));
            if (history.getDescription() != null && !history.getDescription().equals(""))
                infoViewHolder.tvDescription.setText(history.getDescription());
            else {
                infoViewHolder.tvDescription.setText(context.getString(R.string.none));
            }
            infoViewHolder.tvPaymentDate.setText(simpleDateFormat.format(paymentDate));
            if (history.getAccount() != null)
                infoViewHolder.tvAccount.setText(history.getAccount().getName());
            else {
                infoViewHolder.tvAccount.setText(context.getString(R.string.none));
            }
            infoViewHolder.tvAmount.setText(decimalFormat.format(history.getAmount()) + " " + currency.getAbbr());
            infoViewHolder.tvCount.setText(String.valueOf(position));
            if (position != items.size() - 1) {
                BillingOperationsHistory nextHistory = (BillingOperationsHistory) items.get(position + 1);
                if (!history.getAmount().equals(nextHistory.getAmount())) {
                    infoViewHolder.tvAmount.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                }
                if (history.getAccount() != null) {
                    if (nextHistory.getAccount() == null) {
                        infoViewHolder.tvAccount.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                    } else if (!history.getAccount().getId().equals(nextHistory.getAccount().getId())) {
                        infoViewHolder.tvAccount.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                    }
                } else {
                    if (nextHistory.getAccount() != null)
                        infoViewHolder.tvAccount.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                }
                if (!history.getDescription().equals(nextHistory.getDescription())) {
                    infoViewHolder.tvDescription.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
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
        @BindView(R.id.tvDescription)
        TextView tvDescription;

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
        @BindView(R.id.tvDescription)
        TextView tvDescription;

        public BillingInfoEditedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
