package com.jim.multipos.ui.vendor_products_view.adapters;

import android.content.Context;
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
import com.jim.multipos.ui.vendor_products_view.model.ProductState;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jim.multipos.data.db.model.consignment.Consignment.INCOME_CONSIGNMENT;
import static com.jim.multipos.data.db.model.consignment.Consignment.RETURN_CONSIGNMENT;

/**
 * Created by Portable-Acer on 18.11.2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public interface ProductAdapterListener {
        void onInvoiceClick(ProductState state);
        void onOutvoiceClick(ProductState state);
    }

    private Context context;
    private ProductAdapterListener listener;
    private DecimalFormat decimalFormat;
    private List<ProductState> items;
    public ProductAdapter(Context context, DecimalFormat decimalFormat, ProductAdapterListener listener, List<ProductState> items) {
        this.context = context;
        this.listener = listener;
        this.decimalFormat = decimalFormat;
        this.items = items;
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
        holder.tvInventory.setText(decimalFormat.format(items.get(position).getValue()));
        holder.tvUnit.setText(items.get(position).getProduct().getMainUnit().getAbbr());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.llcontainer)
        LinearLayout llcontainer;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvBarcode)
        TextView tvBarcode;
        @BindView(R.id.tvSku)
        TextView tvSku;
        @BindView(R.id.tvInventory)
        TextView tvInventory;
        @BindView(R.id.tvUnit)
        TextView tvUnit;

        @BindView(R.id.ivIncome)
        ImageView ivIncome;
        @BindView(R.id.ivReturn)
        ImageView ivReturn;

        public ProductViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            tvName.setPaintFlags(tvName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            ivIncome.setOnClickListener(v -> {
                listener.onInvoiceClick(items.get(getAdapterPosition()));
            });

            ivReturn.setOnClickListener(v -> {
                listener.onOutvoiceClick(items.get(getAdapterPosition()));
            });
        }
    }
}
