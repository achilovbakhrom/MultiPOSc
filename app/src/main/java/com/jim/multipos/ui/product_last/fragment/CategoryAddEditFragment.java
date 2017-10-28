package com.jim.multipos.ui.product_last.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpCheckbox;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.ui.product_last.ProductActivity;
import com.jim.multipos.ui.product_last.ProductPresenter;
import com.jim.multipos.ui.product_last.helpers.AddingMode;
import com.jim.multipos.ui.product_last.helpers.FragmentType;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

public class CategoryAddEditFragment extends BaseFragment {

    @BindView(R.id.tvName)
    TextView tvName;

    @NotEmpty(messageId = R.string.category_length_validation)
    @BindView(R.id.etSubCategoryName)
    MpEditText name;

    @BindView(R.id.llCategoryChoose)
    LinearLayout categoryChooseContainer;

    @BindView(R.id.tvChooseCategory)
    TextView categoryChoose;

    @BindView(R.id.etSubCategoryDescription)
    EditText description;

    @BindView(R.id.chbActive)
    MpCheckbox active;

    @BindView(R.id.btnSubCategoryDelete)
    MpButton delete;

    @BindView(R.id.btnSubCategorySave)
    MpButton save;

    @Override
    protected int getLayout() {
        return R.layout.category_add_edit_fragment;
    }

    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ProductPresenter presenter = ((ProductActivity) getContext()).getPresenter();
        setFragmentType(presenter.getType());
        setMode(presenter.getMode(), presenter.getCategory());
    }


    public void setFragmentType(FragmentType type) {
        if (type == FragmentType.CATEGORY) {
            categoryChooseContainer.setVisibility(View.GONE);
            tvName.setText(R.string.category);
        }
        else  {
            categoryChooseContainer.setVisibility(View.VISIBLE);
            tvName.setText(R.string.subcategory);
        }
    }

    public void setMode(AddingMode mode, Category category) {
        if (mode == AddingMode.ADD) {
            save.setText(R.string.save);
            name.setText("");
            name.requestFocus();
            categoryChoose.setText("");
            description.setText("");
            active.setChecked(true);
            delete.setVisibility(View.GONE);
        }
        else  {
            save.setText(R.string.update);
            name.setText(category.getName());
            if (!Objects.equals(category.getParentId(), Category.WITHOUT_PARENT)) {
                categoryChoose.setText(((ProductActivity) getContext()).getPresenter().getCategoryById(category.getParentId()).getName());
            }
            description.setText(category.getDescription());
            active.setChecked(category.isActive());
            delete.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(value = {R.id.btnSubCategorySave, R.id.btnSubCategoryDelete, R.id.btnSubCategoryCancel})
    public void buttonClick(View view) {
        ProductPresenter presenter = ((ProductActivity) getContext()).getPresenter();
        switch (view.getId()) {
            case R.id.btnSubCategorySave:
                presenter.addCategory(name.getText().toString(), description.getText().toString(), active.isChecked());
                break;

            case R.id.btnSubCategoryDelete:
                presenter.deleteCategory();
                break;

            case R.id.btnSubCategoryCancel:

                break;
        }
    }

}
