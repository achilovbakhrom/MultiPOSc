package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.stock.Stock;
import java.util.List;
import io.reactivex.Observable;

/**
 * Created by user on 17.08.17.
 */

public interface StockOperations {
    Observable<Long> addStock(Stock stock);
    Observable<Boolean> addStocks(List<Stock> stocks);
    Observable<List<Stock>> getAllStocks();
    Observable<Boolean> removeStock(Stock stock);
    Observable<Boolean> removeAllStocks();
}
