package com.jim.multipos.ui.mainpospage.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.multipos.R;

/**
 * Created by developer on 24.08.2017.
 */

public class ProductInfoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.product_info_fragment, container, false);
    }

}
