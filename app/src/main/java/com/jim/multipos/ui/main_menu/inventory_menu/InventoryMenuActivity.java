package com.jim.multipos.ui.main_menu.inventory_menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.db.model.intosystem.TitleDescription;
import com.jim.multipos.ui.inventory.InventoryActivity;
import com.jim.multipos.ui.main_menu.MenuListAdapter;
import com.jim.multipos.ui.main_menu.inventory_menu.presenters.InventoryMenuPresenter;
import com.jim.multipos.ui.products_expired.ExpiredProductsActivity;
import com.jim.multipos.ui.vendor_item_managment.VendorItemsActivity;
import com.jim.multipos.utils.OnItemClickListener;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DEV on 09.08.2017.
 */

public class InventoryMenuActivity extends BaseActivity implements InventoryMenuView {
    @BindView(R.id.rvMenu)
    RecyclerView rvMenu;
    @BindView(R.id.btnBackToMain)
    MpButton btnBackToMain;
    @BindView(R.id.mpToolBar)
    MpToolbar mpToolbar;
    @Inject
    InventoryMenuPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_menu_layout);
        ButterKnife.bind(this);
        mpToolbar.setMode(MpToolbar.DEFAULT_TYPE);
        String title[] = getResources().getStringArray(R.array.inventory_menu_title);
        String description[] = getResources().getStringArray(R.array.inventory_menu_description);
        presenter.setRecyclerViewItems(title, description);
    }


    @OnClick(R.id.btnBackToMain)
    public void onBack() {
        finish();
    }


    @Override
    public void setRecyclerView(ArrayList<TitleDescription> titleDescriptions) {
        rvMenu.setLayoutManager(new LinearLayoutManager(this));
        MenuListAdapter adapter = new MenuListAdapter(titleDescriptions);
        rvMenu.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener<TitleDescription>() {
            @Override
            public void onItemClicked(int position) {
                presenter.setItemPosition(position);
            }

            @Override
            public void onItemClicked(TitleDescription item) {

            }
        });
    }

    @Override
    public void openActivity(int position) {
        switch (position) {
            case 0:
                Intent intent1 = new Intent(this, InventoryActivity.class);
                startActivity(intent1);
                break;
            case 1:
                Intent intent2 = new Intent(this, VendorItemsActivity.class);
                startActivity(intent2);
                break;
            case 2:
                Intent intent = new Intent(this, ExpiredProductsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
