package com.jim.multipos.ui.cash_management.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.inventory.HistoryInventoryState;
import com.jim.multipos.data.db.model.inventory.InventoryState;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.data.db.model.till.TillManagementOperation;
import com.jim.multipos.ui.cash_management.view.CloseTillDialogFragmentView;
import com.jim.multipos.ui.cash_management.view.CloseTillFirstStepFragment;
import com.jim.multipos.ui.cash_management.view.CloseTillSecondStepFragment;
import com.jim.multipos.ui.cash_management.view.CloseTillThirdStepFragment;
import com.jim.multipos.ui.first_configure_last.CompletionMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 09.02.2018.
 */

public class CloseTillDialogFragmentPresenterImpl extends BasePresenterImpl<CloseTillDialogFragmentView> implements CloseTillDialogFragmentPresenter {

    private Map<String, Boolean> completion;
    public static final String FIRST_KEY = CloseTillFirstStepFragment.class.getName();
    public static final String SECOND_KEY = CloseTillSecondStepFragment.class.getName();
    public static final String THIRD_KEY = CloseTillThirdStepFragment.class.getName();
    private List<TillManagementOperation> secondStepOperations;
    private List<TillManagementOperation> thirdStepOperations;
    private Till till;
    private DatabaseManager databaseManager;

    @Inject
    protected CloseTillDialogFragmentPresenterImpl(CloseTillDialogFragmentView view, DatabaseManager databaseManager) {
        super(view);
        this.databaseManager = databaseManager;
        completion = new HashMap<>();
        secondStepOperations = new ArrayList<>();
        thirdStepOperations = new ArrayList<>();
    }

    @Override
    public void setTill(Long tillId) {
        till = databaseManager.getTillById(tillId).blockingGet();
    }

    @Override
    public void initData() {
        completion.put(FIRST_KEY, false);
        completion.put(SECOND_KEY, false);
        completion.put(THIRD_KEY, false);
        view.initAllSteps();
    }

    @Override
    public void showFirstStep() {
        CompletionMode mode = CompletionMode.NEXT;
        if (isLast(FIRST_KEY))
            mode = CompletionMode.FINISH;
        view.showFirstStep();
        view.setCompletionMode(mode);
    }

    @Override
    public void showSecondStep() {
        CompletionMode mode = CompletionMode.NEXT;
        if (isLast(SECOND_KEY))
            mode = CompletionMode.FINISH;
        view.showSecondStep();
        view.setCompletionMode(mode);
    }

    @Override
    public void showThirdStep() {
        CompletionMode mode = CompletionMode.NEXT;
        if (isLast(THIRD_KEY))
            mode = CompletionMode.FINISH;
        view.showThirdStep();
        view.setCompletionMode(mode);
    }

    @Override
    public void setSecondStepData(List<TillManagementOperation> operations) {
        secondStepOperations = operations;
        databaseManager.insertTillCloseOperationList(secondStepOperations).subscribe();
    }

    @Override
    public void setThirdStepData(List<TillManagementOperation> operations) {
        thirdStepOperations = operations;
        databaseManager.insertTillCloseOperationList(thirdStepOperations).subscribe();
    }

    private boolean isLast(String name) {
        if (completion != null) {
            int notCompletedCount = 0;
            for (String key : completion.keySet()) {
                if (notCompletedCount > 1) {
                    return false;
                }
                if (!completion.get(key))
                    notCompletedCount++;
            }
            if (notCompletedCount == 1 && !completion.get(name)) {
                return true;
            }
            if (notCompletedCount == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void checkAllStepsCompletion() {
        if (completion != null) {
            int completedCount = 0;
            for (String key : completion.keySet()) {
                if (completion.get(key))
                    completedCount++;
            }
            view.setCompleteStatus(completedCount == 3);
        }
    }

    @Override
    public void closeTill() {
        till.setCloseDate(System.currentTimeMillis());
        till.setStatus(Till.CLOSED);
        List<Order> orders = databaseManager.getOrdersByTillId(till.getId()).blockingGet();
        for (int i = 0; i < orders.size(); i++) {
            orders.get(i).setIsArchive(true);
            databaseManager.insertOrder(orders.get(i)).blockingGet();
        }
        databaseManager.getInventoryStates().subscribe(inventoryStates -> {
            for (InventoryState inventoryState: inventoryStates) {
                HistoryInventoryState state = new HistoryInventoryState();
                state.setLowStockAlert(inventoryState.getLowStockAlert());
                state.setProduct(inventoryState.getProduct());
                state.setTill(till);
                state.setValue(inventoryState.getValue());
                state.setVendor(inventoryState.getVendor());
                databaseManager.insertHistoryInventoryState(state).blockingGet();
            }
        });
        databaseManager.insertTill(till).blockingGet();
        view.setTillStatus(Till.CLOSED);
        view.closeTillDialog();

    }

    @Override
    public void putCompletion(String key, boolean status) {
        completion.put(key, status);
    }
}
