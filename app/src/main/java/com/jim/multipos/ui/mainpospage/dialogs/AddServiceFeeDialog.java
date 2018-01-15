package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpSwitcher;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.utils.TextWatcherOnTextChange;
import com.jim.multipos.utils.UIUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Portable-Acer on 11.11.2017.
 */

public class AddServiceFeeDialog extends Dialog {
    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnNext)
    MpButton btnNext;
    @BindView(R.id.swServiceFeeType)
    MpSwitcher swServiceFeeType;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.etServiceFeeAmount)
    MpEditText etServiceFeeAmount;
    @BindView(R.id.etResultPrice)
    MpEditText etResultPrice;
    @BindView(R.id.etServiceFeeName)
    MpEditText etServiceFeeName;
    @BindView(R.id.tvServiceAmountType)
    TextView tvServiceAmountType;
    private int serviceAmountType;
    private int serviceType;
    private ServiceFeeDialog.CallbackServiceFeeDialog callbackServiceFeeDialog;
    private double resultPrice = 0;
    private double serviceAmount = 0;

    public AddServiceFeeDialog(@NonNull Context context, DatabaseManager databaseManager, double price, int serviceType, ServiceFeeDialog.CallbackServiceFeeDialog callbackServiceFeeDialog, DecimalFormat formatter) {
        super(context);
        this.serviceType = serviceType;
        this.callbackServiceFeeDialog = callbackServiceFeeDialog;
        View dialogView = getLayoutInflater().inflate(R.layout.service_fee_manual_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

        tvPrice.setText(formatter.format(price));
        String abbr = databaseManager.getMainCurrency().getAbbr();
        if (swServiceFeeType.isLeft()) {
            serviceAmountType = Discount.VALUE;
            tvType.setText(abbr);
        } else {
            serviceAmountType = Discount.PERCENT;
            tvType.setText("%");
        }

        swServiceFeeType.setSwitcherStateChangedListener((isRight, isLeft) -> {
            if (isLeft) {
                serviceAmountType = Discount.VALUE;
                tvType.setText(abbr);
                tvServiceAmountType.setText(context.getString(R.string.service_fee_amount));
                if (!etResultPrice.getText().toString().isEmpty() && !etServiceFeeAmount.getText().toString().isEmpty()) {
                    serviceAmount = resultPrice - price;
                    etResultPrice.requestFocus();
                    etServiceFeeAmount.setText(formatter.format(serviceAmount));
                }
            } else {
                serviceAmountType = Discount.PERCENT;
                tvType.setText("%");
                tvServiceAmountType.setText(context.getString(R.string.service_fee_percent));
                if (!etResultPrice.getText().toString().isEmpty() && !etServiceFeeAmount.getText().toString().isEmpty()) {
                    serviceAmount = (100 * resultPrice / price) - 100;
                    etResultPrice.requestFocus();
                    etServiceFeeAmount.setText(formatter.format(serviceAmount));
                }

            }
        });

        etServiceFeeAmount.addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (getCurrentFocus() == etServiceFeeAmount)
                    if (charSequence.length() != 0) {
                        try {
                            serviceAmount = formatter.parse(etServiceFeeAmount.getText().toString()).doubleValue();
                            double result = 0;
                            if (serviceAmountType == Discount.VALUE) {
                                result = price + serviceAmount;
                                resultPrice = result;
                                etResultPrice.setText(formatter.format(result));
                            } else {
                                result = price + (price * serviceAmount / 100);
                                resultPrice = result;
                                etResultPrice.setText(formatter.format(result));
                            }
                        } catch (Exception e) {
                            etServiceFeeAmount.setError(context.getString(R.string.invalid));
                        }
                    } else {
                        etResultPrice.setText(formatter.format(0));
                    }
            }
        });

        etResultPrice.addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (getCurrentFocus() == etResultPrice)
                    if (charSequence.length() != 0) {
                        try {
                            resultPrice = formatter.parse(etResultPrice.getText().toString()).doubleValue();
                            if (resultPrice >= price) {
                                double result = 0;
                                if (serviceAmountType == Discount.VALUE)
                                    result = resultPrice - price;
                                else {
                                    result = (100 * resultPrice / price) - 100;
                                }
                                etServiceFeeAmount.setText(formatter.format(result));
                            }
                        } catch (Exception e) {
                            etResultPrice.setError(context.getString(R.string.invalid));
                        }
                    } else etServiceFeeAmount.setText(formatter.format(0));
            }
        });

        btnNext.setOnClickListener(view -> {
            if (etResultPrice.getText().toString().isEmpty() && etServiceFeeAmount.getText().toString().isEmpty()) {
                etServiceFeeAmount.setError(context.getString(R.string.enter_amount));
                etResultPrice.setError(context.getString(R.string.enter_amount));
            } else if (resultPrice < price) {
                etResultPrice.setError(context.getString(R.string.billing_price_cannot_be_lower));
            } else if (etServiceFeeName.getText().toString().isEmpty()) {
                etServiceFeeName.setError(context.getString(R.string.service_fee_reason_cant_be_empty));
            } else {
                new android.os.Handler().postDelayed(() -> {
                    ServiceFee serviceFee = new ServiceFee();
                    serviceFee.setAmount(serviceAmount);
                    serviceFee.setApplyingType(serviceType);
                    serviceFee.setType(serviceAmountType);
                    serviceFee.setCreatedDate(System.currentTimeMillis());
                    serviceFee.setName(etServiceFeeName.getText().toString());
                    callbackServiceFeeDialog.choiseManualServiceFee(serviceFee);
                    dismiss();
                },300);
                UIUtils.closeKeyboard(etServiceFeeName,getContext());
            }
        });

        btnCancel.setOnClickListener(view -> dismiss());

    }

}
