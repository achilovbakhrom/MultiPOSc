package com.jim.multipos.ui.vendor_products_view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.ui.vendor_products_view.fragments.VendorDetailsFragment;
import com.jim.multipos.ui.vendor_products_view.fragments.VendorDetailsList;

import java.text.DecimalFormat;

import javax.inject.Inject;

import lombok.Getter;

import static com.jim.multipos.ui.inventory.InventoryActivity.VENDOR_ID;

public class VendorProductsViewActivity extends DoubleSideActivity implements VendorProductsView {
    @Inject
    @Getter
    VendorProductsViewPresenter presenter;
    @Inject
    @Getter
    DecimalFormat decimalFormat;

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Long vendorId = bundle.getLong(VENDOR_ID);
            presenter.setVendorId(vendorId);
        }
        addFragmentToLeft(new VendorDetailsFragment());
        addFragmentToRight(new VendorDetailsList());
    }
}
