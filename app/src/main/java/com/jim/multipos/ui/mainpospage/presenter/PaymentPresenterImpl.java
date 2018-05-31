package com.jim.multipos.ui.mainpospage.presenter;

import android.content.Context;
import android.os.Bundle;

import com.jim.mpviews.model.PaymentTypeWithService;
import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Account;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.mainpospage.view.PaymentView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

public class PaymentPresenterImpl extends BasePresenterImpl<PaymentView> implements PaymentPresenter {

    private DatabaseManager databaseManager;
    List<PaymentType> paymentTypes;
    private Context context;
    private PreferencesHelper preferencesHelper;
    PaymentType currentPayment;
    List<PayedPartitions> payedPartitions;
    private Order order;
    double lastPaymentAmountState = 0;
    private Customer customer = null;
    private PaymentType debtPayment;
    @Inject
    public PaymentPresenterImpl(PaymentView paymentView, DatabaseManager databaseManager, Context context, PreferencesHelper preferencesHelper) {
        super(paymentView);
        this.databaseManager = databaseManager;
        paymentTypes = databaseManager.getPaymentTypes();
        this.context = context;
        this.preferencesHelper = preferencesHelper;
        debtPayment = databaseManager.getDebtPaymentType().blockingGet();
    }


    /**
     special for sending local datas to Debt dialog, now not have any data
    */
    @Override
    public void onDebtBorrowClicked() {
        if(debt==null){
            view.openAddDebtDialog(databaseManager, order, customer,order.getForPayAmmount() - totalPayed());
        }else {
            debt = null;
            removeDebtPaymentPartition();
            view.showDebtDialog();
        }
    }

    /**
     change payment type for state
     */
    @Override
    public void changePayment(int positionPayment) {
        currentPayment = paymentTypes.get(positionPayment);
    }

    /**
     method for remove payed partition from list payed partition
     */
    @Override
    public void removePayedPart(int removedPayedPart) {
        if(payedPartitions.get(removedPayedPart).getPaymentType().getTypeStaticPaymentType() == PaymentType.DEBT_PAYMENT_TYPE){
            debt = null;
            view.showDebtDialog();
        }
        payedPartitions.remove(removedPayedPart);
        view.updatePaymentList();
        view.updateViews(order,totalPayed());
        updateChange();
        view.onPayedPartition();

    }

