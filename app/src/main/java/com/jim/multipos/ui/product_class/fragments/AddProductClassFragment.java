package com.jim.multipos.ui.product_class.fragments;

import android.os.Bundle;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpSpinner;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.ui.product_class.presenters.AddProductClassPresenter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.ProductClassEvent;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

public class AddProductClassFragment extends BaseFragment implements AddProductClassView  {

    public static final String CLICK_PRODUCT_CLASS = "click_prog";
    public static final String ADD_PRODUCT_CLASS = "add_prog";

    @Inject
    AddProductClassPresenter presenter;
    @Inject
    RxBusLocal rxBusLocal;
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





    @Override
    protected int getLayout() {
        return R.layout.product_class_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.onCreateView(savedInstanceState);
        cbActive.setChecked(true);
        RxView.clicks(btnSave).subscribe(aVoid -> {
            String className = etClassName.getText().toString();
            int pos = spParent.selectedItemPosition();
            boolean active = cbActive.isCheckboxChecked();
            presenter.onSaveButtonPress(className, pos,active);
        });
        RxView.clicks(btnCancel).subscribe(aVoid -> {
            getActivity().finish();
        });
    }

    ArrayList<Disposable> subscriptions;
    @Override
    protected void rxConnections() {
        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBusLocal.toObservable().subscribe(o -> {
                    if(o instanceof ProductClassEvent){
                        ProductClassEvent productClassEvent = (ProductClassEvent) o;
                        if(productClassEvent.getEventType().equals(CLICK_PRODUCT_CLASS)){
                            presenter.onClickProductClass(productClassEvent.getProductClass());
                        }else if(productClassEvent.getEventType().equals(ADD_PRODUCT_CLASS)){
                            presenter.addProductClass();
                        }
                    }}));

    }


    @Override
    public void onDestroy() {
        RxBus.removeListners(subscriptions);
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
    public void setParentSpinnerItems(ArrayList<String> productClasses) {
        spParent.setItems(productClasses);
        spParent.setAdapter();
    }

    @Override
    public void setParentSpinnerPosition(int position) {

        spParent.setSelection(0);
    }

    @Override
    public void classNameShort() {
        etClassName.setError(getString(R.string.class_name_should_be_longer));
    }

    @Override
    public void classNameEmpty() {
        etClassName.setError(getString(R.string.class_name_empty));

    }





}
