package com.jim.multipos.ui.mainpospage.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.unit.UnitCategory;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.consignment.dialogs.StockPositionsDialog;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.mainpospage.connection.MainPageConnection;
import com.jim.multipos.ui.mainpospage.dialogs.DiscountDialog;
import com.jim.multipos.ui.mainpospage.dialogs.ServiceFeeDialog;
import com.jim.multipos.ui.mainpospage.dialogs.SetQuantityDialog;
import com.jim.multipos.ui.mainpospage.dialogs.UnitValuePicker;
import com.jim.multipos.ui.mainpospage.model.OrderProductItem;
import com.jim.multipos.ui.mainpospage.presenter.ProductInfoPresenter;
import com.jim.multipos.utils.GlideApp;
import com.jim.multipos.utils.TextWatcherOnTextChange;
import com.jim.multipos.utils.WarningDialog;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by developer on 24.08.2017.
 */

public class ProductInfoFragment extends BaseFragment implements ProductInfoView {
    @Inject
    ProductInfoPresenter presenter;
    DecimalFormat decimalFormat;
    DecimalFormat decimalFormatLocal;
    @BindView(R.id.ivProductImage)
    ImageView ivProductImage;
    @BindView(R.id.tvProductName)
    TextView tvProductName;

    @BindView(R.id.tvQuantity)
    TextView tvQuantity;
    @BindView(R.id.ivMinus)
    ImageView ivMinus;
    @BindView(R.id.ivPlus)
    ImageView ivPlus;
    @BindView(R.id.tvOrderQuantity)
    TextView tvOrderQuantity;
    @BindView(R.id.btnSetQuantity)
    MpButton btnSetQuantity;

    @BindView(R.id.btnServiceFee)
    MpButton btnServiceFee;
    @BindView(R.id.btnDiscountItem)
    MpButton btnDiscountItem;
    @BindView(R.id.btnPosition)
    MpButton btnPosition;
    @BindView(R.id.btnClose)
    MpButton btnClose;
    @BindView(R.id.etSpecialRequest)
    EditText etSpecialRequest;
    @BindView(R.id.ivAlert)
    ImageView ivAlert;
    @BindView(R.id.btnRemove)
    MpButton btnRemove;
    @BindView(R.id.tvUnitName)
    TextView tvUnitName;


    @Inject
    DatabaseManager databaseManager;

    @Inject
    MainPageConnection mainPageConnection;
    @Inject
    PreferencesHelper preferencesHelper;
    @Override
    protected int getLayout() {
        return R.layout.product_info_fragment;
    }
    @Override
    protected void init(Bundle savedInstanceState) {
        mainPageConnection.setProductInfoView(this);
        mainPageConnection.giveToProductInfoFragmentProductItem();
        DecimalFormat formatter;
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(6);
        formatter = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);
        decimalFormatLocal =  formatter;


        DecimalFormat formatter2;
        NumberFormat numberFormat2 = NumberFormat.getNumberInstance();
        numberFormat2.setMaximumFractionDigits(3);
        formatter2 = (DecimalFormat) numberFormat2;
        DecimalFormatSymbols symbols2 = formatter2.getDecimalFormatSymbols();
        symbols2.setGroupingSeparator(' ');
        formatter2.setDecimalFormatSymbols(symbols2);
        decimalFormat =  formatter2;



