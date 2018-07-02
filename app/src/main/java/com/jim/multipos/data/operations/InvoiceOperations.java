package com.jim.multipos.data.operations;

import com.jim.multipos.data.db.model.consignment.Invoice;
import com.jim.multipos.data.db.model.consignment.Outvoice;
import com.jim.multipos.data.db.model.inventory.BillingOperations;
import com.jim.multipos.data.db.model.inventory.IncomeProduct;
import com.jim.multipos.data.db.model.inventory.OutcomeProduct;
import com.jim.multipos.data.db.model.inventory.StockQueue;
import com.jim.multipos.ui.consignment_list.model.InvoiceListItem;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Single;

public interface InvoiceOperations {
    Single<Invoice> insertInvoiceWithBillingAndIncomeProduct(Invoice invoice, List<IncomeProduct> incomeProductList, List<StockQueue> stockQueueList, List<BillingOperations> billingOperationsList);
    Single<List<Invoice>> getAllInvoices();
    Single<Outvoice> insertOutvoiceWithBillingAndOutcomeProducts(Outvoice outvoice, List<OutcomeProduct> outcomeProducts, BillingOperations operationDebt);
    Single<Boolean> isOutvoiceNumberExists(String number);
    Single<List<InvoiceListItem>> getInvoiceListItemByVendorId(Long vendorId);
    Single<List<InvoiceListItem>> getInvoiceListItemsInIntervalByVendor(Long vendorId, Calendar fromDate, Calendar toDate);
}
