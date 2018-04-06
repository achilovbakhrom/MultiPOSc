package com.jim.multipos.ui.customer_debt.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jim.mpviews.MPosSpinner;
import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.CustomerPayment;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.utils.UIUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by developer on 04.12.2017.
 */

public class PayToDebtDialog extends Dialog {

    @BindView(R.id.spPaymentType)
    MPosSpinner spPaymentType;
    @BindView(R.id.etAmount)
    MpEditText etAmount;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    @BindView(R.id.btnPay)
    MpButton btnPay;
    @BindView(R.id.tvCurrencyAbbr)
    TextView tvCurrencyAbbr;
    private DecimalFormat decimalFormat;
    private List<Debt> debtList;
    private double allDebt;

    public PayToDebtDialog(Context context, Customer customer, Debt debt, DatabaseManager databaseManager, boolean closeDebt, boolean payToAll, UpdateCustomerDebtsListCallback callback) {
        super(context);

        View dialogView = getLayoutInflater().inflate(R.layout.payment_to_debt, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        decimalFormat = BaseAppModule.getFormatter();
        v.setBackgroundResource(android.R.color.transparent);
        tvCurrencyAbbr.setText(databaseManager.getMainCurrency().getAbbr());
        List<PaymentType> paymentTypes = databaseManager.getPaymentTypes();
        List<String> paymentTypeNames = new ArrayList<>();
        for (PaymentType paymentType : paymentTypes) {
            paymentTypeNames.add(paymentType.getName());
        }
        spPaymentType.setAdapter(paymentTypeNames);

        if (closeDebt) {
            double feeAmount = debt.getFee() * debt.getDebtAmount() / 100;
            double dueSum = debt.getDebtAmount() + feeAmount;
            if (debt.getCustomerPayments().size() > 0) {
                for (CustomerPayment payment : debt.getCustomerPayments()) {
                    dueSum -= payment.getPaymentAmount();
                }
            }
            etAmount.setText(decimalFormat.format(dueSum));
        }
        allDebt = 0;
        if (payToAll) {
            databaseManager.getDebtsByCustomerId(customer.getId()).subscribe((debts, throwable) -> {
                debtList = debts;
                for (Debt item : debtList) {
                    double feeAmount = item.getFee() * item.getDebtAmount() / 100;
                    allDebt += item.getDebtAmount() + feeAmount;
                    if (item.getCustomerPayments().size() > 0)
                        for (int i = 0; i < item.getCustomerPayments().size(); i++) {
                            allDebt -= item.getCustomerPayments().get(i).getPaymentAmount();
                        }
                }
                etAmount.setText(decimalFormat.format(allDebt));
            });

        }


        btnBack.setOnClickListener(view -> {
            UIUtils.closeKeyboard(btnBack, context);
            dismiss();
        });

        btnPay.setOnClickListener(view -> {
            if (!etAmount.getText().toString().isEmpty()) {
                if (debtList != null && debtList.size() > 0) {
                    double amount = 0;
                    try {
                        amount = decimalFormat.parse(etAmount.getText().toString().replace(",", ".")).doubleValue();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (allDebt < amount)
                        etAmount.setError("Payment amount cannot be bigger than debt amount");
                    else if (amount == 0)
                        etAmount.setError("Payment amount cannot be equal zero");
                    else {
                        for (int i = 0; i < debtList.size(); i++) {
                            Debt item = debtList.get(i);
                            double totalDebt = item.getDebtAmount() + item.getFee() * item.getDebtAmount() / 100;
                            if (item.getCustomerPayments().size() > 0) {
                                for (CustomerPayment payment : item.getCustomerPayments()) {
                                    totalDebt -= payment.getPaymentAmount();
                                }
                            }
                            if (amount > totalDebt) {
                                CustomerPayment payment = new CustomerPayment();
                                payment.setDebt(item);
                                payment.setPaymentDate(System.currentTimeMillis());
                                payment.setPaymentType(paymentTypes.get(spPaymentType.getSelectedPosition()));
                                payment.setPaymentAmount(totalDebt);
                                payment.setDebtDue(0);
                                item.setStatus(Debt.CLOSED);
                                databaseManager.addDebt(item).blockingGet();
                                databaseManager.addCustomerPayment(payment).subscribe();
                                item.resetCustomerPayments();
                                amount -= totalDebt;
                            } else if (amount == totalDebt) {
                                CustomerPayment payment = new CustomerPayment();
                                payment.setDebt(item);
                                payment.setPaymentDate(System.currentTimeMillis());
                                payment.setPaymentType(paymentTypes.get(spPaymentType.getSelectedPosition()));
                                payment.setPaymentAmount(amount);
                                payment.setDebtDue(0);
                                item.setStatus(Debt.CLOSED);
                                databaseManager.addDebt(item).blockingGet();
                                databaseManager.addCustomerPayment(payment).subscribe();
                                item.resetCustomerPayments();
                                break;
                            } else {
                                CustomerPayment payment = new CustomerPayment();
                                payment.setDebt(item);
                                payment.setPaymentDate(System.currentTimeMillis());
                                payment.setPaymentType(paymentTypes.get(spPaymentType.getSelectedPosition()));
                                payment.setPaymentAmount(amount);
                                payment.setDebtDue(totalDebt - amount);
                                databaseManager.addCustomerPayment(payment).subscribe();
                                item.resetCustomerPayments();
                                break;
                            }
                        }
                        callback.onPay(debt.getCustomer());
                        UIUtils.closeKeyboard(btnPay, context);
                        dismiss();
                    }
                } else {
                    double feeAmount = debt.getFee() * debt.getDebtAmount() / 100;
                    double total = debt.getDebtAmount() + feeAmount;
                    double amount = 0;
                    try {
                        amount = decimalFormat.parse(etAmount.getText().toString().replace(",", ".")).doubleValue();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (total < amount)
                        etAmount.setError("Payment amount cannot be bigger than debt amount");
                    else if (amount == 0)
                        etAmount.setError("Payment amount cannot be equal zero");
                    else if (total != amount && debt.getDebtType() == Debt.ALL) {
                        etAmount.setError("You cannot pay in participle for this debt");
                    } else {
                        double dueSum = total;
                        if (debt.getCustomerPayments().size() > 0) {
                            for (CustomerPayment payment : debt.getCustomerPayments()) {
                                dueSum -= payment.getPaymentAmount();
                            }
                        }
                        CustomerPayment payment = new CustomerPayment();
                        payment.setDebt(debt);
                        payment.setPaymentDate(System.currentTimeMillis());
                        payment.setPaymentType(paymentTypes.get(spPaymentType.getSelectedPosition()));
                        payment.setPaymentAmount(amount);
                        payment.setDebtDue(dueSum - amount);
                        if (dueSum - amount == 0) {
                            debt.setStatus(Debt.CLOSED);
                            databaseManager.addDebt(debt).blockingGet();
                        }
                        databaseManager.addCustomerPayment(payment).subscribe();
                        debt.resetCustomerPayments();
                        callback.onPay(debt.getCustomer());
                        UIUtils.closeKeyboard(btnPay, context);
                        dismiss();
                    }
                }
            } else {
                etAmount.setError(context.getString(R.string.enter_amount));
            }
        });

    }

    public interface UpdateCustomerDebtsListCallback {
        void onPay(Customer customer);
    }
}
