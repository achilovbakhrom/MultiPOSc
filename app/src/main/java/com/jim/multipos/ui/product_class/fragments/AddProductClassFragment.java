package com.jim.multipos.ui.product_class.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.intosystem.NameIdProdClass;
import com.jim.multipos.ui.product_class.di.ProductClassComponent;
import com.jim.multipos.ui.product_class.presenters.AddProductClassPresenter;
import com.jim.multipos.utils.RxBus;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddProductClassFragment extends BaseFragment implements AddProductClassView  {
    @Inject
    RxBus rxBus;
    @Inject
    AddProductClassPresenter presenter;
    @BindView(R.id.etClassName)
    EditText etClassName;
    @BindView(R.id.spParent)
    MpSpinner spParent;
    @BindView(R.id.cbActive)
    MpCheckbox cbActive;
    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnSave)
    MpButton btnSave;
    private ArrayList<NameIdProdClass> productClasses;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_class_fragment, container, false);
        this.getComponent(ProductClassComponent.class).inject(this);
        productClasses = new ArrayList<>();
        ButterKnife.bind(this, view);
        presenter.init(this);
        cbActive.setChecked(true);
        RxView.clicks(btnSave).subscribe(aVoid -> {
            String className = etClassName.getText().toString();
            int pos = spParent.selectedItemPosition();
            boolean active = cbActive.isCheckboxChecked();
            if(className.isEmpty()||className.length()<4){
                    etClassName.setError("Input Name");
                    return; }
            String id = null;
            if(pos>0)
                id =  productClasses.get(pos-1).getId();
            presenter.onSaveButtonPress(className, id,active);
        });
        RxView.clicks(btnCancel).subscribe(aVoid -> {
            getActivity().finish();
        });

        return view;
    }

    @Override
    public void onDestroy() {
        presenter.onDestroyView();
        super.onDestroy();
    }

    @Override
    public void fillView(ProductClass productClass) {
        etClassName.setText(productClass.getName());
        cbActive.setChecked(productClass.getActive());
    }

    @Override
    public void onAddNew() {
        etClassName.setText("");
        spParent.setSelection(0);
        cbActive.setChecked(true);
    }

    @Override
    public void setParentSpinnerItems(ArrayList<NameIdProdClass> productClasses) {
        this.productClasses = productClasses;
        ArrayList<String> strings = new ArrayList<>();
        for(NameIdProdClass nameIdProdClass:productClasses)
            strings.add(nameIdProdClass.getName());
        strings.add(0,getString(R.string.none));
        spParent.setItems(strings);
        spParent.setAdapter();
    }

    @Override
    public void setParentSpinnerPosition(String parent) {
        for (int i = 0; i < productClasses.size(); i++) {
            if(parent.equals(productClasses.get(i).getId())){
                spParent.setSelection(i+1);
                return;
            }
        }
        spParent.setSelection(0);
    }
}
