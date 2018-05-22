package com.jim.multipos.ui.vendor_products_view.fragments;


import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButtonWithIcon;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.ui.vendor_products_view.VendorProductsViewActivity;
import com.jim.multipos.utils.GlideApp;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.inventory_events.BillingOperationEvent;
import com.jim.multipos.utils.rxevents.inventory_events.InventoryStateEvent;
import com.jim.multipos.utils.rxevents.inventory_events.VendorEvent;
import com.jim.multipos.utils.rxevents.main_order_events.ConsignmentEvent;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorDetailsFragment extends BaseFragment {
    @BindView(R.id.tvCompanyName)
    TextView tvCompanyName;
    @BindView(R.id.tvFullName)
    TextView tvFullName;
    @BindView(R.id.llPhones)
    LinearLayout llPhones;
    @BindView(R.id.llEmails)
    LinearLayout llEmails;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.ivEditVendor)
    ImageView ivEditVendor;
    @BindView(R.id.btnPay)
    MpButtonWithIcon btnPay;
    @BindView(R.id.btnPayments)
    MpButtonWithIcon btnPayments;
    @BindView(R.id.btnIncome)
    MpButtonWithIcon btnIncome;
    @BindView(R.id.btnReturn)
    MpButtonWithIcon btnReturn;
    @BindView(R.id.btnStory)
    MpButtonWithIcon btnStory;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.tvVendorDebt)
    TextView tvVendorDebt;
    @BindView(R.id.tvPaidSum)
    TextView tvPaidSum;
    @BindView(R.id.tvDebt)
    TextView tvDebt;
    private ArrayList<Disposable> subscriptions;

    @Override
    protected int getLayout() {
        return R.layout.vendor_details_fragment;
    }

    @Override
    protected void rxConnections() {
        RxBus rxBus = ((VendorProductsViewActivity) getContext()).getRxBus();
        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBus.toObservable().subscribe(o -> {
                    if (o instanceof VendorEvent) {
                        VendorEvent event = (VendorEvent) o;
                        switch (event.getType()) {
                            case GlobalEventConstants.UPDATE: {
                                ((VendorProductsViewActivity) getContext()).getPresenter().setVendorId(event.getVendor().getId());
                                ((VendorProductsViewActivity) getContext()).getPresenter().initVendorDetails();
                                break;
                            }
                        }
                    }
                    if (o instanceof InventoryStateEvent){
                        InventoryStateEvent event = (InventoryStateEvent) o;
                        if (event.getType() == GlobalEventConstants.UPDATE) {
                            ((VendorProductsViewActivity) getContext()).getPresenter().updateBillings();
                        }
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.removeListners(subscriptions);
    }

    public void initVendor(String name, String photoPath, String address, String contactName, List<Contact> contacts, Double debt, double paid, String abbr) {
        tvCompanyName.setPaintFlags(tvCompanyName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        GlideApp.with(this)
                .load(photoPath)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .thumbnail(0.2f)
                .centerCrop()
                .transform(new RoundedCorners(20))
                .placeholder(R.drawable.camera)
                .error(R.drawable.camera)
                .into(ivPhoto);
        tvCompanyName.setText(name);
        tvFullName.setText(contactName);
        tvAddress.setText(address);
        llEmails.removeAllViews();
        llPhones.removeAllViews();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getType() == Contact.PHONE) {
                String phone = contacts.get(i).getName();
                View view = LayoutInflater.from(getContext()).inflate(R.layout.phone_view, null);
                ((TextView) view.findViewById(R.id.tvPhone)).setText(phone);
                llPhones.addView(view);
            } else {
                String email = contacts.get(i).getName();
                View view = LayoutInflater.from(getContext()).inflate(R.layout.email_view, null);
                ((TextView) view.findViewById(R.id.tvEmail)).setText(email);
                llEmails.addView(view);
            }
        }
        if (debt <= 0) {
            tvDebt.setText(getContext().getString(R.string.debt));
            tvVendorDebt.setTextColor(Color.parseColor("#df4f4f"));
        } else {
            tvDebt.setText(getContext().getString(R.string.overpaid));
            tvVendorDebt.setTextColor(Color.parseColor("#36a614"));
        }
        DecimalFormat decimalFormat = ((VendorProductsViewActivity) getContext()).getDecimalFormat();
        tvVendorDebt.setText(decimalFormat.format(debt) + " " + abbr);
        tvPaidSum.setText(decimalFormat.format(paid) + " " + abbr);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ((VendorProductsViewActivity) getContext()).getPresenter().initVendorDetails();

        RxView.clicks(ivEditVendor).subscribe(o -> {
            ((VendorProductsViewActivity) getActivity()).getPresenter().openVendorEditing();
        });

        btnPay.setOnClickListener(view -> {
            ((VendorProductsViewActivity) getActivity()).getPresenter().openPayDialog();
        });
        btnPayments.setOnClickListener(view -> {
            ((VendorProductsViewActivity) getActivity()).getPresenter().openPaymentsList();
        });
        btnIncome.setOnClickListener(view -> {
            ((VendorProductsViewActivity) getActivity()).getPresenter().openIncomeConsignment();
        });
        btnReturn.setOnClickListener(view -> {
            ((VendorProductsViewActivity) getActivity()).getPresenter().openReturnConsignment();
        });
        btnStory.setOnClickListener(view -> {
            ((VendorProductsViewActivity) getActivity()).getPresenter().openConsignmentList();
        });
    }

    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }

    public void updateBillings(Double debt, double paid, String abbr) {
        if (debt <= 0) {
            tvDebt.setText(getContext().getString(R.string.debt));
            tvVendorDebt.setTextColor(Color.parseColor("#df4f4f"));
        } else {
            tvDebt.setText(getContext().getString(R.string.overpaid));
            tvVendorDebt.setTextColor(Color.parseColor("#36a614"));
        }
        DecimalFormat decimalFormat = ((VendorProductsViewActivity) getContext()).getDecimalFormat();
        tvVendorDebt.setText(decimalFormat.format(debt) + " " + abbr);
        tvPaidSum.setText(decimalFormat.format(paid) + " " + abbr);
        ((VendorProductsViewActivity) getContext()).getRxBus().send(new BillingOperationEvent(GlobalEventConstants.BILLING_IS_DONE));
    }
}
