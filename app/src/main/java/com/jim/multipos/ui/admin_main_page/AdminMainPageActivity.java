package com.jim.multipos.ui.admin_main_page;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jim.mpviews.MpSpinnerTransparent;
import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.DoubleSideAdminActivity;
import com.jim.multipos.ui.admin_main_page.fragments.company.CompanyAddFragment;
import com.jim.multipos.ui.admin_main_page.fragments.company.CompanyInfoFragment;
import com.jim.multipos.ui.admin_main_page.fragments.dashboard.DashboardMainFragment;
import com.jim.multipos.ui.admin_main_page.fragments.dashboard.OrdersFragment;
import com.jim.multipos.ui.admin_main_page.fragments.dashboard.PosFragment;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.EstablishmentAddFragment;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.EstablishmentFragment;
import com.jim.multipos.ui.admin_main_page.fragments.product.ProductAddFragment;
import com.jim.multipos.ui.admin_main_page.fragments.product.ProductsFragment;
import com.jim.multipos.utils.EntitiesDialog;

import butterknife.BindView;

public class AdminMainPageActivity extends DoubleSideAdminActivity{

    private int lasPos = -1;

    @BindView(R.id.toolbar)
    MpToolbar toolbar;
    @BindView(R.id.flLeftContainer)
    FrameLayout layout;

    View v;

    enum ToolbarItems{
        COMPANY, DASHBOARD, ESTABLISHMENT,
        ENTITIES_PRODUCT, ENTITIES_PRODUCT_CLASS,
        ENTITIES_DISCOUNT, ENTITIES_SERVICE_FEE,
        VENDORS, INVENTORY, CRM,
        HRM, REPORTS
    }

    ToolbarItems toolbarItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        toolbar.setMode(MpToolbar.ADMIN_MODE);
        ((MpSpinnerTransparent) findViewById(R.id.trans_spinner)).setItems(new String[]{"John John", "Shean Shean"}, new String[]{"1", "2"}, new String[]{"123"});
        ((MpSpinnerTransparent) findViewById(R.id.trans_spinner)).setAdapter();
        ((MpSpinnerTransparent) findViewById(R.id.trans_spinner)).setOnItemSelectedListener((adapterView, view, i, l) -> {
            Toast.makeText(this, ""+i,Toast.LENGTH_LONG).show();
        });

        v = findViewById(R.id.flLeftContainer);

        openCompanyFragment(new CompanyInfoFragment(), new CompanyAddFragment());
        colorViews(findViewById(R.id.company_container));
        toolbarItems = ToolbarItems.COMPANY;

    }


    public void onClick(final View view) {
        ///this method is just for color the views
        colorViews(view);

        switch (view.getId()) {
            case R.id.company_container:
                if(toolbarItems != ToolbarItems.COMPANY) {
                    toolbarItems = ToolbarItems.COMPANY;
                    openCompanyFragment(new CompanyInfoFragment(), new CompanyAddFragment());
                }
                break;
            case R.id.dashboard_container:
                if(toolbarItems != ToolbarItems.DASHBOARD) {
                    toolbarItems = ToolbarItems.DASHBOARD;
                    openDashboardFragment(new DashboardMainFragment(), new PosFragment(), new OrdersFragment());
                }
                break;
            case R.id.establishment_container:
                if(toolbarItems != ToolbarItems.ESTABLISHMENT) {
                    toolbarItems = ToolbarItems.ESTABLISHMENT;
                    openEstablishmentFragment(new EstablishmentFragment(), new EstablishmentAddFragment());
                }
                break;
            case R.id.entity_container:

                if(toolbarItems != ToolbarItems.ENTITIES_DISCOUNT){
                    toolbarItems = ToolbarItems.ENTITIES_DISCOUNT;
                    openEntitiesFragment();
                }
                break;
        }

    }

    private void colorViews(final View view) {
        if (lasPos != -1) {
            findViewById(lasPos).setBackgroundColor(ContextCompat.getColor(this, R.color.colorCircleBlue));
        }
        view.setBackgroundColor(Color.parseColor("#438FBF"));
        lasPos = view.getId();
    }

    public void openEditor(){
        openEditContainer();
    }

    public void closeEditor(){
        closeEditContainer();
    }


}
