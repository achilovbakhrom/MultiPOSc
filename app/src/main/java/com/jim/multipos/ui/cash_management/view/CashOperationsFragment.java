package com.jim.multipos.ui.cash_management.view;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.jim.mpviews.MpButtonWithIcon;
import com.jim.mpviews.MpEditText;
import com.jim.mpviews.MpNumPad;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.cash_management.dialog.CloseTillDialog;

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
    MpButtonWithIcon btnCloseTill;

    @Override
    protected int getLayout() {
        return R.layout.cash_operations_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        btnClear.getNumPadTextView().setTextColor(ContextCompat.getColor(getContext(), R.color.colorTintGrey));
        etPaymentAmount.setRawInputType(InputType.TYPE_CLASS_TEXT);
        etPaymentAmount.setTextIsSelectable(true);
        initButtons();
        btnPayIn.setOnClickListener(view -> {

        });
        btnPayOut.setOnClickListener(view -> {

        });
        btnBankDrop.setOnClickListener(view -> {

        });
        btnCloseTill.setOnClickListener(view -> {
            CloseTillDialog closeTillDialog = new CloseTillDialog();
            closeTillDialog.show(getFragmentManager(), "CloseTillDialog");
        });
    }

    private void initButtons() {
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
            pressedKey(",");
        });

        btnBackSpace.setOnClickListener(view -> {
            StringBuilder builder = new StringBuilder();
            builder.append(etPaymentAmount.getText().toString());
            int selectionStart = etPaymentAmount.getSelectionStart();
            if (selectionStart == 0) return;
            builder.deleteCharAt(selectionStart - 1);
            etPaymentAmount.setText(builder.toString());
            etPaymentAmount.setSelection(selectionStart - 1);
        });

        btnClear.setOnClickListener(view -> {
            etPaymentAmount.setText("");
        });
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
