package com.jim.multipos.ui.mainpospage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextClock;
import android.widget.TextView;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.data.db.model.Discount;
import com.jim.multipos.data.db.model.ServiceFee;
import com.jim.multipos.ui.main_menu.customers_menu.CustomersMenuActivity;
import com.jim.multipos.ui.main_menu.inventory_menu.InventoryMenuActivity;
import com.jim.multipos.ui.main_menu.product_menu.ProductMenuActivity;
import com.jim.multipos.ui.mainpospage.view.OrderListFragment;
import com.jim.multipos.ui.mainpospage.view.ProductPickerFragment;
import com.jim.multipos.ui.mainpospage.view.SearchModeFragment;
import com.jim.multipos.utils.RxBusLocal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;

public class MainPosPageActivity extends DoubleSideActivity implements MainPosPageActivityView {
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
    @Getter
    RxBusLocal rxBusLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

        addFragmentToLeft(new OrderListFragment());
//        posFragmentManager.displayFragmentWithoutBackStack(new PaymentFragment(), R.id.rightLowContainer);
        addFragmentToRight(new ProductPickerFragment());

        handler = new Handler();
        handler.post(timerUpdate);

        toolbar.setOnClickListener(view -> {
        });
        toolbar.setOnSettingsClickListener(view -> {
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
        toolbar.setOnSearchClickListener(view -> {
            addFragmentToRight(new SearchModeFragment());

        });
//        ArrayList<Contact>  contactsTemp = new ArrayList<>();
//        for(int i=0;i<1000000;i++) {
//            contactsTemp.add( new Contact(String.valueOf(System.currentTimeMillis() + Math.random()), "phone2", "islomov49@gmail.com"));
//        }
//        databaseManager.addContact(contactsTemp).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
//
//        });
//
//        databaseManager.getAllContacts().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(contacts -> {
//            Log.d("testDatabaseManager", "onCreate: "+contacts.size());
//            Toast.makeText(MainPosPageActivity.this,contacts.size()+"",Toast.LENGTH_SHORT).show();
//        });
    }

    @Override
    protected int getToolbarMode() {
        return MpToolbar.MAIN_PAGE_TYPE;
    }

    @Override
    public List<Discount> getDiscounts() {
        return presenter.getDiscounts();
    }

    @Override
    public void addDiscount(double amount, String description, String amountType) {
        presenter.addDiscount(amount, description, amountType);
    }

    @Override
    public List<ServiceFee> getServiceFees() {
        return presenter.getServiceFees();
    }

    @Override
    public void addServiceFee(double amount, String description, String amountType) {
        presenter.addServiceFee(amount, description, amountType);
    }

    Runnable timerUpdate = new Runnable() {
        @Override
        public void run() {
            tvDate.setText(new SimpleDateFormat("dd - MMM, yyyy", Locale.ENGLISH).format(new Date()));
            handler.postDelayed(timerUpdate, 30000);
        }
    };
}
