package com.jim.multipos.ui.first_configure.presenters;

import android.content.Context;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.operations.CurrencyOperations;
import com.jim.multipos.ui.first_configure.FirstConfigureActivity;
import com.jim.multipos.ui.first_configure.fragments.CurrencyFragmentView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;



/**
 * Created by user on 05.08.17.
 */

public class CurrencyFragmentPresenterImpl implements CurrencyFragmentPresenter {
    private CurrencyFragmentView view;
    private List<Currency> mainCurrencies;
    private List<Currency> otherCurrencies;
    private List<Currency> selectedCurrencies;
    private Currency mainRemovedCurrency;
    private int currentMainCurrency;
    private List<Currency> removedList;
    private CurrencyOperations currencyOperations;
    private String[] curr;
    private String[] abbr;
    private List<Currency> currencies;
    private boolean isFirstLaunch = true;
    private PaymentTypeFragmentPresenter paymentTypeFragmentPresenter;

    public CurrencyFragmentPresenterImpl(Context context, PaymentTypeFragmentPresenter paymentTypeFragmentPresenter, CurrencyOperations currencyOperations) {
        this.currencyOperations = currencyOperations;
        this.paymentTypeFragmentPresenter = paymentTypeFragmentPresenter;
        curr = context.getResources().getStringArray(R.array.base_currencies);
        abbr = context.getResources().getStringArray(R.array.base_abbrs);
        mainCurrencies = getCurr();
        otherCurrencies = getCurr();
        selectedCurrencies = new ArrayList<>();
        removedList = new ArrayList<>();
        currencyOperations.getAllCurrencies().subscribe(currencies -> {
            this.currencies = currencies;
        });
    }

    @Override
    public void init(CurrencyFragmentView view) {
        this.view = view;
    }

    @Override
    public boolean isCompleteData() {
        return true;
    }

    @Override
    public void setData() {
        if (isFirstLaunch) {
            if (!currencies.isEmpty()) {
                for (Currency currency : currencies) {
                    if (currency.isMain()) {
                        for (int i = 0; i < mainCurrencies.size(); i++) {
                            if (mainCurrencies.get(i).getName().equals(currency.getName())) {
                                currentMainCurrency = i;
                                mainCurrencies.set(i, currency);

                                for (int j = 0; j < otherCurrencies.size(); j++) {
                                    if (otherCurrencies.get(j).getName().equals(currency.getName())) {
                                        mainRemovedCurrency = otherCurrencies.get(j);
                                        otherCurrencies.remove(i);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < otherCurrencies.size(); i++) {
                            if (otherCurrencies.get(i).getName().equals(currency.getName())) {
                                otherCurrencies.remove(i);
                                break;
                            }
                        }

                        selectedCurrencies.add(currency);
                    }
                }
            } else {
                currentMainCurrency = 0;
                mainRemovedCurrency = mainCurrencies.get(0);
                mainRemovedCurrency.setIsMain(true);
                otherCurrencies.remove(0);
            }

            isFirstLaunch = false;
        }
        paymentTypeFragmentPresenter.updateCurrencyData(getCurrencies());
        view.showMainCurrencies(mainCurrencies, currentMainCurrency);
        view.showOtherCurrencies(otherCurrencies);
        view.showRecyclerView(selectedCurrencies);
    }

    @Override
    public void setMainCurrencies() {
        view.showMainCurrencies(mainCurrencies, currentMainCurrency);
    }

    @Override
    public void setOtherCurrencies() {
        view.showOtherCurrencies(otherCurrencies);
    }

    @Override
    public void changeMainCurrency(int position) {
        Currency mainCurrency = mainCurrencies.get(position);
        String mainName = mainCurrency.getName();
        mainCurrency.setMain(true);
        mainRemovedCurrency.setMain(false);

        for (int i = 0; i < otherCurrencies.size(); i++) {
            if (otherCurrencies.get(i).getName().equals(mainName)) {
                Currency temp = otherCurrencies.get(i);
                otherCurrencies.remove(i);
                otherCurrencies.add(0, mainRemovedCurrency);
                mainRemovedCurrency = temp;

                break;
            }
        }

        for (int i = 0; i < selectedCurrencies.size(); i++) {
            Currency currency = selectedCurrencies.get(i);

            if (currency.getName().equals(mainName)) {
                otherCurrencies.add(0, mainRemovedCurrency);
                mainRemovedCurrency = currency;
                selectedCurrencies.remove(currency);

                break;
            }
        }

        currentMainCurrency = position;

        paymentTypeFragmentPresenter.updateCurrencyData(getCurrencies());
        view.updateView();
    }

    @Override
    public List<Currency> getCurrencies() {
        List<Currency> currencies = new ArrayList();
        currencies.add(mainCurrencies.get(currentMainCurrency));
        currencies.addAll(selectedCurrencies);

        return currencies;
    }

    @Override
    public void setAdapterData() {
        view.showRecyclerView(selectedCurrencies);
    }

    @Override
    public void addCurrency(int position) {
        selectedCurrencies.add(0, otherCurrencies.get(position));
        otherCurrencies.remove(position);

        paymentTypeFragmentPresenter.updateCurrencyData(getCurrencies());
        view.updateView();
    }

    @Override
    public void removeSelectedCurrency(int position) {
        Currency currency = selectedCurrencies.get(position);

        otherCurrencies.add(0, currency);
        removedList.add(currency);
        selectedCurrencies.remove(position);

        paymentTypeFragmentPresenter.updateCurrencyData(getCurrencies());
        view.updateView();
    }

    @Override
    public List<Currency> getRemovedList() {
        return removedList;
    }

    private List<Currency> getCurr() {
        List<Currency> list = new ArrayList<>();

        for (int i = 0; i < curr.length; i++) {
            Currency currency = new Currency();
            currency.setName(curr[i]);
            currency.setAbbr(abbr[i]);

            list.add(currency);
        }

        return list;
    }

    @Override
    public void saveData() {
        currencyOperations.removeAllCurrencies().subscribe(aBoolean -> {
            currencies.clear();
            currencies.add(mainCurrencies.get(currentMainCurrency));
            currencies.addAll(selectedCurrencies);
            currencyOperations.addCurrencies(currencies).subscribe();
        });
    }
}
