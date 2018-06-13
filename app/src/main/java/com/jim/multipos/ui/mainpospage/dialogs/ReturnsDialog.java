package com.jim.multipos.ui.mainpospage.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.view.Window;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpSearchView;
import com.jim.multipos.R;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.db.model.products.Return;
import com.jim.multipos.ui.mainpospage.adapter.ProductSearchResultsAdapter;
import com.jim.multipos.ui.mainpospage.adapter.ReturnsAdapter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.TextWatcherOnTextChange;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.WarningDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sirojiddin on 06.01.2018.
 */

public class ReturnsDialog extends Dialog {

    @BindView(R.id.rvProducts)
    RecyclerView rvProducts;
    @BindView(R.id.svProductSearch)
    MpSearchView svProductSearch;
    @BindView(R.id.rvReturnProducts)
    RecyclerView rvReturnProducts;
    @BindView(R.id.btnNext)
    MpButton btnNext;
    @BindView(R.id.btnClose)
    MpButton btnClose;
    private List<Product> productList, searchResults;
    private List<Return> returnsList;
    private ProductSearchResultsAdapter searchResultsAdapter;
    private ReturnsAdapter returnsAdapter;

    public ReturnsDialog(@NonNull Context context, DatabaseManager databaseManager, DecimalFormat decimalFormat, List<Return> returns, RxBus rxBus) {
        super(context);
        View dialogView = getLayoutInflater().inflate(R.layout.return_dialog, null);
        ButterKnife.bind(this, dialogView);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        View v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

        productList = databaseManager.getAllProducts().blockingSingle();
        this.returnsList = new ArrayList<>();

        returnsAdapter = new ReturnsAdapter(returnsList, decimalFormat, context, (item, position) -> {
            WarningDialog warningDialog = new WarningDialog(context);
            warningDialog.setWarningMessage(context.getString(R.string.do_you_want_delete));
            warningDialog.setOnYesClickListener(view1 -> {
                returnsList.remove(item);
                returnsAdapter.notifyItemRemoved(position);
                warningDialog.dismiss();
            });
            warningDialog.setOnNoClickListener(view -> warningDialog.dismiss());
            warningDialog.show();
        });

        if (returns != null) {
            this.returnsList = returns;
            returnsAdapter.setData(this.returnsList);
        }

        searchResultsAdapter = new ProductSearchResultsAdapter(context);

        rvProducts.setLayoutManager(new LinearLayoutManager(context));
        rvProducts.setAdapter(searchResultsAdapter);
        ((SimpleItemAnimator) rvProducts.getItemAnimator()).setSupportsChangeAnimations(false);
        rvReturnProducts.setLayoutManager(new LinearLayoutManager(context));
        rvReturnProducts.setAdapter(returnsAdapter);
        ((SimpleItemAnimator) rvReturnProducts.getItemAnimator()).setSupportsChangeAnimations(false);
        searchResultsAdapter.setData(productList);

        svProductSearch.getSearchView().addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = svProductSearch.getSearchView().getText().toString();
                if (searchText.isEmpty()) {
                    searchResults = null;
                    searchResultsAdapter.setData(productList);
                } else {
                    searchResults = new ArrayList<>();
                    for (int j = 0; j < productList.size(); j++) {
                        if (productList.get(j).getName().toUpperCase().contains(searchText.toUpperCase())) {
                            searchResults.add(productList.get(j));
                            continue;
                        }
                        if (productList.get(j).getBarcode().toUpperCase().contains(searchText.toUpperCase())) {
                            searchResults.add(productList.get(j));
                            continue;
                        }
                        if (productList.get(j).getSku().toUpperCase().contains(searchText.toUpperCase())) {
                            searchResults.add(productList.get(j));
                        }
                    }
                    searchResultsAdapter.setData(searchResults);
                }
            }
        });
        searchResultsAdapter.setListener(product -> {
            Return returnProduct = new Return();
            returnProduct.setProduct(product);
            returnProduct.setCreateAt(System.currentTimeMillis());
            returnProduct.setQuantity(1);
            //TODO RETURN DIALOG
//            returnProduct.setVendor(product.getVendor().get(0));
            returnProduct.setReturnAmount(product.getPrice());
            returnsAdapter.addItem(returnProduct);
        });

        btnNext.setOnClickListener(view -> {
            UIUtils.closeKeyboard(btnNext, context);
            if (!this.returnsList.isEmpty()) {
                ReturnsConfirmDialog confirmDialog = new ReturnsConfirmDialog(getContext(), this.returnsList, databaseManager, decimalFormat, rxBus);
                confirmDialog.show();
                dismiss();
            } else {
                WarningDialog warningDialog = new WarningDialog(context);
                warningDialog.setWarningMessage(context.getString(R.string.products_were_not_chose_for_return));
                warningDialog.onlyText(true);
                warningDialog.setOnYesClickListener(view1 -> warningDialog.dismiss());
                warningDialog.show();
            }
        });

        btnClose.setOnClickListener(view -> {
            UIUtils.closeKeyboard(btnClose, context);
            dismiss();
        });

    }
}
