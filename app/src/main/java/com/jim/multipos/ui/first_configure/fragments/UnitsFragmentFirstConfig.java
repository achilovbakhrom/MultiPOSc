package com.jim.multipos.ui.first_configure.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.ui.first_configure.Constants;
import com.jim.multipos.ui.first_configure.FirstConfigureActivity;
import com.jim.multipos.ui.first_configure.adapters.UnitAdapter;
import com.jim.multipos.ui.first_configure.common.BaseFragmentFirstConfig;
import com.jim.multipos.ui.first_configure.di.FirstConfigureActivityComponent;
import com.jim.multipos.ui.first_configure.presenters.UnitsFragmentPresenter;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnitsFragmentFirstConfig extends Fragment { //BaseFragmentFirstConfig implements UnitsFragmentsView, UnitAdapter.OnClick {
    @BindView(R.id.btnNext)
    MpButton btnNext;
    @BindView(R.id.btnRevert)
    MpButton btnRevert;
    @BindView(R.id.rvWeightUnits)
    RecyclerView rvWeightUnits;
    @BindView(R.id.rvLengthUnits)
    RecyclerView rvLengthUnits;
    @BindView(R.id.rvAreaUnits)
    RecyclerView rvAreaUnits;
    @BindView(R.id.rvVolumeUnits)
    RecyclerView rvVolumeUnits;
    @Inject
    UnitsFragmentPresenter presenter;
    @Inject
    FirstConfigureActivity activity;
    private UnitAdapter weightAdapter;
    private UnitAdapter lengthAdapter;
    private UnitAdapter areaAdapter;
    private UnitAdapter volumeAdapter;

    public UnitsFragmentFirstConfig() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.units_settings, container, false);

//        this.getComponent(FirstConfigureActivityComponent.class).inject(this);
//        presenter.init(this);

        ButterKnife.bind(this, view);

        presenter.setWeightRV();
        presenter.setLengthRV();
        presenter.setAreaRV();
        presenter.setVolumeRV();

        if (FirstConfigureActivity.SAVE_BUTTON_MODE == activity.getButtonMode()) {
            btnNext.setText(R.string.save);
        } else if (FirstConfigureActivity.NEXT_BUTTON_MODE == activity.getButtonMode()) {
            btnNext.setText(R.string.next);
        }

        RxView.clicks(btnNext).subscribe(aVoid -> {
            presenter.openNextFragment();
        });

        RxView.clicks(btnRevert).subscribe(aVoid -> {
            activity.finish();
        });

        return view;
    }

    public boolean checkData() {
        return presenter.isCompleteData();
    }

    public HashMap<String, String> getData() {
        return null;
    }

    public void openNextFragment() {
        activity.openNextFragment();
    }

    public void showUnitError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    public void showWeightRV(List<Unit> units, String[] weightProperty) {
//        weightAdapter = new UnitAdapter(units, weightProperty,(int) Constants.WEIGHT,this);
        rvWeightUnits.setLayoutManager(new LinearLayoutManager(getContext()));
        rvWeightUnits.setAdapter(weightAdapter);
    }

    public void showLengthRV(List<Unit> units, String[] lengthProperty) {
//        lengthAdapter = new UnitAdapter(units, lengthProperty, (int) Constants.LENGTH, this);
        rvLengthUnits.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLengthUnits.setAdapter(lengthAdapter);
    }

    public void showAreaRV(List<Unit> units, String[] areaProperty) {
//        areaAdapter = new UnitAdapter(units, areaProperty, (int) Constants.AREA, this);
        rvAreaUnits.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAreaUnits.setAdapter(areaAdapter);
    }

    public void showVolumeRV(List<Unit> units, String[] volumeProperty) {
//        volumeAdapter = new UnitAdapter(units, volumeProperty, (int) Constants.VOLUME, this);
        rvVolumeUnits.setLayoutManager(new LinearLayoutManager(getContext()));
        rvVolumeUnits.setAdapter(volumeAdapter);
    }

    public void add(int position, int whichAdapter) {
        presenter.addUnit(position, whichAdapter);
    }

    public void remove(int position, int whichAdapter) {
        presenter.removeUnit(position, whichAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

//        hideKeyboard();
    }

    public void unitAdded(int position, int whichAdapter) {
       itemChanged(position, whichAdapter);
    }

    public void unitRemoved(int position, int whichAdapter) {
        itemChanged(position, whichAdapter);
    }

    private void itemChanged(int position, int whichAdapter) {
        if (whichAdapter == Constants.WEIGHT) {
            weightAdapter.notifyItemChanged(position);
        } else if (whichAdapter == Constants.LENGTH) {
            lengthAdapter.notifyItemChanged(position);
        } else if (whichAdapter == Constants.AREA) {
            areaAdapter.notifyItemChanged(position);
        } else if (whichAdapter == Constants.VOLUME) {
            volumeAdapter.notifyItemChanged(position);
        }
    }

    public void saveData() {
        presenter.saveData();
    }
}
