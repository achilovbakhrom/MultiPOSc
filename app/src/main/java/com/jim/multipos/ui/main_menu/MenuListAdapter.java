package com.jim.multipos.ui.main_menu;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.data.db.model.intosystem.TitleDescription;
import com.jim.multipos.ui.main_menu.customers_menu.presenters.CustomersMenuPresenter;
import com.jim.multipos.ui.main_menu.employer_menu.presenters.EmployerMenuPresenter;
import com.jim.multipos.ui.main_menu.inventory_menu.presenters.InventoryMenuPresenter;
import com.jim.multipos.ui.main_menu.product_menu.presenters.ProductMenuPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;


/**
 * Created by DEV on 08.08.2017.
 */

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuViewHolder> {
    private Context context;
    private ArrayList<TitleDescription> descriptions;
    private ProductMenuPresenter productMenuPresenter;
    private CustomersMenuPresenter customersMenuPresenter;
    private InventoryMenuPresenter inventoryMenuPresenter;
    private EmployerMenuPresenter employerMenuPresenter;
    private int menu = -1;

    public MenuListAdapter(Context context, ArrayList<TitleDescription> list, ProductMenuPresenter presenter, int menu) {
        this.context = context;
        this.descriptions = list;
        this.productMenuPresenter = presenter;
        this.menu = menu;
    }

    public MenuListAdapter(Context context, ArrayList<TitleDescription> list, CustomersMenuPresenter presenter, int menu) {
        this.context = context;
        this.descriptions = list;
        this.customersMenuPresenter = presenter;
        this.menu = menu;
    }

    public MenuListAdapter(Context context, ArrayList<TitleDescription> list, InventoryMenuPresenter presenter, int menu) {
        this.context = context;
        this.descriptions = list;
        this.inventoryMenuPresenter = presenter;
        this.menu = menu;
    }

    public MenuListAdapter(Context context, ArrayList<TitleDescription> list, EmployerMenuPresenter presenter, int menu) {
        this.context = context;
        this.descriptions = list;
        this.employerMenuPresenter = presenter;
        this.menu = menu;
    }

    @Override
    public MenuListAdapter.MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuListAdapter.MenuViewHolder holder, int position) {
        holder.tvMenuItem.setText(descriptions.get(position).getTitle());
        holder.tvMenuItemDescription.setText(descriptions.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return descriptions.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvMenuItem)
        TextView tvMenuItem;
        @BindView(R.id.tvMenuItemDescription)
        TextView tvMenuItemDescription;
        @BindView(R.id.llMenuItem)
        LinearLayout llMenuItem;

        public MenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.llMenuItem)
        public void onItemClick() {
            switch (menu) {
                case 0:
                    productMenuPresenter.setItemPosition(getAdapterPosition());
                    break;
                case 1:
                    customersMenuPresenter.setItemPosition(getAdapterPosition());
                    break;
                case 2:
                    inventoryMenuPresenter.setItemPosition(getAdapterPosition());
                    break;
                case 3:
                    employerMenuPresenter.setItemPosition(getAdapterPosition());
                    break;
            }
        }

        @OnTouch(R.id.llMenuItem)
        public boolean onItemTouch(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    llMenuItem.setBackgroundColor(context.getResources().getColor(R.color.colorGreyLightSecond));
                    return false;
                case MotionEvent.ACTION_UP:
                    llMenuItem.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                    return false;
                case MotionEvent.ACTION_CANCEL:
                    llMenuItem.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                    return false;
            }
            return false;
        }
    }
}
