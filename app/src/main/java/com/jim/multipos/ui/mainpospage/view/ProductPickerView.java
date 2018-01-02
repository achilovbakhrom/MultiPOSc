package com.jim.multipos.ui.mainpospage.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.products.Category;

/**
 * Created by Sirojiddin on 27.10.2017.
 */

public interface ProductPickerView extends BaseView {
    void updateCategoryTitles(Category category, String type);
}
