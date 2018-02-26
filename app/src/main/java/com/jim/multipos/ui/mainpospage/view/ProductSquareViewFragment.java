package com.jim.multipos.ui.mainpospage.view;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.mainpospage.adapter.SquareViewCategoryAdapter;
import com.jim.multipos.ui.mainpospage.adapter.SquareViewProductAdapter;
import com.jim.multipos.ui.mainpospage.connection.MainPageConnection;
import com.jim.multipos.ui.mainpospage.presenter.ProductSquareViewPresenter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.MessageEvent;
import com.jim.multipos.utils.rxevents.EditEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

import static com.jim.multipos.ui.product_last.ProductPresenterImpl.CATEGORY_ADD;
import static com.jim.multipos.ui.product_last.ProductPresenterImpl.CATEGORY_DELETE;
import static com.jim.multipos.ui.product_last.ProductPresenterImpl.CATEGORY_UPDATE;
import static com.jim.multipos.ui.product_last.ProductPresenterImpl.PRODUCT_ADD;
import static com.jim.multipos.ui.product_last.ProductPresenterImpl.PRODUCT_DELETE;
import static com.jim.multipos.ui.product_last.ProductPresenterImpl.PRODUCT_UPDATE;

/**
 * Created by Sirojiddin on 27.10.2017.
 */

public class ProductSquareViewFragment extends BaseFragment implements ProductSquareView {

    @BindView(R.id.rvCategory)
    RecyclerView rvCategory;
    @BindView(R.id.rvSubcategory)
    RecyclerView rvSubcategory;
    @BindView(R.id.rvProduct)
    RecyclerView rvProduct;
    @Inject
    ProductSquareViewPresenter presenter;
    @Inject
    RxBus rxBus;
    @Inject
    RxBusLocal rxBusLocal;
    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    MainPageConnection mainPageConnection;
    private SquareViewCategoryAdapter categoryAdapter, subcategoryAdapter;
    private SquareViewProductAdapter productAdapter;
    private static final String CATEGORY_TITLE = "category_title";
    private static final String SUBCATEGORY_TITLE = "subcategory_title";
    public static final String OPEN_PRODUCT = "open_product";
    private ArrayList<Disposable> subscriptions;

    @Override
    protected int getLayout() {
        return R.layout.square_product_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.setCategoryRecyclerView();
    }

    @Override
    protected void rxConnections() {
        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBus.toObservable().subscribe(o -> {
                    if (o instanceof MessageEvent) {
                        MessageEvent event = (MessageEvent) o;
                        switch (event.getMessage()) {
                            case CATEGORY_ADD:
                            case CATEGORY_DELETE:
                            case CATEGORY_UPDATE: {
                                presenter.setCategoryRecyclerView();
                                break;
                            }
                        }
                    }
                    if (o instanceof EditEvent) {
                        EditEvent event = (EditEvent) o;
                        switch (event.getMessage()) {
                            case PRODUCT_ADD:
                            case PRODUCT_DELETE:
                            case PRODUCT_UPDATE: {
                                presenter.setCategoryRecyclerView();
                                break;
                            }
                        }
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.removeListners(subscriptions);
    }

    @Override
    public void setCategoryRecyclerViewItems(List<Category> categories) {
        rvCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryAdapter = new SquareViewCategoryAdapter(categories);
        rvCategory.setAdapter(categoryAdapter);
        if (categories.size() > 0) {
            presenter.setSelectedCategory(preferencesHelper.getLastPositionCategory());
            categoryAdapter.setSelected(preferencesHelper.getLastPositionCategory());
            mainPageConnection.sendSelectedCategory(categories.get(preferencesHelper.getLastPositionCategory()), CATEGORY_TITLE);
        } else mainPageConnection.sendSelectedCategory(null, CATEGORY_TITLE);
        categoryAdapter.setOnItemClickListener(new ClickableBaseAdapter.OnItemClickListener<Category>() {
            @Override
            public void onItemClicked(int position) {
                preferencesHelper.setLastPositionCategory(position);
            }

            @Override
            public void onItemClicked(Category item) {
                presenter.setClickedCategory(item);
                mainPageConnection.sendSelectedCategory(item, CATEGORY_TITLE);
            }
        });
    }

    @Override
    public void setSubCategoryRecyclerView(List<Category> subCategories) {
        rvSubcategory.setLayoutManager(new LinearLayoutManager(getContext()));
        subcategoryAdapter = new SquareViewCategoryAdapter(subCategories);
        rvSubcategory.setAdapter(subcategoryAdapter);
        subcategoryAdapter.setOnItemClickListener(new ClickableBaseAdapter.OnItemClickListener<Category>() {
            @Override
            public void onItemClicked(int position) {
                preferencesHelper.setLastPositionSubCategory(String.valueOf(subCategories.get(position).getParentId()), position);
            }

            @Override
            public void onItemClicked(Category item) {
                presenter.setClickedSubCategory(item);
                mainPageConnection.sendSelectedCategory(item, SUBCATEGORY_TITLE);
            }
        });
        if (subCategories.size() > 0) {
            presenter.setSelectedSubCategory(preferencesHelper.getLastPositionSubCategory(String.valueOf(subCategories.get(0).getParentId())));
            subcategoryAdapter.setSelected(preferencesHelper.getLastPositionSubCategory(String.valueOf(subCategories.get(0).getParentId())));
            mainPageConnection.sendSelectedCategory(subCategories.get(preferencesHelper.getLastPositionSubCategory(String.valueOf(subCategories.get(0).getParentId()))), SUBCATEGORY_TITLE);
        } else mainPageConnection.sendSelectedCategory(null, SUBCATEGORY_TITLE);
    }

    @Override
    public void setProductRecyclerView(List<Product> products) {
        rvProduct.setLayoutManager(new GridLayoutManager(getContext(), 4));
        productAdapter = new SquareViewProductAdapter(products);
        rvProduct.setAdapter(productAdapter);
        productAdapter.setOnItemClickListener(new ClickableBaseAdapter.OnItemClickListener<Product>() {
            @Override
            public void onItemClicked(int position) {

            }

            @Override
            public void onItemClicked(Product item) {
                mainPageConnection.addProductToOrder(item.getId());
            }
        });
    }

    @Override
    public void refreshCategories(List<Category> categoryList) {
        categoryAdapter.setItems(categoryList);
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshSubCategories(List<Category> subCategoryList) {
        subcategoryAdapter.setItems(subCategoryList);
        subcategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshProducts(List<Product> productList) {
        productAdapter.setItems(productList);
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public void setSelectedCategory(int position) {
        categoryAdapter.setSelected(position);
    }

    @Override
    public void setSelectedSubCategory(int position) {
        subcategoryAdapter.setSelected(position);
    }

    @Override
    public void sendEvent(Category category, String subcategoryTitle) {
        mainPageConnection.sendSelectedCategory(category, subcategoryTitle);
    }

    public void onShow() {
        presenter.updateTitles();
    }
}
