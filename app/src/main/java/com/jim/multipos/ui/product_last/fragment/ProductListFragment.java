package com.jim.multipos.ui.product_last.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.product_last.ProductActivity;
import com.jim.multipos.ui.product_last.adapter.CategoryAdapter;
import com.jim.multipos.ui.product_last.adapter.ProductAdapter;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.item_touch_helper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */
public class ProductListFragment extends BaseFragment {

    @BindView(R.id.rvCategory)
    RecyclerView categories;
    @BindView(R.id.rvSubCategory)
    RecyclerView subcategories;
    @BindView(R.id.rvProduct)
    RecyclerView products;

    @BindView(R.id.tvCategory)
    TextView categoryPath;

    @BindView(R.id.tvSubCategory)
    TextView subcategoryPath;

    @BindView(R.id.ivArrowFirst)
    ImageView firstArrow;

    @BindView(R.id.ivArrow)
    ImageView secordArrow;

    @BindView(R.id.switchShowActive)
    Switch isActiveEnabled;

    private final String IS_ACTIVE_KEY = "IS_ACTIVE_KEY";

    @Override
    protected int getLayout() {
        return R.layout.choose_product_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        isActiveEnabled.setChecked(preferences.getBoolean(IS_ACTIVE_KEY, false));
        ((ProductActivity) getContext()).getPresenter().initDataForList();
    }

