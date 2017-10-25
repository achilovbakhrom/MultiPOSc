package com.jim.multipos.ui.product.presenter;


import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.intosystem.Activatable;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.product.view.ProductListView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.jim.multipos.utils.CategoryUtils.isSubcategory;

/**
 * Created by DEV on 17.08.2017.
 */
@PerFragment
public class ProductListPresenterImpl extends BasePresenterImpl<ProductListView> implements ProductListPresenter {
    private DatabaseManager databaseManager;
    private List<Category> categoryList, subCategoryList;
    private List<Product> productList;
    private PreferencesHelper preferencesHelper;
    private Category currentCategory;
    private final static String PARENT = "parent";
    private final static String CLICK = "click";
    private boolean showNonActive = false;

    @Inject
    public ProductListPresenterImpl(ProductListView view, DatabaseManager databaseManager, PreferencesHelper preferencesHelper) {
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
    public void setSubCategoryRecyclerView() {
        currentCategory = categoryList.get(preferencesHelper.getLastPositionCategory());
        if (currentCategory != null) {
            databaseManager.getSubCategories(currentCategory).subscribe(subCategories -> {
                subCategoryList = subCategories;
                view.subCategoryMode();
                view.setViewsVisibility(1);
                view.setSubCategoryRecyclerView(subCategoryList);
            });
        } else view.setSubCategoryRecyclerView(subCategoryList);

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
            currentCategory = categoryList.get(selectedPosition);
            view.sendCategoryEvent(currentCategory, CLICK);
            view.setCategoryName(currentCategory.getName());
            view.setViewsVisibility(1);
            refreshSubCategoryList();
        } else {
            preferencesHelper.setLastPositionCategory(selectedPosition);
            view.allInvisible();
            view.setViewsVisibility(0);
            view.sendCategoryEvent(null, CLICK);
        }
    }

    @Override
    public void setSubCategoryItems(int selectedPosition) {
        if (selectedPosition != 0) {
            preferencesHelper.setLastPositionSubCategory(String.valueOf(currentCategory.getId()), selectedPosition);
            view.sendSubCategoryEvent(subCategoryList.get(selectedPosition), CLICK);
            view.setSubCategoryName(subCategoryList.get(selectedPosition).getName());
            view.setViewsVisibility(2);
//            refreshProductList();
        } else {
            preferencesHelper.setLastPositionSubCategory(String.valueOf(currentCategory.getId()), selectedPosition);
            view.categoryMode();
            view.setViewsVisibility(1);
            view.sendSubCategoryEvent(null, CLICK);
        }
    }

    @Override
    public void setProductItems(int selectedPosition) {
//        int productPosition;
//        if (selectedPosition != -1) {
//            productPosition = selectedPosition;
//            preferencesHelper.setLastPositionProduct(selectedPosition);
//            view.sendProductEvent(productList.get(productPosition), CLICK);
//            view.setProductName(productList.get(selectedPosition).getName());
//        } else {
//            productPosition = selectedPosition;
//            view.sendProductEvent(null, CLICK);
//            view.subCategoryMode();
//        }
    }

    @Override
    public void refreshCategoryList() {
        databaseManager.getAllCategories().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(categories -> {
            categoryList.clear();
            categoryList.addAll(categories);
            view.updateCategoryItems();
        });
    }

    @Override
    public void refreshSubCategoryList() {
        if (currentCategory != null)
            databaseManager.getSubCategories(currentCategory).subscribe(subCategories -> {
                subCategoryList.clear();
                subCategoryList.addAll(subCategories);
                view.updateSubCategoryItems();
            });
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
        if (preferencesHelper.getLastPositionCategory() != 0) {
            view.sendCategoryEvent(categoryList.get(preferencesHelper.getLastPositionCategory()), CLICK);
        } else view.sendCategoryEvent(null, CLICK);
    }

    @Override
    public void subCatFragmentOpened() {
        view.sendCategoryEvent(categoryList.get(preferencesHelper.getLastPositionCategory()), PARENT);
        if (preferencesHelper.getLastPositionSubCategory(String.valueOf(currentCategory.getId())) != 0) {
            view.sendSubCategoryEvent(subCategoryList.get(preferencesHelper.getLastPositionSubCategory(String.valueOf(currentCategory.getId()))), CLICK);
        } else view.sendSubCategoryEvent(null, CLICK);
    }

