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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jim.mpviews.MpSearchView;
import com.jim.multipos.R;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.consignment.adapter.ProductsForIncomeAdapter;
import com.jim.multipos.ui.consignment.model.ProductForIncomeItem;
import com.jim.multipos.utils.BarcodeStack;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jim.multipos.ui.consignment.dialogs.ProductsForIncomeDialog.ProductSortingStates.SORTED_BY_ARTICLE;
import static com.jim.multipos.ui.consignment.dialogs.ProductsForIncomeDialog.ProductSortingStates.SORTED_BY_ARTICLE_INVERT;
import static com.jim.multipos.ui.consignment.dialogs.ProductsForIncomeDialog.ProductSortingStates.SORTED_BY_BARCODE;
import static com.jim.multipos.ui.consignment.dialogs.ProductsForIncomeDialog.ProductSortingStates.SORTED_BY_BARCODE_INVERT;
import static com.jim.multipos.ui.consignment.dialogs.ProductsForIncomeDialog.ProductSortingStates.SORTED_BY_NAME;
import static com.jim.multipos.ui.consignment.dialogs.ProductsForIncomeDialog.ProductSortingStates.SORTED_BY_NAME_INVERT;
import static com.jim.multipos.ui.consignment.dialogs.ProductsForIncomeDialog.ProductSortingStates.SORTED_BY_TYPE;
import static com.jim.multipos.ui.consignment.dialogs.ProductsForIncomeDialog.ProductSortingStates.SORTED_BY_TYPE_INVERT;

public class ProductsForIncomeDialog extends Dialog {

    @BindView(R.id.rvProductList)
    RecyclerView rvProductList;
    @BindView(R.id.svProductSearch)
    MpSearchView svProductSearch;
    @BindView(R.id.llProductName)
    LinearLayout llProductName;
    @BindView(R.id.llArticle)
    LinearLayout llArticle;
    @BindView(R.id.llBarcode)
    LinearLayout llBarcode;
    @BindView(R.id.llSupplyStatus)
    LinearLayout llSupplyStatus;
    @BindView(R.id.ivArticleSort)
    ImageView ivArticleSort;
    @BindView(R.id.ivProductNameSort)
    ImageView ivProductNameSort;
    @BindView(R.id.ivBarcodeSort)
    ImageView ivBarcodeSort;
    @BindView(R.id.ivSupplySort)
    ImageView ivSupplySort;
    private ProductSortingStates filterMode = SORTED_BY_TYPE_INVERT;

    public void setBarcode(String barcode) {
        svProductSearch.getSearchView().setText(barcode);
    }

    public enum ProductSortingStates {
        SORTED_BY_NAME, SORTED_BY_NAME_INVERT, SORTED_BY_ARTICLE, SORTED_BY_ARTICLE_INVERT, SORTED_BY_BARCODE, SORTED_BY_BARCODE_INVERT, SORTED_BY_TYPE, SORTED_BY_TYPE_INVERT
    }

    private Context context;
    private OnSelectClickListener listener;
    private List<Product> productList;
    private List<Product> vendorProducts;
    private BarcodeStack barcodeStack;
    private List<ProductForIncomeItem> searchResults;
    private List<ProductForIncomeItem> items;
    private ProductsForIncomeAdapter adapter;

    public ProductsForIncomeDialog(@NonNull Context context, List<Product> productList, List<Product> vendorProducts, BarcodeStack barcodeStack) {
        super(context);
        this.context = context;
        this.productList = productList;
        this.vendorProducts = vendorProducts;
        this.barcodeStack = barcodeStack;
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
        adapter = new ProductsForIncomeAdapter(context);
        rvProductList.setLayoutManager(new LinearLayoutManager(getContext()));
        ((SimpleItemAnimator) rvProductList.getItemAnimator()).setSupportsChangeAnimations(false);
        rvProductList.setAdapter(adapter);
        items = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            ProductForIncomeItem item = new ProductForIncomeItem();
            item.setProduct(productList.get(i));
            item.setWasSupplied(vendorProducts.contains(productList.get(i)));
            items.add(item);
        }
        sortList();
        adapter.setData(items);
        adapter.setListener(product -> {
            listener.onSelect(product);
            dismiss();
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

        llProductName.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_NAME) {
                filterMode = SORTED_BY_NAME;
                sortList();
                ivProductNameSort.setVisibility(View.VISIBLE);
                ivProductNameSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_NAME_INVERT;
                ivProductNameSort.setVisibility(View.VISIBLE);
                ivProductNameSort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });

