package com.jim.multipos.ui.service_fee.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jim.mpviews.MpCheckbox;
import com.jakewharton.rxbinding2.view.RxView;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.ui.service_fee.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 28.08.17.
 */

public class ServiceFeeAdapter extends RecyclerView.Adapter<ServiceFeeAdapter.ViewHolder> {
    public interface OnClick {
        void setCheckedTaxed(boolean state, int position);
        void setCheckedActive(boolean state, int position);
    }

    private List<ServiceFee> serviceFeeList;
    private OnClick onClickCallback;
    private String[] types;
    private String[] appTypes;
    private List<Currency> currencies;

    public ServiceFeeAdapter(List<ServiceFee> serviceFeeList, List<Currency> currencies, OnClick onClickCallback, String[] types, String[] appTypes) {
        this.serviceFeeList = serviceFeeList;
        this.onClickCallback = onClickCallback;
        this.types = types;
        this.appTypes = appTypes;
        this.currencies = currencies;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_fee_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ServiceFee serviceFee = serviceFeeList.get(position);
/*
        holder.tvCurrency.setText("");
        //holder.tvName.setText(serviceFee.getName());
        holder.tvAmount.setText(String.valueOf(serviceFee.getAmount()));
        holder.tvType.setText(getType(serviceFee.getType()));
        //TODO
        holder.tvCurrency.setText("We must add currency");
        holder.tvAppType.setText(getAppType(serviceFee.getApplyingType()));
        *//*holder.chbTaxed.setChecked(serviceFee.getIsTaxed());
        holder.chbActive.setChecked(serviceFee.getIsActive());

        if (serviceFee.getCurrencyId() != null) {
            holder.tvCurrency.setText(serviceFee.getCurrency().getName());
        }*/
    }

    private String getType(String type) {
        String result;

        if (type.equals(Constants.TYPE_PERCENT)) {
            result = types[0];
        } else {
            result = types[1];
        }

        return result;
    }

    private String getAppType(String appType) {
        String result;

        switch (appType) {
            case Constants.APP_TYPE_ITEM:
                result = appTypes[0];
                break;
            case Constants.APP_TYPE_ORDER:
                result = appTypes[1];
                break;
            case Constants.APP_TYPE_ALL:
                result = appTypes[2];
                break;
            default:
                result = "";
        }

        return result;
    }

    @Override
    public int getItemCount() {
        return serviceFeeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etAmount)
        EditText etAmount;
        @BindView(R.id.tvType)
        TextView tvType;
        @BindView(R.id.tvCurrency)
        TextView tvCurrency;
        //@BindView(R.id.tvAppType)
        TextView tvAppType;
        /*@BindView(R.id.chbTaxed)
        MpCheckbox chbTaxed;*/
        @BindView(R.id.chbActive)
        MpCheckbox chbActive;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
/*
            RxView.clicks(chbTaxed).subscribe(aVoid -> {
               if (chbTaxed.isChecked()) {
                   chbTaxed.setChecked(false);
                   onClickCallback.setCheckedTaxed(false, getAdapterPosition());
               } else {
                   chbTaxed.setChecked(true);
                   onClickCallback.setCheckedTaxed(true, getAdapterPosition());
               }
            });*/

            RxView.clicks(chbActive).subscribe(aVoid -> {
                if (chbActive.isChecked()) {
                    chbActive.setChecked(false);
                    onClickCallback.setCheckedActive(false, getAdapterPosition());
                } else {
                    chbActive.setChecked(true);
                    onClickCallback.setCheckedActive(true, getAdapterPosition());
                }
            });
        }
    }
}
