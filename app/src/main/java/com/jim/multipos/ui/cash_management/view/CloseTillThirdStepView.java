package com.jim.multipos.ui.cash_management.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.till.TillManagementOperation;

import java.util.List;

/**
 * Created by Sirojiddin on 13.01.2018.
 */

public interface CloseTillThirdStepView extends BaseView{
    void checkCompletion();
    void setDataToRecyclerView(List<TillManagementOperation> operations, Currency currency);
    void sendThirdStepCompletionStatus(boolean status);
    void collectData();
    void sendAllDataToParent(List<TillManagementOperation> operations);
}
