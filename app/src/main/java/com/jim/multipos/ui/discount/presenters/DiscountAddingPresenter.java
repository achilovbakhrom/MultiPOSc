package com.jim.multipos.ui.discount.presenters;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.Discount;

/**
 * Created by developer on 23.10.2017.
 */

public interface DiscountAddingPresenter extends Presenter {
    void onAddPressed(double amount,int amountTypeAbbr, String discription, int usedTypeAbbr, boolean active);
    void onSave(double amount,int amountTypeAbbr, String discription, int usedTypeAbbr, boolean active, Discount discount);
    void onDelete(Discount discount);
    void sortList(DiscountAddingPresenterImpl.DiscountSortTypes discountSortTypes);
    void onCloseAction();
}
