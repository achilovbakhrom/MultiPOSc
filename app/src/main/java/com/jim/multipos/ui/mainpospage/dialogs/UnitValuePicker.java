package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.widget.TextViewCompat;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jim.mpviews.MpNumPad;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.unit.Unit;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.utils.DecimalUtils;
import com.jim.multipos.utils.TextWatcherOnTextChange;
import com.jim.multipos.utils.WarningDialog;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.widget.TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM;

/**
 * Created by developer on 08.01.2018.
 */

public class UnitValuePicker extends Dialog {
    View dialogView;
    @BindView(R.id.etCost)
    EditText etCost;
    @BindView(R.id.etWeight)
    EditText etWeight;
    @BindView(R.id.tvPriceProduct)
    EditText tvPriceProduct;
    @BindView(R.id.llWeight)
    LinearLayout llWeight;
    @BindView(R.id.llCost)
    LinearLayout llCost;
    @BindView(R.id.tvUnitCategory)
    TextView tvUnitCategory;

    @BindView(R.id.tvFirstSubUnit)
    TextView tvFirstSubUnit;
    @BindView(R.id.tvSecondSubUnit)
    TextView tvSecondSubUnit;
    @BindView(R.id.tvThreeSubUnit)
    TextView tvThreeSubUnit;
    @BindView(R.id.tvFourSubUnit)
    TextView tvFourSubUnit;
    @BindView(R.id.tvFiveSubUnit)
    TextView tvFiveSubUnit;
    @BindView(R.id.etAbr)
    EditText etAbr;
    @BindView(R.id.tvZeroUnitPrice)
    TextView tvZeroUnitPrice;
    @BindView(R.id.tvWeightDisp)
    TextView tvWeightDisp;

    boolean isFocusedInWeight = true;
    DecimalFormat decimalFormatWithoutProbel;
    List<Unit> units;
    int currentUnitPosition = -1;
    private Context context;
    private Product product;
    private CallbackUnitPicker callbackUnitPicker;
    private DecimalFormat decimalFormat;

    double weight = 0;
    double cost = 0;

    public interface CallbackUnitPicker{
        void onWeight(double weight);
    }


