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
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.CategoryEvent;
import com.jim.multipos.utils.rxevents.ProductEvent;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

public class ProductPickerFragment extends BaseFragment implements ProductPickerView {


    @BindView(R.id.ivFolderView)
    ImageView ivFolderView;
    @BindView(R.id.ivSquareView)
    ImageView ivSquareView;
    @BindView(R.id.tvCategory)
    TextView tvCategory;
    @BindView(R.id.tvSubCategory)
    TextView tvSubCategory;
    @BindView(R.id.ivArrowCategory)
    ImageView ivArrowCategory;
    @BindView(R.id.ivArrowSubCategory)
    ImageView ivArrowSubCategory;
    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    RxBus rxBus;
    private FragmentManager fragmentManager;
    private static final int SQUARE_VIEW = 0;
    private static final int FOLDER_VIEW = 1;
    private static final String CATEGORY_TITLE = "category_title";
    private static final String SUBCATEGORY_TITLE = "subcategory_title";
    private static final String OPEN_PRODUCT = "open_product";
    ArrayList<Disposable> subscriptions;

    @Override
    protected int getLayout() {
        return R.layout.product_picker_fragment;
    }

    @Override
    protected void rxConnections() {
        super.rxConnections();
        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBus.toObservable().subscribe(o -> {
                    if (o instanceof CategoryEvent) {
                        CategoryEvent event = (CategoryEvent) o;
                        if (event.getEventType().equals(CATEGORY_TITLE)) {
                            if (event.getCategory() != null)
                                tvCategory.setText(event.getCategory().getName());
                            else tvCategory.setText(getResources().getString(R.string.category));
                        }
                        if (event.getEventType().equals(SUBCATEGORY_TITLE)) {
                            if (event.getCategory() != null) {
                                tvSubCategory.setText(event.getCategory().getName());
                                ivArrowSubCategory.setVisibility(View.VISIBLE);
                            } else {
                                tvSubCategory.setText("");
                                ivArrowSubCategory.setVisibility(View.GONE);
                            }
                        }
                    }
                    if (o instanceof ProductEvent){
                        ProductEvent event = (ProductEvent) o;
                        if (event.getEventType().equals(OPEN_PRODUCT))
                            replaceViewFragments(new ProductInfoFragment(), "PRODUCT_INFO");
                    }
                }));
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        fragmentManager = getActivity().getSupportFragmentManager();
        int viewType = preferencesHelper.getProductListViewType();
        changeViewTypeIcon(viewType);
        switch (viewType) {
            case SQUARE_VIEW:
                replaceViewFragments(new ProductSquareViewFragment(), String.valueOf(SQUARE_VIEW));
                break;
            case FOLDER_VIEW:
                replaceViewFragments(new ProductFolderViewFragment(), String.valueOf(FOLDER_VIEW));
                break;
        }
    }

    @OnClick(R.id.flFolderView)
    void onFolderViewClick() {
        ProductFolderViewFragment fragment = (ProductFolderViewFragment) fragmentManager.findFragmentByTag(String.valueOf(FOLDER_VIEW));
        if (fragment == null) {
            fragment = new ProductFolderViewFragment();
            replaceViewFragments(fragment, String.valueOf(FOLDER_VIEW));
            preferencesHelper.setProductListViewType(FOLDER_VIEW);
            changeViewTypeIcon(FOLDER_VIEW);
            tvCategory.setText(getResources().getString(R.string.category));
        }
    }

    @OnClick(R.id.flSquareView)
    void onSquareViewClick() {
        ProductSquareViewFragment fragment = (ProductSquareViewFragment) fragmentManager.findFragmentByTag(String.valueOf(SQUARE_VIEW));
        if (fragment == null) {
            fragment = new ProductSquareViewFragment();
            replaceViewFragments(fragment, String.valueOf(SQUARE_VIEW));
            preferencesHelper.setProductListViewType(SQUARE_VIEW);
            changeViewTypeIcon(SQUARE_VIEW);
        }
    }

    private void replaceViewFragments(Fragment fragment, String tag) {
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.flProductListContainer, fragment, tag)
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
    public void onDestroy() {
        super.onDestroy();
        RxBus.removeListners(subscriptions);
    }
}
