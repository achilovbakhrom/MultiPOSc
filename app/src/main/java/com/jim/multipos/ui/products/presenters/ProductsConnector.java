package com.jim.multipos.ui.products.presenters;

import com.jim.multipos.core.RxForPresenter;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.SubCategory;
import com.jim.multipos.ui.products.ProductsActivity;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.BooleanMessageEvent;
import com.jim.multipos.utils.rxevents.MessageEvent;
import com.jim.multipos.utils.rxevents.ProductEvent;
import com.jim.multipos.utils.rxevents.SubCategoryEvent;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

/**
 * Created by DEV on 07.09.2017.
 */

public abstract class ProductsConnector extends RxForPresenter {
    private final static String ADVANCE = "advanced_options";
    private final static String BOOLEAN_STATE = "recipe";
    private final static String CLICK = "click";
    private final static String UNIT_ADDED = "unit_added";
    private final static String UNIT_REMOVED = "unit_removed";
    private final static String CURRENCY = "currency_added";
    private final static String PRODUCT_CLASS = "product_class_added";


    abstract void advancedOptionsOpened();

    private RxBus rxBus;
    private RxBusLocal rxBusLocal;

    @Override
    public void initConnectors(RxBus rxBus, RxBusLocal rxBusLocal) {
        this.rxBus = rxBus;
        this.rxBusLocal = rxBusLocal;
        setRxListners();
    }

    @Override
    public void setRxListners() {
        ArrayList<Disposable> subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBusLocal.toObservable().subscribe(o -> {
                    if (o instanceof MessageEvent) {
                        MessageEvent event = (MessageEvent) o;
                        if (event.getCategory().equals(ADVANCE)) {
                            advancedOptionsOpened();
                        }
                        if (event.getCategory().equals("product")) {
                            isVisible();
                        }
                        if (event.getCategory().equals(UNIT_ADDED) || event.getCategory().equals(UNIT_REMOVED)) {
                            getUnits();
                        }
                    }
                    if (o instanceof ProductEvent) {
                        ProductEvent event = (ProductEvent) o;
                        if (event.getEventType().equals(CLICK)) {
                            setProduct(event.getProduct());
                        }
                        if (event.getEventType().equals(ADVANCE)) {
                            saveProduct(event.getProduct());
                        }
                    }
                    if (o instanceof SubCategoryEvent) {
                        SubCategoryEvent event = (SubCategoryEvent) o;
                        if (event.getEventType().equals("parent")) {
                            setParentSubCategory(event.getSubCategory());
                        }
                    }
                    if (o instanceof BooleanMessageEvent) {
                        BooleanMessageEvent event = (BooleanMessageEvent) o;
                        if (event.getMsg().equals(BOOLEAN_STATE)) {
                            setRecipeState(event.getState());
                        }
                    }
                }));
        subscriptions.add(
                rxBus.toObservable().subscribe(o -> {
                    if (o instanceof MessageEvent) {
                        MessageEvent event = (MessageEvent) o;
                        if (event.getCategory().equals(UNIT_ADDED)) {
                            getUnits();
                        }
                        if (event.getCategory().equals(CURRENCY)) {
                            getCurrencies();
                        }
                        if (event.getCategory().equals(PRODUCT_CLASS)) {
                            getProductClass();
                        }
                    }
                }));

        initUnsubscribers(rxBus, ProductsActivity.class.getName(), subscriptions);
    }

    protected abstract void getProductClass();

    protected abstract void getCurrencies();

    protected abstract void getUnits();

    protected abstract void saveProduct(Product product);

    protected abstract void setRecipeState(boolean state);

    protected abstract void isVisible();

    protected abstract void setParentSubCategory(SubCategory subCategory);

    protected abstract void setProduct(Product product);
}
