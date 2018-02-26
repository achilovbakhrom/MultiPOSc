package com.jim.multipos.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by developer on 05.10.2017.
 */

public abstract class DoubleSideActivity extends BaseActivity{

    @BindView(R.id.toolbar)
    MpToolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_double_activity_layout);
        ButterKnife.bind(this);
        toolbar.setMode(getToolbarMode());
    }

    protected abstract int getToolbarMode();


    protected final void addFragmentToLeft(Fragment fragment){addFragment(R.id.flLeftContainer,fragment);}
    protected final void addFragmentWithTagToLeft(Fragment fragment, String tag){addFragmentWithTag(R.id.flLeftContainer,fragment, tag);}
    protected final void addFragmentWithBackStackToLeft(Fragment fragment) {addFragmentWithBackStack(R.id.flLeftContainer,fragment);}
    protected final void addFragmentToRight(Fragment fragment){addFragment(R.id.flRightContainer,fragment);}
    protected final void addFragmentWithTagToRight(Fragment fragment, String tag){addFragmentWithTag(R.id.flRightContainer,fragment, tag);}
    protected final void addFragmentWithBackStackToRight(Fragment fragment) {addFragmentWithBackStack(R.id.flRightContainer,fragment);}
    protected final void addFragmentToTopRight(Fragment fragment){addFragment(R.id.flRightTop,fragment);}
    protected final void addFragmentToRightWrap(Fragment fragment){addFragment(R.id.flRightWrap,fragment);}
    protected final void addFragmentWithTagToTopRight(Fragment fragment, String tag){addFragmentWithTag(R.id.flRightTop,fragment, tag);}
    protected final void addFragmentWithBackStackToTopRight(Fragment fragment) {addFragmentWithBackStack(R.id.flRightTop,fragment);}
    protected final void addFragmentToFull(Fragment fragment){addFragment(R.id.flFullContainer,fragment);}
    protected final void addFragmentWithTagToFull(Fragment fragment, String tag){addFragmentWithTag(R.id.flFullContainer,fragment, tag);}
    protected final void addFragmentWithTagToFullWindow(Fragment fragment, String tag){addFragmentWithTag(R.id.flFull,fragment, tag);}
    protected final void addFragmentWithBackStackToFull(Fragment fragment) {addFragmentWithBackStack(R.id.flFullContainer,fragment);}

    protected final void replaceFragmentToLeft(Fragment fragment){replaceFragment(R.id.flLeftContainer,fragment);}
    protected final void replaceFragmentToRight(Fragment fragment){replaceFragment(R.id.flRightContainer,fragment);}
    protected final void replaceFragmentToTopRight(Fragment fragment){replaceFragment(R.id.flRightTop,fragment);}
    protected final void replaceFragmentToRightWrap(Fragment fragment){replaceFragment(R.id.flRightWrap,fragment);}
    protected final void replaceFragmentToFull(Fragment fragment){replaceFragment(R.id.flFullContainer,fragment);}

    protected final void popFragment(Fragment fragment){
        activityFragmentManager.beginTransaction().remove(fragment).commit();
    }

    protected final void popFragmentFromLeft(){
        Fragment fragment = activityFragmentManager.findFragmentById(R.id.flLeftContainer);
        if(fragment!=null)
            getSupportFragmentManager().beginTransaction().
                    remove(fragment).commit();
    }
    protected final void popFragmentFromRight(){
        Fragment fragment = activityFragmentManager.findFragmentById(R.id.flRightContainer);
        if(fragment!=null)
            getSupportFragmentManager().beginTransaction().
                    remove(fragment).commit();
    }
    protected final void popFragmentFromRightTop(){
        Fragment fragment = activityFragmentManager.findFragmentById(R.id.flRightTop);
        if(fragment!=null)
            getSupportFragmentManager().beginTransaction().
                    remove(fragment).commit();
    }
    protected final void popFragmentFromFull(){
        Fragment fragment = activityFragmentManager.findFragmentById(R.id.flFullContainer);
        if(fragment!=null)
            getSupportFragmentManager().beginTransaction().
                    remove(fragment).commit();
    }

    protected final void popFragmentFromRightWrap(){
        Fragment fragment = activityFragmentManager.findFragmentById(R.id.flRightWrap);
        if(fragment!=null)
            getSupportFragmentManager().beginTransaction().
                    remove(fragment).commit();
    }

    public final Fragment getCurrentFragmentLeft(){
        return  activityFragmentManager.findFragmentById(R.id.flLeftContainer);
    }


    public final Fragment getCurrentFragmentRight(){
        return  activityFragmentManager.findFragmentById(R.id.flRightContainer);

    }

    public final Fragment getCurrentFragmentRightTop(){
        return  activityFragmentManager.findFragmentById(R.id.flRightTop);

    }

    protected final Fragment getCurrentFragmentFull(){
        return  activityFragmentManager.findFragmentById(R.id.flFullContainer);

    }
    protected final void popBackStack() {
        activityFragmentManager.popBackStack();
    }

    protected final int getFragmentCount() {
        return activityFragmentManager.getBackStackEntryCount();
    }

    protected final Fragment getFragmentByTag(String tag) {
        return activityFragmentManager.findFragmentByTag(tag);
    }
}
