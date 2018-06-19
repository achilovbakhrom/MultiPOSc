package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.consignment.Invoice;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.inventory.IncomeProduct;
import com.jim.multipos.data.db.model.inventory.StockQueue;

import java.util.List;

import io.reactivex.Single;

public interface InvoiceOperations {
    Single<Invoice> insertInvoiceWithBillingAndIncomeProduct(Invoice invoice, List<IncomeProduct> incomeProductList, List<StockQueue> stockQueueList, List<BillingOperations> billingOperationsList);
    Single<List<Invoice>> getAllInvoices();
}
