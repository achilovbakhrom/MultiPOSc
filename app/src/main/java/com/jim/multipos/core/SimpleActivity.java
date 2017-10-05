package com.jim.multipos.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jim.multipos.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bakhrom on 10/4/17.
 */

public abstract class SimpleActivity extends BaseActivity {

    protected static final int WITHOUT_TOOLBAR = -1;

    @BindView(R.id.llToolbar)
    LinearLayout toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_simple_activity);
        ButterKnife.bind(this);
        if (getToolbar() == WITHOUT_TOOLBAR) {
            toolbar.setVisibility(View.GONE);
        } else {
            LayoutInflater.from(this).inflate(getToolbar(), toolbar, true);
        }
    }

    protected abstract int getToolbar();

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
