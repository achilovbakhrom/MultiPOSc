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
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.ui.mainpospage.adapter.ServiceFeeAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
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
    private CallbackServiceFeeDialog callbackServiceFeeDialog;
    private double orginalAmount;
    private int serviceFeeApplyType;

    public interface CallbackServiceFeeDialog{
        void choiseStaticServiceFee(ServiceFee serviceFee);
        void choiseManualServiceFee(ServiceFee serviceFee);
    }
    public ServiceFeeDialog(@NonNull Context context, DatabaseManager databaseManager , CallbackServiceFeeDialog callbackServiceFeeDialog, double originalAmount, int serviceFeeApplyType, DecimalFormat formatter) {
        super(context);
        this.databaseManager = databaseManager;
        this.databaseManager = databaseManager;
        this.callbackServiceFeeDialog = callbackServiceFeeDialog;
        this.orginalAmount = originalAmount;
        this.serviceFeeApplyType = serviceFeeApplyType;
        View dialogView = getLayoutInflater().inflate(R.layout.service_fee_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        if (caption != null) {
            tvCaption.setText(caption);
        }
        serviceFees = new ArrayList<>();
        databaseManager.getAllServiceFees().subscribe(serviceFees1 -> {
            for (ServiceFee serviceFee: serviceFees1){
                if ((serviceFee.getApplyingType() == ServiceFee.ALL || serviceFee.getApplyingType() == serviceFeeApplyType) && !serviceFee.getIsManual())
                    serviceFees.add(serviceFee);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ServiceFeeAdapter(getContext(), this, serviceFees);
        recyclerView.setAdapter(adapter);
        RxView.clicks(btnBack).subscribe(o -> dismiss());
        RxView.clicks(btnAdd).subscribe(o -> {
            AddServiceFeeDialog dialog = new AddServiceFeeDialog(getContext(), databaseManager, orginalAmount, serviceFeeApplyType,callbackServiceFeeDialog, formatter);
            dialog.show();
            dismiss();
        });
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public void onItemClicked(ServiceFee serviceFee) {
        callbackServiceFeeDialog.choiseStaticServiceFee(serviceFee);
        dismiss();
    }
}
