package com.jim.multipos.ui.mainpospage.presenter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.jim.multipos.R;
import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.DiscountLog;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.ServiceFeeLog;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.customer.Debt;
import com.jim.multipos.data.db.model.intosystem.StockResult;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.OrderChangesLog;
import com.jim.multipos.data.db.model.order.OrderProduct;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.unit.UnitCategory;
import com.jim.multipos.ui.mainpospage.dialogs.DiscountDialog;
import com.jim.multipos.ui.mainpospage.dialogs.ServiceFeeDialog;
import com.jim.multipos.ui.mainpospage.model.DiscountItem;
import com.jim.multipos.ui.mainpospage.model.OrderProductItem;
import com.jim.multipos.ui.mainpospage.model.ServiceFeeItem;
import com.jim.multipos.ui.mainpospage.view.OrderListFragment;
import com.jim.multipos.ui.mainpospage.view.OrderListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;


/**
 * Created by Portable-Acer on 27.10.2017.
 */

public class OrderListPresenterImpl extends BasePresenterImpl<OrderListView> implements OrderListPresenter {
    private DatabaseManager databaseManager;
    private Order order;
    List<Object> list;
    DiscountItem discountItem;
    ServiceFeeItem serviceFeeItem;
    Customer customer;
    List<PayedPartitions> payedPartitions;
    private Context context;
    HashMap<Long, Double> summaryOrderCount;

    @Inject
    public OrderListPresenterImpl(OrderListView orderListView, DatabaseManager databaseManager, Context context) {
        super(orderListView);
        this.context = context;
        list = new ArrayList<>();
        order = new Order();
        payedPartitions = new ArrayList<>();
        summaryOrderCount = new HashMap<>();
        this.databaseManager = databaseManager;
        fromEdit = false;
        fromHold = false;
    }

    @Override
    public List<Discount> getDiscounts() {
        return databaseManager.getAllDiscounts().blockingGet();
    }

    @Override
    public List<ServiceFee> getServiceFees() {
        return databaseManager.getServiceFeeOperations().getAllServiceFees().blockingSingle();
    }

    @Override
    public void onCreateView(Bundle bundle) {
        super.onCreateView(bundle);
        view.initOrderList(list);
        if (bundle != null) {
            initNewOrder();
            long newOrderId = bundle.getLong(OrderListFragment.NEW_ORDER_ID);
            view.setOrderNumberToToolbar(newOrderId);
        } else {
            view.updateOrderDetials(order, customer, payedPartitions);
            updateDetials();
        }
    }

    @Override
    public void addDiscount(double amount, String description, int amountType) {
        Discount discount = new Discount();
        discount.setAmount(amount);
        discount.setName(description);
        discount.setAmountType(amountType);
        discount.setUsedType(Discount.ORDER);
        discount.setCreatedDate(System.currentTimeMillis());
        discount.setDelete(false);
        databaseManager.insertDiscount(discount).subscribe(discount1 -> {
            DiscountLog discountLog = new DiscountLog();
            discountLog.setChangeDate(System.currentTimeMillis());
            discountLog.setDiscount(discount1);
            discountLog.setStatus(DiscountLog.DISCOUNT_ADDED);
            databaseManager.insertDiscountLog(discountLog).subscribe();
        });
    }

    @Override
    public void addServiceFee(double amount, String description, int amountType) {
        ServiceFee serviceFee = new ServiceFee();
        serviceFee.setAmount(amount);
        serviceFee.setName(description);
        serviceFee.setType(amountType);
        serviceFee.setApplyingType(Discount.ORDER);
        serviceFee.setCreatedDate(System.currentTimeMillis());
        serviceFee.setDeleted(false);
        databaseManager.getServiceFeeOperations().addServiceFee(serviceFee).subscribe(serviceFee1 -> {
            ServiceFeeLog serviceFeeLog = new ServiceFeeLog();
            serviceFeeLog.setServiceFee(serviceFee1);
            serviceFeeLog.setChangeDate(System.currentTimeMillis());
            serviceFeeLog.setStatus(ServiceFeeLog.SERVICE_FEE_ADDED);
            databaseManager.insertServiceFeeLog(serviceFeeLog).subscribe();
        });
    }

    @Override
    public void onPlusCount(int position) {
        OrderProductItem orderProductItem = (OrderProductItem) list.get(position);
        double neededCount = (summaryOrderCount.get(orderProductItem.getOrderProduct().getProductId()) == null ? 0 : summaryOrderCount.get(orderProductItem.getOrderProduct().getProductId())) + 1;
        if (databaseManager.checkProductAvailable(orderProductItem.getOrderProduct().getProductId(), neededCount, oldOrder) == StockResult.STOCK_OK) {
            orderProductItem.getOutcomeProduct().setCustomPickSock(false);
            orderProductItem.getOutcomeProduct().setPickedStockQueueId(0l);
            orderProductItem.getOutcomeProduct().setSumCountValue(orderProductItem.getOutcomeProduct().getSumCountValue() + 1);
            list.set(position, orderProductItem);
            updateDetials();
            view.updateOrderDetials(order, customer, payedPartitions);
            view.notifyItemChanged(position, list.size(), updateOrderDiscountServiceFee());
            view.sendToProductInfoProductItem();
        } else {
            view.stockOut();
        }
    }

    @Override
    public void onMinusCount(int position) {
        OrderProductItem orderProductItem = (OrderProductItem) list.get(position);
        if (orderProductItem.getOutcomeProduct().getSumCountValue() < 1) return;
        orderProductItem.getOutcomeProduct().setCustomPickSock(false);
        orderProductItem.getOutcomeProduct().setPickedStockQueueId(0l);
        orderProductItem.getOutcomeProduct().setSumCountValue(orderProductItem.getOutcomeProduct().getSumCountValue() - 1);
        list.set(position, orderProductItem);
        updateDetials();
        view.updateOrderDetials(order, customer, payedPartitions);
        view.notifyItemChanged(position, list.size(), updateOrderDiscountServiceFee());
        view.sendToProductInfoProductItem();
    }

    @Override
    public void setCount(int position, double count) {
        OrderProductItem orderProductItem = (OrderProductItem) list.get(position);
        double neededCount = (summaryOrderCount.get(orderProductItem.getOrderProduct().getProductId()) == null ? 0 : summaryOrderCount.get(orderProductItem.getOrderProduct().getProductId())) - orderProductItem.getOutcomeProduct().getSumCountValue() + count;

        if (databaseManager.checkProductAvailable(orderProductItem.getOrderProduct().getProductId(), neededCount, oldOrder) == StockResult.STOCK_OK) {
            orderProductItem.getOutcomeProduct().setCustomPickSock(false);
            orderProductItem.getOutcomeProduct().setPickedStockQueueId(0l);
            orderProductItem.getOutcomeProduct().setSumCountValue(count);
            list.set(position, orderProductItem);
            updateDetials();
            view.updateOrderDetials(order, customer, payedPartitions);
            view.notifyItemChanged(position, list.size(), updateOrderDiscountServiceFee());
        } else {
            view.stockOut();
        }
    }

    @Override
    public void onOrderProductClick(int position) {
        OrderProductItem orderProductItem = (OrderProductItem) list.get(position);
        view.openProductInfoFragment(orderProductItem, position);
    }

    @Override
    public void onOrderDiscountClick() {

    }

    @Override
    public void onOrderServiceFeeClick() {

    }

