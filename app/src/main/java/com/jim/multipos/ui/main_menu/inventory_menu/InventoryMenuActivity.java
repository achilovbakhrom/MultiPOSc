package com.jim.multipos.ui.main_menu.inventory_menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.db.model.intosystem.TitleDescription;
import com.jim.multipos.di.BaseAppComponent;
import com.jim.multipos.ui.HasComponent;
import com.jim.multipos.ui.main_menu.MenuListAdapter;
import com.jim.multipos.ui.main_menu.inventory_menu.di.InventoryMenuComponent;
import com.jim.multipos.ui.main_menu.inventory_menu.di.InventoryMenuModule;
import com.jim.multipos.ui.main_menu.inventory_menu.presenters.InventoryMenuPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DEV on 09.08.2017.
 */

public class InventoryMenuActivity extends BaseActivity implements HasComponent<InventoryMenuComponent>, InventoryMenuView {
    @BindView(R.id.rvMenu)
    RecyclerView rvMenu;
    @BindView(R.id.btnBackToMain)
    MpButton btnBackToMain;
    @Inject
    InventoryMenuPresenter presenter;
    private InventoryMenuComponent inventoryMenuComponent;
    final int INVENTORY_MENU = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.product_menu_layout);
        ButterKnife.bind(this);
        String title[] = getResources().getStringArray(R.array.inventory_menu_title);
        String description[] = getResources().getStringArray(R.array.inventory_menu_description);
        presenter.init(this);
        presenter.setRecyclerViewItems(title, description);
    }

    @Override
    protected void setupComponent(BaseAppComponent baseAppComponent) {
        inventoryMenuComponent = baseAppComponent.plus(new InventoryMenuModule(this));
        inventoryMenuComponent.inject(this);
    }

    @Override
    public InventoryMenuComponent getComponent() {
        return inventoryMenuComponent;
    }

    @OnClick(R.id.btnBackToMain)
    public void onBack() {
        finish();
    }


    @Override
    public void setRecyclerView(ArrayList<TitleDescription> titleDescriptions) {
        rvMenu.setLayoutManager(new LinearLayoutManager(this));
        MenuListAdapter adapter = new MenuListAdapter(this, titleDescriptions, presenter, INVENTORY_MENU);
        rvMenu.setAdapter(adapter);
    }

    @Override
    public void openActivity(int position) {
        //TODO add activities for opening
        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
}
