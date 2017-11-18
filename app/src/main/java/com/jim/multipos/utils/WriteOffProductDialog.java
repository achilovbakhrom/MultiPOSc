package com.jim.multipos.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpSpinner;
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

public class WriteOffProductDialog extends Dialog {
    private WriteOffCallback writeOffCallback;
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
    public WriteOffProductDialog(@NonNull Context context, WriteOffCallback writeOffCallback, InventoryItem inventoryItem, DecimalFormat decimalFormat){
        super(context);
        this.writeOffCallback = writeOffCallback;
        this.product = product;
        dialogView = getLayoutInflater().inflate(R.layout.write_off_dialog, null);
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
                double v1;
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
                aDouble = inventoryItem.getInventory() - v1;
                tvActual.setText(decimalFormat.format(aDouble));
            }
        });
        btnNext.setOnClickListener(view -> {
            if(aDouble!=0) {
                UIUtils.closeKeyboard(etShortage,context);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    writeOffCallback.writeOff(inventoryItem, inventoryItem.getProduct().getVendor().get(spVenders.getSelectedPosition()), aDouble, etReason.getText().toString());
                    dismiss();
                },300);

            }
            else etShortage.setError(context.getString(R.string.write_of_zero));
        });
        btnCancel.setOnClickListener(view -> {
            dismiss();
        });
    }
    public interface WriteOffCallback{
        void writeOff(InventoryItem inventoryItem,Vendor vendor, double v,String etReason);
    }


}
