package com.jim.multipos.ui.consignment.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.jim.mpviews.MpSearchView;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.consignment.adapter.ProductsForIncomeAdapter;
import com.jim.multipos.ui.consignment.model.ProductForIncomeItem;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsForIncomeDialog extends Dialog {

    @BindView(R.id.rvProductList)
    RecyclerView rvProductList;
    @BindView(R.id.svProductSearch)
    MpSearchView svProductSearch;
    private Context context;

    private OnSelectClickListener listener;

    private List<Product> productList;
    private List<Product> vendorProducts;
    private List<ProductForIncomeItem> searchResults;

    public ProductsForIncomeDialog(@NonNull Context context, List<Product> productList, List<Product> vendorProducts) {
        super(context);
        this.context = context;
        this.productList = productList;
        this.vendorProducts = vendorProducts;
    }

    public void setListener(OnSelectClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.product_for_income_dialog, null, false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);
        ProductsForIncomeAdapter adapter = new ProductsForIncomeAdapter(context);
        rvProductList.setLayoutManager(new LinearLayoutManager(getContext()));
        ((SimpleItemAnimator) rvProductList.getItemAnimator()).setSupportsChangeAnimations(false);
        rvProductList.setAdapter(adapter);
        List<ProductForIncomeItem> items = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            ProductForIncomeItem item = new ProductForIncomeItem();
            item.setProduct(productList.get(i));
            item.setWasSupplied(vendorProducts.contains(productList.get(i)));
            items.add(item);
        }
        Collections.sort(items, (o1, o2) -> o1.isWasSupplied().compareTo(o2.isWasSupplied()));
        adapter.setData(items);
        adapter.setListener(new ProductsForIncomeAdapter.OnProductIncomeClicked() {
            @Override
            public void onInfo(Product product) {

            }

            @Override
            public void onSelect(Product product) {
                listener.onSelect(product);
                dismiss();
            }
        });

        svProductSearch.getSearchView().addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int position, int i1, int i2) {
                String searchText = svProductSearch.getSearchView().getText().toString();
                if (searchText.isEmpty()) {
                    searchResults = null;
                    sortList();
                    adapter.setData(items);
                } else {
                    searchResults = new ArrayList<>();
                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getProduct().getName().toUpperCase().contains(searchText.toUpperCase())) {
                            searchResults.add(items.get(i));
                            continue;
                        }
                        if (items.get(i).getProduct().getBarcode().toUpperCase().contains(searchText.toUpperCase())) {
                            searchResults.add(items.get(i));
                            continue;
                        }
                        if (items.get(i).getProduct().getSku().toUpperCase().contains(searchText.toUpperCase())) {
                            searchResults.add(items.get(i));
                            continue;
                        }
                    }
                    sortList();
                    adapter.setSearchResult(searchResults, searchText);
                }
            }
        });
    }

    private void sortList() {

    }

    public interface OnSelectClickListener {
        void onSelect(Product product);
    }
}
