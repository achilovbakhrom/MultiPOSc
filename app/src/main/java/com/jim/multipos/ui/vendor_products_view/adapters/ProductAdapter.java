package com.jim.multipos.ui.vendor_products_view.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseAdapter;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.inventory.model.InventoryItem;
import com.jim.multipos.ui.vendor_products_view.VendorProductsView;
import com.jim.multipos.ui.vendor_products_view.dialogs.MinusInventoryDialog;
import com.jim.multipos.ui.vendor_products_view.dialogs.PlusInventoryDialog;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Portable-Acer on 18.11.2017.
 */

public class ProductAdapter extends BaseAdapter<InventoryItem, ProductAdapter.ProductViewHolder> {
    public interface ProductAdapterListener {
        void showMinusDialog(int position);
        void showPlusDialog(int position);
        ProductClass getProductClass(Long id);
    }

    private Context context;
    private ProductAdapterListener listener;
    private DecimalFormat decimalFormat;

    public ProductAdapter(Context context, DecimalFormat decimalFormat, ProductAdapterListener listener, List<InventoryItem> items) {
        super(items);
        this.context = context;
        this.listener = listener;
        this.decimalFormat = decimalFormat;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_details_item, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        holder.tvName.setText(items.get(position).getProduct().getName());
        holder.tvBarcode.setText(items.get(position).getProduct().getBarcode());
        holder.tvSku.setText(items.get(position).getProduct().getSku());
        holder.tvInventory.setText(decimalFormat.format(items.get(position).getInventory()));
        holder.tvUnit.setText(items.get(position).getProduct().getMainUnit().getAbbr());
        holder.tvClass.setText("");
        holder.tvSubClass.setText("");

        if (items.get(position).getProduct().getProductClass().getParentId() != null) {
            ProductClass parentProductClass = listener.getProductClass(items.get(position).getProduct().getProductClass().getParentId());

            if (parentProductClass.getName().length() > 9) {
                holder.tvClass.setText(parentProductClass.getName().substring(0, 9) + "..., ");
            } else {
                holder.tvClass.setText(parentProductClass.getName() + ", ");
            }

            if (items.get(position).getProduct().getProductClass().getName().length() > 9) {
                holder.tvSubClass.setText(items.get(position).getProduct().getProductClass().getName().substring(0, 9) + "...");
            } else {
                holder.tvSubClass.setText(items.get(position).getProduct().getProductClass().getName());
            }
        } else {
            if (items.get(position).getProduct().getProductClass().getName().length() > 9) {
                holder.tvClass.setText(items.get(position).getProduct().getProductClass().getName() + "...");
            } else {
                holder.tvClass.setText(items.get(position).getProduct().getProductClass().getName());
            }
        }

        if (position % 2 == 0) {
            holder.llcontainer.setBackgroundColor(Color.parseColor("#f9f9f9"));
        } else {
            holder.llcontainer.setBackgroundColor(Color.parseColor("#ededed"));
        }
    }

    public void updateItem(InventoryItem item) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProduct().getName().equals(item.getProduct().getName())) {
                items.get(i).setInventory(item.getInventory());
                notifyItemChanged(i);
                break;
            }
        }
    }

    class ProductViewHolder extends BaseViewHolder {
        @BindView(R.id.llcontainer)
        LinearLayout llcontainer;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvClass)
        TextView tvClass;
        @BindView(R.id.tvSubClass)
        TextView tvSubClass;
        @BindView(R.id.tvBarcode)
        TextView tvBarcode;
        @BindView(R.id.tvSku)
        TextView tvSku;
        @BindView(R.id.tvInventory)
        TextView tvInventory;
        @BindView(R.id.tvUnit)
        TextView tvUnit;
        @BindView(R.id.ivMinus)
        ImageView ivMinus;
        @BindView(R.id.ivPlus)
        ImageView ivPlus;

        public ProductViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

           tvName.setPaintFlags(tvName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

           ivMinus.setOnClickListener(v -> {
               listener.showMinusDialog(getAdapterPosition());
           });

           ivPlus.setOnClickListener(v -> {
               listener.showPlusDialog(getAdapterPosition());
           });
        }
    }
}
