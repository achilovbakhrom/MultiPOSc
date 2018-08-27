package com.jim.multipos.ui.admin_main_page.fragments.product;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.ui.admin_main_page.AdminMainPageActivity;
import com.jim.multipos.ui.admin_main_page.fragments.product.adapter.ProductAdapter;
import com.jim.multipos.ui.admin_main_page.fragments.product.model.Product;
import com.jim.multipos.utils.OnItemClickListener;
import com.jim.multipos.utils.RxBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class ProductsFragment extends BaseFragment implements OnItemClickListener{

    @BindView(R.id.rvProductList)
    RecyclerView rvProductList;

    @Inject
    RxBus bus;

    @OnClick(R.id.fab)
    public void onClick(View view){
        if(getActivity() instanceof AdminMainPageActivity && getActivity()!=null)
            ((AdminMainPageActivity) getActivity()).openEditor();
    }

    List<Product> items;

    @Override
    protected int getLayout() {
        return R.layout.admin_product_list_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setUpItems();
        rvProductList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvProductList.setAdapter(new ProductAdapter(items, this));
    }

    void setUpItems(){
        items = new ArrayList<>();
        items.add(new Product("Cola", "Barcode 98956211", "SKU 1532354", "7000 UZS", "1 USD", ""));
        items.add(new Product("Pepsi", "Barcode 98956211", "SKU 1532354", "7000 UZS", "1 USD", ""));
        items.add(new Product("Fanta", "Barcode 98956211", "SKU 1532354", "7000 UZS", "1 USD", ""));
        items.add(new Product("Sprite", "Barcode 98956211", "SKU 1532354", "7000 UZS", "1 USD", ""));
        items.add(new Product("RCCola", "Barcode 98956211", "SKU 1532354", "7000 UZS", "1 USD", ""));
        items.add(new Product("7UP", "Barcode 98956211", "SKU 1532354", "7000 UZS", "1 USD", ""));
        items.add(new Product("Cola", "Barcode 98956211", "SKU 1532354", "7000 UZS", "1 USD", ""));
    }

    public void onCompanyEdit(boolean isVisible) {
        if(isVisible)
            rvProductList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        else rvProductList.setLayoutManager(new GridLayoutManager(getContext(), 3));

    }

    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public void onItemClicked(Object item) {
        if(item instanceof Product){
            bus.send(item);
        }
        if(getActivity() instanceof AdminMainPageActivity && getActivity()!=null)
            ((AdminMainPageActivity) getActivity()).openEditor();
    }
}
