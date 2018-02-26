package com.jim.multipos.ui.product_last;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.VendorProductCon;
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
    Long getSubcategorySelectedPosition();
    Category getSubcategoryByPosition(int position);

    //new time
    void selectAddCategoryItem();
    void selectAddSubcategoryItem();
    void selectCategory(Long id);
    void selectSubcategory(Long id);
    void selectProductListItem(Long id);
    Category getSelectedCategory();
    Category getSelectedSubcategory();
    void editCategory(Category category);
    void editSubcategory(Category category);
    void editProduct(Product product);
    void addCategory(Category category);
    void addSubcategory(Category category);
    void deleteCategory(Category category);
    void deleteSubcategory(Category category);
    void setListToCategories(List<Category> categories);
    void setListToSubcategories(List<Category> subcategories);
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
    public void openProductEditMode(String name,
                                    String barCode,
                                    String sku,
                                    boolean isActive,
                                    String priceCurrencyAbbr,
                                    String costCurrencyAbbr,
                                    String productClassPos,
                                    int unitCategoryPos,
                                    String[] units,
                                    int unitPos,
                                    List<Long> vendors,
                                    String description,
                                    String url,
                                    double price);
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
    String getCost();
    List<Long> getVendorSelectedPos();
    String getProductClassSelectedPos();
    boolean getProductIsActive();
    void openVendorChooserDialog(List<Vendor> vendors);
    void openChooseProductCostDialog(List<String> vendors, List<VendorProductCon> costs);
    void setVendorNameToAddEditProductFragment(String vendorName);
    boolean isActiveVisible();
    void setCostValue(String result);
    void saveProduct(boolean isBigger);
    String getPhotoPath();
    void finishActivity();
    void openCategoryFragment();
    void sendEvent(String event);
    void showCannotDeleteItemWithPlusValue(double value);
    void showCannotDeleteItemWithMinusValue(double value);
    void showInventoryStateShouldBeEmptyDialog();
    void sendProductEvent(Long id, Long newId, String message);
}
