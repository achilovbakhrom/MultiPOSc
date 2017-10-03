package com.jim.multipos.ui.service_fee;

import android.content.Context;

import com.jim.multipos.R;
import com.jim.multipos.core.RxForPresenter;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.operations.CurrencyOperations;
import com.jim.multipos.data.operations.PaymentTypeOperations;
import com.jim.multipos.data.operations.ServiceFeeOperations;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.GlobalEventsConstants;
import com.jim.multipos.utils.rxevents.ServiceFeeEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by user on 28.08.17.
 */

public class ServiceFeePresenterImpl extends RxForPresenter implements ServiceFeePresenter {
    private Context context;
    private ServiceFeeView view;
    private List<ServiceFee> serviceFeeList;
    private List<Currency> currencies;
    private List<PaymentType> paymentTypes;
    private String[] types;
    private String[] appTypes;
    private ServiceFeeOperations serviceFeeOperations;
    private CurrencyOperations currencyOperations;
    private PaymentTypeOperations paymentTypeOperations;
    private RxBus rxBus;

    public ServiceFeePresenterImpl(RxBus rxBus, Context context, ServiceFeeView serviceFeeView, ServiceFeeOperations serviceFeeOperations, CurrencyOperations currencyOperations, PaymentTypeOperations paymentTypeOperations) {
        this.context = context;
        this.view = serviceFeeView;
        this.serviceFeeOperations = serviceFeeOperations;
        this.currencyOperations = currencyOperations;
        this.paymentTypeOperations = paymentTypeOperations;
        this.rxBus = rxBus;

        serviceFeeOperations.getAllServiceFees().subscribe(serviceFees ->
                        serviceFeeList = serviceFees
                , throwable -> serviceFeeList = new ArrayList<>());

        setRxListners();
    }

    @Override
    public void setRxListners() {
        ArrayList<Disposable> subscriptions = new ArrayList<>();

        subscriptions.add(rxBus.toObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof PaymentType) {
                        paymentTypeOperations.getAllPaymentTypes().subscribe(paymentTypes -> {
                            this.paymentTypes.clear();
                            this.paymentTypes.addAll(paymentTypes);
                        });
                    }

                    if (o instanceof Currency) {
                        currencyOperations.getAllCurrencies().subscribe(currencies -> {
                            this.currencies.clear();
                            this.currencies.addAll(currencies);

                            view.showSpCurrency(currencies);
                        });
                    }
                }));

        initUnsubscribers(rxBus, ServiceFeeActivity.class.getName(), subscriptions);
    }

    @Override
    public void initConnectors(RxBus rxBus, RxBusLocal rxBusLocal) {

    }

    @Override
    public void getSpTypes() {
        types = getStringArray(R.array.service_fee_type);
        view.showSpType(types);
    }

    @Override
    public void getCurrencies() {
        currencyOperations.getAllCurrencies().subscribe(currencies -> {
            this.currencies = currencies;
            view.showSpCurrency(currencies);
        });
    }

    @Override
    public void getAppTypes() {
        appTypes = getStringArray(R.array.service_fee_app_type);
        view.showAppType(appTypes);
    }

    @Override
    public void getPaymentType() {
        paymentTypeOperations.getAllPaymentTypes().subscribe(paymentTypes -> {
            this.paymentTypes = paymentTypes;
            view.openAutoApplyDialog(paymentTypes);
        });
    }

    @Override
    public void getServiceFeeData() {
        view.showRVServiceData(serviceFeeList, currencies, types, appTypes);
    }

    private boolean checkData(String name, String amount) {
        boolean hasError = false;

        if (name.isEmpty()) {
            hasError = true;
            view.showNameError(getString(R.string.enter_name));
        }

        if (amount.isEmpty()) {
            hasError = true;
            view.showAmountError(getString(R.string.enter_amount));
        }

        String[] str = amount.split("\\.");

        if (str[0].length() > 3) {
            hasError = true;
            view.showAmountError(getString(R.string.should_not_be_greater_than_999));
        }

        return hasError;
    }

    public void addItem(String name, String amount, int type, int currency, int appType, boolean isTaxed, boolean isActive, int paymentType) {
        boolean hasError = checkData(name, amount);

        if (!hasError) {
            ServiceFee serviceFee = new ServiceFee();
            serviceFee.setName(name);
            serviceFee.setAmount(Double.parseDouble(amount));
            serviceFee.setType(getTypeConst(type));
            serviceFee.setApplyingType(getAppTypeConst(appType));
            serviceFee.setIsTaxed(isTaxed);
            serviceFee.setIsActive(isActive);

            if (paymentType != -1) {
                serviceFee.setPaymentType(paymentTypes.get(paymentType));
            }

            if (type != 0) {
                serviceFee.setCurrency(currencies.get(currency));
            }

            serviceFeeOperations.addServiceFee(serviceFee).subscribe(aLong -> {
                rxBus.send(new ServiceFeeEvent(serviceFee, GlobalEventsConstants.ADD));
                serviceFeeList.add(0, serviceFee);

                view.clearViews();
                view.updateRecyclerView();
            });
        }
    }

    private void updateItem(int position) {
        ServiceFee serviceFee = serviceFeeList.get(position);

        serviceFeeOperations.addServiceFee(serviceFee).subscribe(aLong -> {
            rxBus.send(new ServiceFeeEvent(serviceFee, GlobalEventsConstants.UPDATE));
        });
    }

    private String getTypeConst(int position) {
        String type;

        if (position == 0) {
            type = Constants.TYPE_PERCENT;
        } else {
            type = Constants.TYPE_VALUE;
        }

        return type;
    }

    @Override
    public void setCheckedTaxed(boolean state, int position) {
        serviceFeeList.get(position).setIsTaxed(state);
        updateItem(position);
    }

    @Override
    public void setCheckedActive(boolean state, int position) {
        serviceFeeList.get(position).setIsActive(state);
        updateItem(position);
    }

    private String getAppTypeConst(int position) {
        String appType;

        switch (position) {
            case 0:
                appType = Constants.APP_TYPE_ITEM;
                break;
            case 1:
                appType = Constants.APP_TYPE_ORDER;
                break;
            case 2:
                appType = Constants.APP_TYPE_ALL;
                break;
            default:
                appType = "";
        }

        return appType;
    }

    @Override
    public void saveData() {
        view.closeActivity();
    }

    @Override
    public void changeType(int position) {
        if (position == 0) {
            view.enableCurrency(false);
        } else {
            view.enableCurrency(true);
        }
    }

    @Override
    public void openUsageTypeDialog(String name, String amount, int type, int currency, int appType, boolean isTaxed, boolean isActive) {
        boolean hasError = checkData(name, amount);

        if (!hasError) {
            view.openUsageTypeDialog();
        }
    }

    private String[] getStringArray(int resId) {
        return context.getResources().getStringArray(resId);
    }

    private String getString(int resId) {
        return context.getString(resId);
    }
}
