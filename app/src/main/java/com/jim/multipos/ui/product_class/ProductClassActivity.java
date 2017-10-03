package com.jim.multipos.ui.product_class;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseActivity;
import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.di.BaseAppComponent;
import com.jim.multipos.ui.HasComponent;
import com.jim.multipos.ui.product_class.di.ProductClassComponent;
import com.jim.multipos.ui.product_class.di.ProductClassModule;
import com.jim.multipos.ui.product_class.fragments.AddProductClassFragment;
import com.jim.multipos.ui.product_class.fragments.ProductClassListFragment;
import com.jim.multipos.ui.products.di.ProductsComponent;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.managers.PosFragmentManager;
import com.jim.multipos.utils.rxevents.Unsibscribe;

import javax.inject.Inject;

public class ProductClassActivity extends BaseActivity implements HasComponent<ProductClassComponent> , ProductClassView {
    ProductClassComponent productClassComponent;
    @Inject
    PosFragmentManager posFragmentManager;
    @Inject
    RxBus rxBus;
    @Inject
    RxBusLocal rxBusLocal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_layout);
        posFragmentManager.displayFragmentWithoutBackStack(new AddProductClassFragment(),R.id.flLeftContainer);
        posFragmentManager.displayFragmentWithoutBackStack(new ProductClassListFragment(),R.id.flRightContainer);

    }

    @Override
    protected void setupComponent(BaseAppComponent baseAppComponent) {
        productClassComponent = baseAppComponent.plus(new ProductClassModule(this));
        productClassComponent.inject(this);
    }

    @Override
    protected void onDestroy() {
        rxBus.send(new Unsibscribe(ProductClassActivity.class.getName()));
        super.onDestroy();
    }

    @Override
    public ProductClassComponent getComponent() {
        return productClassComponent;
    }
}
