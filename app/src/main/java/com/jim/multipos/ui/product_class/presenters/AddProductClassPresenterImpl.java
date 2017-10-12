package com.jim.multipos.ui.product_class.presenters;

import android.os.Bundle;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.intosystem.NameIdProdClass;
import com.jim.multipos.ui.product_class.fragments.AddProductClassView;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.GlobalEventsConstants;
import com.jim.multipos.utils.rxevents.ProductClassEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by developer on 29.08.2017.
 */
@PerFragment
public class AddProductClassPresenterImpl extends BasePresenterImpl<AddProductClassView> implements AddProductClassPresenter {


    private AddProductClassView view;
    private RxBus rxBus;
    private DatabaseManager databaseManager;
    private RxBusLocal rxBusLocal;
    private ProductClass productClass;
    private ArrayList<String> parentSpinner;
    List<ProductClass> productClasses;

    @Inject
    public AddProductClassPresenterImpl(RxBus rxBus, DatabaseManager databaseManager, RxBusLocal rxBusLocal, AddProductClassView view){
        super(view);
        this.rxBus = rxBus;
        this.databaseManager = databaseManager;
        this.rxBusLocal = rxBusLocal;
        this.view = view;

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
    String parentId= null;
    @Override
    public void onSaveButtonPress(String className, int position, boolean active) {
        if(className.isEmpty())
            view.classNameEmpty();
        if(className.length()<4)
            view.classNameShort();
        parentId = null;
        if(position!=0){
            parentId = productClasses.get(position-1).getId();
        }
        if(productClass==null){
            productClass = new ProductClass();
            productClass.setName(className);
            productClass.setParentId(parentId);
            productClass.setActive(active);
            productClass.setCreatedDate(System.currentTimeMillis());
            productClass.setDeleted(false);
            productClass.setNotModifyted(true);
            productClass.setRootId(null);
            databaseManager.insertProductClass(productClass).subscribe(aLong -> {
                productClasses.add(productClass);
                Collections.sort(productClasses,(productClass, t1) -> t1.getActive().compareTo(productClass.getActive()));
                rxBus.send(new ProductClassEvent(productClass, GlobalEventsConstants.ADD));
                productClass = null;
                makeDataForParentSpinner();
                view.onAddNew();
            });

        }else {
            productClass.setNotModifyted(false);
            productClass.setDeleted(false);
            databaseManager.insertProductClass(productClass).subscribe(aLong -> {
                ProductClass productClassNew = new ProductClass();
                if(productClass.getRootId()==null)
                productClassNew.setRootId(productClass.getId());
                else productClassNew.setRootId(productClass.getRootId());
                productClassNew.setName(className);
                productClassNew.setParentId(parentId);
                productClassNew.setActive(active);
                productClassNew.setDeleted(false);
                productClassNew.setCreatedDate(System.currentTimeMillis());

                databaseManager.insertProductClass(productClass).subscribe(aLong1 -> {
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

        ArrayList<String> strings = new ArrayList<>();
        for(ProductClass nameIdProdClass:productClasses)
            strings.add(nameIdProdClass.getName());
        //TODO none get from string
        strings.add(0,"none");
        view.setParentSpinnerItems(strings);

        for (int i = 0; i < productClasses.size(); i++) {
            if(parent.equals(productClasses.get(i).getId())){
                view.setParentSpinnerPosition(i+1);
                return;
            }
        }
        view.setParentSpinnerPosition(0);
    }


}
