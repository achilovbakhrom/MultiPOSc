package com.jim.multipos.ui.products.presenters;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.ui.products.fragments.SubCategoryView;

/**
 * Created by DEV on 18.08.2017.
 */

public interface SubCategoryPresenter extends BaseFragmentPresenter<SubCategoryView>{
    void save(String name, String description, boolean checkboxChecked, String photoPath);
    void back();
    void checkData();
    void onDestroy();

}
