package com.jim.multipos.utils.rxevents.main_order_events;

import com.jim.multipos.data.db.model.ServiceFee;

import lombok.Data;

/**
 * Created by developer on 27.02.2018.
 */
@Data
public class ServiceFeeEvent {
    private ServiceFee serviceFee;
    private int type;
}
