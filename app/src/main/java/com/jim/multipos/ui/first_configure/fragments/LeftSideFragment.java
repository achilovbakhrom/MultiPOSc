package com.jim.multipos.ui.first_configure.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.first_configure.adapters.SettingsAdapter;
import com.jim.multipos.ui.first_configure.adapters.SettingsAdapterOld;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.FirstConfigureActivityEvent;

import javax.inject.Inject;

import butterknife.BindView;

import static com.jim.multipos.ui.first_configure.Constants.BUTTON_STATE;
import static com.jim.multipos.ui.first_configure.Constants.COMPLETED_FRAGMENTS;
import static com.jim.multipos.ui.first_configure.Constants.LEFT_SIDE_FRAGMENT_CLICKED;
import static com.jim.multipos.ui.first_configure.Constants.LEFT_SIDE_FRAGMENT_OPENED;

/**
 * Created by user on 10.10.17.
 */

public class LeftSideFragment extends BaseFragment implements SettingsAdapter.OnClickListener {
    @Inject
    RxBusLocal rxBusLocal;
    @BindView(R.id.rvSettings)
    RecyclerView rvSettings;
    private SettingsAdapter adapter;

    public static LeftSideFragment newInstance(boolean[] isCompletedFragments) {
        Bundle args = new Bundle();
        args.putBooleanArray(COMPLETED_FRAGMENTS, isCompletedFragments);

        LeftSideFragment fragment = new LeftSideFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.start_configuration_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        //rxBusLocal.send(new FirstConfigureActivityEvent(LEFT_SIDE_FRAGMENT_OPENED));
        Bundle bundle = getArguments();
        boolean[] isCompletedFragments = null;

        if (bundle != null) {
            isCompletedFragments = bundle.getBooleanArray(COMPLETED_FRAGMENTS);
        }

        initRecyclerView(isCompletedFragments);
    }

    @Override
    protected void rxConnections() {

    }

    private void initRecyclerView(boolean[] isCompletedFragments) {
        String[] titles = getStringArray(R.array.start_configuration_title);
        String[] descriptions = getStringArray(R.array.start_configuration_description);

        adapter = new SettingsAdapter(getContext(), this, titles, descriptions, isCompletedFragments);
        rvSettings.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSettings.setAdapter(adapter);
    }

    private String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    @Override
    public void onClick(int position) {
        rxBusLocal.send(new FirstConfigureActivityEvent(LEFT_SIDE_FRAGMENT_CLICKED, position));
    }
}
