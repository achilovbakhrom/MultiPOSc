package com.jim.multipos.ui.product.presenter;


import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.intosystem.CategoryPosition;
import com.jim.multipos.data.db.model.intosystem.ProductPosition;
import com.jim.multipos.data.db.model.intosystem.SubCategoryPosition;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.SubCategory;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.utils.CommonUtils;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.ui.product.view.ProductListView;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.CategoryEvent;
import com.jim.multipos.utils.rxevents.ProductEvent;
import com.jim.multipos.utils.rxevents.SubCategoryEvent;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.jim.multipos.utils.rxevents.GlobalEventsConstants.CLICK;

/**
 * Created by DEV on 17.08.2017.
 */

public class ProductListPresenterImpl extends ProductListConnector implements ProductListPresenter {
    private ProductListView view;
    private DatabaseManager databaseManager;
    private RxBus rxBus;
    private List<Category> categoryList, tempCatList;
    private List<SubCategory> subCategoryList, tempSubCatList;
    private List<Product> productList, tempProductsList;
    private PreferencesHelper preferencesHelper;
    private RxBusLocal rxBusLocal;
    private int productPosition;

    public ProductListPresenterImpl(DatabaseManager databaseManager, RxBus rxBus, RxBusLocal rxBusLocal, PreferencesHelper preferencesHelper) {
        this.databaseManager = databaseManager;
        this.rxBus = rxBus;
        this.rxBusLocal = rxBusLocal;
        this.preferencesHelper = preferencesHelper;
        categoryList = new ArrayList<>();
        subCategoryList = new ArrayList<>();
        productList = new ArrayList<>();
        tempSubCatList = new ArrayList<>();
        tempCatList = new ArrayList<>();
        tempProductsList = new ArrayList<>();
        initConnectors(rxBus, rxBusLocal);
    }

    @Override
    public void onListCategoryPositionChanged() {
        List<CategoryPosition> categoryPositions = new ArrayList<>();
        tempCatList.addAll(categoryList);
        for (int i = tempCatList.size() - 1; i >= 0; i--) {
            if (tempCatList.get(i) == null) {
                tempCatList.remove(i);
            }
        }
        for (int i = 0; i < tempCatList.size(); i++) {
            CategoryPosition categoryPosition = new CategoryPosition();
            categoryPosition.setCategory(tempCatList.get(i));
            categoryPosition.setPosition(i);
            categoryPositions.add(categoryPosition);
        }
        databaseManager.addCategoryPositions(categoryPositions).subscribe();
        tempCatList.clear();
    }

    @Override
    public void onListSubCategoryPositionChanged() {
        int selectedPos = preferencesHelper.getLastPositionCategory();
        List<SubCategoryPosition> subCategoryPositions = new ArrayList<>();
        tempSubCatList.addAll(subCategoryList);
        for (int i = tempSubCatList.size() - 1; i >= 0; i--) {
            if (tempSubCatList.get(i) == null)
                tempSubCatList.remove(i);
        }
        for (int i = 0; i < tempSubCatList.size(); i++) {
            SubCategoryPosition subCategoryPosition = new SubCategoryPosition();
            subCategoryPosition.setSubCategory(tempSubCatList.get(i));
            subCategoryPosition.setSubCategoryId(tempSubCatList.get(i).getId());
            subCategoryPosition.setPosition(i);
            subCategoryPosition.setCategoryId(categoryList.get(selectedPos).getId());
            subCategoryPositions.add(subCategoryPosition);
        }
        databaseManager.addSubCategoryPositions(subCategoryPositions, categoryList.get(selectedPos)).subscribe();
        tempSubCatList.clear();
    }

    @Override
    public void onListProductPositionChanged() {
        int selectedPos = preferencesHelper.getLastPositionSubCategory();
        List<ProductPosition> productPositions = new ArrayList<>();
        tempProductsList.addAll(productList);
        for (int i = tempProductsList.size() - 1; i >= 0; i--) {
            if (tempProductsList.get(i) == null)
                tempProductsList.remove(i);
        }
        for (int i = 0; i < tempProductsList.size(); i++) {
            ProductPosition productPosition = new ProductPosition();
            productPosition.setProduct(tempProductsList.get(i));
            productPosition.setProductId(tempProductsList.get(i).getId());
            productPosition.setPosition(i);
            productPosition.setSubCategoryId(subCategoryList.get(selectedPos).getId());
            productPositions.add(productPosition);
        }
        databaseManager.addProductPositions(productPositions, subCategoryList.get(selectedPos)).subscribe();
        tempProductsList.clear();
    }

    @Override
    public void setViewsVisibility(int mode) {
        view.setViewsVisibility(mode);
    }

    @Override
    public void init(ProductListView view) {
        this.view = view;
        setCategoryRecyclerView();
        this.view.categoryMode();
        setRxListners();
    }

    @Override
    public void setCategoryRecyclerView() {
        databaseManager.getAllCategoryPositions().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(categories -> {
            categoryList = categories;
            view.setCategoryRecyclerViewItems(categoryList);
            setSubCategoryRecyclerView();
        });
    }

