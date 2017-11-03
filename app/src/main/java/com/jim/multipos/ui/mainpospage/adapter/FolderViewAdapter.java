package com.jim.multipos.ui.mainpospage.adapter;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.db.model.intosystem.FolderItem;
import com.jim.multipos.data.db.model.products.Product;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Sirojiddin on 12.10.2017.
 */

public class FolderViewAdapter extends ClickableBaseAdapter<FolderItem, BaseViewHolder> {

    private int mode = 0;
    private static final int CATEGORY = 0;
    private static final int SUBCATEGORY = 1;
    private static final int PRODUCT = 2;

    public FolderViewAdapter(List items, int mode) {
        super(items);
        this.mode = mode;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (holder instanceof FolderViewHolder) {
            FolderViewHolder folderViewHolder = ((FolderViewHolder) holder);
            folderViewHolder.tvFolderItemName.setText(items.get(position).getCategory().getName());
            folderViewHolder.tvFolderItemSize.setText(items.get(position).getSize() + " products");
        } else {
            ProductViewHolder productViewHolder = (ProductViewHolder) holder;
            Product product = items.get(position).getProduct();
            productViewHolder.tvProductName.setText(product.getName());
            productViewHolder.tvProductSKU.setText("SKU: " + product.getSku());
            productViewHolder.tvProductQty.setText("100.0 pcs");
            productViewHolder.tvProductPrice.setText(product.getPrice() + " sum");
//            if (!product.getPhotoPath().equals("")){
//                //TODO load product image
//            }
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
