package com.jim.multipos.ui.discount.presenters;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.ui.discount.adapters.DiscountListAdapter;
import com.jim.multipos.ui.discount.fragments.DiscountAddingView;
import com.jim.multipos.ui.discount.model.DiscountApaterDetials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by developer on 23.10.2017.
 */

public class DiscountAddingPresenterImpl extends BasePresenterImpl<DiscountAddingView> implements DiscountAddingPresenter {
    List<DiscountApaterDetials> items;
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
            items.add(null);
            for (int i = 0; i < discounts1.size(); i++) {
                DiscountApaterDetials discountApaterDetials = new DiscountApaterDetials();
                discountApaterDetials.setObject(discounts1.get(i));
                items.add(discountApaterDetials);
            }
            view.refreshList(items);
        });

    }

    @Override
    public void onAddPressed(double amount, int amountTypeAbbr, String discription, int usedTypeAbbr, boolean active) {
        Discount discount = new Discount();
        discount.setAmount(amount * -1);
        discount.setAmountType(amountTypeAbbr);
        discount.setName(discription);
        discount.setUsedType(usedTypeAbbr);
        discount.setActive(active);
        discount.setCreatedDate(System.currentTimeMillis());
        discount.setNotModifyted(true);
        discount.setDeleted(false);
        databaseManager.insertDiscount(discount).subscribe((aLong, throwable) -> {
            DiscountApaterDetials discountApaterDetials = new DiscountApaterDetials();
            discountApaterDetials.setObject(discount);
            items.add(1, discountApaterDetials);
            view.notifyItemAdd(1);
        });
    }

    @Override
    public void onSave(double amount, int amountTypeAbbr, String discription, int usedTypeAbbr, boolean active, Discount discount) {
        discount.setNotModifyted(false);
        databaseManager.insertDiscount(discount).subscribe((aLong, throwable) -> {
            Discount discount1 = new Discount();
            discount1.setAmount(amount);
            discount1.setAmountType(amountTypeAbbr);
            discount1.setName(discription);
            discount1.setUsedType(usedTypeAbbr);
            discount1.setActive(active);
            discount1.setCreatedDate(discount.getCreatedDate());
            discount1.setNotModifyted(true);
            discount1.setDeleted(false);
            if (discount.getRootId() != null)
                discount1.setRootId(discount.getId());
            else discount1.setRootId(discount.getRootId());
            databaseManager.insertDiscount(discount1).subscribe((aLong1, throwable1) -> {
                for (int i = 1; i < items.size(); i++) {
                    if (items.get(i).getObject().getId().equals(discount.getId())) {
                        DiscountApaterDetials discountApaterDetials = new DiscountApaterDetials();
                        discountApaterDetials.setObject(discount1);
                        items.set(i, discountApaterDetials);
                        view.notifyItemChanged(i);
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
            for (int i = 1; i < items.size(); i++) {
                if (items.get(i).getObject().getId().equals(discount.getId())) {
                    items.remove(i);
                    view.notifyItemRemove(i);
                    break;
                }
            }
        });
    }



    @Override
    public void sortList(DiscountSortTypes discountSortTypes){
        items.remove(0);
        switch (discountSortTypes){
            case Ammount:
                Collections.sort(items,(discounts, t1) -> t1.getObject().getAmount().compareTo(discounts.getObject().getAmount()));
                break;
            case Type:
                Collections.sort(items,(discounts, t1) -> t1.getObject().getAmountType().compareTo(discounts.getObject().getAmountType()));
                break;
            case Used:
                Collections.sort(items,(discounts, t1) -> t1.getObject().getUsedType().compareTo(discounts.getObject().getUsedType()));
                break;
            case Active:
                Collections.sort(items,(discounts, t1) -> t1.getObject().getActive().compareTo(discounts.getObject().getActive()));
                break;
            case Discription:
                Collections.sort(items,(discounts, t1) -> t1.getObject().getName().compareTo(discounts.getObject().getName()));
                break;
            case Default:
                Collections.sort(items,(discounts, t1) -> t1.getObject().getCreatedDate().compareTo(discounts.getObject().getCreatedDate()));
                break;
        }
        items.add(0,null);
        view.refreshList();
    }

    @Override
    public void onCloseAction() {
        boolean weCanClose = true;
        for (int i = 1; i < items.size(); i++) {
            if(items.get(i).isChanged())
                weCanClose  = false;
        }
        if(weCanClose){
            view.closeDiscountActivity();
        }else {
            view.openWarning();
        }
    }
}
