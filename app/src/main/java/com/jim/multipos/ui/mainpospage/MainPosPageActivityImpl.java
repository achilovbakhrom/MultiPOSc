package com.jim.multipos.ui.mainpospage;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.utils.managers.NotifyManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import static com.jim.multipos.ui.service_fee_new.Constants.TYPE_REPRICE;
import static com.jim.multipos.ui.service_fee_new.Constants.TYPE_VALUE;

/**
 * Created by developer on 07.08.2017.
 */

public class MainPosPageActivityImpl extends BasePresenterImpl<MainPosPageActivityView> implements MainPosPageActivityPresenter {
    private NotifyManager notifyManager;

    @Inject
    protected MainPosPageActivityImpl(MainPosPageActivityView mainPosPageActivityView, NotifyManager notifyManager) {
        super(mainPosPageActivityView);
        this.notifyManager = notifyManager;
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
