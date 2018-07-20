package com.jim.multipos.ui.mainpospage.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jim.mpviews.MpLightButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.currency.Currency;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.consignment.adapter.VendorItemsListAdapter;
import com.jim.multipos.ui.consignment.dialogs.StockPositionsDialog;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.mainpospage.adapter.OrderProductAdapter;
import com.jim.multipos.ui.mainpospage.connection.MainPageConnection;
import com.jim.multipos.ui.mainpospage.dialogs.CustomerDialog;
import com.jim.multipos.ui.mainpospage.dialogs.DiscountDialog;
import com.jim.multipos.ui.mainpospage.dialogs.ServiceFeeDialog;
import com.jim.multipos.ui.mainpospage.dialogs.UnitValuePicker;
import com.jim.multipos.ui.mainpospage.model.OrderProductItem;
import com.jim.multipos.ui.mainpospage.presenter.OrderListPresenter;
import com.jim.multipos.utils.BarcodeStack;
import com.jim.multipos.utils.LinearLayoutManagerWithSmoothScroller;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.WarningDialog;
import com.jim.multipos.utils.managers.NotifyManager;
import com.jim.multipos.utils.rxevents.inventory_events.InventoryStateEvent;
import com.jim.multipos.utils.rxevents.main_order_events.CustomerEvent;
import com.jim.multipos.utils.rxevents.main_order_events.DebtEvent;
import com.jim.multipos.utils.rxevents.main_order_events.DiscountEvent;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;
import com.jim.multipos.utils.rxevents.main_order_events.ProductEvent;
import com.jim.multipos.utils.rxevents.main_order_events.ServiceFeeEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


public class OrderListFragment extends BaseFragment implements OrderListView {
    public static final String NEW_ORDER_ID = "new_order_id";
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
    @Inject
    BarcodeStack barcodeStack;
    @Inject
    RxBus rxBus;
    private ArrayList<Disposable> subscriptions;
    @Inject
    PreferencesHelper preferencesHelper;
    Currency currency;
    @BindView(R.id.tvBalanceDueLabel)
    TextView tvBalanceDueLabel;
    @BindView(R.id.llDiscountGroup)
    LinearLayout llDiscountGroup;
    @BindView(R.id.llServiceFeeGroup)
    LinearLayout llServiceFeeGroup;
    @BindView(R.id.tvPayed)
    TextView tvPayed;
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
    @BindView(R.id.lbCancelOrder)
    MpLightButton lbCancelOrder;
    @BindView(R.id.lbHoldOrder)
    MpLightButton lbHoldOrder;
    @Inject
    OrderProductAdapter orderProductAdapter;
    @BindView(R.id.lbChoiseCustomer)
    MpLightButton lbChooseCustomer;
    @BindView(R.id.ivPay)
    ImageView ivPay;
    @BindView(R.id.tvPay)
    TextView tvPay;
    @BindView(R.id.llTipsGroup)
    LinearLayout llTipsGroup;
    @BindView(R.id.tvTips)
    TextView tvTips;
    private OrderProductItem orderProductItem;
    private int currentPosition = -1;
    public static final String PRODUCT_ADD_TO_ORDER = "addorderproduct";
    private CustomerDialog customerDialog;
    private boolean fromAddCustomer = false;
    private Dialog dialog;
    @Inject
    VendorItemsListAdapter vendorItemsListAdapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_order_list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        subscriptions = new ArrayList<>();
        mainPageConnection.setOrderListView(this);
        currency = databaseManager.getMainCurrency();

