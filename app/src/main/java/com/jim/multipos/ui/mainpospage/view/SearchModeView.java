package com.jim.multipos.ui.mainpospage.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.products.Product;

import java.util.List;

/**
 * Created by developer on 02.11.2017.
 */

public interface SearchModeView  extends BaseView{
    void setTextFrame(String textFrame);
    void setResultsList(List<Product> resultsList,String textSearch);
    void addProductToOrderInCloseSelf();
}
