package com.jim.multipos.ui.main_menu.product_menu;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.intosystem.TitleDescription;
import com.jim.multipos.ui.discount.DiscountAddingActivity;
import com.jim.multipos.ui.main_menu.MenuListAdapter;
import com.jim.multipos.ui.main_menu.product_menu.dialogs.ProductExportDialog;
import com.jim.multipos.ui.main_menu.product_menu.dialogs.ImportDialog;
import com.jim.multipos.ui.main_menu.product_menu.presenters.ProductMenuPresenter;
import com.jim.multipos.ui.product_class_new.ProductsClassActivity;
import com.jim.multipos.ui.product_last.ProductActivity;
import com.jim.multipos.ui.service_fee_new.ServiceFeeActivity;
import com.jim.multipos.ui.vendor.add_edit.VendorAddEditActivity;
import com.jim.multipos.utils.RxBus;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DEV on 08.08.2017.
 */

public class ProductMenuActivity extends BaseActivity implements ProductMenuView {

    @BindView(R.id.rvMenu)
    RecyclerView rvProductMenu;
    @BindView(R.id.btnBackToMain)
    MpButton btnBackToMain;
    @BindView(R.id.mpToolBar)
    MpToolbar mpToolbar;
    @Inject
    ProductMenuPresenter presenter;
    @Inject
    DatabaseManager databaseManager;
    @Inject
    RxBus rxBus;
    @Inject
    RxPermissions permissions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_menu_layout);
        ButterKnife.bind(this);
        mpToolbar.setMode(MpToolbar.DEFAULT_TYPE);
        String title[] = getResources().getStringArray(R.array.products_menu_title);
        String description[] = getResources().getStringArray(R.array.products_menu_description);
        presenter.setRecyclerViewItems(title, description);
    }


    @OnClick(R.id.btnBackToMain)
    public void onBack() {
        finish();
    }


    @Override
    public void setRecyclerView(ArrayList<TitleDescription> titleDescriptions) {
        rvProductMenu.setLayoutManager(new LinearLayoutManager(this));
        MenuListAdapter adapter = new MenuListAdapter(titleDescriptions);
        rvProductMenu.setAdapter(adapter);
        adapter.setOnItemClickListener(new ClickableBaseAdapter.OnItemClickListener<TitleDescription>() {
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
        //TODO add activities for opening
        switch (position) {
            case 0:
                Intent intent = new Intent(this, ProductActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intentVendor = new Intent(this, VendorAddEditActivity.class);
                startActivity(intentVendor);
                break;
            case 2:
                Intent intentProdActivity = new Intent(this, ProductsClassActivity.class);
                startActivity(intentProdActivity);
                break;
            case 3:
                Intent intentDiscount = new Intent(this, DiscountAddingActivity.class);
                startActivity(intentDiscount);
                break;
            case 4:
                Intent intentServiceFee = new Intent(this, ServiceFeeActivity.class);
                startActivity(intentServiceFee);
                break;
            case 5:
                permissions.request( Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(aBoolean -> {
                    if (aBoolean) {
                        ProductExportDialog dialog = new ProductExportDialog(this, databaseManager);
                        dialog.show();
                    }
                });
                break;
            case 6:
                ImportDialog importDialog = new ImportDialog(this, databaseManager, rxBus);
                importDialog.show();
                break;

        }
    }
}