    @Override
    public void setSubCategoryRecyclerView() {
        int selectedPos = preferencesHelper.getLastPositionCategory();
        if (selectedPos > 0) {
            databaseManager.getAllSubCategoryPositions(categoryList.get(selectedPos)).subscribeOn(AndroidSchedulers.mainThread()).subscribe(subCategories -> {
                if (subCategories != null)
                    subCategoryList = subCategories;
                view.setSubCategoryRecyclerView(subCategoryList);
                setProductRecyclerView();
            });
        } else {
            view.setSubCategoryRecyclerView(subCategoryList);
            setProductRecyclerView();
        }
    }

    @Override
    public void setProductRecyclerView() {
        int selectedPos = preferencesHelper.getLastPositionSubCategory() -1;
        if (selectedPos > -1 && subCategoryList.get(selectedPos) != null) {
            databaseManager.getAllProductPositions(subCategoryList.get(selectedPos)).subscribeOn(AndroidSchedulers.mainThread()).subscribe(products -> {
                if (products != null)
                    productList = CommonUtils.getAllNewVersionPlusId(products, "");
                view.setProductRecyclerView(productList);
            });
        } else {
            view.setProductRecyclerView(productList);
        }
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
        if (selectedPosition != -1) {
            preferencesHelper.setLastPositionCategory(selectedPosition);
            rxBusLocal.send(new CategoryEvent(categoryList.get(selectedPosition), CLICK));
            view.setCategoryName(categoryList.get(selectedPosition).getName());
            refreshSubCategoryList();
        } else {

            preferencesHelper.setLastPositionCategory(selectedPosition);
            view.allInvisible();
            rxBusLocal.send(new CategoryEvent(null, CLICK));
        }
    }

    @Override
    public void setSubCategoryItems(int selectedPosition) {
        if (selectedPosition != -1) {
            preferencesHelper.setLastPositionSubCategory(selectedPosition);
            rxBusLocal.send(new SubCategoryEvent(subCategoryList.get(selectedPosition), CLICK));
            view.setSubCategoryName(subCategoryList.get(selectedPosition).getName());
            refreshProductList();
        } else {
            preferencesHelper.setLastPositionSubCategory(selectedPosition);
            view.categoryMode();
            rxBusLocal.send(new SubCategoryEvent(null, CLICK));
        }
    }

    @Override
    public void setProductItems(int selectedPosition) {
        if (selectedPosition != -1) {
            productPosition = selectedPosition;
            preferencesHelper.setLastPositionProduct(selectedPosition);
            rxBusLocal.send(new ProductEvent(productList.get(selectedPosition), CLICK));
            view.setProductName(productList.get(selectedPosition).getName());
        } else {
            productPosition = selectedPosition;
            rxBusLocal.send(new ProductEvent(null, CLICK));
            view.subCategoryMode();
        }
    }

    @Override
    public void refreshCategoryList() {
        databaseManager.getAllCategoryPositions().subscribeOn(AndroidSchedulers.mainThread()).subscribe(categories -> {
            categoryList.clear();
            categoryList.addAll(categories);
            view.updateCategoryItems();
        });
    }

    @Override
    public void refreshSubCategoryList() {
        int selectedPos = preferencesHelper.getLastPositionCategory();
        databaseManager.getAllSubCategoryPositions(categoryList.get(selectedPos)).subscribeOn(AndroidSchedulers.mainThread()).subscribe(subCategories -> {
            subCategoryList.clear();
            subCategoryList.addAll(subCategories);
            view.updateSubCategoryItems();
        });
    }

    @Override
    protected void productFragmentOpened() {
        rxBusLocal.send(new SubCategoryEvent(subCategoryList.get(preferencesHelper.getLastPositionSubCategory()), "parent"));
        if (productPosition != -1) {
            rxBusLocal.send(new ProductEvent(productList.get(productPosition), CLICK));
        } else {
            rxBusLocal.send(new ProductEvent(null, CLICK));
        }
    }

    @Override
    protected void subCatFragmentOpened() {
        rxBusLocal.send(new CategoryEvent(categoryList.get(preferencesHelper.getLastPositionCategory()), "parent"));
        if (preferencesHelper.getLastPositionSubCategory() != -1){
            rxBusLocal.send(new SubCategoryEvent(subCategoryList.get(preferencesHelper.getLastPositionSubCategory()), CLICK));
        } else rxBusLocal.send(new SubCategoryEvent(null, CLICK));
    }

    @Override
    public void refreshProductList() {
        int selectedPos = preferencesHelper.getLastPositionSubCategory();
        databaseManager.getAllProductPositions(subCategoryList.get(selectedPos)).subscribeOn(AndroidSchedulers.mainThread()).subscribe(products -> {
            productList.clear();
            productList.addAll(CommonUtils.getAllNewVersionPlusId(products, ""));
             view.updateProductItems();
        });
    }


    @Override
    public void onDestroyView() {
        this.view = null;
    }
}
