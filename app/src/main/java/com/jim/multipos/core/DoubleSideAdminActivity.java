package com.jim.multipos.core;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.jim.mpviews.MPCustomContainer;
import com.jim.multipos.R;
import com.jim.multipos.ui.admin_main_page.fragments.company.CompanyInfoFragment;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.EstablishmentFragment;
import com.jim.multipos.ui.admin_main_page.fragments.product.ProductAddFragment;
import com.jim.multipos.ui.admin_main_page.fragments.product.ProductsFragment;
import com.jim.multipos.utils.RxBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class DoubleSideAdminActivity extends BaseActivity{

    @Inject
    RxBus bus;

    @BindView(R.id.flLeftContainer)
    MPCustomContainer flLeftContainer;
    @BindView(R.id.flRightContainer)
    FrameLayout flRightContainer;

    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.fragmentInfo)
    RelativeLayout frInfo;


    protected boolean isVisible = false, entered = false;

    enum EditorCases{
        DRAGGING,
        OPEN_WHILE_CLOSE,
        OPEN_WHILE_OPEN,
        CLOSE_WHILE_CLOSE,
        CLOSE_WHILE_OPEN
    }
    EditorCases editorCases;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_double_admin_activity_layout);
        ButterKnife.bind(this);

        editorCases = EditorCases.OPEN_WHILE_CLOSE;
        setUpViews();

        flLeftContainer.setListener(new MPCustomContainer.MPCustomContainerListener() {
            @Override
            public void onClick() {

            }

            @Override
            public void onDrag(float x) {
                editorCases = EditorCases.DRAGGING;
                if(!isVisible)
                    flRightContainer.animate().translationX(x + flRightContainer.getWidth()).setDuration(0);
                else flRightContainer.animate().translationX(x).setDuration(0).setListener(null);

            }

            @Override
            public void startDragging() {

            }

            @Override
            public void stopDragging() {

            }

            @Override
            public void onClose() {
                if(!isVisible)
                    closeWhileClosed();
                else
                    closeWhileOpened();
            }

            @Override
            public void onOpen() {
                if(!isVisible)
                    openWhileClosed();
                else
                    openWhileOpened();
            }
        });
    }

    protected void openWhileClosed(){

        editorCases = EditorCases.OPEN_WHILE_CLOSE;

        flLeftContainer.animate().translationX(-flRightContainer.getWidth()).setDuration(150)
                .setUpdateListener(animation -> {
                    if(flLeftContainer.getX() == -flRightContainer.getWidth()){
                        setUpRV(true);
                        isVisible = true;
                        flLeftContainer.setTranslationX(0);
                        flLeftContainer.setLayoutParams(new RelativeLayout.LayoutParams((flLeftContainer
                                .getRootView().getWidth() - flRightContainer.getWidth()), ViewGroup.LayoutParams.MATCH_PARENT));
                    }

                })
                ;
        flRightContainer.animate().translationX(0).setDuration(150);
        isVisible = true;

    }
    protected void openWhileOpened(){

        editorCases = EditorCases.OPEN_WHILE_OPEN;
        isVisible = true;
//        flLeftContainer.animate().translationX(0).setDuration(150);
        flRightContainer.animate().translationX(0)
                .setUpdateListener(animation -> {
                    if(editorCases == EditorCases.OPEN_WHILE_OPEN){
                        flLeftContainer.setLayoutParams(new RelativeLayout
                                .LayoutParams((int) flRightContainer.getX(), ViewGroup.LayoutParams.MATCH_PARENT));
                    }
                }).setDuration(150);
    }

    protected void closeWhileOpened(){
        editorCases = EditorCases.CLOSE_WHILE_OPEN;

//        flLeftContainer.animate().translationX(flRightContainer.getWidth())
//                .setDuration(150)
//                .setListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                if(editorCases == EditorCases.CLOSE_WHILE_OPEN){
//                    flLeftContainer.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.MATCH_PARENT));
//                    flLeftContainer.setTranslationX(0);
//                    setUpRV(false);
//                    isVisible = false;
//                }
//            }
//        });
        isVisible = false;
        flRightContainer.animate().translationX(flRightContainer.getWidth())
                .setUpdateListener(animation -> {
                    if(editorCases == EditorCases.CLOSE_WHILE_OPEN){
                        flLeftContainer.setLayoutParams(new RelativeLayout.
                                LayoutParams((int) flRightContainer.getX(), ViewGroup.LayoutParams.MATCH_PARENT));
                    }
                })
                .setDuration(150);
        setUpRV(false);
    }
    protected void closeWhileClosed(){
        editorCases = EditorCases.CLOSE_WHILE_CLOSE;
        isVisible = false;
        flLeftContainer.animate().translationX(0).setDuration(150);
        flRightContainer.animate().translationX(flRightContainer.getWidth()).setDuration(150);
    }

    private void setUpRV(boolean visibility){
        Fragment fragment = getFragmentWithTag("left");
        if(fragment instanceof CompanyInfoFragment){
            ((CompanyInfoFragment) fragment).onCompanyEdit(visibility);
        }else if(fragment instanceof EstablishmentFragment)
            ((EstablishmentFragment) fragment).isEditorOpen(visibility);
        else if(fragment instanceof ProductsFragment)
            ((ProductsFragment) fragment).onCompanyEdit(visibility);
    }

    private void setUpViews(){
        flLeftContainer.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if(!entered) {
                entered = true;
                isVisible = false;
                flLeftContainer.setOpen(false);
                flRightContainer.animate().translationX(flRightContainer.getWidth()).setDuration(0);
                flLeftContainer.animate().translationX(0).setDuration(0);
                flLeftContainer.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
            }
        });
    }

    protected final void openCompanyFragment(Fragment leftFragment, Fragment rightFragment) {
        findViewById(R.id.company_fr_container).setVisibility(View.VISIBLE);
        findViewById(R.id.dashboard_fr_container).setVisibility(View.GONE);
        replaceFragmentWithTag(R.id.flLeftContainer, leftFragment, "left");
        replaceFragmentWithTag(R.id.flRightContainer, rightFragment, "right");
        tabLayout.setVisibility(View.GONE);
        frInfo.setVisibility(View.VISIBLE);
        restartViews();
    }

    protected final void openEntitiesFragment(){
        tabLayout.setVisibility(View.VISIBLE);
        frInfo.setVisibility(View.GONE);
        if(tabLayout.getTabCount() == 0) {
            tabLayout.addTab(tabLayout.newTab().setText("Products"));
            tabLayout.addTab(tabLayout.newTab().setText("Product Class"));
            tabLayout.addTab(tabLayout.newTab().setText("Discount"));
            tabLayout.addTab(tabLayout.newTab().setText("Service Fee"));
            tabLayout.addTab(tabLayout.newTab().setText("Unit"));
            tabLayout.addTab(tabLayout.newTab().setText("Product Category"));
            tabLayout.addTab(tabLayout.newTab().setText("Import"));
            tabLayout.addTab(tabLayout.newTab().setText("Export"));
            openEntityItems(0);
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tabLayout.getSelectedTabPosition()){
                        case 0:
                            openEntityItems(0);
                            break;

                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }else openEntityItems(0);
    }

    private void openEntityItems(int position){
        switch (position){
            case 0:
                replaceFragmentWithTag(R.id.flLeftContainer, new ProductsFragment(), "left");
                replaceFragmentWithTag(R.id.flRightContainer, new ProductAddFragment(), "right");
                break;
        }
    }

    public final void addFragmentOnLeft(Fragment fragment, String tag) {
        findViewById(R.id.company_fr_container).setVisibility(View.VISIBLE);
        findViewById(R.id.dashboard_fr_container).setVisibility(View.GONE);
        addFragmentWithTag(R.id.flLeftContainer, fragment, tag);
    }

    public final void addFragmentOnRight(Fragment fragment, String tag) {
        findViewById(R.id.company_fr_container).setVisibility(View.VISIBLE);
        findViewById(R.id.dashboard_fr_container).setVisibility(View.GONE);
        addFragmentWithTag(R.id.flRightContainer, fragment, tag);
    }

    public final void replaceFragmentOnLeft(Fragment fragment, String tag) {
        findViewById(R.id.company_fr_container).setVisibility(View.VISIBLE);
        findViewById(R.id.dashboard_fr_container).setVisibility(View.GONE);
        replaceFragmentWithTag(R.id.flLeftContainer, fragment, tag);
    }



    public final void replaceFragmentOnRight(Fragment fragment, String tag) {
        findViewById(R.id.company_fr_container).setVisibility(View.VISIBLE);
        findViewById(R.id.dashboard_fr_container).setVisibility(View.GONE);
        replaceFragmentWithTag(R.id.flRightContainer, fragment, tag);
    }


    public final Fragment getFragmentWithTag(String tag) {
        FragmentManager fm = getSupportFragmentManager();
        return fm.findFragmentByTag(tag);
    }


    protected final void openEditContainer() {
        if(!isVisible) {
            openWhileClosed();
            flLeftContainer.setOpen(true);
        }

    }

    protected final void closeEditContainer(){
        if(isVisible) {
            closeWhileOpened();
            flLeftContainer.setOpen(false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

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

    void restartViews(){
        isVisible = false;
        flLeftContainer.setOpen(false);
        flRightContainer.animate().translationX(flRightContainer.getWidth()).setDuration(0);
        flLeftContainer.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        flLeftContainer.animate().translationX(0).setDuration(0);
        editorCases = EditorCases.DRAGGING;
    }
}
