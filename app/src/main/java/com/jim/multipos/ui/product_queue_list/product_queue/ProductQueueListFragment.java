package com.jim.multipos.ui.product_queue_list.product_queue;

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
import com.jim.multipos.ui.product_queue_list.ProductQueueListActivity;
import com.jim.multipos.ui.product_queue_list.adapter.ProductQueueListAdapter;
import com.jim.multipos.ui.product_queue_list.dialogs.ProductQueueFilterDialog;
import com.jim.multipos.ui.product_queue_list.dialogs.StockQueueInfoDialog;
import com.jim.multipos.utils.DateIntervalPicker;
import com.jim.multipos.utils.TextWatcherOnTextChange;
import com.jim.multipos.utils.UIUtils;
import com.jim.multipos.utils.WriteOffProductDialog;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnTextChanged;

import static com.jim.multipos.ui.consignment.ConsignmentActivity.PRODUCT_ID;
import static com.jim.multipos.ui.consignment.ConsignmentActivity.VENDOR_ID;

public class ProductQueueListFragment extends BaseFragment implements ProductQueueListFragmentView {

    @Inject
    ProductQueueListFragmentPresenter presenter;
    @Inject
    DatabaseManager databaseManager;
    @Inject
    DecimalFormat decimalFormat;
    @BindView(R.id.ivSearchImage)
    ImageView ivSearchImage;
    @BindView(R.id.mpSearchEditText)
    MpEditText mpSearchEditText;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDateInterval)
    TextView tvDateInterval;
    @BindView(R.id.llDateInterval)
    LinearLayout llDateInterval;
    @BindView(R.id.llFilter)
    LinearLayout llFilter;
    @BindView(R.id.flClearSearch)
    FrameLayout flClearSearch;
    @BindView(R.id.rvStockQueuesList)
    RecyclerView rvStockQueuesList;
    @BindView(R.id.tvName)
    TextView tvName;
    private ProductQueueListAdapter adapter;
    private int mode;

    @Override
    protected int getLayout() {
        return R.layout.product_queue_list_layout;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        adapter = new ProductQueueListAdapter();
        rvStockQueuesList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvStockQueuesList.setAdapter(adapter);
        if (getArguments() != null) {
            Long productId = (Long) getArguments().get(PRODUCT_ID);
            Long vendorId = (Long) getArguments().get(VENDOR_ID);
            presenter.initData(productId, vendorId);
        }

        llDateInterval.setOnClickListener(v -> presenter.openDateIntervalPicker());

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

        adapter.setListener(new ProductQueueListAdapter.OnStockOperationsListener() {
            @Override
            public void onInfo(StockQueue stockQueue) {
                StockQueueInfoDialog dialog = new StockQueueInfoDialog(getContext(), stockQueue);
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
    }

    @Override
    public void setTitle(String name) {
        tvTitle.setText("Stock positions for: " + name);
    }

    @Override
    public void fillRecyclerView(List<StockQueue> stockQueueList) {
        adapter.setItems(stockQueueList);
    }

    @Override
    public void updateDateIntervalUI(String date) {
        tvDateInterval.setText(date);
    }

    @Override
    public void openDateIntervalPicker(Calendar fromDate, Calendar toDate) {
        DateIntervalPicker dateIntervalPicker = new DateIntervalPicker(getContext(), fromDate, toDate, (fromDate1, toDate1) -> {
            presenter.dateIntervalPicked(fromDate1, toDate1);
        });
        dateIntervalPicker.show();
    }

    @Override
    public void initSearchResults(List<StockQueue> searchResults, String searchText) {
        adapter.setSearchResults(searchResults, searchText);
    }

    @Override
    public void openFilterDialog(int[] filterConfig) {
        ProductQueueFilterDialog dialog = new ProductQueueFilterDialog(getContext(), filterConfig, config -> presenter.onFilterApplied(config));
        dialog.show();
    }


    @Override
    public void openWriteOffDialog(InventoryItem inventoryItem, WriteOffProductDialog.WriteOffCallback writeOffCallback,StockQueue stockQueue) {
        WriteOffProductDialog writeOffProductDialog = new WriteOffProductDialog(getContext(), writeOffCallback, inventoryItem, decimalFormat, databaseManager,stockQueue);
        writeOffProductDialog.show();
    }


    @Override
    public void openReturnInvoice(Long productId, Long vendorId, Long id) {
        ((ProductQueueListActivity) getActivity()).openReturnInvoice(productId, vendorId, id);
    }

    @Override
    public void openWarningDialog(String text) {
        UIUtils.showAlert(getContext(), getString(R.string.ok), getString(R.string.warning), text, () -> {

        });
    }

    @Override
    public void setMode(int mode) {
        this.mode = mode;
        if (mode == ProductQueueListFragmentPresenterImpl.VENDOR){
            tvName.setText(getString(R.string.product_name));
        } else {
            tvName.setText(getString(R.string.vendor_name));
        }
        adapter.setMode(mode);
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
