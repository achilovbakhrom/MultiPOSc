package com.jim.multipos.ui.customers_edit.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.customer.CustomerGroup;
import com.jim.multipos.ui.customer_group.CustomerGroupActivity;
import com.jim.multipos.ui.customers_edit.adapters.CustomerGroupAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

/**
 * Created by user on 13.09.17.
 */

public class CustomerGroupDialog extends Dialog {
    public interface OnClickListener {
        void onClick(List<CustomerGroup> selectedItems);
    }

    @BindView(R.id.rvCustomerGroups)
    RecyclerView rvCustomerGroups;
    @BindView(R.id.btnSave)
    MpButton btnSave;
    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.tvAdd)
    TextView tvAdd;
    private List<CustomerGroup> customerGroups;
    private List<CustomerGroup> selectedCustomerGroups;
    private OnClickListener onClickListener;
    private Context context;
    private CustomerGroupAdapter adapter;

    public CustomerGroupDialog(@NonNull Context context, List<CustomerGroup> customerGroups, List<CustomerGroup> selectedCustomerGroups, OnClickListener onClickListener) {
        super(context);
        this.context = context;
        this.customerGroups = customerGroups;
        this.selectedCustomerGroups = selectedCustomerGroups;
        this.onClickListener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.customer_dialog);

        View view = getWindow().getDecorView();
        view.setBackgroundResource(R.color.colorTransparent);

        ButterKnife.bind(this);

        rvCustomerGroups.setLayoutManager(new LinearLayoutManager(context));
        adapter = new CustomerGroupAdapter(context, customerGroups, selectedCustomerGroups);
        rvCustomerGroups.setAdapter(adapter);

        RxView.clicks(btnCancel).subscribe(o -> {
            dismiss();
        });

        RxView.clicks(btnSave).subscribe(o -> {
            selectedCustomerGroups.clear();
            selectedCustomerGroups.addAll(adapter.getSelectedItems());
            onClickListener.onClick(selectedCustomerGroups);

            dismiss();
        });

        RxView.clicks(tvAdd).subscribe(o -> {
            context.startActivity(new Intent(context, CustomerGroupActivity.class));
        });
    }
}
