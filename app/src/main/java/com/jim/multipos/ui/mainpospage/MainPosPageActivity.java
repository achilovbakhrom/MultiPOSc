package com.jim.multipos.ui.mainpospage;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.di.BaseAppComponent;
import com.jim.multipos.ui.HasComponent;
import com.jim.multipos.ui.main_menu.customers_menu.CustomersMenuActivity;
import com.jim.multipos.ui.main_menu.employer_menu.EmployerMenuActivity;
import com.jim.multipos.ui.main_menu.inventory_menu.InventoryMenuActivity;
import com.jim.multipos.ui.mainpospage.di.MainPosPageActivityModule;
import com.jim.multipos.ui.mainpospage.di.MainPosPageActivityComponent;
import com.jim.multipos.ui.mainpospage.fragments.OrderListFragment;
import com.jim.multipos.ui.mainpospage.fragments.PaymentFragment;
import com.jim.multipos.ui.mainpospage.fragments.ProductInfoFragment;
import com.jim.multipos.ui.mainpospage.fragments.RectangleProductChoiserFragment;
import com.jim.multipos.ui.main_menu.product_menu.ProductMenuActivity;
import com.jim.multipos.utils.managers.PosFragmentManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainPosPageActivity extends BaseActivity implements MainPosPageActivityView, HasComponent<MainPosPageActivityComponent> {
    @BindView(R.id.toolbar)
    MpToolbar toolbar;

    @Inject
    DatabaseManager databaseManager;
    @Inject
    PosFragmentManager posFragmentManager;

    MainPosPageActivityComponent mainPosPageFragmentComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_pos_page);
        ButterKnife.bind(this);

        posFragmentManager.displayFragmentWithoutBackStack(new OrderListFragment(), R.id.leftSingleContainer);
//        posFragmentManager.displayFragmentWithoutBackStack(new PaymentFragment(), R.id.rightLowContainer);
        posFragmentManager.displayFragmentWithoutBackStack(new RectangleProductChoiserFragment(), R.id.rightLowContainer);

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

    protected void setupComponent(BaseAppComponent baseAppComponent) {
//        mainPosPageFragmentComponent = baseAppComponent.plus(new MainPosPageActivityModule(this));
        mainPosPageFragmentComponent.inject(this);
    }

    @Override
    public MainPosPageActivityComponent getComponent() {
        return mainPosPageFragmentComponent;
    }
}
