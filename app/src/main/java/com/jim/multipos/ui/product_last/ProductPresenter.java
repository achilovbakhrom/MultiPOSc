package com.jim.multipos.ui.product_last;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.Vendor;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.ui.product_last.helpers.CategoryAddEditMode;
import com.jim.multipos.ui.product_last.helpers.FragmentType;

import java.util.List;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

public interface ProductPresenter extends Presenter {

    void categorySelected(Category category);
    void subcategorySelected(Category category);
    void addCategory(String name, String description, boolean isActive);
    void addProduct(String name, Double price, Long createdDate, String barcode, String sku, String photoPath, boolean isActive, Currency costCurrency,
                    Currency priceCurrency, ProductClass productClass, Unit mainUnit, List<Unit> subunits, Vendor vendor, String description);
    List<Category> getSubcategories(Category category);
    boolean isSubcategoryNameUnique(String categoryName, String subcategoryName);
    boolean isCategoryNameUnique(String categoryName);
    CategoryAddEditMode getMode();
    void setMode(CategoryAddEditMode mode);
    Category getCategory();
    void setCategory(Category category);
    Category getCategoryById(Long id);
    void deleteCategory();
    void setCategoryItemsMoved();
    void setSubcategoryItemsMoved();
    void productSelected(Product product);
    DatabaseManager getDatabaseManager();
}
