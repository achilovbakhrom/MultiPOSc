package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.ui.mainpospage.adapter.ServiceFeeAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Portable-Acer on 11.11.2017.
 */

public class ServiceFeeDialog extends Dialog implements ServiceFeeAdapter.OnClickListener {

    @BindView(R.id.tvCaption)
    TextView tvCaption;
    @BindView(R.id.rvServiceFees)
    RecyclerView recyclerView;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    @BindView(R.id.btnAdd)
    MpButton btnAdd;
    private ServiceFeeAdapter adapter;
    private List<ServiceFee> serviceFees;
    private String caption;
    private DatabaseManager databaseManager;

    public ServiceFeeDialog(@NonNull Context context, DatabaseManager databaseManager) {
        super(context);
        this.databaseManager = databaseManager;
        this.databaseManager = databaseManager;
        View dialogView = getLayoutInflater().inflate(R.layout.service_fee_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        if (caption != null) {
            tvCaption.setText(caption);
        }
        serviceFees = databaseManager.getAllServiceFees().blockingSingle();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ServiceFeeAdapter(getContext(), this, serviceFees);
        recyclerView.setAdapter(adapter);
        RxView.clicks(btnBack).subscribe(o -> dismiss());
        RxView.clicks(btnAdd).subscribe(o -> {
            AddServiceFeeDialog dialog = new AddServiceFeeDialog(getContext(), databaseManager, 30000, ServiceFee.ORDER);
            dialog.show();
            dismiss();
        });
    }

    public void setServiceFee(List<ServiceFee> serviceFees) {
        this.serviceFees = serviceFees;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public void onItemClicked(ServiceFee serviceFee) {
        //TODO
        dismiss();
    }
}
