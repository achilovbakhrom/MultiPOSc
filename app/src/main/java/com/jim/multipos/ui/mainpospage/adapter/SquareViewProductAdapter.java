package com.jim.multipos.ui.mainpospage.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.mpviews.MPListItemView;
import com.jim.mpviews.MpListItem;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Sirojiddin on 12.10.2017.
 */

public class SquareViewProductAdapter extends RecyclerView.Adapter<SquareViewProductAdapter.SquareCategoryViewHolder> {
    List<Product> items;
    private OnItemClickListener onItemClickListener;

    public SquareViewProductAdapter(List items,OnItemClickListener onItemClickListener) {
        this.items = items;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(SquareCategoryViewHolder holder, int position) {
        holder.mpSquareItem.setTextSize(12);
        holder.mpSquareItem.setText(items.get(position).getName());
    }



    @Override
    public SquareCategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.square_view_product_item, viewGroup, false);
        return new SquareCategoryViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
        void onItemClicked(Product item);
    }

    public void setItems(List<Product> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public class SquareCategoryViewHolder extends BaseViewHolder {
        @BindView(R.id.mpSquareItem)
        MpListItem mpSquareItem;
        public SquareCategoryViewHolder(View itemView) {
            super(itemView);
            mpSquareItem.setOnClickListener(view -> {
                onItemClickListener.onItemClicked(items.get(getAdapterPosition()));
            });
        }
    }
}
