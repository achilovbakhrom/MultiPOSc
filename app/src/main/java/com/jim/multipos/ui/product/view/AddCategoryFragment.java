package com.jim.multipos.ui.product.view;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;

import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.ui.product.presenter.CategoryPresenter;

import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.CategoryEvent;
import com.jim.multipos.utils.rxevents.MessageEvent;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.MinLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import io.reactivex.disposables.Disposable;


/**
 * Created by DEV on 09.08.2017.
 */

public class AddCategoryFragment extends BaseFragment implements CategoryView {

    @NotEmpty(messageId = R.string.name_validation, order = 1)
    @MinLength(messageId = R.string.category_length_validation, order = 2, value = 4)
    @BindView(R.id.etCategoryName)
    MpEditText etCategoryName;
    @BindView(R.id.etCategoryDescription)
    EditText etCategoryDescription;
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;
    @Inject
    RxPermissions rxPermissions;
    @Inject
    CategoryPresenter presenter;
    @Inject
    RxBusLocal rxBusLocal;
    @Inject
    RxBus rxBus;
    private static final String CLICK = "click";
    private final static String FRAGMENT_OPENED = "category";
    private static final String ADD = "added";
    private static final String UPDATE = "update";
    ArrayList<Disposable> subscriptions;

    @Override
    protected int getLayout() {
        return R.layout.add_category_fragment;
    }


   @Override
   public boolean isValid() {
        return FormValidator.validate(this, new SimpleErrorPopupCallback(getContext()));
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        rxBus.send(new MessageEvent(FRAGMENT_OPENED));
        etCategoryName.setOnClickListener(view -> etCategoryName.setError(null));
    }

    @Override
    protected void rxConnections() {
        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBusLocal.toObservable().subscribe(o -> {
                    if (o instanceof CategoryEvent) {
                        CategoryEvent event = (CategoryEvent) o;
                        if (event.getEventType().equals(CLICK)) {
                            presenter.clickedCategory(event.getCategory());
                            etCategoryName.setError(null);
                        }
                    }
                }));
    }

    @OnClick(R.id.btnCategoryCancel)
    public void onBack() {
        getActivity().finish();
    }

    @OnClick(R.id.btnCategorySave)
    public void onSave() {
        if (isValid())
            presenter.saveCategory(etCategoryName.getText().toString(),
                    etCategoryDescription.getText().toString(),
                    chbActive.isChecked());

    }

    @Override
    public void setFields(String name, String description, boolean active) {
        etCategoryName.setText(name);
        etCategoryDescription.setText(description);
        chbActive.setChecked(active);
    }

    @Override
    public void clearFields() {
        etCategoryName.setText("");
        etCategoryDescription.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.removeListners(subscriptions);
    }

    @Override
    public void setData() {
        presenter.checkData();
    }

    @Override
    public void setError(String error) {
        etCategoryName.setError(error);
    }

    @Override
    public void sendEvent(Category category, String event) {
        if (event.equals(ADD)) {
            rxBus.send(new CategoryEvent(category, ADD));
        } else if (event.equals(UPDATE)) {
            rxBus.send(new CategoryEvent(category, UPDATE));
//            ((ProductsActivity) getActivity()).openCategory();
        }
    }

    @Override
    public void confirmChanges() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Warning");
        builder.setMessage("Do you really want to accept changes?");
        builder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
            presenter.acceptChanges();
            dialogInterface.dismiss();
        });
        builder.setNegativeButton(R.string.no, (dialogInterface, i) -> {
            presenter.notAcceptChanges();
            dialogInterface.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
