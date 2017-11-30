package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by developer on 24.10.2017.
 */

public interface DiscountOperations {
    Single<List<Discount>> getAllDiscounts();
    Single<Long> insertDiscount(Discount discount);
    Observable<List<Discount>> getDiscountsWithAllItemTypes(String[] discountTypes);
}
