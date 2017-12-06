package com.jim.multipos.ui.vendor_products_view.fragments;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButtonWithIcon;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.vendor.add_edit.VendorAddEditActivity;
import com.jim.multipos.ui.vendor_products_view.VendorProductsViewActivity;

import butterknife.BindView;

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

    @Override
    protected int getLayout() {
        return R.layout.vendor_details_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Vendor vendor = ((VendorProductsViewActivity) getActivity()).getPresenter().getVendor();
        tvCompanyName.setPaintFlags(tvCompanyName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tvCompanyName.setText(vendor.getName());
        tvFullName.setText(vendor.getContactName());
        tvAddress.setText(vendor.getAddress());

        RxView.clicks(ivEditVendor).subscribe(o -> {
            ((VendorProductsViewActivity) getActivity()).getPresenter().openVendorEditing();
        });

        for (int i = 0; i < vendor.getContacts().size(); i++) {
            if (vendor.getContacts().get(i).getType() == Contact.PHONE) {
                String phone = vendor.getContacts().get(i).getName();
                View view = LayoutInflater.from(getContext()).inflate(R.layout.phone_view, null);
                ((TextView) view.findViewById(R.id.tvPhone)).setText(phone);
                llPhones.addView(view);
            } else {
                String email = vendor.getContacts().get(i).getName();
                View view = LayoutInflater.from(getContext()).inflate(R.layout.email_view, null);
                ((TextView) view.findViewById(R.id.tvEmail)).setText(email);
                llEmails.addView(view);
            }
        }

        btnPay.setOnClickListener(view -> {

        });
        btnPayments.setOnClickListener(view -> {

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
}
