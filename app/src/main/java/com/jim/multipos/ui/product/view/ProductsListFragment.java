package com.jim.multipos.ui.product.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.product.ProductsActivity;
import com.jim.multipos.ui.product.adapter.CategoryAdapter;
import com.jim.multipos.ui.product.adapter.ProductsListAdapter;
import com.jim.multipos.ui.product.presenter.ProductListPresenter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.item_touch_helper.SimpleItemTouchHelperCallback;
import com.jim.multipos.utils.rxevents.CategoryEvent;
import com.jim.multipos.utils.rxevents.MessageEvent;
import com.jim.multipos.utils.rxevents.ProductEvent;
import com.jim.multipos.utils.rxevents.SubCategoryEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

import static com.jim.multipos.utils.UIUtils.closeKeyboard;

/**
 * Created by DEV on 10.08.2017.
 */

public class ProductsListFragment extends BaseFragment implements ProductListView {

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
    @BindView(R.id.switchShowActive)
    Switch switchShowActive;
    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    ProductListPresenter presenter;
    @Inject
    ProductsActivity activity;
    @Inject
    RxBus rxBus;
    @Inject
    RxBusLocal rxBusLocal;
    private ProductsListAdapter productsAdapter;
    private CategoryAdapter categoryAdapter, subCategoryAdapter;
    private ItemTouchHelper touchHelper;
    private static final int CATEGORY = 0;
    private static final int SUBCATEGORY = 1;
    private static final int PRODUCT = 2;
    private final static String ADD = "added";
    private final static String UPDATE = "update";
    private static final String DELETE = "delete";
    private final static String SUBCAT_OPENED = "subcategory";
    private final static String PRODUCT_OPENED = "product";
    private final static String CATEGORY_OPENED = "category";
    ArrayList<Disposable> subscriptions;

    @Override
    protected int getLayout() {
        return R.layout.choose_product_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.setActiveElements(preferencesHelper.getActiveItemVisibility());
        switchShowActive.setChecked(preferencesHelper.getActiveItemVisibility());
        presenter.setViewsVisibility(CATEGORY);
        presenter.setCategoryRecyclerView();
        categoryMode();
//        switchShowActive.setOnCheckedChangeListener((compoundButton, state) -> {
//            presenter.setActiveElements(state);
//            presenter.refreshCategoryList();
//            presenter.refreshSubCategoryList();
//        });
    }

