package com.jim.multipos.ui.mainpospage.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.prefs.PreferencesHelper;
import com.jim.multipos.ui.mainpospage.dialogs.DiscountDialog;
import com.jim.multipos.ui.mainpospage.dialogs.ServiceFeeDialog;
import com.jim.multipos.ui.mainpospage.model.OrderProductItem;

import java.util.List;

/**
 * Created by Portable-Acer on 27.10.2017.
 */

public interface OrderListView extends BaseView {
    void initOrderList(List<Object> objectList);

    void notifyList();

    void notifyItemAdded(int adapterPosition, int listSize, int[] extraPositionsForUpdate);

    void notifyItemRemove(int adapterPosition, int listSize, int[] extraPositionsForUpdate);

    void notifyItemChanged(int adapterPosition, int listSize, int[] extraPositionsForUpdate);

    void openProductInfoFragment(OrderProductItem orderProductItem, int position);

    void sendToProductInfoProductItem();

    void plusProductCount();

    void minusProductCount();

    void updateOrderDetials(Order order, Customer customer, List<PayedPartitions> payedPartitions);

    void scrollToPosition(int pos);

    void addProductToOrder(Long productId);

    void addDiscountToProduct(Long productId, Discount discount, boolean isManual);

    void addServiceFeeToProduct(Long productId, ServiceFee serviceFee, boolean isManual);

    void openDiscountDialog(DiscountDialog.CallbackDiscountDialog callbackDiscountDialog, double orginalAmount);

    void openSericeFeeDialog(ServiceFeeDialog.CallbackServiceFeeDialog callbackServiceFeeDialog, double orginalAmount);

    void disableDiscountButton(String discountName);

    void enableDiscountButton();

    void disableServiceFeeButton(String serviceFeeName);

    void enableServiceFeeButton();

    void setCount(double count);

    void changeDiscription(String discription);

    void removeOrderProducts();

    void setDiscountToProduct(Discount discountToProduct);

    void setServiceFeeProduct(ServiceFee serviceFeeProduct);

    void changeCustomer(Customer customer);

    void openCustomerDialog();

    void openUnitValuePicker(Product product);

    void openUnitValuePickerEdit(Product product, double weight);

    void addProductWithWeightToListEdit(double weight);

    void sendToPaymentFragmentOrderAndPaymentsList();

    void sendDataToPaymentFragment(Order order, List<PayedPartitions> payedPartitions);

    void sendDataToPaymentFragmentWhenEdit(Order order, List<PayedPartitions> payedPartitions, Debt debt);

    void visiblePayButton();

    void visibleBackButton();

    void onPayedPartition();

    void initScan();

    void isPaymentOpen();

    void isPaymentClose();

    void onCloseOrder(Order order, List<PayedPartitions> payedPartitions, Debt debt);

    void updateCustomer(Customer customer);

    void scanBarcodeFor(boolean fromAddCustomer);

    void updateViewCustomer(Customer customer);

    void sendCustomerToPaymentFragment(Customer customer);

    void setOrderNumberToToolbar(Long orderNumber);

    void fistufulCloseOrder();

    void onNewOrderPaymentFragment();

    void onEditOrder(String reason, Order order, Long newOrderId);

    void onEditComplete(String reason, Order order);

    void onHoldOrderSendingData(Order order, List<PayedPartitions> payedPartitions, Debt debt);

    void orderAdded(Order order);

    void holdOrderClosed(Order order);

    void newOrderHolded(Order order);

    void holdOrderHolded(Order order);

    void editedOrderHolded(String reason, Order order);

    void openWarningDialog(String text);

    void hideProductInfoFragment();

    void checkOrder(Order order, DatabaseManager databaseManager, PreferencesHelper preferencesHelper);

    void stockCheckOrder(long tillId, long orderNumber, long now, List<OrderProductItem> orderProducts, Customer customer);

    void choiseOneProduct(List<Product> products);

    void sureCancel();

    void onStockPositionClicked();

    void openStockPositionDialog(OutcomeProduct outcomeProduct, List<OutcomeProduct> outcomeProductList, List<OutcomeProduct> exceptionList);

    void stockOut();

    void stockOutTillCloseOrder();
}
