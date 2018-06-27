package com.jim.multipos.ui.product_last;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.utils.UIUtils;

import java.util.List;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

public interface ProductView extends BaseView {
    void addToCategoryList(Category category);
    void addToSubcategoryList(Category category);
    void addToProductList(Product product);
    void clearSubcategoryList();
    void setListToSubcategoryList(List<Category> categories);
    void setListToCategoryList(List<Category> categories);
    void suchCategoryNameExists(String name);
    void suchSubcategoryNameExists(String name);
    void showCannotDeleteActiveItemDialog();
    void selectSubcategoryListItem(Long position);

    //new time
    void selectAddCategoryItem();
    void selectAddSubcategoryItem();
    void selectCategory(Long id);
    void selectSubcategory(Long id);
    void selectProductListItem(Long id);
    void editCategory(Category category);
    void editSubcategory(Category category);
    void editProduct(Product product);
    void addCategory(Category category);
    void initRightSide(List<Category> categories);

    void openAddCategoryMode();
    void openAddSubcategoryMode(String parentName);
    void openEditCategoryMode(String name, String desctription, boolean isActive);
    void openEditSubcategoryMode(String name, String description, boolean isActive, String parentName);
    List<Category> getCategories();
    List<Category> getSubcategories();
    List<Product> getProducts();
    void setListToProducts(List<Product> products);
    void selectAddProductListItem();
    void unselectSubcategoryList();
    void unselectCategoryList();
    void unselectProductsList();
    void clearProductList();

    void openProductAddMode();
    void openProductEditMode(String name,
                             String barCode,
                             String sku,
                             boolean isActive,
                             String priceCurrencyAbbr,
                             String productClassPos,
                             int unitCategoryPos,
                             String[] units,
                             int unitPos,
                             String description,
                             String url,
                             double price, int stockKeepType);
    void initProductForm(String[] unitCategoryList, String[] unitList, List<ProductClass> productClasses, String currencyAbbr);
    void closeKeyboard();
    void setCategoryPath(String name);
    void setSubcategoryPath(String name);
    void showDiscardChangesDialog(UIUtils.AlertListener listener);
    String getName();
    String getDescription();
    boolean isActive();
    void showListMustBeEmptyDialog();
    void showDeleteDialog(UIUtils.AlertListener listener);
    void showEditDialog(UIUtils.AlertListener listener);
    void setUnitsToProductsAddEdit(String[] units, int unitPos);

    String getProductName();
    String getBarCode();
    String getSku();
    int getUnitCategorySelectedPos();
    int getUnitSelectedPos();
    Double getPrice();
    boolean getProductIsActive();
    boolean isActiveVisible();
    void saveProduct();
    String getPhotoPath();
    void finishActivity();
    void openCategoryFragment();
    void sendCategoryEvent(Category category, int event);
    void showCannotDeleteItemWithPlusValue(double value);
    void showCannotDeleteItemWithMinusValue(double value);
    void showInventoryStateShouldBeEmptyDialog();
    void sendProductChangeEvent(int type, Product oldProduct, Product newProduct);
    void sendProductEvent(int type, Product product);
    void setResultsList(List<Product> productList, String s);
    void addProductToOrderInCloseSelf();
}
