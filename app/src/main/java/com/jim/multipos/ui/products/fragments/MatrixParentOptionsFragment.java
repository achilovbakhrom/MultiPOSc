package com.jim.multipos.ui.products.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.multipos.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by DEV on 19.09.2017.
 */

public class MatrixParentOptionsFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.ivAddFirstAttribute)
    ImageView ivAddFirstAttribute;
    private AttributeAddClickListener callback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.matrix_parent_fragment, container,
                false);
        unbinder = ButterKnife.bind(this, rootView);
        callback = (AttributeAddClickListener) getParentFragment();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.ivAddFirstAttribute)
    public void onAttributeAdd(){
        callback.onAttributeAddClick();
    }
}
