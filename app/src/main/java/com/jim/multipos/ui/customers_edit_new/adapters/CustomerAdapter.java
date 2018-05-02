package com.jim.multipos.ui.customers_edit_new.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.telephony.PhoneNumberFormattingTextWatcher;
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
import com.jim.multipos.utils.BarcodeStack;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.validator.MultipleCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class CustomerAdapter extends BaseAdapter<Customer, BaseViewHolder> {
    public static final int CUSTOMER_ADD_ITEM = 0;
    public static final int CUSTOMER_ITEM = 1;

    public void setBarcode(String contents) {
        addCustomer.setQrCode(contents);
        notifyDataSetChanged();
    }

    public void setBarcode(String contents, int pos) {
        getItem(pos).setQrCode(contents);
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onAddClicked(String clientId, String fullName, String phone, String address, String qrCode, List<CustomerGroup> customerGroups);

        void onSaveClicked(Customer customer);

        void onDeleteClicked(Customer customer);

        void onSortByClientId();

        void onSortByFullName();

        void onSortByAddress();

        void onSortByQrCode();

        void onSortByDefault();

        Long getQrCode();

        Long getClientId();

        boolean isCustomerExists(String name);

        void showItemCustomerGroupsDialog(int position);

        void showAddCustomerGroupDialog(Customer customer);

        void scanBarcode();

        void scanBarcode(int position);
    }

    private OnClickListener listener;
    private BarcodeStack barcodeStack;
    private Context context;
    private Customer addCustomer;
    private Set<Customer> notSavedItems;

    public CustomerAdapter(Context context, List<Customer> customers, OnClickListener listener, BarcodeStack barcodeStack) {
        super(customers);
        this.context = context;
        this.listener = listener;
        this.barcodeStack = barcodeStack;
        addCustomer = new Customer();
        addCustomer.setCustomerGroups(new ArrayList<>());
        notSavedItems = new HashSet<>();
        barcodeStack.register(barcode1 -> {
            addCustomer.setQrCode(barcode1);
            notifyItemChanged(0);
        });
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = null;
        switch (viewType) {
            case CUSTOMER_ADD_ITEM:
                baseViewHolder = new CustomerAddViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_type_item, parent, false));
                break;
            case CUSTOMER_ITEM:
                baseViewHolder = new CustomerItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item, parent, false));
                break;
        }

        return baseViewHolder;
    }
    String barcode = "";
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (position == 0) {
            ((CustomerAddViewHolder) holder).tvId.setText(String.valueOf(addCustomer.getClientId()));
            ((CustomerAddViewHolder) holder).etFullName.setText(addCustomer.getName());
            ((CustomerAddViewHolder) holder).etPhone.setText(addCustomer.getPhoneNumber());
            ((CustomerAddViewHolder) holder).etAddress.setText(addCustomer.getAddress());
            ((CustomerAddViewHolder) holder).tvQrCodeInAdd.setText(addCustomer.getQrCode());

            ((CustomerAddViewHolder) holder).etPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

            if (addCustomer.getCustomerGroups().isEmpty()) {
                ((CustomerAddViewHolder) holder).tvCustomerGroup.setText(context.getString(R.string.not_selected));
            } else {
                ((CustomerAddViewHolder) holder).tvCustomerGroup.setText("");
                for (int i = 0; i < addCustomer.getCustomerGroups().size(); i++) {
                    ((CustomerAddViewHolder) holder).tvCustomerGroup.append(addCustomer.getCustomerGroups().get(i).getName());

                    if (i != (addCustomer.getCustomerGroups().size() - 1)) {
                        ((CustomerAddViewHolder) holder).tvCustomerGroup.append(", ");
                    }
                }
            }
        } else {
            ((CustomerItemViewHolder) holder).tvClientId.setText(getItem(position - 1).getClientId().toString());
            ((CustomerItemViewHolder) holder).etFullName.setText(getItem(position - 1).getName());
            ((CustomerItemViewHolder) holder).etPhone.setText(getItem(position - 1).getPhoneNumber());
            ((CustomerItemViewHolder) holder).etAddress.setText(getItem(position - 1).getAddress());
            ((CustomerItemViewHolder) holder).tvQrCode.setText(getItem(position - 1).getQrCode());
            ((CustomerItemViewHolder) holder).etPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

            if (notSavedItems.contains(getItem(position - 1))) {
                ((CustomerItemViewHolder) holder).btnSave.enable();
            } else {
                ((CustomerItemViewHolder) holder).btnSave.disable();
            }
            if (getItem(position - 1).getCustomerGroups().isEmpty()) {
                ((CustomerItemViewHolder) holder).tvCustomerGroup.setText(context.getString(R.string.not_selected));
            } else {
                ((CustomerItemViewHolder) holder).tvCustomerGroup.setText("");
                for (int i = 0; i < getItem(position - 1).getCustomerGroups().size(); i++) {
                    ((CustomerItemViewHolder) holder).tvCustomerGroup.append(getItem(position - 1).getCustomerGroups().get(i).getName());

                    if (i != (getItem(position - 1).getCustomerGroups().size() - 1)) {
                        ((CustomerItemViewHolder) holder).tvCustomerGroup.append(", ");
                    }
                }
            }
        }
    }

    public Customer getCustomer() {
        return addCustomer;
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

    @Override
    public void addItem(Customer item) {
        items.add(0, item);
        notifyItemInserted(1);
    }

    @Override
    public void removeItem(Customer item) {
        int index = items.indexOf(item);
        items.remove(item);
        notifyItemRemoved(index + 1);
    }

    public void updateItem(Customer customer, boolean isModified) {
        if (isModified && !customer.equals(addCustomer)) {
            notSavedItems.add(customer);
        }

        notifyItemChanged(items.indexOf(customer) + 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return CUSTOMER_ADD_ITEM;
        }

        return CUSTOMER_ITEM;
    }

    public int getNotSavedItemCount() {
        return notSavedItems.size();
    }

    class CustomerAddViewHolder extends BaseViewHolder {
        @BindView(R.id.tvClientId)
        TextView tvClientId;
        @BindView(R.id.tvFullName)
        TextView tvFullName;
        @BindView(R.id.tvPhone)
        TextView tvPhone;
        @BindView(R.id.tvAddress)
        TextView tvAddress;
        @BindView(R.id.tvQrCode)
        TextView tvQrCode;
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
        @BindView(R.id.tvQrCodeInAdd)
        TextView tvQrCodeInAdd;
        @BindView(R.id.ivRefreshQrCode)
        ImageView ivRefreshQrCode;
        @BindView(R.id.tvCustomerGroup)
        TextView tvCustomerGroup;
        @BindView(R.id.btnAdd)
        MpActionButton btnAdd;
        boolean isSortedByClientId = false;
        boolean isSortedByFullName = false;
        boolean isSortedByPhone = false;
        boolean isSortedByAddress = false;
        boolean isSortedByQrCode = false;

        public CustomerAddViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            addCustomer.setClientId(listener.getClientId());
            addCustomer.setQrCode("");

            RxView.clicks(btnAdd).subscribe(o -> {
                UIUtils.closeKeyboard(btnAdd, context);
                if (FormValidator.validate(context, this, new MultipleCallback())) {
                    if (listener.isCustomerExists(etFullName.getText().toString())) {
                        etFullName.setError(context.getString(R.string.customer_name_exists));
                    } else {
                        listener.onAddClicked(tvId.getText().toString(),
                                etFullName.getText().toString(),
                                etPhone.getText().toString(),
                                etAddress.getText().toString(),
                                tvQrCodeInAdd.getText().toString(),
                                addCustomer.getCustomerGroups());

                        addCustomer.setName("");
                        addCustomer.setPhoneNumber("");
                        addCustomer.setAddress("");
                        addCustomer.setClientId(listener.getClientId());
                        addCustomer.setQrCode("");
                        addCustomer.getCustomerGroups().clear();
                        tvId.setText(addCustomer.getClientId().toString());
                        etFullName.setText("");
                        etPhone.setText("");
                        etAddress.setText("");
                        tvCustomerGroup.setText(context.getString(R.string.not_selected));
                        tvQrCodeInAdd.setText(addCustomer.getQrCode());
                        etFullName.requestFocus();
                    }
                }
            });

            RxView.clicks(tvClientId).subscribe(o -> {
                UIUtils.closeKeyboard(tvClientId, context);
                if (isSortedByClientId) {
                    isSortedByClientId = false;
                    tvClientId.setTypeface(Typeface.create(tvClientId.getTypeface(), Typeface.NORMAL));
                    listener.onSortByDefault();
                } else {
                    isSortedByClientId = true;
                    tvClientId.setTypeface(Typeface.create(tvClientId.getTypeface(), Typeface.BOLD));
                    listener.onSortByClientId();
                }

                isSortedByFullName = false;
                isSortedByPhone = false;
                isSortedByAddress = false;
                isSortedByQrCode = false;

                tvFullName.setTypeface(Typeface.create(tvFullName.getTypeface(), Typeface.NORMAL));
                tvPhone.setTypeface(Typeface.create(tvPhone.getTypeface(), Typeface.NORMAL));
                tvAddress.setTypeface(Typeface.create(tvAddress.getTypeface(), Typeface.NORMAL));
                tvQrCode.setTypeface(Typeface.create(tvQrCode.getTypeface(), Typeface.NORMAL));
            });

            RxView.clicks(tvFullName).subscribe(o -> {
                UIUtils.closeKeyboard(tvFullName, context);
                if (isSortedByFullName) {
                    isSortedByFullName = false;
                    tvFullName.setTypeface(Typeface.create(tvFullName.getTypeface(), Typeface.NORMAL));
                    listener.onSortByDefault();
                } else {
                    isSortedByFullName = true;
                    tvFullName.setTypeface(Typeface.create(tvFullName.getTypeface(), Typeface.BOLD));
                    listener.onSortByFullName();
                }

                isSortedByClientId = false;
                isSortedByPhone = false;
                isSortedByAddress = false;
                isSortedByQrCode = false;

                tvClientId.setTypeface(Typeface.create(tvClientId.getTypeface(), Typeface.NORMAL));
                tvPhone.setTypeface(Typeface.create(tvPhone.getTypeface(), Typeface.NORMAL));
                tvAddress.setTypeface(Typeface.create(tvAddress.getTypeface(), Typeface.NORMAL));
                tvQrCode.setTypeface(Typeface.create(tvQrCode.getTypeface(), Typeface.NORMAL));
            });

            RxView.clicks(tvAddress).subscribe(o -> {
                UIUtils.closeKeyboard(tvAddress, context);
                if (isSortedByAddress) {
                    isSortedByAddress = false;
                    tvAddress.setTypeface(Typeface.create(tvAddress.getTypeface(), Typeface.NORMAL));
                    listener.onSortByDefault();
                } else {
                    isSortedByAddress = true;
                    tvAddress.setTypeface(Typeface.create(tvAddress.getTypeface(), Typeface.BOLD));
                    listener.onSortByAddress();
                }

                isSortedByClientId = false;
                isSortedByFullName = false;
                isSortedByPhone = false;
                isSortedByQrCode = false;

                tvClientId.setTypeface(Typeface.create(tvClientId.getTypeface(), Typeface.NORMAL));
                tvFullName.setTypeface(Typeface.create(tvFullName.getTypeface(), Typeface.NORMAL));
                tvPhone.setTypeface(Typeface.create(tvPhone.getTypeface(), Typeface.NORMAL));
                tvQrCode.setTypeface(Typeface.create(tvQrCode.getTypeface(), Typeface.NORMAL));
            });

            RxView.clicks(tvQrCode).subscribe(o -> {
                UIUtils.closeKeyboard(tvQrCode, context);
                if (isSortedByQrCode) {
                    isSortedByQrCode = false;
                    tvQrCode.setTypeface(Typeface.create(tvQrCode.getTypeface(), Typeface.NORMAL));
                    listener.onSortByDefault();
                } else {
                    isSortedByQrCode = true;
                    tvQrCode.setTypeface(Typeface.create(tvQrCode.getTypeface(), Typeface.BOLD));
                    listener.onSortByQrCode();
                }

                isSortedByClientId = false;
                isSortedByFullName = false;
                isSortedByPhone = false;
                isSortedByQrCode = false;

                tvClientId.setTypeface(Typeface.create(tvClientId.getTypeface(), Typeface.NORMAL));
                tvFullName.setTypeface(Typeface.create(tvFullName.getTypeface(), Typeface.NORMAL));
                tvPhone.setTypeface(Typeface.create(tvPhone.getTypeface(), Typeface.NORMAL));
                tvAddress.setTypeface(Typeface.create(tvAddress.getTypeface(), Typeface.NORMAL));
            });

            RxView.clicks(ivRefreshQrCode).subscribe(o -> {
                listener.scanBarcode();
            });

            RxView.clicks(tvCustomerGroup).subscribe(o -> {
                listener.showAddCustomerGroupDialog(addCustomer);
            });

            initTextWatcher(etFullName);
            initTextWatcher(etPhone);
            initTextWatcher(etAddress);
        }

        private void initTextWatcher(EditText editText) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    switch (editText.getId()) {
                        case R.id.etFullName:
                            if (!etFullName.getText().toString().equals(addCustomer.getName())) {
                                addCustomer.setName(editText.getText().toString());
                            }
                            break;
                        case R.id.etPhone:
                            if (!etPhone.getText().toString().equals(addCustomer.getPhoneNumber())) {
                                addCustomer.setPhoneNumber(editText.getText().toString());
                            }
                            break;
                        case R.id.etAddress:
                            if (!etAddress.getText().toString().equals(addCustomer.getAddress())) {
                                addCustomer.setAddress(editText.getText().toString());
                            }
                            break;
                    }
                }
            });
        }
    }

    class CustomerItemViewHolder extends BaseViewHolder {
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
        MpEditText tvQrCode;
        @BindView(R.id.ivRefreshQrCode)
        ImageView ivRefreshQrCode;
        @BindView(R.id.tvCustomerGroup)
        TextView tvCustomerGroup;
        @BindView(R.id.btnSave)
        MpMiniActionButton btnSave;
        @BindView(R.id.btnRemove)
        MpMiniActionButton btnRemove;

        public CustomerItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            RxView.clicks(btnSave).subscribe(o -> {
                UIUtils.closeKeyboard(btnSave, context);
                if (FormValidator.validate(context, this, new MultipleCallback())) {
                    getItem(getAdapterPosition() - 1).setModifiedDate(System.currentTimeMillis());
                    listener.onSaveClicked(getItem(getAdapterPosition() - 1));
                    notSavedItems.remove(getItem(getAdapterPosition() - 1));
                    btnSave.disable();
                }
            });

            RxView.clicks(btnRemove).subscribe(o -> {
                UIUtils.closeKeyboard(btnRemove, context);
                notSavedItems.remove(getItem(getAdapterPosition() - 1));
                listener.onDeleteClicked(getItem(getAdapterPosition() - 1));
            });

            RxView.clicks(ivRefreshQrCode).subscribe(o -> {
                listener.scanBarcode(getAdapterPosition() - 1);
                notSavedItems.add(getItem(getAdapterPosition() - 1));
                btnSave.enable();
            });

            RxView.clicks(tvCustomerGroup).subscribe(o -> {
                listener.showItemCustomerGroupsDialog(getAdapterPosition() - 1);
            });

            initTextWatcher(etFullName);
            initTextWatcher(etPhone);
            initTextWatcher(etAddress);
            initTextWatcher(tvQrCode);
        }

        private void initTextWatcher(EditText editText) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    switch (editText.getId()) {
                        case R.id.etFullName:
                            if (!etFullName.getText().toString().equals(getItem(getAdapterPosition() - 1).getName())) {
                                getItem(getAdapterPosition() - 1).setName(editText.getText().toString());
                                notSavedItems.add(getItem(getAdapterPosition() - 1));
                                btnSave.enable();
                            }
                            break;
                        case R.id.etPhone:
                            if (!etPhone.getText().toString().equals(getItem(getAdapterPosition() - 1).getPhoneNumber())) {
                                getItem(getAdapterPosition() - 1).setPhoneNumber(editText.getText().toString());
                                notSavedItems.add(getItem(getAdapterPosition() - 1));
                                btnSave.enable();
                            }
                            break;
                        case R.id.etAddress:
                            if (!etAddress.getText().toString().equals(getItem(getAdapterPosition() - 1).getAddress())) {
                                getItem(getAdapterPosition() - 1).setAddress(editText.getText().toString());
                                notSavedItems.add(getItem(getAdapterPosition() - 1));
                                btnSave.enable();
                            }
                            break;
                        case R.id.tvQrCode:
                            if (!tvQrCode.getText().toString().equals(getItem(getAdapterPosition() - 1).getQrCode())) {
                                getItem(getAdapterPosition() - 1).setQrCode(editText.getText().toString());
                                notSavedItems.add(getItem(getAdapterPosition() - 1));
                                btnSave.enable();
                            }
                            break;

                    }
                }
            });
        }
    }
}
