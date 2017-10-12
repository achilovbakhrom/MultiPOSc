package com.jim.multipos.ui.first_configure.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.first_configure.FirstConfigureActivity;
import com.jim.multipos.ui.first_configure.FirstConfigurePresenterImpl;
import com.jim.multipos.ui.first_configure.adapters.SettingsAdapter;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.FirstConfigureActivityEvent;

import javax.inject.Inject;

import butterknife.BindView;

import static com.jim.multipos.ui.first_configure.Constants.COMPLETED_FRAGMENTS;
import static com.jim.multipos.ui.first_configure.Constants.LEFT_SIDE_FRAGMENT_CLICKED;

/**
 * Created by user on 10.10.17.
 */

public class LeftSideFragment extends BaseFragment implements SettingsAdapter.OnClickListener {
    @Inject
    RxBusLocal rxBusLocal;
    @BindView(R.id.rvSettings)
    RecyclerView rvSettings;
    private SettingsAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.start_configuration_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initRecyclerView(((FirstConfigurePresenterImpl) ((FirstConfigureActivity) getActivity()).getPresenter()).getCompletedFragments());
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

    public void updateAdapter(int position) {
        adapter.updateAdapter(position);
    }

    @Override
    public void onClick(int position) {
        ((FirstConfigureActivity) getActivity()).getPresenter().openNextFragment(position);
    }
}
