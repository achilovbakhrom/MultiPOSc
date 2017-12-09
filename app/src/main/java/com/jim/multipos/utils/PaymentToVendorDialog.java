package com.jim.multipos.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.products.Vendor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by developer on 04.12.2017.
 */

public class PaymentToVendorDialog extends Dialog {
    private View dialogView;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.spAccount)
    MPosSpinner spAccount;
    @BindView(R.id.llFromAccount)
    LinearLayout llFromAccount;
    @BindView(R.id.chbFromAccount)
    MpCheckbox chbFromAccount;
    @BindView(R.id.etAmmount)
    MpEditText etAmmount;
    @BindView(R.id.tvCurrencyAbbr)
    TextView tvCurrencyAbbr;
    @BindView(R.id.tvVendorName)
    TextView tvVendorName;
    @BindView(R.id.tvVendorCantact)
    TextView tvVendorCantact;
    @BindView(R.id.btnWarningYES)
    MpButton btnWarningYES;
    @BindView(R.id.btnWarningNO)
    MpButton btnWarningNO;
    @BindView(R.id.etDate)
    MpEditText etDate;
    @BindView(R.id.etDisc)
    EditText etDisc;

    Calendar calendar;
    private List<Account> accounts;
    private PaymentToVendorCallback paymentToVendorCallback;
    SimpleDateFormat simpleDateFormat;
    public interface PaymentToVendorCallback{
        void onChanged();
        void onCancel();
    }
    public PaymentToVendorDialog(@NonNull Context context, Vendor vendor,PaymentToVendorCallback paymentToVendorCallback,DatabaseManager databaseManager) {
        super(context);
        this.accounts = databaseManager.getAccounts();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        calendar = Calendar.getInstance();
        this.paymentToVendorCallback = paymentToVendorCallback;
        dialogView = getLayoutInflater().inflate(R.layout.payment_to_vendor_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        llFromAccount.setOnClickListener(view -> {
            chbFromAccount.setChecked(!chbFromAccount.isChecked());
        });
        chbFromAccount.setCheckedChangeListener(isChecked -> {
            if(isChecked){
                tvAccount.setVisibility(View.VISIBLE);
                spAccount.setVisibility(View.VISIBLE);
            }else {
                tvAccount.setVisibility(View.GONE);
                spAccount.setVisibility(View.GONE);
            }
        });
        tvCurrencyAbbr.setText(databaseManager.getMainCurrency().getAbbr());
        tvVendorName.setText(vendor.getName());
        if(vendor.getName()!=null && !vendor.getName().isEmpty())
        tvVendorCantact.setText(context.getString(R.string.contact_name)+": "+vendor.getContactName());
        else tvVendorCantact.setVisibility(View.GONE);
        List<String> accountsString = new ArrayList<>();
        for(Account accName:accounts){
            accountsString.add(accName.getName());
        }
        btnWarningYES.setOnClickListener(view -> {
            double ammount = 0;
            etAmmount.setError(null);
            if(etAmmount.getText().toString().isEmpty()) {
                etAmmount.setError(context.getString(R.string.ammount_is_empty));
                return;
            }
            try{
                ammount = Double.parseDouble(etAmmount.getText().toString());
            }catch (Exception o){
                etAmmount.setError(context.getString(R.string.invalid));
                return;
            }
            if(ammount == 0){
                etAmmount.setError(context.getString(R.string.ammount_cant_be_zero));
                return;
            }
            BillingOperations billingOperations = new BillingOperations();
            billingOperations.setAmount(ammount);

            if(chbFromAccount.isChecked()){
                billingOperations.setAccount(accounts.get(spAccount.getSelectedPosition()));
            }else billingOperations.setAccount(null);

            billingOperations.setPaymentDate(calendar.getTimeInMillis());
            billingOperations.setCreateAt(System.currentTimeMillis());
            billingOperations.setIsActive(true);
            billingOperations.setIsNotModified(true);
            billingOperations.setVendor(vendor);
            billingOperations.setDescription(etDisc.getText().toString());
            billingOperations.setOperationType(BillingOperations.PAID_TO_CONSIGNMENT);
            databaseManager.insertBillingOperation(billingOperations).subscribe();
            paymentToVendorCallback.onChanged();
            dismiss();
        });
        btnWarningNO.setOnClickListener(view -> {
            paymentToVendorCallback.onCancel();
            dismiss();
        });
        etDate.setText(simpleDateFormat.format(calendar.getTime()));
        etDate.setOnClickListener(view -> {

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, i, i1, i2) -> {
                calendar.set(Calendar.YEAR,i);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.DAY_OF_MONTH,i2);
                etDate.setText(simpleDateFormat.format(calendar.getTime()));

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        spAccount.setAdapter(accountsString);
        chbFromAccount.setChecked(false);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

    }
}