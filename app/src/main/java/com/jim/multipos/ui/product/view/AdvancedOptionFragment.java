package com.jim.multipos.ui.product.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.RecyclerViewWithMaxHeight;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.ui.product.adapter.SubUnitsAdapter;
import com.jim.multipos.ui.product.presenter.AdvancedOptionPresenter;
import com.jim.multipos.utils.managers.PosFragmentManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by DEV on 31.08.2017.
 */
public class AdvancedOptionFragment extends BaseFragment implements AdvancedOptionView {
    private Unbinder unbinder;
    @Inject
    AdvancedOptionPresenter presenter;
    @Inject
    PosFragmentManager posFragmentManager;
    @BindView(R.id.tvMainUnit)
    TextView tvMainUnit;
    @BindView(R.id.etMainUnitQty)
    MpEditText etMainUnitQty;
    @BindView(R.id.etProductDescription)
    EditText etProductDescription;
    @BindView(R.id.etSubUnitName)
    MpEditText etSubUnitName;
    @BindView(R.id.rvSubUnits)
    RecyclerViewWithMaxHeight rvSubUnits;
    @BindView(R.id.btnSaveOptions)
    MpButton btnSaveOptions;
    @BindView(R.id.chbSubUnitActive)
    MpCheckbox chbSubUnitActive;
    private SubUnitsAdapter subUnitsAdapter;
    private LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_advance_options, container, false);
        unbinder = ButterKnife.bind(this, view);
        layoutManager = new LinearLayoutManager(getContext());
        presenter.init(this);
        RxView.clicks(btnSaveOptions).subscribe(o ->
                presenter.saveOptions(etProductDescription.getText().toString())
        );
        return view;
    }

    @Override
    protected int getLayout() {
        return 0;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void rxConnections() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.btnBackTo)
    public void onBack() {
        posFragmentManager.popBackStack();
    }

    @OnClick(R.id.ivAddSubUnit)
    public void addSubUnit() {
        presenter.setSubUnits(etSubUnitName.getText().toString(), etMainUnitQty.getText().toString(), chbSubUnitActive.isCheckboxChecked());
    }

    @Override
    public void setSubUnitsRecyclerView(List<Unit> subUnitsLists) {
        subUnitsAdapter = new SubUnitsAdapter(subUnitsLists, presenter);
        rvSubUnits.setLayoutManager(layoutManager);
        rvSubUnits.setAdapter(subUnitsAdapter);
    }

    @Override
    public void updateSubUnits() {
        subUnitsAdapter.notifyDataSetChanged();
    }

    @Override
    public void setFields(Unit mainUnit, String description) {
        tvMainUnit.setText(mainUnit.getAbbr());
        etProductDescription.setText(description);
    }

    @Override
    public void popBackStack() {
        posFragmentManager.popBackStack();
    }

    @Override
    public void setError(String nameError, String qtyError) {
        if (!nameError.equals(""))
            etSubUnitName.setError(nameError);
        if (!qtyError.equals(""))
            etMainUnitQty.setError(qtyError);
    }

}