        llArticle.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_ARTICLE) {
                filterMode = SORTED_BY_ARTICLE;
                sortList();
                ivArticleSort.setVisibility(View.VISIBLE);
                ivArticleSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_ARTICLE_INVERT;
                ivArticleSort.setVisibility(View.VISIBLE);
                ivArticleSort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });

        llBarcode.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_BARCODE) {
                filterMode = SORTED_BY_BARCODE;
                sortList();
                ivBarcodeSort.setVisibility(View.VISIBLE);
                ivBarcodeSort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_BARCODE_INVERT;
                ivBarcodeSort.setVisibility(View.VISIBLE);
                ivBarcodeSort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });

        llSupplyStatus.setOnClickListener(view -> {
            deselectAll();
            if (filterMode != SORTED_BY_TYPE) {
                filterMode = SORTED_BY_TYPE;
                sortList();
                ivSupplySort.setVisibility(View.VISIBLE);
                ivSupplySort.setImageResource(R.drawable.sorting);
            } else {
                filterMode = SORTED_BY_TYPE_INVERT;
                ivSupplySort.setVisibility(View.VISIBLE);
                ivSupplySort.setImageResource(R.drawable.sorting_invert);
                sortList();
            }
        });

        svProductSearch.getBarcodeView().setOnClickListener(view -> listener.onBarcodeClick());
        barcodeStack.register(barcode -> svProductSearch.getSearchView().setText(barcode));
        setOnDismissListener(o -> barcodeStack.unregister());
    }

    private void deselectAll() {
        ivSupplySort.setVisibility(View.GONE);
        ivBarcodeSort.setVisibility(View.GONE);
        ivArticleSort.setVisibility(View.GONE);
        ivProductNameSort.setVisibility(View.GONE);
    }

    private void sortList() {
        List<ProductForIncomeItem> tempList;
        if (searchResults != null)
            tempList = searchResults;
        else tempList = items;
        switch (filterMode) {
            case SORTED_BY_ARTICLE:
                Collections.sort(tempList, (item, t1) -> item.getProduct().getSku().toUpperCase().compareTo(t1.getProduct().getSku().toUpperCase()));
                break;
            case SORTED_BY_BARCODE:
                Collections.sort(tempList, (item, t1) -> item.getProduct().getBarcode().compareTo(t1.getProduct().getBarcode()));
                break;
            case SORTED_BY_NAME:
                Collections.sort(tempList, (item, t1) -> item.getProduct().getName().toUpperCase().compareTo(t1.getProduct().getName().toUpperCase()));
                break;
            case SORTED_BY_TYPE:
                Collections.sort(tempList, (item, t1) -> item.isWasSupplied().compareTo(t1.isWasSupplied()));
                break;
            case SORTED_BY_ARTICLE_INVERT:
                Collections.sort(tempList, (item, t1) -> item.getProduct().getSku().toUpperCase().compareTo(t1.getProduct().getSku().toUpperCase()) * -1);
                break;
            case SORTED_BY_BARCODE_INVERT:
                Collections.sort(tempList, (item, t1) -> item.getProduct().getBarcode().compareTo(t1.getProduct().getBarcode()) * -1);
                break;
            case SORTED_BY_NAME_INVERT:
                Collections.sort(tempList, (item, t1) -> item.getProduct().getName().toUpperCase().compareTo(t1.getProduct().getName().toUpperCase()) * -1);
                break;
            case SORTED_BY_TYPE_INVERT:
                Collections.sort(tempList, (item, t1) -> item.isWasSupplied().compareTo(t1.isWasSupplied()) * -1);
                break;
        }
        adapter.notifyDataSetChanged();
    }

    public interface OnSelectClickListener {
        void onSelect(Product product);

        void onBarcodeClick();
    }
}