    @Override
    public void addProductToList(Long productId) {
        Product product = databaseManager.getProductById(productId).blockingGet();
        if (product.getMainUnit().getUnitCategory().getUnitType() != UnitCategory.PIECE) {
            view.openUnitValuePicker(product);
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof OrderProductItem) {
                OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                if (orderProductItem.getOrderProduct().getProduct().getId().equals(productId)) {
                    if (orderProductItem.getDiscount() == null && orderProductItem.getServiceFee() == null && !orderProductItem.getOutcomeProduct().getCustomPickSock()) {
                        double neededCount = (summaryOrderCount.get(orderProductItem.getOrderProduct().getProductId()) == null ? 0 : summaryOrderCount.get(orderProductItem.getOrderProduct().getProductId())) + 1;
                        if (databaseManager.checkProductAvailable(orderProductItem.getOrderProduct().getProductId(), neededCount, oldOrder) == StockResult.STOCK_OK) {
                            orderProductItem.getOutcomeProduct().setCustomPickSock(false);
                            orderProductItem.getOutcomeProduct().setPickedStockQueueId(0l);
                            orderProductItem.getOutcomeProduct().setSumCountValue(orderProductItem.getOutcomeProduct().getSumCountValue() + 1);
                            list.set(i, orderProductItem);
                            updateDetials();
                            view.updateOrderDetials(order, customer, payedPartitions);
                            view.notifyItemChanged(i, list.size(), updateOrderDiscountServiceFee());
                            return;
                        } else {
                            view.stockOut();
                        }
                    }
                }
            }
        }
        double neededCount = (summaryOrderCount.get(productId) == null ? 0 : summaryOrderCount.get(productId)) + 1;
        if (databaseManager.checkProductAvailable(productId, neededCount, oldOrder) == StockResult.STOCK_OK) {
            OutcomeProduct outcomeProduct = new OutcomeProduct();
            outcomeProduct.setCustomPickSock(false);
            outcomeProduct.setOutcomeDate(System.currentTimeMillis());
            outcomeProduct.setOutcomeType(OutcomeProduct.ORDER_SALES);
            outcomeProduct.setSumCountValue(1d);
            outcomeProduct.setProductId(productId);
            outcomeProduct.setProduct(product);
            OrderProductItem orderProductItem = new OrderProductItem();
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setPrice(product.getPrice());
            orderProduct.setProduct(product);
            orderProductItem.setOrderProduct(orderProduct);
            orderProductItem.setOutcomeProduct(outcomeProduct);
            int positionToAdd = findPositionToAdd();
            list.add(positionToAdd, orderProductItem);
            updateDetials();
            view.updateOrderDetials(order, customer, payedPartitions);
            view.notifyItemAdded(positionToAdd, list.size(), updateOrderDiscountServiceFee());
        } else {
            view.stockOut();
        }
    }

    @Override
    public void addProductWithWeightToList(Product product, double weight) {
        double neededCount = (summaryOrderCount.get(product.getId()) == null ? 0 : summaryOrderCount.get(product.getId())) + weight;
        if (databaseManager.checkProductAvailable(product.getId(), neededCount, oldOrder) == StockResult.STOCK_OK) {
            OutcomeProduct outcomeProduct = new OutcomeProduct();
            outcomeProduct.setCustomPickSock(false);
            outcomeProduct.setOutcomeDate(System.currentTimeMillis());
            outcomeProduct.setOutcomeType(OutcomeProduct.ORDER_SALES);
            outcomeProduct.setSumCountValue(weight);
            outcomeProduct.setProductId(product.getId());
            outcomeProduct.setProduct(product);
            OrderProductItem orderProductItem = new OrderProductItem();
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setPrice(product.getPrice());
            orderProduct.setProduct(product);
            orderProductItem.setOrderProduct(orderProduct);
            orderProductItem.setOutcomeProduct(outcomeProduct);
            int positionToAdd = findPositionToAdd();
            list.add(positionToAdd, orderProductItem);
            updateDetials();
            view.updateOrderDetials(order, customer, payedPartitions);
            view.notifyItemAdded(positionToAdd, list.size(), updateOrderDiscountServiceFee());
        } else {
            view.stockOut();
        }

    }

    @Override
    public void addDiscountToProduct(Long productId, Discount discount, boolean isManual) {

    }

    @Override
    public void addServiceFeeToProduct(Long productId, ServiceFee serviceFee, boolean isManual) {

    }


    @Override
    public void openDiscountDialog() {
        if (isEmptyOrder()) {
            view.openWarningDialog(context.getString(R.string.for_empty_order_cant_use_discount));
            return;
        }
        if (discountItem != null) {
            int q = -1;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof DiscountItem) {
                    list.remove(i);
                    q = i;
                    break;
                }
            }
            if (q != -1) {
                discountItem = null;
                updateDetials();
                view.updateOrderDetials(order, customer, payedPartitions);
                view.notifyItemRemove(q, list.size(), updateOrderDiscountServiceFee());
                view.enableDiscountButton();
            } else {
                new Throwable("DISCOUNT NOT FOUND").printStackTrace();
            }
        } else {
            DiscountDialog.CallbackDiscountDialog callbackDiscountDialog = new DiscountDialog.CallbackDiscountDialog() {
                @Override
                public void choiseStaticDiscount(Discount discount) {
                    discountItem = new DiscountItem();
                    discountItem.setDiscount(discount);
                    list.add(discountItem);
                    view.disableDiscountButton(discount.getName());
                    updateDetials();
                    view.updateOrderDetials(order, customer, payedPartitions);
                    view.notifyItemAdded(list.size() - 1, list.size(), updateOrderDiscountServiceFee());

                }

                @Override
                public void choiseManualDiscount(Discount discount) {
                    discountItem = new DiscountItem();
                    discountItem.setDiscount(discount);
                    list.add(discountItem);
                    view.disableDiscountButton(discount.getName());
                    updateDetials();
                    view.updateOrderDetials(order, customer, payedPartitions);
                    view.notifyItemAdded(list.size() - 1, list.size(), updateOrderDiscountServiceFee());

                }
            };
            view.openDiscountDialog(callbackDiscountDialog, order.getSubTotalValue() + order.getDiscountTotalValue() + order.getServiceTotalValue());
        }

    }

    @Override
    public void openSeriveFeeDialog() {
        if (isEmptyOrder()) {
            view.openWarningDialog(context.getString(R.string.for_empty_order_cant_use_service_fee));
            return;
        }
        if (serviceFeeItem != null) {
            int q = -1;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof ServiceFeeItem) {
                    list.remove(i);
                    q = i;
                    break;
                }
            }
            if (q != -1) {
                serviceFeeItem = null;
                updateDetials();
                view.updateOrderDetials(order, customer, payedPartitions);
                view.notifyItemRemove(q, list.size(), updateOrderDiscountServiceFee());
                view.enableServiceFeeButton();
            } else {
                new Throwable("DISCOUNT NOT FOUND").printStackTrace();
            }
        } else {
            ServiceFeeDialog.CallbackServiceFeeDialog callbackServiceFeeDialog = new ServiceFeeDialog.CallbackServiceFeeDialog() {
                @Override
                public void choiseStaticServiceFee(ServiceFee serviceFee) {
                    serviceFeeItem = new ServiceFeeItem();
                    serviceFeeItem.setServiceFee(serviceFee);
                    list.add(serviceFeeItem);
                    view.disableServiceFeeButton(serviceFee.getName());
                    updateDetials();
                    view.updateOrderDetials(order, customer, payedPartitions);
                    view.notifyItemAdded(list.size() - 1, list.size(), updateOrderDiscountServiceFee());
                }

                @Override
                public void choiseManualServiceFee(ServiceFee serviceFee) {
                    serviceFeeItem = new ServiceFeeItem();
                    serviceFeeItem.setServiceFee(serviceFee);
                    list.add(serviceFeeItem);
                    view.disableServiceFeeButton(serviceFee.getName());
                    updateDetials();
                    view.updateOrderDetials(order, customer, payedPartitions);
                    view.notifyItemAdded(list.size() - 1, list.size(), updateOrderDiscountServiceFee());
                }
            };
            view.openSericeFeeDialog(callbackServiceFeeDialog, order.getSubTotalValue() + order.getDiscountTotalValue() + order.getServiceTotalValue());
        }
    }

    @Override
    public void changeDiscription(String discription, int position) {
        if (position == -1) return;
        OrderProductItem orderProductItem = (OrderProductItem) list.get(position);
        orderProductItem.getOrderProduct().setDiscription(discription);
    }

    @Override
    public void removeOrderProducts(int removePosition) {
        list.remove(removePosition);
        if (!isListHaveProducts()) {
            cleanOrder();
        } else {
            updateDetials();
            view.updateOrderDetials(order, customer, payedPartitions);
            view.notifyItemRemove(removePosition, list.size(), updateOrderDiscountServiceFee());
        }

    }

    private boolean isListHaveProducts() {
        if (list.size() == 0) {
            return false;
        }
        int productCount = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof OrderProductItem) {
                productCount++;
            }
        }
        if (productCount == 0) return false;
        else return true;
    }

    @Override
    public void setDiscountToProduct(Discount discountToProduct, int currentPosition) {
        OrderProductItem orderProductItem = (OrderProductItem) list.get(currentPosition);
        orderProductItem.setDiscount(discountToProduct);
        list.set(currentPosition, orderProductItem);
        updateDetials();
        view.updateOrderDetials(order, customer, payedPartitions);
        view.notifyItemChanged(currentPosition, list.size(), updateOrderDiscountServiceFee());
    }

    @Override
    public void setServiceFeeProduct(ServiceFee serviceFeeProduct, int currentPosition) {
        OrderProductItem orderProductItem = (OrderProductItem) list.get(currentPosition);
        orderProductItem.setServiceFee(serviceFeeProduct);
        list.set(currentPosition, orderProductItem);
        updateDetials();
        view.updateOrderDetials(order, customer, payedPartitions);
        view.notifyItemChanged(currentPosition, list.size(), updateOrderDiscountServiceFee());
    }

    @Override
    public void changeCustomer(Customer customer) {
        this.customer = customer;
        view.updateOrderDetials(order, customer, payedPartitions);
    }

    @Override
    public void onClickChooseCustomerButton() {
        if (customer != null) {
            customer = null;
            view.updateOrderDetials(order, customer, payedPartitions);
            view.sendCustomerToPaymentFragment(customer);
        } else {
            view.openCustomerDialog();

        }
    }

    @Override
    public void addProductWithWeightToListEdit(Product product, double weight) {
        OrderProductItem orderProductItem = (OrderProductItem) list.get(positionOfWeightItem);
        double neededCount = (summaryOrderCount.get(product.getId()) == null ? 0 : summaryOrderCount.get(product.getId())) - orderProductItem.getOutcomeProduct().getSumCountValue() + weight;

        if (databaseManager.checkProductAvailable(product.getId(), neededCount, oldOrder) == StockResult.STOCK_OK) {
            orderProductItem.getOutcomeProduct().setCustomPickSock(false);
            orderProductItem.getOutcomeProduct().setPickedStockQueueId(0l);
            orderProductItem.getOutcomeProduct().setSumCountValue(weight);
            list.set(positionOfWeightItem, orderProductItem);
            updateDetials();
            view.updateOrderDetials(order, customer, payedPartitions);
            view.notifyItemChanged(positionOfWeightItem, list.size(), updateOrderDiscountServiceFee());
        } else {
            view.stockOut();
        }
    }

    @Override
    public void addProductWithWeightToListEditFromInfo(int currentPosition, double weight) {
        OrderProductItem orderProductItem = (OrderProductItem) list.get(currentPosition);
        double neededCount = (summaryOrderCount.get(orderProductItem.getOrderProduct().getProductId()) == null ? 0 : summaryOrderCount.get(summaryOrderCount.get(orderProductItem.getOrderProduct().getProductId()))) - orderProductItem.getOutcomeProduct().getSumCountValue() + weight;

        if (databaseManager.checkProductAvailable(orderProductItem.getOrderProduct().getId(), neededCount, oldOrder) == StockResult.STOCK_OK) {
            orderProductItem.getOutcomeProduct().setCustomPickSock(false);
            orderProductItem.getOutcomeProduct().setPickedStockQueueId(0l);
            orderProductItem.getOutcomeProduct().setSumCountValue(weight);
            list.set(currentPosition, orderProductItem);
            updateDetials();
            view.updateOrderDetials(order, customer, payedPartitions);
            view.notifyItemChanged(currentPosition, list.size(), updateOrderDiscountServiceFee());
        } else {
            view.stockOut();
        }
    }

    int positionOfWeightItem = -1;

    @Override
    public void onCountWeigtClick(int position) {
        positionOfWeightItem = position;
        OrderProductItem orderProductItem = (OrderProductItem) list.get(position);
        view.openUnitValuePickerEdit(orderProductItem.getOrderProduct().getProduct(), orderProductItem.getOutcomeProduct().getSumCountValue());
    }

    @Override
    public void sendToPaymentFragmentOrderAndPaymentsList() {
        view.sendDataToPaymentFragment(order, payedPartitions);
    }

    @Override
    public void onPayedPartition() {
        view.updateOrderDetials(order, customer, payedPartitions);
    }

    @Override
    public void cleanOrder() {
        // agar clean knopkasini bosganda edit rejim otmena bo'lishi mumkun bo'lsa unda: fromEdit = false
        // agar otmena bomasdan shunchaki polotno clean bolishi kerak bolsa unda: fromEdit ni o'chirib yuborish kerak
        fromEdit = false;
        fromHold = false;
        payedPartitions.clear();
        list.clear();
        discountItem = null;
        serviceFeeItem = null;
        customer = null;
        view.enableServiceFeeButton();
        view.enableDiscountButton();
        updateDetials();
        view.updateOrderDetials(order, customer, payedPartitions);
        view.notifyList();
    }

    List<OrderProduct> orderProducts;


    /**
     * onCloseOrder(): ushbu metod yangi orderni sozdat qilib yangi ID bilan bazaga qo'shish
     **/
    List<OutcomeProduct> outcomeProducts;
    OrderChangesLog orderChangesLog = null;


    /**
     * I AM NOT SHIT CODER, BUT IN THIS PLACE LOGIC IS VERY DEFICULT FOR THAT FOR EVERY SITUATION CODE WROTE SEPARATELY
     * <p>
     * FROM:          RESULT:
     * Edit  --->
     * Hold  --->     CLOSE
     * New   --->
     */
    @Override
    public void onCloseOrder(Order order, List<PayedPartitions> payedPartitions, Debt debt) {
        if (fromEdit) {
            outcomeProducts = new ArrayList<>();
            final List<OrderProduct> oldOrderProducts = oldOrder.getOrderProducts();
            final List<OutcomeProduct> oldOutcomeProducts = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof OrderProductItem) {
                    OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                    outcomeProducts.add(orderProductItem.getOutcomeProduct());
                }
            }

            for (int i = 0; i < oldOrderProducts.size(); i++) {
                oldOutcomeProducts.add(oldOrderProducts.get(i).getOutcomeProduct());
            }

            databaseManager.checkPositionAvailablityWithoutSomeOutcomes(outcomeProducts, oldOutcomeProducts).subscribe(stockOutOutcomeProductsWithDetials -> {
                if (stockOutOutcomeProductsWithDetials.size() != 0) {

                    orderProducts = new ArrayList<>();

                    //COLLECTION ORDERPRODUCT AND REGISTER PRODUCT DISCOUNT AND SERVICE FEE
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) instanceof OrderProductItem) {
                            OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                            if (orderProductItem.getDiscount() != null) {
                                if (orderProductItem.getDiscount().getIsManual())
                                    databaseManager.insertDiscount(orderProductItem.getDiscount()).blockingGet();
                                orderProductItem.getOrderProduct().setDiscount(orderProductItem.getDiscount());
                                orderProductItem.getOrderProduct().setDiscountAmount(orderProductItem.getDiscountAmmount());
                            }
                            if (orderProductItem.getServiceFee() != null) {
                                if (orderProductItem.getServiceFee().getIsManual())
                                    databaseManager.addServiceFee(orderProductItem.getServiceFee()).blockingFirst();
                                orderProductItem.getOrderProduct().setServiceFee(orderProductItem.getServiceFee());
                                orderProductItem.getOrderProduct().setServiceAmount(orderProductItem.getServiceFeeAmmount());
                            }
                            orderProductItem.getOrderProduct().setCount(orderProductItem.getOutcomeProduct().getSumCountValue());
                            orderProducts.add(orderProductItem.getOrderProduct());
                        }
                    }

                    //ORDER DISCOUNT SAVE TO TABLE
                    if (discountItem != null && discountItem.getDiscount() != null) {
                        if (discountItem.getDiscount().getIsManual()) {
                            databaseManager.insertDiscount(discountItem.getDiscount()).subscribe(discount -> {
                                DiscountLog discountLog = new DiscountLog();
                                discountLog.setChangeDate(System.currentTimeMillis());
                                discountLog.setDiscount(discount);
                                discountLog.setStatus(DiscountLog.DISCOUNT_ADDED);
                                databaseManager.insertDiscountLog(discountLog).subscribe();
                            });
                        }
                        order.setDiscount(discountItem.getDiscount());
                        order.setDiscountAmount(discountItem.getAmmount());
                    }

                    //SERVICE DISCOUNT SAVE TO TABLE
                    if (serviceFeeItem != null && serviceFeeItem.getServiceFee() != null) {
                        if (serviceFeeItem.getServiceFee().getIsManual()) {
                            databaseManager.addServiceFee(serviceFeeItem.getServiceFee()).subscribe(serviceFee -> {
                                ServiceFeeLog serviceFeeLog = new ServiceFeeLog();
                                serviceFeeLog.setServiceFee(serviceFee);
                                serviceFeeLog.setChangeDate(System.currentTimeMillis());
                                serviceFeeLog.setStatus(ServiceFeeLog.SERVICE_FEE_ADDED);
                                databaseManager.insertServiceFeeLog(serviceFeeLog).subscribe();
                            });
                        }
                        order.setServiceFee(serviceFeeItem.getServiceFee());
                        order.setServiceAmount(serviceFeeItem.getAmmount());
                    }


                    if (customer != null)
                        order.setCustomer(customer);

                    if (debt != null)
                        order.setToDebtValue(debt.getDebtAmount());

                    order.setCreateAt(System.currentTimeMillis());

                    double totalPayedSum = 0;
                    for (PayedPartitions payedPartitions1 : payedPartitions) {
                        totalPayedSum += payedPartitions1.getValue();
                    }
                    order.setTotalPayed(totalPayedSum);

                    orderChangesLog = new OrderChangesLog();
                    orderChangesLog.setToStatus(Order.CLOSED_ORDER);
                    orderChangesLog.setChangedAt(System.currentTimeMillis());
                    orderChangesLog.setReason(reason);
                    orderChangesLog.setChangedCauseType(OrderChangesLog.EDITED);
                    orderChangesLog.setRelationshipOrderId(beforeOrderID);
                    databaseManager.insertOrderChangeLog(orderChangesLog).blockingGet();
                    order.setIsArchive(false);
                    order.setStatus(Order.CLOSED_ORDER);
                    //CANCEL OLD ORDER AND DELETE OUTCOME
                    cancelOldOrderWhenEditOrderHoldedOrClosed(oldOrder, beforeOrderID);

                    order.setLastChangeLogId(orderChangesLog.getId());

                    if (debt != null) {
                        databaseManager.addDebt(debt).blockingGet();
                        order.setDebtId(debt.getId());
                    }

                    order.setTillId(databaseManager.getCurrentOpenTillId().blockingGet());

                    databaseManager.insertOrder(order).subscribe((order1, throwable) -> {
                        for (int i = 0; i < orderProducts.size(); i++) {
                            orderProducts.get(i).setOrderId(order1.getId());
                        }

                        if (orderProducts.size() != stockOutOutcomeProductsWithDetials.size())
                            new Exception("SOME THINK IS WRONG!!! orderProductItems size should be similar with outcomeDetial Products").printStackTrace();

                        outcomeProducts = databaseManager.insertAndFillOutcomeProducts(stockOutOutcomeProductsWithDetials);

                        for (int i = 0; i < orderProducts.size(); i++) {
                            orderProducts.get(i).setOutcomeProductId(outcomeProducts.get(i).getId());
                        }

                        databaseManager.insertOrderProducts(orderProducts).subscribeOn(Schedulers.io()).subscribe(orderProducts1 -> {

                            for (int i = 0; i < orderProducts.size(); i++) {
                                outcomeProducts.get(i).setOrderProductId(orderProducts.get(i).getId());
                            }
                            databaseManager.updateOutcomeProduct(outcomeProducts).subscribe();

                        });

                        orderChangesLog.setOrderId(order1.getId());
                        databaseManager.insertOrderChangeLog(orderChangesLog).subscribeOn(Schedulers.io()).subscribe();

                        for (int i = 0; i < payedPartitions.size(); i++) {
                            payedPartitions.get(i).setOrderId(order1.getId());
                        }
                        databaseManager.insertPayedPartitions(payedPartitions).subscribeOn(Schedulers.io()).blockingGet();

                        if (debt != null) {
                            debt.setOrderId(order1.getId());
                            databaseManager.addDebt(debt).subscribeOn(Schedulers.io()).subscribe();
                        }
                        view.onEditComplete(reason, order);

                    });

                } else {
                    view.stockOutTillCloseOrder();
                    Log.d("PROBLEM", "stockOutOutComesSize HOLD ORDER -> CLOSE ORDER");
                }
            });
        } else if (fromHold) {
            outcomeProducts = new ArrayList<>();
            final List<OrderProduct> oldOrderProducts = order.getOrderProducts();
            final List<OutcomeProduct> oldOutcomeProducts = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof OrderProductItem) {
                    OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                    outcomeProducts.add(orderProductItem.getOutcomeProduct());
                }
            }

            for (int i = 0; i < oldOrderProducts.size(); i++) {
                oldOutcomeProducts.add(oldOrderProducts.get(i).getOutcomeProduct());
            }

            databaseManager.checkPositionAvailablityWithoutSomeOutcomes(outcomeProducts, oldOutcomeProducts).subscribe(stockOutOutcomeProductsWithDetials -> {
                if (stockOutOutcomeProductsWithDetials.size() != 0) {

                    orderProducts = new ArrayList<>();

                    //COLLECTION ORDERPRODUCT AND REGISTER PRODUCT DISCOUNT AND SERVICE FEE
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) instanceof OrderProductItem) {
                            OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                            if (orderProductItem.getDiscount() != null) {
                                if (orderProductItem.getDiscount().getIsManual())
                                    databaseManager.insertDiscount(orderProductItem.getDiscount()).blockingGet();
                                orderProductItem.getOrderProduct().setDiscount(orderProductItem.getDiscount());
                                orderProductItem.getOrderProduct().setDiscountAmount(orderProductItem.getDiscountAmmount());
                            }
                            if (orderProductItem.getServiceFee() != null) {
                                if (orderProductItem.getServiceFee().getIsManual())
                                    databaseManager.addServiceFee(orderProductItem.getServiceFee()).blockingFirst();
                                orderProductItem.getOrderProduct().setServiceFee(orderProductItem.getServiceFee());
                                orderProductItem.getOrderProduct().setServiceAmount(orderProductItem.getServiceFeeAmmount());
                            }
                            orderProductItem.getOrderProduct().setCount(orderProductItem.getOutcomeProduct().getSumCountValue());
                            orderProducts.add(orderProductItem.getOrderProduct());
                        }
                    }

                    //ORDER DISCOUNT SAVE TO TABLE
                    if (discountItem != null && discountItem.getDiscount() != null) {
                        if (discountItem.getDiscount().getIsManual()) {
                            databaseManager.insertDiscount(discountItem.getDiscount()).subscribe(discount -> {
                                DiscountLog discountLog = new DiscountLog();
                                discountLog.setChangeDate(System.currentTimeMillis());
                                discountLog.setDiscount(discount);
                                discountLog.setStatus(DiscountLog.DISCOUNT_ADDED);
                                databaseManager.insertDiscountLog(discountLog).subscribe();
                            });
                        }
                        order.setDiscount(discountItem.getDiscount());
                        order.setDiscountAmount(discountItem.getAmmount());
                    }

                    //SERVICE DISCOUNT SAVE TO TABLE
                    if (serviceFeeItem != null && serviceFeeItem.getServiceFee() != null) {
                        if (serviceFeeItem.getServiceFee().getIsManual()) {
                            databaseManager.addServiceFee(serviceFeeItem.getServiceFee()).subscribe(serviceFee -> {
                                ServiceFeeLog serviceFeeLog = new ServiceFeeLog();
                                serviceFeeLog.setServiceFee(serviceFee);
                                serviceFeeLog.setChangeDate(System.currentTimeMillis());
                                serviceFeeLog.setStatus(ServiceFeeLog.SERVICE_FEE_ADDED);
                                databaseManager.insertServiceFeeLog(serviceFeeLog).subscribe();
                            });
                        }
                        order.setServiceFee(serviceFeeItem.getServiceFee());
                        order.setServiceAmount(serviceFeeItem.getAmmount());
                    }


                    if (customer != null)
                        order.setCustomer(customer);

                    if (debt != null)
                        order.setToDebtValue(debt.getDebtAmount());

                    order.setCreateAt(System.currentTimeMillis());

                    double totalPayedSum = 0;
                    for (PayedPartitions payedPartitions1 : payedPartitions) {
                        totalPayedSum += payedPartitions1.getValue();
                    }
                    order.setTotalPayed(totalPayedSum);

                    orderChangesLog = new OrderChangesLog();
                    orderChangesLog.setToStatus(Order.CLOSED_ORDER);
                    orderChangesLog.setChangedAt(System.currentTimeMillis());
                    orderChangesLog.setReason("");
                    orderChangesLog.setChangedCauseType(OrderChangesLog.PAYED);
                    databaseManager.insertOrderChangeLog(orderChangesLog).blockingGet();

                    order.setStatus(Order.CLOSED_ORDER);
                    order.setIsArchive(false);
                    order.setLastChangeLogId(orderChangesLog.getId());


                    //RETURN TO STOCK AND DELETE OLD HOLDED ORDER PRODUCTS
                    databaseManager.deleteOrderProductsOnHold(oldOrderProducts).subscribe();
                    //DELETE PAYED PARTITIONS OLD HOLDED ORDER PRODUCTS
                    databaseManager.deletePayedPartitions(order.getPayedPartitions()).subscribe();
                    //DELETE DEBT OLD HOLDED ORDER PRODUCTS
                    if (order.getDebt() != null) {
                        databaseManager.deleteDebt(order.getDebt()).subscribe();
                    }

                    order.setLastChangeLogId(orderChangesLog.getId());

                    if (debt != null) {
                        databaseManager.addDebt(debt).blockingGet();
                        order.setDebtId(debt.getId());
                    }

                    order.setTillId(databaseManager.getCurrentOpenTillId().blockingGet());

                    databaseManager.insertOrder(order).subscribe((order1, throwable) -> {
                        for (int i = 0; i < orderProducts.size(); i++) {
                            orderProducts.get(i).setOrderId(order1.getId());
                        }

                        if (orderProducts.size() != stockOutOutcomeProductsWithDetials.size())
                            new Exception("SOME THINK IS WRONG!!! orderProductItems size should be similar with outcomeDetial Products").printStackTrace();

                        outcomeProducts = databaseManager.insertAndFillOutcomeProducts(stockOutOutcomeProductsWithDetials);

                        for (int i = 0; i < orderProducts.size(); i++) {
                            orderProducts.get(i).setOutcomeProductId(outcomeProducts.get(i).getId());
                        }

                        databaseManager.insertOrderProducts(orderProducts).subscribeOn(Schedulers.io()).subscribe(orderProducts1 -> {

                            for (int i = 0; i < orderProducts.size(); i++) {
                                outcomeProducts.get(i).setOrderProductId(orderProducts.get(i).getId());
                            }
                            databaseManager.updateOutcomeProduct(outcomeProducts).subscribe();

                        });

                        orderChangesLog.setOrderId(order1.getId());
                        databaseManager.insertOrderChangeLog(orderChangesLog).subscribeOn(Schedulers.io()).subscribe();

                        for (int i = 0; i < payedPartitions.size(); i++) {
                            payedPartitions.get(i).setOrderId(order1.getId());
                        }
                        databaseManager.insertPayedPartitions(payedPartitions).subscribeOn(Schedulers.io()).blockingGet();

                        if (debt != null) {
                            debt.setOrderId(order1.getId());
                            databaseManager.addDebt(debt).subscribeOn(Schedulers.io()).subscribe();
                        }
                        view.holdOrderClosed(order);
                    });

                } else {
                    view.stockOutTillCloseOrder();
                    Log.d("PROBLEM", "stockOutOutComesSize HOLD ORDER -> CLOSE ORDER");
                }
            });
        } else {
            outcomeProducts = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof OrderProductItem) {
                    OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                    outcomeProducts.add(orderProductItem.getOutcomeProduct());
                }
            }
            databaseManager.checkPositionAvailablity(outcomeProducts).subscribe(stockOutOutcomeProductsWithDetials -> {
                if (stockOutOutcomeProductsWithDetials.size() != 0) {

                    orderProducts = new ArrayList<>();

                    //COLLECTION ORDERPRODUCT AND REGISTER PRODUCT DISCOUNT AND SERVICE FEE
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) instanceof OrderProductItem) {
                            OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                            if (orderProductItem.getDiscount() != null) {
                                if (orderProductItem.getDiscount().getIsManual())
                                    databaseManager.insertDiscount(orderProductItem.getDiscount()).blockingGet();
                                orderProductItem.getOrderProduct().setDiscount(orderProductItem.getDiscount());
                                orderProductItem.getOrderProduct().setDiscountAmount(orderProductItem.getDiscountAmmount());
                            }
                            if (orderProductItem.getServiceFee() != null) {
                                if (orderProductItem.getServiceFee().getIsManual())
                                    databaseManager.addServiceFee(orderProductItem.getServiceFee()).blockingFirst();
                                orderProductItem.getOrderProduct().setServiceFee(orderProductItem.getServiceFee());
                                orderProductItem.getOrderProduct().setServiceAmount(orderProductItem.getServiceFeeAmmount());
                            }
                            orderProductItem.getOrderProduct().setCount(orderProductItem.getOutcomeProduct().getSumCountValue());
                            orderProducts.add(orderProductItem.getOrderProduct());
                        }
                    }

                    //ORDER DISCOUNT SAVE TO TABLE
                    if (discountItem != null && discountItem.getDiscount() != null) {
                        if (discountItem.getDiscount().getIsManual()) {
                            databaseManager.insertDiscount(discountItem.getDiscount()).subscribe(discount -> {
                                DiscountLog discountLog = new DiscountLog();
                                discountLog.setChangeDate(System.currentTimeMillis());
                                discountLog.setDiscount(discount);
                                discountLog.setStatus(DiscountLog.DISCOUNT_ADDED);
                                databaseManager.insertDiscountLog(discountLog).subscribe();
                            });
                        }
                        order.setDiscount(discountItem.getDiscount());
                        order.setDiscountAmount(discountItem.getAmmount());
                    }

                    //SERVICE DISCOUNT SAVE TO TABLE
                    if (serviceFeeItem != null && serviceFeeItem.getServiceFee() != null) {
                        if (serviceFeeItem.getServiceFee().getIsManual()) {
                            databaseManager.addServiceFee(serviceFeeItem.getServiceFee()).subscribe(serviceFee -> {
                                ServiceFeeLog serviceFeeLog = new ServiceFeeLog();
                                serviceFeeLog.setServiceFee(serviceFee);
                                serviceFeeLog.setChangeDate(System.currentTimeMillis());
                                serviceFeeLog.setStatus(ServiceFeeLog.SERVICE_FEE_ADDED);
                                databaseManager.insertServiceFeeLog(serviceFeeLog).subscribe();
                            });
                        }
                        order.setServiceFee(serviceFeeItem.getServiceFee());
                        order.setServiceAmount(serviceFeeItem.getAmmount());
                    }


                    if (customer != null)
                        order.setCustomer(customer);

                    if (debt != null)
                        order.setToDebtValue(debt.getDebtAmount());

                    order.setCreateAt(System.currentTimeMillis());

                    double totalPayedSum = 0;
                    for (PayedPartitions payedPartitions1 : payedPartitions) {
                        totalPayedSum += payedPartitions1.getValue();
                    }
                    order.setTotalPayed(totalPayedSum);

                    orderChangesLog = new OrderChangesLog();
                    orderChangesLog.setToStatus(Order.CLOSED_ORDER);
                    orderChangesLog.setChangedAt(System.currentTimeMillis());
                    orderChangesLog.setReason("");
                    orderChangesLog.setChangedCauseType(OrderChangesLog.PAYED);
                    order.setStatus(Order.CLOSED_ORDER);
                    order.setIsArchive(false);
                    databaseManager.insertOrderChangeLog(orderChangesLog).blockingGet();

                    order.setLastChangeLogId(orderChangesLog.getId());

                    if (debt != null) {
                        databaseManager.addDebt(debt).blockingGet();
                        order.setDebtId(debt.getId());
                    }

                    order.setTillId(databaseManager.getCurrentOpenTillId().blockingGet());

                    databaseManager.insertOrder(order).subscribe((order1, throwable) -> {
                        for (int i = 0; i < orderProducts.size(); i++) {
                            orderProducts.get(i).setOrderId(order1.getId());
                        }

                        if (orderProducts.size() != stockOutOutcomeProductsWithDetials.size())
                            new Exception("SOME THINK IS WRONG!!! orderProductItems size should be similar with outcomeDetial Products").printStackTrace();

                        outcomeProducts = databaseManager.insertAndFillOutcomeProducts(stockOutOutcomeProductsWithDetials);

                        for (int i = 0; i < orderProducts.size(); i++) {
                            orderProducts.get(i).setOutcomeProductId(outcomeProducts.get(i).getId());
                        }

                        databaseManager.insertOrderProducts(orderProducts).subscribeOn(Schedulers.io()).subscribe(orderProducts1 -> {
                            for (int i = 0; i < orderProducts.size(); i++) {
                                outcomeProducts.get(i).setOrderProductId(orderProducts.get(i).getId());
                            }
                            databaseManager.updateOutcomeProduct(outcomeProducts).subscribe();

                        });


                        orderChangesLog.setOrderId(order1.getId());
                        databaseManager.insertOrderChangeLog(orderChangesLog).subscribeOn(Schedulers.io()).subscribe();

                        for (int i = 0; i < payedPartitions.size(); i++) {
                            payedPartitions.get(i).setOrderId(order1.getId());
                        }
                        databaseManager.insertPayedPartitions(payedPartitions).subscribeOn(Schedulers.io()).blockingGet();

                        if (debt != null) {
                            debt.setOrderId(order1.getId());
                            databaseManager.addDebt(debt).subscribeOn(Schedulers.io()).subscribe();
                        }
                        view.orderAdded(order);
                    });

                } else {
                    view.stockOutTillCloseOrder();
                    Log.d("PROBLEM", "stockOutOutComesSize NEW ORDER -> CLOSE ORDER: ");
                }
            });

        }


    }

    /**
     * I AM NOT SHIT CODER, BUT IN THIS PLACE LOGIC IS VERY DEFICULT FOR THAT FOR EVERY SITUATION CODE WROTE SEPARATELY
     * <p>
     * FROM:          RESULT:
     * Edit  --->
     * Hold  --->     HOLD
     * New   --->
     */
    @Override
    public void onHoldOrderSendingData(Order order, List<PayedPartitions> payedPartitions, Debt debt) {

        boolean hasOpenTill = databaseManager.hasOpenTill().blockingGet();
        if (!hasOpenTill) {
            view.openWarningDialog(context.getString(R.string.opened_till_wnt_found_pls_open_till));
            return;
        }
        if (isEmptyOrder()) {
            view.openWarningDialog(context.getString(R.string.empty_till_cant_hold));
            return;
        }

        if (fromEdit) {
            outcomeProducts = new ArrayList<>();
            final List<OrderProduct> oldOrderProducts = oldOrder.getOrderProducts();
            final List<OutcomeProduct> oldOutcomeProducts = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof OrderProductItem) {
                    OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                    outcomeProducts.add(orderProductItem.getOutcomeProduct());
                }
            }

            for (int i = 0; i < oldOrderProducts.size(); i++) {
                oldOutcomeProducts.add(oldOrderProducts.get(i).getOutcomeProduct());
            }

            databaseManager.checkPositionAvailablityWithoutSomeOutcomes(outcomeProducts, oldOutcomeProducts).subscribe(stockOutOutcomeProductsWithDetials -> {
                if (stockOutOutcomeProductsWithDetials.size() != 0) {

                    orderProducts = new ArrayList<>();

                    //COLLECTION ORDERPRODUCT AND REGISTER PRODUCT DISCOUNT AND SERVICE FEE
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) instanceof OrderProductItem) {
                            OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                            if (orderProductItem.getDiscount() != null) {
                                if (orderProductItem.getDiscount().getIsManual())
                                    databaseManager.insertDiscount(orderProductItem.getDiscount()).blockingGet();
                                orderProductItem.getOrderProduct().setDiscount(orderProductItem.getDiscount());
                                orderProductItem.getOrderProduct().setDiscountAmount(orderProductItem.getDiscountAmmount());
                            }
                            if (orderProductItem.getServiceFee() != null) {
                                if (orderProductItem.getServiceFee().getIsManual())
                                    databaseManager.addServiceFee(orderProductItem.getServiceFee()).blockingFirst();
                                orderProductItem.getOrderProduct().setServiceFee(orderProductItem.getServiceFee());
                                orderProductItem.getOrderProduct().setServiceAmount(orderProductItem.getServiceFeeAmmount());
                            }
                            orderProductItem.getOrderProduct().setCount(orderProductItem.getOutcomeProduct().getSumCountValue());
                            orderProducts.add(orderProductItem.getOrderProduct());
                        }
                    }

                    //ORDER DISCOUNT SAVE TO TABLE
                    if (discountItem != null && discountItem.getDiscount() != null) {
                        if (discountItem.getDiscount().getIsManual()) {
                            databaseManager.insertDiscount(discountItem.getDiscount()).subscribe(discount -> {
                                DiscountLog discountLog = new DiscountLog();
                                discountLog.setChangeDate(System.currentTimeMillis());
                                discountLog.setDiscount(discount);
                                discountLog.setStatus(DiscountLog.DISCOUNT_ADDED);
                                databaseManager.insertDiscountLog(discountLog).subscribe();
                            });
                        }
                        order.setDiscount(discountItem.getDiscount());
                        order.setDiscountAmount(discountItem.getAmmount());
                    }

                    //SERVICE DISCOUNT SAVE TO TABLE
                    if (serviceFeeItem != null && serviceFeeItem.getServiceFee() != null) {
                        if (serviceFeeItem.getServiceFee().getIsManual()) {
                            databaseManager.addServiceFee(serviceFeeItem.getServiceFee()).subscribe(serviceFee -> {
                                ServiceFeeLog serviceFeeLog = new ServiceFeeLog();
                                serviceFeeLog.setServiceFee(serviceFee);
                                serviceFeeLog.setChangeDate(System.currentTimeMillis());
                                serviceFeeLog.setStatus(ServiceFeeLog.SERVICE_FEE_ADDED);
                                databaseManager.insertServiceFeeLog(serviceFeeLog).subscribe();
                            });
                        }
                        order.setServiceFee(serviceFeeItem.getServiceFee());
                        order.setServiceAmount(serviceFeeItem.getAmmount());
                    }


                    if (customer != null)
                        order.setCustomer(customer);

                    if (debt != null)
                        order.setToDebtValue(debt.getDebtAmount());

                    order.setCreateAt(System.currentTimeMillis());

                    double totalPayedSum = 0;
                    for (PayedPartitions payedPartitions1 : payedPartitions) {
                        totalPayedSum += payedPartitions1.getValue();
                    }
                    order.setTotalPayed(totalPayedSum);

                    orderChangesLog = new OrderChangesLog();
                    orderChangesLog.setToStatus(Order.HOLD_ORDER);
                    orderChangesLog.setChangedAt(System.currentTimeMillis());
                    orderChangesLog.setReason(reason);
                    orderChangesLog.setChangedCauseType(OrderChangesLog.EDITED);
                    orderChangesLog.setRelationshipOrderId(beforeOrderID);
                    order.setIsArchive(false);
                    order.setStatus(Order.HOLD_ORDER);
                    databaseManager.insertOrderChangeLog(orderChangesLog).blockingGet();

                    //CANCEL OLD ORDER AND DELETE OUTCOME
                    cancelOldOrderWhenEditOrderHoldedOrClosed(oldOrder, beforeOrderID);

                    order.setLastChangeLogId(orderChangesLog.getId());

                    if (debt != null) {
                        databaseManager.addDebt(debt).blockingGet();
                        order.setDebtId(debt.getId());
                    }

                    order.setTillId(databaseManager.getCurrentOpenTillId().blockingGet());

                    databaseManager.insertOrder(order).subscribe((order1, throwable) -> {
                        for (int i = 0; i < orderProducts.size(); i++) {
                            orderProducts.get(i).setOrderId(order1.getId());
                        }

                        if (orderProducts.size() != stockOutOutcomeProductsWithDetials.size())
                            new Exception("SOME THINK IS WRONG!!! orderProductItems size should be similar with outcomeDetial Products").printStackTrace();

                        outcomeProducts = databaseManager.insertAndFillOutcomeProducts(stockOutOutcomeProductsWithDetials);

                        for (int i = 0; i < orderProducts.size(); i++) {
                            orderProducts.get(i).setOutcomeProductId(outcomeProducts.get(i).getId());
                        }

                        databaseManager.insertOrderProducts(orderProducts).subscribeOn(Schedulers.io()).subscribe(orderProducts1 -> {
                            for (int i = 0; i < orderProducts.size(); i++) {
                                outcomeProducts.get(i).setOrderProductId(orderProducts.get(i).getId());
                            }
                            databaseManager.updateOutcomeProduct(outcomeProducts).subscribe();
                        });


                        orderChangesLog.setOrderId(order1.getId());
                        databaseManager.insertOrderChangeLog(orderChangesLog).subscribeOn(Schedulers.io()).subscribe();

                        for (int i = 0; i < payedPartitions.size(); i++) {
                            payedPartitions.get(i).setOrderId(order1.getId());
                        }
                        databaseManager.insertPayedPartitions(payedPartitions).subscribeOn(Schedulers.io()).blockingGet();

                        if (debt != null) {
                            debt.setOrderId(order1.getId());
                            databaseManager.addDebt(debt).subscribeOn(Schedulers.io()).subscribe();
                        }
                        view.editedOrderHolded(reason, order);
                    });

                } else {
                    view.stockOutTillCloseOrder();
                    Log.d("PROBLEM", "stockOutOutComesSize HOLD ORDER -> CLOSE ORDER");
                }
            });

        } else if (fromHold) {
            outcomeProducts = new ArrayList<>();
            final List<OrderProduct> oldOrderProducts = order.getOrderProducts();
            final List<OutcomeProduct> oldOutcomeProducts = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof OrderProductItem) {
                    OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                    outcomeProducts.add(orderProductItem.getOutcomeProduct());
                }
            }

            for (int i = 0; i < oldOrderProducts.size(); i++) {
                oldOutcomeProducts.add(oldOrderProducts.get(i).getOutcomeProduct());
            }

            databaseManager.checkPositionAvailablityWithoutSomeOutcomes(outcomeProducts, oldOutcomeProducts).subscribe(stockOutOutcomeProductsWithDetials -> {
                if (stockOutOutcomeProductsWithDetials.size() != 0) {

                    orderProducts = new ArrayList<>();

                    //COLLECTION ORDERPRODUCT AND REGISTER PRODUCT DISCOUNT AND SERVICE FEE
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) instanceof OrderProductItem) {
                            OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                            if (orderProductItem.getDiscount() != null) {
                                if (orderProductItem.getDiscount().getIsManual())
                                    databaseManager.insertDiscount(orderProductItem.getDiscount()).blockingGet();
                                orderProductItem.getOrderProduct().setDiscount(orderProductItem.getDiscount());
                                orderProductItem.getOrderProduct().setDiscountAmount(orderProductItem.getDiscountAmmount());
                            }
                            if (orderProductItem.getServiceFee() != null) {
                                if (orderProductItem.getServiceFee().getIsManual())
                                    databaseManager.addServiceFee(orderProductItem.getServiceFee()).blockingFirst();
                                orderProductItem.getOrderProduct().setServiceFee(orderProductItem.getServiceFee());
                                orderProductItem.getOrderProduct().setServiceAmount(orderProductItem.getServiceFeeAmmount());
                            }
                            orderProductItem.getOrderProduct().setCount(orderProductItem.getOutcomeProduct().getSumCountValue());
                            orderProducts.add(orderProductItem.getOrderProduct());
                        }
                    }

                    //ORDER DISCOUNT SAVE TO TABLE
                    if (discountItem != null && discountItem.getDiscount() != null) {
                        if (discountItem.getDiscount().getIsManual()) {
                            databaseManager.insertDiscount(discountItem.getDiscount()).subscribe(discount -> {
                                DiscountLog discountLog = new DiscountLog();
                                discountLog.setChangeDate(System.currentTimeMillis());
                                discountLog.setDiscount(discount);
                                discountLog.setStatus(DiscountLog.DISCOUNT_ADDED);
                                databaseManager.insertDiscountLog(discountLog).subscribe();
                            });
                        }
                        order.setDiscount(discountItem.getDiscount());
                        order.setDiscountAmount(discountItem.getAmmount());
                    }

                    //SERVICE DISCOUNT SAVE TO TABLE
                    if (serviceFeeItem != null && serviceFeeItem.getServiceFee() != null) {
                        if (serviceFeeItem.getServiceFee().getIsManual()) {
                            databaseManager.addServiceFee(serviceFeeItem.getServiceFee()).subscribe(serviceFee -> {
                                ServiceFeeLog serviceFeeLog = new ServiceFeeLog();
                                serviceFeeLog.setServiceFee(serviceFee);
                                serviceFeeLog.setChangeDate(System.currentTimeMillis());
                                serviceFeeLog.setStatus(ServiceFeeLog.SERVICE_FEE_ADDED);
                                databaseManager.insertServiceFeeLog(serviceFeeLog).subscribe();
                            });
                        }
                        order.setServiceFee(serviceFeeItem.getServiceFee());
                        order.setServiceAmount(serviceFeeItem.getAmmount());
                    }


                    if (customer != null)
                        order.setCustomer(customer);

                    if (debt != null)
                        order.setToDebtValue(debt.getDebtAmount());

                    order.setCreateAt(System.currentTimeMillis());

                    double totalPayedSum = 0;
                    for (PayedPartitions payedPartitions1 : payedPartitions) {
                        totalPayedSum += payedPartitions1.getValue();
                    }
                    order.setTotalPayed(totalPayedSum);

                    orderChangesLog = new OrderChangesLog();
                    orderChangesLog.setToStatus(Order.HOLD_ORDER);
                    orderChangesLog.setChangedAt(System.currentTimeMillis());
                    orderChangesLog.setChangedCauseType(OrderChangesLog.CONTINUE);
                    order.setStatus(Order.HOLD_ORDER);
                    order.setIsArchive(false);
                    databaseManager.insertOrderChangeLog(orderChangesLog).blockingGet();


                    //RETURN TO STOCK AND DELETE OLD HOLDED ORDER PRODUCTS
                    databaseManager.deleteOrderProductsOnHold(oldOrderProducts).subscribe();
                    //DELETE PAYED PARTITIONS OLD HOLDED ORDER PRODUCTS
                    databaseManager.deletePayedPartitions(order.getPayedPartitions()).subscribe();
                    //DELETE DEBT OLD HOLDED ORDER PRODUCTS
                    if (order.getDebt() != null) {
                        databaseManager.deleteDebt(order.getDebt()).subscribe();
                    }

                    order.setLastChangeLogId(orderChangesLog.getId());

                    if (debt != null) {
                        databaseManager.addDebt(debt).blockingGet();
                        order.setDebtId(debt.getId());
                    }

                    order.setTillId(databaseManager.getCurrentOpenTillId().blockingGet());

                    databaseManager.insertOrder(order).subscribe((order1, throwable) -> {
                        for (int i = 0; i < orderProducts.size(); i++) {
                            orderProducts.get(i).setOrderId(order1.getId());
                        }

                        if (orderProducts.size() != stockOutOutcomeProductsWithDetials.size())
                            new Exception("SOME THINK IS WRONG!!! orderProductItems size should be similar with outcomeDetial Products").printStackTrace();

                        outcomeProducts = databaseManager.insertAndFillOutcomeProducts(stockOutOutcomeProductsWithDetials);

                        for (int i = 0; i < orderProducts.size(); i++) {
                            orderProducts.get(i).setOutcomeProductId(outcomeProducts.get(i).getId());
                        }

                        databaseManager.insertOrderProducts(orderProducts).subscribeOn(Schedulers.io()).subscribe(orderProducts1 -> {
                            for (int i = 0; i < orderProducts.size(); i++) {
                                outcomeProducts.get(i).setOrderProductId(orderProducts.get(i).getId());
                            }
                            databaseManager.updateOutcomeProduct(outcomeProducts).subscribe();

                        });


                        orderChangesLog.setOrderId(order1.getId());
                        databaseManager.insertOrderChangeLog(orderChangesLog).subscribeOn(Schedulers.io()).subscribe();

                        for (int i = 0; i < payedPartitions.size(); i++) {
                            payedPartitions.get(i).setOrderId(order1.getId());
                        }
                        databaseManager.insertPayedPartitions(payedPartitions).subscribeOn(Schedulers.io()).blockingGet();

                        if (debt != null) {
                            debt.setOrderId(order1.getId());
                            databaseManager.addDebt(debt).subscribeOn(Schedulers.io()).subscribe();
                        }
                        view.holdOrderHolded(order);
                    });

                } else {
                    view.stockOutTillCloseOrder();
                    Log.d("PROBLEM", "stockOutOutComesSize HOLD ORDER -> CLOSE ORDER");
                }
            });
        } else {
            outcomeProducts = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof OrderProductItem) {
                    OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                    outcomeProducts.add(orderProductItem.getOutcomeProduct());
                }
            }
            databaseManager.checkPositionAvailablity(outcomeProducts).subscribe(stockOutOutcomeProductsWithDetials -> {
                if (stockOutOutcomeProductsWithDetials.size() != 0) {

                    orderProducts = new ArrayList<>();

                    //COLLECTION ORDERPRODUCT AND REGISTER PRODUCT DISCOUNT AND SERVICE FEE
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) instanceof OrderProductItem) {
                            OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                            if (orderProductItem.getDiscount() != null) {
                                if (orderProductItem.getDiscount().getIsManual())
                                    databaseManager.insertDiscount(orderProductItem.getDiscount()).blockingGet();
                                orderProductItem.getOrderProduct().setDiscount(orderProductItem.getDiscount());
                                orderProductItem.getOrderProduct().setDiscountAmount(orderProductItem.getDiscountAmmount());
                            }
                            if (orderProductItem.getServiceFee() != null) {
                                if (orderProductItem.getServiceFee().getIsManual())
                                    databaseManager.addServiceFee(orderProductItem.getServiceFee()).blockingFirst();
                                orderProductItem.getOrderProduct().setServiceFee(orderProductItem.getServiceFee());
                                orderProductItem.getOrderProduct().setServiceAmount(orderProductItem.getServiceFeeAmmount());
                            }
                            orderProductItem.getOrderProduct().setCount(orderProductItem.getOutcomeProduct().getSumCountValue());
                            orderProducts.add(orderProductItem.getOrderProduct());
                        }
                    }

                    //ORDER DISCOUNT SAVE TO TABLE
                    if (discountItem != null && discountItem.getDiscount() != null) {
                        if (discountItem.getDiscount().getIsManual()) {
                            databaseManager.insertDiscount(discountItem.getDiscount()).subscribe(discount -> {
                                DiscountLog discountLog = new DiscountLog();
                                discountLog.setChangeDate(System.currentTimeMillis());
                                discountLog.setDiscount(discount);
                                discountLog.setStatus(DiscountLog.DISCOUNT_ADDED);
                                databaseManager.insertDiscountLog(discountLog).subscribe();
                            });
                        }
                        order.setDiscount(discountItem.getDiscount());
                        order.setDiscountAmount(discountItem.getAmmount());
                    }

                    //SERVICE DISCOUNT SAVE TO TABLE
                    if (serviceFeeItem != null && serviceFeeItem.getServiceFee() != null) {
                        if (serviceFeeItem.getServiceFee().getIsManual()) {
                            databaseManager.addServiceFee(serviceFeeItem.getServiceFee()).subscribe(serviceFee -> {
                                ServiceFeeLog serviceFeeLog = new ServiceFeeLog();
                                serviceFeeLog.setServiceFee(serviceFee);
                                serviceFeeLog.setChangeDate(System.currentTimeMillis());
                                serviceFeeLog.setStatus(ServiceFeeLog.SERVICE_FEE_ADDED);
                                databaseManager.insertServiceFeeLog(serviceFeeLog).subscribe();
                            });
                        }
                        order.setServiceFee(serviceFeeItem.getServiceFee());
                        order.setServiceAmount(serviceFeeItem.getAmmount());
                    }


                    if (customer != null)
                        order.setCustomer(customer);

                    if (debt != null)
                        order.setToDebtValue(debt.getDebtAmount());

                    order.setCreateAt(System.currentTimeMillis());

                    double totalPayedSum = 0;
                    for (PayedPartitions payedPartitions1 : payedPartitions) {
                        totalPayedSum += payedPartitions1.getValue();
                    }
                    order.setTotalPayed(totalPayedSum);

                    orderChangesLog = new OrderChangesLog();
                    orderChangesLog.setToStatus(Order.HOLD_ORDER);
                    orderChangesLog.setChangedAt(System.currentTimeMillis());
                    orderChangesLog.setReason("");
                    orderChangesLog.setChangedCauseType(OrderChangesLog.HAND);
                    order.setStatus(Order.HOLD_ORDER);
                    order.setIsArchive(false);
                    databaseManager.insertOrderChangeLog(orderChangesLog).blockingGet();

                    order.setLastChangeLogId(orderChangesLog.getId());

                    if (debt != null) {
                        databaseManager.addDebt(debt).blockingGet();
                        order.setDebtId(debt.getId());
                    }

                    order.setTillId(databaseManager.getCurrentOpenTillId().blockingGet());

                    databaseManager.insertOrder(order).subscribe((order1, throwable) -> {
                        for (int i = 0; i < orderProducts.size(); i++) {
                            orderProducts.get(i).setOrderId(order1.getId());
                        }

                        if (orderProducts.size() != stockOutOutcomeProductsWithDetials.size())
                            new Exception("SOME THINK IS WRONG!!! orderProductItems size should be similar with outcomeDetial Products").printStackTrace();

                        outcomeProducts = databaseManager.insertAndFillOutcomeProducts(stockOutOutcomeProductsWithDetials);

                        for (int i = 0; i < orderProducts.size(); i++) {
                            orderProducts.get(i).setOutcomeProductId(outcomeProducts.get(i).getId());
                        }

                        databaseManager.insertOrderProducts(orderProducts).subscribeOn(Schedulers.io()).subscribe(orderProducts1 -> {
                            for (int i = 0; i < orderProducts.size(); i++) {
                                outcomeProducts.get(i).setOrderProductId(orderProducts.get(i).getId());
                            }
                            databaseManager.updateOutcomeProduct(outcomeProducts).subscribe();

                        });


                        orderChangesLog.setOrderId(order1.getId());
                        databaseManager.insertOrderChangeLog(orderChangesLog).subscribeOn(Schedulers.io()).subscribe();

                        for (int i = 0; i < payedPartitions.size(); i++) {
                            payedPartitions.get(i).setOrderId(order1.getId());
                        }
                        databaseManager.insertPayedPartitions(payedPartitions).subscribeOn(Schedulers.io()).blockingGet();

                        if (debt != null) {
                            debt.setOrderId(order1.getId());
                            databaseManager.addDebt(debt).subscribeOn(Schedulers.io()).subscribe();
                        }
                        view.newOrderHolded(order);
                    });

                } else {
                    view.stockOutTillCloseOrder();
                    Log.d("PROBLEM", "stockOutOutComesSize NEW ORDER -> CLOSE ORDER: ");
                }
            });

        }


    }

    @Override
    public void updateCustomer(Customer customer) {
        this.customer = customer;
        view.updateOrderDetials(order, customer, payedPartitions);

    }

    private void initNewOrder() {
        fromEdit = false;
        fromHold = false;
        list.clear();
        oldOrder = null;
        order = new Order();
        payedPartitions.clear();
        discountItem = null;
        serviceFeeItem = null;
        customer = null;
        view.enableServiceFeeButton();
        view.enableDiscountButton();
        updateDetials();
        view.updateOrderDetials(order, customer, payedPartitions);
        view.onNewOrderPaymentFragment();
        view.notifyList();
    }

    private void updateDetials() {
        double totalSubTotal = 0;
        double totalDiscount = 0;
        double totalServiceFee = 0;
        double totalPayed = 0;
        summaryOrderCount.clear();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof OrderProductItem) {
                OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                totalSubTotal += orderProductItem.getOrderProduct().getPrice() * orderProductItem.getOutcomeProduct().getSumCountValue();
                if (orderProductItem.getDiscount() != null) {
                    if (orderProductItem.getDiscount().getAmountType() == Discount.PERCENT) {
                        orderProductItem.setDiscountAmmount((orderProductItem.getOrderProduct().getProduct().getPrice() * orderProductItem.getDiscount().getAmount() / 100) * -1);
                    } else if (orderProductItem.getDiscount().getAmountType() == Discount.VALUE) {
                        orderProductItem.setDiscountAmmount((orderProductItem.getDiscount().getAmount() * -1));
                    }
                    totalDiscount += orderProductItem.getDiscountAmmount() * orderProductItem.getOutcomeProduct().getSumCountValue();
                }
                if (orderProductItem.getServiceFee() != null) {
                    if (orderProductItem.getServiceFee().getType() == ServiceFee.PERCENT) {
                        orderProductItem.setServiceFeeAmmount((orderProductItem.getOrderProduct().getProduct().getPrice() * orderProductItem.getServiceFee().getAmount() / 100));
                    } else if (orderProductItem.getServiceFee().getType() == ServiceFee.VALUE) {
                        orderProductItem.setServiceFeeAmmount((orderProductItem.getServiceFee().getAmount()));
                    }
                    totalServiceFee += orderProductItem.getServiceFeeAmmount() * orderProductItem.getOutcomeProduct().getSumCountValue();
                }
                summaryOrderCount.put(orderProductItem.getOrderProduct().getProductId(), (summaryOrderCount.get(orderProductItem.getOrderProduct().getProductId()) == null ? 0 : summaryOrderCount.get(orderProductItem.getOrderProduct().getProductId())) + orderProductItem.getOutcomeProduct().getSumCountValue());
                list.set(i, orderProductItem);
            } else if (list.get(i) instanceof DiscountItem) {
                DiscountItem discountItem = (DiscountItem) list.get(i);
                if (discountItem.getDiscount().getAmountType() == Discount.PERCENT) {
                    discountItem.setAmmount((totalSubTotal * discountItem.getDiscount().getAmount() / 100) * -1);
                } else if (discountItem.getDiscount().getAmountType() == Discount.VALUE) {
                    discountItem.setAmmount(discountItem.getDiscount().getAmount() * -1);
                }
                totalDiscount += discountItem.getAmmount();
                this.discountItem = discountItem;
                list.set(i, discountItem);
            } else if (list.get(i) instanceof ServiceFeeItem) {
                ServiceFeeItem serviceFeeItem = (ServiceFeeItem) list.get(i);
                if (serviceFeeItem.getServiceFee().getType() == ServiceFee.PERCENT) {
                    serviceFeeItem.setAmmount(totalSubTotal * serviceFeeItem.getServiceFee().getAmount() / 100);
                } else if (serviceFeeItem.getServiceFee().getType() == ServiceFee.VALUE) {
                    serviceFeeItem.setAmmount(serviceFeeItem.getServiceFee().getAmount());
                }
                totalServiceFee += serviceFeeItem.getAmmount();
                this.serviceFeeItem = serviceFeeItem;
                list.set(i, serviceFeeItem);
            }
            if (order.getDaoSession() != null && order.getPayedPartitions().size() != 0)
                for (PayedPartitions payedPartitions : order.getPayedPartitions()) {
                    totalPayed += payedPartitions.getValue();
                }
        }
        order.setSubTotalValue(totalSubTotal);
        order.setTotalPayed(totalPayed);
        order.setDiscountTotalValue(totalDiscount);
        order.setServiceTotalValue(totalServiceFee);

    }

    private int[] updateOrderDiscountServiceFee() {
        int positions[] = {-1, -1};
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof DiscountItem) {
                positions[0] = i;
            } else if (list.get(i) instanceof ServiceFeeItem) {
                positions[1] = i;
            }
        }
        return positions;
    }

    private int findPositionToAdd() {
        int position = list.size();
        if (discountItem != null)
            position -= 1;
        if (serviceFeeItem != null)
            position -= 1;
        return position;
    }

    public boolean isEmptyOrder() {
        boolean isItEmptyOrder = true;
        if (list.size() != 0) isItEmptyOrder = false;
        if (order.getSubTotalValue() != 0) isItEmptyOrder = false;
        if (discountItem != null) isItEmptyOrder = false;
        if (serviceFeeItem != null) isItEmptyOrder = false;
        if (payedPartitions.size() != 0) isItEmptyOrder = false;
        return isItEmptyOrder;
    }

    //edit params
    boolean fromEdit = false;
    String reason;
    long beforeOrderID;
    Order oldOrder;

    @Override
    public void onEditOrder(String reason, Order order, Long newOrderId) {
        for (int i = 0; i < order.getOrderProducts().size(); i++) {
            order.getOrderProducts().get(i).getOutcomeProduct().refresh();
        }
        for (int i = 0; i < order.getPayedPartitions().size(); i++) {
            order.getPayedPartitions().get(i).refresh();
        }
        this.fromEdit = true;
        this.fromHold = false;
        this.reason = reason;
        this.order = order.clone();
        this.beforeOrderID = order.getId();
        this.oldOrder = order;
        this.list.clear();
        this.customer = order.getCustomer();
        list.addAll(order.getListObject());
        payedPartitions.clear();
        payedPartitions.addAll(order.getOrderPayedPartitionsClone());
        discountItem = order.getDiscountItem();
        serviceFeeItem = order.getServiceFeeItem();


        if (discountItem == null)
            view.enableDiscountButton();
        else
            view.disableDiscountButton(discountItem.getDiscount().getName());

        if (serviceFeeItem == null)
            view.enableServiceFeeButton();
        else
            view.disableServiceFeeButton(serviceFeeItem.getServiceFee().getName());

        if (customer == null)
            view.updateCustomer(customer);

        view.setOrderNumberToToolbar(newOrderId);

        updateDetials();
        view.updateOrderDetials(this.order, customer, payedPartitions);

        view.sendDataToPaymentFragmentWhenEdit(this.order, payedPartitions, order.getDebtClone());
        view.notifyList();


    }


    @Override
    public void initNewOrderWithId(Long orderId) {
        initNewOrder();
        view.setOrderNumberToToolbar(orderId);
    }

    boolean fromHold = false;

    @Override
    public void onHoldOrderCountined(Order order) {
        for (int i = 0; i < order.getOrderProducts().size(); i++) {
            order.getOrderProducts().get(i).getOutcomeProduct().refresh();
        }
        for (int i = 0; i < order.getPayedPartitions().size(); i++) {
            order.getPayedPartitions().get(i).refresh();
        }
        this.fromHold = true;
        this.order = order;
        this.list.clear();
        list.addAll(order.getListObject());
        this.customer = order.getCustomer();
        payedPartitions.clear();
        payedPartitions.addAll(order.getOrderPayedPartitionsClone());
        discountItem = order.getDiscountItem();
        serviceFeeItem = order.getServiceFeeItem();
        oldOrder = order;
        if (discountItem == null)
            view.enableDiscountButton();
        else
            view.disableDiscountButton(discountItem.getDiscount().getName());

        if (serviceFeeItem == null)
            view.enableServiceFeeButton();
        else
            view.disableServiceFeeButton(serviceFeeItem.getServiceFee().getName());

        if (customer != null)
            view.updateCustomer(customer);

        view.setOrderNumberToToolbar(order.getId());

        updateDetials();
        view.updateOrderDetials(this.order, customer, payedPartitions);

        view.sendDataToPaymentFragmentWhenEdit(this.order, payedPartitions, order.getDebtClone());
        view.notifyList();

    }


    @Override
    public void eventProductUpdate(Product product, Product newProduct) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof OrderProductItem) {
                OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                if (orderProductItem.getOrderProduct().getProduct().getId().equals(product.getId())) {
                    orderProductItem.getOrderProduct().setProduct(newProduct);
                    orderProductItem.getOrderProduct().setPrice(newProduct.getPrice());
                    list.set(i, orderProductItem);
                    updateDetials();
                    view.updateOrderDetials(order, customer, payedPartitions);
                    view.notifyItemChanged(i, list.size(), updateOrderDiscountServiceFee());
                    view.hideProductInfoFragment();
                    return;
                }
            }
        }
    }

    @Override
    public void eventProductDelete(Product product) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof OrderProductItem) {
                OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                if (orderProductItem.getOrderProduct().getProduct().getId().equals(product.getId())) {
                    list.remove(i);
                    updateDetials();
                    view.updateOrderDetials(order, customer, payedPartitions);
                    view.notifyItemRemove(i, list.size(), updateOrderDiscountServiceFee());
                    view.hideProductInfoFragment();
                    return;
                }
            }
        }
    }

    @Override
    public void eventCustomerUpdate(Customer customer) {
        if (customer != null && this.customer != null && this.customer.getId().equals(customer.getId())) {
            this.customer = customer;
            view.updateOrderDetials(order, customer, payedPartitions);
            view.sendCustomerToPaymentFragment(customer);
        }
    }

    @Override
    public void eventCustomerDelete(Customer customer) {
        if (customer != null && this.customer != null && this.customer.getId().equals(customer.getId())) {
            this.customer = null;
            view.updateOrderDetials(order, this.customer, payedPartitions);
            view.sendCustomerToPaymentFragment(customer);
        }
    }

    @Override
    public void eventDebtUpdate(Debt debt, Debt newDebt) {
        //TODO DEBT TO CUSTOMER DILAOG
    }

    @Override
    public void eventDebtDelete(Debt debt) {
        //TODO DEBT DIALOG
    }

    @Override
    public void eventDiscountUpdate(Discount updatedDiscount) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof OrderProductItem) {
                OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                if (orderProductItem.getDiscount() != null && orderProductItem.getDiscount().getId().equals(updatedDiscount.getId())) {
                    orderProductItem.setDiscount(updatedDiscount);
                    list.set(i, orderProductItem);
                    updateDetials();
                    view.updateOrderDetials(order, customer, payedPartitions);
                    view.notifyItemChanged(i, list.size(), updateOrderDiscountServiceFee());
                    return;
                }
            } else if (list.get(i) instanceof DiscountItem) {
                DiscountItem discountItem = (DiscountItem) list.get(i);
                if (discountItem.getDiscount() != null && discountItem.getDiscount().getId().equals(updatedDiscount.getId())) {
                    discountItem.setDiscount(updatedDiscount);
                    list.set(i, discountItem);
                    view.disableDiscountButton(discountItem.getDiscount().getName());
                    updateDetials();
                    view.updateOrderDetials(order, customer, payedPartitions);
                    view.notifyItemChanged(i, list.size(), updateOrderDiscountServiceFee());
                    return;
                }
            }
        }
    }

    @Override
    public void eventDiscountDelete(Discount discount) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof OrderProductItem) {
                OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                if (orderProductItem.getDiscount() != null && orderProductItem.getDiscount().getId().equals(discount.getId())) {
                    orderProductItem.setDiscount(null);
                    orderProductItem.setDiscountAmmount(0);
                    list.set(i, orderProductItem);
                    updateDetials();
                    view.updateOrderDetials(order, customer, payedPartitions);
                    view.notifyItemChanged(i, list.size(), updateOrderDiscountServiceFee());
                    return;
                }
            } else if (list.get(i) instanceof DiscountItem) {
                DiscountItem discountItem = (DiscountItem) list.get(i);
                if (discountItem.getDiscount() != null && discountItem.getDiscount().getId().equals(discount.getId())) {
                    list.remove(i);
                    view.enableDiscountButton();
                    updateDetials();
                    view.updateOrderDetials(order, customer, payedPartitions);
                    view.notifyItemRemove(i, list.size(), updateOrderDiscountServiceFee());
                    return;
                }
            }
        }
    }

    @Override
    public void eventServiceFeeUpdate(ServiceFee serviceFee) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof OrderProductItem) {
                OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                if (orderProductItem.getServiceFee() != null && orderProductItem.getServiceFee().getId().equals(serviceFee.getId())) {
                    orderProductItem.setServiceFee(serviceFee);
                    list.set(i, orderProductItem);
                    updateDetials();
                    view.updateOrderDetials(order, customer, payedPartitions);
                    view.notifyItemChanged(i, list.size(), updateOrderDiscountServiceFee());
                    return;
                }
            } else if (list.get(i) instanceof ServiceFeeItem) {
                ServiceFeeItem serviceFeeItem = (ServiceFeeItem) list.get(i);
                if (serviceFeeItem.getServiceFee() != null && serviceFeeItem.getServiceFee().getId().equals(serviceFee.getId())) {
                    serviceFeeItem.setServiceFee(serviceFee);
                    list.set(i, serviceFeeItem);
                    view.disableServiceFeeButton(serviceFeeItem.getServiceFee().getName());
                    updateDetials();
                    view.updateOrderDetials(order, customer, payedPartitions);
                    view.notifyItemChanged(i, list.size(), updateOrderDiscountServiceFee());
                    return;
                }
            }
        }
    }

    @Override
    public void eventServiceFeeDelete(ServiceFee serviceFee) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof OrderProductItem) {
                OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                if (orderProductItem.getServiceFee() != null && orderProductItem.getServiceFee().getId().equals(serviceFee.getId())) {
                    orderProductItem.setServiceFee(null);
                    orderProductItem.setServiceFeeAmmount(0);
                    list.set(i, orderProductItem);
                    updateDetials();
                    view.updateOrderDetials(order, customer, payedPartitions);
                    view.notifyItemChanged(i, list.size(), updateOrderDiscountServiceFee());
                    return;
                }
            } else if (list.get(i) instanceof ServiceFeeItem) {
                ServiceFeeItem serviceFeeItem = (ServiceFeeItem) list.get(i);
                if (serviceFeeItem.getServiceFee() != null && serviceFeeItem.getServiceFee().getId().equals(serviceFee.getId())) {
                    list.remove(i);
                    view.enableServiceFeeButton();
                    updateDetials();
                    view.updateOrderDetials(order, customer, payedPartitions);
                    view.notifyItemRemove(i, list.size(), updateOrderDiscountServiceFee());
                    return;
                }
            }
        }
    }

    @Override
    public void eventConsignmentUpdate() {
        view.hideProductInfoFragment();
    }

    @Override
    public void printStockCheck() {
        if (isEmptyOrder()) {
            return;
        }
        List<OrderProductItem> orderProductItems = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof OrderProductItem) {
                OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                orderProductItems.add(orderProductItem);
            }
        }

        boolean isHaveOpenTill = databaseManager.hasOpenTill().blockingGet();
        if (isHaveOpenTill) {
            view.stockCheckOrder(databaseManager.getCurrentOpenTillId().blockingGet(), databaseManager.getLastOrderId().blockingGet() + 1, System.currentTimeMillis(), orderProductItems, customer);
        } else {
            boolean isNoTills = databaseManager.isNoTills().blockingGet();
            if (!isNoTills) {
                view.stockCheckOrder(1L, databaseManager.getLastOrderId().blockingGet() + 1, System.currentTimeMillis(), orderProductItems, customer);
            } else
                view.stockCheckOrder(databaseManager.getLastClosedTill().blockingGet().getId() + 1L, databaseManager.getLastOrderId().blockingGet() + 1, System.currentTimeMillis(), orderProductItems, customer);
        }
