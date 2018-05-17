package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.mainpospage.adapter.VendorAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Portable-Acer on 28.11.2017.
 */

public class ChooseVendorDialog extends Dialog {
    public  interface OnClickListener {
        void onClick(Vendor vendor);
    }

    private List<Vendor> vendors;
    private OnClickListener listener;

    public ChooseVendorDialog(@NonNull Context context, List<Vendor> vendors, OnClickListener listener) {
        super(context);
        this.vendors = vendors;
        this.listener = listener;
    }

    @BindView(R.id.rvProductList)
    RecyclerView rvProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_product_info_vendor_dialog);
        getWindow().getDecorView().setBackgroundResource(R.color.colorTransparent);

        ButterKnife.bind(this);

        VendorAdapter adapter = new VendorAdapter();
        adapter.setListener(vendor -> {
            listener.onClick(vendor);
            dismiss();
        });
        adapter.setData(vendors);
        rvProductList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProductList.setAdapter(adapter);
    }
}
