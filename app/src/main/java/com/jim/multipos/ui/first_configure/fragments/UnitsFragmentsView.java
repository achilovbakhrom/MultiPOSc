package com.jim.multipos.ui.first_configure.fragments;

import com.jim.mpviews.MpCheckbox;
import com.jim.multipos.data.db.model.unit.Unit;

import java.util.List;

/**
 * Created by user on 01.08.17.
 */

public interface UnitsFragmentsView {
    void openNextFragment();
    void showUnitError(String error);
    void showWeightRV(List<Unit> units, String[] weightProperty);
    void showLengthRV(List<Unit> units, String[] lengthProperty);
    void showAreaRV(List<Unit> units, String[] areaProperty);
    void showVolumeRV(List<Unit> units, String[] volumeProperty);
    void unitAdded(int position, int whichAdapter);
    void unitRemoved(int position, int whichAdapter);
}
