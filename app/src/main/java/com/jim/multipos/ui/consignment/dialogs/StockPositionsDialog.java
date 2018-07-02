package com.jim.multipos.ui.consignment.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.mpviews.MpButton;
import com.jim.mpviews.MpEditText;
import com.jim.multipos.R;
import com.jim.multipos.config.common.BaseAppModule;
import com.jim.multipos.data.DatabaseManager;
import com.jim.multipos.data.db.model.intosystem.StockQueueItem;
import com.jim.multipos.data.db.model.inventory.DetialCount;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.ui.consignment.adapter.ProductStockQueuesAdapter;
import com.jim.multipos.utils.TextWatcherOnTextChange;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class StockPositionsDialog extends Dialog {

    private OutcomeProduct outcomeProduct;
    private final List<OutcomeProduct> outcomeProductList;
    private final List<OutcomeProduct> exceptionList;
    private final DatabaseManager databaseManager;
    private List<StockQueueItem> stockQueueItems, searchResults;
    private ProductStockQueuesAdapter adapter;
    private OnStockPositionsChanged listener;
    private boolean customSelected = false;
    private int selectedPosition = -1;
    private StockQueueItem singleSelectStockQueue;

    @BindView(R.id.rvStockQueues)
    RecyclerView rvStockQueues;
    @BindView(R.id.btnConfirm)
    MpButton btnConfirm;
    @BindView(R.id.btnBack)
    MpButton btnBack;
    @BindView(R.id.etSearchView)
    MpEditText etSearchView;
    @BindView(R.id.etProductCount)
    MpEditText etProductCount;
    @BindView(R.id.tvProductUnit)
    TextView tvProductUnit;
    @BindView(R.id.flClearSearch)
    FrameLayout flClearSearch;
    @BindView(R.id.ivSearchImage)
    ImageView ivSearchImage;

    public StockPositionsDialog(Context context, OutcomeProduct outcomeProduct, List<OutcomeProduct> outcomeProductList, List<OutcomeProduct> exceptionList, DatabaseManager databaseManager) {
        super(context);
        this.outcomeProduct = outcomeProduct;
        this.outcomeProductList = outcomeProductList;
        this.exceptionList = exceptionList;
        this.databaseManager = databaseManager;
        stockQueueItems = new ArrayList<>();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.stock_positions, null, false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        ButterKnife.bind(this, dialogView);
        setContentView(dialogView);
        adapter = new ProductStockQueuesAdapter();
        stockQueueItems = databaseManager.getStockQueueItemForOutcomeProduct(outcomeProduct, outcomeProductList, exceptionList).blockingGet();
        DecimalFormat decimalFormat = BaseAppModule.getFormatterGrouping();
        rvStockQueues.setLayoutManager(new LinearLayoutManager(getContext()));
        rvStockQueues.setAdapter(adapter);
        etProductCount.clearFocus();
        etSearchView.clearFocus();
        tvProductUnit.setText(outcomeProduct.getProduct().getMainUnit().getAbbr());
        calculateStockPositions();
        etProductCount.setText(decimalFormat.format(outcomeProduct.getSumCountValue()));
        flClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearchView.setText("");
            }
        });
        etSearchView.addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = etSearchView.getText().toString();
                if (searchText.isEmpty()) {
                    searchResults = null;
                    adapter.setData(stockQueueItems);
                } else {
                    searchResults = new ArrayList<>();
                    for (int i = 0; i < stockQueueItems.size(); i++) {
                        if (String.valueOf(stockQueueItems.get(i).getStockQueue().getIncomeProduct().getInvoiceId()).toUpperCase().contains(searchText.toUpperCase())) {
                            searchResults.add(stockQueueItems.get(i));
                            continue;
                        }
                        if (stockQueueItems.get(i).getStockQueue().getVendor().getName().toUpperCase().contains(searchText.toUpperCase())) {
                            searchResults.add(stockQueueItems.get(i));
                            continue;
                        }
                        if (stockQueueItems.get(i).getStockQueue().getStockId() != null)
                            if (stockQueueItems.get(i).getStockQueue().getStockId().toUpperCase().contains(searchText.toUpperCase())) {
                                searchResults.add(stockQueueItems.get(i));
                                continue;
                            }
                    }
                    adapter.setSearchResult(searchResults, searchText);
                }
            }
        });

        etProductCount.addTextChangedListener(new TextWatcherOnTextChange() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() != 0) {
                    double changedCount = 0;
                    try {
                        double availableCount = databaseManager.getAvailableCountForProduct(outcomeProduct.getProduct().getId()).blockingGet();
                        changedCount = decimalFormat.parse(etProductCount.getText().toString().replace(",", ".")).doubleValue();

                        for (int j = 0; j < outcomeProductList.size(); j++) {
                            availableCount -= outcomeProductList.get(j).getSumCountValue();
                        }
                        customSelected = false;
                        if (changedCount > availableCount) {
                            etProductCount.setError("Count can/'t be bigger available products count");
                        } else {
                            if (outcomeProduct.getCustomPickSock()) {
                                if (changedCount > singleSelectStockQueue.getAvailable()) {
                                    etProductCount.setError("Count can/'t be bigger custom picked stock queue products count");
                                } else {
                                    customSelected = true;
                                    outcomeProduct.setSumCountValue(changedCount);
                                }
                            } else {
                                outcomeProduct.setSumCountValue(changedCount);
                            }
                        }
                    } catch (Exception e) {
                        etProductCount.setError(getContext().getString(R.string.invalid));
                        return;
                    }
                } else {
                    etProductCount.setError(getContext().getString(R.string.please_enter_product_count));
                    outcomeProduct.setSumCountValue(0.0d);
                }
                calculateStockPositions();
            }
        });

        adapter.setListener((stockQueueItem, position) -> {
            selectedPosition = position;
            if (stockQueueItem.getAvailable() <= outcomeProduct.getSumCountValue()) {
                etProductCount.setText(decimalFormat.format(stockQueueItem.getAvailable()));
                singleSelectStockQueue = new StockQueueItem();
                singleSelectStockQueue.setAvailable(stockQueueItem.getAvailable());
                singleSelectStockQueue.setStockQueue(stockQueueItem.getStockQueue());
                List<DetialCount> detialCounts = new ArrayList<>();
                DetialCount detialCount = new DetialCount();
                detialCount.setCost(stockQueueItem.getStockQueue().getCost());
                detialCount.setOutcomeProduct(outcomeProduct);
                detialCount.setStockQueue(stockQueueItem.getStockQueue());
                detialCount.setStockId(stockQueueItem.getStockQueue().getId());
                detialCount.setCount(stockQueueItem.getAvailable());
                detialCounts.add(detialCount);
                singleSelectStockQueue.setDetialCounts(detialCounts);
                for (int i = 0; i < stockQueueItems.size(); i++) {
                    if (i == position) {
                        stockQueueItems.get(i).setDetialCounts(detialCounts);
                    } else {
                        stockQueueItems.get(i).getDetialCounts().clear();
                    }
                }
                adapter.setData(stockQueueItems);
            } else {
                etProductCount.setText(decimalFormat.format(outcomeProduct.getSumCountValue()));
                singleSelectStockQueue = new StockQueueItem();
                singleSelectStockQueue.setAvailable(stockQueueItem.getAvailable());
                singleSelectStockQueue.setStockQueue(stockQueueItem.getStockQueue());
                List<DetialCount> detialCounts = new ArrayList<>();
                DetialCount detialCount = new DetialCount();
                detialCount.setCost(stockQueueItem.getStockQueue().getCost());
                detialCount.setOutcomeProduct(outcomeProduct);
                detialCount.setStockQueue(stockQueueItem.getStockQueue());
                detialCount.setStockId(stockQueueItem.getStockQueue().getId());
                detialCount.setCount(outcomeProduct.getSumCountValue());
                detialCounts.add(detialCount);
                singleSelectStockQueue.setDetialCounts(detialCounts);
                for (int i = 0; i < stockQueueItems.size(); i++) {
                    if (i == position) {
                        stockQueueItems.get(i).setDetialCounts(detialCounts);
                    } else {
                        stockQueueItems.get(i).getDetialCounts().clear();
                    }
                }
                adapter.setData(stockQueueItems);
            }
            customSelected = true;
        });

        btnBack.setOnClickListener(v -> dismiss());

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etProductCount.getText().toString().isEmpty()) {
                    if (singleSelectStockQueue != null && customSelected) {
                        if (selectedPosition != 0) {
                            outcomeProduct.setPickedStockQueueId(singleSelectStockQueue.getStockQueue().getId());
                            outcomeProduct.setStockQueue(singleSelectStockQueue.getStockQueue());
                            outcomeProduct.setCustomPickSock(true);
                        } else {
                            outcomeProduct.setCustomPickSock(false);
                        }
                    }
                    listener.onConfirm(outcomeProduct);
                    dismiss();
                }
            }
        });
    }

    @OnTextChanged(R.id.mpSearchEditText)
    protected void handleTextChange(Editable editable) {
        if(editable.toString().isEmpty()){
            ivSearchImage.setImageResource(R.drawable.search_app);
        }else {
            ivSearchImage.setImageResource(R.drawable.cancel_search);
        }
    }

    private void calculateStockPositions() {
        double sumCount = outcomeProduct.getSumCountValue();
        for (int i = 0; i < stockQueueItems.size(); i++) {
            StockQueueItem stockQueueItem = stockQueueItems.get(i);
            if (outcomeProduct.getCustomPickSock()) {
                if (stockQueueItem.getStockQueue().getId().equals(outcomeProduct.getStockQueue().getId())) {
                    List<DetialCount> detialCounts = new ArrayList<>();
                    DetialCount detialCount = new DetialCount();
                    detialCount.setCost(stockQueueItem.getStockQueue().getCost());
                    detialCount.setOutcomeProduct(outcomeProduct);
                    detialCount.setStockQueue(stockQueueItem.getStockQueue());
                    detialCount.setStockId(stockQueueItem.getStockQueue().getId());
                    detialCount.setCount(outcomeProduct.getSumCountValue());
                    detialCounts.add(detialCount);
                    stockQueueItem.setDetialCounts(detialCounts);
                    singleSelectStockQueue = new StockQueueItem();
                    singleSelectStockQueue.setAvailable(stockQueueItem.getAvailable());
                    singleSelectStockQueue.setStockQueue(stockQueueItem.getStockQueue());
                    singleSelectStockQueue.setDetialCounts(detialCounts);
                } else {
                    List<DetialCount> detialCounts = new ArrayList<>();
                    stockQueueItem.setDetialCounts(detialCounts);
                }
            } else {
                List<DetialCount> detialCounts = new ArrayList<>();
                if (sumCount != 0) {
                    if (sumCount > stockQueueItem.getAvailable()) {
                        DetialCount detialCount = new DetialCount();
                        detialCount.setCost(stockQueueItem.getStockQueue().getCost());
                        double detailCount = stockQueueItem.getAvailable();
                        detialCount.setOutcomeProduct(outcomeProduct);
                        detialCount.setStockQueue(stockQueueItem.getStockQueue());
                        detialCount.setStockId(stockQueueItem.getStockQueue().getId());
                        detialCount.setCount(detailCount);
                        detialCounts.add(detialCount);
                        sumCount -= detailCount;
                    } else if (sumCount < stockQueueItem.getAvailable()) {
                        DetialCount detialCount = new DetialCount();
                        detialCount.setCost(stockQueueItem.getStockQueue().getCost());
                        detialCount.setOutcomeProduct(outcomeProduct);
                        detialCount.setStockQueue(stockQueueItem.getStockQueue());
                        detialCount.setStockId(stockQueueItem.getStockQueue().getId());
                        detialCount.setCount(sumCount);
                        detialCounts.add(detialCount);
                        sumCount = 0;
                    } else {
                        DetialCount detialCount = new DetialCount();
                        detialCount.setCost(stockQueueItem.getStockQueue().getCost());
                        detialCount.setOutcomeProduct(outcomeProduct);
                        detialCount.setStockQueue(stockQueueItem.getStockQueue());
                        detialCount.setStockId(stockQueueItem.getStockQueue().getId());
                        detialCount.setCount(sumCount);
                        detialCounts.add(detialCount);
                        sumCount = 0;
                    }
                }
                stockQueueItem.setDetialCounts(detialCounts);
            }
        }
        adapter.setData(stockQueueItems);
    }

    public void setListener(OnStockPositionsChanged listener) {
        this.listener = listener;
    }

    public interface OnStockPositionsChanged {
        void onConfirm(OutcomeProduct outcomeProduct);
    }
}
