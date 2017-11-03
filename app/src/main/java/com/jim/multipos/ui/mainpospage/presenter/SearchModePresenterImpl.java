package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.BasePresenterImpl;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.products.Category;
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
    @Inject
    protected SearchModePresenterImpl(SearchModeView searchModeView) {
        super(searchModeView);
    }
}
