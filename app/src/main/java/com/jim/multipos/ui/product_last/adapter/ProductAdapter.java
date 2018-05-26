package com.jim.multipos.ui.product_last.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.mpviews.MPAddItemView;
import com.jim.mpviews.MPListItemView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.core.MovableBaseAdapter;
import com.jim.multipos.data.db.model.products.Product;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Achilov Bakhrom on 11/2/17.
 */
public class ProductAdapter extends MovableBaseAdapter<Product, BaseViewHolder> {

    public static final int ADD = 0, ITEM = 1;

    public ProductAdapter(List<Product> items) {
        super(items);

    }

    @Override
    protected void onItemClicked(BaseViewHolder holder, int position) {
        notifyDataSetChanged();
        //TODO while nothing

    }

    public List<Product> getItems(){
        return this.items;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = null;
        View view;
        switch (viewType) {
            case ADD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_add_item, parent, false);
                holder = new AddViewHolder(view);
                break;
            case ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent, false);
                holder = new ItemViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder item = ((ItemViewHolder) holder);
            item.itemView.setActivate(position == selectedPosition);
            item.itemView.setText(items.get(position).getName());
            item.itemView.makeDeleteable(!items.get(position).isActive());
            item.itemView.setTextSize(12);
        } else if (holder instanceof AddViewHolder){
            AddViewHolder item = (AddViewHolder) holder;
            item.itemView.setTextSize(12);
        }
    }

    @Override
    protected boolean isSinglePositionClickDisabled() {
        return true;
    }

    public void editItem(Product product) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) != null && product.getId().equals(items.get(i).getId())) {
                items.set(i, product);
                sort();
                break;
            }
        }
    }

    public Product getSelectedItem() {
        return items.get(selectedPosition);
    }

    public void setSelectedPositionWithId(Long id) {
        for (Product product : items) {
            if (product == null) continue;
            if (product.getId().equals(id)) {
                this.selectedPosition = items.indexOf(product);
                notifyDataSetChanged();
                break;
            }
        }
    }

    private void sort() {
        Collections.sort(items, (o1, o2) -> {
            if (o1 != null && o2 != null)
                return o1.getPosition().compareTo(o2.getPosition());
            return -1;
        });
        Collections.sort(items, (o1, o2) -> {
            if (o1 != null && o2 != null)
                return -((Boolean) o1.isActive()).compareTo(o2.isActive());
            return -1;
        });
        notifyDataSetChanged();
    }
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public void addItems(List<Product> items) {
        super.addItems(items);
        sort();
    }

    @Override
    public void addItem(Product item) {
        super.addItem(item);
        sort();
    }

    @Override
    public void removeItem(int position) {
        super.removeItem(position);
        sort();
    }

    @Override
    public void removeItem(Product item) {
        super.removeItem(item);
        sort();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ADD : ITEM;
    }

    public void unselect() {
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    class AddViewHolder extends BaseViewHolder {

        @BindView(R.id.aivAddItem)
        MPAddItemView itemView;

        AddViewHolder(View itemView) {
            super(itemView);

        }
    }

    class ItemViewHolder extends BaseViewHolder {
        @BindView(R.id.aivItem)
        MPListItemView itemView;

        ItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
