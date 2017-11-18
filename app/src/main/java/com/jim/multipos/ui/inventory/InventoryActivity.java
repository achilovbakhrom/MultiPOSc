package com.jim.multipos.ui.inventory;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.core.SimpleActivity;
import com.jim.multipos.ui.inventory.fragments.InventoryFragment;
import com.jim.multipos.utils.TextWatcherOnTextChange;

/**
 * Created by developer on 09.11.2017.
 */

public class InventoryActivity extends SimpleActivity {
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
