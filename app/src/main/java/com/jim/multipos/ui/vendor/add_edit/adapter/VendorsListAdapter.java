package com.jim.multipos.ui.vendor.add_edit.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jim.mpviews.MpLongItemWithList;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.db.model.products.Vendor;

import java.util.List;

import butterknife.BindView;

/**
 * Created by bakhrom on 10/23/17.
 */

public class VendorsListAdapter extends ClickableBaseAdapter<Vendor, BaseViewHolder> {
    private static final int ADD = 0, ITEM = 1;
    public VendorsListAdapter(List<Vendor> items) {
        super(items);
        selectedPosition = 0;
    }
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        BaseViewHolder holder;
        if (viewType == ADD) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_list_add_item, parent, false);
            holder = new AddItemViewHolder(view);
        }
        else  {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_list_item, parent, false);
            holder = new VendorViewHolder(view);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof VendorViewHolder) {
            ((VendorViewHolder) holder).item.setFirstItemText(items.get(position).getName());
            ((VendorViewHolder) holder).item.setSecondItemText("Items: " + items.get(position).getProducts().size());
            ((VendorViewHolder) holder).item.setThirdItemText(items.get(position).getContactName());
            ((VendorViewHolder) holder).item.setTextSize(12);
            ((VendorViewHolder) holder).item.setActivate(position == selectedPosition);
            ((VendorViewHolder) holder).item.makeDeletable(!items.get(position).getIsActive());
        } else {
            ((AddItemViewHolder) holder).item.setVisibility(View.VISIBLE, View.GONE, View.GONE);
            ((AddItemViewHolder) holder).item.setTextColor(R.color.colorGreenSecond);
            ((AddItemViewHolder) holder).item.setTextSize(14);

        }
    }

    @Override
    protected void onItemClicked(BaseViewHolder holder, int position) {
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? ADD : ITEM;
    }

    class VendorViewHolder extends BaseViewHolder {
        @BindView(R.id.aivItem)
        MpLongItemWithList item;
        VendorViewHolder(View itemView) { super(itemView); }
    }

    class AddItemViewHolder extends BaseViewHolder {
        @BindView(R.id.aivAddItem)
        MpLongItemWithList item;
        AddItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void select(int pos) {
        selectedPosition = pos;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
