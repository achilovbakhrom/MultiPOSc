package com.jim.multipos.ui.product_class.presenters;

import android.os.Bundle;

import com.jim.multipos.config.scope.PerFragment;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.ui.product_class.fragments.AddProductClassFragment;
import com.jim.multipos.ui.product_class.fragments.ProductClassListView;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.ProductClassEvent;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by developer on 29.08.2017.
 */
@PerFragment
public class ProductClassListPresenterImpl  extends BasePresenterImpl<ProductClassListView> implements ProductClassListPresenter {
    private ProductClassListView view;
    private DatabaseManager databaseManager;
    private RxBusLocal rxBusLocal;
    private List<ProductClass> productClasses;

    @Inject
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
            this.productClasses = productClasses1;
            this.productClasses.add(0,null);
            view.setItemsRecyclerView(this.productClasses);
        });
    }



    @Override
    public void onAddProductClass(ProductClass productClass) {
        productClasses.remove(0);
        productClasses.add(0,productClass);
        Collections.sort(productClasses,(productClass1, t1) -> t1.getActive().compareTo(productClass1.getActive()));
        productClasses.add(0,null);
        view.reshreshView();

    }

    @Override
    public void onUpdateProductClass(ProductClass productClass) {
        productClasses.remove(0);
        for (int i = 0;i<productClasses.size();i++){
            if(productClasses.get(i).getId().equals(productClass.getId())){
                productClasses.set(i,productClass);
            }
        }
        Collections.sort(productClasses,(productClass1, t1) -> t1.getCreatedDate().compareTo(productClass1.getCreatedDate()));
        Collections.sort(productClasses,(productClass1, t1) -> t1.getActive().compareTo(productClass1.getActive()));
        productClasses.add(0,null);
        view.reshreshView();
    }

    @Override
    public void onDeleteProductClass(ProductClass productClass) {
        productClasses.remove(0);
        for (int i = 0;i<productClasses.size();i++){
            if(productClasses.get(i).getId().equals(productClass.getId())){
                productClasses.remove(i);
                break;
            }
        }
        productClasses.add(0,null);
        view.reshreshView();
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
