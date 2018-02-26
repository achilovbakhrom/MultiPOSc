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
import com.jim.multipos.ui.discount.model.DiscountApaterDetials;
import com.jim.multipos.ui.discount.presenters.DiscountAddingPresenter;
import com.jim.multipos.ui.discount.presenters.DiscountAddingPresenterImpl;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.WarningDialog;
import com.jim.multipos.utils.rxevents.DiscountEvent;
import com.jim.multipos.utils.rxevents.EditEvent;

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
    @Inject
    RxBus rxBus;

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
            public void onAddPressed(double amount, int amountTypeAbbr, String discription, int usedTypeAbbr, boolean active) {
                presenter.onAddPressed(amount,amountTypeAbbr,discription,usedTypeAbbr,active);
            }

            @Override
            public void onSave(double amount, int amountTypeAbbr, String discription, int usedTypeAbbr, boolean active, Discount discount) {
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
            presenter.onCloseAction();
        });
    }

    @Override
    public void refreshList(List<DiscountApaterDetials> objects) {
        discountListAdapter.setData(objects);
        discountListAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshList() {
        discountListAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyItemChanged(int pos) {
        discountListAdapter.notifyItemChanged(pos);
    }

    @Override
    public void notifyItemAddRange(int from, int to) {
        discountListAdapter.notifyItemRangeInserted(from,to);
    }

    @Override
    public void notifyItemAdd(int pos) {
        discountListAdapter.notifyItemInserted(pos);
    }

    @Override
    public void notifyItemRemove(int pos) {
        discountListAdapter.notifyItemRemoved(pos);
    }

    @Override
    public void notifyItemRemoveRange(int from, int to) {
        discountListAdapter.notifyItemRangeRemoved(from,to);
    }

    @Override
    public void closeAction() {
        presenter.onCloseAction();
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
        warningDialog.setPositiveButtonText(getString(R.string.cancel));
        warningDialog.setNegativeButtonText(getString(R.string.discard));
        warningDialog.show();
    }

    @Override
    public void sendEvent(String event, Discount discount) {
       rxBus.send(new DiscountEvent(discount, event));
    }

    @Override
    public void sendChangeEvent(String event, Long oldId, Long newId) {
       rxBus.send(new EditEvent(oldId, newId, event));
    }

}
