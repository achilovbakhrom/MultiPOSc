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
import com.jim.multipos.ui.product_class.ProductClassView;
import com.jim.multipos.ui.product_class.adapters.ProductsClassListAdapter;
import com.jim.multipos.ui.product_class.di.ProductClassComponent;
import com.jim.multipos.ui.product_class.presenters.ProductClassListPresenter;
import com.jim.multipos.ui.product_class.presenters.ProductClassListPresenterImpl;
import com.jim.multipos.ui.products.adapters.ProductsListAdapter;
import com.jim.multipos.ui.products.fragments.ProductListView;
import com.jim.multipos.utils.RxBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_class_list, container, false);
        this.getComponent(ProductClassComponent.class).inject(this);
        ButterKnife.bind(this, view);
        rvClasses.setLayoutManager(new GridLayoutManager(getContext(), 6));
        rvClasses.setItemAnimator(null);
        presenter.init(this);

        return view;
    }

    @Override
    public void reshreshView() {
        classListAdapter.setToDefault();
        classListAdapter.notifyDataSetChanged();
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
