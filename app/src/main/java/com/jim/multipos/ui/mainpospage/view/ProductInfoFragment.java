package com.jim.multipos.ui.mainpospage.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.order.OrderProduct;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.mainpospage.connection.MainPageConnection;
import com.jim.multipos.ui.mainpospage.dialogs.ChooseVendorDialog;
import com.jim.multipos.ui.mainpospage.dialogs.DiscountDialog;
import com.jim.multipos.ui.mainpospage.dialogs.ServiceFeeDialog;
import com.jim.multipos.ui.mainpospage.dialogs.SetQuantityDialog;
import com.jim.multipos.ui.mainpospage.model.OrderProductItem;
import com.jim.multipos.ui.mainpospage.presenter.ProductInfoPresenter;
import com.jim.multipos.utils.GlideApp;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.rxevents.MessageEvent;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.jim.multipos.ui.consignment.ConsignmentActivity.PRODUCT_ID;

/**
 * Created by developer on 24.08.2017.
 */

public class ProductInfoFragment extends BaseFragment implements ProductInfoView {
    @Inject
    ProductInfoPresenter presenter;
    @BindView(R.id.ivProductImage)
    ImageView ivProductImage;
    @BindView(R.id.tvProductName)
    TextView tvProductName;
    @BindView(R.id.tvCompanyName)
    TextView tvCompanyName;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
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
    @Inject
    RxBus rxBus;
    @Inject
    MainPageConnection mainPageConnection;
    @Override
    protected int getLayout() {
        return R.layout.product_info_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mainPageConnection.setProductInfoView(this);
        mainPageConnection.giveToProductInfoFragmentProductItem();


        RxView.clicks(ivArrowLeft).subscribe(o -> {
            tvVendorName.setText(presenter.getPrevVendor().getName());
        });

        RxView.clicks(ivArrowRight).subscribe(o -> {
            tvVendorName.setText(presenter.getNextVendor().getName());
        });

        RxView.clicks(btnClose).subscribe(o -> {
            UIUtils.closeKeyboard(btnClose, getContext());
            rxBus.send(new MessageEvent("Close_Info_Product"));
        });

        RxView.clicks(btnServiceFee).subscribe(o -> {
            ServiceFeeDialog dialog = new ServiceFeeDialog();
            dialog.setCaption(getString(R.string.choose_for_product));
            dialog.setServiceFee(presenter.getServiceFees());
            dialog.setOnDialogListener(new ServiceFeeDialog.OnDialogListener() {
                @Override
                public List<ServiceFee> getServiceFees() {
                    return presenter.getServiceFees();
                }

                @Override
                public void addServiceFee(double amount, String description, int amountType) {
                    presenter.addServiceFee(amount, description, amountType);
                }
            });
            dialog.show(getActivity().getSupportFragmentManager(), "ServiceFeeProductInfoDialog");
        });

        RxView.clicks(btnDiscountItem).subscribe(o -> {
            DiscountDialog dialog = new DiscountDialog();
            dialog.setDiscounts(presenter.getDiscount(getResources().getStringArray(R.array.discount_used_types_abr)), getResources().getStringArray(R.array.discount_amount_types_abr));
            dialog.setOnDialogListener(new DiscountDialog.OnDialogListener() {
                @Override
                public List<Discount> getDiscounts() {
                    return presenter.getDiscount(getResources().getStringArray(R.array.discount_used_types_abr));
                }

                @Override
                public void addDiscount(double amount, String description, int amountType) {
                    presenter.addDiscount(amount, description, amountType);
                }
            });
            dialog.show(getActivity().getSupportFragmentManager(), "DiscountProductInfoDialog");
            dialog.setCaption(getString(R.string.choose_for_product));
        });
    }


    @Override
    public void initProductData(OrderProductItem orderProductItem) {

        GlideApp.with(this)
                .load(orderProductItem.getOrderProduct().getProduct().getPhotoPath())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .thumbnail(0.2f)
                .centerCrop()
                .transform(new RoundedCorners(20))
                .placeholder(R.drawable.default_product_image)
                .error(R.drawable.default_product_image)
                .into(ivProductImage);

        tvProductName.setText(orderProductItem.getOrderProduct().getProduct().getName());
        tvDescription.setText(orderProductItem.getOrderProduct().getProduct().getDescription());
        tvVendorName.setText(orderProductItem.getOrderProduct().getProduct().getVendor().get(0).getName());
        tvQuantity.setText((orderProductItem.getOrderProduct().getCount() - 1) + " " + orderProductItem.getOrderProduct().getProduct().getMainUnit().getAbbr());
        tvOrderQuantity.setText(String.valueOf(presenter.getCurrentProductQuantity()));

        for (int i = 0; i < orderProductItem.getOrderProduct().getProduct().getVendor().size(); i++) {
            tvCompanyName.append(orderProductItem.getOrderProduct().getProduct().getVendor().get(i).getName());

            if (i < orderProductItem.getOrderProduct().getProduct().getVendor().size() - 1) {
                tvCompanyName.append(", ");
            }
        }

        RxView.clicks(ivMinus).subscribe(o -> {
            mainPageConnection.minusProductCount();
            mainPageConnection.giveToProductInfoFragmentProductItem();
        });

        RxView.clicks(ivPlus).subscribe(o -> {
            mainPageConnection.plusProductCount();
            mainPageConnection.giveToProductInfoFragmentProductItem();

        });

        RxView.clicks(btnSetQuantity).subscribe(o -> {
            SetQuantityDialog dialog = new SetQuantityDialog(getContext(), value -> {
                presenter.setCurrentQuantity(value);
                tvQuantity.setText(String.valueOf(presenter.getProductQuantity()) + " " + orderProductItem.getOrderProduct().getProduct().getMainUnit().getAbbr());
                tvOrderQuantity.setText(String.valueOf(presenter.getCurrentProductQuantity()));
            });

            dialog.show();
        });

        RxView.clicks(btnChooseVendor).subscribe(o -> {
            ChooseVendorDialog dialog = new ChooseVendorDialog(getContext(), orderProductItem.getOrderProduct().getProduct().getVendor(), vendor -> {
                tvVendorName.setText(vendor.getName());
            });

            dialog.show();
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
