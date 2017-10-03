package com.jim.multipos.ui.product_class.fragments;

import com.jim.multipos.data.db.model.ProductClass;
import com.jim.multipos.data.db.model.intosystem.NamePhotoPathId;

import java.util.List;

/**
 * Created by developer on 29.08.2017.
 */

public interface ProductClassListView {
        void reshreshView();
        void setItemsRecyclerView(List<ProductClass> rvItemsList);
}
