package com.jim.multipos.ui.product_last;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.products.VendorProductCon;
import com.jim.multipos.ui.product_last.fragment.CategoryAddEditFragment;
import com.jim.multipos.ui.product_last.fragment.ProductAddEditFragment;
import com.jim.multipos.ui.product_last.fragment.ProductListFragment;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.TestUtils;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.rxevents.main_order_events.ProductEvent;
import com.jim.multipos.utils.rxevents.product_events.CategoryEvent;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import lombok.Getter;

import static com.jim.multipos.utils.managers.BarcodeScannerManager.DEVICE_NAME;

/**
 * Created by Achilov Bakhrom on 10/26/17.
 */

public class ProductActivity extends DoubleSideActivity implements ProductView {

    public static final String CATEGORY_FRAGMENT = CategoryAddEditFragment.class.getName();
    public static final String PRODUCT_FRAGMENT = ProductAddEditFragment.class.getName();

    @Inject
    @Getter
    ProductPresenter presenter;

    @Inject
    @Getter


    RxPermissions permissions;

    @Inject
    RxBus rxBus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TestUtils.createUnits(presenter.getDatabaseManager(), this);
        TestUtils.createCurrencies(presenter.getDatabaseManager(), this);
        addCategoryListFragment();
        addProductAddEditFragment();
        addCategoryAddEditFragment();
        presenter.onCreateView(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    private void addProductAddEditFragment() {
        addFragmentWithTagToLeft(new ProductAddEditFragment(), PRODUCT_FRAGMENT);
    }

    private void addCategoryAddEditFragment() {
        addFragmentWithTagToLeft(new CategoryAddEditFragment(), CATEGORY_FRAGMENT);
    }

    @Override
    public void addToProductList(Product product) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.addProductToProductsList(product);
        }
    }

    public void addCategoryListFragment() {
        addFragmentToRight(new ProductListFragment());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.DEFAULT_TYPE;
    }

    @Override
    public void addToCategoryList(Category category) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.addToCategoryList(category);
        }
    }

    @Override
    public void addToSubcategoryList(Category category) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.addToSubcategoryLIst(category);
        }
    }


    @Override
    public void clearSubcategoryList() {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.clearSubcategoryList();
        }
    }

    @Override
    public void setListToSubcategoryList(List<Category> categories) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.setListToSubcategoryList(categories);
        }
    }

    @Override
    public void setListToCategoryList(List<Category> categories) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.setListToCategoryList(categories);
        }
    }

    @Override
    public void suchCategoryNameExists(String name) {
        UIUtils.showAlert(this, getString(R.string.ok), getString(R.string.warning_such_category_name_exists),
                getString(R.string.warning_such_category_name_exists_message) + " " + name, () -> {
                    Log.d("sss", "suchCategoryNameExists: user accepted existing name warning");
                });
    }

    @Override
    public void suchSubcategoryNameExists(String name) {
        UIUtils.showAlert(this, getString(R.string.ok), getString(R.string.warning_such_subcategory_name_exists_title),
                getString(R.string.warning_subcategory_name_exists) + " " + name, () -> {
                    Log.d("sss", "suchSubcategoryNameExists: user accepted existing name warning");
                });
    }

    @Override
    public void editCategory(Category category) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.editCartegoryItem(category);
        }
    }

    @Override
    public void editSubcategory(Category category) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.editSubcategoryItem(category);
        }
    }

    @Override
    public void showCannotDeleteActiveItemDialog() {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.showCannotDeleteActiveItemDialog();
        }
    }

    @Override
    public void showListMustBeEmptyDialog() {
        UIUtils.showAlert(this, getString(R.string.ok), getString(R.string.cannot_delete_empty_list_title),
                getString(R.string.cannot_delete_empty_list_title), () -> Log.d("sss", "onButtonClicked: "));
    }

    @Override
    public void showInventoryStateShouldBeEmptyDialog() {
        UIUtils.showAlert(this, getString(R.string.ok), getString(R.string.warning),
                "You can not change the vendor who has products available", () -> Log.d("sss", "onButtonClicked: "));
    }

    @Override
    public void sendProductChangeEvent(int type, Product oldProduct, Product newProduct) {
        ProductEvent event = new ProductEvent(oldProduct, type);
        event.setNewProduct(newProduct);
        rxBus.send(event);
    }

    @Override
    public void sendProductEvent(int type, Product product) {
        rxBus.send(new ProductEvent(product, type));
    }

    @Override
    public void selectSubcategoryListItem(Long id) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.selectSubcategoryListItem(id);
        }
    }

    @Override
    public Long getSubcategorySelectedPosition() {
        return null;
    }


    @Override
    public Category getSubcategoryByPosition(int position) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        Category result = null;
        if (fragment != null) {
            result = fragment.getSelectedSubcategory();
        }
        return result;
    }


    @Override
    public void selectAddCategoryItem() {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.selectAddCategoryItem();
        }
    }

    @Override
    public void selectAddSubcategoryItem() {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.selectAddSubcategoryItem();
        }
    }

    @Override
    public void selectCategory(Long id) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.selectCategory(id);
        }
    }

    @Override
    public void selectSubcategory(Long id) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.selectSubcategory(id);
        }
    }

    @Override
    public Category getSelectedCategory() {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            return fragment.getSelectedCategory();
        }
        return null;
    }

    @Override
    public Category getSelectedSubcategory() {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            return fragment.getSelectedSubcategory();
        }
        return null;
    }

    @Override
    public void addCategory(Category category) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.addCategory(category);
        }
    }

    @Override
    public void addSubcategory(Category category) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.addSubcategory(category);
        }
    }

    @Override
    public void deleteCategory(Category category) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.deleteCategory(category);
        }
    }

    @Override
    public void deleteSubcategory(Category category) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.deleteSubcategory(category);
        }
    }

    @Override
    public void setListToCategories(List<Category> categories) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.setListToCategories(categories);
        }
    }

    @Override
    public void setListToSubcategories(List<Category> subcategories) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.setListToSubcategoryList(subcategories);
        }
    }

    @Override
    public void initRightSide(List<Category> categories) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.init(categories);
        }
    }

    private CategoryAddEditFragment getCategoryAddEditFragment() {
        ProductAddEditFragment productAddEditFragment = (ProductAddEditFragment) activityFragmentManager.findFragmentByTag(PRODUCT_FRAGMENT);
        if (productAddEditFragment != null) {
            activityFragmentManager.beginTransaction().hide(productAddEditFragment).commit();
        } else {
            Log.d("sss", "getCategoryAddEditFragment: product fragment is null, maybe not added");
        }
        CategoryAddEditFragment categoryAddEditFragment = (CategoryAddEditFragment) activityFragmentManager.findFragmentByTag(CATEGORY_FRAGMENT);
        if (categoryAddEditFragment != null) {
            activityFragmentManager.beginTransaction().show(categoryAddEditFragment).commit();
            return categoryAddEditFragment;
        } else {
            Log.d("sss", "getCategoryAddEditFragment: category fragment is null, maybe not added");
        }
        return null;
    }

    private ProductAddEditFragment getProductAddEditFragment() {
        CategoryAddEditFragment categoryAddEditFragment = (CategoryAddEditFragment) activityFragmentManager.findFragmentByTag(CATEGORY_FRAGMENT);
        if (categoryAddEditFragment != null) {
            activityFragmentManager.beginTransaction().hide(categoryAddEditFragment).commit();
        } else {
            Log.d("sss", "getCategoryAddEditFragment: category fragment is null, maybe not added");
        }
        ProductAddEditFragment productAddEditFragment = (ProductAddEditFragment) activityFragmentManager.findFragmentByTag(PRODUCT_FRAGMENT);
        if (productAddEditFragment != null) {
            activityFragmentManager.beginTransaction().show(productAddEditFragment).commit();
            return productAddEditFragment;
        } else {
            Log.d("sss", "getCategoryAddEditFragment: product fragment is null, maybe not added");
        }
        return null;
    }

    @Override
    public void openAddCategoryMode() {
        CategoryAddEditFragment fragment = getCategoryAddEditFragment();
        if (fragment != null) {
            fragment.setCategoryAddMode();
        }
    }

    @Override
    public void openAddSubcategoryMode(String parentName) {
        CategoryAddEditFragment fragment = getCategoryAddEditFragment();
        if (fragment != null) {
            fragment.setSubcategoryAddMode(parentName);
        }
    }

    @Override
    public void openEditCategoryMode(String name, String description, boolean isActive) {
        CategoryAddEditFragment fragment = getCategoryAddEditFragment();
        if (fragment != null) {
            fragment.setCategoryEditMode(name, description, isActive);
        }
    }

    @Override
    public void openEditSubcategoryMode(String name, String description, boolean isActive, String parentName) {
        CategoryAddEditFragment fragment = getCategoryAddEditFragment();
        if (fragment != null) {
            fragment.setSubcategoryEditMode(name, description, isActive, parentName);
        }
    }

    @Override
    public List<Category> getCategories() {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            return fragment.getCategories();
        }
        return null;
    }

    @Override
    public List<Product> getProducts() {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            return fragment.getProducts();
        }
        return null;
    }

    @Override
    public List<Category> getSubcategories() {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            return fragment.getSubcategories();
        }
        return null;
    }

    @Override
    public void setListToProducts(List<Product> products) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.setListToProductsList(products);
        }
    }

    @Override
    public void unselectSubcategoryList() {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.unselectSubcategoriesList();
        }
    }

    @Override
    public void unselectProductsList() {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.unselectProductsList();
        }
    }

    @Override
    public void clearProductList() {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.clearProductList();
        }
    }

    @Override
    public void openProductAddMode() {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            fragment.openAddMode();
        }
    }

    @Override
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
                                    double price) {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            fragment.openEditMode(
                    name,
                    barCode,
                    sku,
                    isActive,
                    priceCurrencyAbbr,
                    costCurrencyAbbr,
                    productClassPos,
                    unitCategoryPos,
                    units,
                    unitPos,
                    vendors,
                    description,
                    url,
                    price
            );
        }
    }

    @Override
    public void initProductForm(String[] unitCategoryList, String[] unitList, List<ProductClass> productClasses, String currencyAbbr) {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            fragment.initProductAddEditFragment(unitCategoryList, unitList, productClasses, currencyAbbr);
        }
    }

    @Override
    public void closeKeyboard() {
        UIUtils.closeKeyboard(this.getCurrentFocus(), this);
    }

    @Override
    public void setCategoryPath(String name) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.setCategoryPath(name);
        }
    }

    @Override
    public void setSubcategoryPath(String name) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.setSubcategoryPath(name);
        }
    }

    @Override
    public void unselectCategoryList() {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.unselectCategoryList();
        }
    }

    @Override
    public void showDiscardChangesDialog(UIUtils.AlertListener listener) {
        UIUtils.showAlert(this, getString(R.string.yes), getString(R.string.no), getString(R.string.discard_changes),
                getString(R.string.warning_discard_changes), listener);
    }


    @Override
    public String getName() {
        CategoryAddEditFragment fragment = getCategoryAddEditFragment();
        if (fragment != null) {
            return fragment.getName();
        }
        return "";
    }

    @Override
    public String getDescription() {
        CategoryAddEditFragment fragment = getCategoryAddEditFragment();
        if (fragment != null) {
            return fragment.getDescription();
        }
        return "";
    }

    @Override
    public boolean isActive() {
        CategoryAddEditFragment fragment = getCategoryAddEditFragment();
        if (fragment != null) {
            return fragment.isActive();
        }
        return true;
    }

    @Override
    public void showDeleteDialog(UIUtils.AlertListener listener) {
        UIUtils.showAlert(this, getString(R.string.yes), getString(R.string.no), getString(R.string.delete),
                getString(R.string.warning_delete), listener);
    }

    @Override
    public void showEditDialog(UIUtils.AlertListener listener) {
        UIUtils.showAlert(this, getString(R.string.yes), getString(R.string.no), getString(R.string.edit),
                getString(R.string.update_message), listener);
    }

    @Override
    public void setUnitsToProductsAddEdit(String[] units, int unitPos) {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            fragment.setUnits(units, unitPos);
        }
    }

    @Override
    public String getProductName() {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            return fragment.getProductName();
        }
        return "";
    }

    @Override
    public String getBarCode() {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            return fragment.getBarCode();
        }
        return "";
    }

    @Override
    public String getSku() {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            return fragment.getSku();
        }
        return "";
    }

    @Override
    public int getUnitCategorySelectedPos() {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            return fragment.getUnitCategorySelectedPos();
        }
        return 0;
    }

    @Override
    public int getUnitSelectedPos() {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            return fragment.getUnitSelectedPos();
        }
        return 0;
    }

    @Override
    public Double getPrice() {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            return fragment.getPrice();
        }
        return 0.0d;
    }

    @Override
    public String getCost() {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            return fragment.getCost();
        }
        return "";
    }

    @Override
    public List<Long> getVendorSelectedPos() {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            return fragment.getVendors();
        }
        return new ArrayList<>();
    }

    @Override
    public String getProductClassSelectedPos() {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            return fragment.getProductClassSelectedPos();
        }
        return "";
    }

    @Override
    public boolean getProductIsActive() {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            return fragment.getIsActive();
        }
        return true;
    }

    @Override
    public void editProduct(Product product) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.editProductItem(product);
        }
    }

    @Override
    public void selectProductListItem(Long id) {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.selectProductListItem(id);
        }
    }

    @Override
    public void selectAddProductListItem() {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            fragment.selectAddProductListItem();
        }
    }

    @Override
    public void openVendorChooserDialog(List<Vendor> vendors) {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            fragment.openVendorChooserDialog(vendors);
        }
    }

    @Override
    public void openChooseProductCostDialog(List<String> vendors, List<VendorProductCon> costs) {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            fragment.openChooseProductCostDialog(vendors, costs);
        }
    }

    @Override
    public void setVendorNameToAddEditProductFragment(String vendorName) {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            fragment.setVendorName(vendorName);
        }
    }

    @Override
    public boolean isActiveVisible() {
        ProductListFragment fragment = (ProductListFragment) getCurrentFragmentRight();
        if (fragment != null) {
            return fragment.isActiveEnabled();
        }
        return false;
    }

    @Override
    public void setCostValue(String result) {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            fragment.setCostValue(result);
        }
    }

    @Override
    public void saveProduct(boolean isBigger) {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            fragment.saveProduct(isBigger);
        }
    }

    @Override
    public String getPhotoPath() {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            return fragment.getPhotoPath();
        }
        return "";
    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public void openCategoryFragment() {
        ProductAddEditFragment productAddEditFragment = (ProductAddEditFragment) activityFragmentManager.findFragmentByTag(PRODUCT_FRAGMENT);
        if (productAddEditFragment != null) {
            activityFragmentManager.beginTransaction().hide(productAddEditFragment).commit();
        } else {
            Log.d("sss", "getCategoryAddEditFragment: product fragment is null, maybe not added");
        }
        CategoryAddEditFragment categoryAddEditFragment = (CategoryAddEditFragment) activityFragmentManager.findFragmentByTag(CATEGORY_FRAGMENT);
        if (categoryAddEditFragment != null) {
            activityFragmentManager.beginTransaction().show(categoryAddEditFragment).commit();
        } else {
            Log.d("sss", "getCategoryAddEditFragment: category fragment is null, maybe not added");
        }
    }

    @Override
    public void sendCategoryEvent(Category category, int event) {
        rxBus.send(new CategoryEvent(category, event));
    }

    @Override
    public void showCannotDeleteItemWithPlusValue(double value) {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            fragment.showCannotDeleteItemWithPlusValue(value);
        }
    }

    @Override
    public void showCannotDeleteItemWithMinusValue(double value) {
        ProductAddEditFragment fragment = getProductAddEditFragment();
        if (fragment != null) {
            fragment.showCannotDeleteItemWithMinusValue(value);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent objEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyUp(keyCode, objEvent);
    }


    public void onBackPressed() {
        if (presenter.backPressFinish())
            finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    String barcode = "";

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getDevice().getName().equals(DEVICE_NAME)) {
            char pressedKey = (char) event.getUnicodeChar();
            barcode += pressedKey;
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                ProductAddEditFragment fragment = getProductAddEditFragment();
                if (fragment != null) {
                    fragment.setBarcode(barcode);
                }
                barcode = "";
            }
        }
        return true;
    }
}
