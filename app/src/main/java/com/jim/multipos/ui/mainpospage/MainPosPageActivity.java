package com.jim.multipos.ui.mainpospage;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import com.jim.mpviews.MpToolbar;
import com.jim.multipos.R;
import com.jim.multipos.core.DoubleSideActivity;
import com.jim.multipos.ui.main_menu.customers_menu.CustomersMenuActivity;
import com.jim.multipos.ui.main_menu.inventory_menu.InventoryMenuActivity;
import com.jim.multipos.ui.main_menu.product_menu.ProductMenuActivity;
import com.jim.multipos.ui.mainpospage.view.OrderListFragment;
import com.jim.multipos.ui.mainpospage.view.ProductPickerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainPosPageActivity extends DoubleSideActivity implements MainPosPageActivityView {
    @BindView(R.id.toolbar)
    MpToolbar toolbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

        addFragmentToLeft(new OrderListFragment());
//        posFragmentManager.displayFragmentWithoutBackStack(new PaymentFragment(), R.id.rightLowContainer);
        addFragmentToRight(new ProductPickerFragment());

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

    @Override
    protected int getToolbarMode() {
        return MpToolbar.MAIN_PAGE_TYPE;
    }
}
