package com.jim.multipos.ui.mainpospage.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.mpviews.MpKeyBoard;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.mainpospage.MainPosPageActivity;
import com.jim.multipos.ui.mainpospage.adapter.SearchResultsAdapter;
import com.jim.multipos.ui.mainpospage.connection.MainPageConnection;
import com.jim.multipos.ui.mainpospage.presenter.SearchModePresenter;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by developer on 02.11.2017.
 */

public class SearchModeFragment  extends BaseFragment implements SearchModeView {

    @BindView(R.id.tvSearchSkuMode)
    TextView tvSearchSkuMode;
    @BindView(R.id.tvSearchNameMode)
    TextView tvSearchNameMode;
    @BindView(R.id.tvSearchBarcodeMode)
    TextView tvSearchBarcodeMode;
    @BindView(R.id.tvSearchTextPlace)
    EditText tvSearchTextPlace;
    @BindView(R.id.ivClear)
    ImageView ivClear;
    @BindView(R.id.rvSearchResults)
    RecyclerView rvSearchResults;
    @BindView(R.id.tvSearchResultsCount)
    TextView tvSearchResultsCount;
    @Inject
    SearchModePresenter presenter;

    @BindView(R.id.mpKeyBoard)
    MpKeyBoard mpKeyBoard;
    SearchResultsAdapter searchResultsAdapter;
    List<Product> productList;
    @Inject
    MainPageConnection mainPageConnection;
    boolean barcodeMode = true;
    boolean nameMode = true;
    boolean skuMode = true;

    @Override
    protected int getLayout() {
        return R.layout.search_mode_fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mpKeyBoard.setOnTextTyped(new MpKeyBoard.OnTextTyped() {
            @Override
            public void onSymbolPressed(String s) {
                tvSearchTextPlace.getText().insert(tvSearchTextPlace.getSelectionStart(),s);
            }

            @Override
            public void onOkPressed() {
               presenter.onOkPressed();
            }

            @Override
            public void onBackClear() {
                StringBuilder builder = new StringBuilder();
                builder.append(tvSearchTextPlace.getText().toString());
                int selectionStart = tvSearchTextPlace.getSelectionStart();
                if(selectionStart==0) return;
                builder.deleteCharAt(selectionStart -1);
                tvSearchTextPlace.setText(builder.toString());
                tvSearchTextPlace.setSelection(selectionStart-1);
            }

            @Override
            public void onClear() {
                tvSearchTextPlace.setText("");
            }
        });
        tvSearchTextPlace.addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = tvSearchTextPlace.getText().toString();
                presenter.onSearchTextChange(s);
                if(s.isEmpty()) ivClear.setColorFilter(null);
                else ivClear.setColorFilter(Color.parseColor("#41a0da"));
            }
        });
        ivClear.setOnClickListener(view -> {
            tvSearchTextPlace.setText("");
        });
        tvSearchTextPlace.setText("");
        tvSearchBarcodeMode.setOnClickListener(view -> {
            if(barcodeMode) {
                barcodeMode = false;
                presenter.setBarcodeSearchMode(barcodeMode);
                tvSearchBarcodeMode.setTextColor(Color.parseColor("#cdcdcd"));
            }else {
                barcodeMode = true;
                presenter.setBarcodeSearchMode(barcodeMode);
                tvSearchBarcodeMode.setTextColor(Color.parseColor("#419fd9"));

            }
        });
        productList = new ArrayList<>();
        searchResultsAdapter = new SearchResultsAdapter(getContext(), productList, position -> {
            mainPageConnection.addProductToOrder(productList.get(position).getId());
        });
        rvSearchResults.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        rvSearchResults.setAdapter(searchResultsAdapter);
        tvSearchTextPlace.setRawInputType(InputType.TYPE_CLASS_TEXT);
        tvSearchTextPlace.setTextIsSelectable(true);
        tvSearchTextPlace.requestFocus();
        tvSearchNameMode.setOnClickListener(view -> {
            if(nameMode) {
                nameMode = false;
                presenter.setNameSearchMode(nameMode);
                tvSearchNameMode.setTextColor(Color.parseColor("#cdcdcd"));
            }else {
                nameMode = true;
                presenter.setNameSearchMode(nameMode);
                tvSearchNameMode.setTextColor(Color.parseColor("#419fd9"));

            }
        });
        tvSearchSkuMode.setOnClickListener(view -> {
            if(skuMode) {
                skuMode = false;
                presenter.setSkuSearchMode(skuMode);
                tvSearchSkuMode.setTextColor(Color.parseColor("#cdcdcd"));

            }else {
                skuMode = true;
                presenter.setSkuSearchMode(skuMode);
                tvSearchSkuMode.setTextColor(Color.parseColor("#419fd9"));
            }
        });

    }

    @Override
    public void setTextFrame(String textFrame) {
        tvSearchTextPlace.setText(textFrame);
    }

    @Override
    public void setResultsList(List<Product> resultsList,String searchText) {
        tvSearchResultsCount.setText(getContext().getString(R.string.search_results)+" - "+resultsList.size());
        searchResultsAdapter.setItems(resultsList,searchText);
        productList = resultsList;
        searchResultsAdapter.notifyDataSetChanged();

    }

    @Override
    public void addProductToOrderInCloseSelf() {
        if(productList != null && productList.size()>0 && productList.get(0)!= null && productList.get(0).getId() !=null)
            mainPageConnection.addProductToOrder(productList.get(0).getId());
        ((MainPosPageActivity)getActivity()).hideSearFragmentWithDisableSearchButton();
    }
}
