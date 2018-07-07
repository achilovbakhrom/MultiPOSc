package com.jim.multipos.ui.consignment.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.inventory.IncomeProduct;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.data.db.model.products.Product;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IncomeProductConfigsDialog extends Dialog {

    private StockQueue stockQueue;
    private IncomeProduct incomeProduct;
    private OnIncomeProductConfigsListener listener;
    @BindView(R.id.etStockId)
    MpEditText etStockId;
    @BindView(R.id.etDateCreatead)
    TextView etDateCreated;
    @BindView(R.id.etDateExpired)
    TextView etDateExpired;
    @BindView(R.id.etDescription)
    EditText etDescription;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    @BindView(R.id.btnSave)
    MpButton btnSave;
    @BindView(R.id.llCreatedDate)
    LinearLayout llCreatedDate;
    @BindView(R.id.llExpiredDate)
    LinearLayout llExpiredDate;

    public IncomeProductConfigsDialog(@NonNull Context context, StockQueue stockQueue, IncomeProduct incomeProduct) {
        super(context);
        this.stockQueue = stockQueue;
        this.incomeProduct = incomeProduct;
    }

    public void setListener(OnIncomeProductConfigsListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View dialogView = getLayoutInflater().inflate(R.layout.invoice_advanced_configs, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Calendar created = Calendar.getInstance();
        Calendar expired = Calendar.getInstance();

        if (!incomeProduct.getDescription().isEmpty())
            etDescription.setText(incomeProduct.getDescription());

        if (stockQueue.getCreatedProductDate() != 0) {
            created.setTimeInMillis(stockQueue.getCreatedProductDate());
            etDateCreated.setText(simpleDateFormat.format(stockQueue.getCreatedProductDate()));
        } else etDateCreated.setText("");
        if (stockQueue.getExpiredProductDate() != 0) {
            expired.setTimeInMillis(stockQueue.getExpiredProductDate());
            etDateExpired.setText(simpleDateFormat.format(stockQueue.getExpiredProductDate()));
        } else etDateExpired.setText("");

        if (stockQueue.getStockId() != null)
            etStockId.setText(stockQueue.getStockId());

        llCreatedDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, i, i1, i2) -> {
                created.set(Calendar.YEAR, i);
                created.set(Calendar.MONTH, i1);
                created.set(Calendar.DAY_OF_MONTH, i2);
                etDateCreated.setText(simpleDateFormat.format(created.getTime()));

            }, created.get(Calendar.YEAR), created.get(Calendar.MONTH), created.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        llExpiredDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, i, i1, i2) -> {
                expired.set(Calendar.YEAR, i);
                expired.set(Calendar.MONTH, i1);
                expired.set(Calendar.DAY_OF_MONTH, i2);
                etDateExpired.setText(simpleDateFormat.format(expired.getTime()));

            }, expired.get(Calendar.YEAR), expired.get(Calendar.MONTH), expired.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        btnBack.setOnClickListener(v -> {
            dismiss();
        });
        btnSave.setOnClickListener(v -> {
            if (!etStockId.getText().toString().isEmpty())
                stockQueue.setStockId(etStockId.getText().toString());
            if (!etDateCreated.getText().toString().isEmpty())
                stockQueue.setCreatedProductDate(created.getTimeInMillis());
            if (!etDateExpired.getText().toString().isEmpty())
                stockQueue.setExpiredProductDate(expired.getTimeInMillis());
            if (!etDescription.getText().toString().isEmpty())
                incomeProduct.setDescription(etDescription.getText().toString());

            if (incomeProduct.getProduct().getStockKeepType() == Product.FEFO) {
                if (!etDateExpired.getText().toString().isEmpty()) {
                    stockQueue.setExpiredProductDate(expired.getTimeInMillis());
                    listener.onSaveClicked(incomeProduct, stockQueue);
                    dismiss();
                } else etDateExpired.setError("Please, enter expired date");
            } else {
                listener.onSaveClicked(incomeProduct, stockQueue);
                dismiss();
            }
        });
    }

    public interface OnIncomeProductConfigsListener {
        void onSaveClicked(IncomeProduct incomeProduct, StockQueue stockQueue);
    }
}
