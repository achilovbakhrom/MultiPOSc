package com.jim.multipos.ui.mainpospage.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.mainpospage.adapter.SquareViewCategoryAdapter;
import com.jim.multipos.ui.mainpospage.presenter.ProductSquareViewPresenter;

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
        SquareViewCategoryAdapter categoryAdapter = new SquareViewCategoryAdapter(categories);
        rvCategory.setAdapter(categoryAdapter);
        presenter.setSubCategoryRecyclerView();
        categoryAdapter.setOnItemClickListener(new ClickableBaseAdapter.OnItemClickListener<Category>() {
            @Override
            public void onItemClicked(int position) {

            }

            @Override
            public void onItemClicked(Category item) {
                presenter.setClickedCategory(item);
            }
        });
    }

    @Override
    public void setSubCategoryRecyclerView(List<Category> subCategories) {
        rvSubcategory.setLayoutManager(new LinearLayoutManager(getContext()));
        SquareViewCategoryAdapter subcategoryAdapter = new SquareViewCategoryAdapter(subCategories);
        rvSubcategory.setAdapter(subcategoryAdapter);
        subcategoryAdapter.setOnItemClickListener(new ClickableBaseAdapter.OnItemClickListener<Category>() {
            @Override
            public void onItemClicked(int position) {

            }

            @Override
            public void onItemClicked(Category item) {
                presenter.setClickedSubCategory(item);
            }
        });
    }

    @Override
    public void setProductRecyclerView(List<Product> products) {

    }
}
