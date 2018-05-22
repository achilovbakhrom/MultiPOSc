package com.jim.multipos.ui.billing_vendor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by developer on 30.11.2017.
 */

public class BillingOperartionsAdapter extends RecyclerView.Adapter<BillingOperartionsAdapter.BillingOperationsViewHolder> {
    private Context context;
    private SimpleDateFormat simpleDateFormat;
    private DecimalFormat decimalFormat;
    private DatabaseManager databaseManager;
    private List<BillingOperations> items;
    private BillingItemCallback billingItemCallback;

    public void setData(List<BillingOperations> items,BillingItemCallback billingItemCallback){
        this.items = items;
        this.billingItemCallback = billingItemCallback;
    }
    public void setData(List<BillingOperations> items){
        this.items = items;
        this.billingItemCallback = billingItemCallback;
    }
    public BillingOperartionsAdapter(Context context, DecimalFormat decimalFormat, DatabaseManager databaseManager){
        this.context = context;
        this.decimalFormat = decimalFormat;
        this.databaseManager = databaseManager;
        items = new ArrayList<>();
        simpleDateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
    }
    @Override
    public BillingOperartionsAdapter.BillingOperationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.billing_item, parent, false);
        return new BillingOperationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BillingOperartionsAdapter.BillingOperationsViewHolder holder, int position) {
        if(position%2==0) holder.llBackground.setBackgroundColor(Color.parseColor("#f9f9f9"));
        else holder.llBackground.setBackgroundColor(Color.parseColor("#f0f0f0"));
        BillingOperations billingOperations = items.get(position);
        if(BillingOperations.DEBT_CONSIGNMENT == billingOperations.getOperationType()){
            holder.ivBillingIcon.setImageResource(R.drawable.imcome_consigment_billing);
            holder.tvType.setText(R.string.consignment_income);
            if(billingOperations.getConsignment()!=null && !billingOperations.getConsignment().getDescription().isEmpty())
                holder.tvDiscription.setText(billingOperations.getConsignment().getDescription());
            else  holder.tvDiscription.setText("-");

            holder.tvExtra.setText(billingOperations.getConsignment().getConsignmentNumber());
        }else if(BillingOperations.RETURN_TO_VENDOR == billingOperations.getOperationType()){
            holder.ivBillingIcon.setImageResource(R.drawable.outcome_consigment_billing);
            holder.tvType.setText(R.string.consigment_return);
            if(billingOperations.getConsignment()!=null && !billingOperations.getConsignment().getDescription().isEmpty())
                holder.tvDiscription.setText(billingOperations.getConsignment().getDescription());
            else  holder.tvDiscription.setText("-");

            holder.tvExtra.setText(billingOperations.getConsignment().getConsignmentNumber());
        }else if(BillingOperations.PAID_TO_CONSIGNMENT == billingOperations.getOperationType()){
            holder.ivBillingIcon.setImageResource(R.drawable.pay_to_vendor_billing);
            holder.tvType.setText(R.string.payment_vendor);
            holder.tvExtra.setText("-");
            if(billingOperations.getDescription()!=null && !billingOperations.getDescription().isEmpty())
            holder.tvDiscription.setText(billingOperations.getDescription());
            else holder.tvDiscription.setText("-");
        }
        holder.tvDate.setText(simpleDateFormat.format(billingOperations.getPaymentDate()));

        if(billingOperations.getAmount()<0){
            holder.tvAmount.setTextColor(Color.parseColor("#df4f4f"));
        }else holder.tvAmount.setTextColor(Color.parseColor("#36a614"));

        Currency currency = databaseManager.getMainCurrency();
         holder.tvAmount.setText(String.format("%s %s", decimalFormat.format(billingOperations.getAmount()), currency.getAbbr()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface BillingItemCallback{
        void onInfoClicked(BillingOperations billingOperations);
    }

    public class BillingOperationsViewHolder extends BaseViewHolder {
        @BindView(R.id.llBackground)
        LinearLayout llBackground;
        @BindView(R.id.ivBillingIcon)
        ImageView ivBillingIcon;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvType)
        TextView tvType;
        @BindView(R.id.tvExtra)
        TextView tvExtra;
        @BindView(R.id.tvDiscription)
        TextView tvDiscription;
        @BindView(R.id.tvAmount)
        TextView tvAmount;
        @BindView(R.id.ivInfo)
        ImageView ivInfo;

        public BillingOperationsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivInfo.setOnClickListener(view1 -> {
                billingItemCallback.onInfoClicked(items.get(getAdapterPosition()));
            });
        }
    }
}
