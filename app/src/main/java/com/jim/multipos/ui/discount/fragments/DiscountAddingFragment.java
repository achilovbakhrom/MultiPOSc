package com.jim.multipos.ui.discount.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.ui.discount.adapters.DiscountListAdapter;
import com.jim.multipos.ui.discount.presenters.DiscountAddingPresenter;
import com.jim.multipos.ui.discount.presenters.DiscountAddingPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by developer on 23.10.2017.
 */

public class DiscountAddingFragment  extends BaseFragment implements DiscountAddingView {
    @Inject
    DiscountListAdapter discountListAdapter;
    @Inject
    DiscountAddingPresenter presenter;

    @BindView(R.id.rvDiscounts)
    RecyclerView rvDiscounts;
    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @Override
    protected int getLayout() {
        return R.layout.discount_adding_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.onCreateView(savedInstanceState);

        discountListAdapter.setListners(new DiscountListAdapter.OnDiscountCallback() {
            @Override
            public void onAddPressed(double amount, String amountTypeAbbr, String discription, String usedTypeAbbr, boolean active) {
                presenter.onAddPressed(amount,amountTypeAbbr,discription,usedTypeAbbr,active);
            }

            @Override
            public void onSave(double amount, String amountTypeAbbr, String discription, String usedTypeAbbr, boolean active, Discount discount) {
                presenter.onSave(amount,amountTypeAbbr,discription,usedTypeAbbr,active,discount);
            }

            @Override
            public void onDelete(Discount discount) {
                presenter.onDelete(discount);
            }

            @Override
            public void sortList(DiscountAddingPresenterImpl.DiscountSortTypes discountSortTypes) {
                presenter.sortList(discountSortTypes);
            }
        });
        rvDiscounts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDiscounts.setAdapter(discountListAdapter);
        ((SimpleItemAnimator) rvDiscounts.getItemAnimator()).setSupportsChangeAnimations(false);
        btnCancel.setOnClickListener(view -> {
            getActivity().finish();
        });
    }

    @Override
    public void refreshList(List<Object> objects) {
//        rvDiscounts.setItemViewCacheSize(objects.size());
        discountListAdapter.setData(objects);
        discountListAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyItemChanged(List<Object> objects,int pos) {
        discountListAdapter.setData(objects);
        discountListAdapter.notifyItemChanged(pos);

    }
}
