package com.jim.multipos.ui.mainpospage.view;

import com.jim.multipos.core.BaseView;
import com.jim.multipos.data.db.model.intosystem.FolderItem;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;

import java.util.List;

/**
 * Created by Sirojiddin on 31.10.2017.
 */

public interface ProductFolderView extends BaseView {
    void setBackItemVisibility(boolean state);
    void setFolderItemRecyclerView(List<FolderItem> folderItems);
    void refreshProductList(List<FolderItem> folderItems, int mode);
    void setSelectedProduct(Product product);
    void sendCategoryEvent(Category category, String key);
}
