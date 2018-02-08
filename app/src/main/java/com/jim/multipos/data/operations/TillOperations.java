package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.data.db.model.till.TillDetails;
import com.jim.multipos.data.db.model.till.TillOperation;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Sirojiddin on 03.02.2018.
 */

public interface TillOperations {
    Single<TillOperation> insertTillOperation(TillOperation tillOperation);
    Single<TillDetails> insertTillDetails(TillDetails tillDetails);
    Single<Till> insertTill(Till till);
    Single<List<TillOperation>> getTillOperationsByAccountId(Long accountId, Long tillId);
    Single<Till> getOpenTill();
    Single<Boolean> hasOpenTill();
    Single<Boolean> isNoTills();
    Single<Till> getLastClosedTill();
}
