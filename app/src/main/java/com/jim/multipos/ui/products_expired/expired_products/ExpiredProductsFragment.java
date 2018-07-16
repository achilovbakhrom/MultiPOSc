package com.jim.multipos.ui.products_expired.expired_products;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.core.BaseFragment;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.ui.inventory.model.InventoryItem;
import com.jim.multipos.ui.products_expired.ExpiredProductsActivity;
import com.jim.multipos.ui.products_expired.adapter.ExpiredProductQueueListAdapter;
import com.jim.multipos.ui.products_expired.dialogs.ExpiredProductQueueFilterDialog;
import com.jim.multipos.ui.products_expired.dialogs.ExpiredProductQueueInfoDialog;
import com.jim.multipos.utils.TextWatcherOnTextChange;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.WriteOffProductDialog;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnTextChanged;

public class ExpiredProductsFragment extends BaseFragment implements ExpiredProductsFragmentView {

    @BindView(R.id.ivSearchImage)
    ImageView ivSearchImage;
    @BindView(R.id.mpSearchEditText)
    MpEditText mpSearchEditText;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDateInterval)
    TextView tvDateInterval;
    @BindView(R.id.llFilter)
    LinearLayout llFilter;
    @BindView(R.id.flClearSearch)
    FrameLayout flClearSearch;
    @BindView(R.id.rvExpiredStockQueue)
    RecyclerView rvExpiredStockQueue;

    @Inject
    ExpiredProductsFragmentPresenter presenter;
    @Inject
    DatabaseManager databaseManager;
    @Inject
    DecimalFormat decimalFormat;
    private ExpiredProductQueueListAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.expired_products;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        adapter = new ExpiredProductQueueListAdapter(getContext());
        rvExpiredStockQueue.setLayoutManager(new LinearLayoutManager(getContext()));
        rvExpiredStockQueue.setAdapter(adapter);

        llFilter.setOnClickListener(v -> presenter.onFilterClicked());

        flClearSearch.setOnClickListener(v -> {
            if (!mpSearchEditText.getText().toString().isEmpty())
                mpSearchEditText.setText("");
        });

        mpSearchEditText.addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.search(mpSearchEditText.getText().toString());
            }
        });

        adapter.setListener(new ExpiredProductQueueListAdapter.OnStockOperationsListener() {
            @Override
            public void onInfo(StockQueue stockQueue) {
                ExpiredProductQueueInfoDialog dialog = new ExpiredProductQueueInfoDialog(getContext(), stockQueue);
                dialog.show();
            }

            @Override
            public void onWriteOff(StockQueue stockQueue, int position) {
                presenter.onWriteOff(stockQueue);
            }

            @Override
            public void onOutvoice(StockQueue stockQueue, int position) {
                presenter.onOutVoice(stockQueue);
            }
        });


        presenter.onCreateView(savedInstanceState);
    }

    @Override
    public void setTime(String date) {
        tvDateInterval.setText("Today: " + date);
    }

    @Override
    public void setExpiredProducts(List<StockQueue> stockQueueList) {
        adapter.setItems(stockQueueList);
    }

    @Override
    public void initSearchResults(List<StockQueue> searchResults, String searchText) {
        adapter.setSearchResults(searchResults, searchText);
    }

    @Override
    public void openFilterDialog(int[] filterConfig) {
        ExpiredProductQueueFilterDialog dialog = new ExpiredProductQueueFilterDialog(getContext(), filterConfig, config -> presenter.onFilterApplied(config));
        dialog.show();
    }

    @Override
    public void openWriteOffDialog(InventoryItem inventoryItem, WriteOffProductDialog.WriteOffCallback writeOffCallback,StockQueue stockQueue) {
        WriteOffProductDialog writeOffProductDialog = new WriteOffProductDialog(getContext(), writeOffCallback, inventoryItem, decimalFormat, databaseManager,stockQueue);
        writeOffProductDialog.show();
    }

    @Override
    public void openReturnInvoice(Long productId, Long vendorId, Long stockQueueId) {
        ((ExpiredProductsActivity) getActivity()).openReturnInvoice(productId, vendorId, stockQueueId);
    }

    @Override
    public void openWarningDialog(String text) {
        UIUtils.showAlert(getContext(), getString(R.string.ok), getString(R.string.warning), text, () -> {

        });
    }

    @OnTextChanged(R.id.mpSearchEditText)
    protected void handleTextChange(Editable editable) {
        if (editable.toString().isEmpty()) {
            ivSearchImage.setImageResource(R.drawable.search_app);
        } else {
            ivSearchImage.setImageResource(R.drawable.cancel_search);
        }
    }
}
