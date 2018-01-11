package com.jim.multipos.ui.mainpospage.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MpLightButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.mainpospage.adapter.OrderProductAdapter;
import com.jim.multipos.ui.mainpospage.connection.MainPageConnection;
import com.jim.multipos.ui.mainpospage.dialogs.CustomerDialog;
import com.jim.multipos.ui.mainpospage.dialogs.DiscountDialog;
import com.jim.multipos.ui.mainpospage.dialogs.ServiceFeeDialog;
import com.jim.multipos.ui.mainpospage.dialogs.UnitValuePicker;
import com.jim.multipos.ui.mainpospage.model.OrderProductItem;
import com.jim.multipos.ui.mainpospage.presenter.OrderListPresenter;
import com.jim.multipos.utils.LinearLayoutManagerWithSmoothScroller;
import com.jim.multipos.utils.managers.NotifyManager;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class OrderListFragment extends BaseFragment implements OrderListView {
    @Inject
    OrderListPresenter presenter;
    @Inject
    DecimalFormat decimalFormat;
    @Inject
    DatabaseManager databaseManager;
    @Inject
    NotifyManager notifyManager;
    @Inject
    MainPageConnection mainPageConnection;
    Currency currency;
    @BindView(R.id.llPay)
    LinearLayout llPay;
    @BindView(R.id.llDiscount)
    LinearLayout llDiscount;
    @BindView(R.id.llServiceFee)
    LinearLayout llServiceFee;
    @BindView(R.id.llPrintCheck)
    LinearLayout llPrintCheck;
    @BindView(R.id.tvCustomerName)
    TextView tvCustomerName;
    @BindView(R.id.tvSubTotal)
    TextView tvSubTotal;
    @BindView(R.id.tvDiscountAmount)
    TextView tvDiscountAmount;
    @BindView(R.id.tvServiceAmount)
    TextView tvServiceAmount;
    @BindView(R.id.tvBalanceDue)
    TextView tvBalanceDue;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.rvOrderProducts)
    RecyclerView rvOrderProducts;
    @BindView(R.id.tvDiscountName)
    TextView tvDiscountName;
    @BindView(R.id.ivDiscount)
    ImageView ivDiscount;
    @BindView(R.id.tvServiceFeeName)
    TextView tvServiceFeeName;
    @BindView(R.id.ivServiceFee)
    ImageView ivServiceFee;
    @Inject
    OrderProductAdapter orderProductAdapter;
    @BindView(R.id.lbChoiseCustomer)
    MpLightButton lbChooseCustomer;
    private OrderProductItem orderProductItem;
    private int currentPosition;
    public static final String PRODUCT_ADD_TO_ORDER = "addorderproduct";
    @Override
    protected int getLayout() {
        return R.layout.fragment_order_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mainPageConnection.setOrderListView(this);
        currency = databaseManager.getMainCurrency();
        presenter.onCreateView(savedInstanceState);
        lbChooseCustomer.setOnLightButtonClickListener(view1 -> {
            presenter.onClickChooseCustomerButton();

        });
        llDiscount.setOnClickListener(view -> {
            presenter.openDiscountDialog();

        });
        llServiceFee.setOnClickListener(view -> {
            presenter.openSeriveFeeDialog();

        });
        llPrintCheck.setOnClickListener(view -> {

        });
        llPay.setOnClickListener(view -> {
            if(isPaymentOpen){
                ((MainPosPageActivity) getActivity()).hidePaymentFragment();
                isPaymentOpen = false;
            }else {
                ((MainPosPageActivity) getActivity()).showPaymentFragment();
                isPaymentOpen = true;
            }
        });
    }
    LinearLayoutManagerWithSmoothScroller linearLayoutManager;
    @Override
    public void initOrderList(List<Object> objectList) {
        linearLayoutManager = new LinearLayoutManagerWithSmoothScroller(getContext());
        rvOrderProducts.setLayoutManager(linearLayoutManager);
        rvOrderProducts.setAdapter(orderProductAdapter);
        rvOrderProducts.setItemAnimator(null);
        orderProductAdapter.setData(objectList, new OrderProductAdapter.CallbackOrderProductList() {
            @Override
            public void onPlusCount(int position) {
                presenter.onPlusCount(position);
            }

            @Override
            public void onMinusCount(int position) {
                presenter.onMinusCount(position);
            }

            @Override
            public void onOrderProductClick(int position) {
                presenter.onOrderProductClick(position);
            }

            @Override
            public void onOrderDiscountClick() {
                presenter.onOrderDiscountClick();
            }

            @Override
            public void onOrderServiceFeeClick() {
                presenter.onOrderServiceFeeClick();
            }

            @Override
            public void onCountWeigtClick(int position) {
                presenter.onCountWeigtClick(position);
            }
        });
    }

    @Override
    public void notifyList() {
        orderProductAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyItemAdded(int adapterPosition, int listSize,int[] extraPositionsForUpdate) {
        orderProductAdapter.notifyItemInserted(adapterPosition);
        scrollToPosition(listSize-1);

        if(extraPositionsForUpdate[0]!=-1 && adapterPosition != extraPositionsForUpdate[0]) {
            orderProductAdapter.notifyItemChanged(extraPositionsForUpdate[0]);
        }
        if(extraPositionsForUpdate[1]!=-1&& adapterPosition != extraPositionsForUpdate[1]) {
            orderProductAdapter.notifyItemChanged(extraPositionsForUpdate[1]);
        }
    }

    @Override
    public void notifyItemRemove(int adapterPosition, int listSize, int[] extraPositionsForUpdate) {
        orderProductAdapter.notifyItemRemoved(adapterPosition);
        scrollToPosition(adapterPosition);

        if(extraPositionsForUpdate[0]!=-1 ) {
            orderProductAdapter.notifyItemChanged(extraPositionsForUpdate[0]);
        }
        if(extraPositionsForUpdate[1]!=-1) {
            orderProductAdapter.notifyItemChanged(extraPositionsForUpdate[1]);
        }
    }


    @Override
    public void notifyItemChanged(int adapterPosition, int listSize,int[] extraPositionsForUpdate) {
        orderProductAdapter.notifyItemChanged(adapterPosition);
        scrollToPosition(adapterPosition);

        if(extraPositionsForUpdate[0]!=-1 && adapterPosition != extraPositionsForUpdate[0]) {
            orderProductAdapter.notifyItemChanged(extraPositionsForUpdate[0]);
        }
        if(extraPositionsForUpdate[1]!=-1&& adapterPosition != extraPositionsForUpdate[1]) {
            orderProductAdapter.notifyItemChanged(extraPositionsForUpdate[1]);
        }
    }
    public void hideInfoProduct(){
        currentPosition = -1;
    }
    public void hidePaymentFragment(){
        isPaymentOpen = false;
    }
    boolean isPaymentOpen = false;
    @Override
    public void openProductInfoFragment(OrderProductItem orderProductItem,int position) {
        this.orderProductItem = orderProductItem;
        if(currentPosition == position) {
            currentPosition = -1;
            ((MainPosPageActivity) getActivity()).hideProductInfoFragment();
        }
        else {
            currentPosition = position;
            ((MainPosPageActivity) getActivity()).showProductInfoFragment();
        }
    }

    @Override
    public void sendToProductInfoProductItem() {
        mainPageConnection.sendProductItemToProductInfo(orderProductItem);
    }

    @Override
    public void plusProductCount() {
        presenter.onPlusCount(currentPosition);
    }

    @Override
    public void minusProductCount() {
        presenter.onMinusCount(currentPosition);
    }

    @Override
    public void addProductToOrder(Long productId) {
        presenter.addProductToList(productId);
    }

    @Override
    public void addDiscountToProduct(Long productId, Discount discount, boolean isManual) {
        presenter.addDiscountToProduct(productId,discount,isManual);
    }

    @Override
    public void addServiceFeeToProduct(Long productId, ServiceFee serviceFee, boolean isManual) {
        presenter.addServiceFeeToProduct(productId,serviceFee,isManual);
    }



    @Override
    public void openDiscountDialog(DiscountDialog.CallbackDiscountDialog callbackDiscountDialog, double orginalAmount) {
        DiscountDialog discountDialog = new DiscountDialog(getContext(), databaseManager,callbackDiscountDialog,orginalAmount,Discount.ORDER);
        discountDialog.show();
    }

    @Override
    public void openSericeFeeDialog(ServiceFeeDialog.CallbackServiceFeeDialog callbackServiceFeeDialog, double orginalAmount) {
        ServiceFeeDialog serviceFeeDialog = new ServiceFeeDialog(getContext(),databaseManager,callbackServiceFeeDialog,orginalAmount,ServiceFee.ORDER);
        serviceFeeDialog.show();
    }

    @Override
    public void disableDiscountButton(String discountName) {
//        tvDiscountName.setText(discountName);
        ivDiscount.setImageResource(R.drawable.cancel_extras);
    }

    @Override
    public void enableDiscountButton() {
        tvDiscountName.setText(R.string.discount);
        ivDiscount.setImageResource(R.drawable.discount);

    }

    @Override
    public void disableServiceFeeButton(String serviceFeeName) {
//        tvServiceFeeName.setText(serviceFeeName);
        ivServiceFee.setImageResource(R.drawable.cancel_extras);
    }

    @Override
    public void enableServiceFeeButton() {
        tvServiceFeeName.setText(R.string.service_fee);
        ivServiceFee.setImageResource(R.drawable.service);
    }

    @Override
    public void setCount(double count) {
        presenter.setCount(currentPosition,count);
    }

    @Override
    public void changeProductVendor(Vendor vendor) {
        presenter.changeProductVendor(vendor,currentPosition);
    }

    @Override
    public void changeDiscription(String discription) {
        presenter.changeDiscription(discription,currentPosition);
    }

    @Override
    public void removeOrderProducts() {
        presenter.removeOrderProducts(currentPosition);
    }

    @Override
    public void setDiscountToProduct(Discount discountToProduct) {
        presenter.setDiscountToProduct(discountToProduct,currentPosition);
    }

    @Override
    public void setServiceFeeProduct(ServiceFee serviceFeeProduct) {
        presenter.setServiceFeeProduct(serviceFeeProduct,currentPosition);
    }

    @Override
    public void changeCustomer(Customer customer) {
        presenter.changeCustomer(customer);
    }

    @Override
    public void openCustomerDialog() {
        CustomerDialog customerDialog = new CustomerDialog(getContext(), databaseManager, notifyManager,mainPageConnection);
        customerDialog.show();
    }

    @Override
    public void openUnitValuePicker(Product product) {
        UnitValuePicker unitValuePicker = new UnitValuePicker(getContext(), product, weight -> {
            presenter.addProductWithWeightToList(product,weight);
        },0);
        unitValuePicker.show();
    }

    @Override
    public void openUnitValuePickerEdit(Product product, double weightOld) {
        UnitValuePicker unitValuePicker = new UnitValuePicker(getContext(), product, weight -> {
            presenter.addProductWithWeightToListEdit(product,weight);
        },weightOld);
        unitValuePicker.show();
    }

    @Override
    public void addProductWithWeightToListEdit(double weight) {
        presenter.addProductWithWeightToListEditFromInfo(currentPosition,weight);

    }


    @Override
    public void updateOrderDetials(Order order,Customer customer) {
        tvSubTotal.setText(decimalFormat.format(order.getSubTotalValue())+" "+currency.getAbbr());
        tvDiscountAmount.setText(decimalFormat.format(order.getDiscountTotalValue())+" "+currency.getAbbr());
        tvServiceAmount.setText(((order.getServiceTotalValue()!=0)?"+":"")+decimalFormat.format(order.getServiceTotalValue())+" "+currency.getAbbr());
        tvTotal.setText(decimalFormat.format(order.getSubTotalValue()+order.getDiscountTotalValue()+order.getServiceTotalValue())+" "+currency.getAbbr());
        tvBalanceDue.setText(decimalFormat.format(order.getSubTotalValue()+order.getDiscountTotalValue()+order.getServiceTotalValue()- order.getTotalPayed())+" "+currency.getAbbr());
        if(customer !=null){
            tvCustomerName.setText(customer.getName());
            lbChooseCustomer.setImage(R.drawable.cancel_customer);
        }else {
            tvCustomerName.setText(getString(R.string.customer_choice));
            lbChooseCustomer.setImage(R.drawable.add_customer);
        }
    }

    @Override
    public void scrollToPosition(int pos) {
        rvOrderProducts.scrollToPosition(pos);


    }

    @Override
    public void onDetach() {
        mainPageConnection.setOrderListView(null);
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
