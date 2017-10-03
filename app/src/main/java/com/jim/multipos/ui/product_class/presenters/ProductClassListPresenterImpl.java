package com.jim.multipos.ui.product_class.presenters;

import com.jim.multipos.core.RxForPresenter;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.employee.Schedule;
import com.jim.multipos.data.db.model.intosystem.NamePhotoPathId;
import com.jim.multipos.ui.product_class.ProductClassActivity;
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

public class ProductClassListPresenterImpl  extends RxForPresenter implements ProductClassListPresenter {
    private ProductClassListView view;
    private DatabaseManager databaseManager;
    private RxBusLocal rxBusLocal;
    private RxBus rxBus;
    private List<ProductClass> productClasses;
    public ProductClassListPresenterImpl(DatabaseManager databaseManager,RxBusLocal rxBusLocal, RxBus rxBus){
        this.databaseManager = databaseManager;
        this.rxBusLocal = rxBusLocal;
        this.rxBus = rxBus;
    }
    @Override
    public void init(ProductClassListView view) {

        this.view = view;
        databaseManager.getAllProductClass().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(productClasses1 -> {
            productClasses = productClasses1;
            productClasses.add(0,null);
            view.setItemsRecyclerView(productClasses);
            setRxListners();
        });
    }

    @Override
    public void setRxListners() {
        ArrayList<Disposable> subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBus.toObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(o -> {
                    if(o instanceof ProductClassEvent){
                        ProductClassEvent productClassEvent = (ProductClassEvent) o;
                        if(productClassEvent.getEventType().equals(GlobalEventsConstants.ADD)) {
                            databaseManager.getAllProductClass().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(productClasses1 -> {
                                productClasses.clear();
                                productClasses.addAll(productClasses1);
                                productClasses.add(0,null);
                                view.reshreshView();
                            });
                        }else if(productClassEvent.getEventType().equals(GlobalEventsConstants.UPDATE)) {
                            databaseManager.getAllProductClass().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(productClasses1 -> {
                                productClasses.clear();
                                productClasses.addAll(productClasses1);
                                productClasses.add(0,null);
                                view.reshreshView();
                            });
                        }
                    }}));

        initUnsubscribers(rxBus, ProductClassActivity.class.getName(),subscriptions);
    }

    @Override
    public void initConnectors(RxBus rxBus, RxBusLocal rxBusLocal) {

    }

    @Override
    public void pressedAddButton() {
        rxBusLocal.send(new ProductClassEvent(null,AddProductClassConnector.ADD_PRODUCT_CLASS));
    }

    @Override
    public void pressedItem(int pos) {
        rxBusLocal.send(new ProductClassEvent(productClasses.get(pos),AddProductClassConnector.CLICK_PRODUCT_CLASS));

    }
}
