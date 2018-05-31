package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.intosystem.FolderItem;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.mainpospage.model.OrderProductItem;

import java.util.List;

/**
 * Created by Sirojiddin on 31.10.2017.
 */

public interface ProductFolderViewPresenter extends Presenter {
    void setFolderItemsRecyclerView();
    void selectedItem(FolderItem item, int position);
    void returnBack(int mode);
    void updateProducts();
    void updateTitles();
    void updateList();
}
