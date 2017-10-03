package com.jim.multipos.ui.first_configure.presenters;

import com.jim.mpviews.MpCheckbox;
import com.jim.multipos.ui.first_configure.common.BaseFragmentPresenterFirstConfig;
import com.jim.multipos.ui.first_configure.fragments.FirstConfigureLeftSideFragmentView;
import com.jim.multipos.ui.first_configure.fragments.UnitsFragmentsView;

import java.util.ArrayList;

/**
 * Created by user on 05.08.17.
 */

public interface UnitsFragmentPresenter extends BaseFragmentPresenterFirstConfig<UnitsFragmentsView> {
    boolean isCompleteData();
    void addUnit(int position, int whichAdapter);
    void removeUnit(int position, int whichAdapter);
    void openNextFragment();
    void setWeightRV();
    void setLengthRV();
    void setAreaRV();
    void setVolumeRV();
    void setData();
}
