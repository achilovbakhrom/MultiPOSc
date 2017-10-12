package com.jim.multipos.ui.main_menu.product_menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.jim.mpviews.MpButton;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.di.BaseAppComponent;
import com.jim.multipos.data.db.model.intosystem.TitleDescription;
import com.jim.multipos.ui.HasComponent;
import com.jim.multipos.ui.main_menu.MenuListAdapter;
import com.jim.multipos.ui.main_menu.product_menu.di.ProductMenuComponent;
import com.jim.multipos.ui.main_menu.product_menu.presenters.ProductMenuPresenter;
import com.jim.multipos.ui.product_class.ProductClassActivity;
import com.jim.multipos.ui.product.ProductsActivity;
import com.jim.multipos.ui.service_fee.ServiceFeeActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DEV on 08.08.2017.
 */

public class ProductMenuActivity extends BaseActivity implements ProductMenuView, HasComponent<ProductMenuComponent> {

    @BindView(R.id.rvMenu)
    RecyclerView rvProductMenu;
    @BindView(R.id.btnBackToMain)
    MpButton btnBackToMain;
    @Inject
    ProductMenuPresenter presenter;
    private ProductMenuComponent productMenuComponent;
    final int PRODUCTS_MENU = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.product_menu_layout);
        ButterKnife.bind(this);
        String title[] = getResources().getStringArray(R.array.products_menu_title);
        String description[] = getResources().getStringArray(R.array.products_menu_description);
        presenter.init(this);
        presenter.setRecyclerViewItems(title, description);
    }

    protected void setupComponent(BaseAppComponent baseAppComponent) {
//        productMenuComponent = baseAppComponent.plus(new ProductMenuModule(this));
        productMenuComponent.inject(this);
    }

    @Override
    public ProductMenuComponent getComponent() {
        return productMenuComponent;
    }

    @OnClick(R.id.btnBackToMain)
    public void onBack() {
        finish();
    }


    @Override
    public void setRecyclerView(ArrayList<TitleDescription> titleDescriptions) {
        rvProductMenu.setLayoutManager(new LinearLayoutManager(this));
        MenuListAdapter adapter = new MenuListAdapter(this, titleDescriptions, presenter, PRODUCTS_MENU);
        rvProductMenu.setAdapter(adapter);
    }

    @Override
    public void openActivity(int position) {
        //TODO add activities for opening
        switch (position) {
            case 0:
                Intent intent = new Intent(this, ProductsActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intentProdActivity = new Intent(this, ProductClassActivity.class);
                startActivity(intentProdActivity);
                break;
            case 2:

                break;
            case 3:
                Intent serviceFee = new Intent(this, ServiceFeeActivity.class);
                startActivity(serviceFee);
                break;

        }
    }
}
