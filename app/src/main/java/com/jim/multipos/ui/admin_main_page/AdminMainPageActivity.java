package com.jim.multipos.ui.admin_main_page;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jim.mpviews.MpSpinnerTransparent;
import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.DoubleSideAdminActivity;
import com.jim.multipos.ui.admin_main_page.fragments.company.CompanyFragment;
import com.jim.multipos.ui.admin_main_page.fragments.company.CompanyInfoFragment;

public class AdminMainPageActivity extends DoubleSideAdminActivity {

    private int lasPos = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addFragmentToLeft(new CompanyFragment());
        addFragmentToRight(new CompanyInfoFragment());
        ((MpToolbar) findViewById(R.id.toolbar)).setMode(MpToolbar.ADMIN_MODE);
        ((MpSpinnerTransparent) findViewById(R.id.trans_spinner)).setItems(new String[]{"John John", "Shean Shean"}, new String[]{"1", "2"}, new String[]{"123"});
        ((MpSpinnerTransparent) findViewById(R.id.trans_spinner)).setAdapter();

    }

    public void onClick(final View view) {
        ///this method is just for color the views
        colorViews(view);

        ////write fragment transaction logic here based on view id
    }

    private void colorViews(final View view) {
        if (lasPos != -1) {
            findViewById(lasPos).setBackgroundColor(getResources().getColor(R.color.colorBlueSecond));
        }
        view.setBackgroundColor(Color.parseColor("#57A1D1"));
        lasPos = view.getId();
    }
}
