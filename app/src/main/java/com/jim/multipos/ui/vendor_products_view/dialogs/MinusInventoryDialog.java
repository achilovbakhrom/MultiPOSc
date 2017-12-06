package com.jim.multipos.ui.vendor_products_view.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.inventory.InventoryState;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendor_products_view.VendorProductsViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.jim.multipos.ui.vendor_products_view.fragments.VendorDetailsList.INVENTORY_POSITION;

/**
 * Created by Portable-Acer on 20.11.2017.
 */

public class MinusInventoryDialog extends DialogFragment {
    public interface MinusInventoryDialogListener {
        void updateInventory(InventoryState inventory, double shortage);
    }

    @BindView(R.id.tvProductName)
    TextView tvProductName;
    @BindView(R.id.tvVender)
    TextView tvVender;
    @BindView(R.id.tvStockRecord)
    TextView tvStockRecord;
    @BindView(R.id.etShortage)
    TextView etShortage;
    @BindView(R.id.tvActual)
    TextView tvActual;
    @BindView(R.id.tvUnit)
    TextView tvUnit;
    @BindView(R.id.etReason)
    TextView etReason;
    @BindView(R.id.btnCancel)
    TextView btnCancel;
    @BindView(R.id.btnNext)
    TextView btnNext;
    private Unbinder unbinder;
    private InventoryState inventory;
    private Vendor vendor;
    private MinusInventoryDialog.MinusInventoryDialogListener listener;
    private double shortage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.minus_inventory_dialog, container, false);
        getDialog().getWindow().getDecorView().setBackgroundResource(R.color.colorTransparent);
        unbinder = ButterKnife.bind(this, view);
        inventory = ((VendorProductsViewActivity) getActivity()).getPresenter().getInventoryState(getArguments().getInt((INVENTORY_POSITION)));
        vendor = ((VendorProductsViewActivity) getActivity()).getPresenter().getVendor();
        tvProductName.setText(inventory.getProduct().getName());
        tvVender.setText(vendor.getName());
        tvStockRecord.setText(((VendorProductsViewActivity) getActivity()).getDecimalFormat().format(inventory.getValue()));
        tvUnit.setText(inventory.getProduct().getMainUnit().getAbbr());
        tvActual.setText(((VendorProductsViewActivity) getActivity()).getDecimalFormat().format(inventory.getValue()));

        etShortage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!etShortage.getText().toString().isEmpty()) {
                    tvActual.setText(((VendorProductsViewActivity) getActivity()).getDecimalFormat().format(inventory.getValue() - Double.parseDouble(etShortage.getText().toString())));
                } else {
                    tvActual.setText(((VendorProductsViewActivity) getActivity()).getDecimalFormat().format(inventory.getValue()));
                }
            }
        });

        RxView.clicks(btnNext).subscribe(o -> {
            if (!etShortage.getText().toString().isEmpty()) {
                shortage = Double.parseDouble(etShortage.getText().toString()) * -1;
                inventory.setValue(Double.parseDouble(tvActual.getText().toString()));
                listener.updateInventory(inventory, shortage);
            }
            dismiss();
        });

        RxView.clicks(btnCancel).subscribe(o -> {
            dismiss();
        });

        return view;
    }

    public void setMinusInventoryDialogListener(MinusInventoryDialog.MinusInventoryDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
