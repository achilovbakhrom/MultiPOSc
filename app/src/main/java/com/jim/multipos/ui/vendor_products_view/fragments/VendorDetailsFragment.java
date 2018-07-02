package com.jim.multipos.ui.vendor_products_view.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Contact;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.billing_vendor.BillingOperationsActivity;
import com.jim.multipos.ui.consignment.ConsignmentActivity;
import com.jim.multipos.ui.consignment_list.ConsignmentListActivity;
import com.jim.multipos.ui.inventory.InventoryActivity;
import com.jim.multipos.ui.vendor.add_edit.fragment.VendorAddEditFragment;
import com.jim.multipos.ui.vendor_products_view.VendorProductsViewActivity;
import com.jim.multipos.ui.vendor_products_view.presenter.VendorDetialsPresenter;
import com.jim.multipos.utils.BundleConstants;
import com.jim.multipos.utils.GlideApp;
import com.jim.multipos.utils.PaymentToVendorDialog;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.inventory_events.BillingOperationEvent;
import com.jim.multipos.utils.rxevents.inventory_events.ConsignmentWithVendorEvent;
import com.jim.multipos.utils.rxevents.inventory_events.InventoryStateEvent;
import com.jim.multipos.utils.rxevents.inventory_events.VendorEvent;
import com.jim.multipos.utils.rxevents.main_order_events.ConsignmentEvent;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.Binds;
import io.reactivex.disposables.Disposable;

import static com.jim.multipos.ui.consignment.ConsignmentActivity.OPERATION_TYPE;
import static com.jim.multipos.ui.consignment.ConsignmentActivity.PRODUCT_ID;
import static com.jim.multipos.ui.consignment.ConsignmentActivity.VENDOR_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorDetailsFragment extends BaseFragment implements VendorDetialsView {
    @Inject
    VendorDetialsPresenter presenter;
    @Inject
    DecimalFormat decimalFormat;
    @Inject
    DatabaseManager databaseManager;
    @BindView(R.id.tvCompanyName)
    TextView tvCompanyName;
    @BindView(R.id.tvFullName)
    TextView tvFullName;
    @BindView(R.id.tvPhoneNumbers)
    TextView tvPhoneNumbers;
    @BindView(R.id.tvEmails)
    TextView tvEmails;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.btnPay)
    MpButtonWithIcon btnPay;
    @BindView(R.id.btnPayments)
    MpButtonWithIcon btnPayments;
    @BindView(R.id.btnIncome)
    MpButtonWithIcon btnIncome;
    @BindView(R.id.btnPosition)
    MpButtonWithIcon btnPosition;
    @BindView(R.id.btnReturn)
    MpButtonWithIcon btnReturn;
    @BindView(R.id.btnStory)
    MpButtonWithIcon btnStory;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.tvVendorDebt)
    TextView tvVendorDebt;
    @BindView(R.id.tvDebt)
    TextView tvDebt;
    @BindView(R.id.tvAddressTitle)
    TextView tvAddressTitle;
    @Inject
    RxBus rxBus;
    private ArrayList<Disposable> subscriptions;


    @Override
    protected int getLayout() {
        return R.layout.vendor_details_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.onCreateView(getArguments());
    }


    @Override
    public void updateView(Vendor vendor, double debt) {
        String maincurrencyAbr = databaseManager.getMainCurrency().getAbbr();
        SpannableString content = new SpannableString(vendor.getName());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tvCompanyName.setText(content);
        tvFullName.setText(vendor.getContactName());
        if(vendor.getAddress()!=null && !vendor.getAddress().isEmpty())
            tvAddress.setText(vendor.getAddress());
        else tvAddressTitle.setVisibility(View.GONE);

        tvVendorDebt.setText(decimalFormat.format(debt)+" "+maincurrencyAbr);

        String phones = "";
        String email = "";
        for (Contact contact:vendor.getContacts()) {
            if(contact.getType() == Contact.PHONE){
                if(!phones.isEmpty())
                    phones += ", ";

                phones += contact.getName();
            }else if(contact.getType() == Contact.E_MAIL){
                if(!email.isEmpty())
                    email += ", ";

                email += contact.getName();
            }
        }
        tvPhoneNumbers.setText(phones);
        tvEmails.setText(email);


        if (vendor.getPhotoPath() != null && !vendor.getPhotoPath().equals("")) {
            GlideApp
                    .with(VendorDetailsFragment.this)
                    .load(Uri.fromFile(new File(vendor.getPhotoPath())))
                    .error(R.drawable.ic_company)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .thumbnail(0.2f)
                    .centerCrop()
                    .transform(new RoundedCorners(20))
                    .into(ivPhoto);
        } else {
            ivPhoto.setImageResource(R.drawable.ic_company);
        }

        btnPay.setOnClickListener(view -> {
            PaymentToVendorDialog paymentToVendorDialog = new PaymentToVendorDialog(getContext(), vendor, new PaymentToVendorDialog.PaymentToVendorCallback() {
                @Override
                public void onChanged() {
                    presenter.updateMyDebtState();
                }

                @Override
                public void onCancel() {

                }
            }, databaseManager, null);
            paymentToVendorDialog.show();
        });
        btnPayments.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), BillingOperationsActivity.class);
            intent.putExtra(BillingOperationsActivity.VENDOR_EXTRA_ID, vendor.getId());
            startActivity(intent);
        });
        btnIncome.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ConsignmentActivity.class);
            intent.putExtra(VENDOR_ID, vendor.getId());
            intent.putExtra(OPERATION_TYPE, BundleConstants.INVOICE);
            startActivity(intent);
        });
        btnPosition.setOnClickListener(view -> {

        });
        btnStory.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ConsignmentListActivity.class);
            intent.putExtra(InventoryActivity.VENDOR_ID, vendor.getId());
            startActivity(intent);
        });
        btnReturn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ConsignmentActivity.class);
            intent.putExtra(VENDOR_ID, vendor.getId());
            intent.putExtra(OPERATION_TYPE, BundleConstants.OUTVOICE);
            startActivity(intent);
        });
    }

    @Override
    protected void rxConnections() {
        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBus.toObservable().subscribe(o -> {
                    if (o instanceof ConsignmentWithVendorEvent) {
                        ConsignmentWithVendorEvent event = (ConsignmentWithVendorEvent) o;
                        switch (event.getType()) {
                            case GlobalEventConstants.UPDATE: {
                                presenter.updateVendorDetials(event.getVendor());
                                break;
                            }
                        }
                    }
                }));
    }


    @Override
    public void updateDebt(double debt) {
        String maincurrencyAbr = databaseManager.getMainCurrency().getAbbr();
        tvVendorDebt.setText(decimalFormat.format(debt)+" "+maincurrencyAbr);
    }

    @Override
    public void onDetach() {
        RxBus.removeListners(subscriptions);
        super.onDetach();
    }
}
