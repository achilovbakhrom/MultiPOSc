package com.jim.multipos.ui.first_configure.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.ui.first_configure.FirstConfigureActivity;
import com.jim.multipos.ui.first_configure.adapters.UnitAdapter;

import butterknife.BindView;

import static com.jim.multipos.ui.first_configure.Constants.UNITS_FRAGMENT_ID;

/**
 * Created by user on 10.10.17.
 */

public class UnitsFragment extends BaseFragment {
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

    @Override
    protected int getLayout() {
        return R.layout.units_settings;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (((FirstConfigureActivity) getActivity()).getPresenter().isNextButton()) {
            btnNext.setText(R.string.next);
        } else {
            btnNext.setText(R.string.save);
        }

        rvWeightUnits.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLengthUnits.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAreaUnits.setLayoutManager(new LinearLayoutManager(getContext()));
        rvVolumeUnits.setLayoutManager(new LinearLayoutManager(getContext()));

        ((FirstConfigureActivity) getActivity()).getPresenter().setupUnits(rvWeightUnits, rvLengthUnits, rvAreaUnits, rvVolumeUnits);

        RxView.clicks(btnNext).subscribe(aVoid -> {
            if (isValid()) {
                ((FirstConfigureActivity) getActivity()).getPresenter().setCompletedFragments(true, UNITS_FRAGMENT_ID);
                ((FirstConfigureActivity) getActivity()).getPresenter().openNextFragment();
            } else {
                ((FirstConfigureActivity) getActivity()).getPresenter().setCompletedFragments(false, UNITS_FRAGMENT_ID);
            }
        });

        RxView.clicks(btnRevert).subscribe(aVoid -> {
            ((FirstConfigureActivity) getActivity()).getPresenter().openPrevFragment();
        });
    }

    @Override
    protected void rxConnections() {

    }

    public void addWeightUnit(Unit unit) {
        ((UnitAdapter) rvWeightUnits.getAdapter()).addUnitItem(unit);
    }

    public void removeWeightUnit(Unit unit) {
        ((UnitAdapter) rvWeightUnits.getAdapter()).removeUnitItem(unit);
    }

    public void addLengthUnit(Unit unit) {
        ((UnitAdapter) rvLengthUnits.getAdapter()).addUnitItem(unit);
    }

    public void removeLengthUnit(Unit unit) {
        ((UnitAdapter) rvLengthUnits.getAdapter()).removeUnitItem(unit);
    }

    public void addAreaUnit(Unit unit) {
        ((UnitAdapter) rvAreaUnits.getAdapter()).addUnitItem(unit);
    }

    public void removeAreaUnit(Unit unit) {
        ((UnitAdapter) rvAreaUnits.getAdapter()).removeUnitItem(unit);
    }

    public void addVolumeUnit(Unit unit) {
        ((UnitAdapter) rvVolumeUnits.getAdapter()).addUnitItem(unit);
    }

    public void removeVolumeUnit(Unit unit) {
        ((UnitAdapter) rvVolumeUnits.getAdapter()).removeUnitItem(unit);
    }
}
