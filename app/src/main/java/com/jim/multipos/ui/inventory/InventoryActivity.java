package com.jim.multipos.ui.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.data.db.model.consignment.Consignment;
import com.jim.multipos.ui.consignment.ConsignmentActivity;
import com.jim.multipos.ui.inventory.fragments.InventoryFragment;
import com.jim.multipos.utils.TextWatcherOnTextChange;

/**
 * Created by developer on 09.11.2017.
 */

public class InventoryActivity extends SimpleActivity {
    public static final String PRODUCT_ID = "PRODUCT_ID";
    public static final String VENDOR_ID = "VENDOR_ID";
    public static final String CONSIGNMENT_TYPE = "CONSIGNMENT_TYPE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InventoryFragment fragment = new InventoryFragment();
        addFragment(fragment);
        toolbar.getSearchEditText().addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fragment.searchText(toolbar.getSearchEditText().getText().toString());
            }
        });

        toolbar.getBarcodeView().setOnClickListener(view -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(InventoryActivity.this);
            intentIntegrator.initiateScan();
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

    public void sendDataWithBundle(Long productId, Long vendorId, int consignment_type) {
        Intent intent = new Intent(this, ConsignmentActivity.class);
        intent.putExtra(VENDOR_ID, vendorId);
        intent.putExtra(PRODUCT_ID, productId);
        intent.putExtra(CONSIGNMENT_TYPE, consignment_type);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() != null) {
                InventoryFragment fragment = (InventoryFragment) getCurrentFragment();
                if (fragment != null && fragment.isVisible())
                    fragment.searchText(intentResult.getContents());
            }
        }
    }
}