    public void init(List<Category> categories) {
        firstArrow.setVisibility(View.GONE);
        secordArrow.setVisibility(View.GONE);
        CategoryAdapter categoryAdapter = new CategoryAdapter(categories, CategoryAdapter.CATEGORY_MODE);
        categoryAdapter.setOnItemClickListener(new ClickableBaseAdapter.OnItemClickListener<Category>() {
            @Override
            public void onItemClicked(int position) {
            }

            @Override
            public void onItemClicked(Category item) {
                ((ProductActivity) getContext()).getPresenter().categorySelected(item);
            }
        });
        this.categories.setLayoutManager(new LinearLayoutManager(getContext()));
        this.categories.setAdapter(categoryAdapter);
        categoryAdapter.setMoveListener((fromPosition, toPosition) -> {
            ((ProductActivity) getContext()).getPresenter().setCategoryItemsMoved();
        });
        ((SimpleItemAnimator) this.categories.getItemAnimator()).setSupportsChangeAnimations(false);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(categoryAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(this.categories);

        CategoryAdapter subcategoryAdapter = new CategoryAdapter(new ArrayList<>(), CategoryAdapter.SUBCATEGORY_MODE);
        subcategoryAdapter.setOnItemClickListener(new ClickableBaseAdapter.OnItemClickListener<Category>() {
            @Override
            public void onItemClicked(int position) {
            }

            @Override
            public void onItemClicked(Category item) {
                ((ProductActivity) getContext()).getPresenter().subcategorySelected(item);
            }
        });
        this.subcategories.setLayoutManager(new LinearLayoutManager(getContext()));
        this.subcategories.setAdapter(subcategoryAdapter);
        subcategoryAdapter.setMoveListener((fromPosition, toPosition) -> {
            ((ProductActivity) getContext()).getPresenter().setSubcategoryItemsMoved();
        });
        ((SimpleItemAnimator) this.subcategories.getItemAnimator()).setSupportsChangeAnimations(false);
        ItemTouchHelper.Callback scCallback = new SimpleItemTouchHelperCallback(subcategoryAdapter);
        ItemTouchHelper scTouchHelper = new ItemTouchHelper(scCallback);
        scTouchHelper.attachToRecyclerView(this.subcategories);

        ProductAdapter productAdapter = new ProductAdapter(new ArrayList<>());
        productAdapter.setOnItemClickListener(new ClickableBaseAdapter.OnItemClickListener<Product>() {
            @Override
            public void onItemClicked(int position) {
            }

            @Override
            public void onItemClicked(Product item) {
                ((ProductActivity) getContext()).getPresenter().productSelected(item);
            }
        });
        this.products.setLayoutManager(new GridLayoutManager(getContext(), 4));
        this.products.setAdapter(productAdapter);
        productAdapter.setMoveListener((fromPosition, toPosition) -> {
            ((ProductActivity) getContext()).getPresenter().setProductItemsMoved();
        });
        ((SimpleItemAnimator) this.products.getItemAnimator()).setSupportsChangeAnimations(false);
        ItemTouchHelper.Callback prCallback = new SimpleItemTouchHelperCallback(productAdapter);
        ItemTouchHelper prTouchHelper = new ItemTouchHelper(prCallback);
        prTouchHelper.attachToRecyclerView(this.products);
        isActiveEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            preferences.edit().putBoolean(IS_ACTIVE_KEY, isChecked).apply();
            ((ProductActivity) getContext()).getPresenter().showActivesToggled();
        });
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
            CategoryAdapter adapter = new CategoryAdapter(new ArrayList<>(), CategoryAdapter.SUBCATEGORY_MODE);
            subcategories.setAdapter(adapter);
        }
    }

    public void editSubcategoryItem(Category category) {
        ((CategoryAdapter) subcategories.getAdapter()).editItem(category);
    }

    public void editCartegoryItem(Category category) {
        ((CategoryAdapter) categories.getAdapter()).editItem(category);
    }

    public void editProductItem(Product product) {
        if (products.getAdapter() != null) {
            ((ProductAdapter) products.getAdapter()).editItem(product);
        }
    }

    public void selectSubcategoryListItem(Long id) {
        if (subcategories.getAdapter() != null) {
            ((CategoryAdapter) subcategories.getAdapter()).setSelectedPositionWithId(id);
        }

    }


    public Category getCategoryByPosition(int position) {
        if (subcategories.getAdapter() != null) {
            return ((CategoryAdapter) subcategories.getAdapter())
                    .getItems()
                    .get(position);
        }
        return null;
    }


    // new time

    public void selectAddCategoryItem() {
        if (categories.getAdapter() != null) {
            ((CategoryAdapter) categories.getAdapter()).setSelectedPosition(0);
        }
    }


    public void selectAddSubcategoryItem() {
        if (subcategories.getAdapter() != null) {
            ((CategoryAdapter) subcategories.getAdapter()).setSelectedPosition(0);
        }
    }


    public void selectCategory(Long id) {
        if (categories.getAdapter() != null) {
            ((CategoryAdapter) categories.getAdapter()).setSelectedPositionWithId(id);
        }
    }

    public void selectSubcategory(Long id) {
        if (subcategories.getAdapter() != null) {
            ((CategoryAdapter) subcategories.getAdapter()).setSelectedPositionWithId(id);
        }
    }

    public Category getSelectedCategory() {
        if (categories.getAdapter() != null) {
            ((CategoryAdapter) categories.getAdapter()).getSelectedItem();
        }
        return null;
    }

    public Category getSelectedSubcategory() {
        if (subcategories.getAdapter() != null) {
            ((CategoryAdapter) subcategories.getAdapter()).getSelectedItem();
        }
        return null;
    }

    public void editCategory(Category category) {
        if (categories.getAdapter() != null) {
            ((CategoryAdapter) categories.getAdapter()).editItem(category);
        }
    }

    public void editSubcategory(Category category) {
        if (subcategories.getAdapter() != null) {
            ((CategoryAdapter) subcategories.getAdapter()).editItem(category);
        }
    }

    public void addCategory(Category category) {
        if (categories.getAdapter() != null) {
            ((CategoryAdapter) categories.getAdapter()).addItem(category);
        }
    }

    public void addSubcategory(Category category) {
        if (subcategories.getAdapter() != null) {
            ((CategoryAdapter) subcategories.getAdapter()).addItem(category);
        }
    }

    public void deleteCategory(Category category) {
        if (categories.getAdapter() != null) {
            ((CategoryAdapter) categories.getAdapter()).removeItem(category);
        }
    }

    public void deleteSubcategory(Category category) {
        if (subcategories.getAdapter() != null) {
            ((CategoryAdapter) subcategories.getAdapter()).removeItem(category);
        }

    }

    public void setListToCategories(List<Category> categories) {
        if (this.categories.getAdapter() != null) {
            ((CategoryAdapter) this.categories.getAdapter()).setItems(categories);
        }
    }

    public void setListToSubcategories(List<Category> subcategories) {
        if (this.subcategories.getAdapter() != null) {
            ((CategoryAdapter) this.subcategories.getAdapter()).setItems(subcategories);
        }
    }

    public List<Category> getCategories() {
        if (this.categories.getAdapter() != null) {
            return ((CategoryAdapter) this.categories.getAdapter()).getItems();
        }
        return null;
    }

    public List<Category> getSubcategories() {
        if (this.subcategories.getAdapter() != null) {
            return ((CategoryAdapter) this.subcategories.getAdapter()).getItems();
        }
        return null;
    }

    public void setListToProductsList(List<Product> products) {
        if (this.products.getAdapter() != null) {
            ((ProductAdapter) this.products.getAdapter()).setItems(products);
        }
    }

    public void unselectSubcategoriesList() {
        if (this.subcategories.getAdapter() != null) {
            ((CategoryAdapter) this.subcategories.getAdapter()).unselect();
        }
    }

    public void unselectProductsList() {
        if (this.products.getAdapter() != null) {
            ((ProductAdapter) this.products.getAdapter()).unselect();
        }
    }

    public void clearProductList() {
        if (this.products.getAdapter() != null) {
            ((ProductAdapter) this.products.getAdapter()).removeAllItems();
        }
    }

    public void setCategoryPath(String name) {
        secordArrow.setVisibility(View.GONE);
        subcategoryPath.setText("");
        if (name == null) {
            firstArrow.setVisibility(View.GONE);
            categoryPath.setText("");
        } else {
            firstArrow.setVisibility(View.VISIBLE);
            categoryPath.setText(name);
        }
    }

    public void unselectCategoryList() {
        if (this.categories.getAdapter() != null) {
            ((CategoryAdapter) this.categories.getAdapter()).unselect();
        }
    }

    public void setSubcategoryPath(String name) {

        if (name == null) {
            secordArrow.setVisibility(View.GONE);
            subcategoryPath.setText("");
        } else {
            secordArrow.setVisibility(View.VISIBLE);
            subcategoryPath.setText(name);
        }
    }

    public void addProductToProductsList(Product product) {
        if (products.getAdapter() != null) {
            ((ProductAdapter) products.getAdapter()).addItem(product);
        }
    }

    public List<Product> getProducts() {
        if (this.products.getAdapter() != null) {
            return ((ProductAdapter) this.products.getAdapter()).getItems();
        }
        return null;
    }

    public void selectProductListItem(Long id) {
        if (this.products.getAdapter() != null) {
            ((ProductAdapter) this.products.getAdapter()).setSelectedPositionWithId(id);
        }
    }

    public void selectAddProductListItem() {
        if (this.products.getAdapter() != null) {
            ((ProductAdapter) this.products.getAdapter()).setSelectedPosition(0);
        }
    }

    public boolean isActiveEnabled() {
        return isActiveEnabled.isChecked();
    }

    public void showCannotDeleteActiveItemDialog() {
        UIUtils.showAlert(getContext(), getContext().getString(R.string.ok), getContext().getString(R.string.warning),
                getContext().getString(R.string.cannot_delete_active_item), () -> Log.d("sss", "onButtonClicked: "));
    }
}
