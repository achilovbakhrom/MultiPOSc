package com.jim.multipos.ui.consignment_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.ui.consignment.ConsignmentActivity;
import com.jim.multipos.ui.consignment_list.view.ConsignmentListFragment;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import static com.jim.multipos.ui.consignment.ConsignmentActivity.CONSIGNMENT_ID;
import static com.jim.multipos.ui.consignment.ConsignmentActivity.CONSIGNMENT_TYPE;
import static com.jim.multipos.ui.consignment.ConsignmentActivity.VENDOR_ID;

/**
 * Created by Sirojiddin on 30.11.2017.
 */

public class ConsignmentListActivity extends SimpleActivity implements ConsignmentListActivityView {

    protected static final int WITH_TOOLBAR = 1;

    @Override
    protected int getToolbar() {
        return WITH_TOOLBAR;
    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.WITH_SEARCH_TYPE;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Long vendorId = bundle.getLong(VENDOR_ID);
            Bundle bundle1 = new Bundle();
            bundle1.putLong(VENDOR_ID, vendorId);
            ConsignmentListFragment fragment = new ConsignmentListFragment();
            fragment.setArguments(bundle1);
            addFragment(fragment);
            toolbar.getSearchEditText().addTextChangedListener(new TextWatcherOnTextChange() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    fragment.setSearchText(toolbar.getSearchEditText().getText().toString());
                }
            });
        }

    }

    public void openConsignment(Long consignmentId, Integer consignmentType) {
        Intent intent = new Intent(this, ConsignmentActivity.class);
        intent.putExtra(CONSIGNMENT_ID, consignmentId);
        intent.putExtra(CONSIGNMENT_TYPE, consignmentType);
        startActivity(intent);
    }
}
