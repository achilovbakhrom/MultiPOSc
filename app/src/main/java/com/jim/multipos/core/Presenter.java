package com.jim.multipos.core;

import android.os.Bundle;

/**
 * Created by bakhrom on 10/3/17.
 */

public interface Presenter {
    void onStart();
    void onResume();
    void onPause();
    void onSaveInstanceState(Bundle bundle);
    void onDestroy();
}
