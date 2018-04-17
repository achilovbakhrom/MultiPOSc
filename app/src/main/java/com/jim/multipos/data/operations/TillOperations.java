package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.data.db.model.till.TillManagementOperation;
import com.jim.multipos.data.db.model.till.TillDetails;
import com.jim.multipos.data.db.model.till.TillOperation;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Single;

/**
 * Created by Sirojiddin on 03.02.2018.
 */

public interface TillOperations {
    Single<TillOperation> insertTillOperation(TillOperation tillOperation);
    Single<TillDetails> insertTillDetails(TillDetails tillDetails);
    Single<Double> getTotalTillOperationsAmount(Long accountId, Long tillId, int type);
    Single<Double> getTotalTillManagementOperationsAmount(Long accountId, Long tillId, int type);
    Single<Till> insertTill(Till till);
    Single<List<TillOperation>> getTillOperationsByAccountId(Long accountId, Long tillId);
    Single<Till> getOpenTill();
    Single<Boolean> hasOpenTill();
    Single<Boolean> isNoTills();
    Single<Till> getLastClosedTill();
    Single<TillManagementOperation> insertTillManagementOperation(TillManagementOperation tillCloseOperation);
    Single<Till> getTillById(Long tillId);
    Single<List<TillManagementOperation>> insertTillCloseOperationList(List<TillManagementOperation> tillCloseOperations);
    Single<Long> getCurrentOpenTillId();
    Single<List<TillManagementOperation>> getTillManagementOperationsByTillId(Long id);
    Single<TillDetails> getTillDetailsByAccountId(Long accountId, Long tillId);
    Single<List<Till>> getAllTills();
    Single<List<TillDetails>> getTillDetailsByTillId(Long tillId);
    Single<List<Till>> getAllClosedTills();
    Single<List<Till>> getClosedTillsInInterval(Calendar fromDate, Calendar toDate);
    Single<List<TillOperation>> getTillOperationsInterval(Calendar fromDate,Calendar toDate);
}
