package com.jim.multipos.ui.product_class.presenters;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.core.RxForPresenter;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.employee.Schedule;
import com.jim.multipos.data.db.model.intosystem.NamePhotoPathId;
import com.jim.multipos.ui.product_class.ProductClassActivity;
import com.jim.multipos.ui.product_class.ProductClassView;
import com.jim.multipos.ui.product_class.fragments.AddProductClassFragment;
import com.jim.multipos.ui.product_class.fragments.AddProductClassView;
import com.jim.multipos.ui.product_class.fragments.ProductClassListView;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.GlobalEventsConstants;
import com.jim.multipos.utils.rxevents.ProductClassEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by developer on 29.08.2017.
 */

public class ProductClassListPresenterImpl  extends BasePresenterImpl<ProductClassListView> implements ProductClassListPresenter {
    private ProductClassListView view;
    private DatabaseManager databaseManager;
    private RxBusLocal rxBusLocal;
    private List<ProductClass> productClasses;
    public ProductClassListPresenterImpl(DatabaseManager databaseManager,RxBusLocal rxBusLocal, RxBus rxBus, ProductClassListView productClassListView){
        super(productClassListView);
        this.databaseManager = databaseManager;
        this.rxBusLocal = rxBusLocal;
        this.view = productClassListView;

    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        databaseManager.getAllProductClass().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(productClasses1 -> {
            productClasses = productClasses1;
            productClasses.add(0,null);
            view.setItemsRecyclerView(productClasses);
        });
    }



    @Override
    public void onAddProductClass(ProductClass productClass) {
        databaseManager.getAllProductClass().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(productClasses1 -> {
            productClasses.clear();
            productClasses.addAll(productClasses1);
            productClasses.add(0,null);
            view.reshreshView();
        });
    }

    @Override
    public void onUpdateProductClass(ProductClass productClass) {
        databaseManager.getAllProductClass().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(productClasses1 -> {
            productClasses.clear();
            productClasses.addAll(productClasses1);
            productClasses.add(0,null);
            view.reshreshView();
        });
    }



    @Override
    public void pressedAddButton() {
        rxBusLocal.send(new ProductClassEvent(null, AddProductClassFragment.ADD_PRODUCT_CLASS));
    }

    @Override
    public void pressedItem(int pos) {
        rxBusLocal.send(new ProductClassEvent(productClasses.get(pos),AddProductClassFragment.CLICK_PRODUCT_CLASS));

    }
}
