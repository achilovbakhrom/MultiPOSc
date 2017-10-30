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
            fillItemsList(productClasses1);
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
            items.add(1,productClass);
            items.add(2,ProductsClassListAdapter.ProductClassItemTypes.SubAddClass);
            view.notifyItemAddRange(1,2);
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
            boolean isParentFound = false ;
            for (int i = 0; i < items.size(); i++) {
                if(!isParentFound && items.get(i) instanceof ProductClass){
                    ProductClass productClass1 = (ProductClass) items.get(i);
                    if(parent.getId().equals(productClass1.getId())){
                        isParentFound  = true;
                    }
                }else if(isParentFound && items.get(i) == ProductsClassListAdapter.ProductClassItemTypes.SubAddClass){
                    items.add(i,productClass);
                    view.notifyItemAdd(i);
                    break;
                }
            }
        });
    }

    @Override
    public void onSave(String name, boolean active, ProductClass productClass) {
        productClass.setName(name);
        productClass.setActive(active);
        databaseManager.insertProductClass(productClass).subscribe((aLong, throwable) -> {
            for (int i = 0; i < items.size(); i++) {
                if(items.get(i) instanceof ProductClass){
                    if(((ProductClass) items.get(i)).getId().equals(productClass.getId())){
                        items.set(i,productClass);
                        view.notifyItemChanged(i);
                        break;
                    }
                }
            }
        });
    }
    int from = 0, to = 0;
    @Override
    public void onDelete(ProductClass productClass) {
        productClass.setDeleted(true);
        from = -1;
        to = -1;
        databaseManager.insertProductClass(productClass).subscribe((aLong, throwable) -> {
            for (int i = 0; i < items.size(); i++) {
                if(items.get(i) instanceof ProductClass) {
                    if (((ProductClass) items.get(i)).getId().equals(productClass.getId())) {
                        if(productClass.getParentId() !=null){
                            from = i;
                            to = i;
                            break;
                        }

                        from = i;
                        if (items.get(i + 1) == ProductsClassListAdapter.ProductClassItemTypes.SubAddClass) {
                            to = i + 1;
                            break;
                        }

                    }
                    else if(((ProductClass) items.get(i)).getParentId()!=null && ((ProductClass) items.get(i)).getParentId().equals(productClass.getId())){
                        ((ProductClass) items.get(i)).setDeleted(true);
                        databaseManager.insertProductClass(((ProductClass) items.get(i))).blockingGet();
                        to = i;
                        if (items.get(i + 1) == ProductsClassListAdapter.ProductClassItemTypes.SubAddClass) {
                            to = i + 1;
                            break;
                        }
                    }
                }
            }
            if(from != -1 && to != -1){

                for (int i = to; i>=from;i--){
                    items.remove(i);
                }
                view.notifyItemRemoveRange(from,to);
            }
        });
    }

    @Override
    public boolean nameIsUnique(String checkName, ProductClass currentProductClass) {
            for (int i = 0; i < items.size(); i++) {
                if(items.get(i) instanceof ProductClass) {
                    if (currentProductClass != null && currentProductClass.getId().equals(((ProductClass) items.get(i)).getId()))
                        continue;
                    if (checkName.equals(((ProductClass) items.get(i)).getName())) return false;
                }
            }
            return true;
    }

    void  fillItemsList(List<ProductClass> productClasses){
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
