package com.jim.multipos.ui.products.presenters;

import android.content.Context;
import android.net.Uri;

import com.jim.multipos.core.BaseFragmentPresenter;
import com.jim.multipos.ui.products.fragments.CategoryView;

/**
 * Created by DEV on 10.08.2017.
 */

public interface CategoryPresenter extends BaseFragmentPresenter<CategoryView> {
    void backPressed();
    void saveCategory(String name, String description, boolean checked, String photoPath);
    void loadImage();
    void checkData();
    void onDestroy();
}
