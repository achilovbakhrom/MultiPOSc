package com.jim.multipos.ui.service_fee_new;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.ui.service_fee_new.adapters.ServiceFeeAdapter;
import com.jim.multipos.ui.service_fee_new.model.ServiceFeeAdapterDetails;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.WarningDialog;
import com.jim.multipos.utils.rxevents.main_order_events.ServiceFeeEvent;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ServiceFeeActivity extends BaseActivity implements ServiceFeeView {
    @Inject
    ServiceFeePresenter presenter;
    @Inject
    RxBus rxBus;
    @BindView(R.id.toolbar)
    MpToolbar toolbar;
    @BindView(R.id.rvItems)
    RecyclerView rvItems;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    Unbinder unbinder;
    private ServiceFeeAdapter serviceFeeAdapter;

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
        serviceFeeAdapter = new ServiceFeeAdapter(this);
        presenter.initDataToServiceFee();
        rvItems.setAdapter(serviceFeeAdapter);
        serviceFeeAdapter.setListners(new ServiceFeeAdapter.OnDiscountCallback() {
            @Override
            public void onAddPressed(double amount, int type, String description, int appType, boolean active) {
                presenter.addServiceFee(amount, type, description, appType, active);
            }

            @Override
            public void onSave(double amount, int type, String description, int appType, boolean active, ServiceFee serviceFee) {
                presenter.onSave(amount, type, description, appType, active, serviceFee);
            }

            @Override
            public void onDelete(ServiceFee serviceFee) {
                presenter.deleteServiceFee(serviceFee);
            }

            @Override
            public void sortList(ServiceFeePresenterImpl.ServiceFeeSortTypes serviceFeeSortTypes) {
                presenter.sortList(serviceFeeSortTypes);
            }
        });
        btnBack.setOnClickListener(view -> presenter.onClose());
    }


    @Override
    public void refreshList(List<ServiceFeeAdapterDetails> detailsList) {
        serviceFeeAdapter.setData(detailsList);
        serviceFeeAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyItemAdd(int position) {
        serviceFeeAdapter.notifyItemInserted(position);
    }

    @Override
    public void refreshList() {
        serviceFeeAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyItemChanged(int pos) {
        serviceFeeAdapter.notifyItemChanged(pos);
    }

    @Override
    public void sendEvent(int event, ServiceFee serviceFee) {
        rxBus.send(new ServiceFeeEvent(serviceFee, event));
    }

    @Override
    public void sendChangeEvent(int event, ServiceFee oldServiceFee, ServiceFee newServiceFee) {
        ServiceFeeEvent serviceFeeEvent = new ServiceFeeEvent(oldServiceFee, event);
        serviceFeeEvent.setNewServiceFee(newServiceFee);
        rxBus.send(serviceFeeEvent);
    }

    @Override
    public void notifyItemRemove(int position) {
        serviceFeeAdapter.notifyItemRemoved(position);
    }

    @Override
    public void closeActivity() {
        finish();
    }

    @Override
    public void openWarning() {
        WarningDialog warningDialog = new WarningDialog(this);
        warningDialog.setWarningMessage(getString(R.string.warning_discard_changes));
        warningDialog.setOnYesClickListener(view1 -> warningDialog.dismiss());
        warningDialog.setOnNoClickListener(view1 -> closeActivity());
        warningDialog.setPositiveButtonText(getString(R.string.cancel));
        warningDialog.setNegativeButtonText(getString(R.string.discard));
        warningDialog.show();
    }
}
