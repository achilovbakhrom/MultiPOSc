package com.jim.multipos.ui.mainpospage.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpTripleSwitcher;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.utils.validator.MultipleCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

/**
 * Created by Portable-Acer on 09.11.2017.
 */

public class AddDiscountDialog extends DialogFragment {
    public interface OnDiscountDialogListener {
        void dismiss();
        void addDiscount(double amount, String description, int amountType);
    }

    @BindView(R.id.tsDiscountType)
    MpTripleSwitcher tsDiscountType;
    @NotEmpty(messageId = R.string.enter_value)
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.ivClearAmount)
    ImageView ivClearAmount;
    @BindView(R.id.etDescription)
    MpEditText etDescription;
    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnNext)
    MpButton btnNext;
    private Unbinder unbinder;
    private OnDiscountDialogListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discount_manual_dialog, container, false);
        getDialog().getWindow().getDecorView().setBackgroundResource(R.color.colorTransparent);
        unbinder = ButterKnife.bind(this, view);

        tsDiscountType.setStateChangedListener((isRight, isCenter, isLeft) -> {
            if (isLeft) {
                checkAmount();
            } else if (isCenter) {
                try {
                    if (!etAmount.getText().toString().isEmpty()) {
                        double amount = Double.parseDouble(etAmount.getText().toString());

                        if (tsDiscountType.isCenter() && amount > 100) {
                            etAmount.setError(getString(R.string.percent_can_not_be_more_hunder));
                        }
                    } else {
                        etAmount.setError(getString(R.string.enter_value));
                    }
                } catch (NumberFormatException e) {
                    etAmount.setError(getString(R.string.invalid));
                }
            } else if (isRight) {
                checkAmount();
            }
        });

        RxView.clicks(btnCancel).subscribe(o -> {
            dismiss();
        });

        RxView.clicks(btnNext).subscribe(o -> {
            if (FormValidator.validate(this, new MultipleCallback())) {
                try {
                    double amount = Double.parseDouble(etAmount.getText().toString());

                    if (tsDiscountType.isCenter() && amount > 100) {
                        etAmount.setError(getString(R.string.percent_can_not_be_more_hunder));
                    } else {
                        String[] amountTypes = getResources().getStringArray(R.array.discount_amount_types_abr);
                        int amountType = -1;

                        if (tsDiscountType.isLeft()) {
                            amountType = Discount.VALUE;
                        } else if (tsDiscountType.isCenter()) {
                            amountType = Discount.PERCENT;
                        } else if (tsDiscountType.isRight()) {
                            amountType = Discount.REPRICE;
                        }

                        listener.addDiscount(amount, etDescription.getText().toString(), amountType);
                        listener.dismiss();
                        dismiss();
                    }
                } catch (NumberFormatException e) {
                    etAmount.setError(getString(R.string.invalid));
                }
            }
        });

        RxView.clicks(ivClearAmount).subscribe(o -> {
            etAmount.setText("");
        });

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (!s.toString().isEmpty()) {
                        double amount = Double.parseDouble(etAmount.getText().toString());

                        if (tsDiscountType.isCenter() && amount > 100) {
                            etAmount.setError(getString(R.string.percent_can_not_be_more_hunder));
                        }
                    } else {
                        etAmount.setError(getString(R.string.enter_value));
                    }
                } catch (NumberFormatException e) {
                    etAmount.setError(getString(R.string.invalid));
                }
            }
        });

        return view;
    }

    private void checkAmount() {
        try {
            if (!etAmount.getText().toString().isEmpty()) {
                double amount = Double.parseDouble(etAmount.getText().toString());

                etAmount.setError(null);
            } else {
                etAmount.setError(getString(R.string.enter_value));
            }
        } catch (NumberFormatException e) {
            etAmount.setError(getString(R.string.invalid));
        }
    }

    public void setOnDiscountDialogListener(OnDiscountDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();

        super.onDestroyView();
    }
}
