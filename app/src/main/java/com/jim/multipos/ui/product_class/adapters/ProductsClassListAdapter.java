package com.jim.multipos.ui.product_class.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jim.mpviews.MpItem;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.intosystem.NameId;
import com.jim.multipos.data.db.model.intosystem.NamePhotoPathId;
import com.jim.multipos.ui.product_class.presenters.ProductClassListPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DEV on 17.08.2017.
 */

public class ProductsClassListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<NameId> list;
    private int selectedPosition = 0;
    private ProductsClassListAdapter.onItemClickListner onItemClickListner;
    private static final int ADD_ITEM = 0;
    private static final int DYNAMIC_ITEMS = 1;
    public interface onItemClickListner{
        void onAddButtonPressed();
        void onItemPressed(int t);
    }
    public void setToDefault(){
        selectedPosition = 0;
    }
    public ProductsClassListAdapter(List<? extends NameId> list,onItemClickListner onItemClickListner) {
        this.list = (List<NameId>) list;

        this.onItemClickListner = onItemClickListner;

        if (!this.list.isEmpty()) {
            for (int i = list.size()-1; i >=0; i--) {
                if(list.get(i)==null)
                    list.remove(i);
            }
            list.add(0,null);
        } else this.list.add(null);

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ADD_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_first_item, parent, false);
            return new ProductsClassListAdapter.FirstItemViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
            return new ProductsClassListAdapter.ProductListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProductListViewHolder) {
            ProductListViewHolder productViewHolder = (ProductListViewHolder) holder;
            productViewHolder.mpItem.setText(list.get(position).getName());
            if (position == selectedPosition) {
                productViewHolder.mpItem.setBackgroundResource(R.drawable.item_pressed_bg);
            } else {
                productViewHolder.mpItem.setBackgroundResource(R.drawable.item_bg);
            }
            if(list.get(position).isActive())
                productViewHolder.mainView.setAlpha(1f);
            else
                productViewHolder.mainView.setAlpha(0.5f);


        } else if (holder instanceof FirstItemViewHolder) {
            FirstItemViewHolder itemViewHolder = (FirstItemViewHolder) holder;
            if (position == selectedPosition) {
                itemViewHolder.tvFirstItem.setTextColor(Color.parseColor("#419fd9"));
                itemViewHolder.ivItemBg.setImageTintList(ColorStateList.valueOf(Color.parseColor("#419fd9")));
            } else {
                itemViewHolder.tvFirstItem.setTextColor(Color.parseColor("#cccccc"));
                itemViewHolder.ivItemBg.setImageTintList(ColorStateList.valueOf(Color.parseColor("#cccccc")));
            }
                    itemViewHolder.tvFirstItem.setText("+Add\n Class");

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) == null) {
            return ADD_ITEM;
        } else return DYNAMIC_ITEMS;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setPosition(int position) {
        this.selectedPosition = position;
    }






    public class ProductListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mpListItem)
        MpItem mpItem;
        View mainView;
        public ProductListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mainView =itemView;
            RxView.clicks(mpItem).subscribe(aVoid -> {
                int prevPosition = selectedPosition;
                selectedPosition = getAdapterPosition();
                notifyItemChanged(prevPosition);
                notifyItemChanged(selectedPosition);
                onItemClickListner.onItemPressed(selectedPosition);
            });
        }

    }

    public class FirstItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rlFirstItemBg)
        RelativeLayout rlFirstItem;
        @BindView(R.id.tvFirstItem)
        TextView tvFirstItem;
        @BindView(R.id.ivItemBg)
        ImageView ivItemBg;
        View mainView;

        public FirstItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mainView =itemView;
            RxView.clicks(rlFirstItem).subscribe(aVoid -> {
                int prevPosition = selectedPosition;
                selectedPosition = getAdapterPosition();
                notifyItemChanged(prevPosition);
                notifyItemChanged(selectedPosition);
                onItemClickListner.onAddButtonPressed();

            });
        }
    }


}
