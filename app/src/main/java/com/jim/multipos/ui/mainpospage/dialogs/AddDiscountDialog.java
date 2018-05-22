package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpSwitcher;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.utils.CommonUtils;
import com.jim.multipos.utils.TextWatcherOnTextChange;
import com.jim.multipos.utils.UIUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Handler;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Portable-Acer on 09.11.2017.
 */

public class AddDiscountDialog extends Dialog {

    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnNext)
    MpButton btnOk;
    @BindView(R.id.swDiscountType)
    MpSwitcher swDiscountType;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.etDiscountAmount)
    MpEditText etDiscountAmount;
    @BindView(R.id.etResultPrice)
    MpEditText etResultPrice;
    @BindView(R.id.etDiscountName)
    MpEditText etDiscountName;
    @BindView(R.id.tvDiscountAmountType)
    TextView tvDiscountAmountType;
    private int discountAmountType;
    private int discountType;
    private DiscountDialog.CallbackDiscountDialog callbackDiscountDialog;
    private double resultPrice = 0;
    private double discountValue = 0;

    public AddDiscountDialog(@NonNull Context context, DatabaseManager databaseManager, double price, int discountType, DiscountDialog.CallbackDiscountDialog callbackDiscountDialog, DecimalFormat formatter) {
        super(context);
        this.discountType = discountType;
        this.callbackDiscountDialog = callbackDiscountDialog;
        View dialogView = getLayoutInflater().inflate(R.layout.discount_manual_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

        tvPrice.setText(formatter.format(price));
        String abbr = databaseManager.getMainCurrency().getAbbr();
        if (swDiscountType.isLeft()) {
            discountAmountType = Discount.VALUE;
            tvType.setText(abbr);
        } else {
            discountAmountType = Discount.PERCENT;
            tvType.setText("%");
        }
        swDiscountType.setSwitcherStateChangedListener((isRight, isLeft) -> {
            if (isLeft) {
                discountAmountType = Discount.VALUE;
                tvType.setText(abbr);
                tvDiscountAmountType.setText(context.getString(R.string.discount_amount));
                if (!etResultPrice.getText().toString().isEmpty() && !etDiscountAmount.getText().toString().isEmpty()) {
                    discountValue = price - resultPrice;
                    etResultPrice.requestFocus();
                    etDiscountAmount.setText(formatter.format(discountValue));
                }
            } else {
                discountAmountType = Discount.PERCENT;
                tvType.setText("%");
                tvDiscountAmountType.setText(context.getString(R.string.discount_percent));
                if (!etResultPrice.getText().toString().isEmpty() && !etDiscountAmount.getText().toString().isEmpty()) {
                    discountValue = 100 - (100 * resultPrice / price);
                    etResultPrice.requestFocus();
                    etDiscountAmount.setText(formatter.format(discountValue));
                    if (discountValue > 100)
                        etDiscountAmount.setError(context.getString(R.string.discount_percent_cannot_be_bigger_hundred));
                }

            }
        });

        etDiscountAmount.addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (getCurrentFocus() == etDiscountAmount)
                    if (charSequence.length() != 0) {
                        try {
                            discountValue = formatter.parse(etDiscountAmount.getText().toString()).doubleValue();
                            double result = 0;
                            if (discountAmountType == Discount.VALUE) {
                                if (discountValue > price) {
                                    etDiscountAmount.setError(context.getString(R.string.discount_value_cant_be_bigger_than_price));
                                } else {
                                    result = price - discountValue;
                                    resultPrice = result;
                                    etResultPrice.setText(formatter.format(result));
                                }
                            } else {
                                if (discountValue > 100) {
                                    etDiscountAmount.setError(context.getString(R.string.discount_percent_cannot_be_bigger_hundred));
                                } else {
                                    result = price - (price * discountValue / 100);
                                    resultPrice = result;
                                    etResultPrice.setText(formatter.format(result));
                                }
                            }
                        } catch (Exception e) {
                            etDiscountAmount.setError(context.getString(R.string.invalid));
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
                            if (resultPrice <= price) {
                                double result = 0;
                                if (discountAmountType == Discount.VALUE)
                                    result = price - resultPrice;
                                else {
                                    result = 100 - (100 * resultPrice / price);
                                }
                                etDiscountAmount.setText(formatter.format(result));
                            } else
                                etResultPrice.setError(context.getString(R.string.result_price_cant_be_bigger));
                        } catch (Exception e) {
                            etResultPrice.setError(context.getString(R.string.invalid));
                        }
                    } else etDiscountAmount.setText(formatter.format(0));
            }
        });

        btnOk.setOnClickListener(view -> {
            if (etDiscountAmount.getText().toString().isEmpty()) {
                etDiscountAmount.setError(context.getString(R.string.enter_amount));
            } else if (etResultPrice.getText().toString().isEmpty()) {
                etResultPrice.setError(context.getString(R.string.enter_amount));
            } else if (etDiscountName.getText().toString().isEmpty()) {
                etDiscountName.setError(context.getString(R.string.disc_reacon_cant_empty));
            } else if (resultPrice > price) {
                etResultPrice.setError(context.getString(R.string.result_price_cant_be_bigger));
            } else if (discountValue > price){
                etDiscountAmount.setError("Discount value cannot be bigger than price");
            } else {
                new android.os.Handler().postDelayed(() -> {
                    Discount discount = new Discount();
                    discount.setIsManual(true);
                    discount.setAmount(discountValue);
                    discount.setCreatedDate(System.currentTimeMillis());
                    discount.setName(etDiscountName.getText().toString());
                    discount.setAmountType(discountAmountType);
                    discount.setUsedType(discountType);
                    this.callbackDiscountDialog.choiseManualDiscount(discount);
                    dismiss();

                }, 300);
                UIUtils.closeKeyboard(etDiscountName, getContext());
            }
        });

        btnCancel.setOnClickListener(view ->

                dismiss());

    }
}
