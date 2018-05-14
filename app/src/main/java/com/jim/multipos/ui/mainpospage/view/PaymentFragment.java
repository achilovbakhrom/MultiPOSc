package com.jim.multipos.ui.mainpospage.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jim.mpviews.MpList;
import com.jim.mpviews.MpNumPad;
import com.jim.mpviews.MpNumPadSecond;
import com.jim.mpviews.MpSecondSwticher;
import com.jim.mpviews.model.PaymentTypeWithService;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.mainpospage.adapter.PaymentPartsAdapter;
import com.jim.multipos.ui.mainpospage.connection.MainPageConnection;
import com.jim.multipos.ui.mainpospage.dialogs.AddDebtDialog;
import com.jim.multipos.ui.mainpospage.dialogs.TipsDialog;
import com.jim.multipos.ui.mainpospage.presenter.PaymentPresenter;
import com.jim.multipos.utils.NumberTextWatcherPaymentFragment;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.printer.CheckPrinter;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;

/**
 * Created by developer on 22.08.2017.
 */

public class PaymentFragment extends BaseFragment implements PaymentView {
    @Inject
    PaymentPresenter presenter;
    @Inject
    DatabaseManager databaseManager;
    DecimalFormat decimalFormat;
    @Inject
    PreferencesHelper preferencesHelper;
    @Inject
    MainPageConnection mainPageConnection;
    @BindView(R.id.llDebtBorrow)
    LinearLayout llDebtBorrow;
    @BindView(R.id.tvPay)
    TextView tvPay;
    @BindView(R.id.mpLPaymentTypeList)
    MpList mpList;
    @BindView(R.id.mpSSwitcher)
    MpSecondSwticher mpSSwitcher;
    @BindView(R.id.flPaymentList)
    FrameLayout flPaymentList;
    @BindView(R.id.rvPaymentsListHistory)
    RecyclerView rvPaymentsListHistory;
    @BindView(R.id.llPrintCheck)
    LinearLayout llPrintCheck;
    @BindView(R.id.etPaymentAmount)
    EditText etPaymentAmount;
    @BindView(R.id.tvBalanceDue)
    TextView tvBalanceDue;
    @BindView(R.id.tvChange)
    TextView tvChange;
    @BindView(R.id.tvBalanceOrChange)
    TextView tvBalanceOrChange;
    @BindView(R.id.ivDebt)
    ImageView ivDebt;
    @BindView(R.id.tvDebt)
    TextView tvDebt;
    @BindView(R.id.llTips)
    LinearLayout llTips;
    @BindView(R.id.tvTips)
    TextView tvTips;
    @BindView(R.id.ivTips)
    ImageView ivTips;
    @BindView(R.id.ivPrint)
    ImageView ivPrint;
    @BindView(R.id.tvPrint)
    TextView tvPrint;
    @BindView(R.id.tvChangeCurrency)
    TextView tvChangeCurrency;
    @BindView(R.id.tvBalanceDueCurrency)
    TextView tvBalanceDueCurrency;
    @BindView(R.id.tvPaymentCurrency)
    TextView tvPaymentCurrency;
    CheckPrinter checkPrinter;

    DecimalFormat df;
    DecimalFormat dfnd;
    PaymentPartsAdapter paymentPartsAdapter;
    private AddDebtDialog dialog;

    TextWatcher watcher;

    @Inject
    @Named(value = "without_grouping_two_decimal")
    DecimalFormat dfe;


