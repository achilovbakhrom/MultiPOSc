package com.jim.multipos.ui.product_last;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.VendorProductCon;
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
                    int priceCurrencuyPos, int productClassPos, int unitCategoryPos, List<Long> vendors, String description, Double resultPrice);
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
    void setCategoryItemsMoved();
    void setSubcategoryItemsMoved();
    void setProductItemsMoved();
    void productSelected(Product product);
    DatabaseManager getDatabaseManager();
    void unitCategorySelected(int position);
    void openVendorChooserDialog();
    void setProductCostDialog();
    void setVendorName(List<Long> vendors);
    void showActivesToggled();
    boolean backPressFinish();
    void setProductCosts(List<VendorProductCon> productConList);
    void comparePriceWithCost(double priceValue);
    void setProductClass(ProductClass productClass);
    void initDataForProduct();
    void initDataForList();
    void finishActivity();
}