    @Override
    protected void rxConnections() {
        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBus.toObservable().subscribe(o -> {
                    if (o instanceof CategoryEvent) {
                        CategoryEvent event = (CategoryEvent) o;
                        if (event.getEventType().equals(ADD)) {
                            presenter.refreshCategoryList();
                        }
                        if (event.getEventType().equals(UPDATE)) {
                            presenter.refreshCategoryList();
//                            presenter.checkIsActive(event.getCategory());
                        }
                        if (event.getEventType().equals(DELETE)) {
                            presenter.setCategoryItems(0);
                            categoryAdapter.setPosition(0);
                            presenter.refreshCategoryList();
                            presenter.refreshSubCategoryList();
                        }
                    }
                    if (o instanceof SubCategoryEvent) {
                        SubCategoryEvent event = (SubCategoryEvent) o;
                        if (event.getEventType().equals(ADD)) {
                            presenter.refreshSubCategoryList();
                        }
                        if (event.getEventType().equals(UPDATE)) {
                            presenter.refreshSubCategoryList();
//                            presenter.checkIsActive(event.getSubCategory());
                        }
                        if (event.getEventType().equals(DELETE)) {
                            presenter.setSubCategoryItems(0);
                            subCategoryAdapter.setPosition(0);
                            presenter.refreshSubCategoryList();
                        }
                    }
                    if (o instanceof ProductEvent) {
                        ProductEvent event = (ProductEvent) o;
                        if (event.getEventType().equals(ADD)) {
                            presenter.refreshProductList();
                        }
                        if (event.getEventType().equals(UPDATE)) {
                            presenter.refreshProductList();
                        }
                    }
                    if (o instanceof MessageEvent) {
                        MessageEvent event = (MessageEvent) o;
                        if (event.getMessage().equals(CATEGORY_OPENED)) {
                            presenter.categoryFragmentOpened();
                        }
                    }
                    if (o instanceof MessageEvent) {
                        MessageEvent event = (MessageEvent) o;
                        if (event.getMessage().equals(SUBCAT_OPENED)) {
                            presenter.subCatFragmentOpened();
                        }
                    }
                }));
        subscriptions.add(
                rxBusLocal.toObservable().subscribe(o -> {

                    if (o instanceof MessageEvent) {
                        MessageEvent event = (MessageEvent) o;
                        if (event.getMessage().equals(PRODUCT_OPENED)) {
                            presenter.productFragmentOpened();
                        }
                    }
                }));
    }

    @Override
    public void setCategoryRecyclerViewItems(List<Category> categories) {
        rvCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryAdapter = new CategoryAdapter(categories, CATEGORY);
        rvCategory.setAdapter(categoryAdapter);
        presenter.setSubCategoryRecyclerView();
        presenter.setCategoryItems(preferencesHelper.getLastPositionCategory());
        categoryAdapter.setOnItemClickListener(new ClickableBaseAdapter.OnItemClickListener<Category>() {
            @Override
            public void onItemClicked(int position) {
                presenter.setCategoryItems(position);
                openCategory();
            }

            @Override
            public void onItemClicked(Category category) {
                if (category != null) {
                    subCategoryAdapter.setPosition(preferencesHelper.getLastPositionSubCategory(String.valueOf(category.getId())));
                }
            }
        });
        categoryAdapter.setMoveListener((fromPosition, toPosition) -> presenter.setCategoryPositions(fromPosition, toPosition));
        ((SimpleItemAnimator) rvCategory.getItemAnimator()).setSupportsChangeAnimations(false);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(categoryAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvCategory);
        categoryAdapter.setPosition(preferencesHelper.getLastPositionCategory());
    }

    @Override
    public void setSubCategoryRecyclerView(List<Category> subCategories) {
        rvSubCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        subCategoryAdapter = new CategoryAdapter(subCategories, SUBCATEGORY);
        rvSubCategory.setAdapter(subCategoryAdapter);
        if (subCategories.size() > 1)
            subCategoryAdapter.setPosition(preferencesHelper.getLastPositionSubCategory(String.valueOf(subCategories.get(1).getParentId())));
        subCategoryAdapter.setOnItemClickListener(new ClickableBaseAdapter.OnItemClickListener<Category>() {
            @Override
            public void onItemClicked(int position) {
                openSubCategory();
                presenter.setSubCategoryItems(position);
            }

            @Override
            public void onItemClicked(Category subcategory) {
            }
        });
        subCategoryAdapter.setMoveListener((fromPosition, toPosition) -> presenter.setSubCategoryPositions(fromPosition, toPosition));
        ((SimpleItemAnimator) rvSubCategory.getItemAnimator()).setSupportsChangeAnimations(false);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(subCategoryAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvSubCategory);
    }

    @Override
    public void setProductRecyclerView(List<Product> products) {
//        rvProduct.setLayoutManager(new GridLayoutManager(getContext(), 4));
//        productsAdapter = new ProductsListAdapter(products, presenter, PRODUCT, this);
//        rvProduct.setAdapter(productsAdapter);
//        ((SimpleItemAnimator) rvProduct.getItemAnimator()).setSupportsChangeAnimations(false);
//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(productsAdapter);
//        touchHelper = new ItemTouchHelper(callback);
//        touchHelper.attachToRecyclerView(rvProduct);
    }

    @Override
    public void updateCategoryItems() {
        categoryAdapter.notifyDataSetChangedWithFirstItem();
    }

    @Override
    public void updateProductItems() {
        productsAdapter.notifyDataSetChangedWithZeroButton();
    }

    @Override
    public void updateSubCategoryItems() {
        subCategoryAdapter.notifyDataSetChangedWithFirstItem();
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
    public void sendSubCategoryEvent(Category subCategory, String event) {
        rxBusLocal.send(new SubCategoryEvent(subCategory, event));
        closeKeyboard(tvCategory, getContext());
    }

    @Override
    public void setCategoryAdapterPosition(int position) {
        categoryAdapter.setPosition(position);
    }

    @Override
    public void setSubCategoryAdapterPosition(int position) {
        subCategoryAdapter.setPosition(position);
    }

    @Override
    public void sendProductEvent(Product product, String event) {
        rxBusLocal.send(new ProductEvent(product, event));
    }

    @Override
    public void sendCategoryEvent(Category category, String event) {
        rxBusLocal.send(new CategoryEvent(category, event));
        closeKeyboard(tvCategory, getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.removeListners(subscriptions);
    }
}
