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
import com.jim.multipos.ui.product_last.ProductActivity;
import com.jim.multipos.ui.product_last.ProductPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */
public class CategoryAddEditFragment extends BaseFragment {

    @BindView(R.id.tvName)
    TextView tvName;
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
    @BindView(R.id.tvCategoryTitle)
    TextView tvCategoryTitle;


    @Override
    protected int getLayout() {
        return R.layout.category_add_edit_fragment;
    }

    /**
     * this fragment will not be added to the container of dagger
     * @return - false
     */
    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setCategoryAddMode();
    }

    /**
     * sets mode for adding category
     */
    public void setCategoryAddMode() {
        categoryChooseContainer.setVisibility(View.GONE);
        tvName.setText(R.string.category);
        save.setText(R.string.save);
        name.setText("");
        name.setError(null);
        description.clearFocus();
        name.requestFocus();
        categoryChoose.setText("");
        description.setText("");
        active.setChecked(true);
        delete.setVisibility(View.GONE);
        tvCategoryTitle.setText(R.string.tap_to_category_to_manage_it);
    }

    /**
     * sets mode for adding subcategory
     */
    public void setSubcategoryAddMode(String parentName) {
        categoryChooseContainer.setVisibility(View.VISIBLE);
        tvName.setText(R.string.subcategory);
        save.setText(R.string.save);
        name.setText("");
        name.setError(null);
        description.clearFocus();
        name.requestFocus();
        categoryChoose.setText(parentName);
        description.setText("");
        active.setChecked(true);
        delete.setVisibility(View.GONE);
        tvCategoryTitle.setText(R.string.tap_to_manage_subcategory_items);
    }

    /**
     * sets mode for editing category
     * @param categoryName - editing category name
     * @param categoryDescription - editing category description
     * @param isActive - editing category activation state
     */
    public void setCategoryEditMode(String categoryName, String categoryDescription, boolean isActive) {
        categoryChooseContainer.setVisibility(View.GONE);
        tvName.setText(R.string.category);
        save.setText(R.string.update);
        name.setText(categoryName);
        name.setError(null);
        description.setText(categoryDescription);
        active.setChecked(isActive);
        delete.setVisibility(View.VISIBLE);
        tvCategoryTitle.setText(R.string.tap_to_category_to_manage_it);
    }

    /**
     * sets mode for editing subcategory
     * @param categoryName - editing subcategory name
     * @param categoryDescription - editing subcategory description
     * @param isActive - editing subcategory activation state
     * @param parentCategoryName - editing subcategory's parent's name
     */
    public void setSubcategoryEditMode(String categoryName, String categoryDescription, boolean isActive, String parentCategoryName) {
        categoryChooseContainer.setVisibility(View.VISIBLE);
        tvName.setText(R.string.subcategory);
        save.setText(R.string.update);
        name.setText(categoryName);
        name.setError(null);
        categoryChoose.setText(parentCategoryName);
        description.setText(categoryDescription);
        active.setChecked(isActive);
        delete.setVisibility(View.VISIBLE);
        tvCategoryTitle.setText(R.string.tap_to_manage_subcategory_items);
    }

    /**
     * processing button clicks
     * @param view - clicked button view
     */
    @OnClick(value = {R.id.btnSubCategorySave, R.id.btnSubCategoryDelete, R.id.btnSubCategoryCancel})
    public void buttonClick(View view) {
        ProductPresenter presenter = ((ProductActivity) getContext()).getPresenter();
        switch (view.getId()) {
            case R.id.btnSubCategorySave:
                if (name.getText().toString().isEmpty()){
                    name.setError(getContext().getString(R.string.category_length_validation));
                } else presenter.addCategory(name.getText().toString(), description.getText().toString(), active.isChecked());
                break;
            case R.id.btnSubCategoryDelete:
                presenter.deleteCategory();
                break;
            case R.id.btnSubCategoryCancel:
                presenter.finishActivity();
                break;
        }
    }

    public String getName() {
        return name.getText().toString();
    }

    public String getDescription() {
        return description.getText().toString();
    }

    public boolean isActive() {
        return active.isChecked();
    }



}
