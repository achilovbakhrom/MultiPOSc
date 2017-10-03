package com.jim.multipos.ui.first_configure.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.intosystem.TitleDescription;
import com.jim.multipos.ui.first_configure.FirstConfigureActivity;
import com.jim.multipos.ui.first_configure.di.FirstConfigureActivityComponent;
import com.jim.multipos.ui.first_configure.adapters.SettingsAdapter;
import com.jim.multipos.ui.first_configure.presenters.FirstConfigureLeftSidePresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstConfigureLeftSideFragment extends BaseFragment implements FirstConfigureLeftSideFragmentView, SettingsAdapter.OnClick {
    /*@Inject
    PosFragmentManager posFragmentManager;*/
    @Inject
    FirstConfigureLeftSidePresenter presenter;
    /*@Inject
    SettingsAdapter adapter;*/
    @Inject
    FirstConfigureActivity activity;
    @BindView(R.id.rvSettings)
    RecyclerView rvSettings;
    private SettingsAdapter settingsAdapter;

    public FirstConfigureLeftSideFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.start_configuration_fragment, container, false);

        this.getComponent(FirstConfigureActivityComponent.class).inject(this);

        presenter.init(this);
        ButterKnife.bind(this, view);
        presenter.setAdapterData();

        return view;
    }


    public void showCheckBoxIndicator(int position, boolean checked, int nextPosition) {
        settingsAdapter.onFragmentNextClickListener(position, checked, nextPosition);
    }

    public void showCheckBoxIndicator(int position, boolean checked) {
        settingsAdapter.changeCheckBoxIndicator(position, checked);
    }

    @Override
    public void showRecyclerView(List<TitleDescription> titleDescriptionList) {
        settingsAdapter = new SettingsAdapter(getContext(), this);
        settingsAdapter.setData(titleDescriptionList, this);
        rvSettings.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSettings.setAdapter(settingsAdapter);
    }

    @Override
    public void itemClick(int position) {
        activity.checkCurrentAndChangePositonFragment(position);
    }
}
