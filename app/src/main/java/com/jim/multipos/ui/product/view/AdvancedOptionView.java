package com.jim.multipos.ui.product.view;

import com.jim.multipos.data.db.model.unit.Unit;

import java.util.List;

/**
 * Created by DEV on 06.09.2017.
 */

public interface AdvancedOptionView {
    void setSubUnitsRecyclerView(List<Unit> subUnitsLists);
    void updateSubUnits();
    void setFields(Unit mainUnit, String description);
    void popBackStack();
    void setError(String nameError, String qtyError);
}
