package com.jim.multipos.ui.mainpospage.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.order.OrderProduct;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.VendorProductCon;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by developer on 21.12.2017.
 */

public class OrderProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<OrderProduct> orderProducts;
    private DecimalFormat decimalFormat;
    private DatabaseManager databaseManager;
    private Context context;

    @Inject
    public OrderProductAdapter(DecimalFormat decimalFormat, DatabaseManager databaseManager, Context context){
        this.databaseManager = databaseManager;
        this.context = context;
        List<Product> products = databaseManager.getAllProducts().blockingFirst();
        orderProducts = new ArrayList<>();
        //FAKE DATA
//        OrderProduct orderProduct = new OrderProduct();
//        VendorProductCon productCon = databaseManager.getVendorProductConnectionById(products.get(0).getId(), products.get(0).getVendor().get(0).getId()).blockingSingle();
//        orderProduct.setCost(productCon.getCost());
//        orderProduct.setPrice(products.get(0).getPrice());
//        orderProduct.setCount(3);
//        orderProduct.setProduct(products.get(0));
//        orderProduct.setDaoSession(databaseManager.getDaoSession());
//
//        Discount discount  = new Discount();
//        discount.setId(125l);
//        discount.setUsedType("item");
//        discount.setActive(true);
//        discount.setAmountType("reprice");
//        discount.setDiscription("Some think great");
//        discount.setCreatedDate(System.currentTimeMillis());
//        discount.setNotModifyted(true);
//        discount.setAmount(120000);
//        orderProduct.setDiscount(discount);
//        orderProduct.setFirstValueChanger(-9000);
//
//        ServiceFee serviceFee = new ServiceFee();
//        serviceFee.setId(22l);
//        serviceFee.setApplyingType("item");
//        serviceFee.setActive(true);
//        serviceFee.setType("percent");
//        serviceFee.setReason("Some think great");
//        serviceFee.setCreatedDate(System.currentTimeMillis());
//        serviceFee.setNotModifyted(true);
//        serviceFee.setAmount(15);
//        orderProduct.setServiceFee(serviceFee);
//        orderProduct.setSecondValueChanger(18000);
//        orderProducts.add(orderProduct);
        this.decimalFormat = decimalFormat;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, parent, false);
        return new OrderProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderMain, int position) {
        if(holderMain instanceof OrderProductViewHolder) {
            OrderProductViewHolder holder = (OrderProductViewHolder) holderMain;
            OrderProduct orderProduct = orderProducts.get(position);
            holder.tvCountProduct.setText(String.valueOf(orderProduct.getCount()));
            holder.tvEach.setText(decimalFormat.format(orderProduct.getCost()));
            holder.tvSum.setText(decimalFormat.format(orderProduct.getCost() * orderProduct.getCount()));
            holder.tvProductName.setText(orderProduct.getProduct().getName());


            if(orderProduct.getFirstValueChanger()!=0){
                if(orderProduct.getFirstValueChanger()>0){
                    //ServiceFee
                    if(orderProduct.getServiceFee().getType().equals("reprice")){
                        holder.tvEach.setPaintFlags(holder.tvEach.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.tvSum.setPaintFlags(holder.tvSum.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.tvFirstChangerToEach.setText("=" + decimalFormat.format(orderProduct.getServiceFee().getAmount()));
                        holder.tvFirstChangerSum.setText("=" + decimalFormat.format(orderProduct.getServiceFee().getAmount()*orderProduct.getCount()));
                    }else {
                        holder.tvFirstChangerToEach.setText("+"+decimalFormat.format(orderProduct.getFirstValueChanger()));
                        holder.tvFirstChangerSum.setText("+"+decimalFormat.format(orderProduct.getFirstValueChanger()*orderProduct.getCount()));
                    }
                    holder.tvFirstChangerName.setText("Service Fee "+ orderProduct.getServiceFee().getServiceFeeTypeName(context)+" :");
                    holder.llFirstChanger.setVisibility(View.VISIBLE);
                }else {
                    //Discount
                    if(orderProduct.getDiscount().getAmountType() == Discount.REPRICE){
                        holder.tvEach.setPaintFlags(holder.tvEach.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.tvSum.setPaintFlags(holder.tvSum.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.tvFirstChangerToEach.setText("=" + decimalFormat.format(orderProduct.getDiscount().getAmount()));
                        holder.tvFirstChangerSum.setText("=" + decimalFormat.format(orderProduct.getDiscount().getAmount()*orderProduct.getCount()));
                    }else {
                        holder.tvFirstChangerToEach.setText(decimalFormat.format(orderProduct.getFirstValueChanger()));
                        holder.tvFirstChangerSum.setText(decimalFormat.format(orderProduct.getFirstValueChanger()*orderProduct.getCount()));
                    }
                    holder.tvFirstChangerName.setText("Discount "+ orderProduct.getDiscount().getDiscountTypeName(context)+" :");
                    holder.llFirstChanger.setVisibility(View.VISIBLE);
                }
            }else {
                holder.llFirstChanger.setVisibility(View.GONE);
            }


            if(orderProduct.getSecondValueChanger()!=0){
                if(orderProduct.getSecondValueChanger()>0){
                        //ServiceFee
                        if(orderProduct.getServiceFee().getType().equals("reprice")) {
                            holder.tvEach.setPaintFlags(holder.tvEach.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            holder.tvSum.setPaintFlags(holder.tvSum.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            holder.tvFirstChangerToEach.setPaintFlags(holder.tvFirstChangerToEach.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            holder.tvFirstChangerSum.setPaintFlags(holder.tvFirstChangerSum.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                            holder.tvSecondChangerForEach.setText("=" + decimalFormat.format(orderProduct.getServiceFee().getAmount()));
                            holder.tvSecondChangerSum.setText("=" + decimalFormat.format(orderProduct.getServiceFee().getAmount()*orderProduct.getCount()));
                        }else {
                            holder.tvSecondChangerForEach.setText("+"+decimalFormat.format(orderProduct.getSecondValueChanger()));
                            holder.tvSecondChangerSum.setText("+"+decimalFormat.format(orderProduct.getSecondValueChanger()*orderProduct.getCount()));
                        }
                        holder.tvSecondChangerName.setText("Service Fee "+ orderProduct.getServiceFee().getServiceFeeTypeName(context)+" :");
                        holder.llSecondChanger.setVisibility(View.VISIBLE);
                    }else {
                        //Discount
                        if(orderProduct.getDiscount().getAmountType().equals("reprice")){
                            holder.tvEach.setPaintFlags(holder.tvEach.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            holder.tvSum.setPaintFlags(holder.tvSum.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            holder.tvFirstChangerToEach.setPaintFlags(holder.tvFirstChangerToEach.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            holder.tvFirstChangerSum.setPaintFlags(holder.tvFirstChangerSum.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                            holder.tvSecondChangerForEach.setText("=" + decimalFormat.format(orderProduct.getDiscount().getAmount()));
                            holder.tvSecondChangerSum.setText("=" + decimalFormat.format(orderProduct.getDiscount().getAmount()*orderProduct.getCount()));
                        }else {
                            holder.tvSecondChangerForEach.setText(decimalFormat.format(orderProduct.getSecondValueChanger()));
                            holder.tvSecondChangerSum.setText(decimalFormat.format(orderProduct.getSecondValueChanger()*orderProduct.getCount()));
                        }
                        holder.tvSecondChangerName.setText("Discount "+ orderProduct.getDiscount().getDiscountTypeName(context)+" :");
                        holder.llSecondChanger.setVisibility(View.VISIBLE);
                }
            }else {
                holder.llSecondChanger.setVisibility(View.GONE);
            }


            if(orderProducts.get(position).getServiceFee() == null && orderProducts.get(position).getDiscount() == null){
                holder.flHaveExtras.setVisibility(View.GONE);
            }else {
                holder.flHaveExtras.setVisibility(View.VISIBLE);
            }

            if(position==orderProducts.size()-1){
                holder.isLastItemGone.setVisibility(View.GONE);
            }else {
                holder.isLastItemGone.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return orderProducts.size();
    }

    class OrderProductViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.tvCountProduct)
        TextView tvCountProduct;
        @BindView(R.id.flLeftMinus)
        FrameLayout flLeftMinus;
        @BindView(R.id.flRightPlus)
        FrameLayout flRightPlus;
        @BindView(R.id.tvEach)
        TextView tvEach;
        @BindView(R.id.tvSum)
        TextView tvSum;
        @BindView(R.id.flHaveExtras)
        FrameLayout flHaveExtras;
        @BindView(R.id.llFirstChanger)
        LinearLayout llFirstChanger;
        @BindView(R.id.tvFirstChangerName)
        TextView tvFirstChangerName;
        @BindView(R.id.tvFirstChangerToEach)
        TextView tvFirstChangerToEach;
        @BindView(R.id.tvFirstChangerSum)
        TextView tvFirstChangerSum;
        @BindView(R.id.llSecondChanger)
        LinearLayout llSecondChanger;
        @BindView(R.id.tvSecondChangerName)
        TextView tvSecondChangerName;
        @BindView(R.id.tvSecondChangerForEach)
        TextView tvSecondChangerForEach;
        @BindView(R.id.tvSecondChangerSum)
        TextView tvSecondChangerSum;
        @BindView(R.id.isLastItemGone)
        FrameLayout isLastItemGone;

        public OrderProductViewHolder(View itemView) {
             super(itemView);
             ButterKnife.bind(this, itemView);
             flLeftMinus.setOnClickListener(view -> {
                 orderProducts.get(getAdapterPosition()).setCount(orderProducts.get(getAdapterPosition()).getCount()-1);
                 notifyItemChanged(getAdapterPosition());
             });
             flRightPlus.setOnClickListener(view -> {
                 orderProducts.get(getAdapterPosition()).setCount(orderProducts.get(getAdapterPosition()).getCount()+1);
                 notifyItemChanged(getAdapterPosition());
             });
         }
     }
}