    /**
     init payment types to view
     */
    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        ArrayList<PaymentTypeWithService> paymentTypeWithServices = new ArrayList<>();
        for (int i = 0; i < paymentTypes.size(); i++) {
            PaymentTypeWithService paymentTypeWithService= new PaymentTypeWithService(paymentTypes.get(i).getName(),"");
            paymentTypeWithServices.add(paymentTypeWithService);
        }
        view.initPaymentTypes(paymentTypeWithServices);
    }

    /**
     it will work when getting datas from order list, for init or update view
     */
    @Override
    public void incomeNewData(Order order, List<PayedPartitions> payedPartitions) {
        this.order = order;
        this.payedPartitions = payedPartitions;
        view.updatePaymentList(payedPartitions);
        view.updateViews(order,totalPayed());
        updateChange();
    }

    /**
     last payment amount automatic parsed when typing payment amount
     */
    @Override
    public void typedPayment(double paymentTyped) {
        lastPaymentAmountState = paymentTyped;
        updateChange();
    }
    boolean isPay = true;

    /**
     calculation change amount and decision making what should we view
     */
    private void updateChange(){
        if(order==null) return;
        double change = order.getForPayAmmount() - totalPayed() - lastPaymentAmountState;
        change *=-1;
        if(order.getSubTotalValue() == 0){
            isPay = false;
            view.updateCloseText();
            return;
        }
        if(change<-0.01){
            //it is not enough money
            isPay = true;
            view.updateBalanceView(change*-1);
        }else if(change>0.01){
            //it is enough money
            isPay = false;
            view.updateChangeView(change);
        }else {
            //it is enough money, payment amount equals balance due
            isPay = false;
            view.updateBalanceZeroText();
        }

    }

    /**
     pressed first optional button
     */
    @Override
    public void pressFirstOptional() {
        double firstOptionalValue = preferencesHelper.getFirstOptionalPaymentButton();
        view.updatePaymentText(lastPaymentAmountState + firstOptionalValue);
    }

    /**
     pressed second optional button
     */
    @Override
    public void pressSecondOptional() {
        double secondOptionalValue = preferencesHelper.getSecondOptionalPaymentButton();
        view.updatePaymentText(lastPaymentAmountState + secondOptionalValue);
    }

    /**
     pressed all amount button
     */
    @Override
    public void pressAllAmount() {
        double payment = order.getForPayAmmount() - totalPayed();
        if(payment<0) payment = 0;
        view.updatePaymentText(payment);
    }

    @Override
    public void payButtonPressed() {
        if(order.getSubTotalValue() == 0){
            view.closeSelf();
            return;
        }
        if(isPay){
            boolean hasOpenTill = databaseManager.hasOpenTill().blockingGet();
            if (!hasOpenTill){
                view.openWarningDialog(context.getString(R.string.opened_till_wnt_found_pls_open_till));
                return;
            }
            //PAY
            //it is pay operation, because payment amount not enough for close order
            if(lastPaymentAmountState <=0)return;
            for (PayedPartitions payedPartition:payedPartitions) {
                if(payedPartition.getPaymentType().getId().equals(currentPayment.getId())){
                    payedPartition.setValue(payedPartition.getValue()+lastPaymentAmountState);
                    view.updateViews(order,totalPayed());
                    view.updatePaymentList();
                    view.onPayedPartition();
                    return;
                }
            }

            PayedPartitions payedPartition = new PayedPartitions();
            payedPartition.setPaymentType(currentPayment);
            payedPartition.setValue(lastPaymentAmountState);
            payedPartitions.add(payedPartition);
            view.updateViews(order,totalPayed());
            view.updatePaymentList();
            view.onPayedPartition();
        }else {
            boolean hasOpenTill = databaseManager.hasOpenTill().blockingGet();
            if (!hasOpenTill){
                view.openWarningDialog(context.getString(R.string.opened_till_wnt_found_pls_open_till));
                return;
            }
            //DONE
            //it is done operation for close order. Because payment amount enough for close order
            if(order.getForPayAmmount() - totalPayed()<=0){
                view.updateViews(order,totalPayed());
                view.updatePaymentList();
                view.onPayedPartition();
                view.closeOrder(order,payedPartitions,debt);
                return;
            }
            if(order.getForPayAmmount()==0){
                //TODO FREE ORDER
                view.updateViews(order,totalPayed());
                view.updatePaymentList();
                view.onPayedPartition();
                view.closeOrder(order,payedPartitions,debt);
                return;
            }

            for (PayedPartitions payedPartition:payedPartitions) {
                if(payedPartition.getPaymentType().getId().equals(currentPayment.getId())){
                    payedPartition.setValue(payedPartition.getValue()+lastPaymentAmountState);
                    view.updateViews(order,totalPayed());
                    view.updatePaymentList();
                    view.onPayedPartition();
                    view.closeOrder(order,payedPartitions,debt);
                    return;
                }
            }

            PayedPartitions payedPartition = new PayedPartitions();
            payedPartition.setPaymentType(currentPayment);
            payedPartition.setValue(lastPaymentAmountState);
            payedPartitions.add(payedPartition);
            view.updateViews(order,totalPayed());
            view.updatePaymentList();
            view.onPayedPartition();
            view.closeOrder(order,payedPartitions,debt);
        }
    }

    @Override
    public void setCustomer(Customer customer) {
        this.customer = customer;
        if(customer == null){
            //DELETE DEBT
            debt = null;
            view.showDebtDialog();
        }
    }
    Debt debt;
    @Override
    public void onDebtSave(Debt debt) {
        //TODO add Debt To order
        customer = debt.getCustomer();
        this.debt = debt;

        PayedPartitions payedPartition = new PayedPartitions();
        payedPartition.setPaymentType(debtPayment);
        payedPartition.setValue(debt.getDebtAmount());
        payedPartitions.add(payedPartition);
        view.updateViews(order,totalPayed());
        view.updatePaymentList();
        view.onPayedPartition();
        view.updateCustomer(debt.getCustomer());
        view.hideDebtDialog();

    }
    private void removeDebtPaymentPartition(){
        for (int i = 0; i < payedPartitions.size(); i++) {
            if(payedPartitions.get(i).getPaymentType().getTypeStaticPaymentType() == PaymentType.DEBT_PAYMENT_TYPE){
                payedPartitions.remove(i);
            }
        }
        view.updateViews(order,totalPayed());
        view.updatePaymentList();
        view.onPayedPartition();
    }
    @Override
    public void onClickedTips() {
        if(order.getTips() == 0){
            view.openTipsDialog((value, account) ->  {
                order.setTipsAccount(account);
                order.setTips(value);
                view.updateViews(order,totalPayed());
                view.updateOrderListDetialsPanel();
                view.disableTipsButton();
            },((order.getForPayAmmount()-totalPayed()-lastPaymentAmountState)>=0)?0:(order.getForPayAmmount()-totalPayed()-lastPaymentAmountState)*-1);
        }else {
            order.setTips(0);
            view.updateViews(order,totalPayed());
            view.updateOrderListDetialsPanel();
            view.enableTipsButton();
        }
    }

    @Override
        public void onNewOrder() {
        debt = null;
        customer = null;
        view.enableTipsButton();
        view.showDebtDialog();
    }

    @Override
    public void sendDataToPaymentFragmentWhenEdit(Order order, List<PayedPartitions> payedPartitions, Debt debt) {
        this.payedPartitions = payedPartitions;
        this.debt = debt;

        if(debt==null)
            view.showDebtDialog();
        else view.hideDebtDialog();

        if(order.getTips() == 0) {
            view.enableTipsButton();
        }else {
            view.disableTipsButton();
        }

        view.updatePaymentList(payedPartitions);
        view.updateViews(order,totalPayed());
        updateChange();
    }

    @Override
    public void onHoldOrderClicked() {
        //TODO IF WANT: PUL KIRITGANDA HOLDNI BOSSA KRIITILGAN PULAM MAYMENT BOB KETISHINI HOHLASA LOGIKANI SHU YERGA DOPISAVAT QILISH KERE
        view.onHoldOrderSendingData(order,payedPartitions,debt);
    }


    /**
     method for sum payed partitions
     */
    private double totalPayed(){
        double totalPayed = 0;
        for (PayedPartitions payedPartition:payedPartitions) {
            totalPayed += payedPartition.getValue();
        }
        return totalPayed;
    }
}
