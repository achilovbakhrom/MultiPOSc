package com.jim.multipos.ui.start_configuration.basics;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.utils.CompletionMode;

public interface BasicsView extends BaseView {
    void checkPosDataCompletion();
    void setMode(CompletionMode mode);
}
