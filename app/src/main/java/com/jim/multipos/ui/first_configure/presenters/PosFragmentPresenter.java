package com.jim.multipos.ui.first_configure.presenters;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.ui.first_configure.common.BaseFragmentPresenterFirstConfig;
import com.jim.multipos.ui.first_configure.fragments.PosDetailsFragmentView;

import java.util.HashMap;

/**
 * Created by user on 01.08.17.
 */

public interface PosFragmentPresenter extends BaseFragmentPresenterFirstConfig<PosDetailsFragmentView> {
    boolean isCompleteData(HashMap<String, String> data);
    void openNextFragment(HashMap<String, String> data);
}
