package com.jim.multipos.ui.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.ui.consignment.ConsignmentActivity;
import com.jim.multipos.ui.inventory.fragments.InventoryFragment;
import com.jim.multipos.ui.product_queue_list.ProductQueueListActivity;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import static com.jim.multipos.ui.consignment.ConsignmentActivity.OPERATION_TYPE;

/**
 * Created by developer on 09.11.2017.
 */

public class InventoryActivity extends SimpleActivity {
    public static final String PRODUCT_ID = "PRODUCT_ID";
    public static final String VENDOR_ID = "VENDOR_ID";

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
    public void setToolbarSearchText(String text){
        toolbar.getSearchEditText().setText(text);
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
        intent.putExtra(OPERATION_TYPE, consignment_type);
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

    public void openStockQueueForProduct(Long id) {
        Intent intent = new Intent(this, ProductQueueListActivity.class);
        intent.putExtra(PRODUCT_ID, id);
        startActivity(intent);
    }
}