        ivMinus.setOnClickListener(view -> {
            mainPageConnection.minusProductCount();
        });
        ivPlus.setOnClickListener(view -> {
            mainPageConnection.plusProductCount();
        });
        btnSetQuantity.setOnClickListener(view -> {
            if(orderProductItem.getOrderProduct().getProduct().getMainUnit().getUnitCategory().getUnitType() == UnitCategory.PIECE) {
                SetQuantityDialog dialog = new SetQuantityDialog(getContext(), value -> {
                    mainPageConnection.setCount(value);
                    mainPageConnection.giveToProductInfoFragmentProductItem();
                });
                dialog.show();
            }else {
                UnitValuePicker unitValuePicker = new UnitValuePicker(getContext(), orderProductItem.getOrderProduct().getProduct(), weight -> {
                    mainPageConnection.addProductWithWeightToListEdit(weight);
                    mainPageConnection.giveToProductInfoFragmentProductItem();
                },orderProductItem.getOutcomeProduct().getSumCountValue());
                unitValuePicker.show();
            }


        });
        btnRemove.setOnClickListener(view -> {
            WarningDialog warningDialog = new WarningDialog(getActivity());
            warningDialog.setWarningMessage(getResources().getString(R.string.are_you_sure_delete_product_from_order));
            warningDialog.setOnYesClickListener(view1 -> {
                warningDialog.dismiss();
                mainPageConnection.removeOrderProducts();
                ((MainPosPageActivity)getActivity()).hideProductInfoFragment();
            });
            warningDialog.setOnNoClickListener(view1 -> {
                warningDialog.dismiss();
            });
            warningDialog.setPositiveButtonText(getResources().getString(R.string.yes));
            warningDialog.setNegativeButtonText(getResources().getString(R.string.cancel));
            warningDialog.show();

        });
        btnClose.setOnClickListener(view -> {
            ((MainPosPageActivity)getActivity()).hideProductInfoFragment();
        });
        btnServiceFee.setOnClickListener(view -> {
            if(orderProductItem.getServiceFee() !=null){
                mainPageConnection.setServiceFeeProduct(null);
                mainPageConnection.giveToProductInfoFragmentProductItem();
            }else {
                ServiceFeeDialog.CallbackServiceFeeDialog callbackServiceFeeDialog = new ServiceFeeDialog.CallbackServiceFeeDialog() {
                    @Override
                    public void choiseStaticServiceFee(ServiceFee serviceFee) {
                        mainPageConnection.setServiceFeeProduct(serviceFee);
                        mainPageConnection.giveToProductInfoFragmentProductItem();
                    }

                    @Override
                    public void choiseManualServiceFee(ServiceFee serviceFee) {
                        mainPageConnection.setServiceFeeProduct(serviceFee);
                        mainPageConnection.giveToProductInfoFragmentProductItem();
                    }
                };
                ServiceFeeDialog serviceFeeDialog = new ServiceFeeDialog(getContext(),databaseManager,callbackServiceFeeDialog,orderProductItem.getOrderProduct().getPrice(),ServiceFee.ITEM, decimalFormat,preferencesHelper);
                serviceFeeDialog.show();
            }
        });
        btnDiscountItem.setOnClickListener(view -> {
            if(orderProductItem.getDiscount()!=null){
                mainPageConnection.setDiscountToProduct(null);
                mainPageConnection.giveToProductInfoFragmentProductItem();
            }else {
                DiscountDialog.CallbackDiscountDialog callbackDiscountDialog = new DiscountDialog.CallbackDiscountDialog() {
                    @Override
                    public void choiseStaticDiscount(Discount discount) {
                        mainPageConnection.setDiscountToProduct(discount);
                        mainPageConnection.giveToProductInfoFragmentProductItem();

                    }

                    @Override
                    public void choiseManualDiscount(Discount discount) {
                        mainPageConnection.setDiscountToProduct(discount);
                        mainPageConnection.giveToProductInfoFragmentProductItem();

                    }
                };
                DiscountDialog discountDialog = new DiscountDialog(getContext(), databaseManager, callbackDiscountDialog, orderProductItem.getOrderProduct().getPrice(), Discount.ITEM, decimalFormat,preferencesHelper);
                discountDialog.show();
            }
        });
        btnPosition.setOnClickListener(view -> {
            mainPageConnection.onStockPositionClicked();
        });



    }

    public void refreshData(){
        mainPageConnection.setProductInfoView(this);
        mainPageConnection.giveToProductInfoFragmentProductItem();
    }
    OrderProductItem orderProductItem;
    @Override
    public void initProductData(OrderProductItem orderProductItem) {
       this.orderProductItem = orderProductItem;

        GlideApp.with(this)
                .load(orderProductItem.getOrderProduct().getProduct().getPhotoPath())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .thumbnail(0.2f)
                .centerCrop()
                .placeholder(R.drawable.default_product_image)
                .error(R.drawable.default_product_image)
                .into(ivProductImage);

        tvProductName.setText(orderProductItem.getOrderProduct().getProduct().getName());
        hideAlert();
        databaseManager.getProductInvenotry(orderProductItem.getOrderProduct().getProductId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(count ->{
            //TODO VOQTINCHALI, ATAK COUNTI HISOBLASHDA ORDERDIGI COUNTLANIYAM HISOBLASH KEREDI
            if(count < 0){
                showAlert();
            }else hideAlert();
            tvQuantity.setText(decimalFormat.format(count) + " " + orderProductItem.getOrderProduct().getProduct().getMainUnit().getAbbr());
        });

        if(orderProductItem.getOrderProduct().getProduct().getMainUnit().getUnitCategory().getUnitType() == UnitCategory.PIECE){
            tvOrderQuantity.setText(decimalFormat.format(orderProductItem.getOutcomeProduct().getSumCountValue()));
            tvOrderQuantity.setBackground(null);
            tvUnitName.setText(getResources().getString(R.string.quantity));
            btnSetQuantity.setText(getResources().getString(R.string.set_quantity));
            ivMinus.setVisibility(View.VISIBLE);
            ivPlus.setVisibility(View.VISIBLE);
        }
        else {

            double count = orderProductItem.getOutcomeProduct().getSumCountValue();

            DecimalFormat df = null;
            if(count<0.001){
                df = decimalFormatLocal;
            }else df = decimalFormat;

            tvOrderQuantity.setText(df.format(count) + " " + orderProductItem.getOrderProduct().getProduct().getMainUnit().getAbbr());
            btnSetQuantity.setText(getResources().getString(R.string.set)+orderProductItem.getOrderProduct().getProduct().getMainUnit().getUnitCategory().getName());
            tvOrderQuantity.setBackgroundResource(R.drawable.order_list_weight_product_item_deactive);
            tvUnitName.setText(orderProductItem.getOrderProduct().getProduct().getMainUnit().getUnitCategory().getName());
            ivMinus.setVisibility(View.GONE);
            ivPlus.setVisibility(View.GONE);
        }


        etSpecialRequest.setText(orderProductItem.getOrderProduct().getDiscription());
        etSpecialRequest.addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mainPageConnection.changeDiscription(etSpecialRequest.getText().toString());
            }
        });
        if(orderProductItem.getDiscount() == null){
            btnDiscountItem.setText(getResources().getString(R.string.discount));
        }else {
            btnDiscountItem.setText(getResources().getString(R.string.remove_discount));
        }
        if(orderProductItem.getServiceFee() == null){
            btnServiceFee.setText(getResources().getString(R.string.service_fee));
        }else {
            btnServiceFee.setText(getResources().getString(R.string.remove_service_fee));
        }


    }

    @Override
    public void changeQuantityColor(int color) {
        tvQuantity.setTextColor(getContext().getResources().getColor(color));
    }

    @Override
    public void showAlert() {
        ivAlert.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAlert() {
        ivAlert.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDetach() {
        mainPageConnection.setProductInfoView(null);
        super.onDetach();
    }
}
