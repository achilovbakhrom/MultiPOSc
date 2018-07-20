package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.ServiceFeeLog;
import com.jim.multipos.data.db.model.currency.Currency;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by user on 29.08.17.
 */

public interface ServiceFeeOperations {
    Observable<ServiceFee> addServiceFee(ServiceFee serviceFee);

    Observable<Boolean> addServiceFees(List<ServiceFee> serviceFeeList);

    Observable<List<ServiceFee>> getAllServiceFees();

    Observable<Boolean> removeAllServiceFees();

    Observable<Boolean> removeServiceFee(ServiceFee serviceFee);

    Observable<List<Currency>> getAllCurrencies();

    Observable<List<ServiceFee>> getServiceFeesWithAllItemTypes();

    Single<ServiceFeeLog> insertServiceFeeLog(ServiceFeeLog serviceFeeLog);

    Single<List<ServiceFeeLog>> getServiceFeeLogs();
}
