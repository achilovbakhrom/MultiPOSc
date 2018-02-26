package com.jim.multipos.ui.cash_management.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.till.TillManagementOperation;

import java.util.List;

/**
 * Created by Sirojiddin on 09.02.2018.
 */

public interface CloseTillDialogFragmentPresenter extends Presenter{
    void initData();
    void putCompletion(String key, boolean status);
    void showFirstStep();
    void showSecondStep();
    void showThirdStep();
    void setSecondStepData(List<TillManagementOperation> operations);
    void setThirdStepData(List<TillManagementOperation> operations);
    void checkAllStepsCompletion();
    void closeTill();
    void setTill(Long tillId);
}