//        view.stockCheckOrder(databaseManager.getCurrentOpenTillId().blockingGet(),databaseManager.getLastOrderId().blockingGet()+1,System.currentTimeMillis(),orderProductItems,customer);
    }

    @Override
    public void onBarcodeReaded(String barcode) {
        List<Product> productsAll = new ArrayList<>();
        databaseManager.getAllProducts().subscribe(products -> {
            for (Product product : products) {
                if (product.getBarcode() != null && product.getBarcode().equals(barcode)) {
                    productsAll.add(product);
                }
            }
            if (productsAll.isEmpty()) {
                databaseManager.getCustomers().subscribe(customers -> {
                    for (int i = customers.size() - 1; i >= 0; i--) {
                        if (customers.get(i).getQrCode() != null && customers.get(i).getQrCode().equals(barcode)) {
                            changeCustomer(customers.get(i));
                            eventCustomerUpdate(customers.get(i));
                            return;
                        }
                    }
                });
            } else if (productsAll.size() == 1) {
                addProductToList(productsAll.get(0).getId());
            } else {
                view.choiseOneProduct(productsAll);
            }
        });
    }

    @Override
    public void onCancelClicked() {
        if (isEmptyOrder()) {
            view.openWarningDialog(context.getString(R.string.empty_till_cant_be_cancel));
            return;
        }
        view.sureCancel();
    }

    @Override
    public void onStockPositionClicked(int position) {
        OrderProductItem orderProductItem = (OrderProductItem) list.get(position);

        List<OutcomeProduct> exceptionOutcomeProducts = null;
        List<OutcomeProduct> outcomes = new ArrayList<>();


        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof OrderProductItem) {
                OrderProductItem orderProductItemTemp = (OrderProductItem) list.get(i);
                if (position != i)
                    outcomes.add(orderProductItemTemp.getOutcomeProduct());
            }
        }


        if (fromEdit) {
            List<OrderProduct> oldOrderProducts = oldOrder.getOrderProducts();
            exceptionOutcomeProducts = new ArrayList<>();
            for (int i = 0; i < oldOrderProducts.size(); i++) {
                exceptionOutcomeProducts.add(oldOrderProducts.get(i).getOutcomeProduct());
            }

        } else if (fromHold) {
            List<OrderProduct> oldOrderProducts = order.getOrderProducts();
            exceptionOutcomeProducts = new ArrayList<>();
            for (int i = 0; i < oldOrderProducts.size(); i++) {
                exceptionOutcomeProducts.add(oldOrderProducts.get(i).getOutcomeProduct());
            }
        }

        view.openStockPositionDialog(orderProductItem.getOutcomeProduct(), outcomes, exceptionOutcomeProducts);
    }

    @Override
    public void updateOutcomeProductFor(int position, OutcomeProduct outcomeProduct) {
        OrderProductItem orderProductItem = (OrderProductItem) list.get(position);
        orderProductItem.setOutcomeProduct(outcomeProduct);
        list.set(position, orderProductItem);
        updateDetials();
        view.updateOrderDetials(order, customer, payedPartitions);
        view.notifyItemChanged(position, list.size(), updateOrderDiscountServiceFee());
        view.sendToProductInfoProductItem();
    }


    private void cancelOldOrderWhenEditOrderHoldedOrClosed(Order orderLocal, Long newOrderId) {
        orderLocal.setStatus(Order.CANCELED_ORDER);
        OrderChangesLog orderChangesLog = new OrderChangesLog();
        orderChangesLog.setToStatus(Order.CANCELED_ORDER);
        orderChangesLog.setChangedAt(System.currentTimeMillis());
        orderChangesLog.setReason(reason);
        orderChangesLog.setChangedCauseType(OrderChangesLog.EDITED);
        orderChangesLog.setOrderId(orderLocal.getId());
        orderChangesLog.setRelationshipOrderId(newOrderId);
        databaseManager.insertOrderChangeLog(orderChangesLog).blockingGet();
        orderLocal.setLastChangeLogId(orderChangesLog.getId());
        databaseManager.cancelOutcomeProductWhenOrderProductCanceled(orderLocal.getOrderProducts()).blockingGet();
        if (orderLocal.getDebt() != null) {
            orderLocal.getDebt().setIsDeleted(true);
            databaseManager.addDebt(orderLocal.getDebt());
        }
        databaseManager.insertOrder(orderLocal).blockingGet();
    }


}
