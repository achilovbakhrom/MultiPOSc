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
import com.jim.multipos.ui.product_class_new.model.ProductsClassAdapterDetials;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by developer on 17.10.2017.
 */

public class ProductsClassPresenterImpl extends BasePresenterImpl<ProductsClassView> implements ProductsClassPresenter {
    Context context;
    private DatabaseManager databaseManager;
    List<ProductsClassAdapterDetials> items;

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
            ProductsClassAdapterDetials productsClassAdapterDetials = new ProductsClassAdapterDetials();
            productsClassAdapterDetials.setObject(productClass);
            productsClassAdapterDetials.setType(ProductsClassListAdapter.ProductClassItemTypes.ProductClass);

            ProductsClassAdapterDetials productsClassAdapterDetials1 = new ProductsClassAdapterDetials();
            productsClassAdapterDetials1.contanierMode();
            productsClassAdapterDetials1.setType(ProductsClassListAdapter.ProductClassItemTypes.SubAddClass);

            items.add(1,productsClassAdapterDetials);
            items.add(2,productsClassAdapterDetials1);
            view.notifyItemAddRange(1,2);
            view.sendEvent(productClass, GlobalEventConstants.ADD);
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
                if(!isParentFound && ( items.get(i).getType() == ProductsClassListAdapter.ProductClassItemTypes.ProductClass || items.get(i).getType() == ProductsClassListAdapter.ProductClassItemTypes.SubProductClass )){
                    ProductClass productClass1 = items.get(i).getObject();
                    if(parent.getId().equals(productClass1.getId())){
                        isParentFound  = true;
                    }
                }else if(isParentFound && items.get(i).getType() == ProductsClassListAdapter.ProductClassItemTypes.SubAddClass){
                    ProductsClassAdapterDetials productsClassAdapterDetials1 = new ProductsClassAdapterDetials();
                    productsClassAdapterDetials1.setObject(productClass);
                    productsClassAdapterDetials1.setType(ProductsClassListAdapter.ProductClassItemTypes.SubProductClass);
                    items.add(i,productsClassAdapterDetials1);
                    view.notifyItemAdd(i);
                    break;
                }
            }
            view.sendEvent(productClass, GlobalEventConstants.ADD);
        });
    }

    @Override
    public void onSave(String name, boolean active, ProductClass productClass) {
        productClass.setName(name);
        productClass.setActive(active);
        databaseManager.insertProductClass(productClass).subscribe((aLong, throwable) -> {
            for (int i = 0; i < items.size(); i++) {
                if(items.get(i).getType() == ProductsClassListAdapter.ProductClassItemTypes.ProductClass || items.get(i).getType() == ProductsClassListAdapter.ProductClassItemTypes.SubProductClass){
                    if( items.get(i).getObject().getId().equals(productClass.getId())){
                        ProductsClassAdapterDetials productsClassAdapterDetials1 = new ProductsClassAdapterDetials();
                        productsClassAdapterDetials1.setObject(productClass);
                        if(productClass.getParentId() == null)
                        productsClassAdapterDetials1.setType(ProductsClassListAdapter.ProductClassItemTypes.ProductClass);
                        else  productsClassAdapterDetials1.setType(ProductsClassListAdapter.ProductClassItemTypes.SubProductClass);
                        items.set(i,productsClassAdapterDetials1);
                        view.notifyItemChanged(i);
                        break;
                    }
                }
            }
            view.sendEvent(productClass, GlobalEventConstants.UPDATE);
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
                if(items.get(i).getType() == ProductsClassListAdapter.ProductClassItemTypes.ProductClass || items.get(i).getType() == ProductsClassListAdapter.ProductClassItemTypes.SubProductClass) {
                    if (items.get(i).getObject().getId().equals(productClass.getId())) {
                        if(productClass.getParentId() !=null){
                            from = i;
                            to = i;
                            break;
                        }

                        from = i;
                        if (items.get(i+1).getType() == ProductsClassListAdapter.ProductClassItemTypes.SubAddClass) {
                            to = i + 1;
                            break;
                        }

                    }
                    else if(items.get(i).getType()==ProductsClassListAdapter.ProductClassItemTypes.SubProductClass && items.get(i).getObject().getParentId().equals(productClass.getId())){
                        items.get(i).getObject().setDeleted(true);
                        databaseManager.insertProductClass(items.get(i).getObject()).blockingGet();
                        to = i;
                        if (items.get(i + 1).getType() == ProductsClassListAdapter.ProductClassItemTypes.SubAddClass) {
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
            view.sendEvent(productClass, GlobalEventConstants.DELETE);
        });
    }

    @Override
    public boolean nameIsUnique(String checkName, ProductClass currentProductClass) {
            for (int i = 0; i < items.size(); i++) {
                if(items.get(i).getType() == ProductsClassListAdapter.ProductClassItemTypes.ProductClass || items.get(i).getType() == ProductsClassListAdapter.ProductClassItemTypes.SubProductClass) {
                    if (currentProductClass != null && currentProductClass.getId().equals( items.get(i).getObject().getId()))
                        continue;
                    if (checkName.equals(items.get(i).getObject().getName())) return false;
                }
            }
            return true;
    }

    @Override
    public void onCloseAction() {
        boolean weCanClose = true;
        for (int i = 1; i < items.size(); i++) {
            if((items.get(i).getType() == ProductsClassListAdapter.ProductClassItemTypes.ProductClass || items.get(i).getType() == ProductsClassListAdapter.ProductClassItemTypes.SubProductClass) &&items.get(i).isChanged())
                weCanClose  = false;
        }
        if(weCanClose){
            view.closeDiscountActivity();
        }else {
            view.openWarning();
        }
    }

    void  fillItemsList(List<ProductClass> productClasses){
        items.clear();
        ProductsClassAdapterDetials productsClassAdapterDetials = new ProductsClassAdapterDetials();
        productsClassAdapterDetials.setType(ProductsClassListAdapter.ProductClassItemTypes.AddClass);
        productsClassAdapterDetials.contanierMode();
        items.add(productsClassAdapterDetials);

        for (int i=0;i<productClasses.size();i++){
            if(productClasses.get(i).getParentId()==null){
                ProductsClassAdapterDetials productsClassAdapterDetials1 = new ProductsClassAdapterDetials();
                productsClassAdapterDetials1.setType(ProductsClassListAdapter.ProductClassItemTypes.ProductClass);
                productsClassAdapterDetials1.setObject(productClasses.get(i));
                items.add(productsClassAdapterDetials1);
                for(int j = productClasses.size()-1; j>=0;j--){
                    if(productClasses.get(i).getId().equals(productClasses.get(j).getParentId())){
                        ProductsClassAdapterDetials productsClassAdapterDetials2 = new ProductsClassAdapterDetials();
                        productsClassAdapterDetials2.setType(ProductsClassListAdapter.ProductClassItemTypes.SubProductClass);
                        productsClassAdapterDetials2.setObject(productClasses.get(j));
                        items.add(productsClassAdapterDetials2);
                    }
                }
                ProductsClassAdapterDetials productsClassAdapterDetials2 = new ProductsClassAdapterDetials();
                productsClassAdapterDetials2.setType(ProductsClassListAdapter.ProductClassItemTypes.SubAddClass);
                productsClassAdapterDetials2.contanierMode();
                items.add(productsClassAdapterDetials2);
            }
        }
    }

}
