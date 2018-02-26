package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.order.PayedPartitions;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by developer on 30.01.2018.
 */

public interface PayedPartitionOperations {
    Single<List<PayedPartitions>> insertPayedPartitions(List<PayedPartitions> payedPartitions);
    Single<Long> deletePayedPartitions(List<PayedPartitions> payedPartitions);
}
