package com.jim.multipos.ui.product_class.fragments;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.intosystem.NamePhotoPathId;
import com.jim.multipos.ui.product_class.ProductClassActivity;
import com.jim.multipos.ui.product_class.ProductClassView;
import com.jim.multipos.ui.product_class.adapters.ProductsClassListAdapter;
import com.jim.multipos.ui.product_class.di.ProductClassComponent;
import com.jim.multipos.ui.product_class.presenters.ProductClassListPresenter;
import com.jim.multipos.ui.product_class.presenters.ProductClassListPresenterImpl;
import com.jim.multipos.ui.products.adapters.ProductsListAdapter;
import com.jim.multipos.ui.products.fragments.ProductListView;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.GlobalEventsConstants;
import com.jim.multipos.utils.rxevents.ProductClassEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by developer on 29.08.2017.
 */

public class ProductClassListFragment extends BaseFragment implements ProductClassListView{

    @Inject
    RxBus rxBus;
    @Inject
    ProductClassListPresenter presenter;

    @BindView(R.id.rvClasses)
    RecyclerView rvClasses;

    private ProductsClassListAdapter classListAdapter;



    @Override
    protected int getLayout() {
        return R.layout.product_class_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        rvClasses.setLayoutManager(new GridLayoutManager(getContext(), 6));
        rvClasses.setItemAnimator(null);
    }
    ArrayList<Disposable> subscriptions;
    @Override
    protected void rxConnections() {
        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBus.toObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(o -> {
                    if(o instanceof ProductClassEvent){
                        ProductClassEvent productClassEvent = (ProductClassEvent) o;
                        if(productClassEvent.getEventType().equals(GlobalEventsConstants.ADD)) {
                            presenter.onAddProductClass(productClassEvent.getProductClass());
                        }else if(productClassEvent.getEventType().equals(GlobalEventsConstants.UPDATE)) {
                            presenter.onUpdateProductClass(productClassEvent.getProductClass());
                        }
                    }}));


    }

    @Override
    public void reshreshView() {
        classListAdapter.setToDefault();
        classListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        RxBus.removeListners(subscriptions);
        super.onDestroyView();
    }

    @Override
    public void setItemsRecyclerView(List<ProductClass> rvItemsList) {
        classListAdapter = new ProductsClassListAdapter(rvItemsList, new ProductsClassListAdapter.onItemClickListner() {
            @Override
            public void onAddButtonPressed() {
                presenter.pressedAddButton();
            }

            @Override
            public void onItemPressed(int t) {
                presenter.pressedItem(t);
            }
        });
        rvClasses.setAdapter(classListAdapter);
    }
}
