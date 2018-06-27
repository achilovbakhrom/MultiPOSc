package com.jim.multipos.ui.product_last;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.product_last.helpers.CategoryAddEditMode;

import java.util.List;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

public interface ProductPresenter extends Presenter {
    void categorySelected(Category category);
    void subcategorySelected(Category category);
    void addCategory(String name, String description, boolean isActive);
    void addProduct(String name, String barcode, String sku, String photoPath, boolean isActive, int costCurrencyPos,
                    int priceCurrencuyPos, int productClassPos, int unitCategoryPos, String description, Double resultPrice, int selectedPosition);
    List<Category> getSubcategories(Category category);
    boolean isSubcategoryNameUnique(String categoryName, String subcategoryName);
    boolean isCategoryNameUnique(String categoryName);
    boolean isProductNameUnique(String productName, Long categoryId);
    CategoryAddEditMode getMode();
    void setMode(CategoryAddEditMode mode);
    Category getCategory();
    void setCategory(Category category);
    Category getCategoryById(Long id);
    void deleteCategory();
    void deleteProduct();
    void productSelected(Product product);
    DatabaseManager getDatabaseManager();
    void unitCategorySelected(int position);
    void showActivesToggled();
    boolean backPressFinish();
    void setProductClass(ProductClass productClass);
    void initDataForProduct();
    void initDataForList();
    void finishActivity();
    void openProduct(Product product);
    void unitCategorySelectedWithPosition(int position, int unitPos);
    boolean isProductNameExists(String name);
    List<ProductClass> updateProductClass();
    boolean isProductSkuExists(String sku);
    void onOkPressed();
    void onSearchTextChange(String s);
    void setBarcodeSearchMode(boolean barcodeMode);
    void setNameSearchMode(boolean nameMode);
    void setSkuSearchMode(boolean skuMode);
    void setSearchProduct(Product product);
}
