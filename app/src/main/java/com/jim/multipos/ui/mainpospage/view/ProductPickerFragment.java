package com.jim.multipos.ui.mainpospage.view;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.prefs.PreferencesHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ProductPickerFragment extends BaseFragment implements ProductPickerView {


    @BindView(R.id.ivFolderView)
    ImageView ivFolderView;
    @BindView(R.id.ivSquareView)
    ImageView ivSquareView;
    @BindView(R.id.tvCategory)
    TextView tvCategory;
    @BindView(R.id.tvSubCategory)
    TextView tvSubCategory;
    @BindView(R.id.tvProduct)
    TextView tvProduct;
    @BindView(R.id.ivArrowCategory)
    ImageView ivArrowCategory;
    @BindView(R.id.ivArrowSubCategory)
    ImageView ivArrowSubCategory;
    @Inject
    PreferencesHelper preferencesHelper;
    private FragmentManager fragmentManager;
    private static final int SQUARE_VIEW = 0;
    private static final int FOLDER_VIEW = 1;


    @Override
    protected int getLayout() {
        return R.layout.product_picker_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        fragmentManager = getActivity().getSupportFragmentManager();
        int viewType = preferencesHelper.getProductListViewType();
        switch (viewType) {
            case SQUARE_VIEW:
                replaceViewFragments(new ProductSquareViewFragment());
                break;
            case FOLDER_VIEW:
                replaceViewFragments(new ProductFolderViewFragment());
                break;
        }
    }

    @OnClick(R.id.ivFolderView)
    void onFolderViewClick() {
        replaceViewFragments(new ProductFolderViewFragment());
        preferencesHelper.setProductListViewType(FOLDER_VIEW);
        changeViewTypeIcon(FOLDER_VIEW);
    }

    @OnClick(R.id.ivSquareView)
    void onSquareViewClick() {
        replaceViewFragments(new ProductSquareViewFragment());
        preferencesHelper.setProductListViewType(SQUARE_VIEW);
        changeViewTypeIcon(SQUARE_VIEW);
    }

    private void replaceViewFragments(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.flProductListContainer, fragment)
                .commit();
    }

    private void changeViewTypeIcon(int viewType) {
        switch (viewType) {
            case SQUARE_VIEW:
                ivSquareView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlue)));
                ivFolderView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorTintGrey)));
                break;
            case FOLDER_VIEW:
                ivSquareView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorTintGrey)));
                ivFolderView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlue)));
                break;
        }
    }

    @Override
    public void categoryMode() {
        tvCategory.setVisibility(View.VISIBLE);
        ivArrowCategory.setVisibility(View.VISIBLE);
        tvSubCategory.setVisibility(View.GONE);
        ivArrowSubCategory.setVisibility(View.GONE);
        tvProduct.setVisibility(View.GONE);
    }

    @Override
    public void subCategoryMode() {
        tvCategory.setVisibility(View.VISIBLE);
        ivArrowCategory.setVisibility(View.VISIBLE);
        tvSubCategory.setVisibility(View.VISIBLE);
        ivArrowSubCategory.setVisibility(View.VISIBLE);
        tvProduct.setVisibility(View.GONE);
    }

    @Override
    public void productMode() {
        tvCategory.setVisibility(View.VISIBLE);
        ivArrowCategory.setVisibility(View.VISIBLE);
        tvSubCategory.setVisibility(View.VISIBLE);
        ivArrowSubCategory.setVisibility(View.VISIBLE);
        tvProduct.setVisibility(View.VISIBLE);
    }

}
