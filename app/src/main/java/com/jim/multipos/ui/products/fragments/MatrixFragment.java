package com.jim.multipos.ui.products.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpSwitcher;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by DEV on 19.09.2017.
 */

public class MatrixFragment extends Fragment { //BaseFragment implements AttributeAddClickListener {
    @BindView(R.id.switcherMatrix)
    MpSwitcher switcherMatrix;
    @BindView(R.id.llChildSearchView)
    LinearLayout llChildSearchView;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.matrix_fragment, container,
                false);
        unbinder = ButterKnife.bind(this, rootView);
        switcherMatrix.setLeft(true);
        checkMatrix();
        RxView.clicks(switcherMatrix).subscribe(event ->
        {
                    checkMatrix();
        });
        getChildFragmentManager().beginTransaction().add(R.id.flMatrixContainer, new MatrixParentOptionsFragment()).commit();
        return rootView;
    }

    private void checkMatrix() {
        if (switcherMatrix.isRight()) {
            llChildSearchView.setVisibility(View.VISIBLE);
            getChildFragmentManager().beginTransaction().replace(R.id.flMatrixContainer, new MatrixChildOptionsFragment()).commit();
        } else {
            llChildSearchView.setVisibility(View.GONE);
            getChildFragmentManager().beginTransaction().replace(R.id.flMatrixContainer, new MatrixParentOptionsFragment()).commit();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void onAttributeAddClick() {
        getChildFragmentManager().beginTransaction().add(R.id.flMatrixContainer, new AddMatrixAttributesFragment()).commit();
    }
}
