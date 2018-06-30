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
import com.jim.multipos.ui.vendor_products_view.presenter.VendorDetialsPresenter;
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

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorDetailsFragment extends BaseFragment implements VendorDetialsView {
    @Inject
    VendorDetialsPresenter presenter;

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


    @Override
    protected int getLayout() {
        return R.layout.vendor_details_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.onCreateView(getArguments());
    }


}
