package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.intosystem.FolderItem;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.operations.CategoryOperations;
import com.jim.multipos.data.operations.ProductOperations;
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
    private List<FolderItem> folderItems;
    private Category category;
    private static final int CATEGORY = 0;
    private static final int SUBCATEGORY = 1;
    private static final int PRODUCT = 2;

    @Inject
    protected ProductFolderViewPresenterImpl(ProductFolderView view, DatabaseManager databaseManager) {
        super(view);
        this.categoryOperations = databaseManager.getCategoryOperations();
        this.productOperations = databaseManager.getProductOperations();
        folderItems = new ArrayList<>();
    }

    @Override
    public void setFolderItemsRecyclerView() {
        categoryOperations.getAllActiveCategories().subscribe(categories -> {
            for (Category category : categories) {
                FolderItem folderItem = new FolderItem();
                folderItem.setCategory(category);
                folderItems.add(folderItem);
            }
            view.setFolderItemRecyclerView(folderItems);
        });
    }

    @Override
    public void selectedItem(FolderItem item) {
        if (item.getCategory() != null) {
            if (isSubcategory(item.getCategory())) {
                folderItems.clear();
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
                folderItems.clear();
                categoryOperations.getAllActiveSubCategories(item.getCategory()).subscribe(subcategories -> {
                    for (Category subcategory : subcategories) {
                        FolderItem folderItem = new FolderItem();
                        folderItem.setCategory(subcategory);
                        folderItems.add(folderItem);
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
                categoryOperations.getAllActiveCategories().subscribe(categories -> {
                    for (Category category : categories) {
                        FolderItem folderItem = new FolderItem();
                        folderItem.setCategory(category);
                        folderItems.add(folderItem);
                    }
                    view.refreshProductList(folderItems, CATEGORY);
                });
                break;
            case PRODUCT:
                folderItems.clear();
                categoryOperations.getAllActiveSubCategories(category).subscribe(subcategories -> {
                    for (Category subcategory : subcategories) {
                        FolderItem folderItem = new FolderItem();
                        folderItem.setCategory(subcategory);
                        folderItems.add(folderItem);
                    }
                    view.refreshProductList(folderItems, SUBCATEGORY);
                    view.setBackItemVisibility(true);
                });
                break;
        }

    }
}
