package com.jim.multipos.core;

import android.os.Bundle;

import javax.annotation.Nullable;

/**
 * Created by bakhrom on 10/3/17.
 */

public class BasePresenterImpl<T extends BaseView> implements Presenter {

    protected final T view;

    protected BasePresenterImpl(T t) {
        this.view = t;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onSaveInstanceState(@Nullable Bundle bundle) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onCreateView(Bundle bundle) {

    }
}
