package com.jim.multipos.ui.cash_management.view;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;

import com.jim.mpviews.MpButtonWithIcon;
import com.jim.mpviews.MpList;
import com.jim.mpviews.MpNumPad;
import com.jim.mpviews.model.PaymentTypeWithService;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.PaymentType;
import com.jim.multipos.data.db.model.till.Till;
import com.jim.multipos.ui.cash_management.connection.CashManagementConnection;
import com.jim.multipos.ui.cash_management.dialog.CashOperationDialog;
import com.jim.multipos.ui.cash_management.dialog.CloseTillDialog;
import com.jim.multipos.ui.cash_management.dialog.OpenTillDialog;
import com.jim.multipos.ui.cash_management.presenter.CashOperationsPresenter;
import com.jim.multipos.utils.UIUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Sirojiddin on 11.01.2018.
 */

public class CashOperationsFragment extends BaseFragment implements CashOperationsView {

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
    MpNumPad btnBackSpace;
    @BindView(R.id.btnClear)
    MpNumPad btnClear;
    @BindView(R.id.etPaymentAmount)
    EditText etPaymentAmount;
    @BindView(R.id.btnPayIn)
    MpButtonWithIcon btnPayIn;
    @BindView(R.id.btnPayOut)
    MpButtonWithIcon btnPayOut;
    @BindView(R.id.btnBankDrop)
    MpButtonWithIcon btnBankDrop;
    @BindView(R.id.btnCloseTill)
    TextView btnCloseTill;
    @BindView(R.id.mpPaymentTypesList)
    MpList mpPaymentTypesList;

    @Inject
    CashOperationsPresenter presenter;
    @Inject
    DecimalFormat decimalFormat;
    @Inject
    CashManagementConnection connection;
    @Inject
    DatabaseManager databaseManager;
    private int tillStatus;

    @Override
    protected int getLayout() {
        return R.layout.cash_operations_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.initData();
        mpPaymentTypesList.setPaymentViewWidth(180);
        btnClear.getNumPadTextView().setTextColor(ContextCompat.getColor(getContext(), R.color.colorTintGrey));
        etPaymentAmount.setRawInputType(InputType.TYPE_CLASS_TEXT);
        etPaymentAmount.setTextIsSelectable(true);
        initButtons();
        btnPayIn.setOnClickListener(view -> {
            try {
                presenter.doPayIn(decimalFormat.parse(etPaymentAmount.getText().toString()).doubleValue());
                etPaymentAmount.setText("");
            } catch (ParseException e) {
                e.printStackTrace();
                etPaymentAmount.setError(getContext().getString(R.string.ammount_is_empty));
            }
        });
        btnPayOut.setOnClickListener(view -> {
            try {
                presenter.doPayOut(decimalFormat.parse(etPaymentAmount.getText().toString()).doubleValue());
                etPaymentAmount.setText("");
            } catch (ParseException e) {
                e.printStackTrace();
                etPaymentAmount.setError(getContext().getString(R.string.ammount_is_empty));
            }
        });
        btnBankDrop.setOnClickListener(view -> {
            try {
                presenter.doBankDrop(decimalFormat.parse(etPaymentAmount.getText().toString()).doubleValue());
                etPaymentAmount.setText("");
            } catch (ParseException e) {
                e.printStackTrace();
                etPaymentAmount.setError(getContext().getString(R.string.ammount_is_empty));
            }
        });
        btnCloseTill.setOnClickListener(view -> {
            if (tillStatus == Till.OPEN) {
                presenter.showCloseTillDialog();
            } else {
                presenter.showOpenTillDialog();
            }
        });
    }

    @Override
    public void initPaymentTypes(ArrayList<PaymentTypeWithService> paymentTypeWithServices) {
        mpPaymentTypesList.setPayments(paymentTypeWithServices);
        mpPaymentTypesList.setOnPaymentClickListner(position -> presenter.changePayment(position));
    }

    @Override
    public void showWarningDialog(String warningText) {
        UIUtils.showAlert(getContext(), getString(R.string.ok), getString(R.string.warning), warningText, () -> {
        });
    }

    @Override
    public void updateDetails() {
        connection.updateCashDetails();
    }

    @Override
    public void openCashOperationDialog(Till till, PaymentType paymentType, int type, double amount) {
        CashOperationDialog dialog = new CashOperationDialog(getContext(), databaseManager, till, paymentType, type, amount,
                (amount1, operationType, description) -> presenter.executeOperation(amount1, operationType, description));
        dialog.show();
    }

    @Override
    public void changeAccount(Long accountId) {
        connection.changeAccount(accountId);
    }

    @Override
    public void setBankDropVisibility(int visibility) {
        btnBankDrop.setVisibility(visibility);
    }

    @Override
    public void getTillStatus(int status) {
        tillStatus = status;
        if (status == Till.OPEN)
            btnCloseTill.setText(getContext().getString(R.string.close_till));
        else btnCloseTill.setText(getContext().getString(R.string.open_till));
    }

    @Override
    public void showCloseTillDialog() {
        CloseTillDialog closeTillDialog = new CloseTillDialog();
        closeTillDialog.show(getFragmentManager(), "CloseTillDialog");
    }

    @Override
    public void showOpenTillDialog() {
        OpenTillDialog openTillDialog = new OpenTillDialog();
        openTillDialog.show(getFragmentManager(), "OpenTillDialog");
    }

    private void initButtons() {
        btnOne.setOnClickListener(view -> pressedKey("1"));
        btnTwo.setOnClickListener(view -> pressedKey("2"));
        btnThree.setOnClickListener(view -> pressedKey("3"));
        btnFour.setOnClickListener(view -> pressedKey("4"));
        btnFive.setOnClickListener(view -> pressedKey("5"));
        btnSix.setOnClickListener(view -> pressedKey("6"));
        btnSeven.setOnClickListener(view -> pressedKey("7"));
        btnEight.setOnClickListener(view -> pressedKey("8"));
        btnNine.setOnClickListener(view -> pressedKey("9"));
        btnZero.setOnClickListener(view -> pressedKey("0"));
        btnDoubleZero.setOnClickListener(view -> pressedKey("00"));
        btnDot.setOnClickListener(view -> pressedKey(","));

        btnBackSpace.setOnClickListener(view -> {
            StringBuilder builder = new StringBuilder();
            builder.append(etPaymentAmount.getText().toString());
            int selectionStart = etPaymentAmount.getSelectionStart();
            if (selectionStart == 0) return;
            builder.deleteCharAt(selectionStart - 1);
            etPaymentAmount.setText(builder.toString());
            etPaymentAmount.setSelection(selectionStart - 1);
        });

        btnClear.setOnClickListener(view -> etPaymentAmount.setText(""));
    }

    private void pressedKey(String key) {
        if (key.equals(",")) {
            if (etPaymentAmount.getText().toString().contains(","))
                return;
        }
        if (etPaymentAmount.getSelectionStart() != etPaymentAmount.getSelectionEnd()) {
            etPaymentAmount.getText().clear();
        }
        etPaymentAmount.getText().insert(etPaymentAmount.getSelectionStart(), key);
    }
}
