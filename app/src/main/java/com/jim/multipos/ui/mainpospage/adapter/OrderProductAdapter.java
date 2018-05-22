package com.jim.multipos.ui.mainpospage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.data.db.model.order.Order;
import com.jim.multipos.data.db.model.order.OrderProduct;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.VendorProductCon;
import com.jim.multipos.data.db.model.unit.UnitCategory;
import com.jim.multipos.ui.mainpospage.model.DiscountItem;
import com.jim.multipos.ui.mainpospage.model.OrderProductItem;
import com.jim.multipos.ui.mainpospage.model.ServiceFeeItem;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by developer on 21.12.2017.
 */

public class OrderProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Object> adapterItem;
    private CallbackOrderProductList callback;
    private DecimalFormat decimalFormat;
    private DatabaseManager databaseManager;
    private Context context;
    private final int PRODUCT_ITEM = 1;
    private final int DISCOUNT_ITEM = 2;
    private final int SERVICE_FEE_ITEM = 3;
    DecimalFormat decimalFormatLocal;
    @Inject
    public OrderProductAdapter( DatabaseManager databaseManager, Context context){
        this.databaseManager = databaseManager;
        this.context = context;
        adapterItem = new ArrayList<>();

        decimalFormatLocal =  BaseAppModule.getFormatterWithoutGroupingPattern("#.###");


        decimalFormat = BaseAppModule.getFormatterGroupingPattern("#,###.##");


    }
    public void setData(List<Object> data, CallbackOrderProductList callbackOrderProductList){
        adapterItem = data;
        this.callback = callbackOrderProductList;
        notifyDataSetChanged();
    }
    public interface CallbackOrderProductList{
        void onPlusCount(int position);
        void onMinusCount(int position);
        void onOrderProductClick(int position);
        void onOrderDiscountClick();
        void onOrderServiceFeeClick();
        void onCountWeigtClick(int position);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        if(viewType == PRODUCT_ITEM){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, parent, false);
            return new OrderProductViewHolder(view);
        }else if(viewType == DISCOUNT_ITEM){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item_discount, parent, false);
            return new OrderDiscountViewHolder(view);
        }else if(viewType == SERVICE_FEE_ITEM){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item_service_fee, parent, false);
            return new OrderServiceFeeViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if(adapterItem.get(position) instanceof DiscountItem){
            return DISCOUNT_ITEM;
        }else if(adapterItem.get(position) instanceof OrderProductItem){
            return PRODUCT_ITEM;
        }else if(adapterItem.get(position) instanceof ServiceFeeItem){
            return SERVICE_FEE_ITEM;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderMain, int position) {

        if(holderMain instanceof OrderProductViewHolder) {
            OrderProductViewHolder holder = (OrderProductViewHolder) holderMain;
            OrderProductItem orderProductItem = (OrderProductItem) adapterItem.get(position);

            if(orderProductItem.getOrderProduct().getProduct().getMainUnit().getUnitCategory().getUnitType() == UnitCategory.PIECE){
                holder.tvCountProduct.setText(decimalFormat.format(orderProductItem.getOrderProduct().getCount()));
                holder.ivLeftMinus.setVisibility(View.VISIBLE);
                holder.ivRightPlus.setVisibility(View.VISIBLE);
                holder.rlUnitQty.setBackground(null);
            }
            else {
                double count = orderProductItem.getOrderProduct().getCount();
                DecimalFormat df = null;
                df = decimalFormatLocal;
                holder.tvCountProduct.setText(df.format(count) + " " + orderProductItem.getOrderProduct().getProduct().getMainUnit().getAbbr());
                holder.ivLeftMinus.setVisibility(View.GONE);
                holder.ivRightPlus.setVisibility(View.GONE);
                holder.rlUnitQty.setBackgroundResource(R.drawable.order_list_weight_product_item);
            }

            holder.tvEach.setText(decimalFormat.format(orderProductItem.getOrderProduct().getPrice()));
            holder.tvSum.setText(decimalFormat.format(orderProductItem.getOrderProduct().getPrice() * orderProductItem.getOrderProduct().getCount()));
            holder.tvProductName.setText(orderProductItem.getOrderProduct().getProduct().getName());


            if(orderProductItem.getDiscount()!=null){
                holder.tvFirstChangerToEach.setText(decimalFormat.format(orderProductItem.getDiscountAmmount()));
                holder.tvFirstChangerSum.setText(decimalFormat.format(orderProductItem.getDiscountAmmount()*orderProductItem.getOrderProduct().getCount()));
                holder.tvFirstChangerName.setText(orderProductItem.getDiscount().getName()+context.getString(R.string.discount_min));
                holder.llFirstChanger.setVisibility(View.VISIBLE);
            }else {
                holder.llFirstChanger.setVisibility(View.GONE);
            }


            if(orderProductItem.getServiceFee()!=null){
                holder.tvSecondChangerForEach.setText("+"+decimalFormat.format(orderProductItem.getServiceFeeAmmount()));
                holder.tvSecondChangerSum.setText("+"+decimalFormat.format(orderProductItem.getServiceFeeAmmount()*orderProductItem.getOrderProduct().getCount()));
                holder.tvSecondChangerName.setText(orderProductItem.getServiceFee().getName()+context.getString(R.string.service_fee_min));
                holder.llSecondChanger.setVisibility(View.VISIBLE);
            }else {
                holder.llSecondChanger.setVisibility(View.GONE);
            }


            if(orderProductItem.getServiceFee() == null && orderProductItem.getDiscount() == null){
                holder.flHaveExtras.setVisibility(View.GONE);
            }else {
                holder.flHaveExtras.setVisibility(View.VISIBLE);
            }

            if(position==adapterItem.size()-1){
                holder.isLastItemGone.setVisibility(View.GONE);
            }else {
                holder.isLastItemGone.setVisibility(View.VISIBLE);
            }


        }
        if(holderMain instanceof OrderDiscountViewHolder){
            OrderDiscountViewHolder holder = (OrderDiscountViewHolder) holderMain;
            DiscountItem discountItem = (DiscountItem) adapterItem.get(position);
            holder.tvDiscountName.setText(discountItem.getDiscount().getName()+context.getString(R.string.order_discount_min));
            holder.tvDiscountAmount.setText(decimalFormat.format(discountItem.getAmmount()));
            if(position==adapterItem.size()-1){
                holder.isLastItemGone.setVisibility(View.GONE);
            }else {
                holder.isLastItemGone.setVisibility(View.VISIBLE);
            }
        }
        if(holderMain instanceof OrderServiceFeeViewHolder){
            OrderServiceFeeViewHolder holder = (OrderServiceFeeViewHolder) holderMain;
            ServiceFeeItem serviceFeeItem = (ServiceFeeItem) adapterItem.get(position);
            holder.tvServiceFeeName.setText(serviceFeeItem.getServiceFee().getName()+context.getString(R.string.order_service_fee_min));
            holder.tvServiceFeeAmount.setText("+"+decimalFormat.format(serviceFeeItem.getAmmount()));
            if(position==adapterItem.size()-1){
                holder.isLastItemGone.setVisibility(View.GONE);
            }else {
                holder.isLastItemGone.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return adapterItem.size();
    }

    class OrderProductViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.tvCountProduct)
        TextView tvCountProduct;
        @BindView(R.id.flLeftMinus)
        View flLeftMinus;
        @BindView(R.id.flRightPlus)
        View flRightPlus;
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
        @BindView(R.id.llProduct)
        LinearLayout llProduct;
        @BindView(R.id.rlUnitQty)
        RelativeLayout rlUnitQty;
        @BindView(R.id.ivLeftMinus)
        ImageView ivLeftMinus;
        @BindView(R.id.ivRightPlus)
        ImageView ivRightPlus;

        public OrderProductViewHolder(View itemView) {
             super(itemView);
             ButterKnife.bind(this, itemView);
             flLeftMinus.setOnClickListener(view -> {
                 OrderProductItem orderProductItem = (OrderProductItem) adapterItem.get(getAdapterPosition());
                 if(orderProductItem.getOrderProduct().getProduct().getMainUnit().getUnitCategory().getUnitType() == UnitCategory.PIECE) {
                     callback.onMinusCount(getAdapterPosition());
                 }else {
                     callback.onCountWeigtClick(getAdapterPosition());
                 }
             });
             flRightPlus.setOnClickListener(view -> {
                 OrderProductItem orderProductItem = (OrderProductItem) adapterItem.get(getAdapterPosition());
                 if(orderProductItem.getOrderProduct().getProduct().getMainUnit().getUnitCategory().getUnitType() == UnitCategory.PIECE) {
                     callback.onPlusCount(getAdapterPosition());
                 }else {
                     callback.onCountWeigtClick(getAdapterPosition());
                 }
             });
             llProduct.setOnClickListener(view -> {
                 callback.onOrderProductClick(getAdapterPosition());
             });

         }
         public void clearAnimation(){
             llProduct.clearAnimation();
         }
     }

    class OrderDiscountViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvDiscountName)
        TextView tvDiscountName;
        @BindView(R.id.tvDiscountAmount)
        TextView tvDiscountAmount;
        @BindView(R.id.isLastItemGone)
        FrameLayout isLastItemGone;
        @BindView(R.id.llDiscount)
        LinearLayout llDiscount;
        public OrderDiscountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            llDiscount.setOnClickListener(view -> {
                callback.onOrderDiscountClick();
            });
        }
        public void clearAnimation(){
            llDiscount.clearAnimation();
        }
    }


    @Override
    public void onViewDetachedFromWindow(final RecyclerView.ViewHolder holder)
    {
        if(holder instanceof OrderProductViewHolder)
        ((OrderProductViewHolder)holder).clearAnimation();
        else if(holder instanceof OrderDiscountViewHolder)
            ((OrderDiscountViewHolder)holder).clearAnimation();
        else if(holder instanceof OrderServiceFeeViewHolder)
            ((OrderServiceFeeViewHolder)holder).clearAnimation();
    }

    class OrderServiceFeeViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvServiceFeeName)
        TextView tvServiceFeeName;
        @BindView(R.id.tvServiceFeeAmount)
        TextView tvServiceFeeAmount;
        @BindView(R.id.isLastItemGone)
        FrameLayout isLastItemGone;
        @BindView(R.id.llServiceFee)
        LinearLayout llServiceFee;
        public OrderServiceFeeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            llServiceFee.setOnClickListener(view -> {
                callback.onOrderServiceFeeClick();
            });
        }
        public void clearAnimation(){
            llServiceFee.clearAnimation();
        }
    }
}
