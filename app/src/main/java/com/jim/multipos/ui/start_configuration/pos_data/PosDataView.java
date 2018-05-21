package com.jim.multipos.ui.start_configuration.pos_data;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.utils.CompletionMode;

public interface PosDataView extends BaseView {
    void setMode(CompletionMode mode);
    void checkPosDataCompletion();
    void onComplete();
}
