package com.jim.multipos.ui.service_fee_new;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.ui.service_fee_new.adapters.ServiceFeeAdapter;
import com.jim.multipos.utils.WarningDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ServiceFeeActivity extends BaseActivity implements ServiceFeeView, ServiceFeeAdapter.OnClickListener {
    @Inject
    ServiceFeePresenter presenter;
    @BindView(R.id.toolbar)
    MpToolbar toolbar;
    @BindView(R.id.rvItems)
    RecyclerView rvItems;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_service_fee_fragment);

        unbinder = ButterKnife.bind(this);
        toolbar.setMode(MpToolbar.DEFAULT_TYPE);

        init();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        presenter.onDestroy();
        super.onDestroy();
    }

    private void init() {
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        ((SimpleItemAnimator) rvItems.getItemAnimator()).setSupportsChangeAnimations(false);
        rvItems.setAdapter(new ServiceFeeAdapter(this, this, presenter.getServiceFees(),
                getResources().getStringArray(R.array.service_fee_type),
                getResources().getStringArray(R.array.service_fee_app_type)));

        RxView.clicks(btnBack).subscribe(o -> {
            if (((ServiceFeeAdapter) rvItems.getAdapter()).getChangedItems().size() == 0) {
                finish();
            } else {
                WarningDialog warningDialog = new WarningDialog(this);
                warningDialog.setWarningMessage(getString(R.string.you_have_unsaved_service_fee, ((ServiceFeeAdapter) rvItems.getAdapter()).getChangedItems().size()));
                warningDialog.setOnYesClickListener(view -> finish());
                warningDialog.setOnNoClickListener(view -> warningDialog.dismiss());
                warningDialog.show();
            }
        });
    }

    @Override
    public void onAddClicked(ServiceFee serviceFee) {
        presenter.addServiceFee(serviceFee);
    }

    @Override
    public void onSaveClicked(ServiceFee serviceFee) {
        presenter.updateServiceFee(serviceFee);
    }

    @Override
    public void onDeleteClicked(ServiceFee serviceFee) {
        WarningDialog removeWarningDialog = new WarningDialog(this);
        removeWarningDialog.setWarningMessage(getString(R.string.do_you_want_delete));
        removeWarningDialog.setOnYesClickListener(view -> {
            presenter.deleteServiceFee(serviceFee);
            removeWarningDialog.dismiss();
        });
        removeWarningDialog.setOnNoClickListener(view -> {
            removeWarningDialog.dismiss();
        });
        removeWarningDialog.show();
    }

    @Override
    public void showActiveItemWarningDialog() {
        WarningDialog warningDialog = new WarningDialog(this);
        warningDialog.onlyText(true);
        warningDialog.setWarningMessage(getString(R.string.change_to_not_delete_when_not_active));
        warningDialog.setOnYesClickListener(view -> warningDialog.dismiss());
        warningDialog.show();
    }

    @Override
    public void onSortByAmountClicked(List<ServiceFee> items) {
        presenter.sortByAmount(items);
    }

    @Override
    public void onSortByTypeClicked(List<ServiceFee> items) {
        presenter.sortByType(items);
    }

    @Override
    public void onSortByReasonClicked(List<ServiceFee> items) {
        presenter.sortByReason(items);
    }

    @Override
    public void onSortByAppTypeClicked(List<ServiceFee> items) {
        presenter.sortByAppType(items);
    }

    @Override
    public void onSortByActiveClicked(List<ServiceFee> items) {
        presenter.sortByActive(items);
    }

    @Override
    public void onSortByDefaultClicked(List<ServiceFee> items) {
        presenter.sortByDefault(items);
    }

    @Override
    public void addServiceFee(ServiceFee serviceFee) {
        ((ServiceFeeAdapter) rvItems.getAdapter()).addItem(serviceFee);
    }

    @Override
    public void removeServiceFee(ServiceFee serviceFee) {
        ((ServiceFeeAdapter) rvItems.getAdapter()).removeItem(serviceFee);
    }

    @Override
    public void updateRecyclerView(List<ServiceFee> items) {
        ((ServiceFeeAdapter) rvItems.getAdapter()).setItems(items);
    }
}
