package com.jim.multipos.ui.product.presenter;


import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.ui.product.view.ProductListView;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DEV on 17.08.2017.
 */
@PerFragment
public class ProductListPresenterImpl extends BasePresenterImpl<ProductListView> implements ProductListPresenter {
   private DatabaseManager databaseManager;
    private List<Category> categoryList, subCategoryList;
    private List<Product> productList;
    private PreferencesHelper preferencesHelper;
    private final static String PARENT = "parent";
    private final static String CLICK = "click";

    @Inject
    public ProductListPresenterImpl(ProductListView view,DatabaseManager databaseManager, PreferencesHelper preferencesHelper) {
        super(view);
        this.databaseManager = databaseManager;
        this.preferencesHelper = preferencesHelper;
        categoryList = new ArrayList<>();
        subCategoryList = new ArrayList<>();
        productList = new ArrayList<>();
    }

    @Override
    public void setViewsVisibility(int mode) {
        view.setViewsVisibility(mode);
    }

    @Override
    public void setCategoryRecyclerView() {
        databaseManager.getAllCategories().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(categories -> {
            categoryList = categories;
            view.setCategoryRecyclerViewItems(categoryList);
        });
    }

    @Override
    public void setSubCategoryRecyclerView(List<Category> subCategories) {
        subCategoryList = subCategories;
        view.setSubCategoryRecyclerView(subCategoryList);
        view.subCategoryMode();
        view.setViewsVisibility(1);
    }

    @Override
    public void setProductRecyclerView() {
//        int selectedPos = preferencesHelper.getLastPositionSubCategory() - 1;
//        if (selectedPos > -1 && subCategoryList.get(selectedPos) != null) {
//            databaseManager.getAllProductPositions(subCategoryList.get(selectedPos)).subscribeOn(AndroidSchedulers.mainThread()).subscribe(products -> {
//                if (products != null)
//                    productList = CommonUtils.getAllNewVersionPlusId(products, "");
//                view.setProductRecyclerView(productList);
//            });
//        } else {
//            view.setProductRecyclerView(productList);
//        }
    }

    @Override
    public void openCategory() {
        view.openCategory();
    }

    @Override
    public void openSubCategory() {
        view.openSubCategory();
    }

    @Override
    public void openProduct() {
        view.openProduct();
    }

    @Override
    public void setCategoryItems(int selectedPosition) {
        if (selectedPosition != 0) {
            preferencesHelper.setLastPositionCategory(selectedPosition);
            view.sendCategoryEvent(categoryList.get(selectedPosition), CLICK);
            view.setCategoryName(categoryList.get(selectedPosition).getName());
//            refreshSubCategoryList();
        } else {
            preferencesHelper.setLastPositionCategory(selectedPosition);
            view.allInvisible();
            view.sendCategoryEvent(null, CLICK);
        }
    }

    @Override
    public void setSubCategoryItems(int selectedPosition) {
//        if (selectedPosition != -1) {
//            preferencesHelper.setLastPositionSubCategory(selectedPosition);
//            view.sendSubCategoryEvent(subCategoryList.get(selectedPosition), CLICK);
//            view.setSubCategoryName(subCategoryList.get(selectedPosition).getName());
//            refreshProductList();
//        } else {
//            preferencesHelper.setLastPositionSubCategory(selectedPosition);
//            view.categoryMode();
//            view.sendSubCategoryEvent(null, CLICK);
//        }
    }

    @Override
    public void setProductItems(int selectedPosition) {
        int productPosition;
        if (selectedPosition != -1) {
            productPosition = selectedPosition;
            preferencesHelper.setLastPositionProduct(selectedPosition);
            view.sendProductEvent(productList.get(productPosition), CLICK);
            view.setProductName(productList.get(selectedPosition).getName());
        } else {
            productPosition = selectedPosition;
            view.sendProductEvent(null, CLICK);
            view.subCategoryMode();
        }
    }

    @Override
    public void refreshCategoryList() {
        databaseManager.getAllCategories().subscribeOn(AndroidSchedulers.mainThread()).subscribe(categories -> {
            categoryList.clear();
            categoryList.addAll(categories);
            view.updateCategoryItems();
        });
    }

    @Override
    public void refreshSubCategoryList() {
//        int selectedPos = preferencesHelper.getLastPositionCategory();
//        databaseManager.getAllSubCategoryPositions(categoryList.get(selectedPos)).subscribeOn(AndroidSchedulers.mainThread()).subscribe(subCategories -> {
//            subCategoryList.clear();
//            subCategoryList.addAll(subCategories);
//            view.updateSubCategoryItems();
//        });
    }

    @Override
    public void productFragmentOpened() {
//        view.sendSubCategoryEvent(subCategoryList.get(preferencesHelper.getLastPositionSubCategory()), PARENT);
//        if (productPosition != -1) {
//            view.sendProductEvent(productList.get(productPosition), CLICK);
//        } else {
//            view.sendProductEvent(null, CLICK);
//        }
    }

    @Override
    public void categoryFragmentOpened() {
        if (preferencesHelper.getLastPositionCategory() != -1){
            view.sendCategoryEvent(categoryList.get(preferencesHelper.getLastPositionCategory()), CLICK);
        } else   view.sendCategoryEvent(null, CLICK);
    }

    @Override
    public void subCatFragmentOpened() {
//        view.sendCategoryEvent(categoryList.get(preferencesHelper.getLastPositionCategory()), PARENT);
//        if (preferencesHelper.getLastPositionSubCategory() != -1){
//            view.sendSubCategoryEvent(subCategoryList.get(preferencesHelper.getLastPositionSubCategory()), CLICK);
//        } else   view.sendSubCategoryEvent(null, CLICK);
    }

    @Override
    public void refreshProductList() {
//        int selectedPos = preferencesHelper.getLastPositionSubCategory();
//        databaseManager.getAllProductPositions(subCategoryList.get(selectedPos)).subscribeOn(AndroidSchedulers.mainThread()).subscribe(products -> {
//            productList.clear();
//            productList.addAll(CommonUtils.getAllNewVersionPlusId(products, ""));
//             view.updateProductItems();
//        });
    }
}
