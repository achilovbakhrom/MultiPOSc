package com.jim.multipos.ui.mainpospage.presenter;

import android.os.Bundle;
import android.widget.Toast;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.OrderProduct;
import com.jim.multipos.data.db.model.order.PayedPartitions;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Vendor;
import com.jim.multipos.data.db.model.products.VendorProductCon;
import com.jim.multipos.data.db.model.unit.UnitCategory;
import com.jim.multipos.ui.mainpospage.dialogs.DiscountDialog;
import com.jim.multipos.ui.mainpospage.dialogs.ServiceFeeDialog;
import com.jim.multipos.ui.mainpospage.model.DiscountItem;
import com.jim.multipos.ui.mainpospage.model.OrderProductItem;
import com.jim.multipos.ui.mainpospage.model.ServiceFeeItem;
import com.jim.multipos.ui.mainpospage.view.OrderListView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


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

    @Inject
    public OrderListPresenterImpl(OrderListView orderListView, DatabaseManager databaseManager) {
        super(orderListView);
        list = new ArrayList<>();
        order = new Order();
        payedPartitions = new ArrayList<>();
        this.databaseManager = databaseManager;
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
        view.updateOrderDetials(order,customer,payedPartitions);
        updateDetials();
        view.initOrderList(list);
    }

    @Override
    public void addDiscount(double amount, String description, int amountType) {
        Discount discount = new Discount();
        discount.setAmount(amount);
        discount.setName(description);
        discount.setAmountType(amountType);
        discount.setUsedType(Discount.ORDER);
        discount.setCreatedDate(System.currentTimeMillis());
        discount.setDeleted(false);
        discount.setNotModifyted(true);
        databaseManager.insertDiscount(discount).subscribe();
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
        serviceFee.setNotModifyted(true);
        databaseManager.getServiceFeeOperations().addServiceFee(serviceFee).blockingSingle();
    }

    @Override
    public void onPlusCount(int position) {
        OrderProductItem orderProductItem   = (OrderProductItem) list.get(position);
        orderProductItem.getOrderProduct().setCount(orderProductItem.getOrderProduct().getCount()+1);
        list.set(position,orderProductItem);
        updateDetials();
        view.updateOrderDetials(order,customer,payedPartitions);
        view.notifyItemChanged(position,list.size(),updateOrderDiscountServiceFee());
        view.sendToProductInfoProductItem();
    }

    @Override
    public void onMinusCount(int position) {
        OrderProductItem orderProductItem   = (OrderProductItem) list.get(position);
        if(orderProductItem.getOrderProduct().getCount()>1){
            orderProductItem.getOrderProduct().setCount(orderProductItem.getOrderProduct().getCount()-1);
        }
        list.set(position,orderProductItem);
        updateDetials();
        view.updateOrderDetials(order,customer,payedPartitions);
        view.notifyItemChanged(position,list.size(),updateOrderDiscountServiceFee());
        view.sendToProductInfoProductItem();

    }

    @Override
    public void setCount(int position, double count) {
        OrderProductItem orderProductItem   = (OrderProductItem) list.get(position);
        orderProductItem.getOrderProduct().setCount(count);
        list.set(position,orderProductItem);
        updateDetials();
        view.updateOrderDetials(order,customer,payedPartitions);
        view.notifyItemChanged(position,list.size(),updateOrderDiscountServiceFee());
    }

    @Override
    public void onOrderProductClick(int position) {
        OrderProductItem orderProductItem   = (OrderProductItem) list.get(position);
        view.openProductInfoFragment(orderProductItem,position);
    }

    @Override
    public void onOrderDiscountClick() {

    }

    @Override
    public void onOrderServiceFeeClick() {

    }

    @Override
    public void addProductToList(Long productId) {
        Product product = databaseManager.getProductById(productId).blockingFirst();
        if(product.getMainUnit().getUnitCategory().getUnitType() != UnitCategory.PIECE){
            view.openUnitValuePicker(product);
            return;
        }
        //TODO
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i) instanceof  OrderProductItem){
                OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                if(orderProductItem.getOrderProduct().getProduct().getId().equals(productId)){
                    if(orderProductItem.getDiscount()==null && orderProductItem.getServiceFee()==null){
                        orderProductItem.getOrderProduct().setCount(orderProductItem.getOrderProduct().getCount()+1);
                        list.set(i,orderProductItem);
                        updateDetials();
                        view.updateOrderDetials(order,customer,payedPartitions);
                        view.notifyItemChanged(i,list.size(),updateOrderDiscountServiceFee());
                        return;
                    }
                }
            }
        }

        OrderProductItem orderProductItem = new OrderProductItem();
        OrderProduct orderProduct = new OrderProduct();
        VendorProductCon productCon = databaseManager.getVendorProductConnectionById(product.getId(), product.getVendor().get(0).getId()).blockingSingle();
        orderProduct.setCost(productCon.getCost());
        orderProduct.setPrice(product.getPrice());
        orderProduct.setCount(1);
        orderProduct.setProduct(product);
        //TODO LAST CHOISEN VENDOR
        orderProduct.setVendor(product.getVendor().get(0));
        orderProductItem.setOrderProduct(orderProduct);
        int positionToAdd = findPositionToAdd();
        list.add(positionToAdd,orderProductItem);
        updateDetials();
        view.updateOrderDetials(order,customer,payedPartitions);
        view.notifyItemAdded(positionToAdd,list.size(),updateOrderDiscountServiceFee());

    }

    @Override
    public void addDiscountToProduct(Long productId, Discount discount, boolean isManual) {

    }

    @Override
    public void addServiceFeeToProduct(Long productId, ServiceFee serviceFee, boolean isManual) {

    }



    @Override
    public void openDiscountDialog() {
        if(discountItem != null){
            int q = -1;
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i) instanceof DiscountItem){
                    list.remove(i);
                    q = i;
                    break;
                }
            }
            if(q!=-1) {
                discountItem = null;
                updateDetials();
                view.updateOrderDetials(order,customer,payedPartitions);
                view.notifyItemRemove(q,list.size(),updateOrderDiscountServiceFee());
                view.enableDiscountButton();
            }else {
                new Throwable("DISCOUNT NOT FOUND").printStackTrace();
            }
        }else {
            DiscountDialog.CallbackDiscountDialog callbackDiscountDialog = new DiscountDialog.CallbackDiscountDialog() {
                @Override
                public void choiseStaticDiscount(Discount discount) {
                    discountItem = new DiscountItem();
                    discountItem.setDiscount(discount);
                    list.add(discountItem);
                    view.disableDiscountButton(discount.getName());
                    updateDetials();
                    view.updateOrderDetials(order,customer,payedPartitions);
                    view.notifyItemAdded(list.size()-1,list.size(),updateOrderDiscountServiceFee());

                }

                @Override
                public void choiseManualDiscount(Discount discount) {
                    discountItem = new DiscountItem();
                    discountItem.setDiscount(discount);
                    list.add(discountItem);
                    view.disableDiscountButton(discount.getName());
                    updateDetials();
                    view.updateOrderDetials(order,customer,payedPartitions);
                    view.notifyItemAdded(list.size()-1,list.size(),updateOrderDiscountServiceFee());

                }
            };
            view.openDiscountDialog(callbackDiscountDialog,order.getSubTotalValue()+order.getDiscountTotalValue()+order.getServiceTotalValue());
        }

    }

    @Override
    public void openSeriveFeeDialog() {
        if(serviceFeeItem !=null){
            int q = -1;
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i) instanceof ServiceFeeItem){
                    list.remove(i);
                    q = i;
                    break;
                }
            }
            if(q!=-1) {
                serviceFeeItem = null;
                updateDetials();
                view.updateOrderDetials(order,customer,payedPartitions);
                view.notifyItemRemove(q,list.size(),updateOrderDiscountServiceFee());
                view.enableServiceFeeButton();
            }else {
                new Throwable("DISCOUNT NOT FOUND").printStackTrace();
            }
        }else {
            ServiceFeeDialog.CallbackServiceFeeDialog callbackServiceFeeDialog = new ServiceFeeDialog.CallbackServiceFeeDialog() {
                @Override
                public void choiseStaticServiceFee(ServiceFee serviceFee) {
                    serviceFeeItem = new ServiceFeeItem();
                    serviceFeeItem.setServiceFee(serviceFee);
                    list.add(serviceFeeItem);
                    view.disableServiceFeeButton(serviceFee.getName());
                    updateDetials();
                    view.updateOrderDetials(order,customer,payedPartitions);
                    view.notifyItemAdded(list.size()-1,list.size(),updateOrderDiscountServiceFee());
                }

                @Override
                public void choiseManualServiceFee(ServiceFee serviceFee) {
                    serviceFeeItem = new ServiceFeeItem();
                    serviceFeeItem.setServiceFee(serviceFee);
                    list.add(serviceFeeItem);
                    view.disableServiceFeeButton(serviceFee.getName());
                    updateDetials();
                    view.updateOrderDetials(order,customer,payedPartitions);
                    view.notifyItemAdded(list.size()-1,list.size(),updateOrderDiscountServiceFee());
                }
            };
            view.openSericeFeeDialog(callbackServiceFeeDialog,order.getSubTotalValue()+order.getDiscountTotalValue()+order.getServiceTotalValue());
        }
    }

    @Override
    public void changeProductVendor(Vendor vendor, int position) {
        OrderProductItem orderProductItem   = (OrderProductItem) list.get(position);
        orderProductItem.getOrderProduct().setVendor(vendor);
        list.set(position,orderProductItem);
        updateDetials();
        view.updateOrderDetials(order,customer,payedPartitions);
        view.notifyItemChanged(position,list.size(),updateOrderDiscountServiceFee());
    }

    @Override
    public void changeDiscription(String discription, int position) {
        if(position == -1) return;
        OrderProductItem orderProductItem   = (OrderProductItem) list.get(position);
        orderProductItem.getOrderProduct().setDiscription(discription);
        list.set(position,orderProductItem);
        updateDetials();
        view.updateOrderDetials(order,customer,payedPartitions);
        view.notifyItemChanged(position,list.size(),updateOrderDiscountServiceFee());
    }

    @Override
    public void removeOrderProducts(int removePosition) {
        list.remove(removePosition);
        if(!isListHaveProducts()){
            cleanOrder();

        }else {
            updateDetials();
            view.updateOrderDetials(order, customer, payedPartitions);
            view.notifyItemRemove(removePosition, list.size(), updateOrderDiscountServiceFee());
        }

    }
    private boolean isListHaveProducts(){
        if(list.size()==0){
            return false;
        }
        int productCount = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof OrderProductItem) {
                productCount ++;
            }
        }
        if(productCount == 0) return false;
        else return true;
    }
    @Override
    public void setDiscountToProduct(Discount discountToProduct, int currentPosition) {
        OrderProductItem orderProductItem   = (OrderProductItem) list.get(currentPosition);
        orderProductItem.setDiscount(discountToProduct);
        list.set(currentPosition,orderProductItem);
        updateDetials();
        view.updateOrderDetials(order,customer,payedPartitions);
        view.notifyItemChanged(currentPosition,list.size(),updateOrderDiscountServiceFee());
    }

    @Override
    public void setServiceFeeProduct(ServiceFee serviceFeeProduct, int currentPosition) {
        OrderProductItem orderProductItem   = (OrderProductItem) list.get(currentPosition);
        orderProductItem.setServiceFee(serviceFeeProduct);
        list.set(currentPosition,orderProductItem);
        updateDetials();
        view.updateOrderDetials(order,customer,payedPartitions);
        view.notifyItemChanged(currentPosition,list.size(),updateOrderDiscountServiceFee());
    }

    @Override
    public void changeCustomer(Customer customer) {
        this.customer = customer;
        view.updateOrderDetials(order,customer,payedPartitions);
    }

    @Override
    public void onClickChooseCustomerButton() {
        if(customer !=null){
            customer = null;
            view.updateOrderDetials(order,customer,payedPartitions);
        }else{
            view.openCustomerDialog();
        }
    }

    @Override
    public void addProductWithWeightToList(Product product, double weight) {
        OrderProductItem orderProductItem = new OrderProductItem();
        OrderProduct orderProduct = new OrderProduct();
        VendorProductCon productCon = databaseManager.getVendorProductConnectionById(product.getId(), product.getVendor().get(0).getId()).blockingSingle();
        orderProduct.setCost(productCon.getCost());
        orderProduct.setPrice(product.getPrice());
        orderProduct.setCount(weight);
        orderProduct.setProduct(product);
        //TODO LAST CHOISEN VENDOR
        orderProduct.setVendor(product.getVendor().get(0));
        orderProductItem.setOrderProduct(orderProduct);
        int positionToAdd = findPositionToAdd();
        list.add(positionToAdd,orderProductItem);
        updateDetials();
        view.updateOrderDetials(order,customer,payedPartitions);
        view.notifyItemAdded(positionToAdd,list.size(),updateOrderDiscountServiceFee());
    }

    @Override
    public void addProductWithWeightToListEdit(Product product, double weight) {
        OrderProductItem orderProductItem   = (OrderProductItem) list.get(positionOfWeightItem);
        orderProductItem.getOrderProduct().setCount(weight);
        list.set(positionOfWeightItem,orderProductItem);
        updateDetials();
        view.updateOrderDetials(order,customer,payedPartitions);
        view.notifyItemChanged(positionOfWeightItem,list.size(),updateOrderDiscountServiceFee());
    }

    @Override
    public void addProductWithWeightToListEditFromInfo(int currentPosition, double weight) {
        OrderProductItem orderProductItem   = (OrderProductItem) list.get(currentPosition);
        orderProductItem.getOrderProduct().setCount(weight);
        list.set(currentPosition,orderProductItem);
        updateDetials();
        view.updateOrderDetials(order,customer,payedPartitions);
        view.notifyItemChanged(currentPosition,list.size(),updateOrderDiscountServiceFee());
    }

    int positionOfWeightItem = -1;
    @Override
    public void onCountWeigtClick(int position) {
        positionOfWeightItem = position;
        OrderProductItem orderProductItem   = (OrderProductItem) list.get(position);
        view.openUnitValuePickerEdit(orderProductItem.getOrderProduct().getProduct(),orderProductItem.getOrderProduct().getCount());
    }

    @Override
    public void sendToPaymentFragmentOrderAndPaymentsList() {
        view.sendDataToPaymentFragment(order,payedPartitions);
    }

    @Override
    public void onPayedPartition() {
        view.updateOrderDetials(order,customer,payedPartitions);
    }

    @Override
    public void cleanOrder() {
        payedPartitions.clear();
        list.clear();
        discountItem = null;
        serviceFeeItem = null;
        view.enableServiceFeeButton();
        view.enableDiscountButton();
        updateDetials();
        view.updateOrderDetials(order, customer, payedPartitions);
        view.notifyList();
    }

    private void updateDetials(){
            double totalSubTotal = 0;
            double totalDiscount = 0;
            double totalServiceFee = 0;
            double totalPayed = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) instanceof OrderProductItem) {
                    OrderProductItem orderProductItem = (OrderProductItem) list.get(i);
                    totalSubTotal += orderProductItem.getOrderProduct().getPrice() * orderProductItem.getOrderProduct().getCount();
                    if (orderProductItem.getDiscount() != null) {
                        if (orderProductItem.getDiscount().getAmountType() == Discount.PERCENT) {
                            orderProductItem.setDiscountAmmount((orderProductItem.getOrderProduct().getProduct().getPrice() * orderProductItem.getDiscount().getAmount() / 100) * -1);
                        } else if (orderProductItem.getDiscount().getAmountType() == Discount.VALUE) {
                            orderProductItem.setDiscountAmmount((orderProductItem.getDiscount().getAmount()));
                        }
                        totalDiscount += orderProductItem.getDiscountAmmount() * orderProductItem.getOrderProduct().getCount();
                    }
                    if (orderProductItem.getServiceFee() != null) {
                        if (orderProductItem.getServiceFee().getType() == ServiceFee.PERCENT) {
                            orderProductItem.setServiceFeeAmmount((orderProductItem.getOrderProduct().getProduct().getPrice() * orderProductItem.getServiceFee().getAmount() / 100));
                        } else if (orderProductItem.getServiceFee().getType() == ServiceFee.VALUE) {
                            orderProductItem.setServiceFeeAmmount((orderProductItem.getServiceFee().getAmount()));
                        }
                        totalServiceFee += orderProductItem.getServiceFeeAmmount() * orderProductItem.getOrderProduct().getCount();
                    }
                    list.set(i, orderProductItem);
                } else if (list.get(i) instanceof DiscountItem) {
                    DiscountItem discountItem = (DiscountItem) list.get(i);
                    if (discountItem.getDiscount().getAmountType() == Discount.PERCENT) {
                        discountItem.setAmmount((totalSubTotal * discountItem.getDiscount().getAmount() / 100) * -1);
                    } else if (discountItem.getDiscount().getAmountType() == Discount.VALUE) {
                        discountItem.setAmmount(discountItem.getDiscount().getAmount()*-1);
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
    private int[] updateOrderDiscountServiceFee(){
        int positions []={-1,-1};
        for (int i = 0;i<list.size();i++) {
            if(list.get(i) instanceof DiscountItem){
                positions[0] = i;
            }else if(list.get(i) instanceof ServiceFeeItem){
                positions[1] = i;
            }
        }
        return positions;
    }
    private int findPositionToAdd(){
        int position = list.size();
        if(discountItem!=null)
            position-=1;
        if(serviceFeeItem!=null)
            position-=1;
        return position;
    }
}
