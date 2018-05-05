package com.jim.multipos.ui.settings.security;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.mainpospage.dialogs.AccessWithEditPasswordDialog;
import com.jim.multipos.ui.settings.SettingsView;

import javax.annotation.security.RunAs;
import javax.inject.Inject;

import butterknife.BindView;
import dagger.Binds;

public class SecurityFragment extends BaseFragment implements SecurityView {

    @BindView(R.id.flWorkerPassChange)
    FrameLayout flWorkerPassChange;
    @BindView(R.id.flManagerPassChange)
    FrameLayout flManagerPassChange;

//    @Inject
//    SecurityPresenter presenter;
    @Inject
    PreferencesHelper preferencesHelper;

    @Override
    protected int getLayout() {
        return R.layout.security_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        forThisFragmentTypedManagerPassword = false;
        securityButtonsInit();
    }





    @BindView(R.id.llProductsSec)
    LinearLayout llProductsSec;
    @BindView(R.id.llReportsSec)
    LinearLayout llReportsSec;
    @BindView(R.id.llCustomerSec)
    LinearLayout llCustomerSec;
    @BindView(R.id.llInventorySec)
    LinearLayout llInventorySec;
    @BindView(R.id.llSettingsSec)
    LinearLayout llSettingsSec;
    @BindView(R.id.llCashManagmentSec)
    LinearLayout llCashManagmentSec;
    @BindView(R.id.llCancelOrderSec)
    LinearLayout llCancelOrderSec;
    @BindView(R.id.llManualDiscountsSec)
    LinearLayout llManualDiscountsSec;
    @BindView(R.id.llEditOderSec)
    LinearLayout llEditOderSec;
    @BindView(R.id.llManualServiceFeeSec)
    LinearLayout llManualServiceFeeSec;

    @BindView(R.id.ivProductsSec)
    ImageView ivProductsSec;
    @BindView(R.id.ivReportsSec)
    ImageView ivReportsSec;
    @BindView(R.id.ivCustomerSec)
    ImageView ivCustomerSec;
    @BindView(R.id.ivInventorySec)
    ImageView ivInventorySec;
    @BindView(R.id.ivSettingsSec)
    ImageView ivSettingsSec;
    @BindView(R.id.ivCashManagmentSec)
    ImageView ivCashManagmentSec;
    @BindView(R.id.ivCancelOrderSec)
    ImageView ivCancelOrderSec;
    @BindView(R.id.ivManualDiscountsSec)
    ImageView ivManualDiscountsSec;
    @BindView(R.id.icEditOrderSec)
    ImageView ivEditOrderSec;
    @BindView(R.id.ivManualServiceFeeSec)
    ImageView ivManualServiceFeeSec;

