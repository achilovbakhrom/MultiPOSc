package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.PaymentType;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by user on 21.08.17.
 */

public interface PaymentTypeOperations {
    Observable<Long> addPaymentType(PaymentType paymentType);
    Observable<Boolean> addPaymentTypes(List<PaymentType> paymentTypes);
    Observable<Boolean> removePaymentType(PaymentType paymentType);
    Observable<Boolean> removeAllPaymentTypes();
    Observable<List<PaymentType>> getAllPaymentTypes();
    Observable <Boolean> isPaymentTypeNameExists(String name);
    List<PaymentType> getPaymentTypes();
    Single<PaymentType> getDebtPaymentType();
}
