package com.jim.multipos.core;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jim.multipos.R;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.admin_main_page.CompanyEditor;

import javax.inject.Inject;

import butterknife.ButterKnife;

public abstract class DoubleSideAdminActivity extends BaseActivity implements View.OnTouchListener {

//    @BindView(R.id.company_container)
//    LinearLayout company_container;
//    @BindView(R.id.dashboard_container)
//    LinearLayout dashboard_container;
@Inject
RxBus bus;

    int x;
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
    boolean isVisible = false, moveable = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_double_admin_activity_layout);
        ButterKnife.bind(this);

        findViewById(R.id.flLeftContainer).setOnTouchListener(this);
    }

    protected final void openCompanyFragment(Fragment leftFragment, Fragment rightFragment) {
        findViewById(R.id.company_fr_container).setVisibility(View.VISIBLE);
        findViewById(R.id.dashboard_fr_container).setVisibility(View.GONE);
        replaceFragment(R.id.flLeftContainer, leftFragment);
        replaceFragment(R.id.flRightContainer, rightFragment);

    }

    protected final void openEditContainer(int id) {
        View v = findViewById(id);
        if (!isVisible) {
            openView(v);
            bus.send(new CompanyEditor(true));
        }
    }

    protected final void closeEditContainer(int id) {
        View v = findViewById(id);
        if (isVisible) {
            closeView(v);
            bus.send(new CompanyEditor(false));
        }
    }

    private void closeView(View view) {
        isVisible = false;
        view.animate()
                .translationX(-view.getWidth())
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                }).start();
    }

    private void openView(View view) {
        isVisible = true;
        view.animate()
                .translationX(0)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        view.setVisibility(View.VISIBLE);
                    }
                }).start();
    }

    protected final void openDashboardFragment(Fragment top, Fragment left, Fragment right) {
        findViewById(R.id.company_fr_container).setVisibility(View.GONE);
        findViewById(R.id.dashboard_fr_container).setVisibility(View.VISIBLE);
        replaceFragment(R.id.dashboard_topContainer, top);
        replaceFragment(R.id.dashboard_leftContainer, left);
        replaceFragment(R.id.dashboard_rightContainer, right);
    }

    protected final void openEstablishmentFragment(Fragment left, Fragment right) {
        openCompanyFragment(left, right);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) (v.getX() - event.getRawX());
                return true;

            case MotionEvent.ACTION_UP:
                float currX = v.getWidth() + v.getX();
                if (currX / v.getWidth() < 0.70) {
                    closeView(v);
                } else
                    openView(v);
                return true;
            case MotionEvent.ACTION_MOVE:
                moveable = !((event.getRawX() + x) > 0);
                if (moveable) {
                    v.animate()
                            .x(event.getRawX() + x)
                            .setDuration(0)
                            .start();
                }
                return true;
            default:
                return false;
        }
    }
}
