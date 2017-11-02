package com.jim.multipos.ui.main_menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseViewHolder;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.db.model.intosystem.TitleDescription;

import java.util.List;

import butterknife.BindView;


/**
 * Created by DEV on 08.08.2017.
 */

public class MenuListAdapter extends ClickableBaseAdapter<TitleDescription, MenuListAdapter.MenuViewHolder> {

    public MenuListAdapter(List<TitleDescription> items) {
        super(items);
    }

    @Override
    public MenuListAdapter.MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.tvMenuItem.setText(items.get(position).getTitle());
        holder.tvMenuItemDescription.setText(items.get(position).getDescription());
    }

    @Override
    protected void onItemClicked(MenuViewHolder holder, int position) {
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
        }
    }
}
