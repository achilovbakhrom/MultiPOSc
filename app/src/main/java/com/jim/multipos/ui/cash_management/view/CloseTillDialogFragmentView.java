package com.jim.multipos.ui.cash_management.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.till.TillManagementOperation;
import com.jim.multipos.ui.first_configure_last.CompletionMode;

import java.util.List;

/**
 * Created by Sirojiddin on 09.02.2018.
 */

public interface CloseTillDialogFragmentView extends BaseView{
    void setFirstStepCompletionStatus(boolean status);
    void setSecondStepCompletionStatus(boolean status);
    void setThirdStepCompletionStatus(boolean status);
    void initAllSteps();
    void setCompletionMode(CompletionMode mode);
    void showFirstStep();
    void showSecondStep();
    void showThirdStep();
    void setSecondStepData(List<TillManagementOperation> operations);
    void setThirdStepData(List<TillManagementOperation> operations);
    void setCompleteStatus(boolean status);
    void closeTillDialog();
    void setTillStatus(int status);
}
