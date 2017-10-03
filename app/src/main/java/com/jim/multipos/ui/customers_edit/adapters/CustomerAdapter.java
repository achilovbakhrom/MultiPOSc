package com.jim.multipos.ui.customers_edit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextSwitcher;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;
import com.jakewharton.rxbinding2.widget.TextViewEditorActionEvent;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customers_edit.dialogs.CustomerGroupDialog;
import com.jim.multipos.ui.customers_edit.entity.CustomersWithCustomerGroups;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.observers.BlockingFirstObserver;

/**
 * Created by user on 02.09.17.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    public interface OnClick {
        void refreshQrCode(int position);

        void removeCustomer(int position);

        void saveCustomer(int position, String fullName, String phone, String address, String qrCode);

        void showCustomerCustomerGroupsDialog(int position);
    }

    private List<CustomersWithCustomerGroups> customersWithCustomerGroups;
    private OnClick onClickCallback;
    private Context context;
    private List<Integer> editedItems;

    public CustomerAdapter(Context context, List<CustomersWithCustomerGroups> customersWithCustomerGroups, OnClick onClickCallback) {
        this.customersWithCustomerGroups = customersWithCustomerGroups;
        this.onClickCallback = onClickCallback;
        editedItems = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StringBuilder str = new StringBuilder();
        Customer customer = customersWithCustomerGroups.get(position).getCustomer();
        List<CustomerGroup> customerGroups = customersWithCustomerGroups.get(position).getCustomerGroups();

        holder.tvClientId.setText(customer.getClientId().toString());
        holder.etFullName.setText(customer.getName());
        holder.etAddress.setText(customer.getAddress());
        holder.etPhone.setText(customer.getPhoneNumber());
        holder.tvQrCode.setText(customer.getQrCode());

        for (int i = 0; i < customerGroups.size(); i++) {
            str.append(customerGroups.get(i).getName());

            if (i < (customerGroups.size() - 1)) {
                str.append(", ");
            }
        }

        holder.tvCustomerGroup.setText(str.toString());

        if (editedItems.contains(new Integer(position))) {
            holder.ivSave.setVisibility(View.VISIBLE);
        } else {
            holder.ivSave.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return customersWithCustomerGroups.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvClientId)
        TextView tvClientId;
        @BindView(R.id.etFullName)
        MpEditText etFullName;
        @BindView(R.id.etPhone)
        MpEditText etPhone;
        @BindView(R.id.etAddress)
        MpEditText etAddress;
        @BindView(R.id.tvQrCode)
        TextView tvQrCode;
        @BindView(R.id.ivRefreshQrCode)
        ImageView ivRefreshQrCode;
        @BindView(R.id.tvCustomerGroup)
        TextView tvCustomerGroup;
        @BindView(R.id.ivSave)
        ImageView ivSave;
        @BindView(R.id.ivRemove)
        ImageView ivRemove;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            RxView.clicks(ivRefreshQrCode).subscribe(aVoid -> {
                onClickCallback.refreshQrCode(getAdapterPosition());
            });

            textWatcherInit(etFullName);
            textWatcherInit(etPhone);
            textWatcherInit(etAddress);

            RxView.clicks(ivSave).subscribe(aVoid -> {
                String fullName = etFullName.getText().toString();
                String phone = etPhone.getText().toString();
                String address = etAddress.getText().toString();
                String qrCode = tvQrCode.getText().toString();

                onClickCallback.saveCustomer(getAdapterPosition(), fullName, phone, address, qrCode);

                ivSave.setVisibility(View.INVISIBLE);
                editedItems.remove(new Integer(getAdapterPosition()));
            });

            RxView.clicks(ivRemove).subscribe(aVoid -> {
                onClickCallback.removeCustomer(getAdapterPosition());
            });

            RxView.clicks(tvCustomerGroup).subscribe(o -> {
                onClickCallback.showCustomerCustomerGroupsDialog(getAdapterPosition());
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
                    Customer customer = customersWithCustomerGroups.get(getAdapterPosition()).getCustomer();

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
                        editedItems.add(new Integer(getAdapterPosition()));
                        ivSave.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
}
