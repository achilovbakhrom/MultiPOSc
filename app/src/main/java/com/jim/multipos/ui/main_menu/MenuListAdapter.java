package com.jim.multipos.ui.main_menu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.data.db.model.intosystem.TitleDescription;
import com.jim.multipos.utils.OnItemClickListener;

import java.util.List;

import butterknife.BindView;


/**
 * Created by DEV on 08.08.2017.
 */

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuViewHolder> {
    List<TitleDescription> items;
    public MenuListAdapter(List<TitleDescription> items) {
        this.items = items;
    }

    @Override
    public MenuListAdapter.MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        holder.tvMenuItem.setText(items.get(position).getTitle());
        holder.tvMenuItemDescription.setText(items.get(position).getDescription());
    }

    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class MenuViewHolder extends BaseViewHolder {
        @BindView(R.id.tvMenuItem)
        TextView tvMenuItem;
        @BindView(R.id.tvMenuItemDescription)
        TextView tvMenuItemDescription;

        public MenuViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(view1 -> {
                if(onItemClickListener!=null)
                    onItemClickListener.onItemClicked(getAdapterPosition());
            });
           }
    }
}
