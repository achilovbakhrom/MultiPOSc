package com.jim.multipos.core;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.multipos.ui.HasComponent;
import com.jim.multipos.utils.validator.MultipleCallback;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;
import eu.inmite.android.lib.validations.form.FormValidator;


/**
 * Created by DEV on 27.07.2017.
 */

public abstract class BaseFragment extends Fragment implements HasSupportFragmentInjector{

    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;

    private Unbinder unbinder;

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        if (isAndroidInjectionEnabled()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                // Perform injection here before M, L (API 22) and below because onAttach(Context)
                // is not yet available at L.
                AndroidSupportInjection.inject(this);
            }
        }
        super.onAttach(activity);
    }

    protected boolean isAndroidInjectionEnabled() {
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        unbinder = ButterKnife.bind(this, view);
        rxConnections();
        init(savedInstanceState);
        return view;
    }

    protected abstract int getLayout();
    protected abstract void init(Bundle savedInstanceState);
    protected void rxConnections(){}
    @Override
    public void onAttach(Context context) {
        if (isAndroidInjectionEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Perform injection here for M (API 23) due to deprecation of onAttach(Activity).
                AndroidSupportInjection.inject(this);
            }
        }
        super.onAttach(context);
    }

    public AndroidInjector<Fragment> supportFragmentInjector() {
        return childFragmentInjector;
    }

    @SuppressWarnings("unchecked")
    public  <T> T getComponent(Class<T> componentType){
        return componentType.cast(((HasComponent<T>)getActivity()).getComponent());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();

        FormValidator.startLiveValidation(this, new MultipleCallback());
    }

    @Override
    public void onStop() {
        super.onStop();

        FormValidator.stopLiveValidation(this);
    }
    public boolean isValid() {
        return FormValidator.validate(this, new MultipleCallback());
    }

    public void onBarcodeScan(String barcode){

    }
}
