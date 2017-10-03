package com.jim.multipos.ui.first_configure.fragments;

import com.jim.multipos.data.db.model.intosystem.TitleDescription;
import com.jim.multipos.ui.first_configure.common.BaseFragmentFirstConfig;

import java.util.List;

/**
 * Created by user on 31.07.17.
 */

public interface FirstConfigureLeftSideFragmentView {
    void showRecyclerView(List<TitleDescription> titleDescriptionList);
}