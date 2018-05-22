package com.jim.multipos.ui.product_class_new.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.ui.product_class_new.adapters.ProductsClassListAdapter;
import com.jim.multipos.ui.product_class_new.model.ProductsClassAdapterDetials;
import com.jim.multipos.ui.product_class_new.presenters.ProductsClassPresenter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.WarningDialog;
import com.jim.multipos.utils.rxevents.product_events.ProductClassEvent;

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
    @Inject
    RxBus rxBus;
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

            @Override
            public boolean nameIsUnique(String checkName, ProductClass currentProductClass) {
                return presenter.nameIsUnique(checkName,currentProductClass);
            }
        });
        rvClasses.setLayoutManager(new LinearLayoutManager(getContext()));
        rvClasses.setAdapter(productsClassListAdapter);
        ((SimpleItemAnimator) rvClasses.getItemAnimator()).setSupportsChangeAnimations(false);

        btnCancel.setOnClickListener(view -> {
            presenter.onCloseAction();
        });
    }

    @Override
    protected void rxConnections() {

    }
    @Override
    public void refreshList(List<ProductsClassAdapterDetials> objects) {
        rvClasses.setItemViewCacheSize(objects.size());
        productsClassListAdapter.setData(objects);
        productsClassListAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyItemChanged(int pos) {
        productsClassListAdapter.notifyItemChanged(pos);
    }

    @Override
    public void notifyItemAddRange(int from, int to) {
        productsClassListAdapter.notifyItemRangeInserted(from,to);
        rvClasses.scrollToPosition(0);

    }

    @Override
    public void notifyItemAdd(int pos) {
        productsClassListAdapter.notifyItemInserted(pos);
    }

    @Override
    public void notifyItemRemove(int pos) {
        productsClassListAdapter.notifyItemRemoved(pos);
    }

    @Override
    public void notifyItemRemoveRange(int from, int to) {
        if(from==to){
            productsClassListAdapter.notifyItemRemoved(from);
        }else
        productsClassListAdapter.notifyItemRangeRemoved(from,to);
    }

    @Override
    public void closeDiscountActivity() {
        getActivity().finish();
    }

    @Override
    public void openWarning() {
        WarningDialog warningDialog = new WarningDialog(getActivity());
        warningDialog.setWarningMessage(getString(R.string.discard_discounts));
        warningDialog.setOnYesClickListener(view1 -> warningDialog.dismiss());
        warningDialog.setOnNoClickListener(view1 -> closeDiscountActivity());
        warningDialog.setPositiveButtonText(getContext().getString(R.string.cancel));
        warningDialog.setNegativeButtonText(getContext().getString(R.string.discard));
        warningDialog.show();
    }

    @Override
    public void closeAction() {
        presenter.onCloseAction();
    }

    @Override
    public void sendEvent(ProductClass productClass, int eventType) {
        rxBus.send(new ProductClassEvent(productClass, eventType));
    }


}
