package com.jim.multipos.ui.product_class.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.intosystem.NameId;
import com.jim.multipos.ui.product_class.adapters.ProductsClassListAdapter;
import com.jim.multipos.ui.product_class.presenters.ProductClassListPresenter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.GlobalEventsConstants;
import com.jim.multipos.utils.rxevents.ProductClassEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
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
        presenter.onCreateView(savedInstanceState);
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
                        }else if(productClassEvent.getEventType().equals(GlobalEventsConstants.DELETE)) {
                            presenter.onDeleteProductClass(productClassEvent.getProductClass());
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
        classListAdapter = new ProductsClassListAdapter((List<NameId>)(List<?>)rvItemsList);
        classListAdapter.setOnItemClickListener(new ClickableBaseAdapter.OnItemClickListener<NameId>() {
            @Override
            public void onItemClicked(int position) {
                if(position == 0){
                    presenter.pressedAddButton();
                }
                else {
                    presenter.pressedItem(position);
                }
            }

            @Override
            public void onItemClicked(NameId item) {

            }
        });
        rvClasses.setAdapter(classListAdapter);
    }
}
