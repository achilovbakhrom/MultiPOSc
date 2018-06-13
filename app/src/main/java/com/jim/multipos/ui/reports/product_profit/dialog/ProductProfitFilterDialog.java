package com.jim.multipos.ui.reports.product_profit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductProfitFilterDialog extends Dialog {

    @BindView(R.id.rgGroupBy)
    RadioGroup rgGroupBy;

    @BindView(R.id.rbProducts)
    RadioButton rbProducts;
    @BindView(R.id.rbCategory)
    RadioButton rbCategory;
    @BindView(R.id.rbSubCategory)
    RadioButton rbSubCategory;
    @BindView(R.id.rbProductClass)
    RadioButton rbProductClass;
    @BindView(R.id.btnCancel)
    MpButton btnCancel;
    @BindView(R.id.btnApply)
    MpButton btnApply;

    private ProductProfitListner orderHistoryFilterListner;

    public interface ProductProfitListner{
        void onApply(int config);
    }
    public ProductProfitFilterDialog(@NonNull Context context, int filterConfig, ProductProfitListner orderHistoryFilterListner) {
        super(context);
        this.orderHistoryFilterListner = orderHistoryFilterListner;
        View dialogView = getLayoutInflater().inflate(R.layout.product_profit_filter_dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.y = CommonUtils.dpToPx(160);
        wlp.x = CommonUtils.dpToPx(10);
        wlp.width = CommonUtils.dpToPx(300);
        wlp.height = CommonUtils.dpToPx(330);
        wlp.gravity = Gravity.TOP | Gravity.LEFT;
        window.setAttributes(wlp);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);

        if(filterConfig == 0) rbProducts.setChecked(true);
        else if(filterConfig == 1) rbCategory.setChecked(true);
        else if(filterConfig == 2) rbSubCategory.setChecked(true);
        else if(filterConfig == 3) rbProductClass.setChecked(true);




        btnApply.setOnClickListener((view -> {
            int choised = 0;
            switch (rgGroupBy.getCheckedRadioButtonId()){
                case R.id.rbProducts:
                    choised = 0;
                    break;
                case R.id.rbCategory:
                    choised = 1;
                    break;
                case R.id.rbSubCategory:
                    choised = 2;
                    break;
                case R.id.rbProductClass:
                    choised = 3;
                    break;
            }
            orderHistoryFilterListner.onApply(choised);
            dismiss();
        }));
        btnCancel.setOnClickListener((view -> {
            dismiss();
        }));
    }
}