    @Override
    public void setCategoryPositions(int fromPosition, int toPosition) {
        double positionFirst = categoryList.get(toPosition).getPosition();
        Category category = categoryList.get(fromPosition);
        if (fromPosition > toPosition) {
            if (toPosition == 1) {
                double positionNew = 0 + positionFirst / 2;
                category.setPosition(positionNew);
                databaseManager.replaceCategory(category).subscribe();
            } else {
                double positionSecond = categoryList.get(toPosition + 1).getPosition();
                double positionNew = positionFirst + (positionSecond - positionFirst) / 2;
                category.setPosition(positionNew);
                databaseManager.replaceCategory(category).subscribe();
            }
        } else {
            if (toPosition == categoryList.size() - 1) {
                category.setPosition(positionFirst + 1d);
                databaseManager.replaceCategory(category).subscribe();
            } else {
                double positionSecond = categoryList.get(toPosition + 1).getPosition();
                double positionNew = positionFirst + (positionSecond - positionFirst) / 2;
                category.setPosition(positionNew);
                databaseManager.replaceCategory(category).subscribe();
            }
        }
        int selectedPosition = preferencesHelper.getLastPositionCategory();
        if (fromPosition == selectedPosition) {
            preferencesHelper.setLastPositionCategory(toPosition);
        } else if (toPosition == selectedPosition) {
            preferencesHelper.setLastPositionCategory(fromPosition);
        }

    }

    @Override
    public void setSubCategoryPositions(int fromPosition, int toPosition) {
        double positionFirst = subCategoryList.get(toPosition).getPosition();
        Category category = subCategoryList.get(fromPosition);
        if (fromPosition > toPosition) {
            double positionSecond = subCategoryList.get(toPosition + 1).getPosition();
            double positionNew = positionFirst - (positionSecond - positionFirst) / 2;
            category.setPosition(positionNew);
            databaseManager.replaceCategory(category).subscribe();
        } else {
            if (toPosition == subCategoryList.size() - 1) {
                category.setPosition(positionFirst + 1d);
                databaseManager.replaceCategory(category).subscribe();
            } else {
                double positionSecond = subCategoryList.get(toPosition + 1).getPosition();
                double positionNew = positionFirst + (positionSecond - positionFirst) / 2;
                category.setPosition(positionNew);
                databaseManager.replaceCategory(category).subscribe();
            }
        }
        int selectedPosition = preferencesHelper.getLastPositionSubCategory(String.valueOf(currentCategory.getId()));
        if (fromPosition == selectedPosition) {
            preferencesHelper.setLastPositionSubCategory(String.valueOf(currentCategory.getId()), toPosition);
        } else if (toPosition == selectedPosition) {
            preferencesHelper.setLastPositionSubCategory(String.valueOf(currentCategory.getId()), fromPosition);
        }
    }

    @Override
    public void setActiveElements(boolean state) {
        this.showNonActive = state;
        preferencesHelper.setActiveItemVisibility(state);
    }

    @Override
    public void checkIsActive(Category category) {
//        if (!category.getIsActive()) {
//            if (showNonActive) {
//                if (isSubcategory(category))
//                    view.setSubCategoryAdapterPosition(subCategoryList.size() - 1);
//                else view.setCategoryAdapterPosition(categoryList.size() - 1);
//            } else {
//                if (isSubcategory(category)) {
//                    view.setSubCategoryAdapterPosition(0);
//                    setSubCategoryItems(0);
//                    openSubCategory();
//                } else {
//                    view.setCategoryAdapterPosition(0);
//                    setCategoryItems(0);
//                    openCategory();
//                }
//            }
//        } else {
//            if (showNonActive) {
//                if (isSubcategory(category)) {
//                    if (currentCategory != null) {
//                        databaseManager.getActiveSubcategories(currentCategory).subscribe(subcategories -> {
//                            if (preferencesHelper.getLastPositionSubCategory(String.valueOf(currentCategory.getId())) > subcategories.size()) {
//                                view.setSubCategoryAdapterPosition(subcategories.size());
//                                openSubCategory();
//                                setSubCategoryItems(preferencesHelper.getLastPositionSubCategory(String.valueOf(currentCategory.getId())));
//                                preferencesHelper.setLastPositionSubCategory(String.valueOf(currentCategory.getId()), subcategories.size());
//                            }
//                        });
//                    }
//                } else {
//                    databaseManager.getActiveCategories().subscribeOn(AndroidSchedulers.mainThread()).subscribe(categories -> {
//                        if (preferencesHelper.getLastPositionCategory() > categories.size()) {
//                            view.setCategoryAdapterPosition(categories.size());
//                            openCategory();
//                            setCategoryItems(preferencesHelper.getLastPositionCategory());
//                            preferencesHelper.setLastPositionCategory(categories.size());
//                        }
//                    });
//                }
//            }
//        }
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
