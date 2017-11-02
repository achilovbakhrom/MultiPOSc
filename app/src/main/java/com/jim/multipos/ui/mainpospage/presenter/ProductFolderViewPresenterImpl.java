package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.intosystem.FolderItem;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.operations.CategoryOperations;
import com.jim.multipos.data.operations.ProductOperations;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.mainpospage.view.ProductFolderView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.jim.multipos.utils.CategoryUtils.isSubcategory;

/**
 * Created by Sirojiddin on 31.10.2017.
 */

public class ProductFolderViewPresenterImpl extends BasePresenterImpl<ProductFolderView> implements ProductFolderViewPresenter {

    private CategoryOperations categoryOperations;
    private ProductOperations productOperations;
    private PreferencesHelper preferencesHelper;
    private List<FolderItem> folderItems;
    private Category category;
    private FolderItem item;
    private static final int CATEGORY = 0;
    private static final int SUBCATEGORY = 1;
    private static final int PRODUCT = 2;
    private static final String CATEGORY_TITLE = "category_title";
    private static final String SUBCATEGORY_TITLE = "subcategory_title";

    @Inject
    protected ProductFolderViewPresenterImpl(ProductFolderView view, DatabaseManager databaseManager, PreferencesHelper preferencesHelper) {
        super(view);
        this.categoryOperations = databaseManager.getCategoryOperations();
        this.productOperations = databaseManager.getProductOperations();
        this.preferencesHelper = preferencesHelper;
        folderItems = new ArrayList<>();
    }

    @Override
    public void setFolderItemsRecyclerView() {
        categoryOperations.getAllActiveCategories().subscribe(categories -> {
            view.sendCategoryEvent(null, SUBCATEGORY_TITLE);
            for (Category category : categories) {
                FolderItem folderItem = new FolderItem();
                folderItem.setCategory(category);
                categoryOperations.getAllActiveSubCategories(category).subscribe(categories1 -> {
                    folderItem.setSize(categories1.size());
                    folderItems.add(folderItem);
                });
            }
            view.setFolderItemRecyclerView(folderItems);
        });
    }

    @Override
    public void selectedItem(FolderItem item) {
        this.item = item;
        if (item.getCategory() != null) {
            if (isSubcategory(item.getCategory())) {
                folderItems.clear();
                view.sendCategoryEvent(item.getCategory(), SUBCATEGORY_TITLE);
                productOperations.getAllActiveProducts(item.getCategory()).subscribe(products -> {
                    for (Product product : products) {
                        FolderItem folderItem = new FolderItem();
                        folderItem.setProduct(product);
                        folderItems.add(folderItem);
                    }
                    view.refreshProductList(folderItems, PRODUCT);
                    view.setBackItemVisibility(true);
                });
            } else {
                this.category = item.getCategory();
                view.sendCategoryEvent(category, CATEGORY_TITLE);
                view.sendCategoryEvent(null, SUBCATEGORY_TITLE);
                folderItems.clear();
                categoryOperations.getAllActiveSubCategories(item.getCategory()).subscribe(subcategories -> {
                    for (Category subcategory : subcategories) {
                        FolderItem folderItem = new FolderItem();
                        folderItem.setCategory(subcategory);
                        productOperations.getAllActiveProducts(subcategory).subscribe(products -> {
                            folderItem.setSize(products.size());
                            folderItems.add(folderItem);
                        });
                    }
                    view.refreshProductList(folderItems, SUBCATEGORY);
                    view.setBackItemVisibility(true);
                });
            }
        } else if (item.getProduct() != null) {
            view.setSelectedProduct(item.getProduct());
        }
    }

    @Override
    public void returnBack(int mode) {
        switch (mode) {
            case SUBCATEGORY:
                view.setBackItemVisibility(false);
                folderItems.clear();
                view.sendCategoryEvent(null, SUBCATEGORY_TITLE);
                categoryOperations.getAllActiveCategories().subscribe(categories -> {
                    for (Category category : categories) {
                        FolderItem folderItem = new FolderItem();
                        folderItem.setCategory(category);
                        categoryOperations.getAllActiveSubCategories(category).subscribe(categories1 -> {
                            folderItem.setSize(categories1.size());
                            folderItems.add(folderItem);
                        });
                    }
                    view.refreshProductList(folderItems, CATEGORY);
                });
                break;
            case PRODUCT:
                folderItems.clear();
                view.sendCategoryEvent(null, SUBCATEGORY_TITLE);
                categoryOperations.getAllActiveSubCategories(category).subscribe(subcategories -> {
                    for (Category subcategory : subcategories) {
                        FolderItem folderItem = new FolderItem();
                        folderItem.setCategory(subcategory);
                        productOperations.getAllActiveProducts(subcategory).subscribe(products -> {
                            folderItem.setSize(products.size());
                            folderItems.add(folderItem);
                        });
                    }
                    view.refreshProductList(folderItems, SUBCATEGORY);
                    view.setBackItemVisibility(true);
                });
                break;
        }
    }

    @Override
    public void setSelectedItem(int position) {
        if (item.getCategory() != null) {
            if (isSubcategory(item.getCategory())) {
                preferencesHelper.setLastPositionSubCategory(String.valueOf(item.getCategory().getParentId()), position);
            } else {
                preferencesHelper.setLastPositionCategory(position);
            }
        }
    }

}
