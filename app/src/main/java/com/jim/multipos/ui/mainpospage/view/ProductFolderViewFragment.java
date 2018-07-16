package com.jim.multipos.ui.mainpospage.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.db.model.intosystem.FolderItem;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.mainpospage.adapter.FolderViewAdapter;
import com.jim.multipos.ui.mainpospage.connection.MainPageConnection;
import com.jim.multipos.ui.mainpospage.presenter.ProductFolderViewPresenter;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.WarningDialog;
import com.jim.multipos.utils.rxevents.inventory_events.InventoryStateEvent;
import com.jim.multipos.utils.rxevents.main_order_events.GlobalEventConstants;
import com.jim.multipos.utils.rxevents.main_order_events.ProductEvent;
import com.jim.multipos.utils.rxevents.product_events.CategoryEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

/**
 * Created by Sirojiddin on 27.10.2017.
 */

public class ProductFolderViewFragment extends BaseFragment implements ProductFolderView {

    @Inject
    ProductFolderViewPresenter presenter;
    @BindView(R.id.rvFolderItems)
    RecyclerView rvFolderItems;
    @BindView(R.id.llBackItem)
    LinearLayout llBackItem;
    @BindView(R.id.tvProductTitle)
    TextView tvProductTitle;
    @BindView(R.id.tvAction)
    TextView tvAction;
    @BindView(R.id.flLine)
    FrameLayout flLine;
    @Inject
    RxBus rxBus;
    @Inject
    RxBusLocal rxBusLocal;
    @Inject
    MainPageConnection mainPageConnection;
    private int mode = 0;
    private static final int PRODUCT = 2;
    private FolderViewAdapter adapter;
    private ArrayList<Disposable> subscriptions;
    DecimalFormat decimalFormat;
    @Override
    protected int getLayout() {
        return R.layout.product_folder_list;
    }


    @Override
    protected void rxConnections() {
        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBus.toObservable().subscribe(o -> {
                    if (o instanceof CategoryEvent) {
                        CategoryEvent event = (CategoryEvent) o;
                        switch (event.getType()) {
                            case GlobalEventConstants.ADD:
                            case GlobalEventConstants.DELETE:
                            case GlobalEventConstants.UPDATE: {
                                presenter.updateList();
                                break;
                            }
                        }
                    }
                    if (o instanceof InventoryStateEvent) {
                        InventoryStateEvent event = (InventoryStateEvent) o;
                        if (event.getType() == GlobalEventConstants.UPDATE)
                            presenter.updateProducts();
                    }
                    if (o instanceof ProductEvent) {
                        ProductEvent event = (ProductEvent) o;
                        switch (event.getType()) {
                            case GlobalEventConstants.ADD:
                            case GlobalEventConstants.DELETE:
                            case GlobalEventConstants.UPDATE: {
                                presenter.updateList();
                                break;
                            }
                        }
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainPageConnection.setProductFolderView(null);
        RxBus.removeListners(subscriptions);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mainPageConnection.setProductFolderView(this);
        decimalFormat = BaseAppModule.getFormatterWithoutGroupingTwoDecimal();
        ((SimpleItemAnimator) rvFolderItems.getItemAnimator()).setSupportsChangeAnimations(false);
        presenter.setFolderItemsRecyclerView();
        if (mode == PRODUCT) {
            tvAction.setVisibility(View.VISIBLE);
            tvProductTitle.setText(getResources().getString(R.string.qty_and_price));
        } else {
            tvProductTitle.setText(getResources().getString(R.string.count_of_products));
            tvAction.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.llBackItem)
    void onBackItemClick() {
        presenter.returnBack(mode);
    }

    @Override
    public void setBackItemVisibility(boolean state) {
        if (state) {
            flLine.setVisibility(View.VISIBLE);
            llBackItem.setVisibility(View.VISIBLE);
        } else {
            llBackItem.setVisibility(View.GONE);
            flLine.setVisibility(View.GONE);
        }
    }

    @Override
    public void setFolderItemRecyclerView(List<FolderItem> folderItems) {
        rvFolderItems.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FolderViewAdapter(folderItems, mode, getContext());
        rvFolderItems.setAdapter(adapter);
        adapter.setListener(new FolderViewAdapter.OnFolderItemClickListener() {
            @Override
            public void onItemClick(FolderItem folderItem, int position) {
                presenter.selectedItem(folderItem,position);
            }

            @Override
            public void onInfoClicked(FolderItem folderItem, int position) {
                presenter.selectedInfo(folderItem, position);
            }
        });
    }

    @Override
    public void refreshProductList(List<FolderItem> folderItems, int mode) {
        this.mode = mode;
        if (mode == PRODUCT) {
            tvAction.setVisibility(View.VISIBLE);
            tvProductTitle.setText(getResources().getString(R.string.qty_and_price));
        } else {
            tvProductTitle.setText(getResources().getString(R.string.count_of_products));
            tvAction.setVisibility(View.INVISIBLE);
        }
        adapter.setMode(mode);
        adapter.setItems(folderItems);
    }

    @Override
    public void setSelectedProduct(Product product) {
        mainPageConnection.addProductToOrder(product.getId());
    }

    @Override
    public void sendCategoryEvent(Category category, String key) {
        mainPageConnection.sendSelectedCategory(category, key);
    }

    @Override
    public void showAvailableDialog(Product product, double available) {
        WarningDialog warningDialog = new WarningDialog(getActivity());
        warningDialog.setWarningMessage(getString(R.string.in_stock_ava)+"        "+decimalFormat.format(available) + " " + product.getMainUnit().getAbbr());
        warningDialog.onlyText(true);
        warningDialog.setDialogTitle(product.getName());
        warningDialog.setOnYesClickListener(view1 -> {
            warningDialog.dismiss();

        });
        warningDialog.setPositiveButtonText(getString(R.string.ok));
        warningDialog.show();
    }

    public void onShow() {
        presenter.updateTitles();
    }
}
