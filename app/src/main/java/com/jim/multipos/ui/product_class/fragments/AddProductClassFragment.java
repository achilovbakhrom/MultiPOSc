package com.jim.multipos.ui.product_class.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
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
import javax.inject.Named;

import butterknife.BindView;
import eu.inmite.android.lib.validations.form.annotations.MinLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import io.reactivex.disposables.Disposable;

public class AddProductClassFragment extends BaseFragment implements AddProductClassView  {

    public static final String CLICK_PRODUCT_CLASS = "click_prog";
    public static final String ADD_PRODUCT_CLASS = "add_prog";

    @Inject
    AddProductClassPresenter presenter;
    @Inject
    RxBusLocal rxBusLocal;
    @MinLength(value = 4,messageId = R.string.class_name_should_be_longer)
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
    @BindView(R.id.btnDelete)
    MpButton btnDelete;





    @Override
    protected int getLayout() {
        return R.layout.product_class_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.onCreateView(savedInstanceState);
        cbActive.setChecked(true);
        btnDelete.setVisibility(View.GONE);
        RxView.clicks(btnSave).subscribe(aVoid -> {
            if(isValid()) {
                String className = etClassName.getText().toString();
                int pos = spParent.selectedItemPosition();
                boolean active = cbActive.isCheckboxChecked();
                presenter.onSaveButtonPress(className, pos, active);
            }
        });
        RxView.clicks(btnCancel).subscribe(aVoid -> {
            getActivity().finish();
        });
        RxView.clicks(btnDelete).subscribe(o -> {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.do_you_want_delete)
                    .setPositiveButton(R.string.sure, (DialogInterface.OnClickListener) (dialog, id) -> {
                        presenter.deleteProductClass();
                        dialog.cancel();
                    }).setNegativeButton(R.string.cancel, (dialog, id) -> dialog.cancel());
            builder.create().show();
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
                            btnDelete.setVisibility(View.VISIBLE);
                        }else if(productClassEvent.getEventType().equals(ADD_PRODUCT_CLASS)){
                            presenter.addProductClass();
                            btnDelete.setVisibility(View.GONE);
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
        btnDelete.setVisibility(View.VISIBLE);

    }

    @Override
    public void onAddNew() {
        etClassName.setText("");
        spParent.setSelection(0);
        cbActive.setChecked(true);
        btnDelete.setVisibility(View.GONE);
    }

    @Override
    public void setParentSpinnerItems(ArrayList<String> productClasses) {
        spParent.setItems(productClasses);
        spParent.setAdapter();
    }

    @Override
    public void setParentSpinnerPosition(int position) {

        spParent.setSelection(position);
    }







}
