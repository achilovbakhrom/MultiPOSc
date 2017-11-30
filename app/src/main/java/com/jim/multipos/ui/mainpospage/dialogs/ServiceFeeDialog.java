package com.jim.multipos.ui.mainpospage.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.ui.mainpospage.adapter.ServiceFeeAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Portable-Acer on 11.11.2017.
 */

public class ServiceFeeDialog extends DialogFragment implements ServiceFeeAdapter.OnClickListener {
    public interface OnDialogListener {
        List<ServiceFee> getServiceFees();
        void addServiceFee(double amount, String description, String amountType);
    }

    @BindView(R.id.tvCaption)
    TextView tvCaption;
    @BindView(R.id.rvServiceFees)
    RecyclerView recyclerView;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    @BindView(R.id.btnAdd)
    MpButton btnAdd;
    private Unbinder unbinder;
    private ServiceFeeAdapter adapter;
    private List<ServiceFee> serviceFees;
    private OnDialogListener listener;
    private String caption;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.service_fee_dialog, container, false);
        getDialog().getWindow().getDecorView().setBackgroundResource(R.color.colorTransparent);

        unbinder = ButterKnife.bind(this, view);

        if (caption != null) {
            tvCaption.setText(caption);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ServiceFeeAdapter(getContext(), this, serviceFees);
        recyclerView.setAdapter(adapter);

        RxView.clicks(btnBack).subscribe(o -> dismiss());

        RxView.clicks(btnAdd).subscribe(o -> {
            AddServiceFeeDialog dialog = new AddServiceFeeDialog();

            dialog.setOnServiceFeeDialogListener(new AddServiceFeeDialog.OnServiceFeeDialogListener() {
                @Override
                public void dismiss() {
                    adapter.setItems(listener.getServiceFees());
                }

                @Override
                public void addServiceFee(double amount, String description, String amountType) {
                    listener.addServiceFee(amount, description, amountType);
                }
            });
            dialog.show(getActivity().getSupportFragmentManager(), "AddServiceFeeDialog");
        });

        return view;
    }

    public void setServiceFee(List<ServiceFee> serviceFees) {
        this.serviceFees = serviceFees;
    }

    public void setOnDialogListener(OnDialogListener listener) {
        this.listener = listener;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();

        super.onDestroyView();
    }

    @Override
    public void onItemClicked(ServiceFee serviceFee) {
        //TODO
        dismiss();
    }
}
