package com.jim.multipos.ui.mainpospage.presenter;

import android.util.Log;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.data.operations.CategoryOperations;
import com.jim.multipos.ui.mainpospage.view.ProductSquareView;
import com.jim.multipos.ui.mainpospage.view.SearchModeView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Sirojiddin on 27.10.2017.
 */

public class SearchModePresenterImpl extends BasePresenterImpl<SearchModeView> implements SearchModePresenter {
    StringBuilder searchBuilder ;
    boolean skuMode = true;
    boolean nameMode = true;
    boolean barcodeMode = true;
    List<Product> productList;
    String searchText ="";
    @Inject
    DatabaseManager databaseManager;
    @Inject
    protected SearchModePresenterImpl(SearchModeView searchModeView) {
        super(searchModeView);
        searchBuilder = new StringBuilder();
    }

    @Override
    public void setSkuSearchMode(boolean active) {
        skuMode = active;
        onSearchTextChange(searchText);
        onModeChange();
    }

    @Override
    public void setNameSearchMode(boolean active) {
        nameMode = active;
        onSearchTextChange(searchText);
        onModeChange();

    }

    @Override
    public void setBarcodeSearchMode(boolean active) {
        barcodeMode = active;
        onSearchTextChange(searchText);
        onModeChange();

    }

    @Override
    public void onSearchTextChange(String s) {
        searchText = s;
       if(s.length() == 1){
        databaseManager.getSearchProducts(s,skuMode,barcodeMode,nameMode).subscribe((products, throwable) -> {
            productList = products;
            view.setResultsList(productList,s);
        });
       }else if(s.length()>0) {
            List<Product> productsTemp = new ArrayList<>();
            for (Product product:productList){
                if(barcodeMode && product.getBarcode().toUpperCase().contains(s.toUpperCase())){
                    productsTemp.add(product);
                }else if(nameMode && product.getName().toUpperCase().contains(s.toUpperCase())){
                    productsTemp.add(product);
                }else if(skuMode && product.getSku().toUpperCase().contains(s.toUpperCase())){
                    productsTemp.add(product);
                }
            }
           view.setResultsList(productsTemp,s);
       }
    }


    @Override
    public void onOkPressed() {

    }

    private void onModeChange(){
        if(searchText.length()>0)
        databaseManager.getSearchProducts(searchText.substring(0,1),skuMode,barcodeMode,nameMode).subscribe((products, throwable) -> {
           productList =products;
            List<Product> productsTemp = new ArrayList<>();

            for (Product product:productList){
                if(barcodeMode && product.getBarcode().toUpperCase().contains(searchText.toUpperCase())){
                    productsTemp.add(product);
                }else if(nameMode && product.getName().toUpperCase().contains(searchText.toUpperCase())){
                    productsTemp.add(product);
                }else if(skuMode && product.getSku().toUpperCase().contains(searchText.toUpperCase())){
                    productsTemp.add(product);
                }
            }
            view.setResultsList(productsTemp,searchText);

        });
    }

}
