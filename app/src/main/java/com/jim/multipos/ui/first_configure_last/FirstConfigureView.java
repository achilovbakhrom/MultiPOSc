package com.jim.multipos.ui.first_configure_last;

import android.content.Context;

import com.jim.multipos.core.BaseView;

/**
 * Created by user on 07.10.17.
 */

public interface FirstConfigureView extends BaseView {

    void openPOSDetailsFragment(CompletionMode mode);

    void openAccountFragment(CompletionMode mode);

    void openCurrencyFragment(CompletionMode mode);

    void openPaymentTypeFragment(CompletionMode mode);

    void select(int position);

    void changeState(int position, int state);

    void makeToast(int resId);

//    void replaceFragment(int position);
//
//    void openPrevFragment();
//
//    void updateLeftSideFragment(int position);
//
//    void closeActivity();
//
//    void addAccountItem(Account account);
//
//    void addPaymentTypeItem(PaymentType paymentType);
//
//    void removeAccountItem(Account account);
//
//    void removePaymentTypeItem(PaymentType paymentType);
//
//    void setCurrencySpinnerData(List<Currency> currencies, int position);
//
//    void showPaymentTypeCurrencyToast();
//
//    void showPaymentTypeAccountToast();
//
//    void showPaymentTypeToast();
//
//    void showAccountToast();
//
//    void setPaymentTypeCurrency(Currency currency);
//
//    void setPaymentTypeAccount(List<Account> accounts);
//
//    void addWeightUnit(Unit unit);
//
//    void removeWeightUnit(Unit unit);
//
//    void addLengthUnit(Unit unit);
//
//    void removeLengthUnit(Unit unit);
//
//    void addAreaUnit(Unit unit);
//
//    void removeAreaUnit(Unit unit);
//
//    void addVolumeUnit(Unit unit);
//
//    void removeVolumeUnit(Unit unit);
}
