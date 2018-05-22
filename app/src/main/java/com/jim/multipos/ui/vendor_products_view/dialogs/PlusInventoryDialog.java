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
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendor_products_view.VendorProductsViewActivity;
import com.jim.multipos.ui.vendor_products_view.model.ProductState;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.jim.multipos.ui.vendor_products_view.fragments.VendorDetailsList.INVENTORY_POSITION;

/**
 * Created by Portable-Acer on 20.11.2017.
 */

public class PlusInventoryDialog extends DialogFragment {
    public interface PlusInventoryDialogListener {
        void updateInventory(ProductState inventory, double shortage, String reason);
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
    private ProductState inventory;
    private Vendor vendor;
    private PlusInventoryDialogListener listener;
    private double shortage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.plus_inventory_dialog, container, false);
        getDialog().getWindow().getDecorView().setBackgroundResource(R.color.colorTransparent);
        unbinder = ButterKnife.bind(this, view);
        inventory = ((VendorProductsViewActivity) getActivity()).getPresenter().getProductState(getArguments().getInt((INVENTORY_POSITION)));
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
                    tvActual.setText(((VendorProductsViewActivity) getActivity()).getDecimalFormat().format(inventory.getValue() + Double.parseDouble(etShortage.getText().toString())));
                } else {
                    tvActual.setText(((VendorProductsViewActivity) getActivity()).getDecimalFormat().format(inventory.getValue()));
                }
            }
        });

        RxView.clicks(btnNext).subscribe(o -> {
            if (etShortage.getText().toString().isEmpty()){
                etShortage.setError(getContext().getString(R.string.please_enter_replashment_reason));
            } else if (etReason.getText().toString().isEmpty()){
                etReason.setError(getContext().getString(R.string.please_enter_replashment_reason));
            } else {
                shortage = Double.parseDouble(etShortage.getText().toString());
                listener.updateInventory(inventory ,shortage, etReason.getText().toString());
                dismiss();
            }
        });

        RxView.clicks(btnCancel).subscribe(o -> {
            dismiss();
        });

        return view;
    }

    public void setPlusInventoryDialogListener(PlusInventoryDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
