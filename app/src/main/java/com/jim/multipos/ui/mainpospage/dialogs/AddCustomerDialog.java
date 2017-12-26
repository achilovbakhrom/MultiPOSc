package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.Customer;
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
    @BindView(R.id.tvDialogTitle)
    TextView tvDialogTitle;
    private Context context;
    private Customer customer;
    private UpdateCustomerCallback callback;
    long id = 0L;

    public AddCustomerDialog(@NonNull Context context, Customer customer, DatabaseManager databaseManager, UpdateCustomerCallback callback) {
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
        if (this.customer != null) {
            tvClientId.setText(String.valueOf(customer.getClientId()));
            etCustomerName.setText(customer.getName());
            etCustomerContact.setText(customer.getPhoneNumber());
            etCustomerAddress.setText(customer.getAddress());
            btnAddSave.setText(context.getString(R.string.update));
            tvDialogTitle.setText(context.getString(R.string.update_customer));
        } else {
            btnAddSave.setText(context.getString(R.string.add));
            id = databaseManager.getAllCustomers().blockingSingle().size() + 1;
            tvClientId.setText(String.valueOf(id));
            tvDialogTitle.setText(context.getString(R.string.add_customer));
        }
        btnAddSave.setOnClickListener(view -> {
            if (this.customer != null) {
                if (isValid()) {
                    this.customer.setName(etCustomerName.getText().toString());
                    this.customer.setPhoneNumber(etCustomerContact.getText().toString());
                    this.customer.setAddress(etCustomerAddress.getText().toString());
                    databaseManager.addCustomer(this.customer).subscribe();
                    callback.onUpdate();
                    dismiss();
                }
            } else {
                if (isValid()) {
                    Customer newCustomer = new Customer();
                    newCustomer.setName(etCustomerName.getText().toString());
                    newCustomer.setPhoneNumber(etCustomerContact.getText().toString());
                    newCustomer.setAddress(etCustomerAddress.getText().toString());
                    newCustomer.setClientId(id);
                    databaseManager.addCustomer(newCustomer).subscribe();
                    callback.onUpdate();
                    dismiss();
                }
            }
            UIUtils.closeKeyboard(etCustomerAddress, context);
        });
    }

    public interface UpdateCustomerCallback {
        void onUpdate();
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
        } else  return true;
    }
}