    public UnitValuePicker(@NonNull Context context, Product product,CallbackUnitPicker callbackUnitPicker, double weightOld) {
        super(context);
        this.context = context;
        this.product = product;
        this.callbackUnitPicker = callbackUnitPicker;
        this.decimalFormat = decimalFormat;
        dialogView = getLayoutInflater().inflate(R.layout.unit_value_picker_dialog, null);
        ButterKnife.bind(this, dialogView);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

        TextViewCompat.setAutoSizeTextTypeWithDefaults(etCost,AUTO_SIZE_TEXT_TYPE_UNIFORM);
        decimalFormatWithoutProbel = BaseAppModule.getFormatterWithoutGroupingPattern("#.###");

        units = product.getMainUnit().getUnitCategory().getUnits();
        for (int i = 0; i < units.size(); i++) {
            if(product.getMainUnit().getId().equals(units.get(i).getId())){
                currentUnitPosition = i;
                break;
            }
        }
        weight = weightOld;
        cost = DecimalUtils.multiply(product.getPrice(),weightOld);

        etCost.setRawInputType(InputType.TYPE_CLASS_TEXT);
        etCost.setTextIsSelectable(true);
        etWeight.setRawInputType(InputType.TYPE_CLASS_TEXT);
        etWeight.setTextIsSelectable(true);
        etWeight.requestFocus();


        decimalFormat =  BaseAppModule.getFormatterGroupingPattern("#,###.###");

        etCost.setText(decimalFormat.format(cost));
        etWeight.setText(decimalFormatWithoutProbel.format(weight));
        etWeight.selectAll();
        etWeight.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                isFocusedInWeight = true;
                etWeight.setText(decimalFormatWithoutProbel.format(DecimalUtils.roundDouble(weight)));
                etCost.setText(decimalFormat.format(DecimalUtils.roundDouble(cost)));
                etWeight.selectAll();
                llWeight.setBackgroundResource(R.drawable.rectangle_solid_blue_rounded);
                llCost.setBackgroundResource(R.drawable.rectangle_solid_white_rounded);
            }
        });
        etCost.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                isFocusedInWeight = false;
                etCost.setText(decimalFormatWithoutProbel.format(DecimalUtils.roundDouble(cost)));
                etWeight.setText(decimalFormat.format(DecimalUtils.roundDouble(weight)));
                etCost.selectAll();
                llCost.setBackgroundResource(R.drawable.rectangle_solid_blue_rounded);
                llWeight.setBackgroundResource(R.drawable.rectangle_solid_white_rounded);
            }
        });

        llCost.setOnClickListener(view -> {
            etCost.requestFocus();
        });
        llWeight.setOnClickListener(view -> {
            etWeight.requestFocus();
        });

        etCost.addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!isFocusedInWeight){
                    double costTemp = 0;
                    try {
                        costTemp = decimalFormat.parse(etCost.getText().toString()).doubleValue();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    cost = costTemp;
                    weight = DecimalUtils.roundDouble((cost * product.getMainUnit().getFactorRoot())/(product.getPrice()*units.get(currentUnitPosition).getFactorRoot())) ;
                    etWeight.setText(decimalFormat.format(weight));
                }
            }
        });
        etWeight.addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(isFocusedInWeight){
                    double weightTemp = 0;
                    try {
                        weightTemp = decimalFormat.parse(etWeight.getText().toString()).doubleValue();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    weight = weightTemp;
                    cost = DecimalUtils.roundDouble((product.getPrice()*units.get(currentUnitPosition).getFactorRoot())/product.getMainUnit().getFactorRoot()*weight);
                    etCost.setText(decimalFormat.format(cost));
                }
            }
        });

        tvUnitCategory.setText(product.getMainUnit().getUnitCategory().getName());
        tvWeightDisp.setText(product.getMainUnit().getUnitCategory().getName());

        etAbr.setText(product.getMainUnit().getAbbr());

        tvZeroUnitPrice.setText(context.getString(R.string.price_1)+product.getMainUnit().getAbbr());
        tvPriceProduct.setText(decimalFormat.format(DecimalUtils.roundDouble(product.getPrice())));
        initSubUnits();
        updateActiveUnit();
        initButtons();
    }
    private void initSubUnits(){
        if(units.size()>0 && units.get(0)!=null){
            tvFirstSubUnit.setText(units.get(0).getAbbr());
            tvFirstSubUnit.setOnClickListener(view -> {
                currentUnitPosition = 0;
                updateAfterChangeUnit();
                updateActiveUnit();
            });
        }
        else tvFirstSubUnit.setVisibility(View.GONE);

        if(units.size()>1 && units.get(1)!=null) {
            tvSecondSubUnit.setText(units.get(1).getAbbr());
            tvSecondSubUnit.setOnClickListener(view -> {
                currentUnitPosition = 1;
                updateAfterChangeUnit();
                updateActiveUnit();
            });

        }
        else tvSecondSubUnit.setVisibility(View.GONE);

        if(units.size()>2 && units.get(2)!=null){
            tvThreeSubUnit.setText(units.get(2).getAbbr());
            tvThreeSubUnit.setOnClickListener(view -> {
                currentUnitPosition = 2;
                updateAfterChangeUnit();
                updateActiveUnit();
            });
        }
        else  tvThreeSubUnit.setVisibility(View.GONE);

        if(units.size()>3 && units.get(3)!=null) {
            tvFourSubUnit.setText(units.get(3).getAbbr());
            tvFourSubUnit.setOnClickListener(view -> {
                currentUnitPosition = 3;
                updateAfterChangeUnit();
                updateActiveUnit();
            });

        }
        else  tvFourSubUnit.setVisibility(View.GONE);

        if(units.size()>4 && units.get(4)!=null){
            tvFiveSubUnit.setText(units.get(4).getAbbr());
            tvFiveSubUnit.setOnClickListener(view -> {
                currentUnitPosition = 4;
                updateAfterChangeUnit();
                updateActiveUnit();
            });
        }
        else  tvFiveSubUnit.setVisibility(View.GONE);


    }
    private void updateAfterChangeUnit(){
        if(isFocusedInWeight){
            cost = DecimalUtils.roundDouble(((product.getPrice()*units.get(currentUnitPosition).getFactorRoot())/product.getMainUnit().getFactorRoot())*weight);
            etCost.setText(decimalFormat.format(cost));
        }else {
            weight = DecimalUtils.roundDouble((cost * product.getMainUnit().getFactorRoot()) / (product.getPrice()*units.get(currentUnitPosition).getFactorRoot()));
            etWeight.setText(decimalFormat.format(weight));
        }
    }
    private void updateActiveUnit(){
        tvFirstSubUnit.setBackgroundResource(R.drawable.unit_frame_deactive);
        tvFirstSubUnit.setTextColor(Color.parseColor("#8ac3e6"));

        tvSecondSubUnit.setBackgroundResource(R.drawable.unit_frame_deactive);
        tvSecondSubUnit.setTextColor(Color.parseColor("#8ac3e6"));

        tvThreeSubUnit.setBackgroundResource(R.drawable.unit_frame_deactive);
        tvThreeSubUnit.setTextColor(Color.parseColor("#8ac3e6"));

        tvFourSubUnit.setBackgroundResource(R.drawable.unit_frame_deactive);
        tvFourSubUnit.setTextColor(Color.parseColor("#8ac3e6"));

        tvFiveSubUnit.setBackgroundResource(R.drawable.unit_frame_deactive);
        tvFiveSubUnit.setTextColor(Color.parseColor("#8ac3e6"));


        switch (currentUnitPosition){
            case 0:
                tvFirstSubUnit.setBackgroundResource(R.drawable.unit_frame_active);
                tvFirstSubUnit.setTextColor(Color.parseColor("#ffffff"));
                break;
            case 1:
                tvSecondSubUnit.setBackgroundResource(R.drawable.unit_frame_active);
                tvSecondSubUnit.setTextColor(Color.parseColor("#ffffff"));
                break;
            case 2:
                tvThreeSubUnit.setBackgroundResource(R.drawable.unit_frame_active);
                tvThreeSubUnit.setTextColor(Color.parseColor("#ffffff"));
                break;
            case 3:
                tvFourSubUnit.setBackgroundResource(R.drawable.unit_frame_active);
                tvFourSubUnit.setTextColor(Color.parseColor("#ffffff"));
                break;
            case 4:
                tvFiveSubUnit.setBackgroundResource(R.drawable.unit_frame_active);
                tvFiveSubUnit.setTextColor(Color.parseColor("#ffffff"));
                break;
        }

        etAbr.setText(units.get(currentUnitPosition).getAbbr());


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
    @BindView(R.id.btnClear)
    LinearLayout btnClear;
    @BindView(R.id.btnBack)
    LinearLayout btnBack;
    @BindView(R.id.btnOk)
    LinearLayout btnOk;

    private void initButtons(){
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
            pressedKey(".");
        });


        btnBackSpace.setOnClickListener(view -> {
            EditText editText = null;
            if(isFocusedInWeight)
                editText = etWeight;
            else editText = etCost;
            StringBuilder builder = new StringBuilder();
            builder.append(editText.getText().toString());
            int selectionStart = editText.getSelectionStart();
            if(selectionStart==0) return;
            builder.deleteCharAt(selectionStart -1);
            editText.setText(builder.toString());
            editText.setSelection(selectionStart-1);
        });
        btnClear.setOnClickListener(view -> {
            EditText editText = null;
            if(isFocusedInWeight)
                editText = etWeight;
            else editText = etCost;
            editText.getText().clear();
        });
        btnBack.setOnClickListener(view -> {
            dismiss();
        });
        Order order = new Order();
        btnOk.setOnClickListener(view -> {
            double weight = DecimalUtils.roundDouble((this.weight*units.get(currentUnitPosition).getFactorRoot())/ product.getMainUnit().getFactorRoot());

            if(weight<0.001){
                WarningDialog warningDialog2 = new WarningDialog(context);
                warningDialog2.onlyText(true);
                warningDialog2.setWarningMessage(context.getString(R.string.its_very_small_valueof)+product.getMainUnit().getUnitCategory().getName());
                warningDialog2.setOnYesClickListener(view1 -> {
                    warningDialog2.dismiss();
                });
                warningDialog2.setPositiveButtonText(context.getString(R.string.yes));
                warningDialog2.show();
            }else if(DecimalUtils.multiply(product.getPrice() , weight) < 0.01){
                WarningDialog warningDialog2 = new WarningDialog(context);
                warningDialog2.onlyText(true);
                warningDialog2.setWarningMessage(context.getString(R.string.its_very_small_cost_for_calculating));
                warningDialog2.setOnYesClickListener(view1 -> {
                    warningDialog2.dismiss();
                });
                warningDialog2.setPositiveButtonText(context.getString(R.string.yes));
                warningDialog2.show();
            }else {
                callbackUnitPicker.onWeight(weight);
                dismiss();
            }
        });
    }

    private void pressedKey(String key){
        if(key.equals(".")){
            if(isFocusedInWeight){
                if(etWeight.getText().toString().contains("."))
                    return;
            }else {
                if(etCost.getText().toString().contains("."))
                    return;
            }
        }
        if(isFocusedInWeight){
            if(etWeight.getSelectionStart() != etWeight.getSelectionEnd()){
                etWeight.getText().clear();
            }
            etWeight.getText().insert(etWeight.getSelectionStart(),key);
        }else{
            if(etCost.getSelectionStart() != etCost.getSelectionEnd()){
                etCost.getText().clear();
            }
            etCost.getText().insert(etCost.getSelectionStart(),key);
        }
    }
}
