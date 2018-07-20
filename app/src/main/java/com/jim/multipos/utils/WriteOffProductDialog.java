package com.jim.multipos.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.consignment.dialogs.StockPositionsDialog;
import com.jim.multipos.ui.inventory.model.InventoryItem;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by developer on 16.11.2017.
 */

public class WriteOffProductDialog extends Dialog {
    private WriteOffCallback writeOffCallback;
    private Product product;
    private View dialogView;

    @BindView(R.id.tvStockType)
    TextView tvStockType;
    @BindView(R.id.flPosition)
    FrameLayout flPosition;
    @BindView(R.id.tvDialogTitle)
    TextView tvDialogTitle;

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
    OutcomeProduct outcomeProduct;
    double aDouble = 0;
    double v1 = 0;
    TextWatcherOnTextChange stock_out;

    public WriteOffProductDialog(@NonNull Context context, WriteOffCallback writeOffCallback, InventoryItem inventoryItem, DecimalFormat decimalFormat, DatabaseManager databaseManager, StockQueue stockQueue) {
        super(context);
        this.writeOffCallback = writeOffCallback;
        this.product = product;
        outcomeProduct = new OutcomeProduct();
        if (stockQueue != null) {
            outcomeProduct.setCustomPickSock(true);
            outcomeProduct.setPickedStockQueueId(stockQueue.getId());
            outcomeProduct.setSumCountValue(stockQueue.getAvailable());
        } else {
            outcomeProduct.setCustomPickSock(false);
            outcomeProduct.setSumCountValue(0d);
        }
        dialogView = getLayoutInflater().inflate(R.layout.write_off_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        tvDialogTitle.setText("Write-off: " + inventoryItem.getProduct().getName());
        if (!outcomeProduct.getCustomPickSock())
            switch (inventoryItem.getProduct().getStockKeepType()) {
                case Product.FIFO:
                    tvStockType.setText("FIFO");
                    break;
                case Product.LIFO:
                    tvStockType.setText("LIFO");
                    break;
                case Product.FEFO:
                    tvStockType.setText("FEFO");
                    break;
            }
        else tvStockType.setText("CUSTOM");

        outcomeProduct.setProduct(inventoryItem.getProduct());

        tvStockRecord.setText(decimalFormat.format(inventoryItem.getInventory()));

        tvUnit.setText(inventoryItem.getProduct().getMainUnit().getAbbr());
        if (inventoryItem.getProduct().getMainUnit().getAbbr().equals("pcs"))
            etShortage.setInputType(InputType.TYPE_CLASS_NUMBER);
        else etShortage.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL);
        flPosition.setOnClickListener(view -> {
            StockPositionsDialog stockPositionsDialog = new StockPositionsDialog(getContext(), outcomeProduct, new ArrayList<>(), null, databaseManager);
            stockPositionsDialog.setListener(new StockPositionsDialog.OnStockPositionsChanged() {
                @Override
                public void onConfirm(OutcomeProduct outcomeProduct) {
                    if (outcomeProduct.getCustomPickSock()) {
                        tvStockType.setText("Custom");
                    } else {
                        switch (inventoryItem.getProduct().getStockKeepType()) {
                            case Product.FIFO:
                                tvStockType.setText("FIFO");
                                break;
                            case Product.LIFO:
                                tvStockType.setText("LIFO");
                                break;
                            case Product.FEFO:
                                tvStockType.setText("FEFO");
                                break;
                        }
                    }
                    WriteOffProductDialog.this.outcomeProduct = outcomeProduct;
                    etShortage.removeTextChangedListener(stock_out);
                    etShortage.setText(decimalFormat.format(outcomeProduct.getSumCountValue()));
                    etShortage.addTextChangedListener(stock_out);

                }
            });
            stockPositionsDialog.show();
        });
        stock_out = new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                databaseManager.getAvailableCountForProduct(inventoryItem.getProduct().getId()).subscribe(available -> {
                    if (!etShortage.getText().toString().isEmpty()) {
                        try {
                            v1 = decimalFormat.parse(etShortage.getText().toString()).doubleValue();
                        } catch (Exception e) {
                            etShortage.setError(context.getString(R.string.invalid));
                            outcomeProduct.setSumCountValue(-1d);
                            return;
                        }
                    } else {
                        v1 = 0;
                    }
                    outcomeProduct.setCustomPickSock(false);
                    outcomeProduct.setPickedStockQueueId(0l);
                    switch (inventoryItem.getProduct().getStockKeepType()) {
                        case Product.FIFO:
                            tvStockType.setText("FIFO");
                            break;
                        case Product.LIFO:
                            tvStockType.setText("LIFO");
                            break;
                        case Product.FEFO:
                            tvStockType.setText("FEFO");
                            break;
                    }

                    if (available < v1) {
                        etShortage.setError("Stock out");
                        outcomeProduct.setSumCountValue(-1d);
                    } else {
                        outcomeProduct.setSumCountValue(v1);

                        etShortage.setError(null);
                    }
                    aDouble = inventoryItem.getInventory() - v1;
                    tvActual.setText(decimalFormat.format(aDouble));
                });
            }
        };

        etShortage.setText(decimalFormat.format(outcomeProduct.getSumCountValue()));
        tvActual.setText(decimalFormat.format(inventoryItem.getInventory() - outcomeProduct.getSumCountValue()));


        etShortage.addTextChangedListener(stock_out);


        btnNext.setOnClickListener(view -> {

            if (outcomeProduct.getSumCountValue() == 0 || outcomeProduct.getSumCountValue() < 0 || etShortage.getText().toString().isEmpty()) {
                etShortage.setError("Invalid value");
                return;
            }
            if (etReason.getText().toString().isEmpty()) {
                etReason.setError(context.getString(R.string.please_enter_write_off_reason));
                return;
            }
            outcomeProduct.setOutcomeType(OutcomeProduct.WASTE);
            outcomeProduct.setDiscription(etReason.getText().toString());
            UIUtils.closeKeyboard(etShortage, context);
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                outcomeProduct.setOutcomeDate(System.currentTimeMillis());
                writeOffCallback.writeOff(inventoryItem, outcomeProduct);
                dismiss();
            }, 300);
        });
        btnCancel.setOnClickListener(view -> {
            dismiss();
        });
    }

    public interface WriteOffCallback {
        void writeOff(InventoryItem inventoryItem, OutcomeProduct outcomeProduct);
    }


}
