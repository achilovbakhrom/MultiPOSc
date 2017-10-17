package com.jim.multipos.ui.product.view;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.ui.product.presenter.SubCategoryPresenter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.CategoryEvent;
import com.jim.multipos.utils.rxevents.MessageEvent;
import com.jim.multipos.utils.rxevents.SubCategoryEvent;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

/**
 * Created by DEV on 18.08.2017.
 */

public class AddSubCategoryFragment extends BaseFragment implements SubCategoryView {
    @BindView(R.id.etSubCategoryName)
    MpEditText etSubCategoryName;
    @BindView(R.id.etSubCategoryDescription)
    EditText etSubCategoryDescription;
    @BindView(R.id.tvChooseCategory)
    TextView tvChooseCategory;
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;
    @Inject
    SubCategoryPresenter presenter;
    @Inject
    RxBusLocal rxBusLocal;
    @Inject
    RxBus rxBus;
    private String categoryName = "";
    private Uri photoSelected;
    private static final String CLICK = "click";
    private final static String FRAGMENT_OPENED = "subcategory";
    private final static String PARENT = "parent";
    private static final String ADD = "added";
    private static final String UPDATE = "update";
    ArrayList<Disposable> subscriptions;

    @Override
    protected int getLayout() {
        return R.layout.add_sub_category_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tvChooseCategory.setText(categoryName);
        rxBus.send(new MessageEvent(FRAGMENT_OPENED));
    }

    @Override
    protected void rxConnections() {
        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBusLocal.toObservable().subscribe(o -> {
                    if (o instanceof SubCategoryEvent) {
                        SubCategoryEvent event = (SubCategoryEvent) o;
                        if (event.getEventType().equals(CLICK)) {
                            presenter.clickedSubCategory(event.getSubCategory());
                        }
                    }
                    if (o instanceof CategoryEvent) {
                        CategoryEvent event = (CategoryEvent) o;
                        if (event.getEventType().equals(PARENT)) {
                            presenter.setParentCategory(event.getCategory());
                        }
                    }
                }));
    }

    @OnClick(R.id.btnSubCategoryCancel)
    public void onBack() {
        getActivity().finish();
    }

    @OnClick(R.id.btnSubCategorySave)
    public void onSave() {
        presenter.save(etSubCategoryName.getText().toString(),
                etSubCategoryDescription.getText().toString(),
                chbActive.isChecked());
    }

    @Override
    public void setFields(String name, String description, boolean active) {
        etSubCategoryName.setText(name);
        etSubCategoryDescription.setText(description);
        chbActive.setChecked(active);
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
        etSubCategoryName.setError(error);
    }

    @Override
    public void sendEvent(Category subCategory, String event) {
        switch (event) {
            case ADD:
                rxBus.send(new SubCategoryEvent(subCategory, ADD));
                break;
            case UPDATE:
                rxBus.send(new SubCategoryEvent(subCategory, UPDATE));
                break;
        }
    }


    @Override
    public void clearFields() {
        etSubCategoryName.setText("");
        etSubCategoryDescription.setText("");
    }

    @Override
    public void setParentCategoryName(String parentCategoryName) {
        tvChooseCategory.setText(parentCategoryName);
        categoryName = parentCategoryName;
    }
}
