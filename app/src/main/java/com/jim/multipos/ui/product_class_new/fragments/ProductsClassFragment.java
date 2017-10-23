package com.jim.multipos.ui.product_class_new.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.product_class_new.adapters.ProductsClassListAdapter;
import com.jim.multipos.ui.product_class_new.presenters.ProductsClassPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by developer on 17.10.2017.
 */

public class ProductsClassFragment  extends BaseFragment implements ProductsClassView {

    @BindView(R.id.btnCancel)
    MpButton btnCancel;



    @BindView(R.id.rvClasses)
    RecyclerView rvClasses;

    @Inject
    ProductsClassListAdapter productsClassListAdapter;
    @Inject
    ProductsClassPresenter presenter;
    @Override
    protected int getLayout() {
        return R.layout.product_class_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.onCreateView(savedInstanceState);
        productsClassListAdapter.setListners( new ProductsClassListAdapter.OnProductClassCallback() {
            @Override
            public void onAddPressed(String name, boolean active) {
                presenter.onAddPressed(name,active);
            }

            @Override
            public void onAddSubPressed(String name, boolean active, ProductClass parent) {
                presenter.onAddSubPressed(name,active,parent);
            }

            @Override
            public void onSave(String name, boolean active, ProductClass productClass) {
                presenter.onSave(name,active,productClass);
            }

            @Override
            public void onDelete(ProductClass productClass) {
                presenter.onDelete(productClass);
            }
        });
        rvClasses.setLayoutManager(new LinearLayoutManager(getContext()));
        rvClasses.setAdapter(productsClassListAdapter);
        btnCancel.setOnClickListener(view -> {
            getActivity().finish();
        });
    }

    @Override
    protected void rxConnections() {

    }

    @Override
    public void refreshList(List<Object> objects) {
        productsClassListAdapter.setData(objects);
        productsClassListAdapter.notifyDataSetChanged();
    }
}
