package com.jim.multipos.ui.reports.tills;

import android.os.Bundle;
import android.view.Gravity;

import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.data.db.model.till.TillDetails;
import com.jim.multipos.data.db.model.till.TillManagementOperation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 27.03.2018.
 */

public class TillsReportPresenterImpl extends BasePresenterImpl<TillsReportView> implements TillsReportPresenter {

    private DatabaseManager databaseManager;
    private List<Till> tills;
    private  Object[][] objects;
    private Calendar fromDate;
    private Calendar toDate;


    @Inject
    protected TillsReportPresenterImpl(TillsReportView view, DatabaseManager databaseManager) {
        super(view);
        this.databaseManager = databaseManager;
        tills = new ArrayList<>();
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        databaseManager.getAllClosedTills().subscribe(tills1 -> {
            tills = tills1;
        });
        objects = new Object[tills.size()][6];
        for (int i = 0; i < tills.size(); i++) {
            Till till = tills.get(i);
            objects[i][0] = till.getId();
            objects[i][1] = till.getOpenDate();
            objects[i][2] = till.getCloseDate();
            objects[i][5] = "details";
            double startMoneyVariance = 0;
            double tillAmountVariance = 0;
            if (till.getId() == 1) {
                objects[i][3] = startMoneyVariance;
                double closedAmount = 0;
                double expectedAmount = 0;
                List<TillManagementOperation> thisTillOperations = databaseManager.getTillManagementOperationsByTillId(till.getId()).blockingGet();
                for (TillManagementOperation operation : thisTillOperations) {
                    if (operation.getType() == TillManagementOperation.CLOSED_WITH)
                        closedAmount += operation.getAmount();
                }
                List<TillDetails> tillDetails = databaseManager.getTillDetailsByTillId(till.getId()).blockingGet();
                for (TillDetails details : tillDetails) {
                    expectedAmount += details.getExpectedTillAmount();
                }
                tillAmountVariance = closedAmount - expectedAmount;
                objects[i][4] = tillAmountVariance;

            } else {
                double moneyLeftFromPrevTill = 0;
                double thisTillStartMoney = 0;
                double closedAmount = 0;
                double expectedAmount = 0;

                List<TillManagementOperation> thisTillOperations = databaseManager.getTillManagementOperationsByTillId(till.getId()).blockingGet();
                for (TillManagementOperation operation : thisTillOperations) {
                    if (operation.getType() == TillManagementOperation.OPENED_WITH)
                        thisTillStartMoney += operation.getAmount();
                    if (operation.getType() == TillManagementOperation.CLOSED_WITH)
                        closedAmount += operation.getAmount();
                }

                List<TillDetails> tillDetails = databaseManager.getTillDetailsByTillId(till.getId()).blockingGet();
                for (TillDetails details : tillDetails) {
                    expectedAmount += details.getExpectedTillAmount();
                }
                List<TillManagementOperation> prevTillOperations = databaseManager.getTillManagementOperationsByTillId(tills.get(i - 1).getId()).blockingGet();
                for (TillManagementOperation operation : prevTillOperations) {
                    if (operation.getType() == TillManagementOperation.TO_NEW_TILL)
                        moneyLeftFromPrevTill += operation.getAmount();
                }

                startMoneyVariance = thisTillStartMoney - moneyLeftFromPrevTill;
                tillAmountVariance = closedAmount - expectedAmount;

                objects[i][3] = startMoneyVariance;
                objects[i][4] = tillAmountVariance;
            }
        }

        view.fillReportView(objects);
    }

    @Override
    public void openTillDetailsDialog(Object[][] objects, int row, int column) {
        if (objects[row][0] instanceof Long){
            Long id = (Long) objects[row][0];
            Till till = databaseManager.getTillById(id).blockingGet();
            view.openTillDetailsDialog(till);
        }

    }

    @Override
    public void onChooseDateInterval(Calendar fromDate, Calendar toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        view.updateDateIntervalUi(fromDate, toDate);
    }

    @Override
    public void onSearchTyped(String searchText) {

    }

    @Override
    public void onClickedDateInterval() {
        view.openDateInterval(fromDate, toDate);
    }

    @Override
    public void onClickedExportExcel() {

    }

    @Override
    public void onClickedExportPDF() {

    }

    @Override
    public void onClickedFilter() {

    }

    @Override
    public void onChoisedPanel(int postion) {

    }
}
