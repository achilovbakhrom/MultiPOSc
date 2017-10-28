package com.jim.multipos.ui.product_last.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.ui.product_last.ProductActivity;
import com.jim.multipos.ui.product_last.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

public class ProductListFragment extends BaseFragment  {

    @BindView(R.id.rvCategory)
    RecyclerView categories;
    @BindView(R.id.rvSubCategory)
    RecyclerView subcategories;
    @BindView(R.id.rvProduct)
    RecyclerView products;

    @Override
    protected int getLayout() {
        return R.layout.choose_product_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initCategoriesAndSubcategories();
    }

    private void initCategoriesAndSubcategories() {
        categories.setLayoutManager(new LinearLayoutManager(getContext()));
        CategoryAdapter categoryAdapter = new CategoryAdapter(((ProductActivity) getContext()).getPresenter().getCategories());
        categoryAdapter.setOnItemClickListener(new ClickableBaseAdapter.OnItemClickListener<Category>() {
            @Override
            public void onItemClicked(int position) {}

            @Override
            public void onItemClicked(Category item) {
                ((ProductActivity) getContext()).getPresenter().categorySelected(item);
            }
        });
        categories.setAdapter(categoryAdapter);

        CategoryAdapter subcategoryAdapter = new CategoryAdapter(new ArrayList<>());
        subcategoryAdapter.setOnItemClickListener(new ClickableBaseAdapter.OnItemClickListener<Category>() {
            @Override
            public void onItemClicked(int position) {}

            @Override
            public void onItemClicked(Category item) {
                ((ProductActivity) getContext()).getPresenter().subcategorySelected(item);
            }
        });
        subcategories.setLayoutManager(new LinearLayoutManager(getContext()));
        subcategories.setAdapter(subcategoryAdapter);
    }

    @Override
    protected boolean isAndroidInjectionEnabled() {
        return false;
    }

    public void addToCategoryList(Category category) {
        if (categories.getAdapter() != null) {
            ((CategoryAdapter) categories.getAdapter()).addItem(category);
        }
    }

    public void addToSubcategoryLIst(Category category) {
        if (subcategories.getAdapter() != null) {
            ((CategoryAdapter) subcategories.getAdapter()).addItem(category);
        }
    }

    public void setListToSubcategoryList(List<Category> categories) {
        if (subcategories.getAdapter() != null) {
            ((CategoryAdapter) subcategories.getAdapter()).setItems(categories);
        }
    }

    public void setListToCategoryList(List<Category> categories) {
        if (this.categories.getAdapter() != null) {
            ((CategoryAdapter) this.categories.getAdapter()).setItems(categories);
        }
    }

    public void clearSubcategoryList() {
        if (subcategories.getAdapter() != null) {
            ((CategoryAdapter) subcategories.getAdapter()).removeAllItems();
        } else {
            CategoryAdapter adapter = new CategoryAdapter(new ArrayList<>());
            subcategories.setAdapter(adapter);
        }
    }

    public void editSubcategoryItem(Category category) {
        ((CategoryAdapter) subcategories.getAdapter()).editItem(category);
    }

    public void editCartegoryItem(Category category) {
        ((CategoryAdapter) categories.getAdapter()).editItem(category);
    }

    public void selectSubcategoryListItem(int position) {
        if (subcategories.getAdapter() != null) {
            ((CategoryAdapter) subcategories.getAdapter()).setSelectedPosition(position);
        }

    }

}
