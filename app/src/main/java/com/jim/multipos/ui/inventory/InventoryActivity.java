package com.jim.multipos.ui.inventory;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.ui.inventory.fragments.InventoryFragment;

/**
 * Created by developer on 09.11.2017.
 */

public class InventoryActivity extends SimpleActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFragment(new InventoryFragment());
    }

    @Override
    protected int getToolbar() {
        return WITH_TOOLBAR;
    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.WITH_SEARCH_TYPE;
    }

}
