package com.jim.multipos.ui.mainpospage.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.intosystem.FolderItem;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.utils.GlideApp;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 12.10.2017.
 */

public class FolderViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final DecimalFormat decimalFormat;
    private OnFolderItemClickListener listener;
    private List<FolderItem> items;
    private int mode = 0;
    private Context context;
    private static final int CATEGORY = 0;
    private static final int SUBCATEGORY = 1;
    private static final int PRODUCT = 2;

    public FolderViewAdapter(List<FolderItem> items, int mode, Context context) {
        this.items = items;
        this.mode = mode;
        this.context = context;
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof FolderViewHolder) {
            FolderViewHolder folderViewHolder = ((FolderViewHolder) holder);
            folderViewHolder.tvFolderItemName.setText(items.get(position).getCategory().getName());
            folderViewHolder.tvFolderItemSize.setText(items.get(position).getCount() + " " + context.getString(R.string.products_number_product));
        } else {
            ProductViewHolder productViewHolder = (ProductViewHolder) holder;
            Product product = items.get(position).getProduct();
            productViewHolder.tvProductName.setText(product.getName());
            productViewHolder.tvProductSKU.setText(context.getString(R.string.sku_) + product.getSku());
            productViewHolder.tvProductPrice.setText(decimalFormat.format(product.getPrice()) + " " + product.getPriceCurrency().getAbbr());
            if (!product.getPhotoPath().equals("")) {
                Uri photoSelected = Uri.fromFile(new File(product.getPhotoPath()));
                GlideApp.with(context).load(photoSelected).diskCacheStrategy(DiskCacheStrategy.RESOURCE).thumbnail(0.2f).centerCrop().into(productViewHolder.ivProductImage);
            } else productViewHolder.ivProductImage.setImageResource(R.drawable.basket);
        }

    }

    public void setItems(List<FolderItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view;
        switch (viewType) {
            case SUBCATEGORY:
            case CATEGORY:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.folder_view_item, viewGroup, false);
                holder = new FolderViewHolder(view);
                break;
            case PRODUCT:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.folder_product_view_item, viewGroup, false);
                holder = new ProductViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return mode;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setListener(OnFolderItemClickListener listener) {
        this.listener = listener;
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvFolderItemName)
        TextView tvFolderItemName;
        @BindView(R.id.tvFolderItemSize)
        TextView tvFolderItemSize;

        public FolderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> listener.onItemClick(items.get(getAdapterPosition()), getAdapterPosition()));
        }
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.tvProductSKU)
        TextView tvProductSKU;
        @BindView(R.id.tvProductPrice)
        TextView tvProductPrice;
        @BindView(R.id.ivProductImage)
        ImageView ivProductImage;
        @BindView(R.id.llInfo)
        LinearLayout llInfo;

        public ProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> listener.onItemClick(items.get(getAdapterPosition()), getAdapterPosition()));
            llInfo.setOnClickListener(v -> {
                listener.onInfoClicked(items.get(getAdapterPosition()),getAdapterPosition());
            });
        }
    }

    public interface OnFolderItemClickListener {
        void onItemClick(FolderItem folderItem, int position);
        void onInfoClicked(FolderItem folderItem, int position);
    }


}
