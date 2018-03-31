package com.jim.multipos.ui.reports.tills;

import android.view.Gravity;

import com.jim.mpviews.utils.ReportViewConstants;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.data.db.model.till.TillDetails;
import com.jim.multipos.data.db.model.till.TillManagementOperation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 27.03.2018.
 */

public class TillsReportPresenterImpl extends BasePresenterImpl<TillsReportView> implements TillsReportPresenter {

    private DatabaseManager databaseManager;
    private final DecimalFormat decimalFormat;
    private List<Till> tills;
    private  Object[][] objects;
    private int dataType[] = {
            ReportViewConstants.ID, ReportViewConstants.DATE, ReportViewConstants.DATE, ReportViewConstants.NAME, ReportViewConstants.NAME, ReportViewConstants.ACTION};
    private int weights[] = {1, 2, 2, 2, 2, 1};
    private int aligns[] = {Gravity.RIGHT, Gravity.CENTER, Gravity.CENTER, Gravity.RIGHT, Gravity.RIGHT, Gravity.CENTER};
    private String titles[] = {"Till ID", "Opened Time", "Closed Time", "Start Money Variance", "Till Amount Variance", "Action"};


    @Inject
    protected TillsReportPresenterImpl(TillsReportView view, DatabaseManager databaseManager, DecimalFormat decimalFormat) {
        super(view);
        this.databaseManager = databaseManager;
        this.decimalFormat = decimalFormat;
        tills = new ArrayList<>();
    }

    @Override
    public void initTillReportData() {
        databaseManager.getAllClosedTills().subscribe(tills1 -> {
            tills.clear();
            tills.addAll(tills1);
        });
        String abbr = databaseManager.getMainCurrency().getAbbr();
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
                objects[i][3] = decimalFormat.format(startMoneyVariance) + " " + abbr;
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
                objects[i][4] = decimalFormat.format(tillAmountVariance) + " " + abbr;

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

                objects[i][3] = decimalFormat.format(startMoneyVariance) + " " + abbr;
                objects[i][4] = decimalFormat.format(tillAmountVariance) + " " + abbr;
            }
        }

        view.fillReportView(objects, dataType, titles, weights, aligns);
    }

    @Override
    public void openTillDetailsDialog(int row, int column) {
        if (objects[row][0] instanceof Long){
            Long id = (Long) objects[row][0];
            Till till = databaseManager.getTillById(id).blockingGet();
            view.openTillDetailsDialog(till);
        }

    }
}
