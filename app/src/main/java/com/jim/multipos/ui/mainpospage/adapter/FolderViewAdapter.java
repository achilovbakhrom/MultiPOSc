package com.jim.multipos.ui.mainpospage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.mpviews.MPListItemView;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.db.model.intosystem.FolderItem;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Sirojiddin on 12.10.2017.
 */

public class FolderViewAdapter extends ClickableBaseAdapter<FolderItem, FolderViewAdapter.FolderViewHolder> {

    private int mode = 0;

    public FolderViewAdapter(List items, int mode) {
        super(items);
        this.mode = mode;
    }

    @Override
    public void onBindViewHolder(FolderViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        switch (mode) {
            case 0:
                holder.tvFolderItemName.setText(items.get(position).getCategory().getName());
//                holder.tvFolderItemSize.setText(items.get(position).getSize());
                break;
            case 1:
                holder.tvFolderItemName.setText(items.get(position).getCategory().getName());
//                holder.tvFolderItemSize.setText(items.get(position).getSize());
                break;
            case 2:
                holder.tvFolderItemName.setText(items.get(position).getProduct().getName());
//                holder.tvFolderItemSize.setText("");
                break;
        }
    }

    @Override
    public void setItems(List<FolderItem> items) {
        super.setItems(items);
    }

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.folder_view_item, viewGroup, false);
        return new FolderViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    protected void onItemClicked(FolderViewHolder holder, int position) {
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
}
