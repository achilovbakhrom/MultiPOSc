package com.jim.multipos.ui.product_class.presenters;

import android.os.Bundle;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.ui.product_class.fragments.AddProductClassView;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.GlobalEventsConstants;
import com.jim.multipos.utils.rxevents.ProductClassEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by developer on 29.08.2017.
 */

public class AddProductClassPresenterImpl extends BasePresenterImpl<AddProductClassView> implements AddProductClassPresenter {




    private AddProductClassView view;
    private String none;
    private RxBus rxBus;
    private DatabaseManager databaseManager;
    private RxBusLocal rxBusLocal;
    private ProductClass productClass;
    List<ProductClass> productClasses;
    ArrayList<ProductClass> classesList;
    @Inject
    public AddProductClassPresenterImpl(RxBus rxBus, DatabaseManager databaseManager, RxBusLocal rxBusLocal, AddProductClassView view, @Named("none") String none){
        super(view);
        this.rxBus = rxBus;
        this.databaseManager = databaseManager;
        this.rxBusLocal = rxBusLocal;
        this.view = view;

        this.none = none;
    }

    @Override
    public void onCreateView(Bundle bundle) {
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



    public void onClickProductClass(ProductClass productClass) {
        this.productClass = productClass;
        if(view!=null){
            view.fillView(productClass);
            makeDataForParentSpinner();
        }
    }

    public void addProductClass() {
        productClass = null;
        makeDataForParentSpinner();

        view.onAddNew();
    }

    @Override
    public void deleteProductClass() {
        if(productClass!=null){
            productClass.setDeleted(true);
            productClass.setParentId(null);
            databaseManager.insertProductClass(productClass).subscribe((aLong, throwable) -> {
                for(int i = productClasses.size()-1;0<=i;i--){
                    if(productClasses.get(i).getId().equals(productClass.getId())){
                        productClasses.remove(i);
                    } else
                    if(productClasses.get(i).getParentId()!=null && productClasses.get(i).getParentId().equals(productClass.getId())){
                            productClasses.get(i).setParentId(null);
                            databaseManager.insertProductClass(productClasses.get(i)).subscribe();
                    }

                }
                rxBus.send(new ProductClassEvent(productClass, GlobalEventsConstants.DELETE));
                productClass = null;
                makeDataForParentSpinner();
                view.onAddNew();
            });
        }
    }


    @Override
    public void onDestroyView() {
        view = null;
    }

    @Override
    public void onSaveButtonPress(String className, int position, boolean active) {

        Long parentId = null;
        if(position!=0){
            parentId = classesList.get(position-1).getId();
        }
        if(productClass==null){
            productClass = new ProductClass();
            productClass.setName(className);
            productClass.setParentId(parentId);
            productClass.setActive(active);
            productClass.setNotModifyted(true);
            productClass.setCreatedDate(System.currentTimeMillis());
            databaseManager.insertProductClass(productClass).subscribe(aLong -> {
                productClasses.add(0,productClass);
                Collections.sort(productClasses,(productClass1, t1) -> t1.getActive().compareTo(productClass1.getActive()));

                rxBus.send(new ProductClassEvent(productClass, GlobalEventsConstants.ADD));
                productClass = null;
                makeDataForParentSpinner();
                view.onAddNew();
            });

        }else {

            productClass.setName(className);
            productClass.setParentId(parentId);
            productClass.setActive(active);
            productClass.setNotModifyted(true);
            databaseManager.insertProductClass(productClass).subscribe(aLong -> {
                for (int i = 0;i<productClasses.size();i++){
                    if(productClasses.get(i).getId().equals(productClass.getId())){
                        productClasses.set(i,productClass);
                    }
                }
                Collections.sort(productClasses,(productClass1, t1) -> t1.getCreatedDate().compareTo(productClass1.getCreatedDate()));
                Collections.sort(productClasses,(productClass1, t1) -> t1.getActive().compareTo(productClass1.getActive()));
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
        Long parent = null;
        classesList = new ArrayList<>();
        for (int i = 0; i < productClasses.size(); i++) {
            if(productClass!=null && productClass.getParentId() != null) {
                    if (productClass.getParentId().equals(productClasses.get(i).getId())) {
                        parent = productClasses.get(i).getId();
                    }
                if (!productClass.getId().equals(productClasses.get(i).getId()))
                    classesList.add(productClasses.get(i));
            }else {
                classesList.add(productClasses.get(i));
            }

        }
        ArrayList<String> strings = new ArrayList<>();
        for(ProductClass nameIdProdClass:classesList)
            strings.add(nameIdProdClass.getName());
        //TODO none get from string
        strings.add(0,none);
        view.setParentSpinnerItems(strings);
        if(parent!=null)
        for (int i = 0; i < classesList.size(); i++) {
            if(parent.equals(classesList.get(i).getId())){
                view.setParentSpinnerPosition(i+1);
            }
        }
        else
        view.setParentSpinnerPosition(0);
    }


}
