package com.jim.multipos.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jim.mpviews.MPosSpinner;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.inventory.model.InventoryItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by developer on 16.11.2017.
 */

public class SurplusProductDialog extends Dialog {
    private SurplusCallback surplus;
    private Product product;
    private View dialogView;

    @BindView(R.id.tvProductName)
    TextView tvProductName;
    @BindView(R.id.spVenders)
    MPosSpinner spVenders;
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

    double aDouble = 0;
    double v1 = 0;
    public SurplusProductDialog(@NonNull Context context, SurplusCallback surplus, InventoryItem inventoryItem, DecimalFormat decimalFormat){
        super(context);
        this.surplus = surplus;
        this.product = product;
        dialogView = getLayoutInflater().inflate(R.layout.surplus_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        tvProductName.setText(inventoryItem.getProduct().getName());

        List<String> vendorsName= new ArrayList<>();
        for (Vendor vendor:inventoryItem.getProduct().getVendor()) {
            vendorsName.add(vendor.getName());
        }
        spVenders.setAdapter(vendorsName);
        tvStockRecord.setText(decimalFormat.format(inventoryItem.getInventory()));
        tvActual.setText(decimalFormat.format(inventoryItem.getInventory()));
        tvUnit.setText(inventoryItem.getProduct().getMainUnit().getAbbr());
        if(inventoryItem.getProduct().getMainUnit().getAbbr().equals("pcs"))
        etShortage.setInputType(InputType.TYPE_CLASS_NUMBER );
        else etShortage.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etShortage.addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!etShortage.getText().toString().isEmpty()){
                    try {
                        v1 = Double.parseDouble(etShortage.getText().toString());
                    }catch (Exception e){
                        etShortage.setError(context.getString(R.string.invalid));
                        return;
                    }
                }else {
                    v1= 0;
                }
                etShortage.setError(null);
                aDouble = inventoryItem.getInventory() + v1;
                tvActual.setText(decimalFormat.format(aDouble));
            }
        });
        btnNext.setOnClickListener(view -> {
                if(!etShortage.getText().toString().isEmpty()){
                    try {
                        v1 = Double.parseDouble(etShortage.getText().toString());
                    }catch (Exception e){
                        etShortage.setError(context.getString(R.string.invalid));
                        return;
                    }
                }else {
                    v1= 0;
                }

                if(v1==0 || v1<0){
                    etShortage.setError(context.getString(R.string.invalid));
                    return;
                }

                if(etReason.getText().toString().isEmpty()){
                    etReason.setError(context.getString(R.string.please_enter_replenishment_reason));
                    return;
                }

                aDouble = inventoryItem.getInventory() + v1;
                UIUtils.closeKeyboard(etShortage,context);
                if (etShortage.getText().toString().isEmpty()){
                    etShortage.setError(context.getString(R.string.cannot_be_empty));
                } else if (etReason.getText().toString().isEmpty()){
                    etReason.setError(context.getString(R.string.cannot_be_empty));
                } else {
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        surplus.surplus(inventoryItem, inventoryItem.getProduct().getVendor().get(spVenders.getSelectedPosition()), aDouble, etReason.getText().toString(), v1);
                        dismiss();
                    },300);
                }
        });
        btnCancel.setOnClickListener(view -> {
            dismiss();
        });
    }
    public interface SurplusCallback{
        void surplus(InventoryItem inventoryItem, Vendor vendor, double v, String etReason, double shortage);
    }


}
