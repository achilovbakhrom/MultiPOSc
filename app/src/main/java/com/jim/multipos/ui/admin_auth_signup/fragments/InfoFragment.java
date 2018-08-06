package com.jim.multipos.ui.admin_auth_signup.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends BaseFragment {


    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    protected int getLayout() {
        return 0;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

}
