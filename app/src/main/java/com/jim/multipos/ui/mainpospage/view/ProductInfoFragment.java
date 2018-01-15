package com.jim.multipos.ui.mainpospage.view;

import android.graphics.Paint;
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
import com.jim.multipos.data.db.model.inventory.InventoryState;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.unit.UnitCategory;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.mainpospage.connection.MainPageConnection;
import com.jim.multipos.ui.mainpospage.dialogs.ChooseVendorDialog;
import com.jim.multipos.ui.mainpospage.dialogs.DiscountDialog;
import com.jim.multipos.ui.mainpospage.dialogs.ServiceFeeDialog;
import com.jim.multipos.ui.mainpospage.dialogs.SetQuantityDialog;
import com.jim.multipos.ui.mainpospage.dialogs.UnitValuePicker;
import com.jim.multipos.ui.mainpospage.model.OrderProductItem;
import com.jim.multipos.ui.mainpospage.presenter.ProductInfoPresenter;
import com.jim.multipos.utils.GlideApp;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.TextWatcherOnTextChange;
import com.jim.multipos.utils.WarningDialog;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

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
    @BindView(R.id.tvCompanyName)
    TextView tvCompanyName;

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
    @BindView(R.id.ivArrowLeft)
    ImageView ivArrowLeft;
    @BindView(R.id.ivArrowRight)
    ImageView ivArrowRight;
    @BindView(R.id.tvVendorName)
    TextView tvVendorName;
    @BindView(R.id.btnChooseVendor)
    MpButton btnChooseVendor;
    @BindView(R.id.btnServiceFee)
    MpButton btnServiceFee;
    @BindView(R.id.btnDiscountItem)
    MpButton btnDiscountItem;
    @BindView(R.id.btnClose)
    MpButton btnClose;
    @BindView(R.id.etSpecialRequest)
    EditText etSpecialRequest;
    @BindView(R.id.ivAlert)
    ImageView ivAlert;
    @BindView(R.id.btnRemove)
    MpButton btnRemove;
    @BindView(R.id.llVendorPicker)
    LinearLayout llVendorPicker;
    @BindView(R.id.tvUnitName)
    TextView tvUnitName;

    @Inject
    RxBus rxBus;
    @Inject
    DatabaseManager databaseManager;

    @Inject
    MainPageConnection mainPageConnection;
    @Override
    protected int getLayout() {
        return R.layout.product_info_fragment;
    }
    int currentVendorPosition = 0;
    @Override
    protected void init(Bundle savedInstanceState) {
        mainPageConnection.setProductInfoView(this);
        mainPageConnection.giveToProductInfoFragmentProductItem();

    }

    public void refreshData(){
        mainPageConnection.setProductInfoView(this);
        mainPageConnection.giveToProductInfoFragmentProductItem();
    }
    @Override
    public void initProductData(OrderProductItem orderProductItem) {
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

        GlideApp.with(this)
                .load(orderProductItem.getOrderProduct().getProduct().getPhotoPath())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .thumbnail(0.2f)
                .centerCrop()
                .placeholder(R.drawable.default_product_image)
                .error(R.drawable.default_product_image)
                .into(ivProductImage);
        tvCompanyName.setPaintFlags(tvCompanyName.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        tvProductName.setText(orderProductItem.getOrderProduct().getProduct().getName());

        tvVendorName.setText(orderProductItem.getOrderProduct().getVendor().getName());
        List<InventoryState> inventoryStates = databaseManager.getInventoryStatesByProductId(orderProductItem.getOrderProduct().getProductId()).blockingFirst();
        InventoryState inventoryState = null;
        for (int i = 0; i < inventoryStates.size(); i++) {
            if(inventoryStates.get(i).getVendor().getId().equals(orderProductItem.getOrderProduct().getVendor().getId())){
                inventoryState = inventoryStates.get(i);
                break;
            }
        }
        tvQuantity.setText(decimalFormat.format(inventoryState.getValue() - orderProductItem.getOrderProduct().getCount()) + " " + orderProductItem.getOrderProduct().getProduct().getMainUnit().getAbbr());

        if(orderProductItem.getOrderProduct().getProduct().getMainUnit().getUnitCategory().getUnitType() == UnitCategory.PIECE){
            tvOrderQuantity.setText(decimalFormat.format(orderProductItem.getOrderProduct().getCount()));
            tvOrderQuantity.setBackground(null);
            tvUnitName.setText("Quantity");
            btnSetQuantity.setText("Set Quantity");
            ivMinus.setVisibility(View.VISIBLE);
            ivPlus.setVisibility(View.VISIBLE);
        }
        else {

            double count = orderProductItem.getOrderProduct().getCount();

            DecimalFormat df = null;
            if(count<0.001){
                df = decimalFormatLocal;
            }else df = decimalFormat;

            tvOrderQuantity.setText(df.format(count) + " " + orderProductItem.getOrderProduct().getProduct().getMainUnit().getAbbr());
            btnSetQuantity.setText("Set "+orderProductItem.getOrderProduct().getProduct().getMainUnit().getUnitCategory().getName());
            tvOrderQuantity.setBackgroundResource(R.drawable.order_list_weight_product_item_deactive);
            tvUnitName.setText(orderProductItem.getOrderProduct().getProduct().getMainUnit().getUnitCategory().getName());
            ivMinus.setVisibility(View.GONE);
            ivPlus.setVisibility(View.GONE);
        }


        String vendorName = "";
        for (Vendor vn:orderProductItem.getOrderProduct().getProduct().getVendor()) {
            if(!vendorName.equals("")) vendorName += ", ";
            vendorName += vn.getName();
        }
        tvCompanyName.setText(vendorName);
        ivMinus.setOnClickListener(view -> {
            mainPageConnection.minusProductCount();
            mainPageConnection.giveToProductInfoFragmentProductItem();
        });
        ivPlus.setOnClickListener(view -> {
            mainPageConnection.plusProductCount();
            mainPageConnection.giveToProductInfoFragmentProductItem();
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
                },orderProductItem.getOrderProduct().getCount());
                unitValuePicker.show();
            }


        });


        if(orderProductItem.getOrderProduct().getProduct().getVendor().size()==1){
            llVendorPicker.setVisibility(View.GONE);
        }else {
            btnChooseVendor.setOnClickListener(view -> {
                ChooseVendorDialog dialog = new ChooseVendorDialog(getContext(), orderProductItem.getOrderProduct().getProduct().getVendor(), vendor -> {
                    mainPageConnection.changeProductVendor(vendor);
                    mainPageConnection.giveToProductInfoFragmentProductItem();
                });
                dialog.show();
            });
            List<Vendor> vendors = orderProductItem.getOrderProduct().getProduct().getVendor();
            Vendor currentVendor = orderProductItem.getOrderProduct().getVendor();

            for (int i = 0; i < vendors.size(); i++) {
                if(currentVendor.getId().equals(vendors.get(i).getId())){
                    currentVendorPosition = i;
                    break;
                }
            }
            ivArrowLeft.setOnClickListener(view -> {
                currentVendorPosition--;
                if(currentVendorPosition < 0 ) currentVendorPosition = vendors.size()-1;
                mainPageConnection.changeProductVendor(vendors.get(currentVendorPosition));
                mainPageConnection.giveToProductInfoFragmentProductItem();
            });
            ivArrowRight.setOnClickListener(view -> {
                mainPageConnection.changeProductVendor(vendors.get((++currentVendorPosition)%vendors.size()));
                mainPageConnection.giveToProductInfoFragmentProductItem();
            });

        }
        etSpecialRequest.setText(orderProductItem.getOrderProduct().getDiscription());
        etSpecialRequest.addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mainPageConnection.changeDiscription(etSpecialRequest.getText().toString());
            }
        });
        if(orderProductItem.getDiscount() == null){
            btnDiscountItem.setText("Discount");
        }else {
            btnDiscountItem.setText("Remove\nDiscount");
        }
        if(orderProductItem.getServiceFee() == null){
            btnServiceFee.setText("Service Fee");
        }else {
            btnServiceFee.setText("Remove\nService Fee");
        }

        btnRemove.setOnClickListener(view -> {
            WarningDialog warningDialog = new WarningDialog(getActivity());
            warningDialog.setWarningMessage("Are you sure delete product from order?");
            warningDialog.setOnYesClickListener(view1 -> {
                warningDialog.dismiss();
                mainPageConnection.removeOrderProducts();
                ((MainPosPageActivity)getActivity()).hideProductInfoFragment();
            });
            warningDialog.setOnNoClickListener(view1 -> {
                warningDialog.dismiss();
            });
            warningDialog.setPositiveButtonText(getString(R.string.yes));
            warningDialog.setNegativeButtonText(getString(R.string.cancel));
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
                ServiceFeeDialog serviceFeeDialog = new ServiceFeeDialog(getContext(),databaseManager,callbackServiceFeeDialog,orderProductItem.getOrderProduct().getPrice(),ServiceFee.ITEM, decimalFormat);
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
                DiscountDialog discountDialog = new DiscountDialog(getContext(), databaseManager, callbackDiscountDialog, orderProductItem.getOrderProduct().getPrice(), Discount.ITEM, decimalFormat);
                discountDialog.show();
            }
        });
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
        super.onDetach();
        mainPageConnection.setProductInfoView(null);
    }
}
