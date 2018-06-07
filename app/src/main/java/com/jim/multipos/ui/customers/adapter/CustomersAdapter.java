package com.jim.multipos.ui.customers.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.mpviews.MpActionButton;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpMiniActionButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customers.customer.CustomerFragmentPresenterImpl;
import com.jim.multipos.ui.customers.dialog.SelectCustomerGroupDialog;
import com.jim.multipos.ui.customers.model.CustomerAdapterDetails;
import com.jim.multipos.utils.BarcodeStack;
import com.jim.multipos.utils.TextWatcherOnTextChange;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.WarningDialog;
import com.jim.multipos.utils.validator.MultipleCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

public class CustomersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ADD_CUSTOMER_ITEM = 0;
    private static final int CUSTOMER_ITEM = 1;
    private Context context;
    private CustomerAdapterDetails addItem;
    private List<CustomerAdapterDetails> items;
    private List<CustomerGroup> customerGroups;
    private long id;
    private CustomerFragmentPresenterImpl.CustomerSortTypes customerSortTypes = CustomerFragmentPresenterImpl.CustomerSortTypes.Default;
    private OnCustomerCallback listener;


    public CustomersAdapter(Context context) {
        this.context = context;
        addItem = new CustomerAdapterDetails();
    }

    public void setListener(OnCustomerCallback listener) {
        this.listener = listener;
    }

    public void setBarcodeStack(BarcodeStack barcodeStack) {
        barcodeStack.register(barcode1 -> {
            addItem.setNewQRCode(barcode1);
            notifyItemChanged(0);
        });
    }

    public void setData(List<CustomerAdapterDetails> items, List<CustomerGroup> customerGroups, long id) {
        this.items = items;
        this.customerGroups = customerGroups;
        this.id = id;
        Customer customer = new Customer();
        customer.setName("");
        customer.setAddress("");
        customer.setQrCode("");
        customer.setPhoneNumber("");
        customer.setCustomerGroups(new ArrayList<>());
        addItem.setObject(customer);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ADD_CUSTOMER_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_type_item, parent, false);
            return new AddCustomerViewHolder(view);
        } else if (viewType == CUSTOMER_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item, parent, false);
            return new CustomerItemViewHolder(view);
        } else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        if (holder1 instanceof AddCustomerViewHolder) {
            AddCustomerViewHolder holder = (AddCustomerViewHolder) holder1;
            if (items.size() > 1) {
                holder.tvId.setText(String.valueOf(items.get(1).getObject().getClientId() + 1));
            } else holder.tvId.setText(String.valueOf(id));

            if (!addItem.isChanged()) {
                holder.etFullName.setText("");
                holder.etAddress.setText("");
                holder.etPhone.setText("");
                holder.etQrCode.setText("");
                holder.tvCustomerGroup.setText(context.getString(R.string.unselected));
            } else {
                holder.etFullName.setText((addItem.getChangedObject().getName() == null) ? "" : addItem.getChangedObject().getName());
                holder.etAddress.setText((addItem.getChangedObject().getAddress() == null) ? "" : addItem.getChangedObject().getAddress());
                holder.etPhone.setText(addItem.getChangedObject().getPhoneNumber() == null ? "" : addItem.getChangedObject().getPhoneNumber());
                holder.etQrCode.setText(addItem.getChangedObject().getQrCode() == null ? "" : addItem.getChangedObject().getQrCode());
                if (addItem.getChangedObject().getCustomerGroups().size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < addItem.getChangedObject().getCustomerGroups().size(); i++) {
                        if (i == addItem.getChangedObject().getCustomerGroups().size() - 1) {
                            stringBuilder.append(addItem.getChangedObject().getCustomerGroups().get(i).getName());
                        } else
                            stringBuilder.append(addItem.getChangedObject().getCustomerGroups().get(i).getName()).append(", ");
                    }
                    holder.tvCustomerGroup.setText(stringBuilder.toString());
                } else holder.tvCustomerGroup.setText(context.getString(R.string.unselected));
            }

            holder.tvClientId.setTypeface(Typeface.create(holder.tvClientId.getTypeface(), Typeface.NORMAL));
            holder.tvFullName.setTypeface(Typeface.create(holder.tvFullName.getTypeface(), Typeface.NORMAL));
            holder.tvQrCode.setTypeface(Typeface.create(holder.tvQrCode.getTypeface(), Typeface.NORMAL));
            holder.tvAddress.setTypeface(Typeface.create(holder.tvAddress.getTypeface(), Typeface.NORMAL));
            holder.tvPhone.setTypeface(Typeface.create(holder.tvPhone.getTypeface(), Typeface.NORMAL));
            switch (customerSortTypes) {
                case Address:
                    holder.tvAddress.setTypeface(Typeface.create(holder.tvAddress.getTypeface(), Typeface.BOLD));
                    break;
                case Name:
                    holder.tvFullName.setTypeface(Typeface.create(holder.tvFullName.getTypeface(), Typeface.BOLD));
                    break;
                case Id:
                    holder.tvClientId.setTypeface(Typeface.create(holder.tvClientId.getTypeface(), Typeface.BOLD));
                    break;
                case Phone:
                    holder.tvPhone.setTypeface(Typeface.create(holder.tvPhone.getTypeface(), Typeface.BOLD));
                    break;
                case QrCode:
                    holder.tvQrCode.setTypeface(Typeface.create(holder.tvQrCode.getTypeface(), Typeface.BOLD));
                    break;
                case Default:
                    break;
            }

        } else if (holder1 instanceof CustomerItemViewHolder) {
            CustomerItemViewHolder holder = (CustomerItemViewHolder) holder1;
            Customer customer;
            if (items.get(position).isChanged()) {
                customer = items.get(position).getChangedObject();
                holder.btnSave.enable();
            } else {
                customer = items.get(position).getObject();
                holder.btnSave.disable();
            }
            holder.tvClientId.setText(String.valueOf(customer.getClientId()));
            holder.etFullName.setText(customer.getName());
            holder.etAddress.setText(customer.getAddress());
            holder.etPhone.setText(customer.getPhoneNumber());
            holder.etQrCode.setText(customer.getQrCode());
            if (customer.getCustomerGroups().size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < customer.getCustomerGroups().size(); i++) {
                    if (i == customer.getCustomerGroups().size() - 1) {
                        stringBuilder.append(customer.getCustomerGroups().get(i).getName());
                    } else
                        stringBuilder.append(customer.getCustomerGroups().get(i).getName()).append(", ");
                }
                holder.tvCustomerGroup.setText(stringBuilder.toString());
            } else holder.tvCustomerGroup.setText(context.getString(R.string.unselected));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) == null) {
            return ADD_CUSTOMER_ITEM;
        } else return CUSTOMER_ITEM;
    }

    public void setBarcode(String contents) {
        addItem.setNewQRCode(contents);
        notifyItemChanged(0);
    }

    public void setBarcodeToPosition(String contents, int scanPosition) {
        items.get(scanPosition).setNewQRCode(contents);
        notifyItemChanged(scanPosition);
    }

    public class AddCustomerViewHolder extends RecyclerView.ViewHolder {

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
        @BindView(R.id.etQrCode)
        MpEditText etQrCode;
        @BindView(R.id.ivQrCode)
        ImageView ivQrCode;
        @BindView(R.id.tvCustomerGroup)
        TextView tvCustomerGroup;
        @BindView(R.id.btnAdd)
        MpActionButton btnAdd;

        public AddCustomerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvClientId.setOnClickListener(view -> {
                if (customerSortTypes == CustomerFragmentPresenterImpl.CustomerSortTypes.Id) {
                    listener.sortList(customerSortTypes = CustomerFragmentPresenterImpl.CustomerSortTypes.Default);
                } else {
                    listener.sortList(customerSortTypes = CustomerFragmentPresenterImpl.CustomerSortTypes.Id);
                }
            });
            tvFullName.setOnClickListener(view -> {
                if (customerSortTypes == CustomerFragmentPresenterImpl.CustomerSortTypes.Name) {
                    listener.sortList(customerSortTypes = CustomerFragmentPresenterImpl.CustomerSortTypes.Default);
                } else {
                    listener.sortList(customerSortTypes = CustomerFragmentPresenterImpl.CustomerSortTypes.Name);
                }
            });
            tvPhone.setOnClickListener(view -> {
                if (customerSortTypes == CustomerFragmentPresenterImpl.CustomerSortTypes.Phone) {
                    listener.sortList(customerSortTypes = CustomerFragmentPresenterImpl.CustomerSortTypes.Default);
                } else {
                    listener.sortList(customerSortTypes = CustomerFragmentPresenterImpl.CustomerSortTypes.Phone);
                }
            });
            tvAddress.setOnClickListener(view -> {
                if (customerSortTypes == CustomerFragmentPresenterImpl.CustomerSortTypes.Address) {
                    listener.sortList(customerSortTypes = CustomerFragmentPresenterImpl.CustomerSortTypes.Default);
                } else {
                    listener.sortList(customerSortTypes = CustomerFragmentPresenterImpl.CustomerSortTypes.Address);
                }
            });
            tvQrCode.setOnClickListener(view -> {
                if (customerSortTypes == CustomerFragmentPresenterImpl.CustomerSortTypes.QrCode) {
                    listener.sortList(customerSortTypes = CustomerFragmentPresenterImpl.CustomerSortTypes.Default);
                } else {
                    listener.sortList(customerSortTypes = CustomerFragmentPresenterImpl.CustomerSortTypes.QrCode);
                }
            });

            etFullName.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    addItem.setNewName(etFullName.getText().toString());
                }
            });
            etQrCode.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    addItem.setNewQRCode(etQrCode.getText().toString());
                }
            });
            etPhone.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    addItem.setNewPhoneNumber(etPhone.getText().toString());
                }
            });
            etAddress.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    addItem.setNewAddress(etAddress.getText().toString());
                }
            });

            ivQrCode.setOnClickListener(v -> listener.scanBarcode());

            tvCustomerGroup.setOnClickListener(view -> {
                SelectCustomerGroupDialog dialog = new SelectCustomerGroupDialog(context, customerGroups, addItem.getActualCustomerGroup());
                dialog.setListener(groups -> {
                    addItem.setNewGroup(groups);
                    notifyItemChanged(getAdapterPosition());
                });
                dialog.show();
            });

            btnAdd.setOnClickListener(view -> {
                if (FormValidator.validate(context, this, new MultipleCallback())) {
                    listener.onAddPressed(tvId.getText().toString(), addItem.getActualName(), addItem.getActualPhoneNumber(), addItem.getActualAddress(), addItem.getActualQRCode(), addItem.getActualCustomerGroup());
                    addItem.setChangedObject(null);
                    notifyItemChanged(0);
                    etFullName.requestFocus();
                }
            });
        }
    }

    public class CustomerItemViewHolder extends RecyclerView.ViewHolder {

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
        @BindView(R.id.etQrCode)
        MpEditText etQrCode;
        @BindView(R.id.ivQrCode)
        ImageView ivQrCode;
        @BindView(R.id.tvCustomerGroup)
        TextView tvCustomerGroup;
        @BindView(R.id.btnSave)
        MpMiniActionButton btnSave;
        @BindView(R.id.btnRemove)
        MpMiniActionButton btnRemove;

        public CustomerItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            etFullName.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (items.get(getAdapterPosition()).setNewName(etFullName.getText().toString()))
                        btnSave.enable();
                }
            });

            etQrCode.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (items.get(getAdapterPosition()).setNewQRCode(etQrCode.getText().toString()))
                        btnSave.enable();
                }
            });
            etPhone.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (items.get(getAdapterPosition()).setNewPhoneNumber(etPhone.getText().toString()))
                        btnSave.enable();
                }
            });
            etAddress.addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (items.get(getAdapterPosition()).setNewAddress(etAddress.getText().toString()))
                        btnSave.enable();
                }
            });

            ivQrCode.setOnClickListener(v -> {
                listener.scanBarcode(getAdapterPosition());
                btnSave.enable();
            });

            tvCustomerGroup.setOnClickListener(view -> {
                SelectCustomerGroupDialog dialog = new SelectCustomerGroupDialog(context, customerGroups, items.get(getAdapterPosition()).getActualCustomerGroup());
                dialog.setListener(groups -> {
                    items.get(getAdapterPosition()).setNewGroup(groups);
                    notifyItemChanged(getAdapterPosition());
                });
                dialog.show();
                btnSave.enable();
            });

            btnSave.setOnClickListener(view -> {
                if (FormValidator.validate(context, this, new MultipleCallback())) {
                    listener.onSave(items.get(getAdapterPosition()).getActualName(), items.get(getAdapterPosition()).getActualPhoneNumber(), items.get(getAdapterPosition()).getActualAddress(), items.get(getAdapterPosition()).getActualQRCode(), items.get(getAdapterPosition()).getActualCustomerGroup(), items.get(getAdapterPosition()).getObject());
                    UIUtils.closeKeyboard(etFullName, context);
                }
            });

            btnRemove.setOnClickListener(view -> {
                UIUtils.closeKeyboard(etFullName, context);
                Customer customer = items.get(getAdapterPosition()).getObject();
                WarningDialog warningDialog = new WarningDialog(context);
                warningDialog.setWarningMessage(context.getString(R.string.do_you_want_delete));
                warningDialog.setOnNoClickListener(view1 -> {
                    warningDialog.dismiss();
                });
                warningDialog.setOnYesClickListener(view1 -> {
                    listener.onDelete(customer);
                    UIUtils.closeKeyboard(etFullName, context);
                    warningDialog.dismiss();
                });
                warningDialog.show();
            });
        }
    }

    public interface OnCustomerCallback {
        void onAddPressed(String id, String name, String phone, String address, String qrCode, List<CustomerGroup> groups);

        void onSave(String name, String phone, String address, String qrCode, List<CustomerGroup> groups, Customer customer);

        void onDelete(Customer customer);

        void sortList(CustomerFragmentPresenterImpl.CustomerSortTypes customerSortTypes);

        void scanBarcode();

        void scanBarcode(int position);
    }
}
