package com.jim.multipos.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.inventory.IncomeProduct;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.consignment.dialogs.IncomeProductConfigsDialog;
import com.jim.multipos.ui.inventory.model.InventoryItem;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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


    @BindView(R.id.tvDialogTitle)
    TextView tvDialogTitle;
    @BindView(R.id.etCost)
    EditText etCost;
    @BindView(R.id.etStockId)
    EditText etStockId;
    @BindView(R.id.etExpiredDate)
    EditText etExpiredDate;
    @BindView(R.id.ivScanBarcode)
    ImageView ivScanBarcode;
    SimpleDateFormat simpleDateFormat;
    IncomeProduct incomeProduct;
    StockQueue stockQueue;
    Calendar expiredDate;
    double costDouble = 0;
    double aDouble = 0;
    double v1 = 0;
    public SurplusProductDialog(@NonNull Context context, SurplusCallback surplus, InventoryItem inventoryItem, DecimalFormat decimalFormat){
        super(context);
        incomeProduct = new IncomeProduct();
        stockQueue = new StockQueue();
        this.surplus = surplus;
        this.product = product;
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dialogView = getLayoutInflater().inflate(R.layout.surplus_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        etShortage.requestFocus();
        tvDialogTitle.setText("Surplus: "+inventoryItem.getProduct().getName());
        etCost.setText(0+"");
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
        etReason.addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                incomeProduct.setDescription(etReason.getText().toString());
            }
        });
        etExpiredDate.setOnClickListener(view -> {
            Calendar calendar;
            if(expiredDate == null){
                calendar = Calendar.getInstance();
            }else {
                calendar = expiredDate;
            }
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, i, i1, i2) -> {
                if(expiredDate == null) expiredDate = Calendar.getInstance();
                expiredDate.set(Calendar.YEAR, i);
                expiredDate.set(Calendar.MONTH, i1);
                expiredDate.set(Calendar.DAY_OF_MONTH, i2);
                etExpiredDate.setText(simpleDateFormat.format(expiredDate.getTime()));
                stockQueue.setExpiredProductDate(expiredDate.getTimeInMillis());
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
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
                if (!etCost.getText().toString().isEmpty()) {
                    try {
                        costDouble = Double.parseDouble(etCost.getText().toString());
                    } catch (Exception e) {
                        etCost.setError(context.getString(R.string.invalid));
                        return;
                    }
                } else {
                    costDouble = 0;
                }

                if(costDouble<0){
                    etCost.setError(context.getString(R.string.invalid));
                    return;
                }

                if(v1==0 || v1<0){
                    etShortage.setError(context.getString(R.string.invalid));
                    return;
                }

                if(incomeProduct.getDescription() == null && incomeProduct.getDescription().isEmpty()){
                    etReason.setError(context.getString(R.string.please_enter_replenishment_reason));
                    return;
                }

                UIUtils.closeKeyboard(etShortage,context);

                if(inventoryItem.getProduct().getStockKeepType() == Product.FEFO){
                    if(expiredDate == null) {
                        etExpiredDate.setError("For FEFO you should choose expired date");
                        return;
                    }else etExpiredDate.setError(null);
                }

                if (etShortage.getText().toString().isEmpty()){
                    etShortage.setError(context.getString(R.string.cannot_be_empty));
                } else if (etReason.getText().toString().isEmpty()){
                    etReason.setError(context.getString(R.string.cannot_be_empty));
                } else {
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        incomeProduct.setCostValue(costDouble);
                        incomeProduct.setCountValue(v1);
                        incomeProduct.setDescription(etReason.getText().toString());
                        incomeProduct.setIncomeDate(System.currentTimeMillis());
                        incomeProduct.setIncomeType(IncomeProduct.SURPLUS_PRODUCT);
                        incomeProduct.setProductId(inventoryItem.getProduct().getId());
                        if(expiredDate != null)
                        stockQueue.setExpiredProductDate(expiredDate.getTimeInMillis());
                        stockQueue.setAvailable(v1);
                        stockQueue.setClosed(false);
                        stockQueue.setIncomeCount(v1);
                        stockQueue.setCost(costDouble);
                        stockQueue.setIncomeProductDate(System.currentTimeMillis());
                        stockQueue.setProductId(inventoryItem.getProduct().getId());

                        surplus.surplus(inventoryItem,incomeProduct,stockQueue);
                        dismiss();
                    },300);
                }
        });

        btnCancel.setOnClickListener(view -> {
            dismiss();
        });
    }
    public interface SurplusCallback{
        void surplus(InventoryItem inventoryItem, IncomeProduct incomeProduct, StockQueue stockQueue);
    }


}
