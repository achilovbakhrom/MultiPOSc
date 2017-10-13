package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.data.db.model.unit.UnitCategory;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by user on 18.08.17.
 */

public interface UnitOperations {
    Observable<Long> addUnit(Unit unit);

    Observable<Boolean> addUnits(List<Unit> units);
    Observable<List<Unit>> getUnits(Long rootId, String name);
    Observable<List<Unit>> getAllStaticUnits();

    Observable<Boolean> removeAllUnits();

    Observable<Boolean> removeUnit(Unit unit);
    Observable<Unit> updateUnit(Unit unit);
}
