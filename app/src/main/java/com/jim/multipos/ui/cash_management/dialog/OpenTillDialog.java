package com.jim.multipos.ui.cash_management.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.multipos.R;

/**
 * Created by Sirojiddin on 13.01.2018.
 */

public class OpenTillDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.open_till_dialog, container, false);





        return view;
    }
}
