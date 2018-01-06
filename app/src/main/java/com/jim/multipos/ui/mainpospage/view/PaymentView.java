package com.jim.multipos.ui.mainpospage.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.DatabaseManager;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

public interface PaymentView extends BaseView {
    void openAddDebtDialog(DatabaseManager databaseManager);
}
