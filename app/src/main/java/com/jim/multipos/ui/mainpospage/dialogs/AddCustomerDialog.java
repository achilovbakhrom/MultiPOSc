package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.ui.mainpospage.connection.MainPageConnection;
import com.jim.multipos.utils.BarcodeStack;
import com.jim.multipos.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sirojiddin on 25.12.2017.
 */

public class AddCustomerDialog extends Dialog {

    @BindView(R.id.tvClientId)
    TextView tvClientId;
    @BindView(R.id.etCustomerName)
    MpEditText etCustomerName;
    @BindView(R.id.etCustomerContact)
    MpEditText etCustomerContact;
    @BindView(R.id.etCustomerAddress)
    MpEditText etCustomerAddress;
    @BindView(R.id.btnAddSave)
    MpButton btnAddSave;
    @BindView(R.id.etCustomerBarcode)
    MpEditText etCustomerBarcode;
    @BindView(R.id.ivScanBarcode)
    ImageView ivScanBarcode;
    @BindView(R.id.tvDialogTitle)
    TextView tvDialogTitle;
    private Context context;
    private Customer customer;
    private UpdateCustomerCallback callback;
    long id = 0L;

    public AddCustomerDialog(@NonNull Context context, Customer customer, DatabaseManager databaseManager, UpdateCustomerCallback callback, MainPageConnection mainPageConnection, BarcodeStack barcodeStack) {
        super(context);
        this.context = context;
        this.customer = customer;
        this.callback = callback;
        View dialogView = getLayoutInflater().inflate(R.layout.add_customer_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        barcodeStack.register(barcode -> {
            etCustomerBarcode.setText(barcode);
        });
        etCustomerContact.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        setOnDismissListener(o -> {
            barcodeStack.unregister();
        });
        if (this.customer != null) {
            tvClientId.setText(String.valueOf(customer.getClientId()));
            etCustomerName.setText(customer.getName());
            etCustomerContact.setText(customer.getPhoneNumber());
            etCustomerAddress.setText(customer.getAddress());
            etCustomerBarcode.setText(customer.getQrCode());
            btnAddSave.setText(context.getString(R.string.update));
            tvDialogTitle.setText(context.getString(R.string.update_customer));
        } else {
            btnAddSave.setText(context.getString(R.string.add));
            id = databaseManager.getAllCustomers().blockingSingle().size() + 1;
            tvClientId.setText(String.valueOf(id));
            tvDialogTitle.setText(context.getString(R.string.add_customer));
        }

        ivScanBarcode.setOnClickListener(view -> mainPageConnection.scanBarcode(true));

        btnAddSave.setOnClickListener(view -> {
            if (this.customer != null) {
                if (isValid()) {
                    this.customer.setName(etCustomerName.getText().toString());
                    this.customer.setPhoneNumber(etCustomerContact.getText().toString());
                    this.customer.setAddress(etCustomerAddress.getText().toString());
                    this.customer.setQrCode(etCustomerBarcode.getText().toString());
                    databaseManager.addCustomer(this.customer).subscribe();
                    callback.onUpdate(null);
                    dismiss();
                }
            } else {
                if (isValid()) {
                    Customer newCustomer = new Customer();
                    newCustomer.setName(etCustomerName.getText().toString());
                    if (etCustomerContact.getText().toString().isEmpty())
                        newCustomer.setPhoneNumber("-");
                    else
                        newCustomer.setPhoneNumber(etCustomerContact.getText().toString());
                    if (etCustomerAddress.getText().toString().isEmpty())
                        newCustomer.setAddress("-");
                    else
                        newCustomer.setAddress(etCustomerAddress.getText().toString());
                    if (etCustomerBarcode.getText().toString().isEmpty())
                        newCustomer.setQrCode("-");
                    else
                        newCustomer.setQrCode(etCustomerBarcode.getText().toString());
                    newCustomer.setClientId(id);
                    databaseManager.addCustomer(newCustomer).subscribe();
                    callback.onUpdate(newCustomer);
                    dismiss();
                }
            }
            UIUtils.closeKeyboard(etCustomerAddress, context);
        });
    }

    public void setBarcode(String barcode) {
        etCustomerBarcode.setText(barcode);
    }

    public interface UpdateCustomerCallback {
        void onUpdate(Customer newCustomer);
    }

    @OnClick(R.id.btnBack)
    public void onBack() {
        UIUtils.closeKeyboard(etCustomerAddress, context);
        dismiss();
    }

    @NonNull
    private Boolean isValid() {
        if (etCustomerName.getText().toString().isEmpty()) {
            etCustomerName.setError(context.getString(R.string.enter_name));
            return false;
        } else if (etCustomerContact.getText().toString().isEmpty()) {
            etCustomerContact.setError(context.getString(R.string.enter_phone));
            return false;
        } else return true;
    }
}
