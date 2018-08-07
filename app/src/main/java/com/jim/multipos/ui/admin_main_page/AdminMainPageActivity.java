package com.jim.multipos.ui.admin_main_page;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.DoubleSideAdminActivity;

public class AdminMainPageActivity extends DoubleSideAdminActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MpToolbar) findViewById(R.id.toolbar)).setMode(MpToolbar.ADMIN_MODE);
    }
}