    boolean forThisFragmentTypedManagerPassword = false;
    void securityButtonsViewsFill(){
        if(preferencesHelper.isProductsProtected()){

        }
    }
    void securityButtonsInit(){
        llProductsSec.setOnClickListener(view -> {
            Runnable runnable = () -> {
                preferencesHelper.setProductsProtected(!preferencesHelper.isProductsProtected());
                if(preferencesHelper.isProductsProtected()){
                    ivProductsSec.setImageResource(R.drawable.ic_lock_black_24dp);
                }else {
                    ivProductsSec.setImageResource(R.drawable.ic_lock_open_black_24dp);
                }
            };
            checkAndRun(runnable);
        });
        llReportsSec.setOnClickListener(view -> {
            Runnable runnable = () -> {
                preferencesHelper.setReportsProtected(!preferencesHelper.isRepotsProtected());
                if(preferencesHelper.isRepotsProtected()){
                    ivReportsSec.setImageResource(R.drawable.ic_lock_black_24dp);
                }else {
                    ivReportsSec.setImageResource(R.drawable.ic_lock_open_black_24dp);
                }
            };
            checkAndRun(runnable);
        });
        llCustomerSec.setOnClickListener(view -> {
            Runnable runnable = () -> {
                preferencesHelper.setCustomersProtected(!preferencesHelper.isCustomersProtected());
                if(preferencesHelper.isCustomersProtected()){
                    ivCustomerSec.setImageResource(R.drawable.ic_lock_black_24dp);
                }else {
                    ivCustomerSec.setImageResource(R.drawable.ic_lock_open_black_24dp);
                }
            };
            checkAndRun(runnable);
        });
        llInventorySec.setOnClickListener(view -> {
            Runnable runnable = () -> {
                preferencesHelper.setInventorysProtected(!preferencesHelper.isInventoryProtected());
                if(preferencesHelper.isInventoryProtected()){
                    ivInventorySec.setImageResource(R.drawable.ic_lock_black_24dp);
                }else {
                    ivInventorySec.setImageResource(R.drawable.ic_lock_open_black_24dp);
                }
            };
            checkAndRun(runnable);
        });
        llSettingsSec.setOnClickListener(view -> {
            Runnable runnable = () -> {
                preferencesHelper.setSettingsProtected(!preferencesHelper.isSettingsProtected());
                if(preferencesHelper.isSettingsProtected()){
                    ivSettingsSec.setImageResource(R.drawable.ic_lock_black_24dp);
                }else {
                    ivSettingsSec.setImageResource(R.drawable.ic_lock_open_black_24dp);
                }
            };
            checkAndRun(runnable);
        });
        llCashManagmentSec.setOnClickListener(view -> {
            Runnable runnable = () -> {
                preferencesHelper.setCashManagmentsProtected(!preferencesHelper.isCashManagmentProtected());
                if(preferencesHelper.isCashManagmentProtected()){
                    ivCashManagmentSec.setImageResource(R.drawable.ic_lock_black_24dp);
                }else {
                    ivCashManagmentSec.setImageResource(R.drawable.ic_lock_open_black_24dp);
                }
            };
            checkAndRun(runnable);
        });
        llCancelOrderSec.setOnClickListener(view -> {
            Runnable runnable = () -> {
                preferencesHelper.setCancelOrderProtected(!preferencesHelper.isCancelOrderProtected());
                if(preferencesHelper.isCancelOrderProtected()){
                    ivCancelOrderSec.setImageResource(R.drawable.ic_lock_black_24dp);
                }else {
                    ivCancelOrderSec.setImageResource(R.drawable.ic_lock_open_black_24dp);
                }
            };
            checkAndRun(runnable);
        });

        llManualDiscountsSec.setOnClickListener(view -> {
            Runnable runnable = () -> {
                preferencesHelper.setManualDiscountProtected(!preferencesHelper.isManualDiscountsProtected());
                if(preferencesHelper.isManualDiscountsProtected()){
                    ivManualDiscountsSec.setImageResource(R.drawable.ic_lock_black_24dp);
                }else {
                    ivManualDiscountsSec.setImageResource(R.drawable.ic_lock_open_black_24dp);
                }
            };
            checkAndRun(runnable);
        });

        llEditOderSec.setOnClickListener(view -> {
            Runnable runnable = () -> {
                preferencesHelper.setEditOrderProtected(!preferencesHelper.isEditOrderProtected());
                if(preferencesHelper.isEditOrderProtected()){
                    ivEditOrderSec.setImageResource(R.drawable.ic_lock_black_24dp);
                }else {
                    ivEditOrderSec.setImageResource(R.drawable.ic_lock_open_black_24dp);
                }
            };
            checkAndRun(runnable);
        });
        llManualServiceFeeSec.setOnClickListener(view -> {
            Runnable runnable = () -> {
                preferencesHelper.setManualServiceFeeProtected(!preferencesHelper.isManualServiceFeeProtected());
                if(preferencesHelper.isManualServiceFeeProtected()){
                    ivManualServiceFeeSec.setImageResource(R.drawable.ic_lock_black_24dp);
                }else {
                    ivManualServiceFeeSec.setImageResource(R.drawable.ic_lock_open_black_24dp);
                }
            };
            checkAndRun(runnable);
        });

    }

    void checkAndRun(Runnable runnable){
        if(forThisFragmentTypedManagerPassword){
            runnable.run();
        }else {
            AccessWithEditPasswordDialog accessWithEditPasswordDialog = new AccessWithEditPasswordDialog(getContext(), new AccessWithEditPasswordDialog.OnAccsessListner() {
                @Override
                public void accsessSuccess() {
                    forThisFragmentTypedManagerPassword = true;
                    runnable.run();
                }

                @Override
                public void onBruteForce() {

                }
            },preferencesHelper);
            accessWithEditPasswordDialog.show();
        }

    }


}
