package com.jim.multipos.ui.mainpospage.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.core.ClickableBaseAdapter;
import com.jim.multipos.data.db.model.intosystem.FolderItem;
import com.jim.multipos.data.db.model.products.Category;
import com.jim.multipos.data.db.model.products.Product;
import com.jim.multipos.ui.mainpospage.adapter.FolderViewAdapter;
import com.jim.multipos.ui.mainpospage.connection.MainPageConnection;
import com.jim.multipos.ui.mainpospage.presenter.ProductFolderViewPresenterImpl;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.RxBusLocal;
import com.jim.multipos.utils.rxevents.CategoryEvent;
import com.jim.multipos.utils.rxevents.MessageEvent;
import com.jim.multipos.utils.rxevents.ProductEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

import static com.jim.multipos.ui.consignment.view.IncomeConsignmentFragment.INVENTORY_STATE_UPDATE;
import static com.jim.multipos.ui.mainpospage.view.ProductSquareViewFragment.OPEN_PRODUCT;
import static com.jim.multipos.ui.product_last.ProductPresenterImpl.PRODUCT_ADD;
import static com.jim.multipos.ui.product_last.ProductPresenterImpl.PRODUCT_DELETE;
import static com.jim.multipos.ui.product_last.ProductPresenterImpl.PRODUCT_UPDATE;

/**
 * Created by Sirojiddin on 27.10.2017.
 */

public class ProductFolderViewFragment extends BaseFragment implements ProductFolderView {

    @Inject
    ProductFolderViewPresenterImpl presenter;
    @BindView(R.id.rvFolderItems)
    RecyclerView rvFolderItems;
    @BindView(R.id.llBackItem)
    LinearLayout llBackItem;
    @BindView(R.id.tvProductTitle)
    TextView tvProductTitle;
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

    @Override
    protected int getLayout() {
        return R.layout.product_folder_list;
    }


    @Override
    protected void rxConnections() {
        subscriptions = new ArrayList<>();
        subscriptions.add(
                rxBus.toObservable().subscribe(o -> {
                    if (o instanceof MessageEvent) {
                        MessageEvent event = (MessageEvent) o;
                        switch (event.getMessage()) {
                            case PRODUCT_ADD:
                            case PRODUCT_DELETE:
                            case PRODUCT_UPDATE: {
                                presenter.setFolderItemsRecyclerView();
                                break;
                            }
                            case INVENTORY_STATE_UPDATE:
                                presenter.updateProducts();
                                break;
                        }
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.removeListners(subscriptions);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        presenter.setFolderItemsRecyclerView();
        if (mode == PRODUCT) {
            tvProductTitle.setText(getResources().getString(R.string.qty_and_price));
        } else tvProductTitle.setText(getResources().getString(R.string.count_of_products));
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
        adapter.setOnItemClickListener(new ClickableBaseAdapter.OnItemClickListener<FolderItem>() {
            @Override
            public void onItemClicked(int position) {
            }

            @Override
            public void onItemClicked(FolderItem item) {
                presenter.selectedItem(item);
            }
        });
    }

    @Override
    public void refreshProductList(List<FolderItem> folderItems, int mode) {
        this.mode = mode;
        if (mode == PRODUCT) {
            tvProductTitle.setText(getResources().getString(R.string.qty_and_price));
        } else tvProductTitle.setText(getResources().getString(R.string.count_of_products));
        adapter.setMode(mode);
        adapter.setItems(folderItems);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setSelectedProduct(Product product) {
        rxBusLocal.send(new ProductEvent(product, OPEN_PRODUCT));
    }

    @Override
    public void sendCategoryEvent(Category category, String key) {
        mainPageConnection.sendSelectedCategory(category, key);
    }
}
