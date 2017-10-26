package com.jim.multipos.ui.customers_edit.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpActionButton;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseAdapter;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customers_edit.entity.CustomersWithCustomerGroups;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.WarningDialog;
import com.jim.multipos.utils.validator.MultipleCallback;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

/**
 * Created by user on 02.09.17.
 */

public class CustomerAdapter extends BaseAdapter<CustomersWithCustomerGroups, BaseViewHolder> {
    public static final int CUSTOMER_ITEM = 0;
    public static final int CUSTOMER_TYPE_ITEM = 1;

    public interface OnClickListener {
        void refreshQrCode(int position);

        void removeCustomer(int position);

        void saveCustomer(int position, String fullName, String phone, String address, String qrCode);

        void showCustomerCustomerGroupsDialog(int position);

        void showCustomerCustomerGroupsDialog();

        void addCustomer(String clientId, String fullName, String phone, String address, String qrCode, List<CustomerGroup> selectedGroups);

        void refreshCustomerItemQrCode();

        void updateClientId();

        boolean isCustomerExists(String name);
    }

    private OnClickListener onClickListener;
    private List<Integer> editedItems;
    private Context context;
    private List<CustomerGroup> customerTypeSelectedCustomerGroups;
    private Customer customer;

    public CustomerAdapter(Context context, List<CustomerGroup> customerTypeSelectedCustomerGroups, List<CustomersWithCustomerGroups> customersWithCustomerGroups, String clientId, String qrCode, OnClickListener onClickListener) {
        super(customersWithCustomerGroups);
        this.customerTypeSelectedCustomerGroups = customerTypeSelectedCustomerGroups;
        this.onClickListener = onClickListener;
        this.context = context;
        editedItems = new ArrayList<>();
        customer = new Customer();
        customer.setClientId(Long.parseLong(clientId));
        customer.setQrCode(qrCode);
        customer.setName("");
        customer.setAddress("");
        customer.setPhoneNumber("");
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;

        switch (viewType) {
            case CUSTOMER_ITEM:
                viewHolder = new CustomerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item, parent, false));
                break;
            case CUSTOMER_TYPE_ITEM:
                viewHolder = new CustomerTypeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_type_item, parent, false));
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        StringBuilder str = null;

        switch (holder.getItemViewType()) {
            case CUSTOMER_ITEM:
                CustomerViewHolder customerHolder = (CustomerViewHolder) holder;

                str = new StringBuilder();
                Customer customer = getItem(position - 1).getCustomer();
                List<CustomerGroup> customerGroups = getItem(position - 1).getCustomerGroups();

                customerHolder.tvClientId.setText(customer.getClientId().toString());
                customerHolder.etFullName.setText(customer.getName());
                customerHolder.etAddress.setText(customer.getAddress());
                customerHolder.etPhone.setText(customer.getPhoneNumber());
                customerHolder.tvQrCode.setText(customer.getQrCode());

                if (customerGroups.isEmpty()) {
                    customerHolder.tvCustomerGroup.setText(R.string.not_selected);
                } else {
                    for (int i = 0; i < customerGroups.size(); i++) {
                        str.append(customerGroups.get(i).getName());

                        if (i < (customerGroups.size() - 1)) {
                            str.append(", ");
                        }
                    }

                    customerHolder.tvCustomerGroup.setText(str.toString());
                }

                if (editedItems.contains(new Integer(position))) {
                    customerHolder.btnSave.enable();
                } else {
                    customerHolder.btnSave.disable();
                }
                break;
            case CUSTOMER_TYPE_ITEM:
                str = new StringBuilder();
                CustomerTypeViewHolder customerTypeHolder = (CustomerTypeViewHolder) holder;

                customerTypeHolder.tvQrCode.setText(this.customer.getQrCode());
                customerTypeHolder.tvId.setText(this.customer.getClientId().toString());
                customerTypeHolder.etFullName.setText(this.customer.getName().toString());
                customerTypeHolder.etAddress.setText(this.customer.getAddress().toString());
                customerTypeHolder.etPhone.setText(this.customer.getPhoneNumber().toString());

                if (customerTypeSelectedCustomerGroups.isEmpty()) {
                    customerTypeHolder.tvCustomerGroup.setText(R.string.not_selected);
                } else {
                    for (int i = 0; i < customerTypeSelectedCustomerGroups.size(); i++) {
                        str.append(customerTypeSelectedCustomerGroups.get(i).getName());

                        if (i < (customerTypeSelectedCustomerGroups.size() - 1)) {
                            str.append(", ");
                        }
                    }

                    customerTypeHolder.tvCustomerGroup.setText(str.toString());
                }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? CUSTOMER_TYPE_ITEM : CUSTOMER_ITEM;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    public void refreshCustomerTypeQrCode(String qrCode) {
        customer.setQrCode(qrCode);
        notifyItemChanged(0);
    }

    public void updateClientId(String clientId) {
        customer.setClientId(Long.parseLong(clientId));
        notifyItemChanged(0);
    }

    public void setCustomersList(List<CustomersWithCustomerGroups> customersWithCustomerGroups) {
        items = customersWithCustomerGroups;
        notifyDataSetChanged();
    }

    class CustomerViewHolder extends BaseViewHolder {
        @BindView(R.id.tvClientId)
        TextView tvClientId;
        @NotEmpty(messageId = R.string.enter_full_name)
        @BindView(R.id.etFullName)
        MpEditText etFullName;
        @NotEmpty(messageId = R.string.enter_phone)
        @BindView(R.id.etPhone)
        MpEditText etPhone;
        @NotEmpty(messageId = R.string.enter_address)
        @BindView(R.id.etAddress)
        MpEditText etAddress;
        @BindView(R.id.tvQrCode)
        TextView tvQrCode;
        @BindView(R.id.ivRefreshQrCode)
        ImageView ivRefreshQrCode;
        @BindView(R.id.tvCustomerGroup)
        TextView tvCustomerGroup;
        @BindView(R.id.btnSave)
        MpMiniActionButton btnSave;
        @BindView(R.id.btnRemove)
        MpMiniActionButton btnRemove;

        public CustomerViewHolder(View itemView) {
            super(itemView);

            RxView.clicks(ivRefreshQrCode).subscribe(aVoid -> {
                onClickListener.refreshQrCode(getAdapterPosition() - 1);
            });

            textWatcherInit(etFullName);
            textWatcherInit(etPhone);
            textWatcherInit(etAddress);

            RxView.clicks(btnSave).subscribe(aVoid -> {
                UIUtils.closeKeyboard(etFullName, context);

                if (FormValidator.validate(context, this, new MultipleCallback())) {
                    if (items.get(getAdapterPosition() - 1).getCustomer().getName().equals(etFullName.getText().toString()) || !onClickListener.isCustomerExists(etFullName.getText().toString())) {
                        String fullName = etFullName.getText().toString();
                        String phone = etPhone.getText().toString();
                        String address = etAddress.getText().toString();
                        String qrCode = tvQrCode.getText().toString();

                        onClickListener.saveCustomer(getAdapterPosition() - 1, fullName, phone, address, qrCode);

                        btnSave.disable();
                        editedItems.remove(new Integer(getAdapterPosition() - 1));
                    } else {
                        etFullName.setError(context.getString(R.string.customer_name_exists));
                    }
                }
            });

            RxView.clicks(btnRemove).subscribe(aVoid -> {
                WarningDialog warningDialog = new WarningDialog(context);
                warningDialog.setWarningText(context.getString(R.string.deleting_customer_message, etFullName.getText().toString()));
                warningDialog.setOnYesClickListener(v -> {
                    onClickListener.removeCustomer(getAdapterPosition() - 1);

                    warningDialog.dismiss();
                });
                warningDialog.setOnNoClickListener(v -> {
                    warningDialog.dismiss();
                });

                warningDialog.show();
            });

            RxView.clicks(tvCustomerGroup).subscribe(o -> {
                onClickListener.showCustomerCustomerGroupsDialog(getAdapterPosition());
            });
        }

        private void textWatcherInit(EditText editText) {
            editText.addTextChangedListener(new TextWatcher() {
                private String before;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    before = s.toString();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String content;
                    Customer customer = getItem(getAdapterPosition() - 1).getCustomer();

                    switch (editText.getId()) {
                        case R.id.etFullName:
                            content = customer.getName();
                            break;
                        case R.id.etPhone:
                            content = customer.getPhoneNumber();
                            break;
                        case R.id.etAddress:
                            content = customer.getAddress();
                            break;
                        default:
                            content = "";
                    }

                    if (!before.isEmpty() && !before.equals(s.toString()) && before.equals(content)) {
                        editedItems.add(new Integer(getAdapterPosition() - 1));
                        btnSave.enable();
                    }
                }
            });
        }
    }

    class CustomerTypeViewHolder extends BaseViewHolder {
        @BindView(R.id.tvId)
        TextView tvId;
        @NotEmpty(messageId = R.string.enter_full_name)
        @BindView(R.id.etFullName)
        MpEditText etFullName;
        @NotEmpty(messageId = R.string.enter_phone)
        @BindView(R.id.etPhone)
        MpEditText etPhone;
        @NotEmpty(messageId = R.string.enter_address)
        @BindView(R.id.etAddress)
        MpEditText etAddress;
        @BindView(R.id.tvQrCode)
        TextView tvQrCode;
        @BindView(R.id.ivRefreshQrCode)
        ImageView ivRefreshQrCode;
        @BindView(R.id.tvCustomerGroup)
        TextView tvCustomerGroup;
        @BindView(R.id.btnAdd)
        MpActionButton btnAdd;

        public CustomerTypeViewHolder(View itemView) {
            super(itemView);

            RxView.clicks(btnAdd).subscribe(aVoid -> {
                if (FormValidator.validate(context, this, new MultipleCallback())) {
                    UIUtils.closeKeyboard(etFullName, context);
                    if (onClickListener.isCustomerExists(etFullName.getText().toString())) {
                        etFullName.setError(context.getString(R.string.customer_name_exists));
                    } else {
                        onClickListener.addCustomer(tvId.getText().toString(),
                                etFullName.getText().toString(),
                                etPhone.getText().toString(),
                                etAddress.getText().toString(),
                                tvQrCode.getText().toString(), customerTypeSelectedCustomerGroups);

                        customer.setName("");
                        customer.setPhoneNumber("");
                        customer.setAddress("");
                        etFullName.setText("");
                        etPhone.setText("");
                        etAddress.setText("");
                        etFullName.requestFocus();

                        onClickListener.updateClientId();
                        onClickListener.refreshCustomerItemQrCode();
                    }
                }
            });

            RxView.clicks(ivRefreshQrCode).subscribe(aVoid -> {
                onClickListener.refreshCustomerItemQrCode();
                customer.setName(etFullName.getText().toString());
                customer.setAddress(etAddress.getText().toString());
                customer.setPhoneNumber(etPhone.getText().toString());
            });

            RxView.clicks(tvCustomerGroup).subscribe(o -> {
                onClickListener.showCustomerCustomerGroupsDialog(getAdapterPosition());
                customer.setName(etFullName.getText().toString());
                customer.setAddress(etAddress.getText().toString());
                customer.setPhoneNumber(etPhone.getText().toString());
            });
        }
    }
}
