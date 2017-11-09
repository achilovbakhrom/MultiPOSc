package com.jim.multipos.ui.service_fee;

import android.content.Context;

import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.core.RxForPresenter;
import com.jim.multipos.data.DatabaseManager;
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

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by user on 28.08.17.
 */

public class ServiceFeePresenterImpl extends BasePresenterImpl<ServiceFeeView> implements ServiceFeePresenter {
    @Inject
    RxBus rxBus;
    private ServiceFeeView view;
    private List<ServiceFee> serviceFeeList;
    private List<Currency> currencies;
    private List<PaymentType> paymentTypes;
    private DatabaseManager databaseManager;
    private String enterName;
    private String enterAmount;
    private String shouldNotBeGreaterThan999;
    private String[] serviceFeeType;
    private String[] serviceFeeAppType;

    @Inject
    public ServiceFeePresenterImpl(ServiceFeeView view, DatabaseManager databaseManager,
                                   @Named(value = "enter_name") String enterName,
                                   @Named(value = "enter_amount") String enterAmount,
                                   @Named(value = "should_not_be_greater_than_999") String shouldNotBeGreaterThan999,
                                   @Named(value = "service_fee_type") String[] serviceFeeType,
                                   @Named(value = "service_fee_app_type") String[] serviceFeeAppType) {
        super(view);
        this.view = view;
        this.databaseManager = databaseManager;

        databaseManager.getServiceFeeOperations().getAllServiceFees().subscribe(serviceFees ->
                        serviceFeeList = serviceFees
                , throwable -> serviceFeeList = new ArrayList<>());

        this.enterName = enterName;
        this.enterAmount = enterAmount;
        this.shouldNotBeGreaterThan999 = shouldNotBeGreaterThan999;
        this.serviceFeeType = serviceFeeType;
        this.serviceFeeAppType = serviceFeeAppType;
        //setRxListners();
    }

    /*@Override
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
*/
    @Override
    public void getSpTypes() {
        view.showSpType(serviceFeeType);
    }

    @Override
    public void getCurrencies() {
        databaseManager.getCurrencyOperations().getAllCurrencies().subscribe(currencies -> {
            this.currencies = currencies;
            view.showSpCurrency(currencies);
        });
    }

    @Override
    public void getAppTypes() {
        view.showAppType(serviceFeeAppType);
    }

    @Override
    public void getPaymentType() {
        databaseManager.getPaymentTypeOperations().getAllPaymentTypes().subscribe(paymentTypes -> {
            this.paymentTypes = paymentTypes;
            view.openAutoApplyDialog(paymentTypes);
        });
    }

    @Override
    public void getServiceFeeData() {
        view.showRVServiceData(serviceFeeList, currencies, serviceFeeType, serviceFeeAppType);
    }

    private boolean checkData(String name, String amount) {
        boolean hasError = false;

        if (name.isEmpty()) {
            hasError = true;
            view.showNameError(enterName);
        }

        if (amount.isEmpty()) {
            hasError = true;
            view.showAmountError(enterAmount);
        }

        String[] str = amount.split("\\.");

        if (str[0].length() > 3) {
            hasError = true;
            view.showAmountError(shouldNotBeGreaterThan999);
        }

        return hasError;
    }

    public void addItem(String name, String amount, int type, int currency, int appType, boolean isTaxed, boolean isActive, int paymentType) {
        boolean hasError = checkData(name, amount);

        if (!hasError) {
            ServiceFee serviceFee = new ServiceFee();
            //serviceFee.setName(name);
            serviceFee.setAmount(Double.parseDouble(amount));
            serviceFee.setType(getTypeConst(type));
            serviceFee.setApplyingType(getAppTypeConst(appType));
            //serviceFee.setIsTaxed(isTaxed);
            serviceFee.setIsActive(isActive);

            if (paymentType != -1) {
              //  serviceFee.setPaymentType(paymentTypes.get(paymentType));
            }

            if (type != 0) {
                //serviceFee.setCurrency(currencies.get(currency));
            }

            databaseManager.getServiceFeeOperations().addServiceFee(serviceFee).subscribe(aLong -> {
                rxBus.send(new ServiceFeeEvent(serviceFee, GlobalEventsConstants.ADD));
                serviceFeeList.add(0, serviceFee);

                view.clearViews();
                view.updateRecyclerView();
            });
        }
    }

    private void updateItem(int position) {
        ServiceFee serviceFee = serviceFeeList.get(position);

        databaseManager.getServiceFeeOperations().addServiceFee(serviceFee).subscribe(aLong -> {
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
        //serviceFeeList.get(position).setIsTaxed(state);
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
}
