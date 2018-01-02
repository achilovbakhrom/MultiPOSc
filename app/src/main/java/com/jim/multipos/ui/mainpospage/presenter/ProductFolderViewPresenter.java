package com.jim.multipos.ui.mainpospage.presenter;

import com.jim.multipos.core.Presenter;
import com.jim.multipos.data.db.model.intosystem.FolderItem;

/**
 * Created by Sirojiddin on 31.10.2017.
 */

public interface ProductFolderViewPresenter extends Presenter {
    void setFolderItemsRecyclerView();
    void selectedItem(FolderItem item);
    void returnBack(int mode);
    void updateProducts();
}
