package com.jim.multipos.ui.discount.presenters;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.ui.discount.adapters.DiscountListAdapter;
import com.jim.multipos.ui.discount.fragments.DiscountAddingView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by developer on 23.10.2017.
 */

public class DiscountAddingPresenterImpl extends BasePresenterImpl<DiscountAddingView> implements DiscountAddingPresenter {
    List<Object> items;
    List<Discount> discounts;
    private AppCompatActivity context;
    private DatabaseManager databaseManager;
    public enum DiscountSortTypes {Ammount,Type,Discription,Used,Active,Default}
    @Inject
    protected DiscountAddingPresenterImpl(AppCompatActivity context, DatabaseManager databaseManager, DiscountAddingView view) {
        super(view);
        this.context = context;
        this.databaseManager = databaseManager;
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        items = new ArrayList<>();
        databaseManager.getAllDiscounts().subscribe(discounts1 -> {
            discounts = discounts1;
            fillItemsList();
            view.refreshList(items);
        });

    }

    @Override
    public void onAddPressed(double amount, String amountTypeAbbr, String discription, String usedTypeAbbr, boolean active) {
        Discount discount = new Discount();
        discount.setAmount(amount);
        discount.setAmountType(amountTypeAbbr);
        discount.setDiscription(discription);
        discount.setUsedType(usedTypeAbbr);
        discount.setActive(active);
        discount.setCreatedDate(System.currentTimeMillis());
        discount.setNotModifyted(true);
        discount.setDeleted(false);
        databaseManager.insertDiscount(discount).subscribe((aLong, throwable) -> {
            discounts.add(0, discount);
            fillItemsList();
            view.refreshList(items);
        });
    }

    @Override
    public void onSave(double amount, String amountTypeAbbr, String discription, String usedTypeAbbr, boolean active, Discount discount) {
        discount.setNotModifyted(false);
        databaseManager.insertDiscount(discount).subscribe((aLong, throwable) -> {
            Discount discount1 = new Discount();
            discount1.setAmount(amount);
            discount1.setAmountType(amountTypeAbbr);
            discount1.setDiscription(discription);
            discount1.setUsedType(usedTypeAbbr);
            discount1.setActive(active);
            discount1.setCreatedDate(discount.getCreatedDate());
            discount1.setNotModifyted(true);
            discount1.setDeleted(false);
            if (discount.getRootId() != null)
                discount1.setRootId(discount.getId());
            else discount1.setRootId(discount.getRootId());
            databaseManager.insertDiscount(discount1).subscribe((aLong1, throwable1) -> {
                for (int i = 0; i < discounts.size(); i++) {
                    if (discounts.get(i).getId().equals(discount.getId())) {
                        discounts.set(i, discount1);
                        fillItemsList();
                        view.notifyItemChanged(items,i+1);
                        return;
                    }
                }
            });
        });
    }

    @Override
    public void onDelete(Discount discount) {
        discount.setDeleted(true);
        databaseManager.insertDiscount(discount).subscribe((aLong, throwable) -> {
            for (int i = 0; i < discounts.size(); i++) {
                if (discounts.get(i).getId().equals(discount.getId())) {
                    discounts.remove(i);
                    fillItemsList();
                    view.refreshList(items);
                    break;
                }
            }
        });
    }

    private void fillItemsList() {
        items.clear();
        items.add(DiscountListAdapter.DiscountAdapterItemTypes.AddDiscount);
        items.addAll(discounts);
    }

    @Override
    public void sortList(DiscountSortTypes discountSortTypes){
        switch (discountSortTypes){
            case Ammount:
                Collections.sort(discounts,(discounts, t1) -> t1.getAmount().compareTo(discounts.getAmount()));
                break;
            case Type:
                Collections.sort(discounts,(discounts, t1) -> t1.getAmountType().compareTo(discounts.getAmountType()));
                break;
            case Used:
                Collections.sort(discounts,(discounts, t1) -> t1.getUsedType().compareTo(discounts.getUsedType()));
                break;
            case Active:
                Collections.sort(discounts,(discounts, t1) -> t1.getActive().compareTo(discounts.getActive()));
                break;
            case Discription:
                Collections.sort(discounts,(discounts, t1) -> t1.getDiscription().compareTo(discounts.getDiscription()));
                break;
            case Default:
                Collections.sort(discounts,(discounts, t1) -> t1.getCreatedDate().compareTo(discounts.getCreatedDate()));
                break;
        }
        fillItemsList();
        view.refreshList(items);
    }
}
