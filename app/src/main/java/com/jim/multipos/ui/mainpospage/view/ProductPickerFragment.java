package com.jim.multipos.ui.mainpospage.view;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.mainpospage.connection.MainPageConnection;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.OrderProductAddEvent;
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
    MainPageConnection mainPageConnection;
    @Inject
    RxBusLocal rxBusLocal;
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
                rxBusLocal.toObservable().subscribe(o -> {
                    if (o instanceof ProductEvent) {
                        ProductEvent event = (ProductEvent) o;
                        if (event.getEventType().equals(OPEN_PRODUCT)) {
                            Long id = event.getProduct().getId();
                            rxBusLocal.send(new OrderProductAddEvent(id, OrderListFragment.PRODUCT_ADD_TO_ORDER));
                        }
                    }

                }));
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mainPageConnection.setProductPickerView(this);
        fragmentManager = getActivity().getSupportFragmentManager();
        int viewType = preferencesHelper.getProductListViewType();
        changeViewTypeIcon(viewType);
        switch (viewType) {
            case SQUARE_VIEW:
                showProductSquareViewFragment();
                break;
            case FOLDER_VIEW:
                showProductFolderViewFragment();
                break;
        }
    }

    @OnClick(R.id.flFolderView)
    void onFolderViewClick() {
        ProductSquareViewFragment squareViewFragment = (ProductSquareViewFragment) fragmentManager.findFragmentByTag(String.valueOf(SQUARE_VIEW));
        if (squareViewFragment != null && squareViewFragment.isVisible()) {
            hideProductSquareViewFragment();
        }
        showProductFolderViewFragment();
        preferencesHelper.setProductListViewType(FOLDER_VIEW);
        changeViewTypeIcon(FOLDER_VIEW);
    }

    @OnClick(R.id.flSquareView)
    void onSquareViewClick() {
        ProductFolderViewFragment folderViewFragment = (ProductFolderViewFragment) fragmentManager.findFragmentByTag(String.valueOf(FOLDER_VIEW));
        if (folderViewFragment != null && folderViewFragment.isVisible()) {
            hideProductFolderViewFragment();
        }
        showProductSquareViewFragment();
        preferencesHelper.setProductListViewType(SQUARE_VIEW);
        changeViewTypeIcon(SQUARE_VIEW);
    }

    public void showProductSquareViewFragment() {
        ProductSquareViewFragment squareViewFragment = (ProductSquareViewFragment) fragmentManager.findFragmentByTag(String.valueOf(SQUARE_VIEW));
        if (squareViewFragment == null) {
            squareViewFragment = new ProductSquareViewFragment();
            addFragmentWithTagStatic(R.id.flProductListContainer, squareViewFragment, String.valueOf(SQUARE_VIEW));
        } else {
            fragmentManager.beginTransaction().show(squareViewFragment).commit();
            squareViewFragment.onShow();
        }
    }

    public void showProductFolderViewFragment() {
        ProductFolderViewFragment folderViewFragment = (ProductFolderViewFragment) fragmentManager.findFragmentByTag(String.valueOf(FOLDER_VIEW));
        if (folderViewFragment == null) {
            folderViewFragment = new ProductFolderViewFragment();
            addFragmentWithTagStatic(R.id.flProductListContainer, folderViewFragment, String.valueOf(FOLDER_VIEW));
            tvCategory.setText(getResources().getString(R.string.category));
        } else {
            fragmentManager.beginTransaction().show(folderViewFragment).commit();
            folderViewFragment.onShow();
        }
    }

    public void hideProductSquareViewFragment() {
        ProductSquareViewFragment squareViewFragment = (ProductSquareViewFragment) fragmentManager.findFragmentByTag(String.valueOf(SQUARE_VIEW));
        if (squareViewFragment != null) {
            fragmentManager.beginTransaction().hide(squareViewFragment).commit();
        }
    }

    public void hideProductFolderViewFragment() {
        ProductFolderViewFragment folderViewFragment = (ProductFolderViewFragment) fragmentManager.findFragmentByTag(String.valueOf(FOLDER_VIEW));
        if (folderViewFragment != null) {
            fragmentManager.beginTransaction().hide(folderViewFragment).commit();
        }
    }


    private void addFragmentWithTagStatic(@IdRes int containerViewId, Fragment fragment, String tag) {
        if (fragment.isAdded()) return;
        fragmentManager.beginTransaction()
                .add(containerViewId, fragment, tag)
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


    @Override
    public void onDetach() {
        mainPageConnection.setOrderListView(null);
        super.onDetach();
    }

    @Override
    public void updateCategoryTitles(Category category, String type) {
        if (type.equals(CATEGORY_TITLE)) {
            if (category != null)
                tvCategory.setText(category.getName());
            else tvCategory.setText(getResources().getString(R.string.category));
        }
        if (type.equals(SUBCATEGORY_TITLE)) {
            if (category != null) {
                tvSubCategory.setText(category.getName());
                ivArrowSubCategory.setVisibility(View.VISIBLE);
            } else {
                tvSubCategory.setText("");
                ivArrowSubCategory.setVisibility(View.GONE);
            }
        }
    }
}
