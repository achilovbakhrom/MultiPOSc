package com.jim.multipos.ui.products.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.products.ProductsActivity;
import com.jim.multipos.ui.products.di.ProductsComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by DEV on 31.08.2017.
 */

public class GlobalVendorsFragment extends Fragment { //BaseFragment {
    private Unbinder unbinder;
    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnLink)
    MpButton btnLink;
    @Inject
    ProductsActivity activity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.global_vendors, container, false);
        unbinder = ButterKnife.bind(this, view);
//        this.getComponent(ProductsComponent.class).inject(this);
        return view;
    }

    @OnClick(R.id.btnLink)
    public void onLink(){
        getFragmentManager().popBackStack();
    }
    @OnClick(R.id.btnCancel)
    public void onCancel(){
        getFragmentManager().popBackStack();
    }

}
