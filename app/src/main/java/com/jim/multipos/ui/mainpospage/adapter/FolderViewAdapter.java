package com.jim.multipos.ui.mainpospage.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.db.model.intosystem.FolderItem;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.utils.GlideApp;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Sirojiddin on 12.10.2017.
 */

public class FolderViewAdapter extends ClickableBaseAdapter<FolderItem, BaseViewHolder> {

    private final DecimalFormat decimalFormat;
    private int mode = 0;
    private Context context;
    private static final int CATEGORY = 0;
    private static final int SUBCATEGORY = 1;
    private static final int PRODUCT = 2;

    public FolderViewAdapter(List items, int mode, Context context) {
        super(items);
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
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (holder instanceof FolderViewHolder) {
            FolderViewHolder folderViewHolder = ((FolderViewHolder) holder);
            folderViewHolder.tvFolderItemName.setText(items.get(position).getCategory().getName());
            folderViewHolder.tvFolderItemSize.setText(items.get(position).getCount() + " " + context.getString(R.string.products_number_product));
        } else {
            ProductViewHolder productViewHolder = (ProductViewHolder) holder;
            Product product = items.get(position).getProduct();
            productViewHolder.tvProductName.setText(product.getName());
            productViewHolder.tvProductSKU.setText(context.getString(R.string.sku_) + product.getSku());
            productViewHolder.tvProductQty.setText(items.get(position).getCount() + " " + product.getMainUnit().getAbbr());
            productViewHolder.tvProductPrice.setText(decimalFormat.format(product.getPrice()) + " " + product.getPriceCurrency().getAbbr());
            if (!product.getPhotoPath().equals("")){
                Uri photoSelected = Uri.fromFile(new File(product.getPhotoPath()));
                GlideApp.with(context).load(photoSelected).diskCacheStrategy(DiskCacheStrategy.RESOURCE).thumbnail(0.2f).centerCrop().transform(new RoundedCorners(20)).into(productViewHolder.ivProductImage);
            } else productViewHolder.ivProductImage.setImageResource(R.drawable.basket);
        }

    }

    @Override
    public void setItems(List<FolderItem> items) {
        super.setItems(items);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        BaseViewHolder holder = null;
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
    protected boolean isSinglePositionClickDisabled() {
        return true;
    }

    @Override
    public int getItemViewType(int position) {
        return mode;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected void onItemClicked(BaseViewHolder holder, int position) {
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public class FolderViewHolder extends BaseViewHolder {
        @BindView(R.id.tvFolderItemName)
        TextView tvFolderItemName;
        @BindView(R.id.tvFolderItemSize)
        TextView tvFolderItemSize;

        public FolderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ProductViewHolder extends BaseViewHolder {
        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.tvProductSKU)
        TextView tvProductSKU;
        @BindView(R.id.tvProductQty)
        TextView tvProductQty;
        @BindView(R.id.tvProductPrice)
        TextView tvProductPrice;
        @BindView(R.id.ivProductImage)
        ImageView ivProductImage;
        public ProductViewHolder(View itemView) {
            super(itemView);
        }
    }


}
