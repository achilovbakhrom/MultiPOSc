package com.jim.multipos.ui.product_class.presenters;

import android.util.Log;

import com.jim.multipos.core.RxForPresenter;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.employee.Schedule;
import com.jim.multipos.data.db.model.intosystem.NameIdProdClass;
import com.jim.multipos.ui.product_class.ProductClassActivity;
import com.jim.multipos.ui.product_class.fragments.AddProductClassView;
import com.jim.multipos.ui.service_fee.Constants;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.GlobalEventsConstants;
import com.jim.multipos.utils.rxevents.ProductClassEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by developer on 29.08.2017.
 */

public class AddProductClassPresenterImp extends AddProductClassConnector implements AddProductClassPresenter {


    private AddProductClassView view;
    private RxBus rxBus;
    private DatabaseManager databaseManager;
    private RxBusLocal rxBusLocal;
    private ProductClass productClass;
    private ArrayList<String> parentSpinner;
    List<ProductClass> productClasses;
    public AddProductClassPresenterImp(RxBus rxBus, DatabaseManager databaseManager, RxBusLocal rxBusLocal){
        this.rxBus = rxBus;
        this.databaseManager = databaseManager;
        this.rxBusLocal = rxBusLocal;
    }
    @Override
    public void init(AddProductClassView view) {
        this.view = view;
        initConnectors(rxBus,rxBusLocal);
        databaseManager
                .getAllProductClass()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(productClasses -> {
                     this.productClasses = productClasses;
                    makeDataForParentSpinner();
                });
        if(productClass!=null)
            view.fillView(productClass);
    }

    @Override
    public void onClickProductClass(ProductClass productClass) {
        this.productClass = productClass;
        if(view!=null){
            view.fillView(productClass);
            makeDataForParentSpinner();
        }
    }

    @Override
    public void addProductClass() {
        productClass = null;
        makeDataForParentSpinner();

        view.onAddNew();
    }


    @Override
    public void onDestroyView() {
        view = null;
    }

    @Override
    public void onSaveButtonPress(String className, String parentId, boolean active) {
        if(productClass==null){
            productClass = new ProductClass();
            productClass.setName(className);
            productClass.setParentId(parentId);
            productClass.setActive(active);
            databaseManager.insertProductClass(productClass).subscribe(aLong -> {
                productClasses.add(productClass);
                Collections.sort(productClasses,(productClass, t1) -> t1.getActive().compareTo(productClass.getActive()));
                rxBus.send(new ProductClassEvent(productClass, GlobalEventsConstants.ADD));
                productClass = null;
                makeDataForParentSpinner();
                view.onAddNew();
            });

        }else {

            productClass.setName(className);
            productClass.setParentId(parentId);
            productClass.setActive(active);
            databaseManager.insertProductClass(productClass).subscribe(aLong -> {
                for (int i = 0; i < productClasses.size(); i++) {
                    if(productClasses.get(i).getId().equals(productClass.getId())){
                        productClasses.remove(i);
                        productClasses.add(productClass);
                    }
                }
                Collections.sort(productClasses,(productClass, t1) -> t1.getActive().compareTo(productClass.getActive()));
                rxBus.send(new ProductClassEvent(productClass, GlobalEventsConstants.UPDATE));
                productClass = null;
                makeDataForParentSpinner();
                view.onAddNew();
            });

        }
    }

    @Override
    public void onCancelButtonPresed() {

    }

    private void makeDataForParentSpinner(){
        String parent = "";
        ArrayList<NameIdProdClass> classesList = new ArrayList<>();
        for (int i = 0; i < productClasses.size(); i++) {
            if(productClass!=null)
                if(productClass.getParentId()!=null)
                if(productClass.getParentId().equals(productClasses.get(i).getId())){
                    parent = productClasses.get(i).getId();
                }
            if(productClass!=null) {
                if (!productClass.getId().equals(productClasses.get(i).getId()))
                    classesList.add(new NameIdProdClass(productClasses.get(i).getName(), productClasses.get(i).getId()));
            }
            else    classesList.add(new NameIdProdClass(productClasses.get(i).getName(), productClasses.get(i).getId()));

        }
            view.setParentSpinnerItems(classesList);
            view.setParentSpinnerPosition(parent);
    }
}
