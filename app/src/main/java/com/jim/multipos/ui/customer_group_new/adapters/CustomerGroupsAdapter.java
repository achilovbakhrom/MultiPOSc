package com.jim.multipos.ui.customer_group_new.adapters;

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
import com.jim.mpviews.MPListItemView;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.customer.CustomerGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DEV on 17.08.2017.
 */

public class CustomerGroupsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CustomerGroup> list;
    private int selectedPosition = 0;
    private OnItemClickListener onItemClickListener;
    private static final int ADD_ITEM = 0;
    private static final int DYNAMIC_ITEMS = 1;

    public interface OnItemClickListener {
        void onAddButtonPressed();

        void onItemPressed(int t, int prevPosition);
    }

    public void setToDefault() {
        selectedPosition = 0;
    }

    public CustomerGroupsAdapter(List<CustomerGroup> list, OnItemClickListener onItemClickListener, int selectedPosition) {
        this.list = list;
        this.onItemClickListener = onItemClickListener;
        this.selectedPosition = selectedPosition;

        if (!this.list.isEmpty()) {
            for (int i = list.size() - 1; i >= 0; i--) {
                if (list.get(i) == null)
                    list.remove(i);
            }
            list.add(0, null);
        } else this.list.add(null);

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ADD_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_group_list_first_item, parent, false);
            return new CustomerGroupsAdapter.FirstItemViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_group_list_item, parent, false);
            return new CustomerGroupViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CustomerGroupViewHolder) {
            CustomerGroupViewHolder productViewHolder = (CustomerGroupViewHolder) holder;
            productViewHolder.mpItem.setText(list.get(position).getName());
            if (position == selectedPosition) {
                productViewHolder.mpItem.setActivate(true);
            } else {
                productViewHolder.mpItem.setActivate(false);
            }
            if (list.get(position).getIsActive())
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
            itemViewHolder.tvFirstItem.setText("+Add\n Group");

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
        notifyDataSetChanged();
    }

    public void addItem(CustomerGroup customerGroup) {
        list.add(1, customerGroup);
        //notifyItemInserted(1);
        notifyDataSetChanged();
    }

    public void removeItem(CustomerGroup customerGroup) {
        selectedPosition = 0;
        list.remove(customerGroup);
        notifyDataSetChanged();
    }

    public void updateItem(CustomerGroup customerGroup) {
        selectedPosition = 0;
        /*int index = list.indexOf(customerGroup);
        notifyItemChanged(index);
        notifyItemChanged(0);*/
        notifyDataSetChanged();
    }


    public class CustomerGroupViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mpListItem)
        MPListItemView mpItem;
        View mainView;

        public CustomerGroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mainView = itemView;
            mpItem.setTextSize(14);
            RxView.clicks(mpItem).subscribe(aVoid -> {
                int prevPosition = selectedPosition;
                selectedPosition = getAdapterPosition();
                notifyItemChanged(prevPosition);
                notifyItemChanged(selectedPosition);
                onItemClickListener.onItemPressed(selectedPosition, prevPosition);
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
            mainView = itemView;
            RxView.clicks(rlFirstItem).subscribe(aVoid -> {
                int prevPosition = selectedPosition;
                selectedPosition = getAdapterPosition();
                notifyItemChanged(prevPosition);
                notifyItemChanged(selectedPosition);
                onItemClickListener.onAddButtonPressed();
            });
        }
    }


}
