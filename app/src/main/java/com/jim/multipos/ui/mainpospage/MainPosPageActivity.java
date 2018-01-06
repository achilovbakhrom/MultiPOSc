package com.jim.multipos.ui.mainpospage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextClock;
import android.widget.TextView;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.core.MainPageDoubleSideActivity;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.customer.Customer;
import com.jim.multipos.ui.main_menu.customers_menu.CustomersMenuActivity;
import com.jim.multipos.ui.main_menu.inventory_menu.InventoryMenuActivity;
import com.jim.multipos.ui.main_menu.product_menu.ProductMenuActivity;
import com.jim.multipos.ui.mainpospage.view.CustomerNotificationsFragment;
import com.jim.multipos.ui.mainpospage.view.OrderListFragment;
import com.jim.multipos.ui.mainpospage.view.ProductPickerFragment;
import com.jim.multipos.ui.mainpospage.view.SearchModeFragment;
import com.jim.multipos.utils.MainMenuDialog;
import com.jim.multipos.utils.OrderMenuDialog;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.TestUtils;
import com.jim.multipos.utils.managers.NotifyManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;

public class MainPosPageActivity extends MainPageDoubleSideActivity implements MainPosPageActivityView {
    @Inject
    @Getter
    MainPosPageActivityPresenter presenter;
    @BindView(R.id.toolbar)
    MpToolbar toolbar;
    @BindView(R.id.tvTime)
    TextClock tvTime;
    @BindView(R.id.tvDate)
    TextView tvDate;
    private Handler handler;
    @Inject
    DatabaseManager databaseManager;
    @Inject
    NotifyManager notifyManager;

    @Inject
    @Getter
    RxBusLocal rxBusLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        TestUtils.createCurrencies(databaseManager, this);
        TestUtils.createAccount(databaseManager);
        initOrderListFragmentToLeft();
        initProductPickerFragmentToRight();
        addFragmentToTopRight(new CustomerNotificationsFragment());
        notifyManager.setView(this);
        handler = new Handler();
        handler.post(timerUpdate);

        toolbar.setOnClickListener(view -> {

        });

        toolbar.setOnSettingsClickListener(view -> {
            MainMenuDialog mainMenuDialog = new MainMenuDialog(this);
            mainMenuDialog.show();
        });
        toolbar.setOnCustomerClickListener(view -> {
            Intent intent = new Intent(this, CustomersMenuActivity.class);
            startActivity(intent);
        });
        toolbar.setOnInventoryClickListener(view -> {
            Intent intent = new Intent(this, InventoryMenuActivity.class);
            startActivity(intent);
        });

        toolbar.setOnProductClickListener(view -> {
            Intent intent = new Intent(this, ProductMenuActivity.class);
            startActivity(intent);
        });
        toolbar.setOnReportClickListener(view -> {
        });
        toolbar.setOnSearchClickListener(new MpToolbar.CallbackSearchFragmentClick() {
            @Override
            public void onOpen() {
                showSearchFragment();
            }

            @Override
            public void onClose() {
                hideSearchFragment();
            }
        });

        toolbar.setOnOrderClickListener(view -> {
            OrderMenuDialog orderMenuDialog = new OrderMenuDialog(this);
            orderMenuDialog.show();
        });

    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.MAIN_PAGE_TYPE;
    }

    Runnable timerUpdate = new Runnable() {
        @Override
        public void run() {
            tvDate.setText(new SimpleDateFormat("dd - MMM, yyyy", Locale.ENGLISH).format(new Date()));
            handler.postDelayed(timerUpdate, 30000);
        }
    };

    public void closeProductInfoFragment() {
        activityFragmentManager.popBackStack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notifyManager.setView(null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void notifyView(Customer customer) {
//        Bundle bundle = new Bundle();
//        bundle.putLong("CUSTOMER_ID", customer.getId());
//        CustomerNotificationsFragment fragment = new CustomerNotificationsFragment();
//        fragment.setArguments(bundle);
//        addFragmentToTopRight(fragment);
//        CustomerNotificationsFragment fragment = (CustomerNotificationsFragment) getCurrentFragmentRightTop();
//        if (fragment != null){
//            fragment.addDataToCustomerList(customer);
//        }
    }
}