        llServiceFeeGroup.setVisibility(View.GONE);
        llDiscountGroup.setVisibility(View.GONE);

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
            presenter.printStockCheck();
        });
        llPay.setOnClickListener(view -> {
            if (isPaymentOpen) {
                ((MainPosPageActivity) getActivity()).hidePaymentFragment();
                isPaymentOpen = false;
            } else {
                ((MainPosPageActivity) getActivity()).showPaymentFragment();
                isPaymentOpen = true;
            }
        });
        lbHoldOrder.setOnLightButtonClickListener(view -> {
            mainPageConnection.onHoldOrderClicked();
        });
        lbCancelOrder.setOnLightButtonClickListener(view -> {
            presenter.onCancelClicked();

        });
        presenter.onCreateView(getArguments());
        subscriptions.add(
                rxBus.toObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(o -> {
                    if (o instanceof ProductEvent) {
                        ProductEvent productEvent = (ProductEvent) o;
                        if (productEvent.getType() == GlobalEventConstants.UPDATE) {
                            presenter.eventProductUpdate(productEvent.getProduct(), productEvent.getNewProduct());
                        } else if (productEvent.getType() == GlobalEventConstants.DELETE) {
                            presenter.eventProductDelete(productEvent.getProduct());
                        }
                    } else if (o instanceof CustomerEvent) {
                        CustomerEvent customerEvent = (CustomerEvent) o;
                        if (customerEvent.getType() == GlobalEventConstants.UPDATE) {
                            presenter.eventCustomerUpdate(customerEvent.getCustomer());
                        } else if (customerEvent.getType() == GlobalEventConstants.DELETE) {
                            presenter.eventCustomerDelete(customerEvent.getCustomer());
                        }
                    } else if (o instanceof DebtEvent) {
                        DebtEvent debtEvent = (DebtEvent) o;
                        if (debtEvent.getType() == GlobalEventConstants.UPDATE) {
                            presenter.eventDebtUpdate(debtEvent.getDebt(), debtEvent.getNewDebt());
                        } else if (debtEvent.getType() == GlobalEventConstants.DELETE) {
                            presenter.eventDebtDelete(debtEvent.getDebt());
                        }
                    } else if (o instanceof DiscountEvent) {
                        DiscountEvent discountEvent = (DiscountEvent) o;
                        if (discountEvent.getType() == GlobalEventConstants.UPDATE) {
                            presenter.eventDiscountUpdate(discountEvent.getDiscount());
                        } else if (discountEvent.getType() == GlobalEventConstants.DELETE) {
                            presenter.eventDiscountDelete(discountEvent.getDiscount());
                        }
                    } else if (o instanceof ServiceFeeEvent) {
                        ServiceFeeEvent serviceFeeEvent = (ServiceFeeEvent) o;
                        if (serviceFeeEvent.getType() == GlobalEventConstants.UPDATE) {
                            presenter.eventServiceFeeUpdate(serviceFeeEvent.getServiceFee());
                        } else if (serviceFeeEvent.getType() == GlobalEventConstants.DELETE) {
                            presenter.eventServiceFeeDelete(serviceFeeEvent.getServiceFee());
                        }
                    } else if (o instanceof InventoryStateEvent) {
                        InventoryStateEvent event = (InventoryStateEvent) o;
                        if (event.getType() == GlobalEventConstants.UPDATE) {
                            presenter.eventConsignmentUpdate();
                        }
                    }

                })
        );

        barcodeStack.register(barcode -> {
            if (isAdded() && isVisible())
                presenter.onBarcodeReaded(barcode);
        });
    }

    @Override
    public void isPaymentOpen() {
        isPaymentOpen = true;
    }

    @Override
    public void isPaymentClose() {
        isPaymentOpen = false;
    }

    @Override
    public void onCloseOrder(Order order, List<PayedPartitions> payedPartitions, Debt debt) {
        presenter.onCloseOrder(order, payedPartitions, debt);
    }

    @Override
    public void updateCustomer(Customer customer) {
        presenter.updateCustomer(customer);
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
        presenter.sendToPaymentFragmentOrderAndPaymentsList();
    }

    @Override
    public void notifyItemAdded(int adapterPosition, int listSize, int[] extraPositionsForUpdate) {
        orderProductAdapter.notifyItemInserted(adapterPosition);
        scrollToPosition(listSize - 1);

        if (extraPositionsForUpdate[0] != -1 && adapterPosition != extraPositionsForUpdate[0]) {
            orderProductAdapter.notifyItemChanged(extraPositionsForUpdate[0]);
        }
        if (extraPositionsForUpdate[1] != -1 && adapterPosition != extraPositionsForUpdate[1]) {
            orderProductAdapter.notifyItemChanged(extraPositionsForUpdate[1]);
        }
        presenter.sendToPaymentFragmentOrderAndPaymentsList();
    }

    @Override
    public void notifyItemRemove(int adapterPosition, int listSize, int[] extraPositionsForUpdate) {
        orderProductAdapter.notifyItemRemoved(adapterPosition);
        scrollToPosition(adapterPosition);

        if (extraPositionsForUpdate[0] != -1) {
            orderProductAdapter.notifyItemChanged(extraPositionsForUpdate[0]);
        }
        if (extraPositionsForUpdate[1] != -1) {
            orderProductAdapter.notifyItemChanged(extraPositionsForUpdate[1]);
        }
        presenter.sendToPaymentFragmentOrderAndPaymentsList();
    }


    @Override
    public void notifyItemChanged(int adapterPosition, int listSize, int[] extraPositionsForUpdate) {
        orderProductAdapter.notifyItemChanged(adapterPosition);
        scrollToPosition(adapterPosition);

        if (extraPositionsForUpdate[0] != -1 && adapterPosition != extraPositionsForUpdate[0]) {
            orderProductAdapter.notifyItemChanged(extraPositionsForUpdate[0]);
        }
        if (extraPositionsForUpdate[1] != -1 && adapterPosition != extraPositionsForUpdate[1]) {
            orderProductAdapter.notifyItemChanged(extraPositionsForUpdate[1]);
        }
        presenter.sendToPaymentFragmentOrderAndPaymentsList();

    }

    public void hideInfoProduct() {
        currentPosition = -1;
    }

    public void hidePaymentFragment() {
        isPaymentOpen = false;
    }

    boolean isPaymentOpen = false;

    @Override
    public void openProductInfoFragment(OrderProductItem orderProductItem, int position) {
        this.orderProductItem = orderProductItem;
        if (currentPosition == position) {
            currentPosition = -1;
            ((MainPosPageActivity) getActivity()).hideProductInfoFragment();
        } else {
            currentPosition = position;
            ((MainPosPageActivity) getActivity()).showProductInfoFragment();
        }
    }

    @Override
    public void sendToProductInfoProductItem() {
        if (orderProductItem != null)
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
        presenter.addDiscountToProduct(productId, discount, isManual);
    }

    @Override
    public void addServiceFeeToProduct(Long productId, ServiceFee serviceFee, boolean isManual) {
        presenter.addServiceFeeToProduct(productId, serviceFee, isManual);
    }


    @Override
    public void openDiscountDialog(DiscountDialog.CallbackDiscountDialog callbackDiscountDialog, double orginalAmount) {
        DiscountDialog discountDialog = new DiscountDialog(getContext(), databaseManager, callbackDiscountDialog, orginalAmount, Discount.ORDER, decimalFormat, preferencesHelper);
        discountDialog.show();
    }

    @Override
    public void openSericeFeeDialog(ServiceFeeDialog.CallbackServiceFeeDialog callbackServiceFeeDialog, double orginalAmount) {
        ServiceFeeDialog serviceFeeDialog = new ServiceFeeDialog(getContext(), databaseManager, callbackServiceFeeDialog, orginalAmount, ServiceFee.ORDER, decimalFormat, preferencesHelper);
        serviceFeeDialog.show();
    }

    @Override
    public void disableDiscountButton(String discountName) {
//        tvDiscountName.setText(discountName);
        ivDiscount.setImageResource(R.drawable.cancel_extras);
    }

    @Override
    public void enableDiscountButton() {
        tvDiscountName.setText(getContext().getString(R.string.discount));
        ivDiscount.setImageResource(R.drawable.ic_discount);

    }

    @Override
    public void disableServiceFeeButton(String serviceFeeName) {
//        tvServiceFeeName.setText(serviceFeeName);
        ivServiceFee.setImageResource(R.drawable.cancel_extras);
    }

    @Override
    public void enableServiceFeeButton() {
        tvServiceFeeName.setText(getContext().getString(R.string.service_fee));
        ivServiceFee.setImageResource(R.drawable.ic_coins);
    }

    @Override
    public void setCount(double count) {
        presenter.setCount(currentPosition, count);
    }


    @Override
    public void changeDiscription(String discription) {
        presenter.changeDiscription(discription, currentPosition);
    }

    @Override
    public void removeOrderProducts() {
        presenter.removeOrderProducts(currentPosition);
    }

    @Override
    public void setDiscountToProduct(Discount discountToProduct) {
        presenter.setDiscountToProduct(discountToProduct, currentPosition);
    }

    @Override
    public void setServiceFeeProduct(ServiceFee serviceFeeProduct) {
        presenter.setServiceFeeProduct(serviceFeeProduct, currentPosition);
    }

    @Override
    public void changeCustomer(Customer customer) {
        presenter.changeCustomer(customer);
    }

    @Override
    public void openCustomerDialog() {
        customerDialog = new CustomerDialog(getContext(), databaseManager, notifyManager, mainPageConnection, barcodeStack);
        customerDialog.show();
        customerDialog.setListener(this::initScan);
    }

    @Override
    public void initScan() {
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }

    @Override
    public void scanBarcodeFor(boolean fromAddCustomer) {
        this.fromAddCustomer = fromAddCustomer;
    }

    @Override
    public void updateViewCustomer(Customer customer) {
        tvCustomerName.setText(customer.getName());
    }

    @Override
    public void sendCustomerToPaymentFragment(Customer customer) {
        mainPageConnection.setCustomer(customer);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() != null) {
                if (fromAddCustomer)
                    customerDialog.setBarcodeForAddCustomerDialog(intentResult.getContents());
                else customerDialog.setBarcode(intentResult.getContents());
            }
        }
    }

    @Override
    public void openUnitValuePicker(Product product) {
        UnitValuePicker unitValuePicker = new UnitValuePicker(getContext(), product, weight -> {
            presenter.addProductWithWeightToList(product, weight);
        }, 0);
        unitValuePicker.show();
    }

    @Override
    public void openUnitValuePickerEdit(Product product, double weightOld) {
        UnitValuePicker unitValuePicker = new UnitValuePicker(getContext(), product, weight -> {
            presenter.addProductWithWeightToListEdit(product, weight);
        }, weightOld);
        unitValuePicker.show();
    }

    @Override
    public void addProductWithWeightToListEdit(double weight) {
        presenter.addProductWithWeightToListEditFromInfo(currentPosition, weight);

    }

    @Override
    public void sendToPaymentFragmentOrderAndPaymentsList() {
        presenter.sendToPaymentFragmentOrderAndPaymentsList();
    }

    @Override
    public void sendDataToPaymentFragment(Order order, List<PayedPartitions> payedPartitions) {
        mainPageConnection.sendDataToPaymentFragment(order, payedPartitions);
    }

    @Override
    public void sendDataToPaymentFragmentWhenEdit(Order order, List<PayedPartitions> payedPartitions, Debt debt) {
        mainPageConnection.sendDataToPaymentFragmentWhenEdit(order, payedPartitions, debt);
    }


    @Override
    public void updateOrderDetials(Order order, Customer customer, List<PayedPartitions> payedPartitions) {
        tvSubTotal.setText(decimalFormat.format(order.getSubTotalValue()) + " " + currency.getAbbr());

        if (order.getDiscountTotalValue() != 0) {
            tvDiscountAmount.setText(decimalFormat.format(order.getDiscountTotalValue()) + " " + currency.getAbbr());
            llDiscountGroup.setVisibility(View.VISIBLE);
        } else llDiscountGroup.setVisibility(View.GONE);

        if (order.getServiceTotalValue() != 0) {
            tvServiceAmount.setText(((order.getServiceTotalValue() != 0) ? "+" : "") + decimalFormat.format(order.getServiceTotalValue()) + " " + currency.getAbbr());
            llServiceFeeGroup.setVisibility(View.VISIBLE);
        } else llServiceFeeGroup.setVisibility(View.GONE);


        tvTotal.setText(decimalFormat.format(order.getSubTotalValue() + order.getDiscountTotalValue() + order.getServiceTotalValue()) + " " + currency.getAbbr());
        double totalPayed = 0;
        for (PayedPartitions payedPartitions1 : payedPartitions) {
            totalPayed += payedPartitions1.getValue();
        }
        tvPayed.setText(decimalFormat.format(totalPayed) + " " + currency.getAbbr());
        double number = order.getForPayAmmount() - totalPayed;
        if (number < 0) {
            tvBalanceDueLabel.setText(getContext().getString(R.string.change_));
            tvBalanceDue.setText(decimalFormat.format(number * -1) + " " + currency.getAbbr());
            tvBalanceDue.setTextColor(Color.parseColor("#4ac21b"));
        } else {
            tvBalanceDueLabel.setText(getContext().getString(R.string.balance_due_));
            tvBalanceDue.setText(decimalFormat.format(number) + " " + currency.getAbbr());
            tvBalanceDue.setTextColor(Color.parseColor("#ff5e52"));

        }

        if (order.getTips() > 0) {
            llTipsGroup.setVisibility(View.VISIBLE);
            tvTips.setText(decimalFormat.format(order.getTips()) + " " + currency.getAbbr());
        } else {
            llTipsGroup.setVisibility(View.GONE);
        }

        if (customer != null) {
            tvCustomerName.setText(customer.getName());
            lbChooseCustomer.setImage(R.drawable.cancel_customer);
        } else {
            tvCustomerName.setText(getContext().getString(R.string.select_customer));
            lbChooseCustomer.setImage(R.drawable.add_customer);

        }
        presenter.sendToPaymentFragmentOrderAndPaymentsList();

    }

    @Override
    public void scrollToPosition(int pos) {
        rvOrderProducts.scrollToPosition(pos);


    }

    @Override
    public void onDetach() {
        mainPageConnection.setOrderListView(null);
        RxBus.removeListners(subscriptions);
        barcodeStack.unregister();
        super.onDetach();
    }

    @Override
    public void visibleBackButton() {
        tvPay.setText(getContext().getString(R.string.back));
        tvPay.setTextColor(Color.parseColor("#88898c"));
        ivPay.setImageResource(R.drawable.ic_back_left_arrow);

    }

    @Override
    public void onPayedPartition() {
        presenter.onPayedPartition();
    }

    @Override
    public void visiblePayButton() {
        tvPay.setText(getContext().getString(R.string.pay));
        tvPay.setTextColor(Color.parseColor("#419fd9"));
        ivPay.setImageResource(R.drawable.ic_dollar_symbol);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBarcodeScan(String barcode) {
        super.onBarcodeScan(barcode);
    }

    public void historyOpened() {

    }

    @Override
    public void setOrderNumberToToolbar(Long orderNumber) {
        ((MainPosPageActivity) getActivity()).updateIndicator(orderNumber);
    }


    @Override
    public void fistufulCloseOrder() {
        WarningDialog warningDialog = new WarningDialog(getActivity());
        warningDialog.setWarningMessage(getContext().getString(R.string.are_you_sure_cancel_order));
        warningDialog.setOnYesClickListener(view1 -> {
            warningDialog.dismiss();
            presenter.cleanOrder();
            //TODO
//            ((MainPosPageActivity)getActivity()).showOrderListHistoryFragment();
        });
        warningDialog.setOnNoClickListener(view1 -> {
            warningDialog.dismiss();
        });
        warningDialog.setPositiveButtonText(getContext().getString(R.string.discard));
        warningDialog.setNegativeButtonText(getContext().getString(R.string.cancel));
        warningDialog.show();
    }


    @Override
    public void onNewOrderPaymentFragment() {
        mainPageConnection.onNewOrderPaymentFragment();
    }

    @Override
    public void onEditOrder(String reason, Order order, Long newOrderId) {
        presenter.onEditOrder(reason, order, newOrderId);
    }


    @Override
    public void onHoldOrderSendingData(Order order, List<PayedPartitions> payedPartitions, Debt debt) {
        presenter.onHoldOrderSendingData(order, payedPartitions, debt);
    }

    @Override
    public void orderAdded(Order order) {
        checkOrder(order, databaseManager, preferencesHelper);
        ((MainPosPageActivity) getActivity()).orderAdded(order);
    }


    @Override
    public void holdOrderClosed(Order order) {
        checkOrder(order, databaseManager, preferencesHelper);
        ((MainPosPageActivity) getActivity()).holdOrderClosed(order);
    }

    @Override
    public void newOrderHolded(Order order) {
        ((MainPosPageActivity) getActivity()).newOrderHolded(order);
    }

    @Override
    public void holdOrderHolded(Order order) {
        ((MainPosPageActivity) getActivity()).holdOrderHolded(order);
    }

    @Override
    public void editedOrderHolded(String reason, Order order) {
        ((MainPosPageActivity) getActivity()).editedOrderHolded(reason, order);
    }

    @Override
    public void openWarningDialog(String text) {
        UIUtils.showAlert(getContext(), getContext().getString(R.string.ok), getContext().getString(R.string.warning), text, () -> {
        });
    }

    @Override
    public void hideProductInfoFragment() {
        currentPosition = -1;
        ((MainPosPageActivity) getActivity()).hideProductInfoFragment();
    }

    @Override
    public void checkOrder(Order order, DatabaseManager databaseManager, PreferencesHelper preferencesHelper) {
        ((MainPosPageActivity) getActivity()).checkOrder(order, databaseManager, preferencesHelper);
    }

    @Override
    public void stockCheckOrder(long tillId, long orderNumber, long now, List<OrderProductItem> orderProducts, Customer customer) {
        ((MainPosPageActivity) getActivity()).stockCheckOrder(tillId, orderNumber, now, orderProducts, customer, databaseManager, preferencesHelper);
    }

    @Override
    public void choiseOneProduct(List<Product> products) {
        vendorItemsListAdapter.setData(products);
        vendorItemsListAdapter.notifyDataSetChanged();
        dialog = new Dialog(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.vendor_product_list_dialog, null, false);
        RecyclerView rvProductList = dialogView.findViewById(R.id.rvProductList);
        rvProductList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProductList.setAdapter(vendorItemsListAdapter);
        TextView textView = dialogView.findViewById(R.id.tvDialogTitle);
        textView.setText(getContext().getString(R.string.found_products));
        dialog.setContentView(dialogView);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        vendorItemsListAdapter.setListener(product -> {
            presenter.addProductToList(product.getId());
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void sureCancel() {

        WarningDialog warningDialog = new WarningDialog(getActivity());
        warningDialog.setWarningMessage(getContext().getString(R.string.are_you_sure_cancel_order));
        warningDialog.setOnYesClickListener(view1 -> {
            warningDialog.dismiss();
            presenter.cleanOrder();
            mainPageConnection.onNewOrderPaymentFragment();
            ((MainPosPageActivity) getActivity()).hideProductInfoFragment();
        });
        warningDialog.setOnNoClickListener(view1 -> {
            warningDialog.dismiss();
        });
        warningDialog.setPositiveButtonText(getString(R.string.yes));
        warningDialog.setNegativeButtonText(getString(R.string.cancel));
        warningDialog.show();
    }

    @Override
    public void onStockPositionClicked() {
        presenter.onStockPositionClicked(currentPosition);
    }

    @Override
    public void openStockPositionDialog(OutcomeProduct outcomeProduct, List<OutcomeProduct> outcomeProductList, List<OutcomeProduct> exceptionList) {
        StockPositionsDialog stockPositionsDialog = new StockPositionsDialog(getContext(), outcomeProduct, outcomeProductList, exceptionList, databaseManager);
        stockPositionsDialog.setListener(new StockPositionsDialog.OnStockPositionsChanged() {
            @Override
            public void onConfirm(OutcomeProduct outcomeProduct) {
                presenter.updateOutcomeProductFor(currentPosition, outcomeProduct);
            }
        });
        stockPositionsDialog.show();
    }

    @Override
    public void stockOut() {
        WarningDialog warningDialog = new WarningDialog(getActivity());
        warningDialog.onlyText(true);
        warningDialog.setDialogTitle(getString(R.string.warning));
        warningDialog.setWarningMessage(getString(R.string.product_stock_outed));
        warningDialog.setOnYesClickListener(view1 -> {
            warningDialog.dismiss();
        });
        warningDialog.setPositiveButtonText(getString(R.string.ok));
        warningDialog.show();
    }

    @Override
    public void stockOutTillCloseOrder() {
        WarningDialog warningDialog = new WarningDialog(getActivity());
        warningDialog.setWarningMessage(getString(R.string.this_product_till_close_order_stock_outed));
        warningDialog.onlyText(true);
        warningDialog.setDialogTitle(getString(R.string.stock_out));
        warningDialog.setOnYesClickListener(view1 -> {
            warningDialog.dismiss();

        });
        warningDialog.setPositiveButtonText(getString(R.string.ok));
        warningDialog.show();
    }


    @Override
    public void onEditComplete(String reason, Order order) {
        checkOrder(order, databaseManager, preferencesHelper);
        ((MainPosPageActivity) getActivity()).onEditComplete(reason, order);
    }

    public void initNewOrderWithNumber(Long orderId) {
        presenter.initNewOrderWithId(orderId);
    }

    public void onHoldOrderCountined(Order order) {
        presenter.onHoldOrderCountined(order);
    }
}
