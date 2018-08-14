package com.jim.multipos.ui.admin_main_page;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.jim.mpviews.MpSpinnerTransparent;
import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.DoubleSideAdminActivity;
import com.jim.multipos.ui.admin_main_page.fragments.company.CompanyFragment;
import com.jim.multipos.ui.admin_main_page.fragments.company.CompanyInfoFragment;
import com.jim.multipos.ui.admin_main_page.fragments.dashboard.DashboardMainFragment;
import com.jim.multipos.ui.admin_main_page.fragments.dashboard.OrdersFragment;
import com.jim.multipos.ui.admin_main_page.fragments.dashboard.PosFragment;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.EstablishmentAddFragment;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.EstablishmentFragment;
import com.jim.multipos.utils.EntitiesDialog;
import com.jim.multipos.utils.rxevents.admin_main_page.CompanyItemClick;

import javax.inject.Inject;

import butterknife.BindView;

public class AdminMainPageActivity extends DoubleSideAdminActivity implements AdminMainPageView {

    private int lasPos = -1;
    @BindView(R.id.toolbar)
    MpToolbar toolbar;
    @BindView(R.id.flLeftContainer)
    FrameLayout layout;

    @Inject
    AdminMainPagePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        openCompanyFragment(new CompanyFragment(), new CompanyInfoFragment());

        toolbar.setMode(MpToolbar.ADMIN_MODE);
        ((MpSpinnerTransparent) findViewById(R.id.trans_spinner)).setItems(new String[]{"John John", "Shean Shean"}, new String[]{"1", "2"}, new String[]{"123"});
        ((MpSpinnerTransparent) findViewById(R.id.trans_spinner)).setAdapter();

        presenter.startObserving();
    }

    public void onClick(final View view) {
        ///this method is just for color the views
        colorViews(view);
        switch (view.getId()) {
            case R.id.company_container:
                openCompanyFragment(new CompanyFragment(), new CompanyInfoFragment());
                break;
            case R.id.dashboard_container:
                openDashboardFragment(new DashboardMainFragment(), new PosFragment(), new OrdersFragment());
                break;
            case R.id.establishment_container:
                openEstablishmentFragment(new EstablishmentAddFragment(), new EstablishmentFragment());
                break;
            case R.id.entity_container:
                EntitiesDialog dialog = new EntitiesDialog(this, findViewById(R.id.entity_container), new EntitiesDialog.OnDialogItemClickListener() {
                    @Override
                    public void onProduct() {

                    }

                    @Override
                    public void onProductClass() {

                    }

                    @Override
                    public void onDiscount() {

                    }

                    @Override
                    public void onServiceFee() {

                    }
                });
                dialog.show();
                break;
        }

    }

    private void colorViews(final View view) {
        if (lasPos != -1) {
            findViewById(lasPos).setBackgroundColor(getResources().getColor(R.color.colorBlueSecond));
        }
        view.setBackgroundColor(Color.parseColor("#57A1D1"));
        lasPos = view.getId();
    }

    @Override
    public void onEvent(Object o) {
        if (o instanceof CompanyItemClick) {
            openEditContainer(R.id.flLeftContainer);
        }
    }

    @Override
    protected void onDestroy() {
        presenter.clearDisposable();
        super.onDestroy();
    }
}
