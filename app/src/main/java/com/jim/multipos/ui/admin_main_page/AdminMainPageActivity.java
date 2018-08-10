package com.jim.multipos.ui.admin_main_page;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jim.mpviews.MpSpinnerTransparent;
import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.DoubleSideAdminActivity;
import com.jim.multipos.ui.admin_main_page.fragments.company.CompanyFragment;
import com.jim.multipos.ui.admin_main_page.fragments.company.CompanyInfoFragment;

public class AdminMainPageActivity extends DoubleSideAdminActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addFragmentToLeft(new CompanyFragment());
        addFragmentToRight(new CompanyInfoFragment());
        ((MpToolbar) findViewById(R.id.toolbar)).setMode(MpToolbar.ADMIN_MODE);
        ((MpSpinnerTransparent) findViewById(R.id.trans_spinner)).setItems(new String[]{"John John", "Shean Shean"}, new String[]{"1", "2"}, new String[]{"123"});
        ((MpSpinnerTransparent) findViewById(R.id.trans_spinner)).setAdapter();

    }
}