    @Override
    protected int getLayout() {
        return R.layout.main_page_payment_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if(checkPrinter ==null){
            checkPrinter = new CheckPrinter(getActivity(),preferencesHelper,databaseManager);
            checkPrinter.connectDevice();
        }

        presenter.onCreateView(savedInstanceState);
        df = BaseAppModule.getFormatterWithoutGroupingTwoDecimal();
        df.setRoundingMode(RoundingMode.DOWN);
        df.setDecimalSeparatorAlwaysShown(true);

        dfnd = BaseAppModule.getFormatterGroupingWithoutDecimalPart();
        dfnd.setRoundingMode(RoundingMode.DOWN);
        decimalFormat = BaseAppModule.getFormatterGrouping();


        //decimal format with space
//        DecimalFormat formatter;
//        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("RU"));
//        numberFormat.setMaximumFractionDigits(2);
//        formatter = (DecimalFormat) numberFormat;
//        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//        symbols.setGroupingSeparator(' ');
//        formatter.setDecimalFormatSymbols(symbols);
//        decimalFormat = formatter;
        //edit text for input with custom buttons
        etPaymentAmount.setRawInputType(InputType.TYPE_CLASS_TEXT);
        etPaymentAmount.setTextIsSelectable(true);
        //sending event press pay button to presenter. In presenter have 2 state : PAY (payment part), DONE (collect and save)
        tvPay.setOnClickListener(view1 -> {
            presenter.payButtonPressed();
        });
        //switcher for change views: left PAYMENT TYPES (mpList), right PAYMENT LIST (flPaymentList)
        mpSSwitcher.setClickListener(new MpSecondSwticher.CallbackFromMpSecondSwitcher() {
            @Override
            public void onLeftSideClick() {
                mpList.setVisibility(View.VISIBLE);
                flPaymentList.setVisibility(View.GONE);
            }

            @Override
            public void onRightSideClick() {
                mpList.setVisibility(View.GONE);
                flPaymentList.setVisibility(View.VISIBLE);
            }
        });
        //sending event press ToBorrow to presenter.
        llDebtBorrow.setOnClickListener(view12 -> {
            presenter.onDebtBorrowClicked();
        });

        if(preferencesHelper.isPrintCheck()){
            ivPrint.setColorFilter(Color.parseColor("#419fd9"));
            tvPrint.setTextColor(Color.parseColor("#419fd9"));
            tvPrint.setText(R.string.print_check_on);
        }else {
            ivPrint.setColorFilter(Color.parseColor("#999999"));
            tvPrint.setTextColor(Color.parseColor("#999999"));
            tvPrint.setText(R.string.print_check);
        }

        //print state dialog, automatic print check or not automatic. Custom print
        llPrintCheck.setOnClickListener(view -> {
            if(preferencesHelper.isPrintCheck()){
                ivPrint.setColorFilter(Color.parseColor("#999999"));
                tvPrint.setTextColor(Color.parseColor("#999999"));
                tvPrint.setText(R.string.print_check);
                preferencesHelper.setPrintCheck(false);
            }else {
                ivPrint.setColorFilter(Color.parseColor("#419fd9"));
                tvPrint.setTextColor(Color.parseColor("#419fd9"));
                tvPrint.setText(R.string.print_check_on);
                preferencesHelper.setPrintCheck(true);
            }
        });
        //edit text change listner used for parsing value in real time, and sending to presenter

        etPaymentAmount.addTextChangedListener(new NumberTextWatcherPaymentFragment(etPaymentAmount,presenter));

        llTips.setOnClickListener(view -> {
            presenter.onClickedTips();
        });

        //init number and opertion square buttons: 1 2 3 4 5 6 7 8 9 0 00 < , op1 op2
        initButtons();
        //get request for order and payed partition list data to OrderListFragment
        mainPageConnection.setPaymentView(this);
        mainPageConnection.giveToPaymentFragmentOrderAndPaymentsList();
    }

    /**
     refresh data when fragment after hide show
     get request for order and payed partition list data to OrderListFragment
     this function called from activity, management fragments
     * */
    public void refreshData(){
        mainPageConnection.setPaymentView(this);
        mainPageConnection.giveToPaymentFragmentOrderAndPaymentsList();
    }

    /**
     show ToDebt dialog, to constructor should give Customer and Order data
     * */
    @Override
    public void openAddDebtDialog(DatabaseManager databaseManager, Order order, Customer customer,double toPay) {
        dialog = new AddDebtDialog(getContext(), customer, databaseManager, order, new AddDebtDialog.onDebtSaveClickListener() {
            @Override
            public void onDebtSave(Debt debt) {
                presenter.onDebtSave(debt);
            }

            @Override
            public void onScanBarcode() {
                initScan();
            }
        },toPay,decimalFormat);
        dialog.show();
    }

