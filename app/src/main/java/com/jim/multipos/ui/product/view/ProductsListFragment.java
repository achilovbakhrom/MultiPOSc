package com.jim.multipos.ui.product.view;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.SubCategory;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.product.ProductsActivity;
import com.jim.multipos.ui.product.adapter.ProductsListAdapter;
import com.jim.multipos.ui.product.presenter.ProductListPresenter;
import com.jim.multipos.utils.item_touch_helper.OnStartDragListener;
import com.jim.multipos.utils.item_touch_helper.SimpleItemTouchHelperCallback;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by DEV on 10.08.2017.
 */

public class ProductsListFragment extends BaseFragment implements ProductListView, OnStartDragListener {

    @BindView(R.id.tvCategory)
    TextView tvCategory;
    @BindView(R.id.tvSubCategory)
    TextView tvSubCategory;
    @BindView(R.id.tvProduct)
    TextView tvProduct;
    @BindView(R.id.ivArrowFirst)
    ImageView ivArrowFirst;
    @BindView(R.id.ivArrow)
    ImageView ivArrow;
    @BindView(R.id.rvCategory)
    RecyclerView rvCategory;
    @BindView(R.id.rvSubCategory)
    RecyclerView rvSubCategory;
    @BindView(R.id.rvProduct)
    RecyclerView rvProduct;
    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    ProductListPresenter presenter;
    @Inject
    ProductsActivity activity;
    private ProductsListAdapter categoryAdapter;
    private ProductsListAdapter subCategoryAdapter, productsAdapter;
    private ItemTouchHelper touchHelper;
    private Unbinder unbinder;
    private static final int CATEGORY = 0;
    private static final int SUBCATEGORY = 1;
    private static final int PRODUCT = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_product_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter.init(this);
        presenter.setViewsVisibility(CATEGORY);
        return view;
    }

    @Override
    protected int getLayout() {
        return 0;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void rxConnections() {

    }

    @Override
    public void setCategoryRecyclerViewItems(List<Category> categories) {
        rvCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryAdapter = new ProductsListAdapter(categories, presenter, CATEGORY, this);
        rvCategory.setAdapter(categoryAdapter);
        rvCategory.setItemAnimator(null);
        categoryAdapter.setPosition(preferencesHelper.getLastPositionCategory());
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(categoryAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvCategory);
    }

    @Override
    public void setSubCategoryRecyclerView(List<SubCategory> subCategories) {
        rvSubCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        subCategoryAdapter = new ProductsListAdapter(subCategories, presenter, SUBCATEGORY, this);
        rvSubCategory.setAdapter(subCategoryAdapter);
        rvSubCategory.setItemAnimator(null);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(subCategoryAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvSubCategory);
    }

    @Override
    public void setProductRecyclerView(List<Product> products) {
        rvProduct.setLayoutManager(new GridLayoutManager(getContext(), 4));
        productsAdapter = new ProductsListAdapter(products, presenter, PRODUCT, this);
        rvProduct.setAdapter(productsAdapter);
        rvSubCategory.setItemAnimator(null);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(productsAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvProduct);
    }

    @Override
    public void updateCategoryItems() {
        categoryAdapter.notifyDataSetChangedWithZeroButton();
    }

    @Override
    public void updateProductItems() {
        productsAdapter.notifyDataSetChangedWithZeroButton();
    }

    @Override
    public void updateSubCategoryItems() {
        subCategoryAdapter.notifyDataSetChangedWithZeroButton();
    }

    @Override
    public void setCategoryName(String name) {
        tvCategory.setText(name);
        categoryMode();
    }

    @Override
    public void setSubCategoryName(String name) {
        tvSubCategory.setText(name);
        subCategoryMode();
    }

    @Override
    public void setViewsVisibility(int mode) {
        switch (mode) {
            case CATEGORY:
                rvSubCategory.setVisibility(View.GONE);
                rvProduct.setVisibility(View.GONE);
                break;
            case SUBCATEGORY:
                rvSubCategory.setVisibility(View.VISIBLE);
                rvProduct.setVisibility(View.GONE);
                break;
            case PRODUCT:
                rvSubCategory.setVisibility(View.VISIBLE);
                rvProduct.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void setProductName(String name) {
        tvProduct.setText(name);
        productMode();
    }

    @Override
    public void openCategory() {
        activity.openCategory();
    }

    @Override
    public void openSubCategory() {
        activity.openSubCategory();
    }

    @Override
    public void openProduct() {
        activity.openProduct();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }

    @Override
    public void categoryMode() {
        tvCategory.setVisibility(View.VISIBLE);
        ivArrowFirst.setVisibility(View.VISIBLE);
        tvSubCategory.setVisibility(View.GONE);
        ivArrow.setVisibility(View.GONE);
        tvProduct.setVisibility(View.GONE);
    }

    @Override
    public void subCategoryMode() {
        tvCategory.setVisibility(View.VISIBLE);
        ivArrowFirst.setVisibility(View.VISIBLE);
        tvSubCategory.setVisibility(View.VISIBLE);
        ivArrow.setVisibility(View.VISIBLE);
        tvProduct.setVisibility(View.GONE);
    }

    @Override
    public void productMode() {
        tvCategory.setVisibility(View.VISIBLE);
        ivArrowFirst.setVisibility(View.VISIBLE);
        tvSubCategory.setVisibility(View.VISIBLE);
        ivArrow.setVisibility(View.VISIBLE);
        tvProduct.setVisibility(View.VISIBLE);
    }

    @Override
    public void allInvisible() {
        tvCategory.setVisibility(View.GONE);
        ivArrowFirst.setVisibility(View.GONE);
        tvSubCategory.setVisibility(View.GONE);
        ivArrow.setVisibility(View.GONE);
        tvProduct.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroyView();
    }
}
