package com.jim.multipos.ui.admin_main_page.fragments.establishment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.admin_main_page.AdminMainPageActivity;
import com.jim.multipos.ui.admin_main_page.fragments.company.presenter.CompanyFragmentPresenter;
import com.jim.multipos.ui.admin_main_page.fragments.company.presenter.CompanyFragmentView;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.model.Establishment;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.model.EstablishmentPos;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.presenter.EstablishmentFragmentPresenter;
import com.jim.multipos.ui.admin_main_page.fragments.establishment.presenter.EstablishmentFragmentView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class EstablishmentAddFragment extends BaseFragment implements EstablishmentFragmentView {

    @BindView(R.id.etName)
    MpEditText etEstablishmentName;
    @BindView(R.id.etAddress)
    MpEditText etEstablishmentAddress;
    @BindView(R.id.etPhone)
    MpEditText etEstablishmentPhone;
    @BindView(R.id.etStock)
    MpEditText etEstablishmentStock;
    @BindView(R.id.etMainStock)
    MpEditText etEstablishmentMainStock;
    @BindView(R.id.etDescription)
    EditText etEstablishmentDescription;
    @BindView(R.id.tvEdit)
    TextView tvEdit;

    @BindView(R.id.isActive)
    Switch isActive;

    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.btnEdit)
    Button btnEdit;

    @Inject
    EstablishmentFragmentPresenter presenter;

    @OnClick(R.id.btnDelete)
    public void onDelete(View view) {
        if (btnDelete.getText().equals(getString(R.string.delete)))
            onDeleteClicked();
        else if (btnDelete.getText().equals(getString(R.string.cancel)))
            onCancelClicked();
    }

    @OnClick(R.id.btnEdit)
    public void onEdit(View view) {
        if (btnEdit.getText().equals(getString(R.string.edit)))
            onEditClicked();
        else if (btnEdit.getText().equals(getString(R.string.save)))
            onSaveClicked();
    }


    @Override
    protected int getLayout() {
        return R.layout.admin_establishment_add_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setEditable(false);
        presenter.startObserving();
    }


    private void onDeleteClicked() {

    }

    private void onCancelClicked() {
        setEditable(false);
        btnEdit.setText(getString(R.string.edit));
        btnDelete.setText(getString(R.string.delete));
        btnDelete.setTextColor(ContextCompat.getColor(getContext(), R.color.colorRed));
    }

    private void onSaveClicked() {
        if(getActivity() instanceof AdminMainPageActivity && getActivity()!=null)
            ((AdminMainPageActivity) getActivity()).closeEditor();
    }

    private void onEditClicked() {
        setEditable(true);
        btnEdit.setText(getString(R.string.save));
        btnDelete.setText(getString(R.string.cancel));
        btnDelete.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreySecondaryDark));
    }

    public void setEditable(boolean mode) {
        if (mode) {
            etEstablishmentName.setKeyListener((KeyListener) etEstablishmentName.getTag());
            etEstablishmentAddress.setKeyListener((KeyListener) etEstablishmentAddress.getTag());
            etEstablishmentPhone.setKeyListener((KeyListener) etEstablishmentPhone.getTag());
            etEstablishmentStock.setKeyListener((KeyListener) etEstablishmentStock.getTag());
            etEstablishmentMainStock.setKeyListener((KeyListener) etEstablishmentMainStock.getTag());
            etEstablishmentDescription.setKeyListener((KeyListener) etEstablishmentDescription.getTag());
            isActive.setClickable(true);
            btnEdit.setText(getText(R.string.save));
        } else {
            etEstablishmentName.setTag(etEstablishmentName.getKeyListener());
            etEstablishmentName.setKeyListener(null);
            etEstablishmentAddress.setTag(etEstablishmentAddress.getKeyListener());
            etEstablishmentAddress.setKeyListener(null);
            etEstablishmentPhone.setTag(etEstablishmentPhone.getKeyListener());
            etEstablishmentPhone.setKeyListener(null);
            etEstablishmentStock.setTag(etEstablishmentStock.getKeyListener());
            etEstablishmentStock.setKeyListener(null);
            etEstablishmentMainStock.setTag(etEstablishmentMainStock.getKeyListener());
            etEstablishmentMainStock.setKeyListener(null);
            etEstablishmentDescription.setTag(etEstablishmentDescription.getKeyListener());
            etEstablishmentDescription.setKeyListener(null);
            isActive.setClickable(false);
        }
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }


    @Override
    public void setUpEditor(Establishment model) {
        etEstablishmentName.setText(model.getName());
        etEstablishmentAddress.setText(model.getAddress());
        etEstablishmentDescription.setText(model.getDescription());
        etEstablishmentMainStock.setText(model.getMainStock());
        etEstablishmentStock.setText(model.getStocks());
        etEstablishmentPhone.setText(model.getPhone());
        tvEdit.setText(R.string.edit_establishment);
    }

    @Override
    public void setUpPosEditor(EstablishmentPos pos) {

    }

    @Override
    public void onAddMode(int mode) {
        setEditable(true);
        clearFields();
        btnDelete.setText(getString(R.string.cancel));
        btnEdit.setText(getString(R.string.add));
        if(mode == 1)
            tvEdit.setText(R.string.add_establishment);
        else tvEdit.setText(R.string.add_pos);
    }

    void clearFields(){
        etEstablishmentName.setText("");
        etEstablishmentAddress.setText("");
        etEstablishmentPhone.setText("");
        etEstablishmentStock.setText("");
        etEstablishmentMainStock.setText("");
        etEstablishmentDescription.setText("");
    }
}
