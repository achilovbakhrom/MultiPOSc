package com.jim.multipos.ui.product.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.ui.product.presenter.CategoryPresenter;

import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.WarningDialog;
import com.jim.multipos.utils.rxevents.CategoryEvent;
import com.jim.multipos.utils.rxevents.MessageEvent;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.annotations.MinLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import io.reactivex.disposables.Disposable;

import static com.jim.multipos.utils.UIUtils.closeKeyboard;


/**
 * Created by DEV on 09.08.2017.
 */

public class AddCategoryFragment extends BaseFragment implements CategoryView {

    @NotEmpty(messageId = R.string.name_validation, order = 1)
    @MinLength(messageId = R.string.category_length_validation, order = 2, value = 3)
    @BindView(R.id.etCategoryName)
    MpEditText etCategoryName;
    @BindView(R.id.etCategoryDescription)
    EditText etCategoryDescription;
    @BindView(R.id.chbActive)
    MpCheckbox chbActive;
    @BindView(R.id.btnCategorySave)
    MpButton btnCategorySave;
    @BindView(R.id.btnCategoryDelete)
    MpButton btnCategoryDelete;
    @Inject
    CategoryPresenter presenter;
    @Inject
    RxBusLocal rxBusLocal;
    @Inject
    RxBus rxBus;
    private static final String CLICK = "click";
    private final static String FRAGMENT_OPENED = "category";
    private final static String DELETE = "delete";
    private static final int HAVE_CHILD = 100;
    private static final int IS_ACTIVE = 101;
    private static final int NOT_UPDATED = 102;
    ArrayList<Disposable> subscriptions;
    private WarningDialog dialog;

    @Override
    protected int getLayout() {
        return R.layout.add_category_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        rxBus.send(new MessageEvent(FRAGMENT_OPENED));
        etCategoryName.setOnClickListener(view -> etCategoryName.setError(null));
        closeKeyboard(btnCategorySave, getContext());
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
                            if (event.getCategory() != null) {
                                btnCategorySave.setText(getResources().getString(R.string.save));
                                btnCategoryDelete.setVisibility(View.VISIBLE);
                            } else {
                                btnCategoryDelete.setVisibility(View.GONE);
                                btnCategorySave.setText(getResources().getString(R.string.add));
                            }
                            closeKeyboard(btnCategorySave, getContext());
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

    @OnClick(R.id.btnCategoryDelete)
    public void onDelete() {
        presenter.checkDeleteOptions();
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
        chbActive.setChecked(true);
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
        rxBus.send(new CategoryEvent(category, event));
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
    public void showWarningDialog(int type) {
        switch (type) {
            case HAVE_CHILD:
                dialog = new WarningDialog(getContext());
                dialog.onlyText(true);
                dialog.setWarningText("You can't delete category, which has subcategories. Firstly, delete subcategories to delete category");
                dialog.setOnYesClickListener(view -> dialog.dismiss());
                dialog.show();
                break;
            case IS_ACTIVE:
                dialog = new WarningDialog(getContext());
                dialog.onlyText(true);
                dialog.setWarningText("You can't delete active category. Make category non-active for deleting");
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
            presenter.deleteCategory();
            dialog.dismiss();
        });
        dialog.setOnNoClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeKeyboard(btnCategorySave, getContext());
    }
}
