package com.jim.multipos.ui.vendor_item_managment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.ui.consignment.ConsignmentActivity;
import com.jim.multipos.ui.vendor_item_managment.fragments.VendorItemFragment;
import com.jim.multipos.ui.vendor_products_view.VendorProductsViewActivity;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import static com.jim.multipos.ui.inventory.InventoryActivity.CONSIGNMENT_TYPE;
import static com.jim.multipos.ui.inventory.InventoryActivity.PRODUCT_ID;
import static com.jim.multipos.ui.inventory.InventoryActivity.VENDOR_ID;

/**
 * Created by developer on 09.11.2017.
 */

public class VendorItemsActivity extends SimpleActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VendorItemFragment fragment = new VendorItemFragment();
        addFragment(fragment);
        toolbar.getSearchEditText().addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    fragment.searchText(toolbar.getSearchEditText().getText().toString());
            }
        });
    }

    @Override
    protected int getToolbar() {
        return WITH_TOOLBAR;
    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.WITH_SEARCH_TYPE;
    }

    public void sendDataToConsignment(Long vendorId, int consignment_type) {
        Intent intent = new Intent(this, ConsignmentActivity.class);
        intent.putExtra(VENDOR_ID, vendorId);
        intent.putExtra(CONSIGNMENT_TYPE, consignment_type);
        startActivity(intent);
    }

    public void openVendorDetails(Long vendorId) {
        Intent intent = new Intent(this, VendorProductsViewActivity.class);
        intent.putExtra(VENDOR_ID, vendorId);
        startActivity(intent);
    }
}
