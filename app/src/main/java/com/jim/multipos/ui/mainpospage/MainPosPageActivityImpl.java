package com.jim.multipos.ui.mainpospage;

import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import static com.jim.multipos.ui.service_fee_new.Constants.TYPE_REPRICE;
import static com.jim.multipos.ui.service_fee_new.Constants.TYPE_VALUE;

/**
 * Created by developer on 07.08.2017.
 */

public class MainPosPageActivityImpl implements MainPosPageActivityPresenter {
    private DatabaseManager databaseManager;

    @Inject
    public MainPosPageActivityImpl(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
}
