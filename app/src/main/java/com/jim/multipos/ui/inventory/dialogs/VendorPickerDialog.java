package com.jim.multipos.ui.inventory.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jim.mpviews.MpSearchView;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.inventory.model.VendorPickerItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VendorPickerDialog extends Dialog {

    private List<Vendor> vendorList;
    private List<Vendor> vendorsWithProduct;
    private List<VendorPickerItem> searchresults;
    private List<VendorPickerItem> vendorPickerItems;
    private OnVendorSelectListener listener;
    @BindView(R.id.svVendorSearch)
    MpSearchView svVendorSearch;
    @BindView(R.id.rvVendorList)
    RecyclerView rvVendorList;
    @BindView(R.id.llVendorName)
    LinearLayout llVendorName;
    @BindView(R.id.llContact)
    LinearLayout llContact;
    @BindView(R.id.llSupplyStatus)
    LinearLayout llSupplyStatus;
    @BindView(R.id.ivContactSort)
    ImageView ivContactSort;
    @BindView(R.id.ivVendorNameSort)
    ImageView ivVendorNameSort;
    @BindView(R.id.ivSupplySort)
    ImageView ivSupplySort;

    public VendorPickerDialog(@NonNull Context context, List<Vendor> vendorList, List<Vendor> vendorsWithProduct) {
        super(context);
        this.vendorList = vendorList;
        this.vendorsWithProduct = vendorsWithProduct;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.vendor_picker_dialog, null, false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);

    }

    public void setListener(OnVendorSelectListener listener) {
        this.listener = listener;
    }

    private interface OnVendorSelectListener{
        void onSelect(Vendor vendor);
    }
}
