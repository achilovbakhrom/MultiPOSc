package com.jim.multipos.ui.discount.presenters;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.DiscountLog;
import com.jim.multipos.ui.discount.fragments.DiscountAddingView;
import com.jim.multipos.ui.discount.model.DiscountApaterDetials;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;

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

    public enum DiscountSortTypes {Ammount, Type, Discription, Used, Active, Default}


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
        databaseManager.getStaticDiscounts().subscribe(discounts -> {
            items.add(null);
            for (int i = 0; i < discounts.size(); i++) {
                DiscountApaterDetials discountApaterDetials = new DiscountApaterDetials();
                discountApaterDetials.setObject(discounts.get(i));
                items.add(discountApaterDetials);
            }
            view.refreshList(items);
        });

    }

    @Override
    public void onAddPressed(double amount, int amountTypeAbbr, String discription, int usedTypeAbbr, boolean active) {
        Discount discount = new Discount();
        discount.setAmount(amount);
        discount.setAmountType(amountTypeAbbr);
        discount.setName(discription);
        discount.setUsedType(usedTypeAbbr);
        discount.setActive(active);
        discount.setCreatedDate(System.currentTimeMillis());
        discount.setDelete(false);
        databaseManager.insertDiscount(discount).subscribe((discount1, throwable) -> {
            DiscountApaterDetials discountApaterDetials = new DiscountApaterDetials();
            discountApaterDetials.setObject(discount);
            DiscountLog discountLog = new DiscountLog();
            discountLog.setChangeDate(System.currentTimeMillis());
            discountLog.setDiscount(discount1);
            discountLog.setStatus(DiscountLog.DISCOUNT_ADDED);
            databaseManager.insertDiscountLog(discountLog).subscribe();
            items.add(1, discountApaterDetials);
            view.notifyItemAdd(1);
            view.sendEvent(GlobalEventConstants.ADD, discount);
        });
    }

    @Override
    public void onSave(double amount, int amountTypeAbbr, String discription, int usedTypeAbbr, boolean active, Discount discount) {
        DiscountLog discountLog = new DiscountLog();
        discountLog.setChangeDate(System.currentTimeMillis());
        discountLog.setDiscount(discount);
        discountLog.setStatus(DiscountLog.DISCOUNT_UPDATED);
        databaseManager.insertDiscountLog(discountLog).subscribe();
        discount.keepToHistory();
        discount.setAmount(amount);
        discount.setAmountType(amountTypeAbbr);
        discount.setName(discription);
        discount.setUsedType(usedTypeAbbr);
        discount.setActive(active);
        databaseManager.insertDiscount(discount).subscribe((discount3, throwable1) -> {
            for (int i = 1; i < items.size(); i++) {
                if (items.get(i).getObject().getId().equals(discount.getId())) {
                    DiscountApaterDetials discountApaterDetials = new DiscountApaterDetials();
                    discountApaterDetials.setObject(discount);
                    items.set(i, discountApaterDetials);
                    view.notifyItemChanged(i);
                    return;
                }
            }
            view.sendChangeEvent(GlobalEventConstants.UPDATE, discount);
        });
    }

    @Override
    public void onDelete(Discount discount) {
        discount.setDelete(true);
        databaseManager.insertDiscount(discount).subscribe((discount1, throwable) -> {
            DiscountLog discountLog1 = new DiscountLog();
            discountLog1.setChangeDate(System.currentTimeMillis());
            discountLog1.setDiscount(discount1);
            discountLog1.setStatus(DiscountLog.DISCOUNT_DELETED);
            databaseManager.insertDiscountLog(discountLog1).subscribe();
            for (int i = 1; i < items.size(); i++) {
                if (items.get(i).getObject().getId().equals(discount.getId())) {
                    items.remove(i);
                    view.notifyItemRemove(i);
                    break;
                }
            }
            view.sendEvent(GlobalEventConstants.DELETE, discount);
        });
    }


    @Override
    public void sortList(DiscountSortTypes discountSortTypes) {
        items.remove(0);
        switch (discountSortTypes) {
            case Ammount:
                Collections.sort(items, (discounts, t1) -> t1.getObject().getAmount().compareTo(discounts.getObject().getAmount()));
                break;
            case Type:
                Collections.sort(items, (discounts, t1) -> t1.getObject().getAmountType().compareTo(discounts.getObject().getAmountType()));
                break;
            case Used:
                Collections.sort(items, (discounts, t1) -> t1.getObject().getUsedType().compareTo(discounts.getObject().getUsedType()));
                break;
            case Active:
                Collections.sort(items, (discounts, t1) -> t1.getObject().getActive().compareTo(discounts.getObject().getActive()));
                break;
            case Discription:
                Collections.sort(items, (discounts, t1) -> t1.getObject().getName().compareTo(discounts.getObject().getName()));
                break;
            case Default:
                Collections.sort(items, (discounts, t1) -> t1.getObject().getCreatedDate().compareTo(discounts.getObject().getCreatedDate()));
                break;
        }
        items.add(0, null);
        view.refreshList();
    }

    @Override
    public void onCloseAction() {
        boolean weCanClose = true;
        for (int i = 1; i < items.size(); i++) {
            if (items.get(i).isChanged())
                weCanClose = false;
        }
        if (weCanClose) {
            view.closeDiscountActivity();
        } else {
            view.openWarning();
        }
    }
}
