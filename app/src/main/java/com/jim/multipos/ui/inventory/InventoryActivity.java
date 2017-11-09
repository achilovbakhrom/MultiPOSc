package com.jim.multipos.ui.inventory;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.SimpleActivity;

/**
 * Created by Sirojiddin on 09.11.2017.
 */

public class InventoryActivity extends SimpleActivity implements InventoryActivityView {

    protected static final int WITH_TOOLBAR = 1;

    @Override
    protected int getToolbar() {
        return WITH_TOOLBAR;
    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }
}
