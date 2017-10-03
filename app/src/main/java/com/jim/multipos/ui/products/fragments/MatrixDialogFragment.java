package com.jim.multipos.ui.products.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by DEV on 18.09.2017.
 */

public class MatrixDialogFragment extends DialogFragment {

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.matrix_dialog_container, container,
                false);
        unbinder = ButterKnife.bind(this, rootView);

        getChildFragmentManager().beginTransaction().add(R.id.flDialogContainer, new MatrixFragment()).commit();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void onResume()
    {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.thousand_ninety_dp);
        int height = getResources().getDimensionPixelSize(R.dimen.six_hundred_ninety_dp);
        Window window = getDialog().getWindow();
        window.setLayout(width, height);
        window.setBackgroundDrawableResource(R.drawable.rounded_bg);
    }
}
