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
import com.jim.multipos.ui.mainpospage.presenter.ProductFolderViewPresenterImpl;
import com.jim.multipos.utils.RxBus;
import com.jim.multipos.utils.rxevents.CategoryEvent;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

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
    private int mode = 0;
    private static final int PRODUCT = 2;
    private FolderViewAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.product_folder_list;
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
        adapter = new FolderViewAdapter(folderItems, mode);
        rvFolderItems.setAdapter(adapter);
        adapter.setOnItemClickListener(new ClickableBaseAdapter.OnItemClickListener<FolderItem>() {
            @Override
            public void onItemClicked(int position) {
                presenter.setSelectedItem(position);
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
        Toast.makeText(getContext(), product.getName(), Toast.LENGTH_SHORT).show();
        //TODO do smth with product
    }

    @Override
    public void sendCategoryEvent(Category category, String key) {
        rxBus.send(new CategoryEvent(category, key));
    }
}
