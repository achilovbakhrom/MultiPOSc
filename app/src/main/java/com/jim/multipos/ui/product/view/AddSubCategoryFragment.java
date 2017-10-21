package com.jim.multipos.ui.product.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.ui.product.presenter.SubCategoryPresenter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.WarningDialog;
import com.jim.multipos.utils.rxevents.CategoryEvent;
import com.jim.multipos.utils.rxevents.MessageEvent;
import com.jim.multipos.utils.rxevents.SubCategoryEvent;

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
 * Created by DEV on 18.08.2017.
 */

public class AddSubCategoryFragment extends BaseFragment implements SubCategoryView {
    @NotEmpty(messageId = R.string.name_validation, order = 1)
    @MinLength(messageId = R.string.subcategory_length_validation, order = 2, value = 3)
    @BindView(R.id.etSubCategoryName)
    MpEditText etSubCategoryName;
    @BindView(R.id.etSubCategoryDescription)
    EditText etSubCategoryDescription;
    @BindView(R.id.tvChooseCategory)
    TextView tvChooseCategory;
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;
    @BindView(R.id.btnSubCategorySave)
    MpButton btnSubCategorySave;
    @BindView(R.id.btnSubCategoryDelete)
    MpButton btnSubCategoryDelete;
    @Inject
    SubCategoryPresenter presenter;
    @Inject
    RxBusLocal rxBusLocal;
    @Inject
    RxBus rxBus;
    private String categoryName = "";
    private static final String CLICK = "click";
    private final static String FRAGMENT_OPENED = "subcategory";
    private final static String PARENT = "parent";
    private static final int HAVE_CHILD = 1;
    private static final int IS_ACTIVE = 2;
    private static final int NOT_UPDATED = 3;
    ArrayList<Disposable> subscriptions;
    private WarningDialog dialog;

    @Override
    protected int getLayout() {
        return R.layout.add_sub_category_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tvChooseCategory.setText(categoryName);
        rxBus.send(new MessageEvent(FRAGMENT_OPENED));
        etSubCategoryName.setOnClickListener(view -> etSubCategoryName.setError(null));
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
                            etSubCategoryName.setError(null);
                            if (event.getSubCategory() != null) {
                                btnSubCategorySave.setText(getResources().getString(R.string.save));
                                btnSubCategoryDelete.setVisibility(View.VISIBLE);
                            } else {
                                btnSubCategorySave.setText(getResources().getString(R.string.add));
                                btnSubCategoryDelete.setVisibility(View.GONE);
                            }
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
        if (isValid())
            presenter.save(etSubCategoryName.getText().toString(),
                    etSubCategoryDescription.getText().toString(),
                    chbActive.isChecked());
    }

    @OnClick(R.id.btnSubCategoryDelete)
    public void onDelete() {
        presenter.checkDeleteOptions();
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
        rxBus.send(new SubCategoryEvent(subCategory, event));
    }

    @Override
    public void confirmChanges() {
        WarningDialog dialog = new WarningDialog(getContext());
        dialog.setWarningText(getString(R.string.want_accept_changes));
        dialog.setOnYesClickListener(view -> {
            presenter.acceptChanges();
            dialog.dismiss();
        });
        dialog.setOnNoClickListener(view -> {
//            presenter.notAcceptChanges();
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void showWarningDialog(int message) {
        switch (message) {
            case HAVE_CHILD:
                dialog = new WarningDialog(getContext());
                dialog.onlyText(true);
                dialog.setWarningText("You can't delete subcategory, which has products. Firstly, delete products to delete subcategory");
                dialog.setOnYesClickListener(view -> dialog.dismiss());
                dialog.show();
                break;
            case IS_ACTIVE:
                dialog = new WarningDialog(getContext());
                dialog.onlyText(true);
                dialog.setWarningText("You can't delete active subcategory. Make subcategory non-active for deleting");
                dialog.setOnYesClickListener(view -> dialog.dismiss());
                dialog.show();
                break;
            case NOT_UPDATED:
                dialog = new WarningDialog(getContext());
                dialog.onlyText(true);
                dialog.setWarningText("No changes found");
                dialog.setOnYesClickListener(view -> dialog.dismiss());
                dialog.show();
                break;
        }
    }

    @Override
    public void confirmDeleting() {
        WarningDialog dialog = new WarningDialog(getContext());
        dialog.setWarningText(getString(R.string.do_you_want_delete));
        dialog.setOnYesClickListener(view -> {
            presenter.deleteSubCategory();
            dialog.dismiss();
        });
        dialog.setOnNoClickListener(view -> dialog.dismiss());
        dialog.show();
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
