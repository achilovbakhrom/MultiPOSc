package com.jim.multipos.ui.admin_main_page.fragments.company;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.admin_main_page.CompanyEvent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CompanyFragment extends BaseFragment {

    @Inject
    RxBus bus;

    @BindView(R.id.etCompanyName)
    MpEditText etCompanyName;
    @BindView(R.id.etCompanyID)
    MpEditText etCompanyID;
    @BindView(R.id.etCompanyAddress)
    MpEditText etCompanyAddress;
    @BindView(R.id.etIPCode)
    MpEditText etIPCode;
    @BindView(R.id.etDescription)
    MpEditText etDescription;

    @BindView(R.id.isActive)
    Switch isActive;

    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.btnEdit)
    Button btnEdit;

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


    CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected int getLayout() {
        return R.layout.admin_add_company_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setEditable(false);
        disposable.add(bus.toObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof CompanyEvent) {
                        etCompanyName.setText(((CompanyEvent) o).getCompanyName());
                        etCompanyID.setText(((CompanyEvent) o).getCompanyID());
                        etCompanyAddress.setText(((CompanyEvent) o).getCompanyAddress());
                        etIPCode.setText(((CompanyEvent) o).getCompanyIPCODE());
                        etDescription.setText(((CompanyEvent) o).getCompanyDescription());
                    }
                }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.dispose();
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

    }

    private void onEditClicked() {
        setEditable(true);
        btnEdit.setText(getString(R.string.save));
        btnDelete.setText(getString(R.string.cancel));
        btnDelete.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreySecondaryDark));
    }

    public void setEditable(boolean mode) {
        if (mode) {
            etCompanyName.setKeyListener((KeyListener) etCompanyName.getTag());
            etCompanyID.setKeyListener((KeyListener) etCompanyID.getTag());
            etCompanyAddress.setKeyListener((KeyListener) etCompanyAddress.getTag());
            etIPCode.setKeyListener((KeyListener) etIPCode.getTag());
            etDescription.setKeyListener((KeyListener) etDescription.getTag());
            isActive.setClickable(true);
            btnEdit.setText(getText(R.string.save));
        } else {
            etCompanyName.setTag(etCompanyName.getKeyListener());
            etCompanyName.setKeyListener(null);
            etCompanyID.setTag(etCompanyID.getKeyListener());
            etCompanyID.setKeyListener(null);
            etCompanyAddress.setTag(etCompanyAddress.getKeyListener());
            etCompanyAddress.setKeyListener(null);
            etIPCode.setTag(etIPCode.getKeyListener());
            etIPCode.setKeyListener(null);
            etDescription.setTag(etDescription.getKeyListener());
            etDescription.setKeyListener(null);
            isActive.setClickable(false);
        }
    }
}
