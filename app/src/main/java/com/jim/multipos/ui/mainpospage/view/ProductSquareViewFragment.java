package com.jim.multipos.ui.mainpospage.view;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.mainpospage.adapter.SquareViewCategoryAdapter;
import com.jim.multipos.ui.mainpospage.adapter.SquareViewProductAdapter;
import com.jim.multipos.ui.mainpospage.presenter.ProductSquareViewPresenter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.CategoryEvent;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

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
    PreferencesHelper preferencesHelper;
    private SquareViewCategoryAdapter categoryAdapter, subcategoryAdapter;
    private SquareViewProductAdapter productAdapter;
    private static final String CATEGORY_TITLE = "category_title";
    private static final String SUBCATEGORY_TITLE = "subcategory_title";

    @Override
    protected int getLayout() {
        return R.layout.square_product_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.setCategoryRecyclerView();
    }

    @Override
    public void setCategoryRecyclerViewItems(List<Category> categories) {
        rvCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryAdapter = new SquareViewCategoryAdapter(categories);
        rvCategory.setAdapter(categoryAdapter);
        presenter.setSelectedCategory(preferencesHelper.getLastPositionCategory());
        categoryAdapter.setSelected(preferencesHelper.getLastPositionCategory());
        rxBus.send(new CategoryEvent(categories.get(preferencesHelper.getLastPositionCategory()), CATEGORY_TITLE));
        categoryAdapter.setOnItemClickListener(new ClickableBaseAdapter.OnItemClickListener<Category>() {
            @Override
            public void onItemClicked(int position) {
                preferencesHelper.setLastPositionCategory(position);
            }

            @Override
            public void onItemClicked(Category item) {
                presenter.setClickedCategory(item);
                rxBus.send(new CategoryEvent(item, CATEGORY_TITLE));
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
                rxBus.send(new CategoryEvent(item, SUBCATEGORY_TITLE));
            }
        });
        if (subCategories.size() > 0) {
            presenter.setSelectedSubCategory(preferencesHelper.getLastPositionSubCategory(String.valueOf(subCategories.get(0).getParentId())));
            subcategoryAdapter.setSelected(preferencesHelper.getLastPositionSubCategory(String.valueOf(subCategories.get(0).getParentId())));
            rxBus.send(new CategoryEvent(subCategories.get(preferencesHelper.getLastPositionSubCategory(String.valueOf(subCategories.get(0).getParentId()))), SUBCATEGORY_TITLE));
        }
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
                Toast.makeText(getContext(), item.getName(), Toast.LENGTH_SHORT).show();
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
        rxBus.send(new CategoryEvent(category, subcategoryTitle));
    }
}
