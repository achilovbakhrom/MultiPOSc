package com.jim.multipos.ui.first_configure;

import com.jim.multipos.core.Presenter;
import java.util.Map;

/**
 * Created by user on 07.10.17.
 */

public interface FirstConfigurePresenter extends Presenter {
    void checkPosDetailsFragmentData(Map<String, String> data);
    void getLeftSideFragmentData();
}
