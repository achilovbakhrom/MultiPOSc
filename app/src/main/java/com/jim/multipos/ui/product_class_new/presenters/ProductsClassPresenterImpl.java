package com.jim.multipos.ui.product_class_new.presenters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.product_class_new.adapters.ProductsClassListAdapter;
import com.jim.multipos.ui.product_class_new.fragments.ProductsClassView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by developer on 17.10.2017.
 */

public class ProductsClassPresenterImpl extends BasePresenterImpl<ProductsClassView> implements ProductsClassPresenter {
    Context context;
    private DatabaseManager databaseManager;
    List<Object> items;
    List<ProductClass> productClasses;

    @Inject
    public ProductsClassPresenterImpl(AppCompatActivity context, DatabaseManager databaseManager,ProductsClassView view){
        super(view);
        this.context = context;
        this.databaseManager = databaseManager;
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        items = new ArrayList<>();
        databaseManager.getAllProductClass().subscribe(productClasses1 -> {
            productClasses = productClasses1;
            fillItemsList();
            view.refreshList(items);
        });

    }

    @Override
    public void onAddPressed(String name, boolean active) {
        ProductClass productClass = new ProductClass();
        productClass.setName(name);
        productClass.setActive(active);
        productClass.setCreatedDate(System.currentTimeMillis());
        productClass.setNotModifyted(true);
        productClass.setDeleted(false);
        productClass.setParentId(null);
        databaseManager.insertProductClass(productClass).subscribe((aLong, throwable) -> {
            productClasses.add(0,productClass);
            fillItemsList();
            view.refreshList(items);
        });
    }

    @Override
    public void onAddSubPressed(String name, boolean active, ProductClass parent) {
        ProductClass productClass = new ProductClass();
        productClass.setName(name);
        productClass.setActive(active);
        productClass.setCreatedDate(System.currentTimeMillis());
        productClass.setNotModifyted(true);
        productClass.setDeleted(false);
        productClass.setParentId(parent.getId());
        databaseManager.insertProductClass(productClass).subscribe((aLong, throwable) -> {
            productClasses.add(0,productClass);
            fillItemsList();
            view.refreshList(items);
        });
    }

    @Override
    public void onSave(String name, boolean active, ProductClass productClass) {
        productClass.setName(name);
        productClass.setActive(active);
        databaseManager.insertProductClass(productClass).subscribe((aLong, throwable) -> {
            for (int i = 0; i < productClasses.size(); i++) {
                if(productClasses.get(i).getId().equals(productClass.getId())){
                    productClasses.set(i,productClass);
                    fillItemsList();
                    view.refreshList(items);
                    break;
                }
            }
        });
    }

    @Override
    public void onDelete(ProductClass productClass) {
        productClass.setDeleted(true);
        databaseManager.insertProductClass(productClass).subscribe((aLong, throwable) -> {
            for (int i = 0; i < productClasses.size(); i++) {
                if(productClasses.get(i).getId().equals(productClass.getId())){
                    productClasses.remove(i);
                    for (int j = productClasses.size()-1; j >=0 ; j--) {
                        if(productClasses.get(j).getParentId()!=null && productClasses.get(j).getParentId().equals(productClass.getId())){
                            productClasses.get(j).setDeleted(true);
                            databaseManager.insertProductClass(productClasses.get(j)).blockingGet();
                            productClasses.remove(j);
                        }
                    }
                    fillItemsList();
                    view.refreshList(items);
                    break;
                }

            }
        });
    }
    void  fillItemsList(){
        items.clear();
        items.add(ProductsClassListAdapter.ProductClassItemTypes.AddClass);
        for (int i=0;i<productClasses.size();i++){
            if(productClasses.get(i).getParentId()==null){
                items.add(productClasses.get(i));
                for(int j = productClasses.size()-1; j>=0;j--){
                    if(productClasses.get(i).getId().equals(productClasses.get(j).getParentId())){
                        items.add(productClasses.get(j));
                    }
                }
                items.add(ProductsClassListAdapter.ProductClassItemTypes.SubAddClass);
            }
        }
    }
}
