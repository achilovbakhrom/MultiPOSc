package com.jim.multipos.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bakhrom on 10/4/17.
 */

public abstract class SimpleActivity extends BaseActivity {

    protected static final int WITHOUT_TOOLBAR = -1;
    protected static final int WITH_TOOLBAR = 1;

    @BindView(R.id.toolbar)
    public MpToolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_simple_activity);
        ButterKnife.bind(this);
        if (getToolbar() == WITHOUT_TOOLBAR) {
            toolbar.setVisibility(View.GONE);
        } else {
            toolbar.setMode(getToolbarMode());

        }
    }

    protected abstract int getToolbar();

    protected abstract int getToolbarMode();


    public final void addFragment(Fragment fragment) {
        addFragment(R.id.flMain, fragment);
    }

    public final void replaceFragment(Fragment fragment) {
        replaceFragment(R.id.flMain, fragment);
    }

    protected final void popBackStack() {
        activityFragmentManager.popBackStack();
    }
}
