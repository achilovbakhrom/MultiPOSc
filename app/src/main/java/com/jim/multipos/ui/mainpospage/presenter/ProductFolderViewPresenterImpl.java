package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.intosystem.FolderItem;
import com.jim.multipos.data.db.model.inventory.InventoryState;
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
    private DatabaseManager databaseManager;
    private List<FolderItem> folderItems;
    private Category category;
    private static final int CATEGORY = 0;
    private static final int SUBCATEGORY = 1;
    private static final int PRODUCT = 2;
    private static final String CATEGORY_TITLE = "category_title";
    private static final String SUBCATEGORY_TITLE = "subcategory_title";
    private int mode = CATEGORY;
    private FolderItem folderItem;

    @Inject
    protected ProductFolderViewPresenterImpl(ProductFolderView view, DatabaseManager databaseManager) {
        super(view);
        this.categoryOperations = databaseManager.getCategoryOperations();
        this.productOperations = databaseManager.getProductOperations();
        this.databaseManager = databaseManager;
        folderItems = new ArrayList<>();
    }

    @Override
    public void setFolderItemsRecyclerView() {
        categoryOperations.getAllActiveCategories().subscribe(categories -> {
            view.sendCategoryEvent(null, SUBCATEGORY_TITLE);
            folderItems.clear();
            for (Category category : categories) {
                FolderItem folderItem = new FolderItem();
                folderItem.setCategory(category);
                int count = productOperations.getAllProductCount(category).blockingSingle();
                folderItem.setCount(count);
                folderItems.add(folderItem);
            }
            view.setFolderItemRecyclerView(folderItems);
        });
    }

    @Override
    public void selectedItem(FolderItem item) {
        this.folderItem = item;
        if (item.getCategory() != null) {
            if (isSubcategory(item.getCategory())) {
                mode = PRODUCT;
                folderItems.clear();
                view.sendCategoryEvent(item.getCategory(), SUBCATEGORY_TITLE);
                productOperations.getAllActiveProducts(item.getCategory()).subscribe(products -> {
                    for (Product product : products) {
                        FolderItem folderItem = new FolderItem();
                        folderItem.setProduct(product);
                        List<InventoryState> inventoryStates = databaseManager.getInventoryStatesByProductId(product.getId()).blockingSingle();
                        int count = 0;
                        for (InventoryState inventoryState : inventoryStates) {
                            count += inventoryState.getValue();
                        }
                        folderItem.setCount(count);
                        folderItems.add(folderItem);
                    }
                    view.refreshProductList(folderItems, PRODUCT);
                    view.setBackItemVisibility(true);
                });
            } else {
                mode = SUBCATEGORY;
                this.category = item.getCategory();
                view.sendCategoryEvent(category, CATEGORY_TITLE);
                view.sendCategoryEvent(null, SUBCATEGORY_TITLE);
                folderItems.clear();
                categoryOperations.getAllActiveSubCategories(item.getCategory()).subscribe(subcategories -> {
                    for (Category subcategory : subcategories) {
                        FolderItem folderItem = new FolderItem();
                        folderItem.setCategory(subcategory);
                        productOperations.getAllActiveProducts(subcategory).subscribe(products -> {
                            folderItem.setCount(products.size());
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
                this.mode = CATEGORY;
                view.setBackItemVisibility(false);
                folderItems.clear();
                view.sendCategoryEvent(null, CATEGORY_TITLE);
                view.sendCategoryEvent(null, SUBCATEGORY_TITLE);
                categoryOperations.getAllActiveCategories().subscribe(categories -> {
                    for (Category category : categories) {
                        FolderItem folderItem = new FolderItem();
                        folderItem.setCategory(category);
                        productOperations.getAllProductCount(category).subscribe(integer -> {
                            folderItem.setCount(integer);
                            folderItems.add(folderItem);
                        });
                    }
                    view.refreshProductList(folderItems, CATEGORY);
                });
                break;
            case PRODUCT:
                this.mode = SUBCATEGORY;
                folderItems.clear();
                view.sendCategoryEvent(null, SUBCATEGORY_TITLE);
                categoryOperations.getAllActiveSubCategories(category).subscribe(subcategories -> {
                    for (Category subcategory : subcategories) {
                        FolderItem folderItem = new FolderItem();
                        folderItem.setCategory(subcategory);
                        productOperations.getAllActiveProducts(subcategory).subscribe(products -> {
                            folderItem.setCount(products.size());
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
    public void updateProducts() {
        if (mode == PRODUCT){
            folderItems.clear();
            view.sendCategoryEvent(folderItem.getCategory(), SUBCATEGORY_TITLE);
            productOperations.getAllActiveProducts(folderItem.getCategory()).subscribe(products -> {
                for (Product product : products) {
                    FolderItem folderItem = new FolderItem();
                    folderItem.setProduct(product);
                    List<InventoryState> inventoryStates = databaseManager.getInventoryStatesByProductId(product.getId()).blockingSingle();
                    int count = 0;
                    for (InventoryState inventoryState : inventoryStates) {
                        count += inventoryState.getValue();
                    }
                    folderItem.setCount(count);
                    folderItems.add(folderItem);
                }
                view.refreshProductList(folderItems, PRODUCT);
                view.setBackItemVisibility(true);
            });
        }
    }
}