    /**
     this method for scanning barcode
     */
    public void initScan(){
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }

    /**
     * in this method we will take scan result
    **/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() != null) {
                if (dialog != null)
                    dialog.setScanResult(intentResult.getContents());
            }
        }
    }

    /**
     init Payment types list to view, and set on item click listener, mpList it is stateable positional list
     * */
    @Override
    public void initPaymentTypes(List<PaymentTypeWithService> paymentTypeWithServices) {
        mpList.setPayments(paymentTypeWithServices);
        mpList.setOnPaymentClickListner(position -> {
            presenter.changePayment(position);
        });
    }

    /**
     this method used for init or update payed parts list
     * */
    @Override
    public void updatePaymentList(List<PayedPartitions> payedPartitions) {
        if(paymentPartsAdapter==null) {
            //init
            paymentPartsAdapter = new PaymentPartsAdapter(payedPartitions, position -> {
                //sending remove payment part event to presenter
                presenter.removePayedPart(position);
            }, decimalFormat);
            rvPaymentsListHistory.setLayoutManager(new LinearLayoutManager(getContext()));
            rvPaymentsListHistory.setAdapter(paymentPartsAdapter);
            ((SimpleItemAnimator) rvPaymentsListHistory.getItemAnimator()).setSupportsChangeAnimations(false);
        }else {
            //refresh
            updatePaymentList();
        }

    }

    /**
     refresh payed parts list
     * */
    @Override
    public void updatePaymentList() {
        paymentPartsAdapter.notifyDataSetChanged();
    }

    /**
     find actually balanceDue, if it is very small value - balance due equals to zero
     * */
    @Override
    public void updateViews(Order order,double totalPayed) {
        double number = order.getForPayAmmount() - totalPayed;
        if(number<0.001){
            number = 0;
        }
        tvBalanceDue.setText(decimalFormat.format(number));
        etPaymentAmount.requestFocus();
    }

    /**
     getting new data from OrderList Fragment via MainpageConnector (dagger 2)
     * */
    @Override
    public void getDataFromListOrder(Order order, List<PayedPartitions> payedPartitions) {
        presenter.incomeNewData(order,payedPartitions);
    }

    /**
     it method will work when payment amount bigger than balance due
     Update views to it done and show Change amount
     * */
    @Override
    public void updateChangeView(double change) {
        tvBalanceOrChange.setText("Change");
        tvBalanceOrChange.setTextColor(Color.parseColor("#4ac21b"));
        tvChange.setText(decimalFormat.format(change));
        tvChange.setTextColor(Color.parseColor("#4ac21b"));
        tvPay.setText("Done");
    }

    /**
     it method will work when payment amount smaller than balance due
     Update views to it payment and show Balance amount
     * */
    @Override
    public void updateBalanceView(double change) {
        tvBalanceOrChange.setText("Balance");
        tvBalanceOrChange.setTextColor(Color.parseColor("#df595a"));
        tvChange.setText(decimalFormat.format(change));
        tvChange.setTextColor(Color.parseColor("#df595a"));
        tvPay.setText("Pay");
    }

    /**
     it method will work when payment amount equals balance due
     Update views to it done and show Change amount
      */
    @Override
    public void updateBalanceZeroText() {
        tvBalanceOrChange.setText("Balance");
        tvBalanceOrChange.setTextColor(Color.parseColor("#4ac21b"));
        tvChange.setText(decimalFormat.format(0));
        tvChange.setTextColor(Color.parseColor("#4ac21b"));
        tvPay.setText("Done");
    }

    /**
     it method will work when payment amount is zero
     Close payment fragment view
     */
    @Override
    public void updateCloseText() {
        tvBalanceOrChange.setText("Balance");
        tvBalanceOrChange.setTextColor(Color.parseColor("#4ac21b"));
        tvChange.setText(decimalFormat.format(0));
        tvChange.setTextColor(Color.parseColor("#4ac21b"));
        tvPay.setText("Close");
    }

    /**
     Close payment fragment (Self)
     */
    @Override
    public void closeSelf() {
        ((MainPosPageActivity) getActivity()).hidePaymentFragment();
    }

    /**
     clear payment amount when click pay button
     sending event "OrderPayed" (ever payment operation will send)
     */
    @Override
    public void onPayedPartition() {
        etPaymentAmount.setText("");
        mainPageConnection.onPayedPartition();
    }

    @Override
    public void closeOrder(Order order, List<PayedPartitions> payedPartitions, Debt debt) {
        closeSelf();
        mainPageConnection.onCloseOrder(order,payedPartitions,debt);

    }

    @Override
    public void updateCustomer(Customer customer) {
        mainPageConnection.updateCustomer(customer);
    }

    @Override
    public void showDebtDialog() {
        ivDebt.setImageResource(R.drawable.borrow);
        tvDebt.setText("Borrow");
    }

    @Override
    public void hideDebtDialog() {
        ivDebt.setImageResource(R.drawable.cancel_customer);
        tvDebt.setText("Cancel Borrow");
    }

    @Override
    public void openTipsDialog(TipsDialog.OnClickListener listener,double change) {
        TipsDialog tipsDialog = new TipsDialog(getContext(),listener,change, databaseManager);
        tipsDialog.show();
    }

    @Override
    public void enableTipsButton() {
        ivTips.setImageResource(R.drawable.tips);
        tvTips.setText("Tips");
    }

    @Override
    public void disableTipsButton() {
        ivTips.setImageResource(R.drawable.cancel_customer);
        tvTips.setText("Cancel Tips");
    }

    @Override
    public void updateOrderListDetialsPanel() {
        mainPageConnection.onPayedPartition();
    }

    @Override
    public void onNewOrder() {
        presenter.onNewOrder();
    }

    @Override
    public void sendDataToPaymentFragmentWhenEdit(Order order, List<PayedPartitions> payedPartitions, Debt debt) {
        presenter.sendDataToPaymentFragmentWhenEdit(order,payedPartitions,debt);
    }

    @Override
    public void onHoldOrderClicked() {
        presenter.onHoldOrderClicked();
    }

    @Override
    public void onHoldOrderSendingData(Order order, List<PayedPartitions> payedPartitions, Debt debt) {
        mainPageConnection.onHoldOrderSendingData(order,payedPartitions,debt);
    }

    @Override
    public void openWarningDialog(String text) {
        UIUtils.showAlert(getContext(), getContext().getString(R.string.ok), getString(R.string.warning), text, () -> {});
    }


    @Override
    public void setCustomer(Customer customer) {
        presenter.setCustomer(customer);
    }

    /**
     setting payment to Payment amount text view
     it used when payment amount set from pragmatically
     */
    @Override
    public void updatePaymentText(double payment) {
        etPaymentAmount.setText(dfe.format(payment));
        etPaymentAmount.setSelection(etPaymentAmount.getText().length());
    }



    @BindView(R.id.btnDot)
    MpNumPad btnDot;
    @BindView(R.id.btnDoubleZero)
    MpNumPad btnDoubleZero;
    @BindView(R.id.btnZero)
    MpNumPad btnZero;
    @BindView(R.id.btnOne)
    MpNumPad btnOne;
    @BindView(R.id.btnTwo)
    MpNumPad btnTwo;
    @BindView(R.id.btnThree)
    MpNumPad btnThree;
    @BindView(R.id.btnFour)
    MpNumPad btnFour;
    @BindView(R.id.btnFive)
    MpNumPad btnFive;
    @BindView(R.id.btnSix)
    MpNumPad btnSix;
    @BindView(R.id.btnSeven)
    MpNumPad btnSeven;
    @BindView(R.id.btnEight)
    MpNumPad btnEight;
    @BindView(R.id.btnNine)
    MpNumPad btnNine;
    @BindView(R.id.btnBackSpace)
    LinearLayout btnBackSpace;
    @BindView(R.id.btnFirstOptional)
    MpNumPadSecond btnFirstOptional;
    @BindView(R.id.btnSecondOptional)
    MpNumPadSecond btnSecondOptional;
    @BindView(R.id.btnAllInOne)
    MpNumPadSecond btnAllInOne;


    /**
     initialization keypad buttons
     */
    private void initButtons(){
        //getting optional buttons from Shared Preference
        //It is used for flexible setting optional buttons from settings activity
        btnFirstOptional.setCurrency(databaseManager.getMainCurrency().getAbbr());
        btnFirstOptional.setValue(decimalFormat.format(preferencesHelper.getFirstOptionalPaymentButton()));
        btnSecondOptional.setCurrency(databaseManager.getMainCurrency().getAbbr());
        btnSecondOptional.setValue(decimalFormat.format(preferencesHelper.getSecondOptionalPaymentButton()));
        tvBalanceDueCurrency.setText(databaseManager.getMainCurrency().getAbbr());
        tvChangeCurrency.setText(databaseManager.getMainCurrency().getAbbr());
        tvPaymentCurrency.setText(databaseManager.getMainCurrency().getAbbr());
        btnFirstOptional.setOnClickListener(view -> {
            //this part of code used for clearing text when some text part selected
            if(etPaymentAmount.getSelectionStart() != etPaymentAmount.getSelectionEnd()){
                etPaymentAmount.getText().clear();
            }
            presenter.pressFirstOptional();
        });
        btnSecondOptional.setOnClickListener(view -> {
            if(etPaymentAmount.getSelectionStart() != etPaymentAmount.getSelectionEnd()){
                etPaymentAmount.getText().clear();
            }
            presenter.pressSecondOptional();
        });

        //this button used for input all balance due value in one
        btnAllInOne.setOnClickListener(view -> {
            presenter.pressAllAmount();
        });

        //sending input key to method
        btnOne.setOnClickListener(view -> {
            pressedKey("1");
        });
        btnTwo.setOnClickListener(view -> {
            pressedKey("2");
        });
        btnThree.setOnClickListener(view -> {
            pressedKey("3");
        });
        btnFour.setOnClickListener(view -> {
            pressedKey("4");
        });
        btnFive.setOnClickListener(view -> {
            pressedKey("5");
        });
        btnSix.setOnClickListener(view -> {
            pressedKey("6");
        });
        btnSeven.setOnClickListener(view -> {
            pressedKey("7");
        });
        btnEight.setOnClickListener(view -> {
            pressedKey("8");
        });
        btnNine.setOnClickListener(view -> {
            pressedKey("9");
        });
        btnZero.setOnClickListener(view -> {
            pressedKey("0");
        });
        btnDoubleZero.setOnClickListener(view -> {
            pressedKey("00");
        });
        btnDot.setOnClickListener(view -> {
            pressedKey(decimalFormat.getDecimalFormatSymbols().getDecimalSeparator()+"");
        });

        btnBackSpace.setOnLongClickListener(view -> {
                etPaymentAmount.getText().clear();
                return true;
        });
        btnBackSpace.setOnClickListener(view -> {
            if(etPaymentAmount.getSelectionStart() != etPaymentAmount.getSelectionEnd()){
                etPaymentAmount.getText().clear();
            }else {
                etPaymentAmount.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            }
        });

    }

    /**
     checking for not input when comma have one more
     */
    private void pressedKey(String key){
        if(key.equals(decimalFormat.getDecimalFormatSymbols().getDecimalSeparator()+"")){
                if(etPaymentAmount.getText().toString().contains(decimalFormat.getDecimalFormatSymbols().getDecimalSeparator()+""))
                    return;
        }
        if(etPaymentAmount.getSelectionStart() != etPaymentAmount.getSelectionEnd()){
                etPaymentAmount.getText().clear();
            }
        etPaymentAmount.getText().insert(etPaymentAmount.getSelectionStart(),key);
        if(key.equals(decimalFormat.getDecimalFormatSymbols().getDecimalSeparator()+"")) {
            etPaymentAmount.setSelection(etPaymentAmount.getText().length());
        }

        }

    /**
     de initialization view from mainpageConnection
     */
    @Override
    public void onDestroy() {
        mainPageConnection.setPaymentView(null);
        super.onDestroy();
    }

}
