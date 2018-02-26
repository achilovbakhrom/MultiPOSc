package com.jim.multipos.ui.mainpospage;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.utils.managers.NotifyManager;

import javax.inject.Inject;

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
