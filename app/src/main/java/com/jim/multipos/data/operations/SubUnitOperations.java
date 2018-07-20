package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.unit.SubUnitsList;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by DEV on 06.09.2017.
 */

public interface SubUnitOperations {
    Observable<Long> addSubUnitList(SubUnitsList subUnitsList);

    Observable<List<SubUnitsList>> getSubUnitList();

    Observable<Boolean> deleteSubUnitList(SubUnitsList subUnitsList);
}
