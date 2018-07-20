package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.DiscountLog;
import com.jim.multipos.data.db.model.ServiceFee;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by developer on 24.10.2017.
 */

public interface DiscountOperations {
    Single<List<Discount>> getAllDiscounts();

    Single<Discount> insertDiscount(Discount discount);

    Observable<List<Discount>> getDiscountsWithAllItemTypes(String[] discountTypes);

    Single<List<Discount>> getAllDiscountsWithoutFiltering();

    Single<DiscountLog> insertDiscountLog(DiscountLog discountLog);

    Single<List<DiscountLog>> getDiscountLogs();

    Single<List<Discount>> getStaticDiscounts();

    Single<List<Discount>> getDiscountsByType(int discountApplyType);
}
